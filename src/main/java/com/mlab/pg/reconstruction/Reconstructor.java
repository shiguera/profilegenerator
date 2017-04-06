package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import com.mlab.pg.reconstruction.strategy.EndingsWithBeginnersAdjuster;
import com.mlab.pg.reconstruction.strategy.EndingsWithBeginnersAdjuster_EqualArea;
import com.mlab.pg.reconstruction.strategy.EndingsWithBeginnersAdjuster_LessSquares;
import com.mlab.pg.reconstruction.strategy.GradeProfileCreator;
import com.mlab.pg.reconstruction.strategy.GradeProfileCreator_EqualArea;
import com.mlab.pg.reconstruction.strategy.GradeProfileCreator_LessSquares;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategy;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyImplementation;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;
import com.mlab.pg.util.MathUtil;
import com.mlab.pg.valign.GradeProfileAlignment;
import com.mlab.pg.valign.VAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

/**
 * Genera un GradeProfile y un VerticalProfile a partir de un XYVectorFunction con los puntos originales {si, gi} 
 * Para ello utiliza un SegmentMaker que genera una Segmentation que solo tiene segmentos
 * del tipo Grade y VerticalCurve.
 * Ofrece una pareja de métodos getGradeProfile() que devuelve el GradeProfile reconstruido y 
 * getVerticalProfile() que devuelve el perfil longitudinal VerticalProfile correspondiente
 * a la integración del GradeProfile
 * 
 * @author shiguera
 *
 */
public class Reconstructor {

	Logger LOG = Logger.getLogger(Reconstructor.class);
	/**
	 * Longitud mínima utilizada en TypeIntervalArrayGenerator para filtrar
	 * tramos menores de esa longitud. Si el anterior o el siguiente son del
	 * mismo tipo, TypeIntervalArrayGenerator los une
	 */
	double MIN_LENGTH = 0.0; // Lo utiliza el TypeIntervalArrayGenerator
	protected int MIN_POINTS_COUNT = 0; // Lo utiliza el TypeIntervalArrayGenerator
	double[] thresholdSlopes = new double[] {1.0e-4, 1.75e-5, 1.5e-5, 1.25e-5, 1.0e-5, 1.75e-6, 1.5e-6, 1.25e-6, 1.0e-6, 1.75e-7, 1.5e-7, 1.25e-7, 1.0e-7}; 
	//double[] thresholdSlopes = new double[] {1.0e-4}; 

	
	protected XYVectorFunction originalGradePoints;
	protected XYVectorFunction originalVerticalProfilePoints;
	protected XYVectorFunction integralVerticalProfilePoints;
	protected int baseSize;
	protected double thresholdSlope;
	protected double startZ;
	
	protected InterpolationStrategy strategy;
	protected TypeIntervalArray typeIntervalArray;
	
	protected VerticalGradeProfile resultGradeProfile;
	protected VerticalProfile resultVerticalProfile;
	protected XYVectorFunction resultVerticalProfilePoints;
	//protected EndingsWithBeginnersAdjuster adjuster;
	
	int bestTest;
	double[][] results;
	
	double startX, endX;
	protected double separacionMedia;
	protected double trackLength;
	protected int alignmentCount;
	protected double meanError;
	protected double maxError;
	protected double ecm;
	protected double varianza;
	
	public Reconstructor(XYVectorFunction originalgradePoints, double startz, InterpolationStrategyType strategyType) {
		originalGradePoints = originalgradePoints.clone();
		startZ = startz;

		strategy = new InterpolationStrategyImplementation(strategyType);
		
		startX = originalGradePoints.getStartX();
		endX = originalGradePoints.getEndX();
		separacionMedia = originalGradePoints.separacionMedia();
		trackLength = originalGradePoints.getLast()[0] - originalGradePoints.getX(0);
		integralVerticalProfilePoints = originalGradePoints.integrate(startz);

	}
	
