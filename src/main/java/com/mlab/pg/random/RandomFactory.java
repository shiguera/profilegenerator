package com.mlab.pg.random;

import java.util.Random;

import org.apache.log4j.Logger;

import com.mlab.pg.norma.CrestCurveLimits;
import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.norma.GradeLimits;
import com.mlab.pg.norma.SagCurveLimits;
import com.mlab.pg.norma.VerticalCurveLimits;
import com.mlab.pg.util.MathUtil;
import com.mlab.pg.valign.Grade;
import com.mlab.pg.valign.VerticalCurve;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.Parabole;
import com.mlab.pg.xyfunction.Straight;

public class RandomFactory {

	private static Logger LOG = Logger.getLogger(RandomFactory.class);
	
	// DesigSpeed
	public static DesignSpeed randomDesignSpeed() {
		int max = DesignSpeed.values().length; 
		Random r = new Random();
		int value = r.nextInt(max);
		DesignSpeed ds = DesignSpeed.values()[value];
		return ds;
	}

	// Perfiles longitudinales
	/**
	 * Genera un perfil longitudinal aleatorio compuesto de una sucesión 
	 * de perfiles básicos tipo I y II. El perfil tendrá vertexCount 
	 * perfiles básicos. El primer perfil básico será aleatoriamente
	 * del tipo I o II.
	 * @param dspeed Velocidad de diseño
	 * @param vertexCount Número de vértices del perfil que se quiere generar
	 * @param s0 Abscisa inicial
	 * @param z0 altura inicial
	 * @return VerticalProfile
	 */
	public static VerticalProfile randomVerticalProfile(DesignSpeed dspeed, double s0, double z0, int vertexCount) {
		double sign = RandomFactory.randomSign();
		if ( sign> 0) {
			return RandomFactory.randomVerticalProfileBeginningOnType_I(dspeed, s0, z0, vertexCount);
		} else {
			return RandomFactory.randomVerticalProfileBeginningOnType_II(dspeed, s0, z0, vertexCount);
		}
	}
	/**
	 * Genera un perfil longitudinal aleatorio compuesto de una sucesión 
	 * de perfiles básicos tipo I y II. El perfil tendrá vertexCount 
	 * perfiles básicos. El primer perfil básico será tipo I
	 * @param dspeed Velocidad de diseño
	 * @param vertexCount Número de vértices del perfil que se quiere generar
	 * @param s0 Abscisa inicial
	 * @param z0 altura inicial
	 * @return VerticalProfile
	 */
	public static VerticalProfile randomVerticalProfileBeginningOnType_I(DesignSpeed dspeed, double s0, double z0, int vertexCount) {
		if(vertexCount < 1) {
			return null;
		}
		VerticalProfile generalProfile = new VerticalProfile(dspeed);
		int count = 0;
		double startS = s0;
		double startZ = z0;
		while (count < vertexCount) {
			VerticalProfile vpI = RandomFactory.randomVerticalProfileType_I(dspeed, startS, startZ);
			generalProfile.add(vpI);
			count++;
			if (count >= vertexCount) {
				break;
			}
			startS = vpI.getEndS();
			startZ = vpI.getLastAlign().getEndZ();
			VerticalProfile vpII = RandomFactory.randomVerticalProfileType_II(dspeed, startS, startZ);
			generalProfile.add(vpII);
			startS = vpII.getEndS();
			startZ = vpII.getLastAlign().getEndZ();
			count++;
		}
		return generalProfile;
	}
	/**
	 * Genera un perfil longitudinal aleatorio compuesto de una sucesión 
	 * de perfiles básicos tipo I y II. El perfil tendrá vertexCount 
	 * perfiles básicos. El primer perfil básico será tipo II
	 * @param dspeed Velocidad de diseño
	 * @param vertexCount Número de vértices del perfil que se quiere generar
	 * @param s0 Abscisa inicial
	 * @param z0 altura inicial
	 * @return VerticalProfile 
	 */
	public static VerticalProfile randomVerticalProfileBeginningOnType_II(DesignSpeed dspeed, double s0, double z0, int vertexCount) {
		if(vertexCount < 1) {
			return null;
		}
		VerticalProfile generalProfile = new VerticalProfile(dspeed);
		int count = 0;
		double startS = s0;
		double startZ = z0;
		while (count < vertexCount) {
			VerticalProfile vpI = RandomFactory.randomVerticalProfileType_II(dspeed, startS, startZ);
			generalProfile.add(vpI);
			count++;
			if (count >= vertexCount) {
				break;
			}
			startS = vpI.getEndS();
			startZ = vpI.getLastAlign().getEndZ();
			VerticalProfile vpII = RandomFactory.randomVerticalProfileType_I(dspeed, startS, startZ);
			generalProfile.add(vpII);
			startS = vpII.getEndS();
			startZ = vpII.getLastAlign().getEndZ();
			count++;
		}
		return generalProfile;
	}
		
