package com.mlab.pg;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.mlab.pg.graphics.FunctionDisplayer;
import com.mlab.pg.reconstruction.Reconstructor;
import com.mlab.pg.reconstruction.ReconstructorByIntervals;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;
import com.mlab.pg.util.IOUtil;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.valign.VerticalProfileWriter;
import com.mlab.pg.xyfunction.XYVectorFunction;
import com.mlab.pg.xyfunction.XYVectorFunctionCsvReader;


public class ReconstructRunner {

	Logger LOG = Logger.getLogger(ReconstructRunner.class);
	
	protected Reconstructor reconstructor;
	protected ReconstructEssayData essayData;
	protected InterpolationStrategyType interpolationStrategy;
	protected XYVectorFunction originalVProfile;
	protected XYVectorFunction originalGradeData;
	protected XYVectorFunction vProfileFromGradeDataIntegration;
	
	protected double startZ;
	protected double zMax;
	protected double zMin;
	protected int baseSize;
	protected double thresholdSlope;
	protected double SHORT_ALIGNMENT_LENGTH = 50.0;
	protected VerticalGradeProfile resultGProfile;
	protected VerticalProfile resultVProfile;
	protected XYVectorFunction resultVProfilePoints;
	protected int verticalCurveCount, gradeCount, cuasiGradeCount, shortAlignmentCount, twoGradeCount;
	protected String stringReport;

	public ReconstructRunner(ReconstructEssayData essaydata) {
		this.essayData = essaydata;
		interpolationStrategy = essayData.getInterpolationStrategy();
		
		readOriginalVProfile();
		readOriginalGradeData();
		setZLimits();
		selectDataInterval();
		
		integrateGradeData();
		
		
		
	}
	private void getResults() {
		resultGProfile = reconstructor.getGradeProfile();
		resultVProfile = reconstructor.getVerticalProfile();
		double starts = resultVProfile.getStartS();
		double ends = resultVProfile.getEndS();
		double sep = reconstructor.getSeparacionMedia();
		resultVProfilePoints = resultVProfile.getSample(starts, ends, sep, true);
		verticalCurveCount  = 0;
		gradeCount = 0;
		cuasiGradeCount = 0;
		shortAlignmentCount = 0;
		twoGradeCount = 0;
		for(int i=0; i<resultGProfile.size(); i++) {
			double slope = resultGProfile.get(i).getPolynom2().getA1();
			double length = resultGProfile.get(i).getEndS() - resultGProfile.get(i).getStartS();
			if(length < SHORT_ALIGNMENT_LENGTH) {
				shortAlignmentCount++;
			}
			if (Math.abs(slope) == 0.0) {
				gradeCount ++;
			} else if(Math.abs(slope)< thresholdSlope) {
				cuasiGradeCount ++;
				if(i>0 && Math.abs(resultGProfile.get(i-1).getSlope())<thresholdSlope) {
					twoGradeCount++;
				}
			} else {
				verticalCurveCount++;
			}
		}
		stringReport = getStringReport();
	}
	public void doUniqueReconstruction(int basesize, double thresholdslope) {
		this.baseSize = basesize;
		this.thresholdSlope = thresholdslope;
		reconstructor = new Reconstructor(originalGradeData, startZ, interpolationStrategy);
		reconstructor.processUnique(baseSize, thresholdSlope);
		getResults();
	}
	public void doIterativeReconstruction() {
		reconstructor = new Reconstructor(originalGradeData, startZ, interpolationStrategy);
		reconstructor.processIterative();
		
		int bestTest = reconstructor.getBestTest();
		baseSize = (int)reconstructor.getResults()[bestTest][0];
		thresholdSlope = reconstructor.getResults()[bestTest][1];

		reconstructor.processUnique(baseSize, thresholdSlope);
		getResults();
	}
	public void doMultiparameterReconstruction() {
		reconstructor = new ReconstructorByIntervals(originalGradeData, startZ, interpolationStrategy);
		reconstructor.processIterative();
		getResults();
	}
	

	private void integrateGradeData() {
		vProfileFromGradeDataIntegration = originalGradeData.integrate(startZ);
	}
	private void readOriginalVProfile() {
		if(essayData.getSzFileName() != "") {
			String filename = IOUtil.composeFileName(essayData.getInPath(), essayData.getSzFileName());
			File file = new File(filename);
			Assert.assertNotNull(file);
			Assert.assertTrue(file.exists());
			
			XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
			originalVProfile = reader.read();
			Assert.assertNotNull(originalVProfile);
		} else {
			originalVProfile = null;
		}
	}
	private void readOriginalGradeData() {
		String filename = IOUtil.composeFileName(essayData.getInPath(), essayData.getSgFileName());
		File file = new File(filename);
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		originalGradeData = reader.read();
		Assert.assertNotNull(originalGradeData);
	}
	private void setZLimits() {
		if(originalVProfile != null) {
			startZ = originalVProfile.getY(originalVProfile.getStartX());
			zMin = originalVProfile.getMinY();
			zMax = originalVProfile.getMaxY();	
		} else {
			startZ = 0.0;
			zMin = -1.0;
			zMax = -1.0;
		}
	}
	private void selectDataInterval() {
		if(essayData.getStartS() != -1.0 && essayData.getEndS() != -1.0) {
			originalGradeData = originalGradeData.extract(essayData.getStartS(), essayData.getEndS());
			if(originalVProfile != null) {
				originalVProfile = originalVProfile.extract(essayData.getStartS(), essayData.getEndS());
			}
		}
	}
		
