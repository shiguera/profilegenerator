package com.mlab.pg.reconstruction;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mlab.pg.util.MathUtil;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class BorderPointsExtractor {

	Logger LOG = Logger.getLogger(BorderPointsExtractor.class);
	
	protected XYVectorFunction function;
	protected int baseSize;
	protected double thresholdSlope;
	protected PointTypeArray originalPointTypes;
	protected TypeIntervalArray typeIntervalArray, resultTypeIntervalArray;

	protected List<Integer> borderPointIndexes;
	

	public BorderPointsExtractor(XYVectorFunction function, int basesize, double thresholdslope) throws NullTypeException {
		this.function = function;
		this.baseSize = basesize;
		this.thresholdSlope = thresholdslope;
		
		// Inicializa las variables que albergarán los resultados
		resultTypeIntervalArray = new TypeIntervalArray();
		borderPointIndexes = new ArrayList<Integer>();

		// Caracterizo un PoinType a cada punto del perfil 
		ProfileCharacteriser characteriser = new ProfileCharacteriser();
		originalPointTypes = characteriser.characterise(function, baseSize, thresholdSlope); 

		// Reuno en un TypeInterval los puntos consecutivos que tienen el mismo PointType
		typeIntervalArray = new TypeIntervalArray(originalPointTypes);
		
		// Si en la creación de los TypeInterval aparece algún intervalo 
		// de PointType.NULL error, cancelo la operación
		if(typeIntervalArray.hasNullSegments()) {
			throw(new NullTypeException());
		}
		
		// Proceso el Array de TypeInterval
		processTypeIntervalArray();
	}
	
	private void processTypeIntervalArray() {
		for (int i=0; i<typeIntervalArray.size(); i++) {
			TypeInterval currentInterval = typeIntervalArray.get(i);
			
			// El primer y último segmento los añado como están
			if(i==0 || i==typeIntervalArray.size()-1) {
				resultTypeIntervalArray.add(currentInterval);
				borderPointIndexes.add(currentInterval.getStart());
				continue;
			}			
			
			// Los segmentos que no son BORDER los añado como están
			if(currentInterval.getPointType() != PointType.BORDER_POINT) {
				resultTypeIntervalArray.add(currentInterval);
				continue;
			}
			
			if(currentInterval.size() == 1) {
				// Si el intervalo solo tiene un punto, añado su índice a la lista de BorderPoints
				borderPointIndexes.add(currentInterval.getStart());
				resultTypeIntervalArray.add(currentInterval);
				continue;
			} else {
				// Si tiene más de un punto, proceso para ver cuál es el que mejor aproxima
				processBorderSegmentWithMoreThanOnePoint(i);
				i=i+1;					
			}
		} 
		
		// Si el primer segmento tiene más de un punto, asigno todos
		// los puntos menos el primero al segundo segmento
		TypeInterval firstInterval = resultTypeIntervalArray.get(0);
		if(firstInterval.getPointType() == PointType.BORDER_POINT && firstInterval.size()>1) {
			//processFirstSegmentAsBorder();
			resultTypeIntervalArray.get(1).setStart(1);
		}
		
		// Si el último segmento tiene más de un punto, asigno todos lo puntos,
		// excepto el último, al penultimo segmento
		TypeInterval lastInterval = resultTypeIntervalArray.get(resultTypeIntervalArray.size()-1);
		if(lastInterval.getPointType() == PointType.BORDER_POINT && lastInterval.size() > 1) {
			resultTypeIntervalArray.get(resultTypeIntervalArray.size()-2).setEnd(lastInterval.getEnd()-1);
			lastInterval.setStart(lastInterval.getEnd());
			
		}
		
		OnePointIntervalFilter filter = new OnePointIntervalFilter(resultTypeIntervalArray);
		resultTypeIntervalArray = filter.getFilteredTypeIntervalArray();
	}
	
	private void processBorderSegmentWithMoreThanOnePoint(int index) {
		
		TypeInterval currentBorderSegment = typeIntervalArray.get(index).copy();
		TypeInterval previousSegment = typeIntervalArray.get(index-1).copy();
		TypeInterval followingSegment = typeIntervalArray.get(index+1).copy();

		// Extraigo los puntos afectados en el procesamiento
		XYVectorFunction originalpoints = function.subList(previousSegment.getStart(), followingSegment.getEnd());
				
		double ecmmin = -1.0;
		int ecmmin_index = -1;
		for(int i=currentBorderSegment.getStart(); i<= currentBorderSegment.getEnd(); i++) {
			
			// Calculo las rectas de minimos cuadrados a ambos lados del borderpoint
			double[] r1 = function.rectaMinimosCuadrados(previousSegment.getStart(), i);
			double[] r2 = function.rectaMinimosCuadrados(i, followingSegment.getEnd());
			
			// Calculo el area encerrado bajo la muestra original de puntos
			double area = function.areaEncerrada(previousSegment.getStart(), followingSegment.getEnd());
			
			// Calculo las rectas desplazadas que encierran el mismo area
			double[] coefs = MathUtil.mueveRactasParaEncerrarArea(r1, r2, previousSegment.getStart(), i, followingSegment.getEnd(), area);
			r1 = new double[] {coefs[0], r1[1]};
			r2 = new double[]{coefs[1], r2[1]};
			double ycomun = coefs[2];
						
			// Genero las coordenadas de los puntos ajustados
			XYVectorFunction adjustedPoints = new XYVectorFunction();
			for(int j=previousSegment.getStart(); j<i; j++) {
				double x = function.getX(j);
				double y = r1[0] + r1[1] * x;
				adjustedPoints.add(new double[]{x, y});
			}
			for(int j=i; j<=followingSegment.getEnd(); j++) {
				double x = function.getX(j);
				double y = r2[0] + r2[1] * x;
				adjustedPoints.add(new double[]{x, y});
			}
			
			// Calculo el ecm
			double[] originalY = originalpoints.getYValues();
			double[] adjustedY = adjustedPoints.getYValues();
			double ecm = MathUtil.ecm(originalY, adjustedY);
			//LOG.debug(ecm);
			if(ecmmin==-1.0) {
				// primera iteración
				ecmmin = ecm;
				ecmmin_index = i;
				continue;
			} else {
				if(ecm < ecmmin) {
					ecmmin=ecm;
					ecmmin_index = i;
				}
			}
		}
		borderPointIndexes.add(ecmmin_index);
		
		resultTypeIntervalArray.get(resultTypeIntervalArray.size()-1).setEnd(ecmmin_index-1);
		resultTypeIntervalArray.add(new TypeInterval(ecmmin_index, ecmmin_index, PointType.BORDER_POINT));
		resultTypeIntervalArray.add(followingSegment.copy());
		resultTypeIntervalArray.get(resultTypeIntervalArray.size()-1).setStart(ecmmin_index+1);
	}
	
	// Getters - Setters
	public List<Integer> getBorderPointIndexes() {
		return borderPointIndexes;
	}
	public TypeIntervalArray getTypeIntervalArray() {
		return typeIntervalArray;
	}
	public TypeIntervalArray getResultTypeIntervalArray() {
		return resultTypeIntervalArray;
	}

}