	/**
	 * Realiza una reconstrucción única para unos valores concretos 
	 * de baseSize y thresholdSlope, recibidos en forma de 
	 * una instancia de ReconstructionParameters
	 * @param parameters
	 */
	public void processUnique(ReconstructionParameters parameters) {
		processUnique(parameters.getBaseSize(), parameters.getThresholdSlope());		
	}
	/**
	 * Realiza una reconstrucción única para unos valores concretos 
	 * de baseSize y thresholdSlope
	 * @param basesize
	 * @param thresholdslope
	 */
	public void processUnique(int basesize, double thresholdslope) {
		baseSize = basesize;
		thresholdSlope = thresholdslope;
		
		// Obtener originalVerticalProfilePoints mediante integración de originalGradePoints
		originalVerticalProfilePoints = originalGradePoints.integrate(startZ);
		
		// Caracterizar los puntos
		PointCharacteriser characteriser = new PointCharacteriser(strategy.getPointCharacteriserStrategy());
		PointTypeArray originalPointTypes = characteriser.characterise(originalGradePoints, baseSize, thresholdSlope); 

		// Agrupar en segmentos de puntos del mismo tipo
		typeIntervalArray = new TypeIntervalArray(originalPointTypes);
		
		// Obtener array de segmentos de puntos caracterizados en GRADE y VERTICALCURVE
		BorderIntervalsProcessor borderPointProcessor = new BorderIntervalsProcessor(strategy, MIN_LENGTH, MIN_POINTS_COUNT);
		typeIntervalArray = borderPointProcessor.processPoints(originalGradePoints, typeIntervalArray, baseSize, thresholdSlope);
		
		// Crear el diagrama de pendientes mediante una alineación para cada segemento de puntos
		resultGradeProfile = createGradeProfile(originalGradePoints, typeIntervalArray, thresholdSlope);
		
		// Ajustar finales y principios de alineaciones
		resultGradeProfile = adjustEndingsAndBeginnings(originalGradePoints, resultGradeProfile, startZ, thresholdSlope);
		
		// Obtener el perfil de pendientes mediante integración del diagrama de pendientes
		resultVerticalProfile = resultGradeProfile.integrate(startZ, thresholdSlope);
		
		// Calcular resultados agregados y errores
		resultVerticalProfilePoints = resultVerticalProfile.getSample(resultVerticalProfile.getStartS(), resultVerticalProfile.getEndS(), separacionMedia, true);
		calculateErrors(integralVerticalProfilePoints, resultVerticalProfilePoints);
	}
	/**
	 * Genera un perfil de pendientes a partir de un array de segmentos de tipos de puntos utilizando
	 * un GradeProfileCreator
	 * @param originalgradepoints puntos originales. Los utiliza el GradeProfileCreator 
	 * @param typeintervalarray Segmentos de puntos clasificados
	 * @param thresholdslope Lo utiliza el GradeProfileCreator_EqualArea
	 * @param strategy Sirve para seleccionar la instancia de GradeProfileCreator
	 * @return
	 */
	private VerticalGradeProfile createGradeProfile(XYVectorFunction originalgradepoints, TypeIntervalArray typeintervalarray, double thresholdslope) {
		GradeProfileCreator gradeProfileCreator = null;
		if(strategy.getInterpolationStrategyType() == InterpolationStrategyType.EqualArea) {
			gradeProfileCreator = new GradeProfileCreator_EqualArea(thresholdslope);
		} else if(strategy.getInterpolationStrategyType() == InterpolationStrategyType.EqualArea_Multiparameter) {
			gradeProfileCreator = new GradeProfileCreator_EqualArea(thresholdslope);
		} else {
			gradeProfileCreator = new GradeProfileCreator_LessSquares();	
		}
		VerticalGradeProfile gradeprofile = gradeProfileCreator.createGradeProfile(originalgradepoints, typeintervalarray);
		return gradeprofile;
	}

