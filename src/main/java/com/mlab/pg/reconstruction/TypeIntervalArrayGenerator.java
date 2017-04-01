package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;

import com.mlab.pg.reconstruction.strategy.InterpolationStrategy;
import com.mlab.pg.reconstruction.strategy.PointCharacteriserStrategy;
import com.mlab.pg.reconstruction.strategy.PointCharacteriserStrategy_EqualArea;
import com.mlab.pg.reconstruction.strategy.PointCharacteriserStrategy_LessSquares;
import com.mlab.pg.reconstruction.strategy.PointCharacteriserStrategy_Multiparameter;
import com.mlab.pg.reconstruction.strategy.ProcessBorderIntervalsStrategy;
import com.mlab.pg.reconstruction.strategy.ProcessBorderIntervalsStrategy_EqualArea;
import com.mlab.pg.reconstruction.strategy.ProcessBorderIntervalsStrategy_LessSquares;
import com.mlab.pg.reconstruction.strategy.ProcessBorderIntervalsStrategy_Multiparameter;
import com.mlab.pg.xyfunction.XYVectorFunction;

/** 
 * A partir de una XYVectorFunction correspondiente a puntos (s, g) de un diagrama de pendientes, un tamaño
 * de la mobileBaseSize y un valor límite para el thresholdSlope genera un TypeIntervalArray 
 * caracterizando los distintos segmentos que encuentra en el perfil de pendientes.
 * El resultado se obtiene, tras llamar al constructor, en el método getResultTypeSegmentArray()
 *  
 * @author shiguera
 *
 */
public class TypeIntervalArrayGenerator {
	
	Logger LOG = Logger.getLogger(TypeIntervalArrayGenerator.class);
	
	/**
	 * Valores {si, gi} correspondientes a un perfil de pendientes que se quieren procesar.
	 * Se reciben como parámetro del constructor
	 */
	protected XYVectorFunction originalGradePoints;
	/**
	 * Tamaño de la base móvil que se utilizará para caracterizar los puntos del perfil de pendientes.
	 * Se recibe como parámetro en el constructor de la clase.
	 */
	protected int mobileBaseSize;
	/**
	 * Valor límite de la pendiente de las rectas que se considerarán como horizontales
	 * durante la caracterización de los puntos. Se recibe como parámetro en el constructor de la clase
	 */
	protected double thresholdSlope;

	protected InterpolationStrategy interpolationStrategy;
	/**
	 * Longitud mínima de los segmentos generados para que no se intente filtrarlos 
	 * uniéndolos con el anterior o el siguiente si son del mismo tipo
	 */
	protected double minLength;
	protected int minPointsCount;
	
	protected ProcessBorderIntervalsStrategy processBorderIntervalsStrategy;
	protected PointCharacteriserStrategy pointCharacteriserStrategy;	
	protected TypeIntervalArray resultIntervalArray;
	
	public TypeIntervalArrayGenerator(InterpolationStrategy strategy, double minlength, int minpointscount) {
		interpolationStrategy = strategy;
		minLength = minlength;
		minPointsCount = minpointscount;

		if (interpolationStrategy == InterpolationStrategy.EqualArea ) {
			pointCharacteriserStrategy = new PointCharacteriserStrategy_EqualArea();
			processBorderIntervalsStrategy = new ProcessBorderIntervalsStrategy_EqualArea();	
		} else if (interpolationStrategy == InterpolationStrategy.EqualArea_Multiparameter) {
			pointCharacteriserStrategy = new PointCharacteriserStrategy_Multiparameter();
			processBorderIntervalsStrategy = new ProcessBorderIntervalsStrategy_Multiparameter();
		} else{
			pointCharacteriserStrategy = new PointCharacteriserStrategy_LessSquares();
			processBorderIntervalsStrategy = new ProcessBorderIntervalsStrategy_LessSquares();				
		}
	}

