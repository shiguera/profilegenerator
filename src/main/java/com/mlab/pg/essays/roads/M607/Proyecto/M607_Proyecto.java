package com.mlab.pg.essays.roads.M607.Proyecto;

import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.EssayData;
import com.mlab.pg.reconstruction.ReconstructRunner;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;
import com.mlab.pg.valign.VerticalProfile;


/**
 * Ensayo: M-607, track Leika, Ascendente, eje promediado
 * @author shiguera
 *
 */
public class M607_Proyecto {

	
	static EssayData essayData;
	static ReconstructRunner recRunner;
	static String stringReport;
	
	public M607_Proyecto() {
		
		essayData = new EssayData();
		essayData.setEssayName("M-607 - Perfil de proyecto");
		essayData.setCarretera("M-607");
		essayData.setSentido("Asscendente");
		essayData.setGraphTitle(essayData.getEssayName());
		essayData.setInPath("/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/Proyecto");
		essayData.setOutPath(essayData.getInPath());
		essayData.setXyzFileName("");
		essayData.setSgFileName("M607_Proyecto_SG.csv");
		essayData.setSzFileName("M607_Proyecto_SZ.csv");
		essayData.setReportFileName("M-607-PerfilProyecto.txt");
		essayData.setInterpolationStrategy(InterpolationStrategyType.EqualArea);
		//essayData.setStartS(2500.0);
		//essayData.setEndS(7000.0);
		
		recRunner = new ReconstructRunner(essayData);		
		
		recRunner.setMinLength(10.0);
		recRunner.setMAX_BASE_LENGTH(10.0);

	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		
		M607_Proyecto essay = new M607_Proyecto();
		//essay.doIterative();
		//essay.doMultiparameter();
		essay.doUnique(4, 6.5e-5);
		
		recRunner.showReport();
		recRunner.printReport();
		recRunner.showProfiles();
		
		doErrorReport();
	}

	private static void doErrorReport() {
		PruebaProyecto pp = new PruebaProyecto();
		VerticalProfile originalProfile = pp.getVprofile();
		VerticalProfile resultProfile = recRunner.getResultVProfile();
		int originalAlignmentCount = originalProfile.size();
		int resultAlignmentCount = resultProfile.size();
		System.out.println("Number of alignments: original=" + originalAlignmentCount + ", result=" + resultAlignmentCount);
		System.out.println("Number of Grades    : original=" + originalProfile.getGradesCount() + ", result=" + resultProfile.getGradesCount());
		System.out.println("Number of VC        : original=" + originalProfile.getVerticalCurvesCount() + ", result=" + resultProfile.getVerticalCurvesCount());
		// Orden y tipo de las alineaciones
		boolean typeandorder = true;
		int sameTypeAndOrderCount = 0;
		for(int i=0; i<originalAlignmentCount && i<resultAlignmentCount; i++) {
			if(originalProfile.get(i).getClass() == resultProfile.get(i).getClass()) {
				sameTypeAndOrderCount++;
			}
		}
		System.out.println("Número de alineaciones coincidentes en tipo y orden: " + sameTypeAndOrderCount);
		// Distancia media entre los puntos frontera de alineaciones
		double sdist = 0.0;
		for(int i=0; i<originalAlignmentCount && i<resultAlignmentCount; i++) {
			double s1 = originalProfile.get(i).getStartS();
			double s2 = resultProfile.get(i).getStartS();
			double dist = Math.abs(s2-s1);
			sdist += dist;
		}
		sdist = sdist / originalProfile.size();
		System.out.println("Distancia media entre puntos frontera de alineaciones: " + sdist);
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
