package com.mlab.pg.essays.roads.M607;

import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.EssayData;
import com.mlab.pg.PruebaProyecto;
import com.mlab.pg.ReconstructRunner;
import com.mlab.pg.graphics.FunctionDisplayer;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;
import com.mlab.pg.xyfunction.XYVectorFunction;


/**
 * Ensayo: M-607, track Leika, Ascendente, eje promediado
 * @author shiguera
 *
 */
public class M607_Essay_17_PdtesMFOM {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public M607_Essay_17_PdtesMFOM() {
		
		essayData = new EssayData();
		essayData.setEssayName("M-607 Ensayo 17: PK 36+000 al 48+300 (Madrid) - Pendientes MFOM");
		essayData.setCarretera("M-607");
		essayData.setSentido("Asscendente");
		essayData.setGraphTitle(essayData.getEssayName());
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/PdtesMFOM");
		essayData.setOutPath(essayData.getInPath());
		essayData.setXyzFileName("");
		essayData.setSgFileName("M607_SG.csv");
		essayData.setSzFileName("M607_SZ.csv");
		essayData.setReportFileName("M-607-Ensayo_17.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		essayData.setStartS(27370.0);
		essayData.setEndS(39670.0);
		
		recRunner = new ReconstructRunner(essayData);		
		recRunner.setMinLength(80.0);
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		M607_Essay_17_PdtesMFOM essay = new M607_Essay_17_PdtesMFOM();
		//essay.doIterative();
		//essay.doMultiparameter();
		essay.doUnique(3, 5e-5);
		
		recRunner.showReport();
		//recRunner.printReport();
		//recRunner.showProfiles();
		
		FunctionDisplayer fd = new FunctionDisplayer();
		XYVectorFunction f1 = recRunner.getOriginalVProfile();
		PruebaProyecto pp = new PruebaProyecto();
		XYVectorFunction f2 = PruebaProyecto.getVProfilePoints();
		fd.showTwoFunctions(f1, f2, "Comparación con el proyecto", "Calculado", "Proyecto", "S", "Z");
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
