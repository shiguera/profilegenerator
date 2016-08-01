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
	/** 
	 * Crea una Parabole a partir de las coordenadas 
	 * de los puntos inicial y final y de la tangente en 
	 * el punto inicial.
	 * 
	 * @param x0 abscisa inicial
	 * @param y0 ordenada inicial
	 * @param t0 tangente inicial
	 * @param xf abscisa final
	 * @param yf ordenada final
	 */
	public Parabole(double x0, double y0, 
			double t0, double xf, double yf) {
		super(0.0,0.0,0.0);
		a2 = (yf-y0-t0*(xf-x0))/(x0*x0-2.0*x0*xf+xf*xf);
		a1 = t0 - 2.0*a2 * x0;
		a0 = yf - a1*xf - a2*xf*xf;
	}

	/**
	 * Crea una Parabole conocidos el punto inicial, la tangente de entrada y la Kv
	 * @param s0 Coordenada s del punto inicial
	 * @param z0 Coordenada z del punto inicial
	 * @param g0 Pendiente en el punto (s0,z0)
	 * @param kv Parámetro de la parábola
	 */
	public Parabole(double s0, double z0, double g0, double kv) {
		super(0.0,0.0,0.0);
		a2 = 1.0/2.0/kv;
		a1 = g0 - s0/kv;
		a0 = z0 - a1*s0 - s0*s0/2/kv;
	}

	
	public boolean isConcave() {
		return (a2 > 0);
	}
	public boolean isConvex() {
		return (a2 < 0);
	}
	
	
	
	

}
