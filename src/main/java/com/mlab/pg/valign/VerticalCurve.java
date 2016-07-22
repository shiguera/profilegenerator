package com.mlab.pg.valign;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.xyfunction.Parabole;

/**
 * Representa una alineación vertical en acuerdo parabólico
 *  
 * @author shiguera
 *
 */
public class VerticalCurve extends AbstractVerticalProfileAlign {

	
	public VerticalCurve(DesignSpeed dspeed, Parabole parabole, double startx, double endx) {
		super(dspeed, parabole, startx, endx);
	}
	public VerticalCurve(DesignSpeed dspeed, double starts, double startz, double startTangent, double kv, double ends) {
		super(dspeed, new Parabole(starts, startz, startTangent, kv), starts, ends);
	}
	@Override
	public Parabole getPolynom2() {
		return (Parabole)(super.getPolynom2());
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
		return 1.0/2.0/polynom.getA2();
	}

}
