package com.mlab.pg.reconstruction;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.mlab.pg.EssayData;
import com.mlab.pg.graphics.FunctionDisplayer;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;
import com.mlab.pg.util.IOUtil;
import com.mlab.pg.util.MathUtil;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.valign.VerticalProfileWriter;
import com.mlab.pg.xyfunction.XYVectorFunction;
import com.mlab.pg.xyfunction.XYVectorFunctionCsvReader;


public class ReconstructRunner {

	Logger LOG = Logger.getLogger(ReconstructRunner.class);
	protected double SHORT_ALIGNMENT_LENGTH = 50.0;
	protected double MIN_LENGTH = 50.0;
	protected double MAX_BASE_LENGTH = 300;
	protected double[] thresholdSlopes = {1.0e-4, 7e-5, 6e-5, 5e-5, 1.75e-5, 1.5e-5, 1.25e-5, 1.0e-5, 1.75e-6, 1.5e-6, 1.25e-6, 1.0e-6, 1.75e-7, 1.5e-7, 1.25e-7, 1.0e-7}; 
	
	protected Reconstructor reconstructor;
	protected EssayData essayData;
	protected InterpolationStrategyType interpolationStrategy;
	protected XYVectorFunction originalVProfile;
	protected XYVectorFunction originalGradeData;
	protected XYVectorFunction vProfileFromGradeDataIntegration;
	
	protected double startZ;
	protected double zMax;
	protected double zMin;
	protected int baseSize;
	protected double thresholdSlope;
	protected VerticalGradeProfile resultGProfile;
	protected VerticalProfile resultVProfile;
	protected XYVectorFunction resultVProfilePoints;
	protected int verticalCurveCount, gradeCount, cuasiGradeCount, shortAlignmentCount, twoGradeCount;
	protected String stringReport;

