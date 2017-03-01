package com.mlab.pg.reconstruction;

import com.mlab.pg.xyfunction.XYVectorFunction;

public class PointCharacteriserStrategy_LessSquareAproximation implements PointCharacteriserStrategy {

	public PointCharacteriserStrategy_LessSquareAproximation() {

	}

	/**
	 * Calcula la interpolación por mínimos cuadrados de la recta anterior de un 
	 * punto de una XYSample. La recta anterior es la formada por el punto y 
	 * los puntos anteriores 
	 *  
	 * @param function XYSample que incluye los puntos de la recta a calcular
	 * @param pointIndex Índice del punto del que se quiere calcular la recta anterior
 	 * @param straightsize Número de puntos de la recta
 	 * 
	 * @return double[] con la recta en la forma y = a[0] + a[1]x
	 */
	@Override
	public double[] calculaRectaAnterior(XYVectorFunction function, int pointIndex, int straightsize) {
		if(function.size()<pointIndex+1) {
			// Si elíndice del punto es mayor que el del último de la XYVectorFunction
			return null;
		}
		// Determinar el primer punto de la recta de interpolación
		int first = pointIndex - straightsize +1;
		if(first<0) {
			// Si el primer punto de la recta es anterior al primero de 
			// la XYVectorFunction
			return null;
		}
		return function.rectaMinimosCuadrados(first, pointIndex);
	}
	/**
	 * Calcula la interpolación por mínimos cuadrados de la recta posterior de un
	 * punto de una XYVectorFunction. La recta posterior está formada por el punto
	 * en cuestión y una serie de puntos posteriores al mismo
	 * 
	 * @param function XYVectorFunction que incluye los puntos de la recta a calcular
	 * @param pointIndex Índice del punto del que se quiere calcular la recta posterior
	 * @param straightsize Número de puntos de la recta
	 * 
	 * @return double[] con la recta en la forma y = a[0] + a[1]x
	 */
	@Override
	public double[] calculaRectaPosterior(XYVectorFunction function, int pointIndex, int straightsize) {
		int last = pointIndex + straightsize-1;
		if(function.size()<last+1) {
			return null;
		}
		return function.rectaMinimosCuadrados(pointIndex, last);
	}

}
