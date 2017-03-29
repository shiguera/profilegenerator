package com.mlab.pg.reconstruction;

public class ReconstructionParameters {

	protected double thresholdSlope;
	protected int baseSize;
	
	public ReconstructionParameters(int baseSize, double thresholdSlope) {
		this.baseSize = baseSize;
		this.thresholdSlope = thresholdSlope;
	}

	// Getters
	public double getThresholdSlope() {
		return thresholdSlope;
	}

	public void setThresholdSlope(double thresholdSlope) {
		this.thresholdSlope = thresholdSlope;
	}

	public int getBaseSize() {
		return baseSize;
	}

	public void setBaseSize(int baseSize) {
		this.baseSize = baseSize;
	}

}
