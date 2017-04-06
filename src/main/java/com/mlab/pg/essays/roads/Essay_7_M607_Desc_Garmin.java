package com.mlab.pg.essays.roads;

import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.EssayData;
import com.mlab.pg.ReconstructRunner;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;
import com.mlab.pg.trackprocessor.TrackUtil;


/**
 * Ensayo: M-607, track Leika, Ascendente, eje promediado
 * @author shiguera
 *
 */
public class Essay_7_M607_Desc_Garmin {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public Essay_7_M607_Desc_Garmin() {
		
		essayData = new EssayData();
		essayData.setEssayName("Ensayo 7.- M-607 Descendente - GPS Garmin - Traza completa");
		essayData.setGraphTitle("Ensayo 7.- M-607 Descendente - GPS Garmin - Traza completa");
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksGarmin");
		essayData.setOutPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksGarmin");
		essayData.setXyzFileName("M607_Desc_2017-03-10.csv");
		essayData.setSgFileName(TrackUtil.generateSGFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setSzFileName(TrackUtil.generateSZFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setReportFileName("Essay_7_M607_Desc_Garmin.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		//essayData.setStartS(4300.0);
		//essayData.setEndS(8000.0);
		
		recRunner = new ReconstructRunner(essayData);		
		
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		Essay_7_M607_Desc_Garmin essay = new Essay_7_M607_Desc_Garmin();
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
