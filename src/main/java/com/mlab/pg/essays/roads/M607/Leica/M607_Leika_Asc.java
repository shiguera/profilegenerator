package com.mlab.pg.essays.roads.M607.Leica;

import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.EssayData;
import com.mlab.pg.reconstruction.ReconstructRunner;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;
import com.mlab.pg.trackprocessor.TrackUtil;


/**
 * Ensayo: M-607, track Leika, Ascendente, track completo
 * @author shiguera
 *
 */
public class M607_Leika_Asc {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public M607_Leika_Asc() {
		
		essayData = new EssayData();
		essayData.setEssayName("M-607 Ascendente - GPS Leica");
		essayData.setCarretera("M-607");
		essayData.setSentido("Ascendente");
		essayData.setGraphTitle("M-607 Ascendente - GPS LEika");
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksLeikaMaria");
		essayData.setOutPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksLeikaMaria");
		essayData.setXyzFileName("M607_Leika_1_xyz_ED50.csv");
		essayData.setSgFileName(TrackUtil.generateSGFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setSzFileName(TrackUtil.generateSZFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setReportFileName("M607_Asc_Leica.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		//essayData.setStartS(1000.0);
		//essayData.setEndS(5000.0);
		
		recRunner = new ReconstructRunner(essayData);		
		recRunner.setMinLength(44.0);
		recRunner.setMAX_BASE_LENGTH(300.0);
		double[] thresholdSlopes = new double[] {1.0e-4, 8e-5, 7e-5, 6.5e-5, 6e-5, 5.5e-5, 5e-5, 4e-5, 3.75e-5, 3.5e-5, 3.25e-5, 3e-5, 2.75e-5, 2.5e-5, 2.25e-5, 2e-5, 1e-5, 1.5e-6, 1e-6, 1.5e-7, 1e-7}; 
		recRunner.setThresholdSlopes(thresholdSlopes);		
		
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		M607_Leika_Asc essay = new M607_Leika_Asc();
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
