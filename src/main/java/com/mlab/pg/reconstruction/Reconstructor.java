package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import com.mlab.pg.reconstruction.strategy.EndingsWithBeginnersAdjuster;
import com.mlab.pg.reconstruction.strategy.EndingsWithBeginnersAdjuster_EqualArea;
import com.mlab.pg.reconstruction.strategy.EndingsWithBeginnersAdjuster_LessSquares;
import com.mlab.pg.reconstruction.strategy.GradeProfileCreator;
import com.mlab.pg.reconstruction.strategy.GradeProfileCreator_EqualArea;
import com.mlab.pg.reconstruction.strategy.GradeProfileCreator_LessSquares;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategy;
import com.mlab.pg.util.MathUtil;
import com.mlab.pg.valign.GradeProfileAlignment;
import com.mlab.pg.valign.VAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

/**
 * Genera un GradeProfile y un VerticalProfile a partir de un XYVectorFunction con los puntos originales {si, gi} 
 * Para ello utiliza un SegmentMaker que genera una Segmentation que solo tiene segmentos
 * del tipo Grade y VerticalCurve.
 * Ofrece una pareja de métodos getGradeProfile() que devuelve el GradeProfile reconstruido y 
 * getVerticalProfile() que devuelve el perfil longitudinal VerticalProfile correspondiente
 * a la integración del GradeProfile
 * 
 * @author shiguera
 *
 */
public class Reconstructor {

	Logger LOG = Logger.getLogger(Reconstructor.class);
	/**
	 * Longitud mínima utilizada en TypeIntervalArrayGenerator para filtrar
	 * tramos menores de esa longitud. Si el anterior o el siguiente son del
	 * mismo tipo, TypeIntervalArrayGenerator los une
	 */
	double MIN_LENGTH = 15.0;
	double[] thresholdSlopes = new double[] {1.0e-4, 1.75e-5, 1.5e-5, 1.25e-5, 1.0e-5, 1.75e-6, 1.5e-6, 1.25e-6, 1.0e-6, 1.75e-7, 1.5e-7, 1.25e-7, 1.0e-7}; 
	//double[] thresholdSlopes = new double[] {1.0e-4}; 

	
	protected XYVectorFunction originalGradePoints;
	protected XYVectorFunction originalVerticalProfilePoints;
	protected XYVectorFunction integralVerticalProfilePoints;
	protected int baseSize;
	protected double thresholdSlope;
	protected double startZ;
	
	protected InterpolationStrategy interpolationStrategy;
	protected TypeIntervalArrayGenerator typeIntervalArrayGenerator;
	protected TypeIntervalArray typeIntervalArray;
	
	protected VerticalGradeProfile gradeProfile;
	protected VerticalProfile resultVerticalProfile;
	protected EndingsWithBeginnersAdjuster adjuster;
	protected GradeProfileCreator gradeProfileCreator;
	
	int bestTest;
	double[][] results;
	
	double startX, endX;
	protected double separacionMedia;
	protected double trackLength;
	protected int alignmentCount;
	protected double meanError;
	protected double maxError;
	protected double ecm;
	protected double varianza;
	
	public Reconstructor(XYVectorFunction originalgradePoints, double startz, InterpolationStrategy strategy) {
		originalGradePoints = originalgradePoints.clone();
		startZ = startz;
		interpolationStrategy = strategy;
		
		startX = originalGradePoints.getStartX();
		endX = originalGradePoints.getEndX();
		separacionMedia = originalGradePoints.separacionMedia();
		integralVerticalProfilePoints = originalGradePoints.integrate(startz);

	}
	
