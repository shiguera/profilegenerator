package com.mlab.pg.reconstruction.strategy;

import com.mlab.pg.reconstruction.ParameterIntervalArray;
import com.mlab.pg.reconstruction.PointType;
import com.mlab.pg.reconstruction.PointTypeArray;
import com.mlab.pg.reconstruction.ProfileCharacteriser;
import com.mlab.pg.reconstruction.TypeInterval;
import com.mlab.pg.reconstruction.TypeIntervalArray;
import com.mlab.pg.util.MathUtil;
import com.mlab.pg.xyfunction.IntegerInterval;
import com.mlab.pg.xyfunction.Parabole;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class ProcessBorderIntervalsStrategy_EqualAreaMultiparameter implements ProcessBorderIntervalsStrategy {

	protected XYVectorFunction originalGradePoints;
	protected int baseSize;
	protected double thresholdSlope;
	protected PointCharacteriserStrategy pointCharacteriserStrategy;
	protected TypeIntervalArray originalIntervalArray;
	protected TypeIntervalArray resultIntervalArray;
	
	public ProcessBorderIntervalsStrategy_EqualAreaMultiparameter() {

	}

	@Override
	public TypeIntervalArray processBorderIntervals(XYVectorFunction originalgradepoints, int basesize,
			double thresholdslope, PointCharacteriserStrategy strategy) {
		this.originalGradePoints = originalgradepoints;
		this.baseSize = basesize;
		this.thresholdSlope = thresholdslope;
		this.pointCharacteriserStrategy = strategy;
		
		ProfileCharacteriser characteriser = new ProfileCharacteriser(pointCharacteriserStrategy);
		PointTypeArray originalPointTypes = characteriser.characterise(originalGradePoints, baseSize, thresholdSlope); 

		originalIntervalArray = new TypeIntervalArray(originalPointTypes);
		
		if(originalIntervalArray.hasNullSegments()) {
			return null;			
		} 

		resultIntervalArray = new TypeIntervalArray();
		
		for(int i=0; i<originalIntervalArray.size(); i++) {			
			// El primer y último segmento los añado como están
			// Más adelante, si son Border, los procesaré
			if(i==0 || i==originalIntervalArray.size()-1) {
				resultIntervalArray.add(originalIntervalArray.get(i));
				continue;
			}
			
			// Los segmentos que no son BORDER los añado como están
			if (originalIntervalArray.get(i).getPointType()!=PointType.BORDER_POINT) {
				resultIntervalArray.add(originalIntervalArray.get(i));
				continue;
			}
			
			// Los intervalos con puntos BorderPoint los proceso
			if(originalIntervalArray.get(i).getPointType()==PointType.BORDER_POINT) {
				if(originalIntervalArray.get(i).size() == 1) {	
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
			// Si el primer segmento ha quedado del tipo BORDER, le asigno del tipo del siguiente
			TypeInterval firstInterval = resultIntervalArray.get(0); 
			if(firstInterval.getPointType() == PointType.BORDER_POINT) {
				processFirstSegmentAsBorder();
			}
			
			// Si el último segmento ha quedado del tipo BORDER, le asigno el tipo del anterior
			int last = resultIntervalArray.size()-1;
			if(resultIntervalArray.get(last).getPointType() == PointType.BORDER_POINT) {
				resultIntervalArray.get(last-1).setEnd(resultIntervalArray.get(last).getEnd());
				resultIntervalArray.remove(last);
			}
		}
		return resultIntervalArray;
	}
	
	@Override
	public TypeIntervalArray processBorderIntervals(XYVectorFunction originalgradepoints,
			ParameterIntervalArray parameterarray, PointCharacteriserStrategy strategy) {
		// No utilizable en esta estrategia
		return null;
	}

	private void processFirstSegmentAsBorder() {
		if(resultIntervalArray.get(0).size()<4) {
			// Si tiene menos de cuatro puntos se lo asigno al siguiente
			resultIntervalArray.get(1).setStart(0);
			resultIntervalArray.remove(0);
			return;
		}
		
		int last = resultIntervalArray.get(0).getEnd();
		double[] rr = originalGradePoints.rectaPosteriorEqualArea(0, last);
		TypeInterval nextSegment =resultIntervalArray.get(1);
		if(rr[1]<=thresholdSlope) {
			// La recta es horizontal = grade
			if(nextSegment.getPointType()==PointType.GRADE) {
				// Si el siguiente es recta, los uno en uno solo
				nextSegment.setStart(0);
				resultIntervalArray.remove(0);
			} else {
				// Si el siguiente es VC, el primero lo convierto en recta
				resultIntervalArray.get(0).setPointType(PointType.GRADE);
			}
		} else {
			// Caso aproxima mejor la parábola
			if(nextSegment.getPointType()==PointType.GRADE) {
				// Si el siguiente es recta, el primero lo convierto en VC
				resultIntervalArray.get(0).setPointType(PointType.VERTICAL_CURVE);
			} else {
				// Si el siguiente es VC las uno en una sola 
				// TODO Quizás habría que comprobar si aproxima mejor con una sola o con dos independientes
				nextSegment.setStart(1);
				resultIntervalArray.remove(0);
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
		TypeInterval lastSegmentAdded = resultIntervalArray.get(resultIntervalArray.size()-1); 
		lastSegmentAdded.setEnd(currentSegment.getStart());
		TypeInterval followingSegment = originalIntervalArray.get(i+1).copy();
		resultIntervalArray.add(followingSegment);
		lastSegmentAdded = resultIntervalArray.get(resultIntervalArray.size()-1);
		lastSegmentAdded.setStart(currentSegment.getEnd());
		return resultIntervalArray;
	}
	private void processBorderIntervalWithMoreThanOnePoint(XYVectorFunction originalGradePoints, TypeIntervalArray originalIntervalArray, int index, TypeIntervalArray resultIntervalArray) {
		TypeInterval currentBorderSegment = originalIntervalArray.get(index).copy();
		TypeInterval followingSegment = originalIntervalArray.get(index+1).copy();
		
		// Si el border segment tiene más de un punto busco
		// el que mejor ajuste da por ecm y prolongo 
		// los segmentos anterior y posterior hasta él
		TypeInterval previousSegment = originalIntervalArray.get(index-1).copy();
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
		//LOG.debug("ecm = " + ecmmin);
		//LOG.debug("ecmmin_index = " + ecmmin_index);
		resultIntervalArray.getLast().setEnd(ecmmin_index);
		resultIntervalArray.add(followingSegment.copy());
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
