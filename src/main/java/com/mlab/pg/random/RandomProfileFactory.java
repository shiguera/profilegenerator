package com.mlab.pg.random;

import java.util.Random;

import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.VerticalCurveAlignment;
import com.mlab.pg.valign.VerticalProfile;

/**
 * Genera perfiles longitudinales aleatorios de los tipos I, II, II y IV de la norma AASHTO
 * - Tipo I    : upgrade - crest curve - down grade
 * - Tipo II a : up grade - crest curve - up grade
 * - Tipo II b : down grade - crest curve - down grade
 * - Tipo III  : down grade - sag curve - up grade
 * - Tipo IV a : down grade - sag curve - down grade
 * - Tipo IV b : up grade - sag curve - up grade
 *  
 *  Los parámetros aleatorios de las alineaciones se distribuyen uniformemente entre 
 *  los valores máximos y mñinimos fijados
 *   
 * @author shiguera
 *
 */
public class RandomProfileFactory {

	/**
	 * Parámetros para la generación de las alineaciones aleatorias
	 */
	protected double s0 = 0.0;
	protected double z0 = 1000.0;
	protected double minSlope = 0.005;
	protected double maxSlope = 0.1;
	protected double slopeIncrement = 0.005;
	protected double minGradeLength = 50.0;
	protected double maxGradeLength = 1500.0;
	protected double gradeLengthIncrement = 50.0;
	protected double minVerticalCurveLength = 150.0;
	protected double maxVerticalCurveLength = 1500.0;
	protected double verticalCurveLengthIncrement = 50.0;
	protected double maxKv = 60000.0;
	protected double minKv = 200.0;
	
	/**
	 * Random
	 */
	protected Random random;
	
	// Constructor: Adopta todos los parámetros por defecto
	public RandomProfileFactory() {
		random = new Random();
	}
	
	// Métodos generadores de los perfiles
	public VerticalProfile randomProfileType_I() {
		
		VerticalProfile vp = new VerticalProfile();
		
		// Generar alineacion grade de entrada
		GradeAlignment grade1 = randomUpGrade(s0, z0);
		vp.add(grade1);
		
		// Pendiente de salida de la vertical curve, pendiente de la segunda grade
		double g2 = -RandomGradeFactory.randomUniformGradeSlope(minSlope, maxSlope, slopeIncrement);
		
		// Generar vertical curve
		double s1 = grade1.getEndS();
		double z1 = grade1.getEndZ();
		double g1 = grade1.getSlope();
		VerticalCurveAlignment vc = randomVerticalCurve(s1, z1, g1, g2);
		vp.add(vc);
	
		// Generar alineación grade de salida
		double s2 = vc.getEndS();
		double z2 = vc.getEndZ();
		GradeAlignment grade2 = randomGrade(s2, z2, g2);
		vp.add(grade2);
		
		return vp;
	}
	
	public GradeAlignment randomUpGrade(double starts, double startz) {
		//LOG.debug("randomUpGrade()");
		double g1 = RandomGradeFactory.randomUniformGradeSlope(minSlope, maxSlope, slopeIncrement);
		double grade1Length = RandomFactory.randomUniformLength(minGradeLength, maxGradeLength, gradeLengthIncrement);
		GradeAlignment grade1 = new GradeAlignment(starts, startz, g1, grade1Length);
		return grade1;
	}
	
	public VerticalCurveAlignment randomVerticalCurve(double s1, double z1, double g1, double g2) {
		//LOG.debug("randomVerticalCurve()");
		double Kv=maxKv+1;
		double verticalCurveLength = 0.0;
		while(Math.abs(Kv)>maxKv) {
			verticalCurveLength = RandomFactory.randomUniformLength(minVerticalCurveLength, maxVerticalCurveLength, verticalCurveLengthIncrement);
			double theta = g2 - g1;
			Kv = verticalCurveLength / theta;	
			//LOG.debug("verticalCurveLength= " + verticalCurveLength);
		}
		double s2 = s1 + verticalCurveLength;
		VerticalCurveAlignment verticalCurve = new VerticalCurveAlignment(s1, z1, g1, Kv, s2);
		return verticalCurve;
	}
	
	public GradeAlignment randomGrade(double starts, double startz, double slope) {
		//LOG.debug("randomGrade()");
		double grade2Length = RandomFactory.randomUniformLength(minGradeLength, maxGradeLength, gradeLengthIncrement);
		GradeAlignment grade2 = new GradeAlignment(starts, startz, slope, grade2Length);
		return grade2;
	}
	
	// Getters and setters
	public double getS0() {
		return s0;
	}
	public void setS0(double s0) {
		this.s0 = s0;
	}
	public double getZ0() {
		return z0;
	}
	public void setZ0(double z0) {
		this.z0 = z0;
	}
	public double getMinSlope() {
		return minSlope;
	}
	public void setMinSlope(double minGrade) {
		this.minSlope = minGrade;
	}
	public double getMaxSlope() {
		return maxSlope;
	}
	public void setMaxSlope(double maxGrade) {
		this.maxSlope = maxGrade;
	}
	public double getSlopeIncrement() {
		return slopeIncrement;
	}
	public void setSlopeIncrement(double gradeIncrement) {
		this.slopeIncrement = gradeIncrement;
	}
	public double getMinGradeLength() {
		return minGradeLength;
	}
	public void setMinGradeLength(double minGradeLength) {
		this.minGradeLength = minGradeLength;
	}
	public double getMaxGradeLength() {
		return maxGradeLength;
	}
	public void setMaxGradeLength(double maxGradeLength) {
		this.maxGradeLength = maxGradeLength;
	}
	public double getGradeLengthIncrement() {
		return gradeLengthIncrement;
	}
	public void setGradeLengthIncrement(double gradeLengthIncrement) {
		this.gradeLengthIncrement = gradeLengthIncrement;
	}
	public double getMinVerticalCurveLength() {
		return minVerticalCurveLength;
	}
	public void setMinVerticalCurveLength(double minVerticalCurveLength) {
		this.minVerticalCurveLength = minVerticalCurveLength;
	}
	public double getMaxVerticalCurveLength() {
		return maxVerticalCurveLength;
	}
	public void setMaxVerticalCurveLength(double maxVerticalCurveLength) {
		this.maxVerticalCurveLength = maxVerticalCurveLength;
	}
	public double getVerticalCurveLengthIncrement() {
		return verticalCurveLengthIncrement;
	}
	public void setVerticalCurveLengthIncrement(double verticalCurveLengthIncrement) {
		this.verticalCurveLengthIncrement = verticalCurveLengthIncrement;
	}
	public double getMaxKv() {
		return maxKv;
	}
	public void setMaxKv(double maxKv) {
		this.maxKv = maxKv;
	}
	public double getMinKv() {
		return minKv;
	}

	public void setMinKv(double minKv) {
		this.minKv = minKv;
	}

	
	
}
