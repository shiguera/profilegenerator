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

/**
 * Test para ensayos de perfiles tipo I : UpGrade - CrestCurve - DownGrade
 * 
 * @author shiguera
 *
 */
public class TestReconstructionProfilesType_I {
	
	private static Logger LOG = Logger.getLogger(TestReconstructionProfilesType_I.class);
	
	int numberOfEssays = 1000;
	int currentEssay;
	int mobileBaseSize = 3;
	double thresholdSlope = 1e-5;
	/**
	 * Separación entre puntos de la muestra del perfil de pendientes
	 */
	double pointSeparation = 7.0;
	boolean displayProfiles = true;
	
	/**
	 * Parámetros para la generación de las alineaciones aleatorias
	 */
	double s0 = 0.0;
	double z0 = 1000.0;
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
	 * Perfil longitudinal del tipo I generado aleatoriamente.
	 * Consta de una UpGrade, una CrestCurve y una DownGrade 
	 */
	VerticalProfile originalVerticalProfile; 
	/**
	 * Alineaciones que componen el perfil tipo I que se está ensayando
	 */
	GradeAlignment originalGrade1, originalGrade2;
	VerticalCurveAlignment originalVerticalCurve;

	/**
	 * Perfil longitudinal resultado de la reconstrucción
	 */
	VerticalProfile resultVerticalProfile;
	/**
	 * Perfil de pendientes correspondiente al originalVerticalProfile
	 */
	VerticalGradeProfile originalGradeProfile;
	/**
	 * Perfil de pendientes reconstruido a partir de la muestra de puntos (si, gi)
	 */
	VerticalGradeProfile resultGradeProfile;
	/**
	 * Muestra de puntos (Si, gi) a la que se aplicará el algoritmo de reconstrucción
	 */
	XYVectorFunction originalGradePoints;
	
	XYVectorFunction resultGradePoints, originalVerticalProfilePoints, resultVerticalProfilePoints;
	
	/**
	 * Errores cuadráticos cometidos en la reconstrucción
	 */
	double[] ecm;
	double maxEcm, minEcm, meanecm;
	
	/**
	 * d1 es la distancia en valor absoluto desde el punto de entrada a la vertical curve
	 * en el perfil original y el reconstruido.
	 * d2 es la distancia en el punto de salida de la vertical curve
	 */
	double[] d1, d2;
	double maxd1, mind1, meand1, maxd2, mind2, meand2;
	
	
	@BeforeClass
	public static void beforeClass() {
		PropertyConfigurator.configure("log4j.properties");	
	
	}
	