	public void processUnique(ReconstructionParameters parameters) {
		processUnique(parameters.getBaseSize(), parameters.getThresholdSlope());		
	}
	public void processUnique(int basesize, double thresholdslope) {
		this.baseSize = basesize;
		this.thresholdSlope = thresholdslope;
		separacionMedia = originalGradePoints.separacionMedia();
		trackLength = originalGradePoints.getLast()[0] - originalGradePoints.getX(0);
		
		originalVerticalProfilePoints = originalGradePoints.integrate(startZ);
		
		typeIntervalArrayGenerator = new TypeIntervalArrayGenerator(originalGradePoints, baseSize, thresholdslope, interpolationStrategy, MIN_LENGTH);
		typeIntervalArray = typeIntervalArrayGenerator.getResultTypeIntervalArray();
		
		createGradeProfile();
		
		boolean changes = true;
		VerticalGradeProfile process = new VerticalGradeProfile();
		process.addAll(gradeProfile);
		
		while(changes) {
			changes=false;
			process = adjustEndingsWithBeginnings(process);
			resultVerticalProfile = process.integrate(startZ, thresholdSlope);
			
			for(int i=1; i<resultVerticalProfile.size(); i++) {
				VAlignment current = resultVerticalProfile.get(i); 
				VAlignment previous = resultVerticalProfile.get(i-1);	
				double previousA2 = previous.getPolynom2().getA2();
				double currentA2 = current.getPolynom2().getA2();
				double previousK = previous.getPolynom2().getKv();
				double currentK = current.getPolynom2().getKv();
				double s1 = process.get(i-1).getStartS();
				double g1 = process.get(i-1).getStartZ();
				double s2 = process.get(i-1).getEndS();
				double g21 = process.get(i-1).getEndZ();
				double g22 = process.get(i).getStartZ();
				double s3 = process.get(i).getEndS();
				double g3 = process.get(i).getEndZ();
				double area = 0.5*(g1+g21)*(s2-s1) + 0.5*(g22+g3)*(s3-s2);
				if(currentA2==0 && previousA2==0) {
					double newg = area / (s3-s1);
					Straight r = new Straight(newg, 0.0);
					GradeProfileAlignment align = new GradeProfileAlignment(r, s1, s3);
					process.remove(i-1);
					process.set(i-1, align);
					changes = true;
					break;
//				} else if (isSimilarKv(previousK, currentK)) {
//					double newg = 2*area/(s3-s1) - g1;
//					Straight r = new Straight(newg, 0.0);
//					GradeProfileAlignment align = new GradeProfileAlignment(r, s1, s3);
//					process.remove(i-1);
//					process.set(i-1, align);
//					changes = true;
//					break;
				}
			}
		}
		gradeProfile = new VerticalGradeProfile();
		gradeProfile.addAll(process);
		resultVerticalProfile = gradeProfile.integrate(startZ, thresholdSlope);
		
		//System.out.println(gradeProfile);
		alignmentCount = countGradeAlignments();
		calculateErrors();
		
		
		//System.out.println(verticalProfile);

	}
	public void processIterative() {
		int maxBaseSize = (int)Math.rint(MIN_LENGTH / separacionMedia);
		if(maxBaseSize < 3) {
			maxBaseSize = 10;
		}
		int maxbysize = originalGradePoints.size() / 2;
		if(!MathUtil.isEven(originalGradePoints.size())) {
			maxbysize = maxbysize + 1;
		}
		if(maxbysize < maxBaseSize) {
			maxBaseSize = maxbysize;
		}
		if(maxBaseSize<3) {
			System.out.println("Aquí");
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
				processUnique( i, thresholdSlopes[j]);
				VerticalGradeProfile gradeProfile = getGradeProfile();
				if(gradeProfile == null || gradeProfile.size()<1) {
					Log.warn("gradeProfile null");
					//continue;
				}
				double ecm = getEcm();
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
		
		processUnique((int)results[bestTest][0] , results[bestTest][1]);

	}
	
	protected void calculateErrors() {
		double sumaErrorAbsoluto = 0.0;
		maxError = 0.0;
		double sumaErrorAbsolutoAlCuadrado = 0.0;
		XYVectorFunction resultVProfilePoints = resultVerticalProfile.getSample(resultVerticalProfile.getStartS(), resultVerticalProfile.getEndS(), separacionMedia, true);
		for(int i=0; i<getPointsCount(); i++) {
			double x = resultVProfilePoints.getX(i);
			double errorAbsoluto = Math.abs(resultVProfilePoints.getY(x) - integralVerticalProfilePoints.getY(x));
			if(Double.isNaN(errorAbsoluto)) {
				continue;
			}
			sumaErrorAbsoluto = sumaErrorAbsoluto + errorAbsoluto;
			sumaErrorAbsolutoAlCuadrado = sumaErrorAbsolutoAlCuadrado + errorAbsoluto * errorAbsoluto;
			if(errorAbsoluto > maxError) {
				maxError = errorAbsoluto;
			}
		}
		meanError = sumaErrorAbsoluto / getPointsCount();
		varianza = sumaErrorAbsolutoAlCuadrado / getPointsCount() - meanError*meanError;
		ecm = resultVProfilePoints.ecm(integralVerticalProfilePoints);
	}
	private boolean isSimilarKv(double k1, double k2) {
		if(Double.isNaN(k1) || Double.isNaN(k2) || !sameSign(k1,k2)) {
			return false;
		}
		double dif = Math.abs(k1-k2);
		double perc = Math.abs(dif/k1);
		if(perc<0.03) {
			return true;
		} else {
			return false;
		}
	}
	private boolean sameSign(double k1, double k2) {
		if(k1*k2 > 0) {
			return true;
		} else {
			return false;
		}
	}
	private int countGradeAlignments() {
		int count = 0;
		for(int i=0; i<gradeProfile.size(); i++) {
			GradeProfileAlignment align = gradeProfile.get(i);
			double slope = align.getSlope();
			if(slope==0.0) {
				count++;
			}
		}
		return count;
	}
	private void createGradeProfile() {
		if(interpolationStrategy == InterpolationStrategy.EqualArea) {
			gradeProfileCreator = new GradeProfileCreator_EqualArea(thresholdSlope);
		} else {
			gradeProfileCreator = new GradeProfileCreator_LessSquares();	
		}
		gradeProfile = gradeProfileCreator.createGradeProfile(originalGradePoints, typeIntervalArray);		
	}
	private VerticalGradeProfile adjustEndingsWithBeginnings(VerticalGradeProfile profile) {
		if(interpolationStrategy == InterpolationStrategy.EqualArea) {
			adjuster = new EndingsWithBeginnersAdjuster_EqualArea(originalGradePoints, thresholdSlope);
		} else {
			adjuster = new EndingsWithBeginnersAdjuster_LessSquares();
		}
		VerticalGradeProfile result = adjuster.adjustEndingsWithBeginnings(profile);
		return result;
	}
	
	
	// Getters
	public VerticalProfile getVerticalProfile() {
		return resultVerticalProfile;
	}
	
	public XYVectorFunction getOriginalGradePoints() {
		return originalGradePoints;
	}
	public XYVectorFunction getOriginalVerticalProfilePoints() {
		return originalVerticalProfilePoints;
	}
	
	public TypeIntervalArray getTypeIntervalArray() {
		return typeIntervalArray;
	}
	public VerticalGradeProfile getGradeProfile() {
		return gradeProfile;
	}

	public double getSeparacionMedia() {
		return separacionMedia;
	}
	public double getTrackLength() {
		return trackLength;
	}
	public double getMeanError() {
		return meanError;
	}
	public double getVarianza() {
		return varianza;
	}
	public double getEcm() {
		return ecm;
	}
	public double getMaxError() {
		return maxError;
	}
	public int getPointsCount() {
		return originalGradePoints.size();
	}
	public int getBaseSize() {
		return baseSize;
	}
	public double getThresholdSlope() {
		return thresholdSlope;
	}
	public int getAlignmentCount() {
		return resultVerticalProfile.size();
	}

	public int getBestTest() {
		return bestTest;
	}

	public double[][] getResults() {
		return results;
	}
}
