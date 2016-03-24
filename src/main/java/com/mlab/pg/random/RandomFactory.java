package com.mlab.pg.random;

import java.util.Random;

import org.apache.log4j.Logger;

import com.mlab.pg.norma.CrestCurveLimits;
import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.norma.GradeLimits;
import com.mlab.pg.norma.SagCurveLimits;
import com.mlab.pg.norma.VerticalCurveLimits;
import com.mlab.pg.valign.GradeAlign;
import com.mlab.pg.valign.VerticalCurveAlign;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.Parabole;
import com.mlab.pg.xyfunction.Straight;

public class RandomFactory {

	private static Logger LOG = Logger.getLogger(RandomFactory.class);
	
	// Perfiles básicos
	public static VerticalProfile randomVerticalProfileType_I(DesignSpeed dspeed, double s0, double z0, int verticalCurvesCount) {
		return null;
	}
	// Alineaciones VerticalCurve
	/**
	 * Calcula una vertical curve que comienza en el punto final del grade1 y tiene una pendiente de 
	 * salida g2. El parámetro es aleatorio entre el mínimo correspondiente a la categoría . 
	 * La pendiente de salida tiene que ser del signo contrario que la pendiente de entrada. 
	 * @param dspeed
	 * @param grade1
	 * @param g2
	 * @return
	 */
	public static VerticalCurveAlign randomVerticalCurve(DesignSpeed dspeed, GradeAlign grade1, double g2) {
		double g1 = grade1.getStartTangent();
		if(g1*g2 > 0) {
			LOG.error("randomVerticalCurve() ERROR: slopes have same sign");
			return null;
		}
		VerticalCurveLimits limits = null;
		if(g1>0) {
			LOG.debug("randomVerticalCurve(): generating crest curve");
			limits = new CrestCurveLimits(dspeed);
		} else {
			LOG.debug("randomVerticalCurve(): generating sag curve");
			limits = new SagCurveLimits(dspeed);
		}
		double kvmin = limits.getMinKv();
		double kvmax = limits.getMaxKv();
		double minlength = limits.getMinLength();
		double maxlength = limits.getMaxLength();
		double lengthIncrement = minlength;
		double theta = g2-g1;
		double kv=0.0;
		double length = 0.0;
		while(Math.abs(kv)<kvmin || Math.abs(kv)>kvmax) {
			length = RandomFactory.randomDoubleByIncrements(minlength, maxlength, lengthIncrement);			
			kv = length / theta;
		}
		double starts = grade1.getEndS();
		double startz = grade1.getEndZ();
		double ends = starts +length;
		Parabole p = new Parabole(starts, startz, g1, kv);
		VerticalCurveAlign align = new VerticalCurveAlign(dspeed, p, starts, ends);
		return align;
	}
	/**
	 * Calcula un parámetro aleatorio para una crest curve correspondiente a 
	 * una velocidad de diseño dada. El signo del parámetro devuelto es negativo
	 * 
	 * @param dspeed
	 * @return
	 */
	public static double randomCrestCurveKv(DesignSpeed dspeed) {
		VerticalCurveLimits limits = new CrestCurveLimits(dspeed);
		double minKv = limits.getMinKv();
		double maxKv= limits.getMaxKv();
		double inc = limits.getKvIncrements();
		return -randomDoubleByIncrements(minKv, maxKv,inc);
	}
	/**
	 * Calcula un parámetro aleatorio para una sag curve correspondiente a 
	 * una velocidad de diseño dada. El signo del parámetro devuelto es positivo
	 * 
	 * @param dspeed
	 * @return
	 */
	public static double randomSagCurveKv(DesignSpeed dspeed) {
		VerticalCurveLimits limits = new SagCurveLimits(dspeed);
		double minKv = limits.getMinKv();
		double maxKv= limits.getMaxKv();
		double inc = limits.getKvIncrements();
		return randomDoubleByIncrements(minKv, maxKv,inc);
	}

	
	
	// Alineaciones Grade
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

	// Funciones utilitarias
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
