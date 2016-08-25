package com.mlab.pg.reconstruction;

import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.random.RandomProfileFactory;
import com.mlab.pg.random.RandomProfileType_I_Factory;
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
	@BeforeClass
	public static void beforeClass() {
		PropertyConfigurator.configure("log4j.properties");	
	
	}
	
	int numberOfEssays = 1000;
	int currentEssay;
	int mobileBaseSize = 6;
	double thresholdSlope = 1e-5;
	/**
	 * Separación entre puntos de la muestra del perfil de pendientes
	 */
	double pointSeparation = 10.0;
	boolean randomPointSeparation = false;
	boolean displayProfiles = false;
		
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
	
	
	
	
	@Test
	public void test() {
		LOG.debug("test()");
		
		ecm = new double[numberOfEssays];
		d1 = new double[numberOfEssays];
		d2 = new double[numberOfEssays];
		
		Random rnd = new Random();
		
		for(currentEssay=0; currentEssay< numberOfEssays; currentEssay++) {
			//System.out.println(currentEssay);
			if(randomPointSeparation) {
				pointSeparation = 1.0 + rnd.nextInt(20)/2.0;
			}
			
			originalVerticalProfile = generateOriginalVerticalProfile();
			if(displayProfiles) {
				System.out.println(originalVerticalProfile);
			}

			originalGradeProfile = generateOriginalGradeProfile();
			
			originalGradePoints = generateGradeSample();
			try {
				doGradeProfileReconstruction();
			} catch (Exception e) {
				LOG.error("Error en el constructor de Reconstructor");
			}
			if(displayProfiles) {
				System.out.println(resultVerticalProfile);
			}
			
			measureErrors();
		}

		showReport();
	}
	
	private VerticalProfile generateOriginalVerticalProfile() {
		//LOG.debug("generateVerticalProfile()");		
		RandomProfileFactory factory = new RandomProfileType_I_Factory();
		return factory.createRandomProfile();
	}

	private VerticalGradeProfile generateOriginalGradeProfile() {
		//LOG.debug("generateVerticalGradeProfile()");
		return originalVerticalProfile.derivative();
		//System.out.println(originalGradeProfile);
	}
	private XYVectorFunction generateGradeSample() {
		//LOG.debug("generateGradeSample()");
		return originalGradeProfile.getSample(originalVerticalProfile.getStartS(), originalVerticalProfile.getEndS(), pointSeparation, true);
		//LOG.debug("Muestra de pendientes: sep = " + pointSeparation + ", points = " + originalGradePoints.size());
	}
	private void doGradeProfileReconstruction() throws Exception {
		//LOG.debug("doGradeProfileReconstruction()");
		double z0 = originalVerticalProfile.getFirstAlign().getStartZ();
		double s0 = originalVerticalProfile.getFirstAlign().getStartS();
		Reconstructor generator = new Reconstructor(originalGradePoints, mobileBaseSize, thresholdSlope, z0);
		resultGradeProfile = generator.getGradeProfile();
		resultGradePoints = resultGradeProfile.getSample(s0,  z0, pointSeparation, true);
		resultVerticalProfile = generator.getVerticalProfile();
	}
	
	private void measureErrors() {
		//LOG.debug("measureErrors()");
		double z0 = originalVerticalProfile.getFirstAlign().getStartZ();
		double s0 = originalVerticalProfile.getFirstAlign().getStartS();
		resultVerticalProfilePoints = resultVerticalProfile.getSample(s0, originalVerticalProfile.getEndS(), pointSeparation, true);
		originalVerticalProfilePoints = originalVerticalProfile.getSample(s0, originalVerticalProfile.getEndS(), pointSeparation, true);		
		double currentEcm = MathUtil.ecm(originalVerticalProfilePoints.getYValues(), resultVerticalProfilePoints.getYValues());
		double currentd1 = Math.abs(originalVerticalProfile.getAlign(1).getStartS() - resultVerticalProfile.getAlign(1).getStartS());
		double currentd2 = Math.abs(originalVerticalProfile.getAlign(1).getEndS() - resultVerticalProfile.getAlign(1).getEndS());
		//System.out.println(pointSeparation + ", " + currentEcm + ", " + currentd1 + ", " + currentd2);
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
		d2[currentEssay] = currentd2;
		
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
		System.out.println("Pendiente límite:: " + thresholdSlope);
		System.out.println("Separación entre puntos: " + (!randomPointSeparation?pointSeparation:"Random"));
		System.out.println("Longitud rectas m.c.: " + (!randomPointSeparation?(mobileBaseSize-1)*pointSeparation:"Random"));
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
