package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;

import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
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

	protected XYVectorFunction originalGradePoints;
	protected TypeIntervalArray typeIntervalArray;
	protected VerticalGradeProfile gradeProfile;
	protected VerticalProfile verticalProfile;
	protected TypeIntervalArrayGenerator typeIntervalArrayGenerator;
	protected InterpolationStrategy interpolationStrategy;
	
	protected int baseSize;
	protected double thresholdSlope;
	
	protected EndingsWithBeginnersAdjuster adjuster;
	protected GradeProfileCreator gradeProfileCreator;
	
	public Reconstructor(XYVectorFunction originalGradePoints, int mobilebasesize, double thresholdslope, double startZ, InterpolationStrategy strategy) {
		this.baseSize = mobilebasesize;
		this.thresholdSlope = thresholdslope;
		
		this.originalGradePoints = originalGradePoints.clone();
		
		interpolationStrategy = strategy;
		
		typeIntervalArrayGenerator = new TypeIntervalArrayGenerator(originalGradePoints, mobilebasesize, thresholdslope, interpolationStrategy);
		typeIntervalArray = typeIntervalArrayGenerator.getResultTypeSegmentArray();
		
		createGradeProfile();
		
		adjustEndingsWithBeginnings();
		
		verticalProfile = gradeProfile.integrate(startZ, thresholdSlope);
		
	}
	private void adjustEndingsWithBeginnings() {
		if(interpolationStrategy == InterpolationStrategy.EqualArea) {
			adjuster = new EndingsWithBeginnersAdjuster_EqualArea(originalGradePoints, thresholdSlope);
		} else {
			adjuster = new EndingsWithBeginnersAdjuster_LessSquares();
		}
		adjuster.adjustEndingsWithBeginnings(gradeProfile);
	}
	
	private void createGradeProfile() {
		if(interpolationStrategy == InterpolationStrategy.EqualArea) {
			gradeProfileCreator = new GradeProfileCreator_EqualArea(thresholdSlope);
		} else {
			gradeProfileCreator = new GradeProfileCreator_LessSquares();	
		}
		gradeProfile = gradeProfileCreator.createGradeProfile(originalGradePoints, typeIntervalArray);		
	}
	
	// Getters
	public VerticalProfile getVerticalProfile() {
		return verticalProfile;
	}
	
	public XYVectorFunction getOriginalPoints() {
		return originalGradePoints;
	}
	public TypeIntervalArray getTypeIntervalArray() {
		return typeIntervalArray;
	}
	public VerticalGradeProfile getGradeProfile() {
		return gradeProfile;
	}

}
