package com.mlab.pg.essays.roads.N403;

import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.EssayData;
import com.mlab.pg.reconstruction.ReconstructRunner;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;


/**
 * Ensayo: M-607, track Leika, Ascendente, eje promediado
 * @author shiguera
 *
 */
public class N403_Avila_Essay_1 {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public N403_Avila_Essay_1() {
		
		essayData = new EssayData();
		essayData.setEssayName("N-403 Ensayo 2: Tramo Casas del Burguillo - ElBarraco (PK 96+000 al 108+000) (Ávila) - Pendientes MFOM");
		essayData.setCarretera("N-403");
		essayData.setSentido("Asscendente");
		essayData.setGraphTitle(essayData.getEssayName());
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/N-403");
		essayData.setOutPath(essayData.getInPath());
		essayData.setXyzFileName("");
		essayData.setSgFileName("N-403_Avila_SG.csv");
		essayData.setSzFileName("N-403_Avila_SZ.csv");
		essayData.setReportFileName("N-403-Avila-Ensayo_1.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		//essayData.setStartS(2500.0);
		//essayData.setEndS(7000.0);
		
		recRunner = new ReconstructRunner(essayData);		
		
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		N403_Avila_Essay_1 essay = new N403_Avila_Essay_1();
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
