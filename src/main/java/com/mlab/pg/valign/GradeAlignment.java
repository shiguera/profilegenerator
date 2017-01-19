package com.mlab.pg.valign;

import com.mlab.pg.xyfunction.Straight;

/**
 * Representa una alineación vertical en rampa o pendiente
 * 
 * @author shiguera
 *
 */
public class GradeAlignment extends AbstractVAlignment {

	
	/**
	 * Crea un Grade a partir de una ecuación de una recta, Straight, 
	 * un inicio, startS, y un final , endS
	 * @param straight Ecuación de la recta
	 * @param starts Inicio de la alineación
	 * @param ends Final de la alineación
	 */
	public GradeAlignment(Straight straight, double starts, double ends) {
		super(straight, starts, ends);
	}

	/**
	 * Crea un Grade a partir de una abscisa inicial, ordenada inicial, pendiente y longitud
	 */
	public GradeAlignment(double starts, double startz, double slope, double length) {
		this(new Straight(starts, startz, slope), starts, starts + length);
	}

	public double getSlope() {
		return getStartTangent();
	}

	

}
