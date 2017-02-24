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
		
		
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		charter = new Charter(charterName, "S", "G");
        		charter.addXYVectorFunction(data);
            	JFrame frame = new JFrame("Charter");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        		frame.setContentPane(charter.getChartPanel());
        		frame.pack();
        		RefineryUtilities.centerFrameOnScreen(frame);
        		frame.setVisible(true);
        		
        		Charter charter2 = new Charter("Perfil de pdtes reconstruido", "S", "G");
        		charter2.addXYVectorFunction(gprofile.getSample(gprofile.getStartS(), gprofile.getEndS(), 10.0, true));
            	JFrame frame2 = new JFrame("Charter");
                frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        		frame2.setContentPane(charter2.getChartPanel());
        		frame2.pack();
        		RefineryUtilities.centerFrameOnScreen(frame2);
        		frame2.setVisible(true);
        		
        		Charter charter3 = new Charter("Perfil logitudinal reconstruido", "S", "G");
        		charter3.addXYVectorFunction(vprofile.getSample(vprofile.getStartS(), vprofile.getEndS(), 10.0, true));
            	JFrame frame3 = new JFrame("Charter");
                frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        		frame3.setContentPane(charter3.getChartPanel());
        		frame3.pack();
        		RefineryUtilities.centerFrameOnScreen(frame3);
        		frame3.setVisible(true);
        		
        		
            }
        });
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
		XYVectorFunction subdata = data.extract(0.0, 807.022);
		return subdata;
	}
	
}
