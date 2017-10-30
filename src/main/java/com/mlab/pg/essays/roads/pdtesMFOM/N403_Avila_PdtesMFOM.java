package com.mlab.pg.essays.roads.pdtesMFOM;

import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.EssayData;
import com.mlab.pg.reconstruction.ReconstructRunner;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;


/**
 * Ensayo: M-607, track Leika, Ascendente, eje promediado
 * @author shiguera
 *
 */
public class N403_Avila_PdtesMFOM {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public N403_Avila_PdtesMFOM() {
		
		essayData = new EssayData();
		essayData.setEssayName("N-403 Tramo Casas del Burguillo - ElBarraco (PK 96+000 al 108+000) (Ávila) - Pendientes MFOM");
		essayData.setCarretera("N-403");
		essayData.setSentido("Asscendente");
		essayData.setGraphTitle(essayData.getEssayName());
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/N-403");
		essayData.setOutPath(essayData.getInPath());
		essayData.setXyzFileName("");
		essayData.setSgFileName("N-403_Avila_SG.csv");
		essayData.setSzFileName("N-403_Avila_SZ.csv");
		essayData.setReportFileName("N-403-Avila.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		//essayData.setStartS(0.0);
		//essayData.setEndS(2000.0);
		
		recRunner = new ReconstructRunner(essayData);		
		recRunner.setMinLength(30.0);
		recRunner.setMAX_BASE_LENGTH(300.0);
		double[] thresholdSlopes = new double[] {1.0e-4,9e-5,8e-5, 7e-5, 6e-5, 5e-5, 4.5e-5, 4e-5, 3.75e-5, 3.5e-5, 3.25e-5, 3e-5, 2e-5, 1e-5, 1e-6 }; 
		recRunner.setThresholdSlopes(thresholdSlopes);

	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		N403_Avila_PdtesMFOM essay = new N403_Avila_PdtesMFOM();
		//essay.doIterative();
		//essay.doMultiparameter();
		essay.doUnique(15, 3.75e-5);
		
		recRunner.showReport();
		recRunner.printReport();
		recRunner.showProfiles();
	}

	
	private void doIterative() {
		recRunner.doIterativeReconstruction();
		stringReport = recRunner.getStringReport();
	}
	private void doMultiparameter() {
		recRunner.doMultiparameterReconstruction();
		stringReport = recRunner.getStringReport();
	}
	private void doUnique(int base, double th) {
		recRunner.doUniqueReconstruction(base, th);
		stringReport = recRunner.getStringReport();
	}
}
