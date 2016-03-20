package com.mlab.pg.valign;

import com.mlab.pg.xyfunction.Parabole;

/**
 * Representa una alineación vertical en acuerdo parabólico
 *  
 * @author shiguera
 *
 */
public class VerticalCurveAlign extends AbstractVAlign {

	
	public VerticalCurveAlign(Parabole parabole, double startx, double endx) {
		super(parabole, startx, endx);
	}
	
	@Override
	public Parabole getPolynom2() {
		return (Parabole)super.getPolynom2();
	}
	
	@Override
	public String toString() {
		return String.format("%12.6f %12.6f %12.6f %12.6f %12.6f %12.6f %12.6f %12.6f %12.6f", 
				startX, polynom.getY(startX), polynom.getTangent(startX),
				endX, polynom.getY(endX), polynom.getTangent(endX),
				polynom.getA0(), polynom.getA1(), polynom.getA2());
	}

	/**
	 * En el caso de las vertical curves no tiene sentido
	 * este parámetro. Devuelve Double.NaN
	 */
	@Override
	public double getSlope() {
		return Double.NaN;
	}
	/**
	 * Devuelve el valor del parámetro del acuerdo, mediante
	 * la expresión 1/2/a2
	 */

	@Override
	public double getKv() {
		return 1.0/2/polynom.getA2();
	}

}
