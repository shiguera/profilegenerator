package com.mlab.pg;

import java.io.File;
import java.net.URL;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jfree.ui.RefineryUtilities;

import com.mlab.pg.graphics.Charter;
import com.mlab.pg.util.MathUtil;
import com.mlab.pg.xyfunction.XYVectorFunction;
import com.mlab.pg.xyfunction.XYVectorFunctionCsvReader;

import junit.framework.Assert;

public class ComparacionPerfilGPS_PerfilGrade_N320_Inventario2008 {

	static Logger LOG = Logger.getLogger(ComparacionPerfilGPS_PerfilGrade_N320_Inventario2008.class);
	
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");	

		LOG.debug("main()");
		
		XYVectorFunction gpsProfile = readGPSVerticalProfile();
		
		XYVectorFunction slopesProfile = readVerticalProfileThroughSlopes();


		double maxdif = 0.0;
		double mean = 0.0;
		for(int i=0; i<gpsProfile.size(); i++) {
			double x1 = gpsProfile.getX(i);
			double y1 = gpsProfile.getY(i);
			double y2 = slopesProfile.getY(x1);
			double dif = Math.abs(y2 - y1);
			if (dif > maxdif) {
				maxdif = dif;
			}
			mean =mean + dif;
		}
		mean = mean / gpsProfile.size();
		System.out.println("Max dif: " + maxdif);
		System.out.println("Media : " + mean);
		
		showProfiles(gpsProfile, slopesProfile);
		
	}

	

	private static void showProfiles(XYVectorFunction gpsData, XYVectorFunction slopesData) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		Charter charter = new Charter("N-320: Vertical ProfilesComparison", "S", "Z");
        		charter.addXYVectorFunction(gpsData, "Vertical profile based on GPS Data");
        		charter.addXYVectorFunction(slopesData, "Vertical Profile based on Grade Data");
   		
        		
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



	private static XYVectorFunction readVerticalProfileThroughSlopes() {
		LOG.debug("readOriginalVerticalProfile()");
		URL url = ClassLoader.getSystemResource("N-320_CalculoDeZ_APartirDeG.csv");
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		// data = data.extract(0.0, 1050.0);
		return data;		
	}
	private static XYVectorFunction readGPSVerticalProfile() {
		LOG.debug("readGPSProfile()");
		URL url = ClassLoader.getSystemResource("N-320_SZ.csv");
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		// data = data.extract(0.0, 1050.0);
		return data;		
	}



}
