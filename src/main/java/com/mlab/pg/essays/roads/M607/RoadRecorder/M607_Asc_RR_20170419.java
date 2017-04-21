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
public class M607_Asc_RR_20170419 {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public M607_Asc_RR_20170419() {
		
		essayData = new EssayData();
		essayData.setEssayName("m-607 - Ascendente - GPS RR Nexus - 20170419");
		essayData.setCarretera("M-608");
		essayData.setSentido("Ascendente");
		essayData.setGraphTitle(essayData.getEssayName());
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/RoadRecorder");
		essayData.setOutPath(essayData.getInPath());
		essayData.setXyzFileName("M607_Asc_20170419.csv");
		essayData.setSgFileName(TrackUtil.generateSGFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setSzFileName(TrackUtil.generateSZFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setReportFileName("M607_Asc_20170419.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		//essayData.setStartS(1333.0);
		//essayData.setEndS(6548.0);
		
		recRunner = new ReconstructRunner(essayData);		
		recRunner.setMinLength(0.0);
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		M607_Asc_RR_20170419 essay = new M607_Asc_RR_20170419();
		essay.doIterative();
		//essay.doMultiparameter();
		//essay.doUnique(104, 1.75e-5);
		
		recRunner.showReport();
		recRunner.printReport();
		recRunner.showProfiles();
		System.out.println(recRunner.getzMin() + ", " + recRunner.getzMax());
		System.out.println(recRunner.getOriginalVProfile().getY(2.0));
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
