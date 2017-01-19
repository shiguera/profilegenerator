package com.mlab.pg.random;

import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;

public class RandomFactory {

	private static Logger LOG = Logger.getLogger(RandomFactory.class);

	static Random random;
	
	static {
		random = new Random(new Date().getTime());
	}
	
	
	

	
	/**
	 * Calcula una longitud aleatoria entre unos valores
	 * máximo y mínimo, redondeada a incrementosincrement
	 */
	public static double randomUniformLength(double min, double max, double increment) {
		double length = Math.rint(RandomFactory.randomDoubleByIncrements(min, max, increment)*10.0)/10.0;
		//System.out.println(length);
		return length;
	}
	// Funciones utilitarias
	/**
	 * Calcula un signo positivo o negativo aleatoriamente. 
	 * Nota.- La función Random.nextInt(n) devuelve un entero distribuido uniformemente
	 * en el intervalo [0, n) 
	 * @return Devuelve un double que es o +1.0 o -1.0 para utilizar
	 * como signo a aplicar.
	 */
	public static double randomSign() {
		Random rnd = new Random();
		int result = rnd.nextInt(2);
		if(result == 0) {
			return -1.0;
		} else {
			return 1.0;
		}
	}
	/**
	 * Genera un número double aleatorio distribuido uniformemente, y 
	 * comprendido entre un valor máximo y uno mínimo, ambos incluidos [min, max] 
	 * y ajustado a intervalos exactos
	 * 
	 * @param min Valor mínimo que puede tomar el número double generado
	 * @param max Valor máximo que puede tomar el número double generado
	 * @param increment Incrementos entre el valor máximo y el mínimo a 
	 * los que se ajustará el resultado
	 * 
	 * @return Numero double comprendido en el intervalo [min, max] y 
	 * ajustado a los incrementos increment
	 */
	public static double randomDoubleByIncrements(double min, double max, double increment) {
		if(max<=min) {
			return Double.NaN;
		}
		double range = max - min;
		int maxsteeps = (int) (range / increment);
		Random rnd = new Random();
		int steeps = rnd.nextInt(maxsteeps+1);
		double randomnumber = min + (double)steeps * increment;
		return randomnumber;
	}
}
