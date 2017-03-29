package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class IterativeReconstructor {
	
	Logger LOG = Logger.getLogger(getClass());

	double MIN_ALIGNMENT_LENGTH = 10.0;
	double[] thresholdSlopes = new double[] {1.0e-4, 1.75e-5, 1.5e-5, 1.25e-5, 1.0e-5, 1.75e-6, 1.5e-6, 1.25e-6, 1.0e-6, 1.75e-7, 1.5e-7, 1.25e-7, 1.0e-7}; 
	//double[] thresholdSlopes = new double[] {1.0e-4, 1.5e-5, 1.0e-5, 1.5e-6, 1.0e-6, 1.5e-7, 1.0e-7}; 
	//double[] thresholdSlopes = new double[] {1.0e-4, 1.5e-5, 1.0e-5, 1.5e-6, 1.0e-6 }; 
	//double[] thresholdSlopes = new double[] {1.0e-4}; 
	
	XYVectorFunction originalGradePoints;
	XYVectorFunction integralProfile;
	double startX, endX, startZ;
	double separacionMedia;
	double[][] results;
	int bestTest;
	Reconstructor rec;
	InterpolationStrategy interpolationStrategy;
	
	public IterativeReconstructor(XYVectorFunction gradepoints, double startz, InterpolationStrategy strategy) {
		this.originalGradePoints = gradepoints;
		this.startZ = startz;
		this.interpolationStrategy = strategy;
		startX = originalGradePoints.getStartX();
		endX = originalGradePoints.getEndX();
		this.separacionMedia = originalGradePoints.separacionMedia();
		integralProfile = gradepoints.integrate(startz);
	}
	
	public void processUnique(int basesize, double thresholdslope) {
		rec = new Reconstructor(originalGradePoints, startZ, interpolationStrategy);
		rec.processUnique(basesize, thresholdslope);
		//VerticalGradeProfile gradeProfile = rec.getGradeProfile();
		//XYVectorFunction resultGradePoints = gradeProfile.getSample(startX, endX, separacionMedia, true);
		//double ecm = originalGradePoints.ecm(resultGradePoints);
//		System.out.println("BaseSize: " + basesize);
//		System.out.println("ThresholdSlope: " + thresholdslope);
//		System.out.println("ECM: " + ecm);
		
		//VerticalProfile resultVProfile = rec.getVerticalProfile();
		//System.out.println(vp);		
		results = new double[][] {{basesize, thresholdslope, rec.getEcm()}};

	}
	
	public void processIterative() {
		int maxBaseSize = (int)Math.rint(MIN_ALIGNMENT_LENGTH / separacionMedia);
		if(maxBaseSize < 10) {
			maxBaseSize = 10;
		}
		int numBaseSizes = maxBaseSize - 2;
		int numThresholdSlopes = thresholdSlopes.length;
		int numTests = numBaseSizes * numThresholdSlopes;
		results = new double[numTests][3];
		double ecmMin = -1.0;
		bestTest = 0;
		int contador = 0;
		for (int i=3; i<=maxBaseSize; i++) {
			for (int j=0; j<thresholdSlopes.length; j++) {
				System.out.println("Test: " + contador + " BaseSize: " + i + ", thresholdSlope: " + thresholdSlopes[j]);
				rec = new Reconstructor(originalGradePoints, startZ, interpolationStrategy);
				rec.processUnique(i, thresholdSlopes[j]);
				VerticalGradeProfile gradeProfile = rec.getGradeProfile();
				if(gradeProfile == null || gradeProfile.size()<2) {
					Log.warn("gradeProfile null");
					continue;
				}
				double ecm = rec.getEcm();
				results[contador][0] = i; // baseSize;
				results[contador][1] = thresholdSlopes[j]; // thresholdSlope
				results[contador][2] = ecm; // ecm
				if(!Double.isNaN(ecm)) {
					if(ecmMin == -1.0) {
						ecmMin = ecm;
						bestTest = contador;
					} else {
						if (ecm < ecmMin) {
							ecmMin = ecm;
							bestTest = contador;
						}					
					}
				}
				contador ++;
			}
		}
		System.out.println("BestTest: " + bestTest);
		System.out.println("BaseSize: " + results[bestTest][0]);
		System.out.println("ThresholdSlope: " + results[bestTest][1]);
		System.out.println("ECM: " + results[bestTest][2]);
		
		rec = new Reconstructor(originalGradePoints, startZ, interpolationStrategy);
		rec.processUnique((int)results[bestTest][0] , results[bestTest][1]);
		
	}

	public int getBestTest() {
		return bestTest;
	}
	public double[][] getResults() {
		return results;
	}
	public double getSeparacionMedia() {
		return separacionMedia;
	}
	public Reconstructor getReconstructor() {
		return rec;
	}
}
