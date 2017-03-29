package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;

import com.mlab.pg.reconstruction.strategy.EndingsWithBeginnersAdjuster;
import com.mlab.pg.reconstruction.strategy.EndingsWithBeginnersAdjuster_EqualArea;
import com.mlab.pg.reconstruction.strategy.EndingsWithBeginnersAdjuster_LessSquares;
import com.mlab.pg.reconstruction.strategy.GradeProfileCreator;
import com.mlab.pg.reconstruction.strategy.GradeProfileCreator_EqualArea;
import com.mlab.pg.reconstruction.strategy.GradeProfileCreator_LessSquares;
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
	
	protected XYVectorFunction originalGradePoints;
	protected XYVectorFunction originalVerticalProfilePoints;
	protected int baseSize;
	protected double thresholdSlope;
	protected double startZ;
	protected double separacionMedia;
	protected double trackLength;
	
	protected InterpolationStrategy interpolationStrategy;
	protected TypeIntervalArrayGenerator typeIntervalArrayGenerator;
	protected TypeIntervalArray typeIntervalArray;
	
	protected VerticalGradeProfile gradeProfile;
	protected VerticalProfile resultVerticalProfile;
	protected EndingsWithBeginnersAdjuster adjuster;
	protected GradeProfileCreator gradeProfileCreator;
	
	protected int alignmentCount;
	protected double meanError;
	protected double maxError;
	protected double ecm;
	protected double varianza;
	
	public Reconstructor(XYVectorFunction originalgradePoints, int mobilebasesize, double thresholdslope, double startz, InterpolationStrategy strategy) {
		originalGradePoints = originalgradePoints.clone();
		baseSize = mobilebasesize;
		thresholdSlope = thresholdslope;
		startZ = startz;
		interpolationStrategy = strategy;
		
		separacionMedia = originalGradePoints.separacionMedia();
		trackLength = originalGradePoints.getLast()[0] - originalGradePoints.getX(0);
		
		originalVerticalProfilePoints = originalGradePoints.integrate(startZ);
		
		typeIntervalArrayGenerator = new TypeIntervalArrayGenerator(originalGradePoints, mobilebasesize, thresholdslope, interpolationStrategy, MIN_LENGTH);
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
	
	public void processUnique(ReconstructionParameters parameters) {
		processUnique(parameters.getBaseSize(), parameters.getThresholdSlope());		
	}
	public void processUnique(int basesize, double thresholdslope) {
		this.baseSize = basesize;
		this.thresholdSlope = thresholdslope;
	}
	
	
	private void calculateErrors() {
		double sumaErrorAbsoluto = 0.0;
		maxError = 0.0;
		double sumaErrorAbsolutoAlCuadrado = 0.0;
		XYVectorFunction resultVProfilePoints = resultVerticalProfile.getSample(resultVerticalProfile.getStartS(), resultVerticalProfile.getEndS(), separacionMedia, true);
		for(int i=0; i<getPointsCount(); i++) {
			double x = resultVProfilePoints.getX(i);
			double errorAbsoluto = Math.abs(resultVProfilePoints.getY(x) - originalVerticalProfilePoints.getY(x));
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
		ecm = resultVProfilePoints.ecm(originalVerticalProfilePoints);
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
}
