package com.mlab.pg.essays.roads.M513.RoadRecorder;

import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.EssayData;
import com.mlab.pg.reconstruction.ReconstructRunner;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;
import com.mlab.pg.trackprocessor.TrackUtil;


/**
 * Ensayo: M-607, track Leika, Ascendente, eje promediado
 * @author shiguera
 *
 */
public class Essay_17_M513_Desc_RoadRecorder {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public Essay_17_M513_Desc_RoadRecorder() {
		
		essayData = new EssayData();
		essayData.setEssayName("Ensayo 17.- M-613 Descendente - RoadRecorder");
		essayData.setCarretera("M-513");
		essayData.setSentido("Ascendente");
		essayData.setGraphTitle(essayData.getEssayName());
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M513");
		essayData.setOutPath(essayData.getInPath());
		essayData.setXyzFileName("20130627_133341.csv");
		essayData.setSgFileName(TrackUtil.generateSGFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setSzFileName(TrackUtil.generateSZFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setReportFileName("Essay_17_M513_Desc_RoadRecorder.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		//essayData.setStartS(4300.0);
		//essayData.setEndS(8000.0);
		
		recRunner = new ReconstructRunner(essayData);		
		recRunner.setMinLength(0.0);
		recRunner.setMAX_BASE_LENGTH(300.0);
		double[] thresholdSlopes = new double[] {1.0e-4, 3e-5, 2.5e-5, 2e-5, 1.75e-5, 1.5e-5, 1.25e-5, 1e-5, 1.5e-6, 1e-6, 1e-7}; 
		recRunner.setThresholdSlopes(thresholdSlopes);			
		
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		Essay_17_M513_Desc_RoadRecorder essay = new Essay_17_M513_Desc_RoadRecorder();
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