	private VerticalGradeProfile adjustEndingsAndBeginnings(XYVectorFunction originalgpoints, VerticalGradeProfile gprofile, double startz, double thresholdslope) {
		boolean changes = true;
		VerticalGradeProfile process = new VerticalGradeProfile();
		process.addAll(gprofile);
		
		while(changes) {
			changes=false;
			process = adjustEndingsWithBeginnings(originalgpoints, process, thresholdslope);
			VerticalProfile verticalProfile = process.integrate(startz, thresholdslope);
			
			for(int i=1; i<verticalProfile.size(); i++) {
				VAlignment current = verticalProfile.get(i); 
				VAlignment previous = verticalProfile.get(i-1);	
				double previousA2 = previous.getPolynom2().getA2();
				double currentA2 = current.getPolynom2().getA2();
				//double previousK = previous.getPolynom2().getKv();
				//double currentK = current.getPolynom2().getKv();
				double s1 = process.get(i-1).getStartS();
				double g1 = process.get(i-1).getStartZ();
				double s2 = process.get(i-1).getEndS();
				double g21 = process.get(i-1).getEndZ();
				double g22 = process.get(i).getStartZ();
				double s3 = process.get(i).getEndS();
				double g3 = process.get(i).getEndZ();
				double area = 0.5*(g1+g21)*(s2-s1) + 0.5*(g22+g3)*(s3-s2);
				if(currentA2==0 && previousA2==0) {
					double newg = area / (s3-s1);
					Straight r = new Straight(newg, 0.0);
					GradeProfileAlignment align = new GradeProfileAlignment(r, s1, s3);
					process.remove(i-1);
					process.set(i-1, align);
					changes = true;
					break;
//				} else if (isSimilarKv(previousK, currentK)) {
//					double newg = 2*area/(s3-s1) - g1;
//					Straight r = new Straight(newg, 0.0);
//					GradeProfileAlignment align = new GradeProfileAlignment(r, s1, s3);
//					process.remove(i-1);
//					process.set(i-1, align);
//					changes = true;
//					break;
				}
			}
		}
		gprofile = new VerticalGradeProfile();
		gprofile.addAll(process);
		return gprofile;
	}
	private VerticalGradeProfile adjustEndingsWithBeginnings(XYVectorFunction originalgpoints, VerticalGradeProfile profile, double thresholdslope) {
		EndingsWithBeginnersAdjuster adjuster = null;
		if(strategy.getInterpolationStrategyType() == InterpolationStrategyType.EqualArea) {
			adjuster = new EndingsWithBeginnersAdjuster_EqualArea(originalgpoints, thresholdslope);
		} else if (strategy.getInterpolationStrategyType() == InterpolationStrategyType.EqualArea_Multiparameter) {
			adjuster = new EndingsWithBeginnersAdjuster_EqualArea(originalgpoints, thresholdslope);
		} else {
			adjuster = new EndingsWithBeginnersAdjuster_LessSquares();
		}
		VerticalGradeProfile result = adjuster.adjustEndingsWithBeginnings(profile);
		return result;
	}
	/**
	 * Realiza una reconstrucción mediante un proceso iterativo,
	 * Probando varios valores de baseSize y thresholdSlope y
	 * seleccionando el que de menor ecm
	 */
	public void processIterative() {
		int maxBaseSize = calculateMaxBaseSize();
		
		int numBaseSizes = maxBaseSize - 2;
		int numThresholdSlopes = thresholdSlopes.length;
		int numTests = numBaseSizes * numThresholdSlopes;
		results = new double[numTests][3];
		double ecmMin = -1.0;
		bestTest = 0;
		int contador = 0;
		for (int i=3; i<=maxBaseSize; i++) {
			for (int j=0; j<thresholdSlopes.length; j++) {
				System.out.println("Test: " + contador + " BaseSize: " + i + ", thresholdSlope: " + thresholdSlopes[j]);
				processUnique( i, thresholdSlopes[j]);
				VerticalGradeProfile gradeProfile = getGradeProfile();
				if(gradeProfile == null || gradeProfile.size()<1) {
					Log.warn("gradeProfile null");
					//continue;
				}
				double ecm = getEcm();
				results[contador][0] = i; // baseSize;
				results[contador][1] = thresholdSlopes[j]; // thresholdSlope
				results[contador][2] = ecm; // ecm
				if(!Double.isNaN(ecm)) {
					if(ecmMin == -1.0) {
						ecmMin = ecm;
						bestTest = contador;
					} else {
						if (ecm < ecmMin) {
							ecmMin = ecm;
							bestTest = contador;
						}					
					}
				}
				contador ++;
			}
		}
		System.out.println("BestTest: " + bestTest);
		System.out.println("BaseSize: " + results[bestTest][0]);
		System.out.println("ThresholdSlope: " + results[bestTest][1]);
		System.out.println("ECM: " + results[bestTest][2]);
		
		processUnique((int)results[bestTest][0] , results[bestTest][1]);

	}
	private int calculateMaxBaseSize() {
		//int maxBaseSize = (int)Math.rint(MIN_LENGTH / separacionMedia);
		//if(maxBaseSize < 3) {
			//maxBaseSize = 10;
		//}
		double sep = separacionMedia;
		int num = (int)Math.rint(100.0/sep);
		return num+2;
//		int maxBaseSize = 200;
//		int maxbysize = originalGradePoints.size() / 2;
//		if(!MathUtil.isEven(originalGradePoints.size())) {
//			maxbysize = maxbysize + 1;
//		}
//		if(maxbysize < maxBaseSize) {
//			maxBaseSize = maxbysize;
//		}
//		if(maxBaseSize<3) {
//			System.out.println("Aquí");
//		}
//		return maxBaseSize;
	}
	
