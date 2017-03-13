package com.mlab.pg;

import java.io.File;
import java.net.URL;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jfree.ui.RefineryUtilities;

import com.mlab.pg.graphics.Charter;
import com.mlab.pg.reconstruction.InterpolationStrategy;
import com.mlab.pg.reconstruction.IterativeReconstructor;
import com.mlab.pg.reconstruction.Reconstructor;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.XYVectorFunction;
import com.mlab.pg.xyfunction.XYVectorFunctionCsvReader;

import junit.framework.Assert;

public class ReconstructM607_TrackLeika_2 {

	static Logger LOG = Logger.getLogger(ReconstructM607_TrackLeika_2.class);
	
	static Charter charter = null;
	static InterpolationStrategy interpolationStrategy;
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");	
		LOG.debug("main()");
		
		XYVectorFunction originalVProfile = readOriginalVerticalProfile();
		XYVectorFunction gradeData = readGradeData();
		
		interpolationStrategy = InterpolationStrategy.EqualArea;
		
		IterativeReconstructor rec = null;
		try {
			rec = new IterativeReconstructor(gradeData, 913.24, interpolationStrategy);
		} catch (Exception e) {
			System.out.println("ERROR " + e.getMessage());
			System.exit(-1);
		}
		
		int bestTest = rec.getBestTest();
		int baseSize = (int)rec.getResults()[bestTest][0];
		double thresholdSlope = rec.getResults()[bestTest][1];
		
		Reconstructor reconstructor = null;
		try {
			//reconstructor = new Reconstructor(gradeData, baseSize, thresholdSlope, 727.0, new PointCharacteriserStrategy_EqualArea(),
			//		new ProcessBorderIntervalsStrategy_EqualArea());
			reconstructor = new Reconstructor(gradeData, baseSize, thresholdSlope, 913.24, interpolationStrategy);
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
        		charter = new Charter("M-607: Track Leika", "S", "Z");
        		charter.addXYVectorFunction(originalData, "Original Data");
        		charter.addXYVectorFunction(resultData, "Result Data");
        		
            	JFrame frame = new JFrame("Charter");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        		frame.setContentPane(charter.getChartPanel());
        		charter.getChart().getXYPlot().getRangeAxis().setRange(800.0, 950.0);
        		frame.pack();
        		RefineryUtilities.centerFrameOnScreen(frame);
        		frame.setVisible(true);
        		        		
            }
        });
		
	}

	private static XYVectorFunction readGradeData() {
		LOG.debug("readGradeData()");
		URL url = ClassLoader.getSystemResource("trackLeika_2_M607_ED50_SG.csv");
		
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
		URL url = ClassLoader.getSystemResource("trackLeika_2_M607_ED50_SZ.csv");
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		// data = data.extract(0.0, 1050.0);
		return data;		
	}



	
}
