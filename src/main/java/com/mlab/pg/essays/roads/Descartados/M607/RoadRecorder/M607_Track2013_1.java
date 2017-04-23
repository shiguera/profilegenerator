package com.mlab.pg.essays.roads.Descartados.M607.RoadRecorder;

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
public class M607_Track2013_1 {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public M607_Track2013_1() {
		
		essayData = new EssayData();
		essayData.setEssayName("M-607 Desc Traza 2013_1 - GPS Samsung");
		essayData.setCarretera("M-607");
		essayData.setSentido("Descendente");
		essayData.setGraphTitle(essayData.getEssayName());
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksRoadRecorder/2013");
		essayData.setOutPath(essayData.getInPath());
		essayData.setXyzFileName("20130427_115603_xyz.csv");
		essayData.setSgFileName(TrackUtil.generateSGFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setSzFileName(TrackUtil.generateSZFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setReportFileName("20130427_115603.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		//essayData.setStartS(2500.0);
		//essayData.setEndS(7000.0);
		
		recRunner = new ReconstructRunner(essayData);		
		recRunner.setMinLength(10.0);
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		M607_Track2013_1 essay = new M607_Track2013_1();
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
