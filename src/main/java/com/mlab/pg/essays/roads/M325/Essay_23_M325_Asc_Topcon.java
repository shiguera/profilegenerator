package com.mlab.pg.essays.roads.M325;

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
public class Essay_23_M325_Asc_Topcon {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public Essay_23_M325_Asc_Topcon() {
		
		essayData = new EssayData();
		essayData.setEssayName("Ensayo 23.- M-325 Ascendente - GPS Topcon ");
		essayData.setCarretera("M-513");
		essayData.setSentido("Ascendente");
		essayData.setGraphTitle(essayData.getEssayName());
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M325");
		essayData.setOutPath(essayData.getInPath());
		essayData.setXyzFileName("M325_ETRS89.csv");
		essayData.setSgFileName(TrackUtil.generateSGFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setSzFileName(TrackUtil.generateSZFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setReportFileName("Essay_23_M325_Asc_Topcon.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		essayData.setStartS(0.0);
		essayData.setEndS(8300.0);
		
		recRunner = new ReconstructRunner(essayData);		
		
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		Essay_23_M325_Asc_Topcon essay = new Essay_23_M325_Asc_Topcon();
		//essay.doIterative();
		//essay.doMultiparameter();
		essay.doUnique(104, 1.75e-5);
		
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
