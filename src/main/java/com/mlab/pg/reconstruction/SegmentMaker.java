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
	protected TypeIntervalArray originalTypeIntervalArray;
	
	/**
	 * Segmentación resultante después del procesamiento. Solo tiene tramos tipo Grade y VerticalCurve
	 */
	protected TypeIntervalArray resultTypeIntervalArray;
	
	PointCharacteriserStrategy strategy;
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
	public SegmentMaker(XYVectorFunction gradesample, int mobilebasesize, double thresholdslope, PointCharacteriserStrategy strategy) throws NullTypeException {
		this.originalGradePoints = gradesample;
		this.mobileBaseSize = mobilebasesize;
		this.thresholdSlope = thresholdslope;
		this.strategy = strategy;
		
		ProfileCharacteriser characteriser = new ProfileCharacteriser(strategy);
		this.originalPointTypes = characteriser.characterise(originalGradePoints, mobileBaseSize, thresholdSlope); 

		this.originalTypeIntervalArray = new TypeIntervalArray(originalPointTypes);
		
		if(!originalTypeIntervalArray.hasNullSegments()) {
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
		resultTypeIntervalArray = new TypeIntervalArray();
		
		for(int i=0; i<originalTypeIntervalArray.size(); i++) {

			TypeInterval currentSegment = originalTypeIntervalArray.get(i).copy();
			
			// El primer y último segmento los añado como están
			// Más adelante, si son Border, los procesaré
			if(i==0 || i==originalTypeIntervalArray.size()-1) {
				resultTypeIntervalArray.add(currentSegment);
				continue;
			}
			
			if(currentSegment.getPointType()==PointType.BORDER_POINT) {
				if(currentSegment.size() == 1) {	
					// Si el border segment tiene solo un punto, lo añado al final del previousSegment y al inicio del followingSegment
					processBorderSegmentWithOnePoint(i);
				} else {
					// Si el BorderSegment tiene más de un punto hay que seleccionar el punto Border que mejor aproxima
					processBorderSegmentWithMoreThanOnePoint(i);	
				}
				i++;				
			} else {
				// Los segmentos que no son BORDER los añado como están
				resultTypeIntervalArray.add(currentSegment);
			}
		}
		// Si el primer segmento ha quedado del tipo BORDER, le asigno del tipo del siguiente
		TypeInterval firstInterval = resultTypeIntervalArray.get(0); 
		if(firstInterval.getPointType() == PointType.BORDER_POINT && firstInterval.size()>1 ) {
			processFirstSegmentAsBorder();
		}
		
		// Si el último segmento ha quedado del tipo BORDER, le asigno el tipo del anterior
		int last = resultTypeIntervalArray.size()-1;
		if(resultTypeIntervalArray.get(last).getPointType() == PointType.BORDER_POINT) {
			resultTypeIntervalArray.get(last-1).setEnd(resultTypeIntervalArray.get(last).getEnd()-1);
			//resultTypeIntervalArray.remove(last);
		}
	}
	private void processFirstSegmentAsBorder() {
		if(resultTypeIntervalArray.get(0).size()<4) {
			// Si tiene menos de cuatro puntos se lo asigno al siguiente
			resultTypeIntervalArray.get(1).setStart(1);
			resultTypeIntervalArray.remove(0);
			return;
		}
		
		int last = resultTypeIntervalArray.get(0).getEnd();
		double[][] origvalues = originalGradePoints.getValuesAsArray(new IntegerInterval(0, last));
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
				nextSegment.setStart(1);
				resultTypeIntervalArray.remove(0);
			}
		}
	}
	private void processBorderSegmentWithOnePoint(int i) {
		TypeInterval currentSegment = originalTypeIntervalArray.get(i);
		TypeInterval lastSegmentAdded = resultTypeIntervalArray.get(resultTypeIntervalArray.size()-1); 
		lastSegmentAdded.setEnd(currentSegment.getStart());
		TypeInterval followingSegment = originalTypeIntervalArray.get(i+1).copy();
		resultTypeIntervalArray.add(followingSegment);
		lastSegmentAdded = resultTypeIntervalArray.get(resultTypeIntervalArray.size()-1);
		lastSegmentAdded.setStart(currentSegment.getEnd());
	}
	private void processBorderSegmentWithMoreThanOnePoint(int index) {
		TypeInterval currentBorderSegment = originalTypeIntervalArray.get(index).copy();
		int startOfCurrentBorderSegment = currentBorderSegment.getStart();
		int endOfCurrentBorderSegment = currentBorderSegment.getEnd();
		TypeInterval followingSegment = originalTypeIntervalArray.get(index+1).copy();
		int endOfFollowingSegment = followingSegment.getEnd();
		//PointTypeSegment lastProcessedSegment = processedSegments.get(processedSegments.size()-1);
		
		// Si el border segment tiene más de un punto busco
		// el que mejor ajuste da por ecm y prolongo 
		// los segmentos anterior y posterior hasta él
		TypeInterval previousSegment = originalTypeIntervalArray.get(index-1).copy();
		int startOfPreviousSegment = previousSegment.getStart();
		XYVectorFunction originalpoints = originalGradePoints.subList(startOfPreviousSegment, endOfFollowingSegment);
		double[] originalY = originalpoints.getYValues();
				
		double ecmmin = -1.0;
		int ecmmin_index = -1;
		for(int i=startOfCurrentBorderSegment; i<=endOfCurrentBorderSegment; i++) {
			XYVectorFunction adjustedPoints = new XYVectorFunction();
			
			double[] r1 = originalGradePoints.rectaMinimosCuadrados(startOfPreviousSegment, i);
			
			double[] r2 = originalGradePoints.rectaMinimosCuadrados(i, endOfFollowingSegment);
			
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
		resultTypeIntervalArray.get(resultTypeIntervalArray.size()-1).setEnd(ecmmin_index);
		resultTypeIntervalArray.add(followingSegment.copy());
		resultTypeIntervalArray.get(resultTypeIntervalArray.size()-1).setStart(ecmmin_index);
	}
	
	
	// Getter -Setter
	public XYVectorFunction getOriginalGradePoints() {
		return originalGradePoints;
	}

	public PointTypeArray getOriginalPointTypes() {
		return originalPointTypes;
	}

	public TypeIntervalArray getOriginalTypeSegmentArray() {
		return originalTypeIntervalArray;
	}
	public TypeIntervalArray getResultTypeSegmentArray() {
		return resultTypeIntervalArray;
	}
	
	public int getMobileBaseSize() {
		return mobileBaseSize;
	}

	public double getThresholdSlope() {
		return thresholdSlope;
	}
	
	
	
	
	
}
