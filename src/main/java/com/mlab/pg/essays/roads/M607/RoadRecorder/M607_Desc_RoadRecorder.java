package com.mlab.pg.essays.roads.M607.RoadRecorder;

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
public class M607_Desc_RoadRecorder {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public M607_Desc_RoadRecorder() {
		
		essayData = new EssayData();
		essayData.setEssayName("Ensayo 14.- M-607 Descendente - RoadRecorder - Traza completa");
		essayData.setCarretera("M-607");
		essayData.setSentido("Descendente");
		essayData.setGraphTitle(essayData.getEssayName());
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksRoadRecorder");
		essayData.setOutPath(essayData.getInPath());
		essayData.setXyzFileName("M607_Desc_1.csv");
		essayData.setSgFileName(TrackUtil.generateSGFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setSzFileName(TrackUtil.generateSZFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setReportFileName("Essay_14_M607_Desc_RoadRecorder.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		//essayData.setStartS(4300.0);
		//essayData.setEndS(8000.0);
		
		recRunner = new ReconstructRunner(essayData);		
		recRunner.setMinLength(0.0);
		recRunner.setMAX_BASE_LENGTH(300.0);
		double[] thresholdSlopes = new double[] {1.0e-4, 8e-5, 6e-5, 4e-5, 3.75e-5, 3.5e-5, 3.25e-5, 3e-5, 2.75e-5, 2.5e-5, 2.25e-5, 2e-5, 1e-5, 1.5e-6, 1e-6, 1.5e-7, 1e-7}; 
		recRunner.setThresholdSlopes(thresholdSlopes);
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		M607_Desc_RoadRecorder essay = new M607_Desc_RoadRecorder();
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
