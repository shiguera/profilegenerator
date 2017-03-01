package com.mlab.pg;

import java.io.File;
import java.net.URL;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jfree.ui.RefineryUtilities;

import com.mlab.pg.graphics.Charter;
import com.mlab.pg.reconstruction.Reconstructor;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.XYVectorFunction;
import com.mlab.pg.xyfunction.XYVectorFunctionCsvReader;

import junit.framework.Assert;

public class ReconstructN320 {

	static Logger LOG = Logger.getLogger(ReconstructN320.class);
	
	static Charter charter = null;
	// Lo establece el método readData
	static String charterName = "";
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");	

		LOG.debug("main()");
		
		XYVectorFunction originalVProfile = readOriginalVerticalProfile();
		XYVectorFunction gradeData = readGradeData();
		
		Reconstructor reconstructor = null;
		try {
			reconstructor = new Reconstructor(gradeData, 4, 0.75e-5, 727.0);
		} catch(Exception e) {
			LOG.error("Error creating Reconstructor");
			System.exit(-1);
		}
		
		VerticalGradeProfile gprofile = reconstructor.getGradeProfile();
		//System.out.println(gprofile);
		
		VerticalProfile vprofile = reconstructor.getVerticalProfile();
		//System.out.println(vprofile);
		
		XYVectorFunction resultVProfileSample = vprofile.getSample(0.0, vprofile.getEndS(), 10, true);
		
		showVProfiles(originalVProfile, resultVProfileSample);
		
	}

	

	private static void showVProfiles(XYVectorFunction originalData, XYVectorFunction resultData) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		charter = new Charter("N-320: Original VProfile", "S", "G");
        		charter.addXYVectorFunction(originalData, "Original Data");
        		charter.addXYVectorFunction(resultData, "Result Data");
        		
            	JFrame frame = new JFrame("Charter");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        		frame.setContentPane(charter.getChartPanel());
        		charter.getChart().getXYPlot().getRangeAxis().setRange(700.0, 850.0);
        		frame.pack();
        		RefineryUtilities.centerFrameOnScreen(frame);
        		frame.setVisible(true);
        		        		
            }
        });
		
	}



	private static XYVectorFunction readOriginalVerticalProfile() {
		LOG.debug("readOriginalVerticalProfile()");
		charterName = "N-320: Original VeticalProfile";
		URL url = ClassLoader.getSystemResource("N-320_CalculoDeZ_APartirDeG.csv");
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
		charterName = "N-320";
		URL url = ClassLoader.getSystemResource("N-320_SG.csv");
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		//data = data.extract(0.0, 1050.0);
		return data;
		
		
		
	}
	
}
