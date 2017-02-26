package com.mlab.pg.reconstruction;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mlab.pg.util.MathUtil;
import com.mlab.pg.xyfunction.IntegerInterval;
import com.mlab.pg.xyfunction.Parabole;
import com.mlab.pg.xyfunction.Straight;
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
		
		ProfileCharacteriser characteriser = new ProfileCharacteriser();
		originalPointTypes = characteriser.characterise(function, baseSize, thresholdSlope); 

		typeIntervalArray = new TypeIntervalArray(originalPointTypes);
		resultTypeIntervalArray = new TypeIntervalArray();
	
		borderPointIndexes = new ArrayList<Integer>();

		if(!typeIntervalArray.hasNullSegments()) {
			processBorderIntervals();			
		} else {
			throw(new NullTypeException());
		}
	}
	
	private void processBorderIntervals() {
		for (int i=0; i<typeIntervalArray.size(); i++) {
			TypeInterval currentInterval = typeIntervalArray.get(i);
			
			// El primer y último segmento los añado como están
			// Más adelante, si son Border, los procesaré
			if(i==0 || i==typeIntervalArray.size()-1) {
				resultTypeIntervalArray.add(currentInterval);
				continue;
			}
			
			if(currentInterval.getPointType() == PointType.BORDER_POINT) {
				if(currentInterval.size() == 1) {
					// Si el intervalo solo tiene unpunto, añado su índice a la lista de BorderPoints
					borderPointIndexes.add(currentInterval.getStart());
				} else {
					// Si tiene más de un punto, proceso para ver cuál es el que mejor aproxima
					processBorderSegmentWithMoreThanOnePoint(i);
				}
				i=i+2;				
			} else {
				// Los segmentos que no son BORDER los añado como están
				resultTypeIntervalArray.add(currentInterval);
			}
		}
		// Si el primer segmento ha quedado del tipo BORDER, le asigno del tipo del siguiente
		if(resultTypeIntervalArray.get(0).getPointType() == PointType.BORDER_POINT) {
			processFirstSegmentAsBorder();
		}
		// Si el último segmento ha quedado del tipo BORDER, le asigno el tipo del anterior
		int last = resultTypeIntervalArray.size()-1;
		if(resultTypeIntervalArray.get(last).getPointType() == PointType.BORDER_POINT) {
			resultTypeIntervalArray.get(last-1).setEnd(resultTypeIntervalArray.get(last).getEnd());
			resultTypeIntervalArray.remove(last);
		}
	}
	private void processFirstSegmentAsBorder() {
		if(resultTypeIntervalArray.get(0).size()<4) {
			// Si tiene menos de cuatro puntos se lo asigno al siguiente
			resultTypeIntervalArray.get(1).setStart(0);
			resultTypeIntervalArray.remove(0);
			return;
		}
		int last = resultTypeIntervalArray.get(0).getEnd();
		double[][] origvalues = function.getValuesAsArray(new IntegerInterval(0, last));
		double[] rr = MathUtil.rectaMinimosCuadrados(origvalues);
		Straight r = new Straight(rr[0], rr[1]);
		double[] pp = MathUtil.parabolaMinimosCuadrados(origvalues);
		Parabole p = new Parabole(pp[0], pp[1], pp[2]);
		double ecmrecta = MathUtil.ecmPolynomToPoints(r, origvalues);
		double ecmparab = MathUtil.ecmPolynomToPoints(p, origvalues);
		TypeInterval nextSegment =resultTypeIntervalArray.get(1);
		if(ecmrecta<=ecmparab) {
			// Caso aproxima mejor la recta
			if(nextSegment.getPointType()==PointType.GRADE) {
				// Si el siguiente es recta, los uno en uno solo
				nextSegment.setStart(0);
				resultTypeIntervalArray.remove(0);
			} else {
				// Si el siguiente es VC, el primero lo convierto en recta
				resultTypeIntervalArray.get(0).setPointType(PointType.GRADE);
			}
		} else {
			// Caso aproxima mejor la parábola
			if(nextSegment.getPointType()==PointType.GRADE) {
				// Si el siguiente es recta, el primero lo convierto en VC
				resultTypeIntervalArray.get(0).setPointType(PointType.VERTICAL_CURVE);
			} else {
				// Si el siguiente es VC las uno en una sola 
				// TODO Quizás habría que comprobar si aproxima mejor con una sola o con dos independientes
				nextSegment.setStart(0);
				resultTypeIntervalArray.remove(0);
			}
		}
	}
	private void processBorderSegmentWithMoreThanOnePoint(int index) {
		TypeInterval currentBorderSegment = typeIntervalArray.get(index).copy();
		int startOfCurrentBorderSegment = currentBorderSegment.getStart();
		int endOfCurrentBorderSegment = currentBorderSegment.getEnd();
		TypeInterval followingSegment = typeIntervalArray.get(index+1).copy();
		int endOfFollowingSegment = followingSegment.getEnd();
		//PointTypeSegment lastProcessedSegment = processedSegments.get(processedSegments.size()-1);
		
		// Si el border segment tiene más de un punto busco
		// el que mejor ajuste da por ecm y prolongo 
		// los segmentos anterior y posterior hasta él
		TypeInterval previousSegment = typeIntervalArray.get(index-1).copy();
		int startOfPreviousSegment = previousSegment.getStart();
		XYVectorFunction originalpoints = function.subList(startOfPreviousSegment, endOfFollowingSegment);
		double[] originalY = originalpoints.getYValues();
				
		double ecmmin = -1.0;
		int ecmmin_index = -1;
		for(int i=startOfCurrentBorderSegment; i<=endOfCurrentBorderSegment; i++) {
			XYVectorFunction adjustedPoints = new XYVectorFunction();
			
			double[] r1 = function.rectaMinimosCuadrados(startOfPreviousSegment, i);
			
			double[] r2 = function.rectaMinimosCuadrados(i, endOfFollowingSegment);
			
			// Calculo el area encerrado bajo la muestra original de puntos
			double area = function.areaEncerrada(startOfPreviousSegment, endOfFollowingSegment);
			
			// Calculo las rectas desplazadas que encierran el mismo area
			double[] coefs = MathUtil.mueveRactasParaEncerrarArea(r1, r2, startOfPreviousSegment, i, endOfFollowingSegment, area);
			r1 = new double[] {coefs[0], r1[1]};
			r2 = new double[]{coefs[1], r2[1]};
			double ycomun = coefs[2];
						
			// Genero las coordenadas de los puntos ajustados
			for(int j=startOfPreviousSegment; j<i; j++) {
				double x = function.getX(j);
				double y = r1[0] + r1[1] * x;
				adjustedPoints.add(new double[]{x, y});
			}
			for(int j=i; j<=endOfFollowingSegment; j++) {
				double x = function.getX(j);
				double y = r2[0] + r2[1] * x;
				adjustedPoints.add(new double[]{x, y});
			}
			// Calculo el ecm
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
		//LOG.debug("ecm = " + ecmmin);
		//LOG.debug("ecmmin_index = " + ecmmin_index);
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