	public ReconstructRunner(EssayData essaydata) {
		this.essayData = essaydata;
		interpolationStrategy = essayData.getInterpolationStrategy();
		
		readOriginalVProfile();
		readOriginalGradeData();
		selectDataInterval();
		setZLimits();
		
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
		reconstructor = new Reconstructor(originalGradeData, startZ, interpolationStrategy, MIN_LENGTH);
		reconstructor.setMAX_BASE_LENGTH(MAX_BASE_LENGTH);
		reconstructor.setThresholdSlopes(thresholdSlopes);
		reconstructor.processUnique(baseSize, thresholdSlope);
		getResults();
	}
	public void doIterativeReconstruction() {
		reconstructor = new Reconstructor(originalGradeData, startZ, interpolationStrategy, MIN_LENGTH);
		reconstructor.setMAX_BASE_LENGTH(MAX_BASE_LENGTH);
		reconstructor.setThresholdSlopes(thresholdSlopes);
		reconstructor.processIterative();
		
		int bestTest = reconstructor.getBestTest();
		baseSize = (int)reconstructor.getResults()[bestTest][0];
		thresholdSlope = reconstructor.getResults()[bestTest][1];

		reconstructor.processUnique(baseSize, thresholdSlope);
		getResults();
	}
	public void doMultiparameterReconstruction() {
		reconstructor = new ReconstructorByIntervals(originalGradeData, startZ, interpolationStrategy);
		reconstructor.setMAX_BASE_LENGTH(MAX_BASE_LENGTH);
		reconstructor.setThresholdSlopes(thresholdSlopes);
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
			if(originalGradeData.size()==0) {
				LOG.error("selectDataInterval() ERROR: GradeData size = 0");
			}
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
		//String title= essayData.getGraphTitle() + " (Vertical Profile)\n s="+MathUtil.doubleToString(x1, 12, 2, true) + " - " + MathUtil.doubleToString(x2, 12, 2, true);
		String title= essayData.getGraphTitle() + " (Vertical profile)";
		
		displayer.showTwoFunctions(getvProfileFromGradeDataIntegration(), getResultVProfilePoints(), 
				title, "Original", "Reconstructed", "S(m)", "Z(m)");
		
		XYVectorFunction originaldata = getOriginalGradeData().extract(x1, x2);
		XYVectorFunction soldata = getResultGProfile().getSample(x1,x2,getSeparacionMedia(),true);
		//title= essayData.getGraphTitle() + " (Slope Diagram) \n s="+Math.rint(x1)+" - "+Math.rint(x2);
		title= essayData.getGraphTitle() + " (Grade profile)";

		displayer.showTwoFunctions(originaldata, soldata, title, "Original", "Reconstructed", "S(m)",  "G");
	}
	public String getStringReport() {
		StringBuffer cad = new StringBuffer();
		cad.append("------------------------------------------------------------------------------" + "\n");
		cad.append("RECONSTRUCCIÓN DE LA GEOMETRÍA DEL PERFIL LONGITUDINAL \n");
		cad.append("------------------------------------------------------------------------------" + "\n");
		cad.append(essayData.getEssayName() + "\n");
		String tramo = "Traza completa";
		if(essayData.getStartS() != -1.0) {
			tramo = essayData.getStartS() + " - " + essayData.getEndS();
		}
		cad.append("Tramo : " + tramo + "\n");
		cad.append("------------------------------------------------------------------------------" + "\n");
		cad.append("DATOS DE LA TRAZA:\n");
		cad.append("Número de puntos     : " + reconstructor.getPointsCount() + "\n");
		cad.append("Longitud         (m) : " + MathUtil.doubleToString(reconstructor.getTrackLength(), 12, 2, true) + "\n");
		cad.append("Separación media (m) : " + MathUtil.doubleToString(reconstructor.getSeparacionMedia(),12,2,true) + "\n");
		cad.append("Altitud máxima   (m) : " + MathUtil.doubleToString(reconstructor.getIntegralVerticalProfilePoints().getMaxY(),12,2,true) + "\n");
		cad.append("Altitud media    (m) : " + MathUtil.doubleToString(reconstructor.getIntegralVerticalProfilePoints().getMeanY(),12,2,true) + "\n");
		cad.append("Altitud mínima   (m) : " + MathUtil.doubleToString(reconstructor.getIntegralVerticalProfilePoints().getMinY(),12,2,true) + "\n");
		cad.append("Pendiente media      : " + MathUtil.doubleToString(reconstructor.getResultVerticalProfile().getMeanSlope(),12,4,true) + "\n");
		
		cad.append("------------------------------------------------------------------------------" + "\n");
		cad.append("PARÁMETROS USADOS EN LA RECONSTRUCCIÓN:\n");
		cad.append("Pendiente límite              : " + thresholdSlope + "\n");
		cad.append("Kv límite                     : " + MathUtil.doubleToString(1.0/thresholdSlope, 12,0,true) + "\n");
		cad.append("Rectas Interpolación (Puntos) : " + baseSize + "\n");
		cad.append("Rectas Interpolación (m)      : " + MathUtil.doubleToString(reconstructor.getSeparacionMedia()*(baseSize-1),12,2,true) + "\n");
		cad.append("------------------------------------------------------------------------------" + "\n");
		cad.append("RESULTADO: ERRORES\n");
		cad.append("Error Cuadrático Medio ECM              : " + MathUtil.doubleToString(reconstructor.getEcm(),12,6,true) + "\n");			
		cad.append("Valor absoluto del Error (varianza)     : " + MathUtil.doubleToString(reconstructor.getVarianza(),12,6,true) + "\n");
		cad.append("Valor absoluto del Error (Desv. Típica) : " + MathUtil.doubleToString(Math.sqrt(reconstructor.getVarianza()),12,6,true) + "\n");
		cad.append("Valor absoluto del Error (media)  (m)   : " + MathUtil.doubleToString(reconstructor.getMeanError(),12,6,true) + "\n");
		cad.append("Valor absoluto del Error (máximo) (m)   : " + MathUtil.doubleToString(reconstructor.getMaxError(),12,6,true) + "\n");
		cad.append("------------------------------------------------------------------------------" + "\n");
		cad.append("RESULTADO: PERFIL LONGITUDINAL RECONSTRUIDO\n");
		cad.append("Número Total de alineaciones       : " + reconstructor.getAlignmentCount() + "\n");
		cad.append("    Grades                         : " + gradeCount + "\n");
		cad.append("    Vertical curves                : " + verticalCurveCount + "\n");
		cad.append("    Cuasi grades                   : " + cuasiGradeCount + "\n");
		cad.append("    Short alignments  (L<" + MathUtil.doubleToString(getSHORT_ALIGNMENT_LENGTH(),6,2,false) + " m)  : " + shortAlignmentCount + "\n");
		cad.append("    Two consecutive grades         : " + twoGradeCount + "\n");
		cad.append("------------------------------------------------------------------------------" + "\n");
		cad.append("------------------------------------------------------------------------------" + "\n");
		return cad.toString();
	}
	public double getSHORT_ALIGNMENT_LENGTH() {
		return SHORT_ALIGNMENT_LENGTH;
	}
	public void setSHORT_ALIGNMENT_LENGTH(double sHORT_ALIGNMENT_LENGTH) {
		SHORT_ALIGNMENT_LENGTH = sHORT_ALIGNMENT_LENGTH;
	}
	public double getMinLength() {
		return MIN_LENGTH;
	}
	public void setMinLength(double minlength) {
		MIN_LENGTH = minlength;
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
	public double getStartZ() {
		return startZ;
	}
	public void setStartZ(double startZ) {
		this.startZ = startZ;
	}
	public double getzMax() {
		return zMax;
	}
	public void setzMax(double zMax) {
		this.zMax = zMax;
	}
	public double getzMin() {
		return zMin;
	}
	public void setzMin(double zMin) {
		this.zMin = zMin;
	}
	public XYVectorFunction getOriginalVProfile() {
		return originalVProfile;
	}
	public double getMAX_BASE_LENGTH() {
		return MAX_BASE_LENGTH;
	}
	public void setMAX_BASE_LENGTH(double mAX_BASE_LENGTH) {
		MAX_BASE_LENGTH = mAX_BASE_LENGTH;
	}

	public double[] getThresholdSlopes() {
		return this.thresholdSlopes;
	}
	public void setThresholdSlopes(double[] thresholdslopes) {
		this.thresholdSlopes = thresholdslopes;
	}
}
