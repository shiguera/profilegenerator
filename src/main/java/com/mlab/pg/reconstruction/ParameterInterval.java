package com.mlab.pg.reconstruction;

public class ParameterInterval {

	protected double startS;
	protected double endS;
	protected int baseSize;
	protected double thresholdSlope;
	
	public ParameterInterval(double starts, double ends, int basesize, double thresholdslope)  {
		this.startS = starts;
		this.endS = ends;
		this.baseSize = basesize;
		this.thresholdSlope = thresholdslope;
	}

	public boolean contains(double x) {
		if(x>=startS && x<=endS) {
			return true;
		} else {
			return false;
		}
	}
	// Getters
	public ReconstructionParameters getParameters() {
		return new ReconstructionParameters(baseSize, thresholdSlope);
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

	public double getStartS() {
		return startS;
	}

	public void setStartS(double startS) {
		this.startS = startS;
	}

	public double getEndS() {
		return endS;
	}

	public void setEndS(double endS) {
		this.endS = endS;
	}

}
