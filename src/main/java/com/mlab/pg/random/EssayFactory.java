package com.mlab.pg.random;

import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.reconstruction.Reconstructor;
import com.mlab.pg.util.MathUtil;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.XYVectorFunction;

/**
 * Test para ensayos de perfiles tipo I : UpGrade - CrestCurve - DownGrade
 * 
 * @author shiguera
 *
 */
public class EssayFactory {
	
	private static Logger LOG = Logger.getLogger(EssayFactory.class);
	
	RandomProfileFactory profileFactory;
	/**
	 * Número de ensayos
	 */
	int essaysCount = 1000;
	/**
	 * Si es true, se muestran en pantalla los perfiles longitudinales y de pendientes
	 */
	boolean displayProfiles = false;
	/**
	 * Ensayo actual durante la ejecución
	 */
	int currentEssay;
	/**
	 * Separación entre puntos de la muestra del perfil de pendientes
	 */
	double pointSeparation = 5.0;
	/**
	 * Si es true, en cada ensayo se genera una separación de puntos aleatoria entre 1 y 10 metros
	 */
	boolean randomPointSeparation = false;
	/**
	 * Número de puntos de las rectas de interpolación
	 */
	int mobileBaseSize = 3;
	/**
	 * Pendiente límite de las rectas consideradas horizontales
	 */
	double thresholdSlope = 1e-5;
		
	/**
	 * Perfil longitudinal del tipo I generado aleatoriamente.
	 * Consta de una UpGrade, una CrestCurve y una DownGrade 
	 */
	VerticalProfile originalVerticalProfile; 
	XYVectorFunction originalVerticalProfilePoints;
	/**
	 * Perfil de pendientes obtenido de originalVerticalProfile por derivación
	 */
	VerticalGradeProfile originalGradeProfile;
	/**
	 * Muestra de puntos (Si, gi) a la que se aplicará el algoritmo de reconstrucción
	 */
	XYVectorFunction originalGradePoints;
	/**
	 * Perfil de pendientes reconstruido a partir de la muestra de puntos (si, gi)
	 */
	VerticalGradeProfile resultGradeProfile;
	XYVectorFunction resultGradePoints;

	/**
	 * Perfil longitudinal resultado de la reconstrucción
	 */
	VerticalProfile resultVerticalProfile;
	XYVectorFunction resultVerticalProfilePoints;

	/**
	 * Errores cuadráticos de cada ensayo cometidos en la reconstrucción.
	 * Es el error cuadrático medio entre los puntos de la muestra original
	 * del perfil de pendientes y los puntos del perfil de pendientes
	 * reconstruido.
	 */
	double[] ecm;
	/**
	 * Error cuadrático máximo, mínimo y medio en la serie de ensayos
	 */
	double maxEcm, minEcm, meanEcm;
	/**
	 * Valor del ecm por encima del cual muestra un mensaje log con el ensayo afectado
	 */
	double alertEcm = 1.0;
	/**
	 * d es la distancia horizontal en valor absoluto desde el punto de entrada a cada alineación
	 * en el perfil original y el reconstruido.
	 */
	int alignmentCount;
	double[][] d; // Una distancia para cada ensayo y cada alineación
	double[] maxd, mind, meand; // Valor promedio de las distancias para cada alineación
	
	
	
