package com.mlab.pg;

import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.graphics.FunctionDisplayer;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;
import com.mlab.pg.trackprocessor.TrackUtil;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.xyfunction.XYVectorFunction;


/**
 * Ensayo: M-607, track Leika, Ascendente, eje promediado
 * @author shiguera
 *
 */
public class Essay_4_M607_Leika_Asc_Axis_prueba {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public Essay_4_M607_Leika_Asc_Axis_prueba() {
		
		essayData = new EssayData();
		essayData.setEssayName("M-607 Ascendente - GPS LEika - Eje promediado");
		essayData.setGraphTitle("M-607 Ascendente - GPS LEika - Eje promediado");
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksLeikaMaria");
		essayData.setOutPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksLeikaMaria");
		essayData.setXyzFileName("M607_Leika_Axis_xyz_prueba.csv");
		essayData.setSgFileName(TrackUtil.generateSGFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setSzFileName(TrackUtil.generateSZFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setReportFileName("M607_Leika_Axis_4_prueba.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		essayData.setStartS(4000.0);
		essayData.setEndS(4500.0);
		
		recRunner = new ReconstructRunner(essayData);		
		
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		Essay_4_M607_Leika_Asc_Axis_prueba essay = new Essay_4_M607_Leika_Asc_Axis_prueba();
		
		//essay.doIterative();
		//essay.doMultiparameter();
		essay.doUnique(4, 1e-4);
		
		recRunner.showReport();
		recRunner.printReport();
		recRunner.showProfiles();

		System.out.println(recRunner.getResultGProfile());
		System.out.println(recRunner.getResultVProfile());
	}

	private void doUnique(int base, double th) {
		recRunner.doUniqueReconstruction(base, th);
		stringReport = recRunner.getStringReport();
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
