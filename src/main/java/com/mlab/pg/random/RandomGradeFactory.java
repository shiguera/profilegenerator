package com.mlab.pg.random;

import java.util.Arrays;
import java.util.Date;
import java.util.Random;

/**
 * Genera penientes aleatorias para carreteras
 * @author shiguera
 *
 */
public class RandomGradeFactory {

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
	public static double randomUniformGradeSlope(double min, double max, double increment) {
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
	public static double randomGaussianGradeSlope(double mean, double standarDeviation) {
		double x = rnd.nextGaussian();
		double z = Math.rint((mean + standarDeviation*x)*1000.0) / 1000.0;
		return z;
	}

	/**
	 * Genera una longitud aleatoria positiva para una grade según una distribución
	 * Normal de media mean y desviación típica sd. Los valores no podrán ser 
	 * menores que el valor mínimo min, ni mayores que el valor max 
	 * @param mean Media de la distribución 
	 * @param sd Desviación típica de la distribución
	 */
	public static double randomGaussianGradeLength(double mean, double sd, double min, double max) {
		double length = Math.rint(mean + rnd.nextGaussian() * sd);
		while(length < 0 || length < min || length > max) {
			length = Math.rint(mean + rnd.nextGaussian() * sd);			
		}
		return length;
	}



	public static double[] generateThreeOrderedSlopes(double minSlope, double maxSlope, double slopeIncrement) {
		double[] slopes = new double[3];
		slopes[0] = RandomGradeFactory.randomUniformGradeSlope(minSlope, maxSlope, slopeIncrement);
		while(true) {
			slopes[1] = RandomGradeFactory.randomUniformGradeSlope(minSlope, maxSlope, slopeIncrement);
			if(slopes[1] != slopes[0]) {
				break;
			}
		}
		while(true) {
			slopes[2] = RandomGradeFactory.randomUniformGradeSlope(minSlope, maxSlope, slopeIncrement);			
			if((slopes[2] != slopes[0]) && (slopes[2] != slopes[1])) {
				break;
			}
		}
		Arrays.sort(slopes);
		return slopes;
	}


}
