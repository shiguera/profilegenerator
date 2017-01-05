package com.mlab.pg.essays;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.mlab.pg.random.RandomProfileFactory;
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
	
	/**
	 * Generador de números aleatorios
	 */
	Random random;
	
	/**
	 * Factory generadora de perfiles aleatorios
	 */
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
	 * Separación entre puntos de la muestra del perfil de pendientes
	 */
	double pointSeparation = 5.0;
	/**
	 * Si es true, en cada ensayo se genera una separación de puntos aleatoria entre 1 y 10 metros
	 */
	boolean randomPointSeparation = false;
	boolean alertIfEcmGreatThanAlertEcm = false;
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
	 * Ensayo actual durante la ejecución
	 */
	int currentEssay;
	/**
	 * Número de alineaciones de las que consta el perfillongitudinal
	 */
	int alignmentCount;
	/**
	 * El wrongProfileCount cuenta el número de perfiles reconstruidos 
	 * en los que no se ha acertado el número de alineaciones, y por tanto,
	 * no es posible medir los errores en los puntos frontera
	 */
	int wrongProfileCount;
	/**
	 * Si es true, los ensayos fallidos se vuelven a probar con thresholdSlope/10
	 */
	boolean tryWithLessThresholdSlope = false;
	/**
	 * Cuando al reconstruir el perfil por primera vez el número de alineaciones
	 * de la reconstrucción no coincide con el del perfil original, se hace un intento
	 * con un thresholdSlope menor. Si se corrige el problema, se incrementa 
	 * este contador.
	 */
	int correctedWrongProfilesCount;
	/**
	 * Errores cuadráticos de cada ensayo cometidos en la reconstrucción.
	 * Es el error cuadrático medio entre los puntos de la muestra original
	 * del perfil de pendientes y los puntos del perfil de pendientes
	 * reconstruido.
	 */
	double[] ecm;
	/**
	 * Valores agregados para la medición del ecm: 
	 * Error cuadrático máximo, mínimo, medio y desviación típica en la serie de ensayos
	 */
	double maxEcm, minEcm, meanEcm, desvEcm;
	/**
	 * Valor del ecm por encima del cual muestra un mensaje log con el ensayo afectado
	 */
	double alertEcm = 1.0;
	/**
	 * d es la distancia horizontal en valor absoluto desde el punto final de cada alineación
	 * en el perfil original y el reconstruido. Hay un punto frontera para medir errores 
	 * por cada alineación del perfil. El primer punto de la primera alineación no se 
	 * considera.
	 * Cada elemento del ArrayList pertenece a uno de los ensayos válidos, que son aquellos
	 * en los que el algoritmo ha acertado el número de alineaciones. Cada elemento es un double[]
	 * con una componente para cada punto frontera de la alineación correspondiente
	 */
	List<double[]> d;
	/**
	 * Valores agregados de los errores cometidos en la determinación
	 * de los puntos finales de las alineaciones. Cada punto frontera tendrá un
	 * valor agregado. Por ejemplo, el punto final de la primera alineación del perfil
	 * tendrá un valor maxd correspondiente al máximo error cometido en la determinación
	 * de ese punto en los diferentes perfiles aleatorios generados.
	 * En los perfiles en los que el algoritmo no acierta con el número de alineaciones,
	 * no se mide este error.
	 * El double[] tiene tantas componentes como alineaciones tenga un perfil, alignmentCount 
	 */
	
	double[] maxd, mind, meand, desvd; 
	
	
	
	public EssayFactory(RandomProfileFactory profilefactory) {
		this.profileFactory = profilefactory;
	}
	
	
	public void doEssays() {
		//LOG.debug("doEssays()");
		
		random = new Random();
		
		ecm = new double[essaysCount];
		d = new ArrayList<double[]>();
		
		wrongProfileCount = 0;
		correctedWrongProfilesCount = 0;
		
		for(currentEssay=0; currentEssay < essaysCount; currentEssay++) {

			if(randomPointSeparation) {
				pointSeparation = calculateRandomPointSeparation();
			}
			
			originalVerticalProfile = generateOriginalVerticalProfile();
			alignmentCount = originalVerticalProfile.size();
			d.add(new double[alignmentCount]);
			if(currentEssay == 0) {
				maxd = new double[alignmentCount];
				mind = new double[alignmentCount];
				meand = new double[alignmentCount];
				desvd = new double[alignmentCount];				
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
			
			calculateEcmError();
			// TODO Al hacer la segunda oportunidad, se guardan los maxEcm y minEcm de los ensayos fallidos
			
			if(resultVerticalProfile.size() == originalVerticalProfile.size()) {
				calculateBorderPointErrors();				
			} else {
				wrongProfileCount++;
				if(tryWithLessThresholdSlope) {
					tryWithLessThresholdSlope();					
				}
			}
			
		}
		
		calculateAggregatesForEcm();
		calculateAggregatesForBorderPointErrors();
		
		showReport();
	}
	private void tryWithLessThresholdSlope() {
		//System.out.println(originalVerticalProfile);
		//System.out.println(resultVerticalProfile);
		double oldThresholdSlope = thresholdSlope;
		setThresholdSlope(oldThresholdSlope / 10.0);
		doGradeProfileReconstruction();
		if(resultVerticalProfile.size() == originalVerticalProfile.size()) {
			correctedWrongProfilesCount++;
			calculateEcmError();
			calculateBorderPointErrors();
			//System.out.println(resultVerticalProfile);	
		} else {
			setThresholdSlope(oldThresholdSlope);
			doGradeProfileReconstruction();
			calculateEcmError();				
		}
		setThresholdSlope(oldThresholdSlope);
	}
	private double calculateRandomPointSeparation() {
		return 1.0 + random.nextInt(19)/2.0;
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
		Reconstructor generator = null;
		try {
			generator = new Reconstructor(originalGradePoints, mobileBaseSize, thresholdSlope, z0);
		} catch(Exception e) {
			LOG.error("Error en el constructor de Reconstructor");
			System.exit(1);
		}
		resultGradeProfile = generator.getGradeProfile();
		resultGradePoints = resultGradeProfile.getSample(s0,z0, pointSeparation, true);
		resultVerticalProfile = generator.getVerticalProfile();
	}
	
	private void calculateEcmError() {
		double starts = originalVerticalProfile.getStartS();
		double ends = originalVerticalProfile.getEndS();
		originalVerticalProfilePoints = originalVerticalProfile.getSample(starts, ends, pointSeparation, true);		
		resultVerticalProfilePoints = resultVerticalProfile.getSample(starts, ends, pointSeparation, true);
		double currentEcm = MathUtil.ecm(originalVerticalProfilePoints.getYValues(), resultVerticalProfilePoints.getYValues());		
		ecm[currentEssay] = currentEcm;
		
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
		
		if(alertIfEcmGreatThanAlertEcm && currentEcm > alertEcm) {
			LOG.warn("Essay number " +currentEssay + "; ECM = " + currentEcm); 
		}

	}
	private void calculateBorderPointErrors() {
		//LOG.debug("measureErrors()");
		
		double[] currentd = new double[alignmentCount];
		for(int i=0; i<alignmentCount; i++) {
			currentd[i] = Math.abs(originalVerticalProfile.getAlign(i).getEndS() - resultVerticalProfile.getAlign(i).getEndS());
		}
		// System.out.println(pointSeparation + ", " + currentEcm + ", " + currentd1 + ", " + currentd2);
		for(int i=0; i < alignmentCount; i++) {
			d.get(d.size()-1)[i] = currentd[i];
		}
		

		if(currentEssay==0) {
			for(int i=0; i<alignmentCount; i++) {
				maxd[i] = currentd[i];
				mind[i] = currentd[i];
			}	
		} else {
			for(int i=0; i<alignmentCount; i++) {
				if(currentd[i] > maxd[i]) {
					maxd[i] = currentd[i];
				}
				if(currentd[i] < mind[i]) {
					mind[i] = currentd[i];
				}
			}	
		}
	}
	private void calculateAggregatesForEcm() {
		calculateMeanForEcmError();
		calculateStandardDeviationForEcmError();		
	}
	private void calculateMeanForEcmError() {
		// Media en el error c. m. del perfil longitudinal reconstruido
		meanEcm = sumDoubleArray(ecm) / (double)essaysCount;		
	}
	private void calculateStandardDeviationForEcmError() {
		desvEcm = 0.0;
		for(int i=0; i<essaysCount; i++) {
			desvEcm += (ecm[i] - meanEcm) * (ecm[i] - meanEcm);
		}
		desvEcm = Math.sqrt(desvEcm) / (double)essaysCount;
	}
	private void calculateAggregatesForBorderPointErrors() {
		
		// Media del error en la distancia a los puntos singulares
		calculateMeanForBorderPointErrors();

		// Desviación típica
		calculateStandardDeviationForBorderPointErrors();		
	}
	private void calculateMeanForBorderPointErrors() {
		double[] sums = new double[alignmentCount];
		for(int i=0; i<sums.length; i++) {
			sums[i] = 0.0;
		}
		
		for(int i=1; i < d.size(); i++) {
			for(int j=0; j<alignmentCount; j++) {
				sums[j] += d.get(i)[j];
			}
		}		
		
		
		for(int i=0; i<sums.length; i++) {
			meand[i] = sums[i] / (double)(d.size());
		}
	}
	private void calculateStandardDeviationForBorderPointErrors() {
		double[] desv = new double[alignmentCount];
		for(int i=0; i<desv.length; i++) {
			desv[i] = 0.0;
		}
		for(int i=1; i < d.size(); i++) {
			for(int j=0; j<alignmentCount; j++) {
				desv[j] += (d.get(i)[j] - meand[j]) * (d.get(i)[j] - meand[j]);				
			}
		}
		for(int i=0; i<desv.length; i++) {
			desvd[i] = Math.sqrt(desv[i]) / (double)(d.size());
		}
	}
	private double sumDoubleArray(double[] array) {
		double sum=0;
		for(int i=0; i<array.length; i++) {
			sum += array[i];
		}
		return sum;
	}
	
	private void showReport() {
		// LOG.debug("showError()");
		System.out.println("\n");
		System.out.println(profileFactory.getFactoryName());
		System.out.println(profileFactory.getDescription());
		System.out.println("Essays count                   : " + essaysCount);
		System.out.println("Point distance                 : " + (!randomPointSeparation?pointSeparation:"Random"));
		System.out.format("Interpolation straight length  : %10.4f \n", (!randomPointSeparation?(mobileBaseSize-1)*pointSeparation:"Random"));
		System.out.println("Threshold slope                : " + thresholdSlope);
		System.out.println("-------------------------------------------------------------------------");

		// Porcentajes de ensayos correctos y fallidos
		double wrongpercentage = (double) wrongProfileCount * 100 / (double)essaysCount;
		double rightpercentage = 100.0 - wrongpercentage;
		double correctedpercentage = correctedWrongProfilesCount * 100 / (double)essaysCount;
		System.out.format("Correct essays percentage                                     : %6.2f \n", rightpercentage);
		System.out.format("Wrong essays percentage                                       : %6.2f \n", wrongpercentage);
		System.out.format("Corrected essays percentage after using lower threshold slope : " + (tryWithLessThresholdSlope?"%6.2f":"%s") + " \n", 
				(tryWithLessThresholdSlope?correctedpercentage:"NOT USED"));
		System.out.format("Correct essays percentage after using lower threshold slope   : " + (tryWithLessThresholdSlope?"%6.2f":"%s") + " \n", 
				(tryWithLessThresholdSlope?rightpercentage + correctedpercentage:"NOT USED"));
		
		System.out.println("\n");
		
		System.out.println("Mean Squared Error (MSE) (Between reconstructed profile and exact profile)");
		System.out.println("--------------------------------------------------------------------------");
		System.out.format("%12s \t %12s \t %12s \t %12s \n", "mean", "deviation", "max", "min");
		System.out.format("%12.8f \t %12.8f \t %12.8f \t %12.8f \n", meanEcm, desvEcm, maxEcm, minEcm);

		System.out.println("\n");
		System.out.println("Border points Accuracy (distance to exact point in metres)");
		System.out.println("----------------------------------------------------------");
		System.out.format("Point %12s \t %12s \t %12s \t %12s\n", "mean",  "dev", "max", "min");
		for(int i=0; i < alignmentCount-1; i++) {
			System.out.format("%5d \t %12.8f \t %12.8f \t %12.8f \t %12.8f\n", i+1, meand[i], desvd[i], maxd[i], mind[i]);
		}
		// Calculo la media de las distancias medias y el máximo de la distancia máxima 
		double ddmean=0.0, ddmax=maxd[0];
		for(int i=0; i < alignmentCount-1; i++) {
			ddmean += meand[i];
			if(maxd[i] > ddmax) {
				ddmax = maxd[i];
			}
		}
		ddmean = ddmean / ((double)meand.length - 1.0);
		System.out.println("\n");
		System.out.format("Mean d = %6.2f \n", ddmean);
		System.out.format("Max  d = %6.2f \n", ddmax);
	}

	// Getters and Setters
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


	public boolean isTryWithLessThresholdSlope() {
		return tryWithLessThresholdSlope;
	}


	public void setTryWithLessThresholdSlope(boolean tryWithLessThresholdSlope) {
		this.tryWithLessThresholdSlope = tryWithLessThresholdSlope;
	}
	

}