	@Test
	public void test() {
		LOG.debug("test()");
		
		ecm = new double[numberOfEssays];
		d1 = new double[numberOfEssays];
		d2 = new double[numberOfEssays];
		
		
		for(currentEssay=0; currentEssay< numberOfEssays; currentEssay++) {
			originalVerticalProfile = generateOriginalVerticalProfile();
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
	
	private VerticalProfile generateOriginalVerticalProfile() {
		//LOG.debug("generateVerticalProfile()");
		
		originalGrade1 = calculateFirstGrade(s0, z0);

		// Pendiente de salida de la vertical curve, pendiente de la segunda grade
		double g2 = -RandomGradeFactory.randomUniformGradeSlope(minGrade, maxGrade, gradeIncrement);

		originalVerticalCurve = calculateVerticalCurve(originalGrade1.getEndS(), originalGrade1.getEndZ(), originalGrade1.getSlope(), g2);

		originalGrade2 = calculateSecondGrade(originalVerticalCurve.getEndS(), originalVerticalCurve.getEndZ(), g2);

		VerticalProfile verticalProfile = new VerticalProfile();
		verticalProfile.add(originalGrade1);
		verticalProfile.add(originalVerticalCurve);
		verticalProfile.add(originalGrade2);
		
		// System.out.println(originalVerticalProfile);
		return verticalProfile;
	}

	private GradeAlignment calculateFirstGrade(double starts, double startz) {
		//LOG.debug("calculateFirstGrade()");
		double g1 = RandomGradeFactory.randomUniformGradeSlope(minGrade, maxGrade, gradeIncrement);
		double grade1Length = RandomFactory.randomUniformLength(minGradeLength, maxGradeLength, gradeLengthIncrement);
		GradeAlignment grade1 = new GradeAlignment(starts, startz, g1, grade1Length);
		return grade1;
	}
	private VerticalCurveAlignment calculateVerticalCurve(double s1, double z1, double g1, double g2) {
		//LOG.debug("calculateVerticalCurve()");
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
	private GradeAlignment calculateSecondGrade(double startS, double startZ, double slope) {
		//LOG.debug("calculateSecondGrade()");
		double grade2Length = RandomFactory.randomUniformLength(minGradeLength, maxGradeLength, gradeLengthIncrement);
		GradeAlignment grade2 = new GradeAlignment(startS, startZ, slope, grade2Length);
		return grade2;
	}
	private void generateOriginalGradeProfile() {
		//LOG.debug("generateVerticalGradeProfile()");
		originalGradeProfile = originalVerticalProfile.derivative();
		//System.out.println(originalGradeProfile);
	}
	private void generateGradeSample() {
		//LOG.debug("generateGradeSample()");
		originalGradePoints = originalGradeProfile.getSample(s0, originalGrade2.getEndS(), pointSeparation, true);
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
		resultVerticalProfilePoints = resultVerticalProfile.getSample(s0, originalVerticalProfile.getEndS(), pointSeparation, true);
		originalVerticalProfilePoints = originalVerticalProfile.getSample(s0, originalVerticalProfile.getEndS(), pointSeparation, true);		
		double currentEcm = MathUtil.ecm(originalVerticalProfilePoints.getYValues(), resultVerticalProfilePoints.getYValues());
		double currentd1 = Math.abs(originalVerticalProfile.getAlign(1).getStartS() - resultVerticalProfile.getAlign(1).getStartS());
		double currentd2 = Math.abs(originalVerticalProfile.getAlign(1).getEndS() - resultVerticalProfile.getAlign(1).getEndS());
		System.out.println(currentEcm + ", " + currentd1 + ", " + currentd2);
		if(currentEssay==0) {
			maxEcm = currentEcm;
			minEcm = currentEcm;
			maxd1 = currentd1;
			mind1 = currentd1;
			maxd2 = currentd2;
			mind2 = currentd2;
		} else {
			if (currentEcm > maxEcm) {
				maxEcm = currentEcm;
			}
			if(currentEcm < minEcm) {
				minEcm = currentEcm;
			}
			if(currentd1 > maxd1) {
				maxd1 = currentd1;
			}
			if(currentd1 < mind1) {
				mind1 = currentd1;
			}
			if(currentd2 > maxd2) {
				maxd2 = currentd2;
			}
			if(currentd2 < mind2) {
				mind2 = currentd2;
			}
		}
		ecm[currentEssay] = currentEcm;
		if(currentEcm>0.01) {
			System.out.println(originalVerticalProfile);
			System.out.println(resultVerticalProfile);
		}
		d1[currentEssay] = currentd1;
		d2[currentEssay] = currentd1;
		
		meanecm = 0.0;
		for(int i=0; i<ecm.length;i++) {
			meanecm+=ecm[i];
		}
		meanecm = meanecm / ecm.length;
		
		meand1 = 0.0;
		for(int i=0; i<d1.length;i++) {
			meand1+=d1[i];
		}
		meand1 = meand1 / d1.length;
		
		meand2 = 0.0;
		for(int i=0; i<d2.length;i++) {
			meand2+=d2[i];
		}
		meand2 = meand2 / d2.length;
		
	}
	private void showReport() {
		// LOG.debug("showError()");
		System.out.println("Número de ensayos: " + numberOfEssays);
		
		System.out.println("mean ecm = " + meanecm);
		System.out.println("min ecm = " + minEcm);
		System.out.println("max ecm = " + maxEcm);
		
		System.out.println("mean d1 = " + meand1);
		System.out.println("min d1 = " + mind1);
		System.out.println("max d1 = " + maxd1);

		System.out.println("mean d2 = " + meand2);
		System.out.println("min d2 = " + mind2);
		System.out.println("max d2 = " + maxd2);

	}
	

}
