package com.mlab.pg;

import java.io.File;
import java.net.URL;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jfree.ui.RefineryUtilities;
import org.junit.Assert;

import com.mlab.pg.graphics.Charter;
import com.mlab.pg.reconstruction.Reconstructor;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.XYVectorFunction;
import com.mlab.pg.xyfunction.XYVectorFunctionCsvReader;


public class ReconstructN403 {

	static Logger LOG = Logger.getLogger(ReconstructN403.class);
	
	static Charter charter = null;
	// Lo establece el método readData
	static InterpolationStrategyType interpolationStrategy;
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");	

		LOG.debug("main()");
		
		interpolationStrategy = InterpolationStrategyType.EqualArea;

		XYVectorFunction originalVProfile = readOriginalVerticalProfile();
		XYVectorFunction gradeData = readGradeData();
		XYVectorFunction modifiedGradeData = new XYVectorFunction();
		for (double x=gradeData.getStartX(); x<=gradeData.getEndX(); x=x+5.0) {
			double g = gradeData.getY(x);
			modifiedGradeData.add(new double[]{x,g});
		}
		
		Reconstructor rec = null;
		try {
			rec = new Reconstructor(gradeData, 765.0, interpolationStrategy);
			//rec = new IterativeReconstructor(modifiedGradeData, 727.0);
		} catch (Exception e) {
			System.out.println("ERROR " + e.getMessage());
			System.exit(-1);
		}
		
		int bestTest = rec.getBestTest();
		int baseSize = (int)rec.getResults()[bestTest][0];
		double thresholdSlope = rec.getResults()[bestTest][1];
		
		Reconstructor reconstructor = null;
		try {
			//reconstructor = new Reconstructor(gradeData, baseSize, thresholdSlope, 765.0, new PointCharacteriserStrategy_EqualArea(),
			//		new ProcessBorderIntervalsStrategy_EqualArea());
			reconstructor = new Reconstructor(gradeData,  765.0, interpolationStrategy);
			reconstructor.processUnique(baseSize, thresholdSlope);
		} catch(Exception e) {
			LOG.error("Error creating Reconstructor");
			System.exit(-1);
		}
		
		VerticalGradeProfile resultGProfile = reconstructor.getGradeProfile();
		//System.out.println(gprofile);
		
		VerticalProfile resultVProfile = reconstructor.getVerticalProfile();
		//System.out.println(vprofile);
		
		XYVectorFunction resultVProfileSample = resultVProfile.getSample(resultVProfile.getStartS(), 
				resultVProfile.getEndS(), rec.getSeparacionMedia(), true);
		
		showVProfiles(originalVProfile, resultVProfileSample);
		
	}

	

	private static void showVProfiles(XYVectorFunction originalData, XYVectorFunction resultData) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		charter = new Charter("N-403: Original VProfile", "S", "G");
        		charter.addXYVectorFunction(originalData, "Original Data");
        		charter.addXYVectorFunction(resultData, "Result Data");
        		
            	JFrame frame = new JFrame("Charter");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        		frame.setContentPane(charter.getChartPanel());
        		charter.getChart().getXYPlot().getRangeAxis().setRange(700.0, 870.0);
        		frame.pack();
        		RefineryUtilities.centerFrameOnScreen(frame);
        		frame.setVisible(true);
        		        		
            }
        });
		
	}



	private static XYVectorFunction readOriginalVerticalProfile() {
		LOG.debug("readOriginalVerticalProfile()");
		URL url = ClassLoader.getSystemResource("N-403_SZ.csv");
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		// data = data.extract(0.0, 1050.0);
		return data;		
	}



	private static XYVectorFunction readGradeData() {
		LOG.debug("readData()");
		URL url = ClassLoader.getSystemResource("N-403_SG.csv");
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		//data = data.extract(0.0, 1050.0);
		return data;
		
		
		
	}
	
}
