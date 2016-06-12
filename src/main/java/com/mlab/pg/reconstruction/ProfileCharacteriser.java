package com.mlab.pg.reconstruction;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mlab.pg.xyfunction.XYVectorFunction;

/**
 * Caracteriza los puntos de una XYVectorFunction a partir de las pendientes 
 * de las bases móviles anterior y posterior
 *  
 * @author shiguera
 *
 */
public class ProfileCharacteriser {
	
	Logger LOG = Logger.getLogger(ProfileCharacteriser.class);
	
	
	public ProfileCharacteriser() {
	}

	/**
	 * Caracteriza los puntos a partir de las pendientes de las bases móviles
	 * previa y posterior.
	 * El primer punto caracterizable es el de índice (mobileBaseSize-1).</br>
	 * El último punto caracterizable es el XYVectorFunction.size()-mobileBaseSize.</br>
	 * 
	 * @param gpsample

	 * @return Lista de puntos con un tipo PointType asignado 
	 */
	List<PointType> characterise(XYVectorFunction gpsample,int mobileBaseSize, double thresholdSlope) {
		if(gpsample==null || gpsample.size()<2*mobileBaseSize-1) {
			LOG.error("characterise() ERROR: NULL RESULT");
			return null;
		}
		int sampleSize = gpsample.size();
		// Se crea el List<PointType> e inicialmente se rellena con tipo NULL todos los puntos, 
		// para tener el List creado
		List<PointType> types = new ArrayList<PointType>();
		for(int i=0; i<sampleSize; i++) {
			types.add(PointType.NULL);
		}
		// Se caracterizan los puntos caracterizables, excluyendo los de los extremos
		int first = mobileBaseSize-1;
		int last = gpsample.size()-mobileBaseSize;
		for(int i=first; i<=last; i++) {
			types.set(i, findPointType(i,gpsample, mobileBaseSize,thresholdSlope));
		}
		return types;
	}

	private PointType findPointType(int i, XYVectorFunction gpsample,
			int mobileBaseSize, double thresholdSlope) {
		PointCharacteriser pch = new PointCharacteriser();
		
		return pch.characterise(i, gpsample, mobileBaseSize, thresholdSlope);
	}
	
	/**
	 * Calcula la interpolación por mínimos cuadrados de la recta anterior de un 
	 * punto de una XYVectorFunction
	 *  
	 * @param function XYVectorFunction que incluye los puntos de la recta a calcular
	 * @param last Índice del punto del que se quiere calcular la recta anterior
 	 * @param straightsize Número de puntos de la recta
 	 * 
	 * @return double[] con la recta en la forma y = a[0] + a[1]x
	 */
	protected double[] calculaRectaAnterior(XYVectorFunction function, int last, int straightsize) {
		if(function.size()<last+1) {
			return null;
		}
		int first = last - straightsize +1;
		if(first<0) {
			return null;
		}
		return function.rectaMinimosCuadrados(first, last);
	}
	/**
	 * Calcula la interpolación por mínimos cuadrados de la recta posterior de un
	 * punto de una XYVectorFunction
	 * 
	 * @param function XYVectorFunction que incluye los puntos de la recta a calcular
	 * @param first Índice del punto del que se quiere calcular la recta posterior
	 * @param straightsize Número de puntos de la recta
	 * 
	 * @return double[] con la recta en la forma y = a[0] + a[1]x
	 */
	protected double[] calculaRectaPosterior(XYVectorFunction function, int first, int straightsize) {
		int last = first + straightsize-1;
		if(function.size()<last+1) {
			return null;
		}
		return function.rectaMinimosCuadrados(first, last);
	}
}
