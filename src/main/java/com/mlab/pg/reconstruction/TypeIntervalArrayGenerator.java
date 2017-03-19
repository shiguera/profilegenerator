package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;

import com.mlab.pg.util.MathUtil;
import com.mlab.pg.xyfunction.IntegerInterval;
import com.mlab.pg.xyfunction.Parabole;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

/** 
 * A de una XYVectorFunction correspondiente a puntos (s, g) de un diagrama de pendientes, un tamaño
 * de la mobileBaseSize y un valor límite para el thresholdSlope genera un TypeIntervalArray 
 * caracterizando los distintos segmentos que encuentra en el perfil de pendientes.
 * El resultado se obtiene, tras llamar al constructor, en el método getResultTypeSegmentArray()
 *  
 * @author shiguera
 *
 */
public class TypeIntervalArrayGenerator {
	
	Logger LOG = Logger.getLogger(TypeIntervalArrayGenerator.class);

	double MIN_LENGTH =40.0;
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

	InterpolationStrategy interpolationStrategy;
	ProcessBorderIntervalsStrategy processBorderIntervalsStrategy;
	PointCharacteriserStrategy pointCharacteriserStrategy;
	
	TypeIntervalArray resultIntervalArray;
	/**
	 * Construye una TypeIntervalArray a partir de una XYVectorFunction. La TypeIntervalArray resultado se
	 * puede consultar en getResultTypeIntervalArray() y solo tiene segmentos del tipo Grade o VerticalCurve.
	 * Si se generan segmentos NULL arroja una excepción
	 * 
	 * @param originalgradePoints
	 * @param mobilebasesize
	 * @param thresholdslope
	 * @throws NullTypeException
	 */
	public TypeIntervalArrayGenerator(XYVectorFunction originalgradePoints, int mobilebasesize, double thresholdslope, InterpolationStrategy strategy) {
		originalGradePoints = originalgradePoints;
		mobileBaseSize = mobilebasesize;
		thresholdSlope = thresholdslope;
		interpolationStrategy = strategy;
		
		if (interpolationStrategy == InterpolationStrategy.EqualArea) {
			this.pointCharacteriserStrategy = new PointCharacteriserStrategy_EqualArea();
			this.processBorderIntervalsStrategy = new ProcessBorderIntervalsStrategy_EqualArea();	
		} else {
			this.pointCharacteriserStrategy = new PointCharacteriserStrategy_LessSquares();
			this.processBorderIntervalsStrategy = new ProcessBorderIntervalsStrategy_LessSquares();				
		}

		
		resultIntervalArray = processBorderIntervalsStrategy.processBorderIntervals(originalGradePoints, mobileBaseSize, thresholdSlope, pointCharacteriserStrategy);
		
		//LOG.debug("TypeIntervalArray before filter: " + resultIntervalArray.size());
		if(resultIntervalArray.size()>1) {
			//LOG.debug("Filtered: YES");
			resultIntervalArray = filter(resultIntervalArray);
			resultIntervalArray = filterTwoGrades(resultIntervalArray);
		} else{
			//LOG.debug("Filtered: NO");
		}
		//LOG.debug("After Filter: " + resultIntervalArray.size());
	}

	private TypeIntervalArray filterTwoGrades(TypeIntervalArray intervalArray) {
		TypeIntervalArray result = new TypeIntervalArray();

		TypeIntervalArray processIntervalArray = new TypeIntervalArray();
		processIntervalArray.addAll(intervalArray);
		boolean changes = true;
		int contador = 0;
		while(changes) {
			contador++;
			System.out.println("Ronda filtro: " + contador);
			System.out.println(processIntervalArray.size());
			changes = false;
			result = new TypeIntervalArray();
			result.add(processIntervalArray.get(0));
			for(int i=1; i<processIntervalArray.size(); i++) {
				TypeInterval current = processIntervalArray.get(i);
				TypeInterval previous = result.getLast();
				if(previous.getPointType() == PointType.GRADE && current.getPointType() == PointType.GRADE) {					
					result.getLast().setEnd(current.getEnd());
					changes = true;
				} else {
					result.add(current);
				}
			}
			processIntervalArray = new TypeIntervalArray();
			processIntervalArray.addAll(result);
		}
		return result;
		
	}
	
	// Filtra las alineaciones de longitud menor que la mínima
	private TypeIntervalArray filter(TypeIntervalArray intervalArray) {
		TypeIntervalArray result = new TypeIntervalArray();
		
		TypeIntervalArray processIntervalArray = new TypeIntervalArray();
		processIntervalArray.addAll(intervalArray);
		boolean changes = true;
		int contador = 0;
		while(changes) {
			contador++;
			//System.out.println("Ronda filtro: " + contador);
			changes = false;
			result = new TypeIntervalArray();
			result.add(processIntervalArray.get(0));
			for(int i=1; i<processIntervalArray.size(); i++) {
				TypeInterval current = processIntervalArray.get(i);
				TypeInterval previous = result.getLast();
				double lengthPrevious = originalGradePoints.getX(previous.getEnd()) - originalGradePoints.getX(previous.getStart());
				if(lengthPrevious < MIN_LENGTH) {
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
			processIntervalArray = new TypeIntervalArray();
			processIntervalArray.addAll(result);
		}
		return result;
	}
	
	
	
	// Getter -Setter
	public XYVectorFunction getOriginalGradePoints() {
		return originalGradePoints;
	}


	public TypeIntervalArray getOriginalTypeIntervalArray() {
		return processBorderIntervalsStrategy.getOriginalTypeIntervalArray();
	}
	public TypeIntervalArray getResultTypeSegmentArray() {
		return resultIntervalArray;
	}
	
	public int getMobileBaseSize() {
		return mobileBaseSize;
	}

	public double getThresholdSlope() {
		return thresholdSlope;
	}
	
	
	
	
	
}
