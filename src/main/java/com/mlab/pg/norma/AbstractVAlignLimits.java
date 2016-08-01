package com.mlab.pg.norma;

/**
 * Implementa el interface VAlignLimits. Ofrece un constructor
 * en función de la DesignSpeed, que establece valores por defecto 
 * para los límites de los valores de la alineación.
 * Los valores están adaptados a la norma de fecha 2016.
 * @author shiguera
 *
 */
public abstract class AbstractVAlignLimits implements VAlignLimits {

	protected double minLength = 50.0;
	protected double maxLength = 3000.0;
	protected double maxSlope;
	protected double MIN_SLOPE = 0.005;
	protected double SLOPE_INCREMENTS = 0.005;
	protected double KV_INCREMENTS = 250.0;
	
	protected AbstractVAlignLimits(DesignSpeed designSpeed) {
		if (designSpeed == DesignSpeed.DS40) {
			this.minLength = Math.round(40.0/.36);
			this.maxSlope = 0.1;	
		} else if (designSpeed == DesignSpeed.DS60) {
			this.minLength = Math.round(60.0/.36);
			this.maxSlope = 0.08;
		} else if (designSpeed == DesignSpeed.DS80) {
			this.minLength = Math.round(80.0 / .36);
			this.maxSlope = 0.07;
		} else if (designSpeed == DesignSpeed.DS100) {
			this.minLength = Math.round(100.0 / .36);
			this.maxSlope = 0.05;
		} else if (designSpeed == DesignSpeed.DS120) {
			this.minLength = Math.round(120.0 /.36);
			this.maxSlope = 0.05;
		}
	}
	protected AbstractVAlignLimits(double minlength, double maxslope) {
		this.minLength = minlength;
		this.maxSlope = maxslope;
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
	public void setSlopeIncrements(double slopeIncrements) {
		this.SLOPE_INCREMENTS = slopeIncrements;	
	}
	@Override
	public double getKvIncrements() {
		return KV_INCREMENTS;
	}
	@Override
	public void setKvIncrements(double kvIncrements) {
		this.KV_INCREMENTS = kvIncrements;
	}
}
