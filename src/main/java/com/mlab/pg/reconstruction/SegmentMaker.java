package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;

import com.mlab.pg.util.MathUtil;
import com.mlab.pg.xyfunction.IntegerInterval;
import com.mlab.pg.xyfunction.XYVectorFunction;

/** 
 * A partir de un perfil de pendientes en la forma de una XYVectorFunction, un tamaño
 * de la mobileBaseSize y un valor límite para el thresholdSlope genera un PointTypeSegmentArray 
 * caracterizando los distintos segmentos que encuentra en el perfil de pendientes
 *  
 * @author shiguera
 *
 */
public class SegmentMaker {
	
	Logger LOG = Logger.getLogger(SegmentMaker.class);

	XYVectorFunction originalGradePoints;
	int mobileBaseSize;
	double thresholdSlope;
	PointTypeArray originalPointTypes;
	PointTypeSegmentArray originalSegments;
	
	XYVectorFunction processedGradePoints;
	PointTypeArray processedPointTypes;
	PointTypeSegmentArray processedSegments;
	
	
	public SegmentMaker(XYVectorFunction gradesample, int mobilebasesize, double thresholdslope) {
		this.originalGradePoints = gradesample;
		this.mobileBaseSize = mobilebasesize;
		this.thresholdSlope = thresholdslope;
		ProfileCharacteriser characteriser = new ProfileCharacteriser();
		this.originalPointTypes = characteriser.characterise(originalGradePoints, mobileBaseSize, thresholdSlope); 
		this.originalSegments = new PointTypeSegmentArray(originalPointTypes);
	}

	public void processBorderPoints() throws CloneNotSupportedException {
		processedGradePoints = new XYVectorFunction();
		processedPointTypes = new PointTypeArray();
		processedSegments = new PointTypeSegmentArray();
		
		for(int i=0; i<originalSegments.size(); i++) {
			// El primer y último segmento los añado como están (deberían ser NULL)
			if(i==0 || i==originalSegments.size()-1) {
				processedSegments.add(originalSegments.get(i).clone());
				continue;
			}
			PointTypeSegment currentSegment = originalSegments.get(i);
			// Los segmentos que no son Border los añado como están
			if(currentSegment.getPointType()==PointType.NULL || currentSegment.getPointType()==PointType.GRADE ||
					currentSegment.getPointType()==PointType.VERTICAL_CURVE) {
				processedSegments.add(currentSegment.clone());
				continue;
			}
			// Proceso los segmentos enmarcados entre Grade y VC
			PointTypeSegment previousSegment = originalSegments.get(i-1);
			PointTypeSegment followingSegment = originalSegments.get(i+1);
			if(currentSegment.getPointType()==PointType.BORDER_POINT && 
					previousSegment.getPointType() != PointType.NULL && 
					followingSegment.getPointType() != PointType.NULL) {
					processCurrentBorderSegment(i);
					i++;
			} else {
				processedSegments.add(originalSegments.get(i).clone());				
			}
		}
	}
	
