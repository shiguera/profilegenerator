package com.mlab.pg.random;

import java.util.Date;
import java.util.Random;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.norma.GradeLimits;
import com.mlab.pg.valign.Grade;
import com.mlab.pg.xyfunction.Straight;

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
	 * Calcula una pendiente aleatoria positiva o negativa para una Grade 
	 * de una determinada DesignSpeed
	 * @param dspeed Velocidad de proyecto de la carretera
	 * @return Pendiente positiva o negativa aleatoria entre los valores 
	 * máximo y mínimo de esa categoría de carreteras a intervalos de 0.005%
	 */
	public static double randomUniformGradeSlope(DesignSpeed dspeed) {
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
	public static double randomUniformGradeLength(DesignSpeed dspeed) {
		GradeLimits limits = new GradeLimits(dspeed);
		double min = limits.getMinLength();
		double max = limits.getMaxLength();
		double inc = 50.0; // limits.getMinLength();
		double length = Math.rint(RandomFactory.randomDoubleByIncrements(min, max, inc));
		return length;
	}
	/**
	 * Calcula una longitud aleatoria para una Grade entre unos valores
	 * máximo y mínimo, redondeada al metro exacto
	 */
	public static double randomUniformGradeLength(double min, double max) {
		double length = Math.rint(RandomFactory.randomDoubleByIncrements(min, max, 1.0));
		return length;
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

	/**
	 * Genera una alineación grade aleatoria para una velocidad de proyecto
	 * y un punto inicial. La pendiente será aleatoria entre el minSlope
	 * y el maxSlope de la categoría. La pendiente puede ser positiva o negativa.
	 * La longitud será aleatoria entre el minLength y el maxLength de la 
	 * categoría.
	 * @param dspeed Velocidad de proyecto
	 * @param s0 Abscisa inicial de la alineación
	 * @param z0 Altitud inicial de la alineación
	 * @return GradeAlign resultado
	 */
	public static Grade randomGradeAlign(DesignSpeed dspeed, double s0, double z0) {
		double slope = randomUniformGradeSlope(dspeed);
		double length = randomUniformGradeLength(dspeed);
		Straight r = new Straight(s0, z0, slope);
		double ends =s0 + length;
		Grade align = new Grade(dspeed, r, s0, ends);
		return align;
	}
	/**
	 * Genera una alineación up-grade aleatoria para una velocidad de proyecto
	 * y un punto inicial. La pendiente será aleatoria entre el minSlope
	 * y el maxSlope de la categoría. La pendiente será positiva.
	 * La longitud será aleatoria entre el minLength y el maxLength de la 
	 * categoría.
	 * @param dspeed Velocidad de proyecto
	 * @param s0 Abscisa inicial de la alineación
	 * @param z0 Altitud inicial de la alineación
	 * @return GradeAlign resultado
	 */
	public static Grade randomUpGradeAlign(DesignSpeed dspeed, double s0, double z0) {
		double slope = Math.abs(randomUniformGradeSlope(dspeed));
		double length = randomUniformGradeLength(dspeed);
		Straight r = new Straight(s0, z0, slope);
		double ends =s0 + length;
		Grade align = new Grade(dspeed, r, s0, ends);
		return align;
	}
	/**
	 * Genera una alineación down-grade aleatoria para una velocidad de proyecto
	 * y un punto inicial. La pendiente será aleatoria entre el minSlope
	 * y el maxSlope de la categoría. La pendiente será negativa.
	 * La longitud será aleatoria entre el minLength y el maxLength de la 
	 * categoría.
	 * @param dspeed Velocidad de proyecto
	 * @param s0 Abscisa inicial de la alineación
	 * @param z0 Altitud inicial de la alineación
	 * @return GradeAlign resultado
	 */
	public static Grade randomDownGradeAlign(DesignSpeed dspeed, double s0, double z0) {
		double slope = -Math.abs(randomUniformGradeSlope(dspeed));
		double length = randomUniformGradeLength(dspeed);
		Straight r = new Straight(s0, z0, slope);
		double ends =s0 + length;
		Grade align = new Grade(dspeed, r, s0, ends);
		return align;
	}



}
