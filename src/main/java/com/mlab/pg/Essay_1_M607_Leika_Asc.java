package com.mlab.pg;

import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;
import com.mlab.pg.trackprocessor.TrackUtil;


/**
 * Ensayo: M-607, track Leika, Ascendente, track completo
 * @author shiguera
 *
 */
public class Essay_1_M607_Leika_Asc {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public Essay_1_M607_Leika_Asc() {
		
		essayData = new EssayData();
		essayData.setEssayName("Ensayo 1: M-607 - GPS LEika - Ascendente 1");
		essayData.setCarretera("M-607");
		essayData.setSentido("Ascendente");
		essayData.setGraphTitle("M-607 - GPS LEika - Ascendente 1");
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksLeikaMaria");
		essayData.setOutPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksLeikaMaria");
		essayData.setXyzFileName("M607_Leika_1_xyz.csv");
		essayData.setSgFileName(TrackUtil.generateSGFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setSzFileName(TrackUtil.generateSZFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setReportFileName("M607_Leika_1_1.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		//essayData.setStartS(1000.0);
		//essayData.setEndS(5000.0);
		
		recRunner = new ReconstructRunner(essayData);		
		
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		Essay_1_M607_Leika_Asc essay = new Essay_1_M607_Leika_Asc();
		essay.doIterative();
		//essay.doMultiparameter();
		
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
	
}