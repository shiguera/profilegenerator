package com.mlab.pg.valign;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.xyfunction.Straight;

/**
 * Representa una alineación vertical en rampa o pendiente
 * 
 * @author shiguera
 *
 */
public class GradeAlign extends AbstractVerticalProfileAlign {

	
	/**
	 * Crea una GradeAlign a partir de una DesignSpeed, una ecuación de una recta, Straight, 
	 * un inicio, startS, y un final , endS
	 * @param dspeed Velocidad de diseño
	 * @param straight Ecuación de la recta
	 * @param starts Inicio de la alineación
	 * @param ends Final de la alineación
	 */
	public GradeAlign(DesignSpeed dspeed,Straight straight, double starts, double ends) {
		super(dspeed, straight, starts, ends);
	}

	/**
	 * Crea una GradeAlign a partir de una DesignSpeed, abscisa inicial, ordenada inicial, pendiente y longitud
	 * No comprueba que la pendiente supere los límites para esa DesignSpeed
	 */
	public GradeAlign(DesignSpeed dspeed, double starts, double startz, double slope, double length) {
		this(dspeed, new Straight(starts, startz, slope), starts, starts + length);
	}
	
	@Override
	public Straight getPolynom2() {
		return (Straight)super.getPolynom2();
	}
	
	@Override
	public double getSlope() {
		return ((Straight)polynom).getSlope();
	}
	/**
	 * En el caso de las alineaciones en rampa o pendiente 
	 * no tiene sentido este parámetro. Devuelve Double.NaN
	 */
	@Override
	public double getKv() {
		return Double.NaN;
	}


	

}
