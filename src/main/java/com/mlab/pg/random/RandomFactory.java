package com.mlab.pg.random;

import java.util.Random;

public class RandomFactory {

	/**
	 * Genera un número double aleatorio comprendido entre un valor máximo 
	 * y uno mínimo, ambos incluidos [min, max] y ajustado a intervalos exactos
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
		double range = max - min;
		int maxsteeps = (int) (range / increment);
		Random rnd = new Random();
		int steeps = rnd.nextInt(maxsteeps+1);
		double randomnumber = min + (double)steeps * increment;
		return randomnumber;
	}
}
