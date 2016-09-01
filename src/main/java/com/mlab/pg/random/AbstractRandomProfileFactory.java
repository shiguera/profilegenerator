package com.mlab.pg.random;

import java.util.Random;

import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.VAlignFactory;
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
public abstract class AbstractRandomProfileFactory implements RandomProfileFactory {

	protected String factoryName = "";
	protected String description = "";
	
	/**
	 * Parámetros para la generación de las alineaciones aleatorias
	 */
	double s0 = 0.0;
	double z0 = 1000.0;
	double minSlope = 0.005;
	double maxSlope = 0.1;
	double slopeIncrement = 0.005;
	double minGradeLength = 100.0;
	double maxGradeLength = 1500.0;
	double gradeLengthIncrement = 100.0;
	double minVerticalCurveLength = 100.0;
	protected double maxVerticalCurveLength = 1500.0;
	protected double verticalCurveLengthIncrement = 50.0;
	protected double maxKv = 60000.0;
	protected double minKv = 200.0;
	
	/**
	 * Random
	 */
	protected Random random;
	
	// Constructor: Adopta todos los parámetros por defecto
	public AbstractRandomProfileFactory() {
		random = new Random();
	}
	
	
	
	public GradeAlignment randomUpGrade(double starts, double startz) {
		//LOG.debug("randomUpGrade()");
		double g1 = RandomGradeFactory.randomUniformGradeSlope(minSlope, maxSlope, slopeIncrement);
		double grade1Length = RandomFactory.randomUniformLength(minGradeLength, maxGradeLength, gradeLengthIncrement);
		GradeAlignment grade1 = new GradeAlignment(starts, startz, g1, grade1Length);
		return grade1;
	}
	public GradeAlignment randomDownGrade(double starts, double startz) {
		//LOG.debug("randomUpGrade()");
		double g1 = - RandomGradeFactory.randomUniformGradeSlope(minSlope, maxSlope, slopeIncrement);
		double grade1Length = RandomFactory.randomUniformLength(minGradeLength, maxGradeLength, gradeLengthIncrement);
		GradeAlignment grade1 = new GradeAlignment(starts, startz, g1, grade1Length);
		return grade1;
	}
	
	public VerticalCurveAlignment randomVerticalCurve(double s1, double z1, double g1, double g2) {
		//LOG.debug("randomVerticalCurve()");
		double Kv=maxKv+1;
		double verticalCurveLength = 0.0;
		while(Math.abs(Kv)>maxKv || Math.abs(Kv) < minKv) {
			verticalCurveLength = RandomFactory.randomUniformLength(minVerticalCurveLength, maxVerticalCurveLength, verticalCurveLengthIncrement);
			double theta = g2 - g1;
			Kv = Math.rint(verticalCurveLength / theta);	
			//LOG.debug("verticalCurveLength= " + verticalCurveLength);
		}
		// double s2 = s1 + verticalCurveLength;
		VerticalCurveAlignment verticalCurve = VAlignFactory.createVCFrom_PointGradeKvAndFinalSlope(s1, z1, g1, Kv, g2);
		return verticalCurve;
	}
	
	public GradeAlignment randomGrade(double starts, double startz, double slope) {
		//LOG.debug("randomGrade()");
		double grade2Length = RandomFactory.randomUniformLength(minGradeLength, maxGradeLength, gradeLengthIncrement);
		GradeAlignment grade2 = new GradeAlignment(starts, startz, slope, grade2Length);
		return grade2;
	}
	
	// Getters and setters
	@Override
	public String getFactoryName() {
		return factoryName;
	}
	@Override
	public String getDescription() {
		return description;
	}
	@Override
	public double getS0() {
		return s0;
	}
	@Override
	public void setS0(double s0) {
		this.s0 = s0;
	}
	@Override
	public double getZ0() {
		return z0;
	}
	@Override
	public void setZ0(double z0) {
		this.z0 = z0;
	}
	@Override
	public double getMinSlope() {
		return minSlope;
	}
	@Override
	public void setMinSlope(double minGrade) {
		this.minSlope = minGrade;
	}
	@Override
	public double getMaxSlope() {
		return maxSlope;
	}
	@Override
	public void setMaxSlope(double maxGrade) {
		this.maxSlope = maxGrade;
	}
	@Override
	public double getSlopeIncrement() {
		return slopeIncrement;
	}
	@Override
	public void setSlopeIncrement(double gradeIncrement) {
		this.slopeIncrement = gradeIncrement;
	}
	@Override
	public double getMinGradeLength() {
		return minGradeLength;
	}
	@Override
	public void setMinGradeLength(double minGradeLength) {
		this.minGradeLength = minGradeLength;
	}
	@Override
	public double getMaxGradeLength() {
		return maxGradeLength;
	}
	@Override
	public void setMaxGradeLength(double maxGradeLength) {
		this.maxGradeLength = maxGradeLength;
	}
	@Override
	public double getGradeLengthIncrement() {
		return gradeLengthIncrement;
	}
	@Override
	public void setGradeLengthIncrement(double gradeLengthIncrement) {
		this.gradeLengthIncrement = gradeLengthIncrement;
	}
	@Override
	public double getMinVerticalCurveLength() {
		return minVerticalCurveLength;
	}
	@Override
	public void setMinVerticalCurveLength(double minVerticalCurveLength) {
		this.minVerticalCurveLength = minVerticalCurveLength;
	}
	@Override
	public double getMaxVerticalCurveLength() {
		return maxVerticalCurveLength;
	}
	@Override
	public void setMaxVerticalCurveLength(double maxVerticalCurveLength) {
		this.maxVerticalCurveLength = maxVerticalCurveLength;
	}
	@Override
	public double getVerticalCurveLengthIncrement() {
		return verticalCurveLengthIncrement;
	}
	@Override
	public void setVerticalCurveLengthIncrement(double verticalCurveLengthIncrement) {
		this.verticalCurveLengthIncrement = verticalCurveLengthIncrement;
	}
	@Override
	public double getMaxKv() {
		return maxKv;
	}
	@Override
	public void setMaxKv(double maxKv) {
		this.maxKv = maxKv;
	}
	@Override
	public double getMinKv() {
		return minKv;
	}

	@Override
	public void setMinKv(double minKv) {
		this.minKv = minKv;
	}

	abstract public VerticalProfile createRandomProfile();

	
	
}
