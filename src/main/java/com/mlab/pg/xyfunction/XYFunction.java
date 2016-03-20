package com.mlab.pg.xyfunction;

/**
 * Representa una función XY y=f(x)
 * Tiene métodos para devolver el valor de la ordenada y,
 * de la tangente y de la curvatura correspondientes a una abcisa x.
 * 
 * 
 * @author shiguera
 *
 */
public interface XYFunction {
	double getY(double x);
	double getTangent(double x);
	double getCurvature(double x);
	
}
