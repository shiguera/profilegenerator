package com.mlab.pg.reconstruction;

import com.mlab.pg.xyfunction.XYVectorFunction;

public class IterativeReconstructor {

	double MIN_ALIGNMENT_LENGTH = 50.0;
	double[] thresholdSlopes = new double[] {1.0e-4, 1.75e-5, 1.5e-5, 1.25e-5, 1.0e-5, 1.75e-6, 1.5e-6, 1.25e-6, 1.0e-6, 1.75e-7, 1.5e-7, 1.25e-7, 1.0e-7}; 
	XYVectorFunction originalGradePoints;
	double startZ;
	double separacionMedia;
	double[][] results;
	int bestTest;
	
	public IterativeReconstructor(XYVectorFunction gradepoints, double startz) throws Exception {
		this.originalGradePoints = gradepoints;
		this.startZ = startz;
		this.separacionMedia = originalGradePoints.separacionMedia();
		int maxBaseSize = (int)Math.rint(MIN_ALIGNMENT_LENGTH / separacionMedia);
		if(maxBaseSize < 3) {
			throw new Exception();
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
				Reconstructor rec = new Reconstructor(originalGradePoints, i, thresholdSlopes[j], startZ, 
						new PointCharacteriserStrategy_EqualArea(), new ProcessBorderIntervalsStrategy_EqualArea());
				XYVectorFunction resultGradePoints = rec.getGradeProfile().getSample(originalGradePoints.getStartX(), originalGradePoints.getEndX(),
						separacionMedia, true);
				double ecm = originalGradePoints.ecm(resultGradePoints);
				System.out.println(i + " - " + thresholdSlopes[j] + " - " + ecm);
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

	}

	public int getBestTest() {
		return bestTest;
	}
	public double[][] getResults() {
		return results;
	}
}
