package com.mlab.pg.essays.roads.M607;

import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.EssayData;
import com.mlab.pg.reconstruction.ReconstructRunner;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;


/**
 * Ensayo: M-607, track Leika, Ascendente, eje promediado
 * @author shiguera
 *
 */
public class M607_Essay_16_PdtesMFOM {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public M607_Essay_16_PdtesMFOM() {
		
		essayData = new EssayData();
		essayData.setEssayName("M-607 Ensayo 16: PK 8+630 al 58+380 (Madrid) - Pendientes MFOM");
		essayData.setCarretera("M-607");
		essayData.setSentido("Asscendente");
		essayData.setGraphTitle(essayData.getEssayName());
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/PdtesMFOM");
		essayData.setOutPath(essayData.getInPath());
		essayData.setXyzFileName("");
		essayData.setSgFileName("M607_SG.csv");
		essayData.setSzFileName("M607_SZ.csv");
		essayData.setReportFileName("M-607-Ensayo_16.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		//essayData.setStartS(2500.0);
		//essayData.setEndS(7000.0);
		
		recRunner = new ReconstructRunner(essayData);		
		recRunner.setMinLength(30.0);
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		M607_Essay_16_PdtesMFOM essay = new M607_Essay_16_PdtesMFOM();
		//essay.doIterative();
		//essay.doMultiparameter();
		essay.doUnique(5, 5e-5);
		
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
