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
public class Essay_13_M607_Asc_RoadRecorder {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public Essay_13_M607_Asc_RoadRecorder() {
		
		essayData = new EssayData();
		essayData.setEssayName("Ensayo 13.- M-607 Ascendente - RoadRecorder - Traza completa");
		essayData.setGraphTitle(essayData.getEssayName());
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksRoadRecorder");
		essayData.setOutPath(essayData.getInPath());
		essayData.setXyzFileName("M607_Asc_1.csv");
		essayData.setSgFileName(TrackUtil.generateSGFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setSzFileName(TrackUtil.generateSZFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setReportFileName("Essay_13_M607_Asc_RoadRecorder.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		//essayData.setStartS(4300.0);
		//essayData.setEndS(8000.0);
		
		recRunner = new ReconstructRunner(essayData);		
		
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		Essay_13_M607_Asc_RoadRecorder essay = new Essay_13_M607_Asc_RoadRecorder();
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