	public EssayFactory(RandomProfileFactory profilefactory) {
		this.profileFactory = profilefactory;
	}
	
	
	public void doEssays() {
		//LOG.debug("work()");
		
		Random rnd = new Random();
		
		ecm = new double[essaysCount];
		d = new double[essaysCount][];
				
		for(currentEssay=0; currentEssay < essaysCount; currentEssay++) {

			if(randomPointSeparation) {
				pointSeparation = 1.0 + rnd.nextInt(19)/2.0;
			}
			
			originalVerticalProfile = generateOriginalVerticalProfile();
			alignmentCount = originalVerticalProfile.size();
			d[currentEssay] = new double[alignmentCount];
			if(currentEssay == 0) {
				meand = new double[alignmentCount];
				maxd = new double[alignmentCount];
				mind = new double[alignmentCount];
			}
			
			if(displayProfiles) {
				System.out.println(originalVerticalProfile);
			}

			originalGradeProfile = generateGradeProfile(originalVerticalProfile);
			
			originalGradePoints = generateGradeSample(originalGradeProfile, pointSeparation);
			
			doGradeProfileReconstruction();
			if(displayProfiles) {
				System.out.println(resultVerticalProfile);
			}
			
			measureEssayErrors();
		}
		doAverages();
		showReport();
	}
	/**
	 * Generación del perfil longitudinal aleatorio
	 * @return
	 */
	private VerticalProfile generateOriginalVerticalProfile() {
		//LOG.debug("generateVerticalProfile()");
		return profileFactory.createRandomProfile();
	}
	/**
	 * Generación del perfil de pendientes por derivación del perfil longitudinal
	 * @param vprofile
	 * @return
	 */
	private VerticalGradeProfile generateGradeProfile(VerticalProfile vprofile) {
		return vprofile.derivative();
	}

	/**
	 * Genera la muestra de puntos (Si, Gi) con la que se hará la reconstrucción
	 * @param gradeProfile
	 * @param pointseparation
	 * @return
	 */
	private XYVectorFunction generateGradeSample(VerticalGradeProfile gradeProfile, double pointseparation) {
		//LOG.debug("generateGradeSample()");
		double starts = gradeProfile.getStartS();
		double ends = gradeProfile.getEndS();
		return gradeProfile.getSample(starts, ends, pointseparation, true);
	}

	/**
	 * Reconstrucción propiamente dicha
	 */
	private void doGradeProfileReconstruction() {
		//LOG.debug("doGradeProfileReconstruction()");
		double z0 = originalVerticalProfile.getFirstAlign().getStartZ();
		double s0 = originalVerticalProfile.getFirstAlign().getStartS();
		Reconstructor generator = new Reconstructor(originalGradePoints, mobileBaseSize, thresholdSlope, z0);
		resultGradeProfile = generator.getGradeProfile();
		resultGradePoints = resultGradeProfile.getSample(s0,z0, pointSeparation, true);
		resultVerticalProfile = generator.getVerticalProfile();
	}
	
	private void measureEssayErrors() {
		//LOG.debug("measureErrors()");
		double starts = originalVerticalProfile.getStartS();
		double ends = originalVerticalProfile.getEndS();
		resultVerticalProfilePoints = resultVerticalProfile.getSample(starts, ends, pointSeparation, true);
		originalVerticalProfilePoints = originalVerticalProfile.getSample(starts, ends, pointSeparation, true);		
		double currentEcm = MathUtil.ecm(originalVerticalProfilePoints.getYValues(), resultVerticalProfilePoints.getYValues());
		double[] currentd = new double[alignmentCount];
		for(int i=0; i<alignmentCount; i++) {
			currentd[i] = Math.abs(originalVerticalProfile.getAlign(i).getStartS() - resultVerticalProfile.getAlign(i).getStartS());
		}
		// System.out.println(pointSeparation + ", " + currentEcm + ", " + currentd1 + ", " + currentd2);
		if(currentEssay==0) {
			maxEcm = currentEcm;
			minEcm = currentEcm;
			for(int i=0; i<alignmentCount; i++) {
				maxd[i] = currentd[i];
				mind[i] = currentd[i];
			}	
		} else {
			if (currentEcm > maxEcm) {
				maxEcm = currentEcm;
			}
			if(currentEcm < minEcm) {
				minEcm = currentEcm;
			}
			for(int i=0; i<alignmentCount; i++) {
				if(currentd[i] > maxd[i]) {
					maxd[i] = currentd[i];
				}
				if(currentd[i] < mind[i]) {
					mind[i] = currentd[i];
				}
			}	
		}
		ecm[currentEssay] = currentEcm;
		if(currentEcm > alertEcm) {
			LOG.warn("Essay number " +currentEssay + "; ECM = " + currentEcm); 
		}
		for(int i=0; i < alignmentCount; i++) {
			d[currentEssay][i] = currentd[i];
		}
		
		
	}
	private void doAverages() {
		meanEcm = 0.0;
		for(int i=0; i < essaysCount;i++) {
			meanEcm += ecm[i];
		}
		meanEcm = meanEcm / essaysCount;
		
		// Comienza en 1, pues el inicio de la primera alineación no se cuenta
		for(int i=1; i < alignmentCount; i++) {
			meand[i] = 0.0;
			for(int j=0; j < essaysCount; j++) {
				meand[i] += d[j][i];
			}
			meand[i] = meand[i] / essaysCount;
		}		
	}
	private void showReport() {
		// LOG.debug("showError()");
		System.out.println("Número de ensayos            : " + essaysCount);
		System.out.println("Separación entre puntos      : " + (!randomPointSeparation?pointSeparation:"Random"));
		System.out.println("Longitud rectas interpolación: " + (!randomPointSeparation?(mobileBaseSize-1)*pointSeparation:"Random"));
		System.out.println("Pendiente límite             : " + thresholdSlope);
		System.out.println("---------------------------------------------------------------------------------------------------------");

		System.out.format("Averages  %12s \t %12s \t %12s \n", "meanecm", "maxecm", "minecm");
		System.out.format("          %12.8f \t %12.8f \t %12.8f \n", meanEcm, maxEcm, minEcm);
		
		System.out.format("Point %12s \t %12s \t %12s \n", "meand", "maxd", "mind");
		for(int i=1; i < alignmentCount; i++) {
			System.out.format("%5d \t %12.8f \t %12.8f \t %12.8f \n", i, meand[i], maxd[i], mind[i]);
		}
	}