	public void printReport() {
		String filename = IOUtil.composeFileName(essayData.getInPath(), essayData.getReportFileName());
		File file = new File(filename);
		VerticalProfileWriter.writeVerticalProfile(file, getResultVProfile(), stringReport);

	}
	public void showReport() {
		System.out.println(stringReport);
	}

	public void showProfiles() {
		VerticalGradeProfile gp = getResultGProfile();
		double x1 = gp.getStartS();
		double x2 = gp.getEndS();
		FunctionDisplayer displayer = new FunctionDisplayer();
		String title= essayData.getGraphTitle() + "\n s="+Math.rint(x1)+" - "+Math.rint(x2);
		displayer.showTwoFunctions(getvProfileFromGradeDataIntegration(), getResultVProfilePoints(), 
				title, "Perfil longitudinal original", "Perfil longitudinal calculado", "S", "Z");
		
		XYVectorFunction originaldata = getOriginalGradeData().extract(x1, x2);
		XYVectorFunction soldata = getResultGProfile().getSample(x1,x2,getSeparacionMedia(),true);
		title= essayData.getGraphTitle() + "(Diagrama de pendientes) \n s="+Math.rint(x1)+" - "+Math.rint(x2);
		displayer.showTwoFunctions(originaldata, soldata, title, "Pendientes originales", "Pendientes calculadas", "S",  "G");
	}
	public String getStringReport() {
		StringBuffer cad = new StringBuffer();
		cad.append("Reconstrucción de la geometría del perfil longitudinal: " + essayData.getEssayName() + "\n");
		cad.append("--------------------------------------------------------------------------" + "\n");
		cad.append("--------------------------------------------------------------------------" + "\n");
		cad.append("Longitud del track (m) : " + Math.round(reconstructor.getTrackLength()*100.0)/100.0 + "\n");
		cad.append("Número de puntos       : " + reconstructor.getPointsCount() + "\n");
		cad.append("Separación media   (m) : " + Math.round(reconstructor.getSeparacionMedia()*100.0)/100.0 + "\n");
		cad.append("--------------------------------------------------------------------------" + "\n");
		cad.append("Rectas Interpolación (Puntos) : " + baseSize + "\n");
		cad.append("Rectas Interpolación (m)      : " + Math.round(baseSize*reconstructor.getSeparacionMedia()*100.0)/100.0 + "\n");
		cad.append("Pendiente límite              : " + thresholdSlope + "\n");
		cad.append("--------------------------------------------------------------------------" + "\n");
		cad.append("Número de alineaciones : " + reconstructor.getAlignmentCount() + "\n");
		cad.append("    Grades             : " + gradeCount + "\n");
		cad.append("    Vertical curves    : " + verticalCurveCount + "\n");
		cad.append("    Cuasi grades       : " + cuasiGradeCount + "\n");
		cad.append("    Short alignments   : " + shortAlignmentCount + "\n");
		cad.append("    Two grades         : " + twoGradeCount + "\n");
		cad.append("--------------------------------------------------------------------------" + "\n");
		cad.append("Valor absoluto del Error (media)  (m)   : " + reconstructor.getMeanError() + "\n");
		cad.append("Valor absoluto del Error (máximo) (m)   : " + reconstructor.getMaxError() + "\n");
		cad.append("Valor absoluto del Error (varianza)     : " + reconstructor.getVarianza() + "\n");
		cad.append("Error Cuadrático Medio ECM              : " + reconstructor.getEcm() + "\n");			
		cad.append("--------------------------------------------------------------------------" + "\n");
		return cad.toString();
	}
	public double getSHORT_ALIGNMENT_LENGTH() {
		return SHORT_ALIGNMENT_LENGTH;
	}
	public void setSHORT_ALIGNMENT_LENGTH(double sHORT_ALIGNMENT_LENGTH) {
		SHORT_ALIGNMENT_LENGTH = sHORT_ALIGNMENT_LENGTH;
	}
	public XYVectorFunction getvProfileFromGradeDataIntegration() {
		return vProfileFromGradeDataIntegration;
	}
	public VerticalGradeProfile getResultGProfile() {
		return resultGProfile;
	}
	public VerticalProfile getResultVProfile() {
		return resultVProfile;
	}
	public XYVectorFunction getResultVProfilePoints() {
		return resultVProfilePoints;
	}
	public XYVectorFunction getOriginalGradeData() {
		return originalGradeData;
	}
	public double getSeparacionMedia() {
		return reconstructor.getSeparacionMedia();
	}

}
