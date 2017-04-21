package com.mlab.pg.essays.roads.M325;

import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.EssayData;
import com.mlab.pg.reconstruction.ReconstructRunner;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;
import com.mlab.pg.trackprocessor.TrackUtil;
import com.mlab.pg.xyfunction.XYVectorFunction;


/**
  * @author shiguera
 *
 */
public class Essay_24_M325_Asc_Topcon {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public Essay_24_M325_Asc_Topcon() {
		
		essayData = new EssayData();
		essayData.setEssayName("Ensayo 24.- M-325 Ascendente - GPS Topcon - Puntos a 2 m");
		essayData.setCarretera("M-513");
		essayData.setSentido("Ascendente");
		essayData.setGraphTitle(essayData.getEssayName());
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M325");
		essayData.setOutPath(essayData.getInPath());
		essayData.setXyzFileName("M325_ETRS89.csv");

		double space = 1.0;
		String sgfilename = "M325_ETRS89_" + String.format("%02.0fm", space)+ "_SG.csv";
		essayData.setSgFileName(sgfilename);
		String szfilename = "M325_ETRS89_" + String.format("%02.0fm", space)+ "_SG.csv";
		essayData.setSzFileName(szfilename);
		essayData.setReportFileName("Essay_24_M325_Asc_Topcon" + String.format("%02.0fm", space)+ ".txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);

		essayData.setStartS(space);
		essayData.setEndS(4750-space);
		
		
		recRunner = new ReconstructRunner(essayData);		
		//recRunner.setStartZ();
		//recRunner.setzMin(586.873);
		//recRunner.setzMax(715.685);
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		Essay_24_M325_Asc_Topcon essay = new Essay_24_M325_Asc_Topcon();
		essay.doIterative();
		//essay.doMultiparameter();
		//essay.doUnique(29, 1.0e-4);
		
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
