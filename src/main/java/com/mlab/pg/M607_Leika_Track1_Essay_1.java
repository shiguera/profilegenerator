package com.mlab.pg;

import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;
import com.mlab.pg.trackprocessor.TrackUtil;


/**
 * Ensayo: M-607, track Leika, Ascendente, track completo
 * @author shiguera
 *
 */
public class M607_Leika_Track1_Essay_1 {

	
	static ReconstructEssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public M607_Leika_Track1_Essay_1() {
		
		essayData = new ReconstructEssayData();
		essayData.setEssayName("M-607 - GPS LEika - Ascendente 1");
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

		
		M607_Leika_Track1_Essay_1 essay = new M607_Leika_Track1_Essay_1();
		//essay.doIterative();
		essay.doMultiparameter();
		
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
