package com.mlab.pg.norma;

public abstract class VerticalCurveLimits extends AbstractVAlignLimits {


	protected double minKvAdelantamiento;
	protected double minKvParada;
	// TODO Establecer maxKvsegún categoría de carretera en las clases CrestCurveLimits y SagCurveLimits
	protected double maxKv = 45000;
	
	protected VerticalCurveLimits(DesignSpeed designSpeed) {
		super(designSpeed);
		
	}

	public double getMinKvAdelantamiento() {
		return minKvAdelantamiento;
	}
	public double getMinKvParada() {
		return minKvParada;
	}

	public double getMaxKv() {
		return maxKv;
	}
	public double getMinKv() {
		if(minKvAdelantamiento<minKvParada) {
			return minKvAdelantamiento;
		} else {
			return minKvParada;
		}
	}
	
}
