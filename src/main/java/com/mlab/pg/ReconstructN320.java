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
		
		XYVectorFunction data = readData();
		Reconstructor reconstructor = null;
		try {
			reconstructor = new Reconstructor(data, 4, 1e-5, 0.0);
		} catch(Exception e) {
			LOG.error("Error creating Reconstructor");
			System.exit(-1);
		}
		VerticalGradeProfile gprofile = reconstructor.getGradeProfile();
		System.out.println(gprofile);
		VerticalProfile vprofile = reconstructor.getVerticalProfile();
		System.out.println(vprofile);
		XYVectorFunction vprofilesample = vprofile.getSample(0.0, vprofile.getEndS(), 10, true);
		for(int i=0; i<vprofilesample.size(); i++) {
			double[] sz = vprofilesample.get(i);
			vprofilesample.set(i, new double[]{sz[0], sz[1]+ originalVProfile.get(0)[1]});
			double y1 = originalVProfile.getY(i);
			double y2 = vprofilesample.getY(i);
			double dif = y1-y2;
			System.out.println(y1+" "+y2+" "+dif);
		}
		showOriginalVProfile(originalVProfile, vprofilesample);
		
	}

	

	private static void showOriginalVProfile(XYVectorFunction data, XYVectorFunction data2) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		charter = new Charter("N-320: Original VProfile", "S", "G");
        		charter.addXYVectorFunction(data);
        		charter.addXYVectorFunction(data2);
        		
            	JFrame frame = new JFrame("Charter");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        		frame.setContentPane(charter.getChartPanel());
        		charter.getChart().getXYPlot().getRangeAxis().setRange(700.0, 750.0);
        		frame.pack();
        		RefineryUtilities.centerFrameOnScreen(frame);
        		frame.setVisible(true);
        		        		
            }
        });
		
	}



	private static XYVectorFunction readOriginalVerticalProfile() {
		LOG.debug("readOriginalVerticalProfile()");
		charterName = "N-320: Original VeticalProfile";
		URL url = ClassLoader.getSystemResource("N-320_SZ.csv");
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		XYVectorFunction subdata = data.extract(477.34, 1050.0);
		return subdata;		
	}



	private static XYVectorFunction readData() {
		LOG.debug("readData()");
		charterName = "N-320";
		URL url = ClassLoader.getSystemResource("N-320_xyvector.csv");
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		XYVectorFunction subdata = data.extract(477.34, 1050.0);
		return subdata;
		
		
		
	}
	
}
