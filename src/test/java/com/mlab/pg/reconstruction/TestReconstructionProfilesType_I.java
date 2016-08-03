package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.random.RandomFactory;
import com.mlab.pg.random.RandomGradeFactory;
import com.mlab.pg.util.MathUtil;
import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.VerticalCurveAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class TestReconstructionProfilesType_I {
	
	private static Logger LOG = Logger.getLogger(TestReconstructionProfilesType_I.class);
	
	int numberOfEssays = 10000;
	int currentEssay;
	int mobileBaseSize = 3;
	double thresholdSlope = 1e-5;
	boolean displayProfiles = false;
	
	
	double minGrade = 0.005;
	double maxGrade = 0.1;
	double gradeIncrement = 0.005;
	double minGradeLength = 50.0;
	double maxGradeLength = 1500.0;
	double gradeLengthIncrement = 50.0;
	double minVerticalCurveLength = 150.0;
	double maxVerticalCurveLength = 1500.0;
	double verticalCurveLengthIncrement = 50.0;
	double maxKv = 60000.0;
	
	/**
	 * Pendiente de la grade de entrada
	 */
	double g1 = 0.0;
	/**
	 * Pendiente de la grade de salida
	 */
	double g2 = 0.0;
	/**
	 * Longitud de la crest curve
	 */
	double verticalCurveLength, grade1Length, grade2Length;
	
	/**
	 * Abscisas y ordenadas de los puntos iniciales y finales de las alineaciones
	 */
	double s0 = 0.0;
	double z0 = 1000.0;
	double s1, z1, s2, z2, s3, z3;
	/**
	 * Parámetro del acuerdo
	 */
	double Kv;
	/**
	 * Diferencia entre pendientes de entrada y salida de la vertical curve
	 */
	double theta;
	
	/**
	 * Errores cuadráticos medios cometidos
	 */
	
	VerticalProfile originalVerticalProfile, resultVerticalProfile;
	VerticalGradeProfile originalGradeProfile, resultGradeProfile;
	XYVectorFunction originalGradePoints, resultGradePoints, originalVerticalProfilePoints, resultVerticalProfilePoints;
	
	double[] ecm;
	double maxEcm, minEcm;
	
	/**
	 * Separación entre puntos de la muestra del perfil de pendientes
	 */
	double pointSeparation = 5.0;
	
	GradeAlignment originalGrade1, originalGrade2;
	VerticalCurveAlignment originalVerticalCurve;
	
	@BeforeClass
	public static void beforeClass() {
		PropertyConfigurator.configure("log4j.properties");	
	
	}
	
	@Test
	public void test() {
		LOG.debug("test()");
		
		ecm = new double[numberOfEssays];
		
		for(currentEssay=0; currentEssay< numberOfEssays; currentEssay++) {
			generateOriginalVerticalProfile();
			if(displayProfiles) {
				System.out.println(originalVerticalProfile);
			}

			generateOriginalGradeProfile();
			
			generateGradeSample();
			
			doGradeProfileReconstruction();
			if(displayProfiles) {
				System.out.println(resultVerticalProfile);
			}
			
			measureErrors();
		}

		showReport();
	}
	
	private void generateOriginalVerticalProfile() {
		//LOG.debug("generateVerticalProfile()");
		
		generateG1G2();
		generateLengths();
		
		originalVerticalProfile = new VerticalProfile();

		calculateFirstGrade();
		originalVerticalProfile.add(originalGrade1);
		
		calculateVerticalCurve();
		originalVerticalProfile.add(originalVerticalCurve);
		
		calculateSecondGrade();
		originalVerticalProfile.add(originalGrade2);
		
		// System.out.println(originalVerticalProfile);
	}
	private void generateG1G2() {
		//LOG.debug("generateG1G2()");
		g1 = RandomGradeFactory.randomUniformGradeSlope(minGrade, maxGrade, gradeIncrement);
		//LOG.debug("g1= " + g1);
		g2 = -RandomGradeFactory.randomUniformGradeSlope(minGrade, maxGrade, gradeIncrement);
		//LOG.debug("g2= " + g2);
	}
	private void generateLengths() {
		//LOG.debug("generateLengths()");
		grade1Length = RandomFactory.randomUniformLength(minGradeLength, maxGradeLength, gradeLengthIncrement);
		//LOG.debug("grade1Length= " + grade1Length);
		grade2Length = RandomFactory.randomUniformLength(minGradeLength, maxGradeLength, gradeLengthIncrement);
		//LOG.debug("grade2Length= " + grade2Length);
		Kv=maxKv+1;
		while(Math.abs(Kv)>maxKv) {
			verticalCurveLength = RandomFactory.randomUniformLength(minVerticalCurveLength, maxVerticalCurveLength, verticalCurveLengthIncrement);
			theta = g2 - g1;
			Kv = verticalCurveLength / theta;	
			//LOG.debug("verticalCurveLength= " + verticalCurveLength);
		}
	}

	private void calculateFirstGrade() {
		//LOG.debug("calculateFirstGrade()");
		s1 = s0 + grade1Length;
		originalGrade1 = new GradeAlignment(s0, z0, g1, grade1Length);
		z1 = originalGrade1.getEndZ();
	}
	private void calculateVerticalCurve() {
		//LOG.debug("calculateVerticalCurve()");
		s2 = s1 + verticalCurveLength;
		originalVerticalCurve = new VerticalCurveAlignment(s1, z1, g1, Kv, s2);
		z2 = originalVerticalCurve.getEndZ();
	}
	private void calculateSecondGrade() {
		//LOG.debug("calculateSecondGrade()");
		s3 = s2 + grade2Length;
		originalGrade2 = new GradeAlignment(s2, z2, g2, grade2Length);
		z3 = originalGrade2.getEndZ();
	}
	private void generateOriginalGradeProfile() {
		//LOG.debug("generateVerticalGradeProfile()");
		originalGradeProfile = originalVerticalProfile.derivative();
		//System.out.println(originalGradeProfile);
	}
	private void generateGradeSample() {
		//LOG.debug("generateGradeSample()");
		originalGradePoints = originalGradeProfile.getSample(s0, s3, pointSeparation, true);
		//LOG.debug("Muestra de pendientes: sep = " + pointSeparation + ", points = " + originalGradePoints.size());
	}
	private void doGradeProfileReconstruction() {
		//LOG.debug("doGradeProfileReconstruction()");
		GradeProfileGenerator generator = new GradeProfileGenerator(originalGradePoints, mobileBaseSize, thresholdSlope, z0);
		resultGradeProfile = generator.getGradeProfile();
		resultGradePoints = resultGradeProfile.getSample(s0,  z0, pointSeparation, true);
		resultVerticalProfile = generator.getVerticalProfile();
	}
	
	private void measureErrors() {
		//LOG.debug("measureErrors()");
		resultVerticalProfilePoints = resultVerticalProfile.getSample(s0, s3, pointSeparation, true);
		originalVerticalProfilePoints = originalVerticalProfile.getSample(s0, s3, pointSeparation, true);		
		double currentEcm = MathUtil.ecm(originalVerticalProfilePoints.getYValues(), resultVerticalProfilePoints.getYValues());
		if(currentEssay==0) {
			maxEcm = currentEcm;
			minEcm = currentEcm;
		} else {
			if (currentEcm > maxEcm) {
				maxEcm = currentEcm;
			}
			if(currentEcm < minEcm) {
				minEcm = currentEcm;
			}
		}
		ecm[currentEssay] = currentEcm;
		if(currentEcm>0.01) {
			System.out.println(originalVerticalProfile);
			System.out.println(resultVerticalProfile);
		}
	}
	private void showReport() {
		// LOG.debug("showError()");
		System.out.println("Número de ensayos: " + numberOfEssays);
		double meanecm = 0.0;
		for(int i=0; i<ecm.length;i++) {
			meanecm+=ecm[i];
		}
		meanecm = meanecm / ecm.length;
		System.out.println("mean ecm = " + meanecm);
		System.out.println("min ecm = " + minEcm);
		System.out.println("max ecm = " + maxEcm);
			
	}
	

}
