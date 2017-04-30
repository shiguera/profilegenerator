package com.mlab.pg.essays.roads.M607.Topcon;

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
public class M607_Asc_2_Topcon {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public M607_Asc_2_Topcon() {
		
		essayData = new EssayData();
		essayData.setEssayName("M-607 Descendente - GPS Topcon");
		essayData.setCarretera("M-607");
		essayData.setSentido("Asscendente");
		essayData.setGraphTitle(essayData.getEssayName());
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/M-607/GPS_LuisIglesias");
		essayData.setOutPath(essayData.getInPath());
		essayData.setXyzFileName("M607_topcon_2.csv");
		essayData.setSgFileName(TrackUtil.generateSGFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setSzFileName(TrackUtil.generateSZFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setReportFileName("M-607_Desc_topcon_2.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		essayData.setStartS(18206.0);
		essayData.setEndS(19277.0);
		
		recRunner = new ReconstructRunner(essayData);		
		recRunner.setMinLength(65.0);
		recRunner.setMAX_BASE_LENGTH(200.0);
		double[] thresholdSlopes = new double[] {1.0e-4, 3e-5, 2e-5, 1.75e-5, 1.5e-5, 1.25e-5, 1e-5, 1.5e-6}; 
		recRunner.setThresholdSlopes(thresholdSlopes);	

	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		M607_Asc_2_Topcon essay = new M607_Asc_2_Topcon();
		essay.doIterative();
		//essay.doMultiparameter();
		//essay.doUnique(10, 5e-5);
		
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
