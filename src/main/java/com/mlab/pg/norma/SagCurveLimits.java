package com.mlab.pg.norma;

public class SagCurveLimits extends VerticalCurveLimits {

	public SagCurveLimits(DesignSpeed designSpeed) {
		super(designSpeed);
		if (designSpeed == DesignSpeed.DS40) {
			this.minKvAdelantamiento = 2400.0;
			this.minKvParada = 760.0;
			
		} else if (designSpeed == DesignSpeed.DS60) {
			this.minKvAdelantamiento = 3600.0;
			this.minKvParada = 1650.0;

		} else if (designSpeed == DesignSpeed.DS80) {
			this.minKvAdelantamiento = 5400.0;
			this.minKvParada = 3000.0;

		} else if (designSpeed == DesignSpeed.DS100) {
			this.minKvAdelantamiento = 7800.0;
			this.minKvParada = 4800.0;

		} else if (designSpeed == DesignSpeed.DS120) {
			this.minKvAdelantamiento = Double.NaN;
			this.minKvParada = 7100.0;

		}
	}

	public SagCurveLimits(double minlength, double maxslope, double minkvadelantamiento, double minkvparada) {
		super(minlength, maxslope);
		this.minKvAdelantamiento = minkvadelantamiento;
		this.minKvParada = minkvparada;
	}
}
