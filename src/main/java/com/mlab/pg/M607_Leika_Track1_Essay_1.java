package com.mlab.pg;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.graphics.FunctionDisplayer;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategy;
import com.mlab.pg.trackprocessor.TrackUtil;
import com.mlab.pg.util.IOUtil;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfileWriter;
import com.mlab.pg.xyfunction.XYVectorFunction;

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
		VerticalGradeProfile gp = recRunner.getResultGProfile();
		double x1 = gp.getStartS();
		double x2 = gp.getEndS();
		XYVectorFunction data = recRunner.getResultGProfile().getSample(x1,x2,20,true);
		displayer.showFunction(data, "Prueba", "Grades", "S",  "G");
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
		essayData.setStartS(1000.0);
		essayData.setEndS(5000.0);
		
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
		File file = new File(filename);
		VerticalProfileWriter.writeVerticalProfile(file, recRunner.getResultVProfile(), stringReport);

	}
	private static void showReport() {
		System.out.println(stringReport);
	}

}