	public RandomProfileFactory getProfileFactory() {
		return profileFactory;
	}


	public void setProfileFactory(RandomProfileFactory profileFactory) {
		this.profileFactory = profileFactory;
	}


	public int getEssaysCount() {
		return essaysCount;
	}


	public void setEssaysCount(int essaysCount) {
		this.essaysCount = essaysCount;
	}


	public boolean isDisplayProfiles() {
		return displayProfiles;
	}


	public void setDisplayProfiles(boolean displayProfiles) {
		this.displayProfiles = displayProfiles;
	}


	public boolean isRandomPointSeparation() {
		return randomPointSeparation;
	}


	public void setRandomPointSeparation(boolean randomPointSeparation) {
		this.randomPointSeparation = randomPointSeparation;
	}


	public double getAlertEcm() {
		return alertEcm;
	}


	public void setAlertEcm(double alertEcm) {
		this.alertEcm = alertEcm;
	}


	public double getPointSeparation() {
		return pointSeparation;
	}
	public void setPointSeparation(double separation) {
		this.pointSeparation = separation;
	}

	public int getMobileBaseSize() {
		return mobileBaseSize;
	}
	public void setMobileBaseSize(int mobilebasesize) {
		this.mobileBaseSize = mobilebasesize;
	}

	public double getThresholdSlope() {
		return thresholdSlope;
	}

	public void setThresholdSlope(double thresholdslope) {
		this.thresholdSlope = thresholdslope;
	}
	public VerticalProfile getOriginalVerticalProfile() {
		return originalVerticalProfile;
	}


	public XYVectorFunction getOriginalVerticalProfilePoints() {
		return originalVerticalProfilePoints;
	}


	public VerticalGradeProfile getOriginalGradeProfile() {
		return originalGradeProfile;
	}


	public XYVectorFunction getOriginalGradePoints() {
		return originalGradePoints;
	}


	public VerticalGradeProfile getResultGradeProfile() {
		return resultGradeProfile;
	}


	public XYVectorFunction getResultGradePoints() {
		return resultGradePoints;
	}


	public VerticalProfile getResultVerticalProfile() {
		return resultVerticalProfile;
	}


	public XYVectorFunction getResultVerticalProfilePoints() {
		return resultVerticalProfilePoints;
	}


	public double getMaxEcm() {
		return maxEcm;
	}


	public double getMinEcm() {
		return minEcm;
	}


	public double getMeanEcm() {
		return meanEcm;
	}


	public double[] getMaxd() {
		return maxd;
	}


	public double[] getMind() {
		return mind;
	}


	public double[] getMeand() {
		return meand;
	}
	

}
