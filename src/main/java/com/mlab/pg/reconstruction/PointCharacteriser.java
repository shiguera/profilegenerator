package com.mlab.pg.reconstruction;

import com.mlab.pg.xyfunction.XYVectorFunction;

/**
 * Caracteriza un punto de un perfil de pendientes según uno de los tipos básicos
 * definidos en PointType: Grade, VerticalCurve, BorderPoint, para unos valores
 * determinados del mobileBaseSize y thresholdSlope 
 * 
 * @author shiguera
 *
 */
public class PointCharacteriser {

	public PointCharacteriser() {
		
	}
	
	public PointType characterise(XYVectorFunction gpsample, int index, int mobileBaseSize, double thresholdSlope) {
		double[] r1 = calculaRectaAnterior(gpsample, index, mobileBaseSize);
		double[] r2 = calculaRectaPosterior(gpsample, index, mobileBaseSize);
		if (r1 == null || r2 == null || r1.length <2 || r2.length <2) {
			return PointType.NULL;
		}
		
		if(isHorizontal(r1, thresholdSlope)) {
			if(isHorizontal(r2, thresholdSlope)) {
				return PointType.GRADE;
			} else {
				return PointType.BORDER_POINT;
			}
		} else {
			if(isHorizontal(r2, thresholdSlope)) {
				return PointType.BORDER_POINT;
			} else {
				if(isEqualSlope(r1, r2, thresholdSlope)) {
					return PointType.VERTICAL_CURVE;
				} else {
					return PointType.BORDER_POINT;
				}
			}
		}
	}
	private boolean isHorizontal(double[] r, double threshold) {
		if(Math.abs(r[1])<threshold) {
			return true;
		} else {
			return false;
		}
	}
	private boolean isEqualSlope(double[] r1, double[] r2, double threshold) {
		if(isHorizontal(r1, threshold) && isHorizontal(r2, threshold)) {
			return true;
		}		
		double slope1 = r1[1];
		double slope2 = r2[1];
		if(isEqualSign(slope1, slope2)) {
			if(Math.abs(slope1 - slope2)<threshold) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	private boolean isEqualSign(double d1, double d2) {
		if(d1>=0 && d2>=0) {
			return true;
		}
		if(d1<=0 && d2<=0) {
			return true;
		}
		return false;
	}
	/**
	 * Calcula la interpolación por mínimos cuadrados de la recta anterior de un 
	 * punto de una XYSample
	 *  
	 * @param function XYSample que incluye los puntos de la recta a calcular
	 * @param last Índice del punto del que se quiere calcular la recta anterior
 	 * @param straightsize Número de puntos de la recta
 	 * 
	 * @return double[] con la recta en la forma y = a[0] + a[1]x
	 */
	private static double[] calculaRectaAnterior(XYVectorFunction function, int last, int straightsize) {
		if(function.size()<last+1) {
			return null;
		}
		int first = last - straightsize +1;
		if(first<0) {
			return null;
		}
		return function.rectaMinimosCuadrados(first, last);
	}
	/**
	 * Calcula la interpolación por mínimos cuadrados de la recta posterior de un
	 * punto de una XYVectorFunction
	 * 
	 * @param function XYVectorFunction que incluye los puntos de la recta a calcular
	 * @param first Índice del punto del que se quiere calcular la recta posterior
	 * @param straightsize Número de puntos de la recta
	 * 
	 * @return double[] con la recta en la forma y = a[0] + a[1]x
	 */
	private static double[] calculaRectaPosterior(XYVectorFunction function, int first, int straightsize) {
		int last = first + straightsize-1;
		if(function.size()<last+1) {
			return null;
		}
		return function.rectaMinimosCuadrados(first, last);
	}

	
}
