package com.mlab.pg.xyfunction;

/**
 * Es un Polinom2 en el que el coeficiente a2 es cero:<br/>
 * y = a0 + a1 * x 
 * 
 * @author shiguera
 *
 */
public class Straight extends Parabole {

	public Straight(double a0, double a1x) {
		super(a0, a1x, 0.0);
	}
	/**
	 * Recta a partir de un punto y una pendiente
	 * @param x0
	 * @param y0
	 * @param pdte
	 */
	public Straight(double x0, double y0, double pdte) {
		super(y0-pdte*x0, pdte, 0.0);
	}
	public double getSlope() {
		return a1;
	}
}
