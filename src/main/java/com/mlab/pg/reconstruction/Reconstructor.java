package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;

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
	protected int baseSize;
	protected double thresholdSlope;
	protected double startZ;
	protected InterpolationStrategy interpolationStrategy;
	
	protected TypeIntervalArrayGenerator typeIntervalArrayGenerator;
	protected TypeIntervalArray typeIntervalArray;
	
	protected VerticalGradeProfile gradeProfile;
	protected VerticalProfile verticalProfile;
	protected EndingsWithBeginnersAdjuster adjuster;
	protected GradeProfileCreator gradeProfileCreator;
	
	public Reconstructor(XYVectorFunction originalgradePoints, int mobilebasesize, double thresholdslope, double startz, InterpolationStrategy strategy) {
		originalGradePoints = originalgradePoints.clone();
		baseSize = mobilebasesize;
		thresholdSlope = thresholdslope;
		startZ = startz;
		interpolationStrategy = strategy;
		
		typeIntervalArrayGenerator = new TypeIntervalArrayGenerator(originalGradePoints, mobilebasesize, thresholdslope, interpolationStrategy, MIN_LENGTH);
		typeIntervalArray = typeIntervalArrayGenerator.getResultTypeIntervalArray();
		
		createGradeProfile();
		int count = countGradeAlignments();
		//System.out.println("After createGradeProfile(): " + count);
		

		boolean changes = true;
		VerticalGradeProfile process = new VerticalGradeProfile();
		process.addAll(gradeProfile);
		
		while(changes) {
			changes=false;
			process = adjustEndingsWithBeginnings(process);
			verticalProfile = process.integrate(startZ, thresholdSlope);
			
			for(int i=1; i<verticalProfile.size(); i++) {
				VAlignment current = verticalProfile.get(i); 
				VAlignment previous = verticalProfile.get(i-1);				
				if(current.getPolynom2().getA2()==0 && previous.getPolynom2().getA2()==0) {
					double s1 = process.get(i-1).getStartS();
					double g1 = process.get(i-1).getStartZ();
					double s2 = process.get(i-1).getEndS();
					double g21 = process.get(i-1).getEndZ();
					double g22 = process.get(i).getStartZ();
					double s3 = process.get(i).getEndS();
					double g3 = process.get(i).getEndZ();
					double area = 0.5*(g1+g21)*(s2-s1) + 0.5*(g22+g3)*(s3-s2);
					double newg = area / (s3-s1);
					Straight r = new Straight(newg, 0.0);
					GradeProfileAlignment align = new GradeProfileAlignment(r, s1, s3);
					process.remove(i-1);
					process.set(i-1, align);
					changes = true;
					break;
				} 
			}
		}
		gradeProfile = new VerticalGradeProfile();
		gradeProfile.addAll(process);
		verticalProfile = gradeProfile.integrate(startZ, thresholdSlope);
		
		//System.out.println(gradeProfile);
		count = countGradeAlignments();
		//System.out.println("After adjustEndingsWithBeginnings(): " + count);
		
		
		//System.out.println(verticalProfile);
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
