package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;

import com.mlab.pg.reconstruction.strategy.PointCharacteriserStrategy;
import com.mlab.pg.xyfunction.XYVectorFunction;

/**
 * Clasifica los puntos de una muestra de puntos de un perfil de pendientes
 * según uno de los tipos PointType, para unos valores concretos de 
 * mobileBaseSize y thresholdSlope.
 * 
 *  
 * @author shiguera
 *
 */
public class ProfileCharacteriser {
	
	Logger LOG = Logger.getLogger(ProfileCharacteriser.class);
	
	PointCharacteriser pointCharacteriser;
	PointCharacteriserStrategy strategy;
	
	public ProfileCharacteriser(PointCharacteriserStrategy strategy) {
		this.strategy = strategy;
	}

	/**
	 * Caracteriza cada punto de la muestra según las pendientes de las rectas de interpolación
	 * anterior y posterior.
	 * El primer punto caracterizable es el de índice (mobileBaseSize-1). Los puntos anteriores los
	 * caracteriza del mismo tipo que este</br>
	 * El último punto caracterizable es el XYVectorFunction.size()-mobileBaseSize. Los puntos posteriores 
	 * los caracteriza del mismo tipo que este</br>
	 * 
	 * @param gpsample Valores {Si, Gi} de los puntos que se quiere caracterizar
	 * @param mobileBaseSize Número de puntos de la base móvil que se utilizará 
	 * para caracterizar los puntos.
	 * @param thresholdSlope Valor de la pendiente límite de las rectas que se consideran
	 * horizontales en la caracterización
	 * @return Lista de puntos con un tipo PointType asignado. El tipo de los puntos es Grade, Border
	 * o VerticalCurve 
	 */
	public PointTypeArray characterise(XYVectorFunction gpsample,int mobileBaseSize, double thresholdSlope) {
		if(gpsample==null || gpsample.size()<2*mobileBaseSize-1) {
			LOG.error("characterise() ERROR: NULL RESULT");
			return null;
		}

		// Se crea el PointTypeArray vacío, inicialmente con los tipos NULL 
		PointTypeArray types = new PointTypeArray();
		for(int i=0; i<gpsample.size(); i++) {
			types.add(PointType.NULL);
		}
		
		// Se caracterizan los puntos caracterizables, excluyendo los de los extremos
		int first = mobileBaseSize-1;
		int last = gpsample.size()-mobileBaseSize;
		pointCharacteriser = new PointCharacteriser();
		for(int i=first; i<=last; i++) {
			if(i==-1) {
				System.out.println(i);				
			}
			types.set(i, pointCharacteriser.characterise(gpsample, i, mobileBaseSize,thresholdSlope, strategy));
		}
		// El primer punto se caracteriza como BorderPoint
		types.set(0, PointType.BORDER_POINT);
		
		// Los puntos anteriores a first, excepto el primer punto, se caracterizan del mismo tipo que first
		for (int i=1; i<first; i++) {
			types.set(i, types.get(first));
		}
		
		// Los puntos posteriores a last,excepto el último, se caracterizan del mismo tipo que last
		for (int i=last+1; i<types.size()-1; i++) {
			types.set(i, types.get(last));
		}
		
		// El último punto se caracteriza como BorderPoint
		types.set(types.size()-1, PointType.BORDER_POINT);
		return types;
	}

	
}