	/**
	 * Calcula valores agregados y errores.
	 * Utiliza resultVerticalProfile y integralVerticalProfilePoints
	 * Asigna valor a variables globales de la clase: alignmentCount, maxError,
	 * meanError, varianza y ecm 
	 */
	protected void calculateErrors(XYVectorFunction originalVProfilePoints, XYVectorFunction resultVProfilePoints) {
		alignmentCount = countGradeAlignments();
		double sumaErrorAbsoluto = 0.0;
		maxError = 0.0;
		int countnan = 0;
		double sumaErrorAbsolutoAlCuadrado = 0.0;
		for(int i=0; i<getPointsCount(); i++) {
			double x = resultVProfilePoints.getX(i);
			double errorAbsoluto = Math.abs(resultVProfilePoints.getY(x) - originalVProfilePoints.getY(x));
			if(Double.isNaN(errorAbsoluto)) {
				countnan ++;
				continue;
			}
			sumaErrorAbsoluto = sumaErrorAbsoluto + errorAbsoluto;
			sumaErrorAbsolutoAlCuadrado = sumaErrorAbsolutoAlCuadrado + errorAbsoluto * errorAbsoluto;
			if(errorAbsoluto > maxError) {
				maxError = errorAbsoluto;
			}
		}
		double counter = (double)getPointsCount() - countnan;
		meanError = sumaErrorAbsoluto / counter;
		varianza = sumaErrorAbsolutoAlCuadrado / counter - meanError*meanError;
		ecm = resultVProfilePoints.ecm(originalVProfilePoints);
	}
	private boolean isSimilarKv(double k1, double k2) {
		if(Double.isNaN(k1) || Double.isNaN(k2) || !sameSign(k1,k2)) {
			return false;
		}
		double dif = Math.abs(k1-k2);
		double perc = Math.abs(dif/k1);
		if(perc<0.03) {
			return true;
		} else {
			return false;
		}
	}
	private boolean sameSign(double k1, double k2) {
		if(k1*k2 > 0) {
			return true;
		} else {
			return false;
		}
	}
	private int countGradeAlignments() {
		int count = 0;
		for(int i=0; i<resultGradeProfile.size(); i++) {
			GradeProfileAlignment align = resultGradeProfile.get(i);
			double slope = align.getSlope();
			if(slope==0.0) {
				count++;
			}
		}
		return count;
	}
	
	
	
	// Getters
	public VerticalProfile getVerticalProfile() {
		return resultVerticalProfile;
	}
	
	public XYVectorFunction getOriginalGradePoints() {
		return originalGradePoints;
	}
	public XYVectorFunction getOriginalVerticalProfilePoints() {
		return originalVerticalProfilePoints;
	}
	
	public TypeIntervalArray getTypeIntervalArray() {
		return typeIntervalArray;
	}
	public VerticalGradeProfile getGradeProfile() {
		return resultGradeProfile;
	}

	public double getSeparacionMedia() {
		return separacionMedia;
	}
	public double getTrackLength() {
		return trackLength;
	}
	public double getMeanError() {
		return meanError;
	}
	public double getVarianza() {
		return varianza;
	}
	public double getEcm() {
		return ecm;
	}
	public double getMaxError() {
		return maxError;
	}
	public int getPointsCount() {
		return originalGradePoints.size();
	}
	public int getBaseSize() {
		return baseSize;
	}
	public double getThresholdSlope() {
		return thresholdSlope;
	}
	public int getAlignmentCount() {
		return resultVerticalProfile.size();
	}

	public int getBestTest() {
		return bestTest;
	}

	public double[][] getResults() {
		return results;
	}

	public double getMIN_LENGTH() {
		return MIN_LENGTH;
	}

	public void setMIN_LENGTH(double mIN_LENGTH) {
		MIN_LENGTH = mIN_LENGTH;
	}

	public double[] getThresholdSlopes() {
		return thresholdSlopes;
	}

	public void setThresholdSlopes(double[] thresholdSlopes) {
		this.thresholdSlopes = thresholdSlopes;
	}

	public double getStartZ() {
		return startZ;
	}

	public void setStartZ(double startZ) {
		this.startZ = startZ;
	}

	public XYVectorFunction getIntegralVerticalProfilePoints() {
		return integralVerticalProfilePoints;
	}

	public InterpolationStrategy getInterpolationStrategy() {
		return strategy;
	}

	public VerticalProfile getResultVerticalProfile() {
		return resultVerticalProfile;
	}

	public XYVectorFunction getResultVerticalProfilePoints() {
		return resultVerticalProfilePoints;
	}

	public double getStartX() {
		return startX;
	}

	public double getEndX() {
		return endX;
	}

	public void setThresholdSlope(double thresholdSlope) {
		this.thresholdSlope = thresholdSlope;
	}
}
