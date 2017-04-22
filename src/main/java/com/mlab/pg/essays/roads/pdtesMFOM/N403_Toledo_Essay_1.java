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
public class N403_Toledo_Essay_1 {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public N403_Toledo_Essay_1() {
		
		essayData = new EssayData();
		essayData.setEssayName("N-403 Tramo Toledo-Rielves (Toledo) - PK 5+000 al 20+000 - Pendientes MFOM");
		essayData.setCarretera("N-403");
		essayData.setSentido("Asscendente");
		essayData.setGraphTitle(essayData.getEssayName());
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/N-403");
		essayData.setOutPath(essayData.getInPath());
		essayData.setXyzFileName("");
		essayData.setSgFileName("N-403_Toledo_SG.csv");
		essayData.setSzFileName("N-403_Toledo_SZ.csv");
		essayData.setReportFileName("N-403-Toledo.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		//essayData.setStartS(2500.0);
		//essayData.setEndS(7000.0);
		
		recRunner = new ReconstructRunner(essayData);		
		recRunner.setMinLength(50.0);
		recRunner.setMAX_BASE_LENGTH(300.0);
		double[] thresholdSlopes = new double[] {1.0e-4,1e-5,1e-6, 5e-7, 1e-7, 5e-8, 1e-8}; 
		recRunner.setThresholdSlopes(thresholdSlopes);
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		N403_Toledo_Essay_1 essay = new N403_Toledo_Essay_1();
		essay.doIterative();
		//essay.doMultiparameter();
		//essay.doUnique(104, 1.75e-5);
		
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
