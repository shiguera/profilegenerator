package com.mlab.pg.random;

import java.util.Random;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.norma.GradeLimits;
import com.mlab.pg.valign.GradeAlign;
import com.mlab.pg.xyfunction.Straight;

public class RandomFactory {

	
	public static GradeAlign randomGradeAlign(DesignSpeed dspeed, double s0, double z0) {
		double slope = RandomFactory.randomGradeSlope(dspeed);
		double length = RandomFactory.randomGradeLength(dspeed);
		Straight r = new Straight(s0, z0, slope);
		double ends =s0 + length;
		GradeAlign align = new GradeAlign(dspeed, r, s0, ends);
		return align;
	}
	
	/**
	 * Calcula una pendiente aleatoria positiva o negativa para una Grade de una determinada
	 * DesignSpeed
	 * @param dspeed Velocidad de proyecto de la carretera
	 * @return Pendiente positiva o negativa aleatoria entre los valores 
	 * máximo y mínimo de esa categoría de carreteras a intervalos de 0.005%
	 */
	public static double randomGradeSlope(DesignSpeed dspeed) {
		GradeLimits limits = new GradeLimits(dspeed);
		double min = limits.getMinSlope();
		double max = limits.getMaxSlope();
		double inc = limits.getSlopeIncrements();
		double slope = Math.rint(RandomFactory.randomDoubleByIncrements(min, max, inc)*1000)/1000;
		double sign = RandomFactory.randomSign();
		return sign*slope;
	}
	/**
	 * Calcula una longitud aleatoria para una Grade de una determinada
	 * DesignSpeed
	 * @param dspeed Velocidad de proyecto de la carretera
	 * @return Longitud aleatoria entre los valores 
	 * máximo y mínimo de esa categoría de carreteras a intervalos de la longitud
	 * mímima para la velocidad de proyecto
	 */
	public static double randomGradeLength(DesignSpeed dspeed) {
		GradeLimits limits = new GradeLimits(dspeed);
		double min = limits.getMinLength();
		double max = limits.getMaxLength();
		double inc = limits.getMinLength();
		double length = Math.rint(RandomFactory.randomDoubleByIncrements(min, max, inc));
		return length;
	}
	
	/**
	 * Calcula un signo positivo o negativo aleatoriamente. 
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