	/**
	 * Construye una TypeIntervalArray a partir de una XYVectorFunction con los originalGradePoints. 
	 * La TypeIntervalArray resultado se puede consultar en getResultTypeIntervalArray() 
	 * y solo tiene segmentos del tipo Grade o VerticalCurve.
	 * 
	 * @param originalgradePoints
	 * @param mobilebasesize
	 * @param thresholdslope
	 * @param strategy Estrategia de interpolación utilizada
	 * @param minlength Longitud mínima de los segmentos para que no intente filtrarlos
	 * uniéndolos al anterior o siguiente, si son del mismo tipo
	 */
	public TypeIntervalArray processPoints(XYVectorFunction originalgradePoints, int mobilebasesize, double thresholdslope) {	
		originalGradePoints = originalgradePoints.clone();
		mobileBaseSize = mobilebasesize;
		if(originalGradePoints.size()<2*mobileBaseSize-1) {
			return null;
		}
		thresholdSlope = thresholdslope;
		resultIntervalArray = processBorderIntervalsStrategy.processBorderIntervals(originalGradePoints, mobileBaseSize, thresholdSlope, pointCharacteriserStrategy);
		
		if(resultIntervalArray.size()>1) {
			resultIntervalArray = filterShortIntervals(originalGradePoints, resultIntervalArray, minLength, minPointsCount);
			resultIntervalArray = filterTwoGrades(resultIntervalArray);
		} else{
			//LOG.debug("Filtered: NO");
		}
		return resultIntervalArray;
	}
	
	/** 
	 * Filtra los casos de dos segmentos seguidos del tipo GRADE
	 * uniéndolos en un único segmento
	 * 
	 * @param intervalArray TypeIntervalArray con los segmentos de puntos
	 * caracterizados
	 * 
	 * @return TypeIntervalArray con los segemento de puntos sin dos 
	 * segementos GRADE consecutivos
	 */
	private TypeIntervalArray filterTwoGrades(TypeIntervalArray intervalArray) {
		TypeIntervalArray result = new TypeIntervalArray();

		TypeIntervalArray process = new TypeIntervalArray();
		process.addAll(intervalArray);
		boolean changes = true;
		while(changes) {
			changes = false;
			result = new TypeIntervalArray();
			result.add(process.get(0));
			for(int i=1; i<process.size(); i++) {
				TypeInterval current = process.get(i);
				TypeInterval previous = result.getLast();
				if(previous.getPointType() == PointType.GRADE && current.getPointType() == PointType.GRADE) {					
					result.getLast().setEnd(current.getEnd());
					changes = true;
				} else {
					result.add(current);
				}
			}
			process = new TypeIntervalArray();
			process.addAll(result);
		}
		return result;
		
	}
	
	/**
	 * Filtra las alineaciones de longitud menor que la mínima o que están formadas
	 * por menos de un número de puntos.
	 * 
	 * @param originalgpoints XYVectorFunction con los puntos originales de laspendientes. Los
	 * necesita para calcular las longitudes de cada segemento
	 * @param intervalArray TypeIntervalArray conlos segmentos de puntos caracterizados
	 * @param minlength Longitud mínima quepueden tener los segmentos
	 * @param minpointscount Número mínimo de puntos que puede tener un segmento
	 * 
	 * @return TypeIntervalArray con los segmentos filtrados. Puede pasar que algún segmento que cumpla las
	 * condiciones de filtrado nose filtre, porque ni el anterior ni el siguiente son del mismo tipo
	 */
	private TypeIntervalArray filterShortIntervals(XYVectorFunction originalgpoints, TypeIntervalArray intervalArray, double minlength, int minpointscount) {
		TypeIntervalArray process = new TypeIntervalArray();
		process.addAll(intervalArray);
		TypeIntervalArray result = new TypeIntervalArray();
		boolean changes = true;
		while(changes) {
			changes = false;
			result = new TypeIntervalArray();			
			result.add(process.get(0));
			for(int i=1; i<process.size(); i++) {
				TypeInterval current = process.get(i);
				int numpoints = current.size();
				double length = originalgpoints.getX(current.getEnd()) - originalgpoints.getX(current.getStart());
				if(length<minlength || numpoints < minpointscount) {
					TypeInterval previous = result.getLast();
					if(previous.getPointType() == current.getPointType()) {
						result.getLast().setEnd(current.getEnd());
						changes = true;
					} else {
						result.add(current);
					}
				} else {
					result.add(current);
				}
			}
			process = new TypeIntervalArray();
			process.addAll(result);
		}			
		return result;
	}
	
	// Getter
	public TypeIntervalArray getResultTypeIntervalArray() {
		return resultIntervalArray;
	}
}
