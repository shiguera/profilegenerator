package com.mlab.pg.valign;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.xyfunction.Straight;

/**
 * Representa una alineación vertical en rampa o pendiente
 * 
 * @author shiguera
 *
 */
public class GradeAlign extends AbstractVAlign {

	
	public GradeAlign(DesignSpeed dspeed,Straight straight, double starts, double ends) {
		super(dspeed, straight, starts, ends);
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


	public static String CABECERA = String.format("%12s %12s %12s %12s %12s %12s %12s %12s %12s %12s", 
			"SE","ZE", "PE", "L", "SS", "ZS", "PS", "a0", "a1x", "a2x^2");
	@Override
	public String toString() {
		return String.format("%12.6f %12.6f %12.6f %12.6f %12.6f %12.6f %12.6f %12.6f %12.6f %12.6f", 
				startX, polynom.getY(startX), polynom.getTangent(startX),
				getLength(), endX, polynom.getY(endX), polynom.getTangent(endX),
				polynom.getA0(), polynom.getA1(), polynom.getA2());
	}

}
