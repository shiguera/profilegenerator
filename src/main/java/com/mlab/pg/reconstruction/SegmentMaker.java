package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;

import com.mlab.pg.util.MathUtil;
import com.mlab.pg.xyfunction.IntegerInterval;
import com.mlab.pg.xyfunction.Parabole;
import com.mlab.pg.xyfunction.Straight;
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

	/**
	 * Resultado de la caracterización de los puntos originalGradePoints. Se calcula en el constructor.
	 * Tiene puntos tipo Grade, Border y VerticalCurve 
	 */
	protected PointTypeArray originalPointTypes;
	
	/**
	 * Segmentación obtenida a partir de los puntos caracterizados en originalPointTypes. Se calcula
	 * en el constructor. Tiene segmentos tipo Grade, Border y VerticalCurve
	 */
	protected Segmentation originalSegmentation;
	
	/**
	 * Segmentación resultante después del procesamiento. Solo tiene tramos tipo Grade y VerticalCurve
	 */
	protected Segmentation resultSegmentation;
	
	/**
	 * Construye una Segmentation a partir de una XYVectorFunction. La Segmentation resultado se
	 * puede consultar en getResultSegmentation() y solo tiene segmentos del tipo Grade o VerticalCurve.
	 * Si se generan segmentos NULL arroja una excepción
	 * 
	 * @param gradesample
	 * @param mobilebasesize
	 * @param thresholdslope
	 * @throws NullTypeException
	 */
	public SegmentMaker(XYVectorFunction gradesample, int mobilebasesize, double thresholdslope) throws NullTypeException {
		this.originalGradePoints = gradesample;
		this.mobileBaseSize = mobilebasesize;
		this.thresholdSlope = thresholdslope;
		
		ProfileCharacteriser characteriser = new ProfileCharacteriser();
		this.originalPointTypes = characteriser.characterise(originalGradePoints, mobileBaseSize, thresholdSlope); 

		this.originalSegmentation = new Segmentation(originalPointTypes);
		
		if(!originalSegmentation.hasNullSegments()) {
			processBorderSegments();			
		} else {
			throw(new NullTypeException());
		}
	}

	/**
	 * Procesa los segmentos de puntos tipo BorderPoint dejando solo segmentos
	 * del tipo Grade y del tipo VerticalCurve
	 * 
	 * @throws CloneNotSupportedException
	 */
	private void processBorderSegments() {
		resultSegmentation = new Segmentation();
		
		for(int i=0; i<originalSegmentation.size(); i++) {

			TypeInterval currentSegment = originalSegmentation.get(i).copy();
			
			// El primer y último segmento los añado como están
			// Más adelante, si son Border, los procesaré
			if(i==0 || i==originalSegmentation.size()-1) {
				resultSegmentation.add(currentSegment);
				continue;
			}
			
			if(currentSegment.getPointType()==PointType.BORDER_POINT) {
				if(currentSegment.size() == 1) {	
					// Si el border segment tiene solo un punto, lo añado al final del previousSegment y al inicio del followingSegment
					TypeInterval lastSegmentAdded = resultSegmentation.get(resultSegmentation.size()-1); 
					lastSegmentAdded.setEnd(currentSegment.getStart());
					TypeInterval followingSegment = originalSegmentation.get(i+1).copy();
					resultSegmentation.add(followingSegment);
					lastSegmentAdded = resultSegmentation.get(resultSegmentation.size()-1);
					lastSegmentAdded.setStart(currentSegment.getEnd());
				} else {
					processCurrentBorderSegment(i);	
				}
				i++;				
			} else {
				// Los segmentos que no son BORDER los añado como están
				resultSegmentation.add(currentSegment);
			}
		}
		// Si el primer segmento ha quedado del tipo BORDER, le asigno del tipo del siguiente
		if(resultSegmentation.get(0).getPointType() == PointType.BORDER_POINT) {
			processFirstSegmentAsBorder();
		}
		// Si el último segmento ha quedado del tipo BORDER, le asigno el tipo del anterior
		int last = resultSegmentation.size()-1;
		if(resultSegmentation.get(last).getPointType() == PointType.BORDER_POINT) {
			resultSegmentation.get(last-1).setEnd(resultSegmentation.get(last).getEnd());
			resultSegmentation.remove(last);
		}
	}
	private void processFirstSegmentAsBorder() {
		if(resultSegmentation.get(0).size()<4) {
			// Si tiene menos de cuatro puntos se lo asigno al siguiente
			resultSegmentation.get(1).setStart(0);
			resultSegmentation.remove(0);
			return;
		}
		int last = resultSegmentation.get(0).getEnd();
		double[][] origvalues = originalGradePoints.getValuesAsArray(new IntegerInterval(0, last));
		double[] rr = MathUtil.rectaMinimosCuadrados(origvalues);
		Straight r = new Straight(rr[0], rr[1]);
		double[] pp = MathUtil.parabolaMinimosCuadrados(origvalues);
		Parabole p = new Parabole(pp[0], pp[1], pp[2]);
		double ecmrecta = MathUtil.ecmPolynomToPoints(r, origvalues);
		double ecmparab = MathUtil.ecmPolynomToPoints(p, origvalues);
		TypeInterval nextSegment =resultSegmentation.get(1);
		if(ecmrecta<=ecmparab) {
			// Caso aproxima mejor la recta
			if(nextSegment.getPointType()==PointType.GRADE) {
				// Si el siguiente es recta, los uno en uno solo
				nextSegment.setStart(0);
				resultSegmentation.remove(0);
			} else {
				// Si el siguiente es VC, el primero lo convierto en recta
				resultSegmentation.get(0).setPointType(PointType.GRADE);
			}
		} else {
			// Caso aproxima mejor la parábola
			if(nextSegment.getPointType()==PointType.GRADE) {
				// Si el siguiente es recta, el primero lo convierto en VC
				resultSegmentation.get(0).setPointType(PointType.VERTICAL_CURVE);
			} else {
				// Si el siguiente es VC las uno en una sola 
				// TODO Quizás habría que comprobar si aproxima mejor con una sola o con dos independientes
				nextSegment.setStart(0);
				resultSegmentation.remove(0);
			}
		}
	}
	private void processCurrentBorderSegment(int index) {
		TypeInterval currentBorderSegment = originalSegmentation.get(index).copy();
		int startOfCurrentBorderSegment = currentBorderSegment.getStart();
		int endOfCurrentBorderSegment = currentBorderSegment.getEnd();
		TypeInterval followingSegment = originalSegmentation.get(index+1).copy();
		int endOfFollowingSegment = followingSegment.getEnd();
		//PointTypeSegment lastProcessedSegment = processedSegments.get(processedSegments.size()-1);
		
		// Si el border segment tiene más de un punto busco
		// el que mejor ajuste da por ecm y prolongo 
		// los segmentos anterior y posterior hasta él
		TypeInterval previousSegment = originalSegmentation.get(index-1).copy();
		int startOfPreviousSegment = previousSegment.getStart();
		XYVectorFunction originalpoints = originalGradePoints.subList(startOfPreviousSegment, endOfFollowingSegment+1);
		double[] originalY = originalpoints.getYValues();
				
		double ecmmin = -1.0;
		int ecmmin_index = -1;
		for(int i=startOfCurrentBorderSegment; i<=endOfCurrentBorderSegment; i++) {
			XYVectorFunction adjustedPoints = new XYVectorFunction();
			double[][] coords_r1 = originalGradePoints.getValuesAsArray(new IntegerInterval(startOfPreviousSegment,i));
			double[] r1 = MathUtil.rectaMinimosCuadrados(coords_r1);
			
			double[][] coords_r2 = originalGradePoints.getValuesAsArray(new IntegerInterval(i, endOfFollowingSegment));
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
			for(int j=startOfPreviousSegment; j<i; j++) {
				double x = originalGradePoints.getX(j);
				double y = r1[0] + r1[1] * x;
				adjustedPoints.add(new double[]{x, y});
			}
			for(int j=i; j<=endOfFollowingSegment; j++) {
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
		resultSegmentation.get(resultSegmentation.size()-1).setEnd(ecmmin_index);
		resultSegmentation.add(followingSegment.copy());
		resultSegmentation.get(resultSegmentation.size()-1).setStart(ecmmin_index);
	}
	
	
	// Getter -Setter
	public XYVectorFunction getOriginalGradePoints() {
		return originalGradePoints;
	}

	public PointTypeArray getOriginalPointTypes() {
		return originalPointTypes;
	}

	public Segmentation getOriginalSegmentation() {
		return originalSegmentation;
	}
	public Segmentation getResultSegmentation() {
		return resultSegmentation;
	}
	
	public int getMobileBaseSize() {
		return mobileBaseSize;
	}

	public double getThresholdSlope() {
		return thresholdSlope;
	}
	
	
	
	
	
}
