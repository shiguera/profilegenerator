package com.mlab.pg;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jfree.ui.RefineryUtilities;

import com.mlab.pg.graphics.Charter;
import com.mlab.pg.reconstruction.Reconstructor2;
import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.VerticalCurveAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.Parabole;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class TestReconstructor2_WithChartView {

	static Logger LOG = Logger.getLogger(TestReconstructor2_WithChartView.class);
	
	static Charter charter = null;
	// Lo establece el método readData
	static String charterName = "";
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");	

		LOG.debug("main()");
		
		VerticalProfile originalVProfile = getSampleVerticalProfile_Type_I();
		VerticalGradeProfile originalGProfile = originalVProfile.derivative();
		double starts = originalGProfile.getStartS();
		double startZ = originalVProfile.get(0).getStartZ();
		double ends = originalGProfile.getEndS();
		double space = 2.0;
		XYVectorFunction originalGData = originalGProfile.getSample(starts, ends, space, true);
		int baseSize = 10;
		double thresholdSlope = 1e-5;
		
		Reconstructor2 reconstructor = null;
		try {
			reconstructor = new Reconstructor2(originalGData, baseSize, thresholdSlope, startZ);
		} catch(Exception e) {
			LOG.error("Error creating Reconstructor");
			System.exit(-1);
		}
		VerticalGradeProfile resultGProfile = reconstructor.getGradeProfile();
		System.out.println(resultGProfile);
		VerticalProfile resultVProfile = reconstructor.getVerticalProfile();
		System.out.println(resultVProfile);
		XYVectorFunction resultVProfileSample = resultVProfile.getSample(starts, ends, space, true);
		XYVectorFunction originalVProfileSample = originalVProfile.getSample(starts, ends, space, true);
		showVerticalProfiles(originalVProfileSample, resultVProfileSample);

		XYVectorFunction originalGradeData = originalGProfile.getSample(starts, ends, space, true);
		XYVectorFunction resultGradeData = resultGProfile.getSample(starts, ends, space, true);
		showGradeProfiles(originalGradeData, resultGradeData);
	}

	

	private static void showVerticalProfiles(XYVectorFunction originalVProfileData, XYVectorFunction resultVProfileData) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	Charter vprofileCharter = new Charter("Vertical Prfiles", "S", "Z");
        		vprofileCharter.addXYVectorFunction(originalVProfileData, "Original Data");
        		vprofileCharter.addXYVectorFunction(resultVProfileData, "Result Data");
            	JFrame frame = new JFrame("Vertical Profiles");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        		frame.setContentPane(vprofileCharter.getChartPanel());
        		//charter.getChart().getXYPlot().getRangeAxis().setRange(650.0, 850.0);
        		frame.pack();
        		RefineryUtilities.centerFrameOnScreen(frame);
        		frame.setVisible(true);     		
            }
        });
	}
		private static void showGradeProfiles(XYVectorFunction originalGradeData, XYVectorFunction resultGradeData) {
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	        		Charter gradeCharter = new Charter("Sample with Grade Profiles", "S", "G");
	        		gradeCharter.addXYVectorFunction(originalGradeData, "Original Data");
	        		
	        		gradeCharter.addXYVectorFunction(resultGradeData, "Result Data");
	            	JFrame frame = new JFrame("Grade Profiles");
	                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        		frame.setContentPane(gradeCharter.getChartPanel());
	        		//charter.getChart().getXYPlot().getRangeAxis().setRange(650.0, 850.0);
	        		frame.pack();
	        		RefineryUtilities.centerFrameOnScreen(frame);
	        		frame.setVisible(true);	        		        		
	            }
	        });
	}

	private static VerticalProfile getSampleVerticalProfile_Type_I() {
		LOG.debug("test_VProfile_Type_I");
		Straight r = new Straight(1000.0, 0.085);
		GradeAlignment grade1 = new GradeAlignment(r, 0.0, 964.3);
		Parabole p = new Parabole(928.811801, 0.232647, -0.000077);
		VerticalCurveAlignment vc = new VerticalCurveAlignment(p, 964.3, 2139.9);
		Straight r2 = new Straight(1279.377820, -0.095);
		GradeAlignment grade2 = new GradeAlignment(r2, 2139.9, 3064.0);
		VerticalProfile vp = new VerticalProfile();
		vp.add(grade1);
		vp.add(vc);
		vp.add(grade2);
		return vp;
		
	}

	
	
}
