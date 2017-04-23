package com.mlab.pg.essays.roads.M607.Leica;

import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.EssayData;
import com.mlab.pg.graphics.FunctionDisplayer;
import com.mlab.pg.reconstruction.ReconstructRunner;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;
import com.mlab.pg.trackprocessor.TrackUtil;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.xyfunction.XYVectorFunction;


/**
 * Ensayo: M-607, track Leika, Ascendente, track completo
 * @author shiguera
 *
 */
public class M607_Leica_Desc_TramoSelec {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public M607_Leica_Desc_TramoSelec() {
		
		essayData = new EssayData();
		essayData.setEssayName("M-607 Descendente - GPS Leica");
		essayData.setGraphTitle("M-607 Descendente - GPS Leica");
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksLeikaMaria");
		essayData.setOutPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksLeikaMaria");
		essayData.setXyzFileName("M607_Leika_2_xyz.csv");
		essayData.setSgFileName(TrackUtil.generateSGFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setSzFileName(TrackUtil.generateSZFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setReportFileName("M607_Leika_2_1.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		essayData.setStartS(0.0);
		essayData.setEndS(9550.0);
		
		recRunner = new ReconstructRunner(essayData);		
		recRunner.setMinLength(40.0);
		recRunner.setMAX_BASE_LENGTH(300.0);
		double[] thresholdSlopes = new double[] {1.0e-4, 8e-5, 7e-5, 6.5e-5, 6e-5, 5.5e-5, 5e-5, 4e-5, 3.75e-5, 3.5e-5, 3.25e-5, 3e-5, 2.75e-5, 2.5e-5, 2.25e-5, 2e-5, 1e-5, 1.5e-6, 1e-6, 1.5e-7, 1e-7}; 
		recRunner.setThresholdSlopes(thresholdSlopes);		
		
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		M607_Leica_Desc_TramoSelec essay = new M607_Leica_Desc_TramoSelec();
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
