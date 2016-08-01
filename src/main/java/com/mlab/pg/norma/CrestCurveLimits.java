package com.mlab.pg.norma;

public class CrestCurveLimits extends VerticalCurveLimits {

	public CrestCurveLimits(DesignSpeed designSpeed) {
		super(designSpeed);
		if (designSpeed == DesignSpeed.DS40) {
			this.minKvAdelantamiento = 300.0;
			this.minKvParada = 250.0;
			
		} else if (designSpeed == DesignSpeed.DS60) {
			this.minKvAdelantamiento = 1200.0;
			this.minKvParada = 800.0;

		} else if (designSpeed == DesignSpeed.DS80) {
			this.minKvAdelantamiento = 3100.0;
			this.minKvParada = 2300.0;

		} else if (designSpeed == DesignSpeed.DS100) {
			this.minKvAdelantamiento = 7100.0;
			this.minKvParada = 5200.0;

		} else if (designSpeed == DesignSpeed.DS120) {
			this.minKvAdelantamiento = Double.NaN;
			this.minKvParada = 11000.0;

		}
	}

	public CrestCurveLimits(double minlength, double maxslope, double minkvadelantamiento, double minkvparada) {
		super(minlength, maxslope);
		this.minKvAdelantamiento = minkvadelantamiento;
		this.minKvParada = minkvparada;
	}
}
