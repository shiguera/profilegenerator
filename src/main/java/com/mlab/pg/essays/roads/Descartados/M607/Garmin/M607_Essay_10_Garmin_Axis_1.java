package com.mlab.pg.essays.roads.Descartados.M607.Garmin;

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
public class M607_Essay_10_Garmin_Axis_1 {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public M607_Essay_10_Garmin_Axis_1() {
		
		essayData = new EssayData();
		essayData.setEssayName("Ensayo 10.- M-607 Descendente - GPS Garmin - Eje promediado dos trazas");
		essayData.setGraphTitle("Ensayo 10.- M-607 Descendente - GPS Garmin - Eje promediado dos trazas");
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksGarmin");
		essayData.setOutPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksGarmin");
		essayData.setXyzFileName("M607_Garmin_2017-03-09_Axis.csv");
		essayData.setSgFileName(TrackUtil.generateSGFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setSzFileName(TrackUtil.generateSZFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setReportFileName("Essay_10_M607_Garmin_Axis.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		//essayData.setStartS(4300.0);
		//essayData.setEndS(8000.0);
		
		recRunner = new ReconstructRunner(essayData);		
		recRunner.setMinLength(0.0);
		recRunner.setMAX_BASE_LENGTH(300.0);
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		M607_Essay_10_Garmin_Axis_1 essay = new M607_Essay_10_Garmin_Axis_1();
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
