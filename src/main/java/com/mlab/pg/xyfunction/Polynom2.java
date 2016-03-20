package com.mlab.pg.xyfunction;

/**
 * Representa un polinomio de grado 2 en la forma :<br/>
 * y = a0 + a1*x + a2*x² <br/>
 * Implementa el interface XYFunction.<br/>
 * Tiene tres propiedades doubles con los coeficientes: a0, a1, a2 <br/>
 * Además de los métodos del interface XYFunction, tiene un constructor 
 * que recibe los tres coeficientes como argumentos y getters y setters para los
 * tres coeficientes.<br/>
 * 
 * @author shiguera
 *
 */
public class Polynom2 implements XYFunction {

	protected double a0, a1, a2;;
	
	/**
	 * Crea una instancia de Polynom2 en la forma:<br/>
	 * P(x) = a0 + a1x + a2x^2
	 * 
	 * @param a0
	 * @param a1
	 * @param a2
	 */
	public Polynom2(double a0, double a1, double a2) {
		this.a0 = a0;
		this.a1 = a1;
		this.a2 = a2;
	}
	
	public double getY(double x) {
		return a0+a1*x+a2*x*x;
	}

	public double getTangent(double x) {
		return a1+2*a2*x;
	}

	public double getCurvature(double x) {
		double f1 = a1 + 2* a2 * x;
		double f2 = 2 * a2;
		double chi = f2 / (Math.pow((1+f1*f1), 3.0/2.0));
		return chi;
	}

	public double getA0() {
		return a0;
	}

	public void setA0(double a0) {
		this.a0 = a0;
	}

	public double getA1() {
		return a1;
	}

	public void setA1(double a1) {
		this.a1 = a1;
	}

	public double getA2() {
		return a2;
	}

	public void setA2(double a2) {
		this.a2 = a2;
	}

	@Override
	public String toString() {
		return String.format("%12.6f + %12.6f * x + %12.6f * x^2", a0,a1,a2);
	}
}
