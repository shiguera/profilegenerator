package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;

import com.mlab.pg.util.MathUtil;
import com.mlab.pg.xyfunction.IntegerInterval;
import com.mlab.pg.xyfunction.Parabole;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

/** 
 * A partir de un perfil de pendientes en la forma de una XYVectorFunction, un tamaño
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

	
	ProcessBorderIntervalsStrategy processBorderIntervalsStrategy;
	
	PointCharacteriserStrategy pointCharacteriserStrategy;
	/**
	 * Construye una TypeIntervalArray a partir de una XYVectorFunction. La TypeIntervalArray resultado se
	 * puede consultar en getResultTypeIntervalArray() y solo tiene segmentos del tipo Grade o VerticalCurve.
	 * Si se generan segmentos NULL arroja una excepción
	 * 
	 * @param gradesample
	 * @param mobilebasesize
	 * @param thresholdslope
	 * @throws NullTypeException
	 */
	public TypeIntervalArrayGenerator(XYVectorFunction gradesample, int mobilebasesize, double thresholdslope, 
			PointCharacteriserStrategy pCharacteriserStrategy, ProcessBorderIntervalsStrategy processBorderIntervalStrategy) {
		this.originalGradePoints = gradesample;
		this.mobileBaseSize = mobilebasesize;
		this.thresholdSlope = thresholdslope;
		this.pointCharacteriserStrategy = pCharacteriserStrategy;
		this.processBorderIntervalsStrategy = processBorderIntervalStrategy;
		
		processBorderIntervalsStrategy.processBorderIntervals(originalGradePoints, mobileBaseSize, thresholdSlope, pointCharacteriserStrategy);
		
		
	}

	
	
	
	// Getter -Setter
	public XYVectorFunction getOriginalGradePoints() {
		return originalGradePoints;
	}


	public TypeIntervalArray getOriginalTypeIntervalArray() {
		return processBorderIntervalsStrategy.getOriginalTypeIntervalArray();
	}
	public TypeIntervalArray getResultTypeSegmentArray() {
		return processBorderIntervalsStrategy.getResultTypeIntervalArray();
	}
	
	public int getMobileBaseSize() {
		return mobileBaseSize;
	}

	public double getThresholdSlope() {
		return thresholdSlope;
	}
	
	
	
	
	
}
