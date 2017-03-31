package com.mlab.pg.reconstruction;

import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class ReconstructorByIntervals {

	
	
	protected XYVectorFunction originalGradePoints;
	protected double startZ;
	protected InterpolationStrategy interpolationStrategy;
	
	/**
	 * Reconstruye un perfil con distinto valor de los parámetros 
	 * baseSize y thresholdSlope en diferentes tramos
	 */
	public ReconstructorByIntervals(XYVectorFunction originalGradePoints, double startZ, InterpolationStrategy strategy) {
		this.originalGradePoints = originalGradePoints;
		this.startZ = startZ;
		this.interpolationStrategy = strategy;
	}
	
	public void reconstruct() {
		Reconstructor rec = new Reconstructor(originalGradePoints, startZ, interpolationStrategy);
		rec.processIterative();
		int bestBaseSize = rec.getBaseSize();
		double bestThresholdSlope = rec.getThresholdSlope();
		VerticalGradeProfile gprofile = rec.getGradeProfile();
		
	}

}
