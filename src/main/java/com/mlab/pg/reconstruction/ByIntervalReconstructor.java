package com.mlab.pg.reconstruction;

import com.mlab.pg.xyfunction.XYVectorFunction;

public class ByIntervalReconstructor {

	
	
	protected XYVectorFunction originalGradePoints;
	protected double startZ;
	protected InterpolationStrategy interpolationStrategy;
	
	/**
	 * Reconstruye un perfil con distinto valor de los parámetros 
	 * baseSize y thresholdSlope en diferentes tramos
	 */
	public ByIntervalReconstructor(XYVectorFunction originalGradePoints, double startZ, InterpolationStrategy strategy) {
		this.originalGradePoints = originalGradePoints;
		this.startZ = startZ;
		this.interpolationStrategy = strategy;
	}
	
	public void reconstruct() {
		IterativeReconstructor itrec = new IterativeReconstructor(originalGradePoints, startZ, interpolationStrategy);
		itrec.processIterative();
		
	}

}
