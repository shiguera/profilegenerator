package com.mlab.pg;

import java.io.File;
import java.net.URL;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jfree.ui.RefineryUtilities;
import org.junit.Assert;

import com.mlab.pg.graphics.Charter;
import com.mlab.pg.reconstruction.InterpolationStrategy;
import com.mlab.pg.reconstruction.IterativeReconstructor;
import com.mlab.pg.reconstruction.Reconstructor;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.XYVectorFunction;
import com.mlab.pg.xyfunction.XYVectorFunctionCsvReader;


public class M325_Reconstruct {

	static Logger LOG = Logger.getLogger(M325_Reconstruct.class);
	
	static Charter charter = null;
	static InterpolationStrategy interpolationStrategy;
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");	
		LOG.debug("main()");
		
		interpolationStrategy = InterpolationStrategy.EqualArea;
		//interpolationStrategy = InterpolationStrategy.LessSquares;
		
		XYVectorFunction originalVProfile = readOriginalVerticalProfile();
		XYVectorFunction gradeData = readGradeData();
		double startZ = 589.274;
		
		IterativeReconstructor rec = new IterativeReconstructor(gradeData, startZ, interpolationStrategy);
		
		boolean unique = true;
		if (unique) {
			int baseSize = 42;
			double thresholdSlope = 1.75e-5;
			rec.processUnique(baseSize, thresholdSlope);
			VerticalGradeProfile resultGProfile = rec.getReconstructor().getGradeProfile();
			VerticalProfile resultVProfile = rec.getReconstructor().getVerticalProfile();
//			for(int i=0; i<resultGProfile.size(); i++) {
//				double slope = resultGProfile.get(i).getSlope();
//				if (Math.abs(slope) == 0.0) {
//					System.out.println("Horizontal");
//				} else if(Math.abs(slope)< thresholdSlope) {
//					System.out.println(resultGProfile.get(i));
//					System.out.println(resultVProfile.get(i));
//				} else {
//					System.out.println("No");
//				}
//			}
			
			System.out.println(resultVProfile);
			
			
			XYVectorFunction resultVProfileSample = resultVProfile.getSample(resultVProfile.getStartS(), 
					resultVProfile.getEndS(), rec.getSeparacionMedia(), true);
			showVProfiles(originalVProfile, resultVProfileSample);
			

		} else {
			try {
				rec.processIterative();
			} catch (Exception e) {
				System.out.println("ERROR " + e.getLocalizedMessage());
				System.exit(-1);
			}
			
			int bestTest = rec.getBestTest();
			int baseSize = (int)rec.getResults()[bestTest][0];
			double thresholdSlope = rec.getResults()[bestTest][1];
			
			Reconstructor reconstructor = null;
			try {
				//reconstructor = new Reconstructor(gradeData, baseSize, thresholdSlope, 727.0, new PointCharacteriserStrategy_EqualArea(),
				//		new ProcessBorderIntervalsStrategy_EqualArea());
				reconstructor = new Reconstructor(gradeData, baseSize, thresholdSlope, startZ, interpolationStrategy);
			} catch(Exception e) {
				LOG.error("Error creating Reconstructor");
				System.exit(-1);
			}
			VerticalGradeProfile resultGProfile = reconstructor.getGradeProfile();
			//System.out.println(gprofile);
			
			VerticalProfile resultVProfile = reconstructor.getVerticalProfile();
			//System.out.println(resultVProfile);
			
			XYVectorFunction resultVProfileSample = resultVProfile.getSample(resultVProfile.getStartS(), 
					resultVProfile.getEndS(), rec.getSeparacionMedia(), true);
			showVProfiles(originalVProfile, resultVProfileSample);
		}
		
	}

	

	private static void showVProfiles(XYVectorFunction originalData, XYVectorFunction resultData) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		charter = new Charter("M-325", "S", "Z");
        		charter.addXYVectorFunction(originalData, "Original Data");
        		charter.addXYVectorFunction(resultData, "Result Data");
        		
            	JFrame frame = new JFrame("Charter");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        		frame.setContentPane(charter.getChartPanel());
        		charter.getChart().getXYPlot().getRangeAxis().setRange(550.0, 900.0);
        		frame.pack();
        		RefineryUtilities.centerFrameOnScreen(frame);
        		frame.setVisible(true);
        		        		
            }
        });
		
	}

	private static XYVectorFunction readGradeData() {
		LOG.debug("readGradeData()");
		URL url = ClassLoader.getSystemResource("M325_SG.csv");
		
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		//data = data.extract(0.0, 8450.0);
		data = data.extract(0.0, 6500.0);
		return data;
	}


	private static XYVectorFunction readOriginalVerticalProfile() {
		LOG.debug("readOriginalVerticalProfile()");
		URL url = ClassLoader.getSystemResource("M325_SZ.csv");
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		//data = data.extract(0.0, 8450.0);
		data = data.extract(0.0, 6500.0);
		return data;		
	}



	
}
