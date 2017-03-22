package com.mlab.pg;

import java.io.File;
import java.net.URL;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jfree.ui.RefineryUtilities;
import org.junit.Assert;

import com.mlab.pg.graphics.Charter;
import com.mlab.pg.reconstruction.CheckEndingsWithBeginnings;
import com.mlab.pg.reconstruction.InterpolationStrategy;
import com.mlab.pg.reconstruction.IterativeReconstructor;
import com.mlab.pg.reconstruction.Reconstructor;
import com.mlab.pg.reconstruction.VProfileFilter_ShortAlignments;
import com.mlab.pg.reconstruction.VerticalProfileWriter;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.XYVectorFunction;
import com.mlab.pg.xyfunction.XYVectorFunctionCsvReader;


public class M607_Reconstruct_TrackLeica {

	static Logger LOG = Logger.getLogger(M607_Reconstruct_TrackLeica.class);
	
	static double startZ = 906.26;
	static double hmax = 930.0;
	static double hmin = 830.0;

	static Charter charter = null;
	static InterpolationStrategy interpolationStrategy;
	
	static enum OPTION {ShowVerticalProfile, UNIQUE, ITERATIVE};
	static OPTION option= OPTION.UNIQUE;
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");	
		LOG.debug("main()");
		
		interpolationStrategy = InterpolationStrategy.EqualArea;
		//interpolationStrategy = InterpolationStrategy.LessSquares;
		
		XYVectorFunction originalVProfile = readOriginalVerticalProfile();
		XYVectorFunction gradeData = readGradeData();
		
		VerticalGradeProfile resultGProfile = null;
		VerticalProfile resultVProfile = null;
		XYVectorFunction resultVProfileSample = null;
		
		IterativeReconstructor rec = new IterativeReconstructor(gradeData, startZ, interpolationStrategy);
		
		int baseSize = 5;
		double thresholdSlope = 1e-4;
		
		switch(option) {
			case ShowVerticalProfile:
				showProfile(originalVProfile);
				break;
			case UNIQUE:
				rec.processUnique(baseSize, thresholdSlope);
				resultGProfile = rec.getReconstructor().getGradeProfile();
				resultVProfile = rec.getReconstructor().getVerticalProfile();
				
				CheckEndingsWithBeginnings checker = new CheckEndingsWithBeginnings();
				boolean result = checker.checkProfile(resultVProfile);
				System.out.println("Check endings with beginnings: " + result);
	
				VProfileFilter_ShortAlignments filter = new VProfileFilter_ShortAlignments(50);
				filter.filter(resultVProfile);
				
				int countVC = 0;
				int countG = 0;
				int countCuasiG = 0;
				int countShorAlignments = 0;
				int countTwoGrades = 0;
				for(int i=0; i<resultGProfile.size(); i++) {
					//System.out.println(resultGProfile.getAlign(i).getPolynom2());
					double slope = resultGProfile.get(i).getPolynom2().getA1();
					double length = resultGProfile.get(i).getEndS() - resultGProfile.get(i).getStartS();
					if(length<50.0) {
						countShorAlignments++;
					}
					if (Math.abs(slope) == 0.0) {
						countG ++;
						//System.out.println("Horizontal");
					} else if(Math.abs(slope)< thresholdSlope) {
						countCuasiG ++;
						if(i>0 && Math.abs(resultGProfile.get(i-1).getSlope())<thresholdSlope) {
							countTwoGrades++;
						}
						//System.out.println(resultGProfile.getAlign(i).getPolynom2());
						//System.out.println(resultGProfile.get(i));
						//System.out.println(resultVProfile.get(i));
					} else {
						countVC++;
						//System.out.println("No");
					}
				}
				System.out.println("Grades: " + countG);
				System.out.println("Cuasi Grades: " + countCuasiG);
				System.out.println("Vertical Curves: " + countVC);
				System.out.println("ShortAlignments: : " + countShorAlignments);
				System.out.println("Two grades: " + countTwoGrades);
				System.out.println(resultVProfile);
				System.out.println("Puntos: " + gradeData.size());
				System.out.println("Sep. Media: " + gradeData.separacionMedia());
				
				resultVProfileSample = resultVProfile.getSample(resultVProfile.getStartS(), 
						resultVProfile.getEndS(), rec.getSeparacionMedia(), true);
				showVProfiles(originalVProfile, resultVProfileSample);
				
				File file = new File("/home/shiguera/ownCloud/workspace/roads/ProfileGenerator/src/main/resources/M325.txt");
				VerticalProfileWriter.writeVerticalProfile(file, resultVProfile, "Reconstrucción de la M-697 (Track Leika): perfil longitudinal");
				break;
			case ITERATIVE:
				try {
					rec.processIterative();
				} catch (Exception e) {
					System.out.println("ERROR " + e.getLocalizedMessage());
					System.exit(-1);
				}
				
				int bestTest = rec.getBestTest();
				baseSize = (int)rec.getResults()[bestTest][0];
				thresholdSlope = rec.getResults()[bestTest][1];
				
				Reconstructor reconstructor = null;
				try {
					//reconstructor = new Reconstructor(gradeData, baseSize, thresholdSlope, 727.0, new PointCharacteriserStrategy_EqualArea(),
					//		new ProcessBorderIntervalsStrategy_EqualArea());
					reconstructor = new Reconstructor(gradeData, baseSize, thresholdSlope, startZ, interpolationStrategy);
				} catch(Exception e) {
					LOG.error("Error creating Reconstructor");
					System.exit(-1);
				}
				resultGProfile = reconstructor.getGradeProfile();
				//System.out.println(gprofile);
				
				resultVProfile = reconstructor.getVerticalProfile();
				//System.out.println(resultVProfile);
				
				resultVProfileSample = resultVProfile.getSample(resultVProfile.getStartS(), 
						resultVProfile.getEndS(), rec.getSeparacionMedia(), true);
				showVProfiles(originalVProfile, resultVProfileSample);
				break;
		}
	
		System.out.println("Done!");
	}

	/**
	 * Muestra un único perfil
	 * @param profile
	 */
	private static void showProfile(XYVectorFunction profile) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		charter = new Charter("M-607.- Track Leika", "S", "Z");
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
        		charter = new Charter("M-607.- Track Leika", "S", "Z");
        		charter.addXYVectorFunction(originalData, "Original Data");
        		charter.addXYVectorFunction(resultData, "Result Data");
        		
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

	private static XYVectorFunction readGradeData() {
		LOG.debug("readGradeData()");
		URL url = ClassLoader.getSystemResource("M607_trackLeika_1_ED50_SG.csv");
		
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		//data = data.extract(0.0, 1050.0);
		return data;
	}

	private static XYVectorFunction readOriginalVerticalProfile() {
		LOG.debug("readOriginalVerticalProfile()");
		URL url = ClassLoader.getSystemResource("M607_trackLeika_1_ED50_SZ.csv");
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		// data = data.extract(0.0, 1050.0);
		return data;		
	}
	
}
