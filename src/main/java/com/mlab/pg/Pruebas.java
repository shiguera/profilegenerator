package com.mlab.pg;

import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.graphics.FunctionDisplayer;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;
import com.mlab.pg.trackprocessor.TrackUtil;
import com.mlab.pg.util.IOUtil;

public class Pruebas {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		Pruebas essay = new Pruebas();
		essay.doMultiparameter();
		showReport();
		//printReport();
		FunctionDisplayer displayer = new FunctionDisplayer();
		displayer.showTwoFunctions(recRunner.getvProfileFromGradeDataIntegration(), recRunner.getResultVProfilePoints(), 
				essayData.getGraphTitle(), "Original Vertical Profile", "Result Vertical Profile", "S", "Z");
	}

	public Pruebas() {
		
		essayData = new EssayData();
		essayData.setEssayName("M-607 - GPS LEika - Ascendente 1");
		essayData.setGraphTitle("M-607 - GPS LEika - Ascendente 1");
		essayData.setInPath("/home/shiguera/ownCloud/workspace/roads/ProfileGenerator");
		essayData.setOutPath("/home/shiguera/ownCloud/workspace/roads/ProfileGenerator");
		essayData.setXyzFileName("alignment2.csv");
		essayData.setSgFileName("alignment2.csv");
		essayData.setSzFileName("");
		essayData.setReportFileName("alignment2.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		
		recRunner = new ReconstructRunner(essayData);		
		
	}
	private void doIterative() {
		recRunner.doIterativeReconstruction();
		stringReport = recRunner.getStringReport();
	}
	private void doMultiparameter() {
		recRunner.doMultiparameterReconstruction();
	}
	private static void printReport() {
		String filename = IOUtil.composeFileName(essayData.getInPath(), essayData.getReportFileName());
		IOUtil.write(filename, stringReport);
	}
	private static void showReport() {
		System.out.println(stringReport);
	}

}
