package com.mlab.pg.reconstruction.strategy;

import com.mlab.pg.reconstruction.ParameterIntervalArray;
import com.mlab.pg.reconstruction.PointType;
import com.mlab.pg.reconstruction.TypeInterval;
import com.mlab.pg.reconstruction.TypeIntervalArray;
import com.mlab.pg.util.MathUtil;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class ProcessBorderIntervalsStrategy_EqualArea implements ProcessBorderIntervalsStrategy {

	protected XYVectorFunction originalGradePoints;
	protected int baseSize;
	protected double thresholdSlope;
	protected PointCharacteriserStrategy pointCharacteriserStrategy;
	protected TypeIntervalArray originalIntervalArray;
	protected TypeIntervalArray resultIntervalArray;
	
	public ProcessBorderIntervalsStrategy_EqualArea() {

	}

	@Override
	public TypeIntervalArray processBorderIntervals(XYVectorFunction originalgradepoints, TypeIntervalArray typeIntervalArray, int basesize,
			double thresholdslope) {
		this.originalGradePoints = originalgradepoints;
		this.baseSize = basesize;
		this.thresholdSlope = thresholdslope;
		this.pointCharacteriserStrategy = new PointCharacteriserStrategy_EqualArea();
		
		originalIntervalArray = typeIntervalArray;
		
		if(originalIntervalArray.hasNullSegments()) {
			return null;			
		} 

		resultIntervalArray = new TypeIntervalArray();
		
		for(int i=0; i<originalIntervalArray.size(); i++) {			
			TypeInterval currentInterval = originalIntervalArray.get(i);
			// El primer y último segmento los añado como están
			// Más adelante, si son Border, los procesaré
			if(i==0 || i==originalIntervalArray.size()-1) {
				resultIntervalArray.add(currentInterval);
				continue;
			}
			
			// Los segmentos que no son BORDER los añado como están
			if (currentInterval.getPointType() != PointType.BORDER_POINT) {
				resultIntervalArray.add(currentInterval);
				continue;
			}
			
			// Los intervalos con puntos BorderPoint los proceso
			if(currentInterval.getPointType() == PointType.BORDER_POINT) {
				if(currentInterval.size() == 1) {	
					// Si el border segment tiene solo un punto, lo añado al final del previousSegment y al inicio del followingSegment
					processBorderIntervalWithOnePoint(originalIntervalArray, i, resultIntervalArray);
				} else {
					// Si el BorderSegment tiene más de un punto hay que seleccionar el punto Border que mejor aproxima
					processBorderIntervalWithMoreThanOnePoint(originalGradePoints, originalIntervalArray, i, resultIntervalArray);	
				}
				i++;				
			}
		}
		//System.out.println(resultIntervalArray.size());
		if(resultIntervalArray.size()>1) {			
			// Si el primer segmento ha quedado del tipo BORDER, lo proceso
			TypeInterval firstInterval = resultIntervalArray.get(0); 
			if(firstInterval.getPointType() == PointType.BORDER_POINT) {
				processFirstSegmentAsBorder();
			}
			
			// Si el último segmento ha quedado del tipo BORDER, lo proceso
			int last = resultIntervalArray.size()-1;
			if(resultIntervalArray.get(last).getPointType() == PointType.BORDER_POINT) {
				processLastSegmentAsBorder();
			}
		}
		return resultIntervalArray;
	}
	
	@Override
	public TypeIntervalArray processBorderIntervals(XYVectorFunction originalgradepoints,
			ParameterIntervalArray parameterarray) {
		// No utilizable en esta estrategia
		return null;
	}
	
	private void processFirstSegmentAsBorder() {
		TypeInterval segment0 = resultIntervalArray.get(0);
		TypeInterval segment1 = resultIntervalArray.get(1);
		
		if(segment0.size()<2) {
			// Si tiene menos de dos puntos se lo asigno al siguiente
			segment1.setStart(0);
			resultIntervalArray.remove(0);
			return;
		}
		
		int last = segment0.getEnd();
		double[] rr = originalGradePoints.rectaPosteriorEqualArea(0, last);
		if(Math.abs(rr[1])<=thresholdSlope) {
			// La recta es horizontal = grade
			if(segment1.getPointType()==PointType.GRADE) {
				// Si el siguiente es recta, los uno en uno solo
				segment1.setStart(0);
				resultIntervalArray.remove(0);
			} else {
				// Si el siguiente es VC, el primero lo convierto en recta
				resultIntervalArray.get(0).setPointType(PointType.GRADE);
			}
		} else {
			// Caso aproxima mejor la parábola
			if(segment1.getPointType()==PointType.GRADE) {
				// Si el siguiente es recta, el primero lo convierto en VC
				resultIntervalArray.get(0).setPointType(PointType.VERTICAL_CURVE);
			} else {
				// Si el siguiente es VC las uno en una sola 
				// TODO Quizás habría que comprobar si aproxima mejor con una sola o con dos independientes
				segment1.setStart(0);
				resultIntervalArray.remove(0);
			}
		}
	}
	private void processLastSegmentAsBorder() {
		TypeInterval lastsegment = resultIntervalArray.getLast();
		int lastindex = resultIntervalArray.size()-1;
		TypeInterval previous = resultIntervalArray.get(lastindex - 1);
		if(lastsegment.size()<2) {
			// Si tiene menos de dos puntos se lo asigno al siguiente
			previous.setEnd(lastsegment.getEnd());
			resultIntervalArray.remove(lastindex);
			return;
		}
		
		int first = lastsegment.getStart();
		double[] rr = originalGradePoints.rectaAnteriorEqualArea(first, lastindex);
		if(Math.abs(rr[1])<=thresholdSlope) {
			// La recta es horizontal = grade
			if(previous.getPointType()==PointType.GRADE) {
				// Si el anterior es recta, los uno en uno solo
				previous.setEnd(lastsegment.getEnd());
				resultIntervalArray.remove(lastindex);
			} else {
				// Si el anterior es VC, el último lo convierto en recta
				resultIntervalArray.get(lastindex).setPointType(PointType.GRADE);
			}
		} else {
			// Caso aproxima mejor la parábola
			if(previous.getPointType()==PointType.GRADE) {
				// Si el anterior es recta, el último lo convierto en VC
				resultIntervalArray.get(lastindex).setPointType(PointType.VERTICAL_CURVE);
			} else {
				// Si el anterior es VC las uno en una sola 
				// TODO Quizás habría que comprobar si aproxima mejor con una sola o con dos independientes
				previous.setEnd(lastsegment.getEnd());
				resultIntervalArray.remove(lastindex);
			}
		}
	}
	/**
	 * Procesa los intervalos BorderPoint con un solo punto
	 * El método modifica el resultIntervalArray que recibe como parámetro
	 * @param originalIntervalArray Array de TypeIntervals original
	 * @param i Indice del TypeInterval que se está procesando
	 * @param resultIntervalArray El resultIntervalArray que va resultando durante todo el procesamiento
	 * @return resultIntervalArray modificado
	 */
	private TypeIntervalArray processBorderIntervalWithOnePoint(TypeIntervalArray originalIntervalArray, int i, TypeIntervalArray resultIntervalArray) {
		TypeInterval currentSegment = originalIntervalArray.get(i);
		TypeInterval lastSegmentAdded = resultIntervalArray.getLast(); 
		lastSegmentAdded.setEnd(currentSegment.getStart());
		TypeInterval followingSegment = originalIntervalArray.get(i+1);
		resultIntervalArray.add(followingSegment);
		lastSegmentAdded = resultIntervalArray.getLast();
		lastSegmentAdded.setStart(currentSegment.getEnd());
		return resultIntervalArray;
	}
	private void processBorderIntervalWithMoreThanOnePoint(XYVectorFunction originalGradePoints, TypeIntervalArray originalIntervalArray, int index, TypeIntervalArray resultIntervalArray) {
		TypeInterval currentBorderSegment = originalIntervalArray.get(index);
		TypeInterval followingSegment = originalIntervalArray.get(index+1);
		
		// Si el border segment tiene más de un punto busco
		// el que mejor ajuste da por ecm y prolongo 
		// los segmentos anterior y posterior hasta él
		//TypeInterval previousSegment = originalIntervalArray.get(index-1).copy();
		TypeInterval previousSegment = resultIntervalArray.getLast();
		XYVectorFunction originalpoints = originalGradePoints.subList(previousSegment.getStart(), followingSegment.getEnd());
		double[] originalY = originalpoints.getYValues();
				
		double ecmmin = -1.0;
		int ecmmin_index = -1;
		for(int i=currentBorderSegment.getStart(); i<=currentBorderSegment.getEnd(); i++) {
			XYVectorFunction adjustedPoints = new XYVectorFunction();
			
			double[] r1 = originalGradePoints.rectaAnteriorEqualArea(previousSegment.getStart(), i);
			double[] r2 = originalGradePoints.rectaPosteriorEqualArea(i, followingSegment.getEnd());
						
			// Genero las coordenadas de los puntos ajustados
			for(int j=previousSegment.getStart(); j<i; j++) {
				double x = originalGradePoints.getX(j);
				double y = r1[0] + r1[1] * x;
				adjustedPoints.add(new double[]{x, y});
			}
			for(int j=i; j<=followingSegment.getEnd(); j++) {
				double x = originalGradePoints.getX(j);
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
		resultIntervalArray.getLast().setEnd(ecmmin_index);
		resultIntervalArray.add(followingSegment);
		resultIntervalArray.getLast().setStart(ecmmin_index);
	}
	@Override
	public TypeIntervalArray getOriginalTypeIntervalArray() {
		return originalIntervalArray;
	}
	@Override
	public TypeIntervalArray getResultTypeIntervalArray() {
		return resultIntervalArray;
	}

}
