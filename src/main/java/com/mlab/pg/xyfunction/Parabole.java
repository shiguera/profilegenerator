package com.mlab.pg.xyfunction;

/**
 * Es un Polinom2 que es parábola, esto es, a0,a1 y a2 son distintos de cero
 * a0 + a1*x + a2*x^2
 * @author shiguera
 *
 */
public class Parabole extends Polynom2{

	public Parabole(double a0, double a1x, double a2x2) {
		super(a0, a1x, a2x2);
	}
	public double getKv() {
		if(a2 == 0.0) {
			return Double.NaN;
		}
		return 0.5 / a2;
	}
	public boolean isConcave() {
		return (a2 > 0);
	}
	public boolean isConvex() {
		return (a2 < 0);
	}
	
	
	
	

}
