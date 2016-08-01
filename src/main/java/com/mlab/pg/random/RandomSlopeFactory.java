package com.mlab.pg.random;

import java.util.Date;
import java.util.Random;

/**
 * Genera penientes aleatorias para carreteras
 * @author shiguera
 *
 */
public class RandomSlopeFactory {

	private static Random rnd;
	
	static {
		rnd = new Random(new Date().getTime());
		
	}
	/**
	 * Devuelve una pendiente aleatoria distribuida uniformemente en el intervalo [min, max]
	 * redondeada a 3 cifras decimales y tomando valores exactos a incrementos 'increment'.
	 * @param min valor mínimo que puede adoptar la pendiente
	 * @param max valor máximo que puede adoptar la pendiente
	 * @param increment incremento entre valores sucesivos de la pendiente
	 * @return
	 */
	public static double uniformSlope(double min, double max, double increment) {
		double slope = Math.rint(RandomFactory.randomDoubleByIncrements(min, max, increment)*1000.0) / 1000.0;
		return slope;
	}
	
	/**
	 * Genera una pendiente aleatoria con distribución normal alrededor de una media mean,
	 *  con una desviación típica standarDeviation, redondeada a 3 decimales (décimas de tanto por ciento)
	 * @param mean Media de la distribución
	 * @param standarDeviation Desviación típica de la distribución.
	 * @return
	 */
	public static double gaussianSlope(double mean, double standarDeviation) {
		double x = rnd.nextGaussian();
		double z = Math.rint((mean + standarDeviation*x)*1000.0) / 1000.0;
		return z;
	}
}
