package com.mlab.pg.graphics;

import java.io.File;
import java.net.URL;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jfree.ui.RefineryUtilities;

import com.mlab.pg.xyfunction.XYVectorFunction;
import com.mlab.pg.xyfunction.XYVectorFunctionCsvReader;

import junit.framework.Assert;

public class TestCharterShow {
	
	static Logger LOG = Logger.getLogger(TestCharter.class);
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");	

		LOG.debug("main()");
		
		Charter charter = new Charter("M-320 slopes", "S", "G");
		XYVectorFunction data = readData();
		charter.addXYVectorFunction(data);
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Charter");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        		frame.setContentPane(charter.getChartPanel());
        		frame.pack();
        		RefineryUtilities.centerFrameOnScreen(frame);
        		
        		frame.setVisible(true);
            }
        });
	}

	

	private static XYVectorFunction readData() {
		LOG.debug("readData()");
		URL url = ClassLoader.getSystemResource("N-320_xyvector_fragment.csv");
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		
		return data;
	}
}