	private void processCurrentBorderSegment(int index) {
		PointTypeSegment previousSegment = originalSegments.get(index-1);
		int start = previousSegment.getStart();
		PointTypeSegment borderSegment = originalSegments.get(index);
		int firstBorder = borderSegment.getStart();
		int lastBorder = borderSegment.getEnd();
		PointTypeSegment followingSegment = originalSegments.get(index+1);
		int end = followingSegment.getEnd();
		XYVectorFunction originalpoints = originalGradePoints.subList(start, end+1);
		double[] originalY = originalpoints.getYValues();
		
		// Si el border segment tiene solo un punto, lo añado al final del previous y al inicio del following
		if(borderSegment.size() == 1) {
			processedSegments.get(processedSegments.size()-1).setEnd(borderSegment.getStart());
			processedSegments.add(followingSegment);
			processedSegments.get(processedSegments.size()-1).setStart(borderSegment.getEnd());
			return;
		}
		// Si el border segment tiene más de un punto buscar por ecm
		double ecmmin = -1.0;
		int ecmmin_index = -1;
		for(int i=firstBorder; i<=lastBorder; i++) {
			XYVectorFunction adjustedPoints = new XYVectorFunction();
			double[][] coords_r1 = originalGradePoints.getValuesAsArray(new IntegerInterval(start,i));
			double[] r1 = MathUtil.rectaMinimosCuadrados(coords_r1);
			
			double[][] coords_r2 = originalGradePoints.getValuesAsArray(new IntegerInterval(i, end));
			double[] r2 = MathUtil.rectaMinimosCuadrados(coords_r2);
			
			// Hay que ajustarlas en el último punto. Las desplazo paralelamente
			// a la ordenada media
			double xcomun = originalGradePoints.getX(i);
			double y1 = r1[0] + r1[1] * xcomun;
			double y2 = r2[0] + r2[1] * xcomun;
			double ycomun = (y1 + y2)/2.0;
			double pdte1 = r1[1];
			r1 = MathUtil.rectaPtoPendiente(xcomun, ycomun, pdte1);
			double pdte2 = r2[1];
			r2 = MathUtil.rectaPtoPendiente(xcomun, ycomun, pdte2);
			// Genero las coordenadas de los puntos ajustados
			for(int j=start; j<i; j++) {
				double x = originalGradePoints.getX(j);
				double y = r1[0] + r1[1] * x;
				adjustedPoints.add(new double[]{x, y});
			}
			for(int j=i; j<=end; j++) {
				double x = originalGradePoints.getX(j);
				double y = r2[0] + r2[1] * x;
				adjustedPoints.add(new double[]{x, y});
			}
			// Calculo el ecm
			double[] adjustedY = adjustedPoints.getYValues();
			double ecm = MathUtil.ecm(originalY, adjustedY);
			//LOG.debug(ecm);
			if(ecmmin==-1.0) {
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
		processedSegments.get(processedSegments.size()-1).setEnd(ecmmin_index);
		processedSegments.add(followingSegment);
		processedSegments.get(processedSegments.size()-1).setStart(ecmmin_index);
	}
	
	
	/**
	 * Procesa las sucesiones de segmentos y cuando se encuentra una
	 * sucesión VC - VCVC - VCE - G añade el segmento VCVC al segmento VC
	 */
	public void processVerticalCurveEndings() {
//		PointTypeSegmentArray newPointTypeSegments = new PointTypeSegmentArray();
//		int lastsegment = pointTypeSegments.size() -1;
//		int lastsegmentadded = -1;
//		for(int i=0; i<= lastsegment-3; i++) {
//			if(pointTypeSegments.get(i).getPointType()==PointType.VERTICAL_CURVE && 
//					pointTypeSegments.get(i+1).getPointType()==PointType.VERTICALCURVE_TO_VERTICALCURVE &&
//					pointTypeSegments.get(i+2).getPointType()==PointType.VERTICALCURVE_END &&
//					pointTypeSegments.get(i+3).getPointType()==PointType.GRADE) {
//				// Convierte en VerticalCurve el segmento VerticalCurveToVerticalCurve
//				//int firstpoint = pointTypeSegments.get(i+2).getStart();
//				int lastPoint = pointTypeSegments.get(i+2).getEnd();
//				newPointTypeSegments.add(pointTypeSegments.get(i));
//				newPointTypeSegments.add(pointTypeSegments.get(i+2));
//				newPointTypeSegments.add(pointTypeSegments.get(i+3));				
//				newPointTypeSegments.get(newPointTypeSegments.size()-3).setEnd(lastPoint);
//				i = i+3;
//			} else {
//				newPointTypeSegments.add(pointTypeSegments.get(i));
//			}
//			lastsegmentadded = i;
//		}
//		if (lastsegmentadded < lastsegment) {
//			for(int i=lastsegmentadded + 1; i<=lastsegment; i++) {
//				newPointTypeSegments.add(pointTypeSegments.get(i));
//			}			
//		}
//		pointTypeSegments = newPointTypeSegments.clone();
	}
	/**
	 * Procesa las sucesiones de segmentos y cuando se encuentra una
	 * sucesión G-VCB-VCVC-VC añade el segmento VCVC al segmento VC
	 */
	public void processVerticalCurveBeginnings() {
//		PointTypeSegmentArray newPointTypeSegments = new PointTypeSegmentArray();
//		int lastsegment = pointTypeSegments.size() -1;
//		int lastsegmentadded = -1;
//		for(int i=0; i<= lastsegment-3; i++) {
//			if(pointTypeSegments.get(i).getPointType()==PointType.GRADE && 
//					pointTypeSegments.get(i+1).getPointType()==PointType.VERTICALCURVE_BEGINNING &&
//					pointTypeSegments.get(i+2).getPointType()==PointType.VERTICALCURVE_TO_VERTICALCURVE &&
//					pointTypeSegments.get(i+3).getPointType()==PointType.VERTICAL_CURVE) {
//				// Convierte en VerticalCurve el segmento VerticalCurveToVerticalCurve
//				int firstpoint = pointTypeSegments.get(i+2).getStart();
//				newPointTypeSegments.add(pointTypeSegments.get(i));
//				newPointTypeSegments.add(pointTypeSegments.get(i+1));
//				newPointTypeSegments.add(pointTypeSegments.get(i+3));				
//				newPointTypeSegments.get(newPointTypeSegments.size()-1).setStart(firstpoint);
//				i = i+3;
//			} else {
//				newPointTypeSegments.add(pointTypeSegments.get(i));
//			}
//			lastsegmentadded = i;
//		}
//		if (lastsegmentadded < lastsegment) {
//			for(int i=lastsegmentadded + 1; i<=lastsegment; i++) {
//				newPointTypeSegments.add(pointTypeSegments.get(i));
//			}			
//		}
//		pointTypeSegments = newPointTypeSegments.clone();
	}
	
	/**
	 * Procesa las ternas G - BVC - VC , asignando los puntos del tramo BVC
	 * a los tramos vecinos
	 */
	public void processThrees_G_BVC_VC() {
		
	}
	
	/**
	 * Procesa los tramos VCVC que están entre dos tramos VC 
	 * asignando sus puntos a los tramos VC vecinos con el criterio de
	 * minimos cuadrados.
	 */
	public void processVerticalCurveToVerticalCurveSegments() {
		
	}
	
	// Getter -Setter
	public XYVectorFunction getGradeSample() {
		return originalGradePoints;
	}

	public int getMobileBaseSize() {
		return mobileBaseSize;
	}

	public double getThresholdSlope() {
		return thresholdSlope;
	}

	public PointTypeArray getPointTypes() {
		return originalPointTypes;
	}

	public PointTypeSegmentArray getPointTypeSegments() {
		return originalSegments;
	}

	
	
	
	
	
}
