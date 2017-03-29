package com.mlab.pg;

import java.io.File;
import java.net.URL;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jfree.ui.RefineryUtilities;

import com.mlab.pg.graphics.Charter;
import com.mlab.pg.reconstruction.IterativeReconstructor;
import com.mlab.pg.reconstruction.Reconstructor;
import com.mlab.pg.reconstruction.strategy.PointCharacteriserStrategy_EqualArea;
import com.mlab.pg.reconstruction.strategy.ProcessBorderIntervalsStrategy_EqualArea;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.XYVectorFunction;
import com.mlab.pg.xyfunction.XYVectorFunctionCsvReader;

import junit.framework.Assert;

public class M325_DisplayVerticalProfile {

	static Logger LOG = Logger.getLogger(M325_DisplayVerticalProfile.class);
	
	static Charter charter = null;
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");	
		LOG.debug("main()");
		
		XYVectorFunction originalVProfile = readOriginalVerticalProfile();
		XYVectorFunction gradeData = readGradeData();
		XYVectorFunction integral = gradeData.integrate(589.274);
		showVProfile(originalVProfile, integral);
		
	}

	

	private static void showVProfile(XYVectorFunction originalData, XYVectorFunction integralData) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		charter = new Charter("M-325", "S", "Z");
        		charter.addXYVectorFunction(originalData, "Original Data");
        		charter.addXYVectorFunction(integralData, "Integral Data");        		
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

	private static XYVectorFunction readOriginalVerticalProfile() {
		LOG.debug("readOriginalVerticalProfile()");
		URL url = ClassLoader.getSystemResource("M325_SZ.csv");
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		data = data.extract(0.0, 8450.0);
		return data;		
	}

	private static XYVectorFunction readGradeData() {
		LOG.debug("readGradeData()");
		URL url = ClassLoader.getSystemResource("M325_SG.csv");
		
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		data = data.extract(0.0, 8450.0);
		return data;
	}

	
}