	// Perfiles básicos
	/**
	 * Genera unperfil aleatorio tipo I: upgrade-crestcurve-downgrade
	 * @param dspeed Velocidad de diseño
	 * @param s0 Abscisa inicial
	 * @param z0 Altura inicial
	 * @return VerticalProfile
	 */
	public static VerticalProfile randomVerticalProfileType_I(DesignSpeed dspeed, double s0, double z0) {
		Grade grade1 = RandomFactory.randomUpGradeAlign(dspeed, s0, z0);
		VerticalCurve crestcurve = RandomFactory.randomCrestCurve(dspeed, grade1.getEndS(),
				grade1.getEndZ(), grade1.getEndTangent(), true);
		double starts2 = crestcurve.getEndS();
		double startz2 = crestcurve.getEndZ();
		double length2 = RandomFactory.randomGradeLength(dspeed);
		double ends2 = starts2 + length2;
		double g2 = crestcurve.getEndTangent();
		Straight straight2 = new Straight(starts2, startz2, g2);
		Grade grade2 = new Grade(dspeed, straight2, starts2, ends2);
		VerticalProfile profile = new VerticalProfile(dspeed);
		profile.add(grade1);
		profile.add(crestcurve);
		profile.add(grade2);
		return profile;
	}
	/**
	 * Genera un perfil aleatorio tipo II: downgrade - sagcurve - upgrade
	 * @param dspeed Velocidad de diseño
	 * @param s0 Abscisa inicial
	 * @param z0 Altura inicial
	 * @return VerticalProfile
	 */
	public static VerticalProfile randomVerticalProfileType_II(DesignSpeed dspeed, double s0, double z0) {
		Grade grade1 = RandomFactory.randomDownGradeAlign(dspeed, s0, z0);
		VerticalCurve sagcurve = RandomFactory.randomSagCurve(dspeed, grade1.getEndS(), 
				grade1.getEndZ(), grade1.getEndTangent(), true);
		double starts2 = sagcurve.getEndS();
		double startz2 = sagcurve.getEndZ();
		double length2 = RandomFactory.randomGradeLength(dspeed);
		double ends2 = starts2 + length2;
		double g2 = sagcurve.getEndTangent();
		Straight straight2 = new Straight(starts2, startz2, g2);
		Grade grade2 = new Grade(dspeed, straight2, starts2, ends2);
		VerticalProfile profile = new VerticalProfile(dspeed);
		profile.add(grade1);
		profile.add(sagcurve);
		profile.add(grade2);
		return profile;
	}
	
