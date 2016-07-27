package com.mlab.pg.norma;

public class NormaUtil {

	/**
	 * Calcula el coeficiente de rozamiento rueda-pavimento mediante la ecuación de 
	 * segundo grado que interpola los valores de la norma 3.1-IC de feb 2016
	 */
	public static double coefRozamiento(double vp) {
		return 0.5288746 - 0.0026515*vp + 0.000005478*vp*vp;
	}
	/**
	 * Calcula la velocidad de parada necesaria segun la norma 3.1-IC de febrero 2016
	 * tomandoun tiempo de percepción de 2 sg.
	 * 
	 * @param vp Velocidad de proyecto de la carretera
	 * 
	 * @return Distancia de parada en metros
	 */
	public static double distanciaParada(double vp, double slope) {
		double fl = coefRozamiento(vp);
		double d = vp*2.0/3.6 + vp*vp / 254.0 / (fl + slope);
		return d;
	}
	
	/**
	 * Distancia de visibilidad para iniciar la prohibición de adelantamiento
	 * según la norma 3.1-Ic feb 2016. El resultado difiere en menos de 2m de los
	 * valores dados en la tabla 3.2 de la norma
	 * @param vp Velocidad de proyecto
	 * 
	 * @return Distancia de visibilidad necesaria en metros
	 */
	public static double distanciaInicioProhibicionAdelantamiento(double vp) {
		return 7.142857142857159 + 0.220238095238095*vp + 0.022023809523809525*vp*vp;
	}

	/**
	 * Distancia de visibilidad para finalizar la prohibición de adelantamiento
	 * según la norma 3.1-Ic feb 2016. El resultado difiere en menos de 6.5 m de los
	 * valores dados en la tabla 3.2 de la norma
	 * @param vp Velocidad de proyecto
	 * 
	 * @return Distancia de visibilidad necesaria en metros
	 */
	public static double distanciaFinProhibicionAdelantamiento(double vp) {
		return 57.142857142857146 + 1.6071428571428572*vp + 0.017857142857142856*vp*vp;
	}
}
