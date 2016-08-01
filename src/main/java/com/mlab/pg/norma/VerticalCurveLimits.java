package com.mlab.pg.norma;

/**
 * Valores límites para vertical curves adaptados a la 
 * norma del año 2016
 * 
 * @author shiguera
 *
 */
public abstract class VerticalCurveLimits extends AbstractVAlignLimits {


	protected double minKvAdelantamiento;
	protected double minKvParada;
	// TODO Establecer maxKvsegún categoría de carretera en las clases CrestCurveLimits y SagCurveLimits
	protected double maxKv = 45000;
	
	protected VerticalCurveLimits(DesignSpeed designSpeed) {
		super(designSpeed);
	}
	protected VerticalCurveLimits(double minlength, double maxslope) {
		super(minlength, maxslope);
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
