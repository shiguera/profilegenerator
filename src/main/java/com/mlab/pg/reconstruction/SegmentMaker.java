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

	/**
	 * Valores {si, gi} correspondientes a un perfil de pendientes que se quieren procesar.
	 * Se reciben como parámetro del constructor
	 */
	XYVectorFunction originalGradePoints;
	/**
	 * Tamaño de la base móvil que se utilizará para caracterizar los puntos del perfil de pendientes.
	 * Se recibe como parámetro en el constructor de la clase.
	 */
	int mobileBaseSize;
	/**
	 * Valor límite de la pendiente de las rectas que se considerarán como horizontales
	 * durante la caracterización de los puntos. Se recibe como parámetro en el constructor de la clase
	 */
	double thresholdSlope;

	/**
	 * Resultado de la caracterización de los puntos originalGradePoints. Se calcula en el constructor.
	 * Tiene puntos tipo Grade, Border y VerticalCurve 
	 */
	PointTypeArray originalPointTypes;
	
	/**
	 * Segmentación obtenida a partir de los puntos caracterizados en originalPointTypes. Se calcula
	 * en el constructor. Tiene segmentos tipo Grade, Border y VerticalCurve
	 */
	PointTypeSegmentArray originalSegments;
	
	/**
	 * Valores {Si, Gi} obtenidos después de procesar los puntos originales originalGradePoints
	 * en el método processBorderPoints(). Los valores de Si son los mismos que en los 
	 * puntos originales. Los valores de Gi se obtienen de las rectas que resultan después
	 * del proceso y que se deducen de la segmentación processedSegments
	 */
	XYVectorFunction processedGradePoints;
	/**
	 * Caracterización de los puntos después de procesarlos y dejar solo
	 * puntos del tipo Grade y VerticalCurve
	 */
	PointTypeArray processedPointTypes;
	/**
	 * Segmentación resultante después del procesamiento. Solo tiene tramos tipo Grade y VerticalCurve
	 */
	PointTypeSegmentArray processedSegments;
	
	
	public SegmentMaker(XYVectorFunction gradesample, int mobilebasesize, double thresholdslope) throws CloneNotSupportedException {
		this.originalGradePoints = gradesample;
		this.mobileBaseSize = mobilebasesize;
		this.thresholdSlope = thresholdslope;
		ProfileCharacteriser characteriser = new ProfileCharacteriser();
		this.originalPointTypes = characteriser.characterise(originalGradePoints, mobileBaseSize, thresholdSlope); 
		this.originalSegments = new PointTypeSegmentArray(originalPointTypes);

		processBorderSegments();
	}

	/**
	 * Procesa los segmentos de puntos tipo BorderPoint dejando solo segmentos
	 * del tipo Grade y del tipo VerticalCurve
	 * 
	 * @throws CloneNotSupportedException
	 */
	public void processBorderSegments() throws CloneNotSupportedException {
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
	
	
	// Getter -Setter
	public XYVectorFunction getOriginalGradePoints() {
		return originalGradePoints;
	}

	public PointTypeArray getOriginalPointTypes() {
		return originalPointTypes;
	}

	public PointTypeSegmentArray getOriginalSegments() {
		return originalSegments;
	}

	public XYVectorFunction getProcessedGradePoints() {
		return processedGradePoints;
	}

	public PointTypeArray getProcessedPointTypes() {
		return processedPointTypes;
	}

	public PointTypeSegmentArray getProcessedSegments() {
		return processedSegments;
	}
	
	public int getMobileBaseSize() {
		return mobileBaseSize;
	}

	public double getThresholdSlope() {
		return thresholdSlope;
	}
	
	
	
	
	
}
