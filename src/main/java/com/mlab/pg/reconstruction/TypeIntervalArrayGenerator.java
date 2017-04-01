package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;

import com.mlab.pg.reconstruction.strategy.InterpolationStrategy;
import com.mlab.pg.reconstruction.strategy.PointCharacteriserStrategy;
import com.mlab.pg.reconstruction.strategy.PointCharacteriserStrategy_EqualArea;
import com.mlab.pg.reconstruction.strategy.PointCharacteriserStrategy_LessSquares;
import com.mlab.pg.reconstruction.strategy.ProcessBorderIntervalsStrategy;
import com.mlab.pg.reconstruction.strategy.ProcessBorderIntervalsStrategy_EqualArea;
import com.mlab.pg.reconstruction.strategy.ProcessBorderIntervalsStrategy_LessSquares;
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

	InterpolationStrategy interpolationStrategy;
	/**
	 * Longitud mínima de los segmentos generados para que no se intente filtrarlos 
	 * uniéndolos con el anterior o el siguiente si son del mismo tipo
	 */
	double minLength;

	
	ProcessBorderIntervalsStrategy processBorderIntervalsStrategy;
	PointCharacteriserStrategy pointCharacteriserStrategy;	
	TypeIntervalArray resultIntervalArray;
	
	/**
	 * Construye una TypeIntervalArray a partir de una XYVectorFunction. La TypeIntervalArray resultado se
	 * puede consultar en getResultTypeIntervalArray() y solo tiene segmentos del tipo Grade o VerticalCurve.
	 * 
	 * @param originalgradePoints
	 * @param mobilebasesize
	 * @param thresholdslope
	 * @param strategy Estrategia de interpolación utilizada
	 * @param minlength Longitud mínima de los segmentos para que no intente filtrarlos
	 * uniéndolos al anterior o siguiente, si son del mismo tipo
	 */
	public TypeIntervalArrayGenerator(XYVectorFunction originalgradePoints, int mobilebasesize, double thresholdslope, InterpolationStrategy strategy, double minlength) {
		originalGradePoints = originalgradePoints.clone();
		mobileBaseSize = mobilebasesize;
		if(originalGradePoints.size()<2*mobileBaseSize-1) {
			System.out.println("Aquí");
		}
		thresholdSlope = thresholdslope;
		interpolationStrategy = strategy;
		minLength = minlength;
		
		if (interpolationStrategy == InterpolationStrategy.EqualArea || interpolationStrategy == InterpolationStrategy.EqualArea_Multiparameter) {
			this.pointCharacteriserStrategy = new PointCharacteriserStrategy_EqualArea();
			this.processBorderIntervalsStrategy = new ProcessBorderIntervalsStrategy_EqualArea();	
		} else {
			this.pointCharacteriserStrategy = new PointCharacteriserStrategy_LessSquares();
			this.processBorderIntervalsStrategy = new ProcessBorderIntervalsStrategy_LessSquares();				
		}

		resultIntervalArray = processBorderIntervalsStrategy.processBorderIntervals(originalGradePoints, mobileBaseSize, thresholdSlope, pointCharacteriserStrategy);
		
		if(resultIntervalArray.size()>1) {
			//LOG.debug("Filtered: YES");
			resultIntervalArray = filterShortIntervals(resultIntervalArray);
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
			//System.out.println("Ronda filtro: " + contador);
			//System.out.println(processIntervalArray.size());
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
	private TypeIntervalArray filterShortIntervals(TypeIntervalArray intervalArray) {
		
		TypeIntervalArray processIntervalArray = new TypeIntervalArray();
		processIntervalArray.addAll(intervalArray);
		
		TypeIntervalArray result = new TypeIntervalArray();
		
		boolean changes = true;
		int contador = 0;
		while(changes) {
			contador++;
			//System.out.println("Ronda filtro: " + contador);
			changes = false;
			result = new TypeIntervalArray();
			
			// Primero trato de unir cada tramo corto con el anterior
			result.add(processIntervalArray.get(0));
			for(int i=1; i<processIntervalArray.size(); i++) {
				TypeInterval current = processIntervalArray.get(i);
				int numpoints = current.size();
				double length = originalGradePoints.getX(current.getEnd()) - originalGradePoints.getX(current.getStart());
				if(length<minLength || numpoints<5) {
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
			processIntervalArray = new TypeIntervalArray();
			processIntervalArray.addAll(result);
			
			// Ahora trato de unir los cortos con el siguiente
			result = new TypeIntervalArray();
			result.add(processIntervalArray.get(0));
			for(int i=1; i<processIntervalArray.size();i++) {
				TypeInterval current = processIntervalArray.get(i);
				TypeInterval previous = processIntervalArray.get(i);
				double length = originalGradePoints.getX(previous.getEnd()) - originalGradePoints.getX(previous.getStart());
				if(length < minLength) {
					if(current.getPointType() == current.getPointType()) {
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


	public TypeIntervalArray getResultTypeIntervalArray() {
		return resultIntervalArray;
	}
	
	public int getMobileBaseSize() {
		return mobileBaseSize;
	}

	public double getThresholdSlope() {
		return thresholdSlope;
	}
	
	
	
	
	
}
