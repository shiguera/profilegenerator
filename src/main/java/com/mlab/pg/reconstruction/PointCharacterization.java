package com.mlab.pg.reconstruction;

public class PointCharacterization {

	protected PointType pointType;
	protected int baseSize;
	protected double thresholdSlope;
	
	public PointCharacterization(PointType type, int basesize, double thresholdslope) {
		pointType = type;
		baseSize = basesize;
		thresholdSlope = thresholdslope;
	}


	// Getters
	public PointType getPointType() {
		return pointType;
	}

	public void setPointType(PointType pointType) {
		this.pointType = pointType;
	}

	public int getBaseSize() {
		return baseSize;
	}

	public void setBaseSize(int baseSize) {
		this.baseSize = baseSize;
	}

	public double getThresholdSlope() {
		return thresholdSlope;
	}

	public void setThresholdSlope(double thresholdSlope) {
		this.thresholdSlope = thresholdSlope;
	}
	
	

}
