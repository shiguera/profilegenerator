package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class IterativeReconstructor {
	
	Logger LOG = Logger.getLogger(getClass());

	double MIN_ALIGNMENT_LENGTH = 1.0;
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
		rec = new Reconstructor(originalGradePoints, basesize , thresholdslope, startZ, interpolationStrategy);
		VerticalGradeProfile gradeProfile = rec.getGradeProfile();
		XYVectorFunction resultGradePoints = gradeProfile.getSample(startX, endX, separacionMedia, true);
		double ecm = originalGradePoints.ecm(resultGradePoints);
		System.out.println("BaseSize: " + basesize);
		System.out.println("ThresholdSlope: " + thresholdslope);
		System.out.println("ECM: " + ecm);
		
		VerticalProfile resultVProfile = rec.getVerticalProfile();
		//System.out.println(vp);		
		System.out.println("Número de alineaciones:" + resultVProfile.size());
		double sumaErrorAbsoluto = 0.0;
		double maxErrorAbsoluto = 0.0;
		double sumaErrorAbsolutoAlCuadrado = 0.0;
		XYVectorFunction resultVProfilePoints = resultVProfile.getSample(resultVProfile.getStartS(), resultVProfile.getEndS(), separacionMedia, true);
		int pointsCount = resultVProfilePoints.size();
		for(int i=0; i<pointsCount; i++) {
			double x = resultVProfilePoints.getX(i);
			double errorAbsoluto = Math.abs(resultVProfilePoints.getY(i) - integralProfile.getY(x));
			if(Double.isNaN(errorAbsoluto)) {
				continue;
			}
			sumaErrorAbsoluto = sumaErrorAbsoluto + errorAbsoluto;
			sumaErrorAbsolutoAlCuadrado = sumaErrorAbsolutoAlCuadrado + errorAbsoluto * errorAbsoluto;
			if(errorAbsoluto > maxErrorAbsoluto) {
				maxErrorAbsoluto = errorAbsoluto;
			}
		}
		double media = sumaErrorAbsoluto / pointsCount;
		double varianza = sumaErrorAbsolutoAlCuadrado / pointsCount - media*media;
		System.out.println("Error absoluto medio: " + media);
		System.out.println("Error absoluto máximo: " + maxErrorAbsoluto);
		System.out.println("Varianza en el error absoluto: " + varianza);

	}
	
	public void processIterative() throws Exception {
		int maxBaseSize = (int)Math.rint(MIN_ALIGNMENT_LENGTH / separacionMedia);
		if(maxBaseSize < 3) {
			maxBaseSize = 5;
			// LOG.error("ERROR: maxBaseSize < 3");
			// throw new Exception();
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
				rec = new Reconstructor(originalGradePoints, i, thresholdSlopes[j], startZ, interpolationStrategy);
				VerticalGradeProfile gradeProfile = rec.getGradeProfile();
				if(gradeProfile == null || gradeProfile.size()<2) {
					Log.warn("gradeProfile null");
					continue;
				}
				XYVectorFunction resultGradePoints = gradeProfile.getSample(originalGradePoints.getStartX(), originalGradePoints.getEndX(),
						separacionMedia, true);
				XYVectorFunction resultVProfilePoints = rec.getVerticalProfile().getSample(originalGradePoints.getStartX(), originalGradePoints.getEndX(),
						separacionMedia, true);
				
				//double ecm = integralProfile.errorAbsolutoMedio(resultVProfilePoints);
				double ecm = integralProfile.ecm(resultVProfilePoints);
				System.out.println("ecm: " + ecm);
				//double ecm = originalGradePoints.ecm(resultGradePoints);
				//double ecm = originalGradePoints.errorAbsolutoMedio(resultGradePoints);
				
				//System.out.println(i + " - " + thresholdSlopes[j] + " - " + ecm);
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
		
		rec = new Reconstructor(originalGradePoints, (int)results[bestTest][0] , results[bestTest][1], startZ, interpolationStrategy);
		//LOG.debug("Reconstructor: " + rec);
		//VerticalGradeProfile gradeProfile = rec.getGradeProfile();
		VerticalProfile vp = rec.getVerticalProfile();
		//System.out.println(vp);
		

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
