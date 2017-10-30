package com.mlab.pg.essays.roads.M325;

import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.EssayData;
import com.mlab.pg.reconstruction.ReconstructRunner;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;


/**
 * Ensayo: M-607, track Leika, Ascendente, eje promediado
 * @author shiguera
 *
 */
public class M325_Asc_Topcon_1 {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public M325_Asc_Topcon_1() {
		
		essayData = new EssayData();
		essayData.setEssayName("M-325 - Zoom from PK 1+050 to PK 1+750");
		essayData.setCarretera("M-325");
		essayData.setSentido("Ascendente");
		essayData.setGraphTitle(essayData.getEssayName());
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M325");
		essayData.setOutPath(essayData.getInPath());
		essayData.setXyzFileName("M325_ETRS89_xyz.csv");
		//essayData.setSgFileName(TrackUtil.generateSGFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		//essayData.setSzFileName(TrackUtil.generateSZFileFromXYZFile(essayData.getInPath(), essayData.getXyzFileName(), 1));
		essayData.setSgFileName("M325_ETRS89_xyz_SG.csv");
		essayData.setSzFileName("M325_ETRS89_xyz_SZ.csv");

		essayData.setReportFileName("M325_Asc_RoadRecorder.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		essayData.setStartS(400.0);
		essayData.setEndS(4126.0);
		
		recRunner = new ReconstructRunner(essayData);		
		recRunner.setMinLength(50.0);
		recRunner.setMAX_BASE_LENGTH(100.0);
		double[] thresholdSlopes = new double[] {1.5e-4, 1.0e-4, 1.5e-5, 1e-5, 1e-6, 1e-7}; 
		recRunner.setThresholdSlopes(thresholdSlopes);	
		
		
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		M325_Asc_Topcon_1 essay = new M325_Asc_Topcon_1();
		//essay.doIterative();
		//essay.doMultiparameter();
		essay.doUnique(26, 1.0e-4);
		
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
