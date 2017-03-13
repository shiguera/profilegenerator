package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;

import com.mlab.pg.util.MathUtil;
import com.mlab.pg.valign.GradeProfileAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.IntegerInterval;
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

	protected XYVectorFunction originalGradePoints;
	protected TypeIntervalArray typeIntervalArray;
	protected VerticalGradeProfile gradeProfile;
	protected VerticalProfile verticalProfile;
	protected TypeIntervalArrayGenerator typeIntervalArrayGenerator;
	protected InterpolationStrategy interpolationStrategy;
	
	protected int baseSize;
	protected double thresholdSlope;
	
	protected EndingsWithBeginnersAdjuster adjuster;
	
	public Reconstructor(XYVectorFunction originalGradePoints, int mobilebasesize, double thresholdslope, 
			double startZ, InterpolationStrategy strategy) {
		this.baseSize = mobilebasesize;
		this.thresholdSlope = thresholdslope;
		
		this.originalGradePoints = originalGradePoints.clone();
		
		interpolationStrategy = strategy;
		
		typeIntervalArrayGenerator = new TypeIntervalArrayGenerator(originalGradePoints, mobilebasesize, thresholdslope, interpolationStrategy);
		typeIntervalArray = typeIntervalArrayGenerator.getResultTypeSegmentArray();
		
		createGradeProfile();
		
		if(strategy == InterpolationStrategy.EqualArea) {
			adjuster = new EndingsWithBeginnersAdjuster_EqualArea(originalGradePoints, thresholdSlope);
		} else {
			adjuster = new EndingsWithBeginnersAdjuster_LessSquares();
		}
		adjuster.adjustEndingsWithBeginnings(gradeProfile);
		
		verticalProfile = gradeProfile.integrate(startZ);
		
	}
	private void createGradeProfile() {
		gradeProfile = new VerticalGradeProfile();
		for(int i=0; i<typeIntervalArray.size(); i++) {
			int first = typeIntervalArray.get(i).getStart();
			int last = typeIntervalArray.get(i).getEnd();
			IntegerInterval interval = new IntegerInterval(first, last);
			double[] r = originalGradePoints.rectaMinimosCuadrados(interval);
			Straight straight = new Straight(r[0], r[1]);
			GradeProfileAlignment align = new GradeProfileAlignment(straight, originalGradePoints.getX(first), originalGradePoints.getX(last));
			//System.out.println(String.format("%f %f", align.getStartZ(), align.getEndZ()));
			gradeProfile.add(align);
		}
	}
	public VerticalProfile getVerticalProfile() {
		return verticalProfile;
	}
	
	public XYVectorFunction getOriginalPoints() {
		return originalGradePoints;
	}
	public TypeIntervalArray getSegments() {
		return typeIntervalArray;
	}
	public VerticalGradeProfile getGradeProfile() {
		return gradeProfile;
	}

}
