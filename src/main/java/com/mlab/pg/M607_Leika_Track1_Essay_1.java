package com.mlab.pg;

import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.graphics.FunctionDisplayer;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategy;
import com.mlab.pg.trackprocessor.TrackUtil;
import com.mlab.pg.util.IOUtil;

public class M607_Leika_Track1_Essay_1 {

	
	static ReconstructEssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		M607_Leika_Track1_Essay_1 essay = new M607_Leika_Track1_Essay_1();
		essay.doMultiparameter();
		showReport();
		printReport();
		FunctionDisplayer displayer = new FunctionDisplayer();
		displayer.showTwoFunctions(recRunner.getvProfileFromGradeDataIntegration(), recRunner.getResultVProfilePoints(), 
				essayData.getGraphTitle(), "Original Vertical Profile", "Result Vertical Profile", "S", "Z");
	}

	public M607_Leika_Track1_Essay_1() {
		
		essayData = new ReconstructEssayData();
		essayData.setEssayName("M-607 - GPS LEika - Ascendente 1");
		essayData.setGraphTitle("M-607 - GPS LEika - Ascendente 1");
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksLeikaMaria");
		essayData.setOutPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksLeikaMaria");
		essayData.setXyzFileName("M607_Leika_1_xyz.csv");
		essayData.setSgFileName(TrackUtil.generateSGFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setSzFileName(TrackUtil.generateSZFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setReportFileName("M607_Leika_1.txt");
		essayData.setInterpolationStrategy(InterpolationStrategy.EqualArea);
		
		recRunner = new ReconstructRunner(essayData);		
		
	}
	private void doIterative() {
		recRunner.doIterativeReconstruction();
		stringReport = recRunner.getStringReport();
	}
	private void doMultiparameter() {
		recRunner.doMultiparameterReconstruction();
		stringReport = recRunner.getStringReport();
	}
	private static void printReport() {
		String filename = IOUtil.composeFileName(essayData.getInPath(), essayData.getReportFileName());
		IOUtil.write(filename, stringReport);
	}
	private static void showReport() {
		System.out.println(stringReport);
	}

}
