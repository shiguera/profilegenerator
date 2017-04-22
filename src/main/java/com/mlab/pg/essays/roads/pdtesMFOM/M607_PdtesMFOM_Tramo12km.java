package com.mlab.pg.essays.roads.pdtesMFOM;

import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.EssayData;
import com.mlab.pg.PruebaProyecto;
import com.mlab.pg.graphics.FunctionDisplayer;
import com.mlab.pg.reconstruction.ReconstructRunner;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;
import com.mlab.pg.xyfunction.XYVectorFunction;


/**
 * Ensayo: M-607, track Leika, Ascendente, eje promediado
 * @author shiguera
 *
 */
public class M607_PdtesMFOM_Tramo12km {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public M607_PdtesMFOM_Tramo12km() {
		
		essayData = new EssayData();
		essayData.setEssayName("M-607: PK 36+000 al 48+300 (Madrid) - Pendientes MFOM");
		essayData.setCarretera("M-607");
		essayData.setSentido("Asscendente");
		essayData.setGraphTitle(essayData.getEssayName());
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/PdtesMFOM");
		essayData.setOutPath(essayData.getInPath());
		essayData.setXyzFileName("");
		essayData.setSgFileName("M607_2_SG.csv");
		essayData.setSzFileName("M607_2_SZ.csv");
		essayData.setReportFileName("M-607_PdtesMFOM_Tramo12km.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		//essayData.setStartS(27370.0);
		//essayData.setEndS(39670.0);
		
		recRunner = new ReconstructRunner(essayData);		
		recRunner.setMinLength(81.0);
		recRunner.setMAX_BASE_LENGTH(300.0);
		double[] thresholdSlopes = new double[] {1.0e-4,9e-5,8e-5, 7e-5, 6e-5, 5e-5, 4e-5, 3.75e-5, 3.5e-5, 3.25e-5, 3e-5, 2.75e-5, 2.5e-5, 2.25e-5, 2e-5, 1e-5, 1.5e-6, 1e-6, 1.5e-7, 1e-7}; 
		recRunner.setThresholdSlopes(thresholdSlopes);

	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		M607_PdtesMFOM_Tramo12km essay = new M607_PdtesMFOM_Tramo12km();
		//essay.doIterative();
		//essay.doMultiparameter();
		essay.doUnique(13, 3e-5);
		
		recRunner.showReport();
		recRunner.printReport();
		recRunner.showProfiles();
		
//		FunctionDisplayer fd = new FunctionDisplayer();
//		XYVectorFunction f1 = recRunner.getOriginalVProfile();
//		PruebaProyecto pp = new PruebaProyecto();
//		XYVectorFunction f2 = PruebaProyecto.getVProfilePoints();
//		fd.showTwoFunctions(f1, f2, "Comparación con el proyecto", "Calculado", "Proyecto", "S", "Z");
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
