package com.mlab.pg.old;

import java.awt.BasicStroke;
import java.io.File;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jfree.ui.RefineryUtilities;
import org.junit.Assert;

import com.mlab.pg.graphics.Charter;
import com.mlab.pg.reconstruction.CheckEndingsWithBeginnings;
import com.mlab.pg.reconstruction.Reconstructor;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.valign.VerticalProfileWriter;
import com.mlab.pg.xyfunction.XYVectorFunction;
import com.mlab.pg.xyfunction.XYVectorFunctionCsvReader;


public class M607_Reconstruct_RoadRecorder {

	static Logger LOG = Logger.getLogger(M607_Reconstruct_RoadRecorder.class);
	
	static double startZ;
	static double hmax;
	static double hmin;

	static Charter charter = null;
	static InterpolationStrategyType interpolationStrategy;
	
	static XYVectorFunction gradeData;
	static VerticalGradeProfile resultGProfile;
	static VerticalProfile resultVProfile;
	static XYVectorFunction resultVProfilePoints;

	static Reconstructor reconstructor;
	
	static enum OPTION {ShowVerticalProfile, UNIQUE, ITERATIVE};
	static OPTION option= OPTION.ITERATIVE;
	//static OPTION option= OPTION.UNIQUE;
	//static OPTION option= OPTION.ShowVerticalProfile;
	
	static String graphTitle = "M-607 - Axis running RoadRecorder over an smartphone";
	static String sginfilename = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M-607/tracksRoadRecorder/M607_Asc_Axis_SG.csv";
	static String szinfilename = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M-607/tracksRoadRecorder/M607_Asc_Axis_SZ.csv";
	static String outFileName = "M607_Axis_RoadRecorder.txt";
	static int baseSize = 6;
	static double thresholdSlope = 1.25e-5;
	static double SHORT_ALIGNMENT_LENGTH =50.0;
	static int verticalCurveCount, gradeCount, cuasiGradeCount, shortAlignmentCount, twoGradeCount;

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");	
		LOG.debug("main()");
		
		interpolationStrategy = InterpolationStrategyType.EqualArea;
		//interpolationStrategy = InterpolationStrategy.LessSquares;
		
		XYVectorFunction originalVProfile = readOriginalVerticalProfile();
		startZ = originalVProfile.getY(originalVProfile.getStartX());
		hmin = originalVProfile.getMinY();
		hmax = originalVProfile.getMaxY();
		
		gradeData = readGradeData();
		XYVectorFunction integratedFromGradesVProfilePoints = gradeData.integrate(startZ);
				
		reconstructor = new Reconstructor(gradeData, startZ, interpolationStrategy);
		
		
		switch(option) {
			case ShowVerticalProfile:
				showProfile(originalVProfile);
				break;
			case UNIQUE:
				processUnique(baseSize, thresholdSlope);
				break;
			case ITERATIVE:
				processIterative();				
				break;
		}

		resultGProfile = reconstructor.getGradeProfile();
		resultVProfile = reconstructor.getVerticalProfile();
		resultVProfilePoints = resultVProfile.getSample(resultVProfile.getStartS(), 
				resultVProfile.getEndS(), reconstructor.getSeparacionMedia(), true);
		System.out.println(resultVProfile);

		calculateReport();
		showReport();
		printFile();
		showVProfiles(integratedFromGradesVProfilePoints, resultVProfilePoints);
		System.out.println("Done!");
	}
	private static void processUnique(int basesize, double thresholdslope) {
		reconstructor.processUnique(baseSize, thresholdSlope);
	}
	private static void processIterative() {
		try {
			reconstructor.processIterative();
		} catch (Exception e) {
			System.out.println("ERROR " + e.getLocalizedMessage());
			System.exit(-1);
		}
		
		int bestTest = reconstructor.getBestTest();
		baseSize = (int)reconstructor.getResults()[bestTest][0];
		thresholdSlope = reconstructor.getResults()[bestTest][1];
		reconstructor = null;
		try {
			reconstructor = new Reconstructor(gradeData,  startZ, interpolationStrategy);
			reconstructor.processUnique(baseSize, thresholdSlope);
		} catch(Exception e) {
			LOG.error("Error creating Reconstructor");
			System.exit(-1);
		}
	}
	private static void calculateReport() {
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
		CheckEndingsWithBeginnings checker = new CheckEndingsWithBeginnings();
		boolean result = checker.checkProfile(resultVProfile);
		System.out.println("Check endings with beginnings: " + result);

	}
	private static String getStringReport() {
		StringBuffer cad = new StringBuffer();
		cad.append("Reconstrucción de la geometría del perfil longitudinal: " + graphTitle + "\n");
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
	private static void showReport() {
		System.out.println(getStringReport());
	}
	private static void printFile() {		
		String filepath = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M-607/tracksRoadRecorder/" + outFileName;
		File file = new File(filepath);
		VerticalProfileWriter.writeVerticalProfile(file, resultVProfile, getStringReport());

	}
	/**
	 * Muestra un único perfil
	 * @param profile
	 */
	private static void showProfile(XYVectorFunction profile) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		charter = new Charter(graphTitle, "S", "Z");
        		charter.addXYVectorFunction(profile, "Original Data");
        		
            	JFrame frame = new JFrame("Charter");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        		frame.setContentPane(charter.getChartPanel());
        		charter.getChart().getXYPlot().getRangeAxis().setRange(hmin, hmax);
        		frame.pack();
        		RefineryUtilities.centerFrameOnScreen(frame);
        		frame.setVisible(true);        		        		
            }
        });		
	}

	private static void showVProfiles(XYVectorFunction originalData, XYVectorFunction resultData) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		charter = new Charter(graphTitle, "S", "Z");
        		charter.addXYVectorFunction(originalData, "Original Data");
        		charter.addXYVectorFunction(resultData, "Result Data");
        		
            	JFrame frame = new JFrame("Charter");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        		frame.setContentPane(charter.getChartPanel());
        		charter.getChart().getXYPlot().getRangeAxis().setRange(hmin, hmax);
        		charter.getChart().getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(2.0f));
        		charter.getChart().getXYPlot().getRenderer().setSeriesStroke(1, new BasicStroke(2.0f));
        		frame.pack();
        		RefineryUtilities.centerFrameOnScreen(frame);
        		frame.setVisible(true);
        		        		
            }
        });
		
	}

	private static XYVectorFunction readGradeData() {
		LOG.debug("readGradeData()");
		
		File file = new File(sginfilename);
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		//data = data.extract(3500.0, 5800.0);
		return data;
	}

	private static XYVectorFunction readOriginalVerticalProfile() {
		LOG.debug("readOriginalVerticalProfile()");
		File file = new File(szinfilename);
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		//data = data.extract(3500.0, 5800.0);
		return data;		
	}
	
}
