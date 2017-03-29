package com.mlab.pg.reconstruction;

import com.mlab.pg.xyfunction.IntegerInterval;

public class ParameterInterval extends IntegerInterval{

	protected int baseSize;
	protected double thresholdSlope;
	
	public ParameterInterval(int start, int end, int basesize, double thresholdslope)  {
		super(start, end);
		this.baseSize = basesize;
		this.thresholdSlope = thresholdslope;
	}

	// Getters
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
