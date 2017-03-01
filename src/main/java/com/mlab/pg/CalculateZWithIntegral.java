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

public class CalculateZWithIntegral {

	static Logger LOG = Logger.getLogger(CalculateZWithIntegral.class);
	
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");	

		LOG.debug("main()");
		
		XYVectorFunction originalVProfile = readOriginalVerticalProfile();
		double Z0= originalVProfile.getY(0);
		
		XYVectorFunction slopesProfile = readSlopesProfile();

		XYVectorFunction calculatedVProfile = new XYVectorFunction();
		double[] firstPoint = new double[]{slopesProfile.getStartX(), Z0};
		calculatedVProfile.add(firstPoint);
		double previousZ = Z0;
		for (int i=1; i<slopesProfile.size(); i++) {
			double previousS = slopesProfile.getX(i-1);
			double previousG = slopesProfile.getY(i-1);
			double S = slopesProfile.getX(i);
			double G = slopesProfile.getY(i);
			double incZ = (previousG + G)/2*(S-previousS);
			double Z = previousZ + incZ;
			double[] newPoint = new double[]{S, Z};
			calculatedVProfile.add(newPoint);
			previousZ = Z;
		}

		XYVectorFunction calculatedVProfile2 = new XYVectorFunction();
		calculatedVProfile2.add(firstPoint);
		previousZ = Z0;
		for (int i=1; i<slopesProfile.size(); i++) {
			double previousS = slopesProfile.getX(i-1);
			double previousG = slopesProfile.getY(i-1);
			double S = slopesProfile.getX(i);
			double G = slopesProfile.getY(i);
			double[] r = MathUtil.rectaPorDosPuntos(new double[]{previousS, previousG}, new double[]{S,G});
			double middleS = (S + previousS)/2;
			double middleG = r[0] + r[1] * middleS;
			double incZ = middleG*(S-previousS);
			double Z = previousZ + incZ;
			double[] newPoint = new double[]{S, Z};
			calculatedVProfile2.add(newPoint);
			previousZ = Z;
		}

		showProfiles(originalVProfile, calculatedVProfile, calculatedVProfile2);
		
	}

	

	private static void showProfiles(XYVectorFunction originalData, XYVectorFunction resultData1, XYVectorFunction resultData2) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		Charter charter = new Charter("N-320: Original and calculated VProfile", "S", "Z");
        		charter.addXYVectorFunction(originalData, "Original Data");
        		charter.addXYVectorFunction(resultData1, "Calculated Data Trapecio");
        		charter.addXYVectorFunction(resultData2, "Calculated Data Rectángulo");
        		
        		
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
		URL url = ClassLoader.getSystemResource("N-320_CalculoDeZ_APartirDeG.csv");
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		// data = data.extract(0.0, 1050.0);
		return data;		
	}
	private static XYVectorFunction readSlopesProfile() {
		LOG.debug("readSlopesProfile()");
		URL url = ClassLoader.getSystemResource("N-320_SG.csv");
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		// data = data.extract(0.0, 1050.0);
		return data;		
	}



}