	// Alineaciones VerticalCurve
	/**
	 * Genera una Sag VerticalCurve aleatoria con punto inicial y pendiente inicial 
	 * conocidas para una velocidad de proyecto dada. 
	 * 
	 * @param dspeed Velocidad de proyecto
	 * @param s0 Abscisa inicial
	 * @param z0 Ordenada inicial
	 * @param g0 Pendiente inicial
	 * @return VerticalCurve resultado con la pendiente de salida positiva
	 */
	public static VerticalCurve randomSagCurve(DesignSpeed dspeed, double s0, double z0, double g0, boolean positiveEndSlope) {
		double endg = RandomFactory.randomGradeSlope(dspeed);
		if(positiveEndSlope) {
			endg = Math.abs(endg);
		} 
		VerticalCurveLimits limits =  new CrestCurveLimits(dspeed);
		double kvmin = limits.getMinKv();
		double kvmax = limits.getMaxKv();
		double minlength = limits.getMinLength();
		double maxlength = limits.getMaxLength();
		double lengthIncrement = minlength;
		double theta = endg-g0;
		double kv=0.0;
		double length = 0.0;
		while(Math.abs(kv)<kvmin || Math.abs(kv)>kvmax || length<limits.getMinLength() | length>limits.getMaxLength()) {
			length = RandomFactory.randomDoubleByIncrements(minlength, maxlength, lengthIncrement);			
			kv = length / theta;
		}
		double starts = s0;
		double startz = z0;
		double ends = starts +length;
		Parabole p = new Parabole(starts, startz, g0, kv);
		VerticalCurve align = new VerticalCurve(dspeed, p, starts, ends);
		return align;
	}
	/**
	 * Genera una Crest VerticalCurve aleatoria con punto inicial y pendiente inicial 
	 * conocidas para una velocidad de proyecto dada. 
	 * 
	 * @param dspeed Velocidad de proyecto
	 * @param s0 Abscisa inicial
	 * @param z0 Ordenada inicial
	 * @param g0 Pendiente inicial
	 * @return VerticalCurve resultado con la pendiente de salida positiva
	 */
	public static VerticalCurve randomCrestCurve(DesignSpeed dspeed, double s0, double z0, double g0, boolean negativeEndSlope) {
		double endg = RandomFactory.randomGradeSlope(dspeed);
		if(negativeEndSlope) {
			endg = - Math.abs(endg);
		} 
		VerticalCurveLimits limits =  new CrestCurveLimits(dspeed);
		double kvmin = limits.getMinKv();
		double kvmax = limits.getMaxKv();
		double minlength = limits.getMinLength();
		double maxlength = limits.getMaxLength();
		double lengthIncrement = minlength;
		double theta = endg-g0;
		double kv=0.0;
		double length = 0.0;
		while(Math.abs(kv)<kvmin || Math.abs(kv)>kvmax || length<limits.getMinLength() | length>limits.getMaxLength()) {
			length = RandomFactory.randomDoubleByIncrements(minlength, maxlength, lengthIncrement);			
			kv = length / theta;
		}
		double starts = s0;
		double startz = z0;
		double ends = starts +length;
		Parabole p = new Parabole(starts, startz, g0, kv);
		VerticalCurve align = new VerticalCurve(dspeed, p, starts, ends);
		return align;
	}
	/**
	 * Calcula un parámetro aleatorio para una crest curve correspondiente a 
	 * una velocidad de diseño dada. El signo del parámetro devuelto es negativo
	 * 
	 * @param dspeed Velocidad de diseño
	 * @return double con el Kv negativo generado
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
	 * @param dspeed Velocodad de diseño
	 * @return double con el Kv positivo generado
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
	public static Grade randomGradeAlign(DesignSpeed dspeed, double s0, double z0) {
		double slope = RandomFactory.randomGradeSlope(dspeed);
		double length = RandomFactory.randomGradeLength(dspeed);
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
		double slope = Math.abs(RandomFactory.randomGradeSlope(dspeed));
		double length = RandomFactory.randomGradeLength(dspeed);
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
		double slope = -Math.abs(RandomFactory.randomGradeSlope(dspeed));
		double length = RandomFactory.randomGradeLength(dspeed);
		Straight r = new Straight(s0, z0, slope);
		double ends =s0 + length;
		Grade align = new Grade(dspeed, r, s0, ends);
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
		double inc = 50.0; // limits.getMinLength();
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
