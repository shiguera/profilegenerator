package com.mlab.pg.reconstruction;

import com.mlab.pg.util.MathUtil;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class PointCharacteriserStrategy_EqualArea implements PointCharacteriserStrategy {

	public PointCharacteriserStrategy_EqualArea() {

	}

	/**
	 * Calcula la recta anterior de forma que el area entre la recta
	 * y el eje de las S sea igual al area que dejan los puntos de la muestra, para 
	 * que el incremento de cota entre los extremos sea el mismo. La recta resultante
	 * se calcula pasandopor el punto analizado
	 *  
	 * @param function XYSample que incluye los puntos de la recta a calcular
	 * @param pointIndex Índice del punto del que se quiere calcular la recta anterior
 	 * @param basesize Número de puntos de la recta
 	 * 
	 * @return double[] con la recta en la forma y = a[0] + a[1]x
	 */
	@Override
	public double[] calculaRectaAnterior(XYVectorFunction function, int pointIndex, int basesize) {
		if(function.size()<pointIndex+1) {
			// Si elíndice del punto es mayor que el del último de la XYVectorFunction
			return null;
		}
		int firstIndex = pointIndex - basesize +1;
		if (firstIndex < 0) {
			return null;
		}
		double[] r = function.rectaAnteriorEqualArea(firstIndex, pointIndex);
		return r;
	}
	/**
	 * Calcula la recta posterior de forma que el area entre la recta
	 * y el eje de las S sea igual al area que dejan los puntos de la muestra, para 
	 * que el incremento de cota entre los extremos sea el mismo. La recta resultante
	 * se calcula pasandopor el punto analizado
	 *  
	 * @param function XYVectorFunction que incluye los puntos de la recta a calcular
	 * @param pointIndex Índice del punto del que se quiere calcular la recta posterior
	 * @param straightsize Número de puntos de la recta
	 * 
	 * @return double[] con la recta en la forma y = a[0] + a[1]x
	 */
	@Override
	public double[] calculaRectaPosterior(XYVectorFunction function, int pointIndex, int basesize) {
		int lastIndex = pointIndex + basesize - 1;
		if (lastIndex > function.size()-1) {
			return null;
		}
		double[] r = function.rectaPosteriorEqualArea(pointIndex, lastIndex);
		return r;
	
	}
//	@Override
//	public double[] calculaRectaPosterior(XYVectorFunction function, int pointIndex, int basesize) {
//		int lastIndex = pointIndex + basesize - 1;
//		if (lastIndex > function.size()-1) {
//			return null;
//		}
//		double area = function.areaEncerrada(pointIndex, lastIndex);
//		double s1 = function.getX(pointIndex);
//		double z1 = function.getY(pointIndex);
//		double s2 = function.getX(lastIndex);
//		double[][] A = new double[][] {{(s2 - s1), (s2*s2 - s1*s2)}, {1, s1}};
//		double[] C = new double[] {2*area - z1 * (s2 - s1), z1};
//		double[] r = MathUtil.solve(A, C);
//		return r;
//	
//	}

}
