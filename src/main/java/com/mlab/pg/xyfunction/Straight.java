package com.mlab.pg.xyfunction;

import com.mlab.pg.util.MathUtil;

/**
 * Es un Polinom2 en el que el coeficiente a2 es cero:
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
	 * @param x0 abscisa delpunto
	 * @param y0 ordenada del punto
	 * @param pdte pendiente de la recta
	 */
	public Straight(double x0, double y0, double pdte) {
		super(y0-pdte*x0, pdte, 0.0);
	}
	
	public Straight(double[] firstPoint, double[] lastPoint) {
		super(MathUtil.rectaPorDosPuntos(firstPoint, lastPoint)[0], MathUtil.rectaPorDosPuntos(firstPoint, lastPoint)[1], 0.0);
	}
	public double getSlope() {
		return a1;
	}
	
	@Override
	public Straight clone() throws CloneNotSupportedException {
		return new Straight(a0, a1);
	}
}
