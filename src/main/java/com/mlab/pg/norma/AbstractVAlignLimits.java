package com.mlab.pg.norma;

public abstract class AbstractVAlignLimits implements VAlignLimits {

	protected DesignSpeed designSpeed;
	protected double maxLength = 3000.0;
	protected double minLength;
	protected double maxSlope;
	protected double MIN_SLOPE = 0.005;
	protected double SLOPE_INCREMENTS = 0.005;
	protected double KV_INCREMENTS = 250.0;
	
	protected AbstractVAlignLimits(DesignSpeed designSpeed) {
		this.designSpeed = designSpeed;
		if (designSpeed == DesignSpeed.DS40) {
			this.minLength = 33.0;
			this.maxSlope = 0.1;	
		} else if (designSpeed == DesignSpeed.DS60) {
			this.minLength = 50.0;
			this.maxSlope = 0.08;
		} else if (designSpeed == DesignSpeed.DS80) {
			this.minLength = 67.0;
			this.maxSlope = 0.07;
		} else if (designSpeed == DesignSpeed.DS100) {
			this.minLength = 83.0;
			this.maxSlope = 0.05;
		} else if (designSpeed == DesignSpeed.DS120) {
			this.minLength = 100.0;
			this.maxSlope = 0.05;
		}
	}
	@Override
	public DesignSpeed getDesignSpeed() {
		return designSpeed;
	}

	@Override
	public double getMaxLength() {
		return maxLength;
	}

	@Override
	public double getMinLength() {
		return minLength;
	}

	@Override
	public double getMaxSlope() {
		return maxSlope;
	}

	@Override
	public double getMinSlope() {
		return MIN_SLOPE;
	}
	@Override
	public double getSlopeIncrements() {
		return SLOPE_INCREMENTS;
	}
	@Override
	public double getKvIncrements() {
		return KV_INCREMENTS;
	}
}
