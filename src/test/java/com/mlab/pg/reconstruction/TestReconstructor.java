package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.util.MathUtil;
import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.VerticalCurveAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class TestReconstructor {

	private static Logger LOG = Logger.getLogger(TestReconstructor.class);
	
	@BeforeClass
	public static void beforeClass() {
		PropertyConfigurator.configure("log4j.properties");	
	}
	
	@Test
	public void test1() throws Exception {
		LOG.debug("test1()");
		VerticalProfile profile = getSampleProfile1();
		double starts = profile.getStartS();
		double ends = profile.getEndS();
		double space = 5.0;
		XYVectorFunction originalVProfilePoints = profile.getSample(starts, ends, space, true);
		VerticalGradeProfile gprofile = profile.derivative();
		XYVectorFunction originalPoints = gprofile.getSample(starts, ends, space, true);
		Reconstructor generator = new Reconstructor(originalPoints,0.0, InterpolationStrategy.LessSquares);
		generator.processUnique(3, 1e-5);
		VerticalProfile vprofile = generator.getVerticalProfile();
		//System.out.println(vprofile);
		XYVectorFunction processedPoints = vprofile.getSample(vprofile.getStartS(), vprofile.getEndS(), 5.0, true);
		for(int i=0; i<processedPoints.size(); i++) {
			//System.out.println(originalVProfilePoints.getX(i) + ", " + originalVProfilePoints.getY(i) + " --- " + processedPoints.getX(i) + ", " + processedPoints.getY(i));
		}
		
		//System.out.println("ecm = "  + MathUtil.ecm(originalVProfilePoints.getYValues(), processedPoints.getYValues()));
	}
	@Test
	public void test2() throws Exception {
		LOG.debug("test2()");
		VerticalProfile profile = getSampleProfile2();
		double starts = profile.getStartS();
		double ends = profile.getEndS();
		double space = 5.0;
		XYVectorFunction originalVProfilePoints = profile.getSample(starts, ends, space, true);
		VerticalGradeProfile gprofile = profile.derivative();
		XYVectorFunction originalPoints = gprofile.getSample(starts, ends, space, true);
		Reconstructor generator = new Reconstructor(originalPoints, 0.0, InterpolationStrategy.LessSquares);
		generator.processUnique(3, 1e-5);
		VerticalProfile vprofile = generator.getVerticalProfile();
		//System.out.println(vprofile);
		XYVectorFunction processedPoints = vprofile.getSample(vprofile.getStartS(), vprofile.getEndS(), 5.0, true);
		for(int i=0; i<processedPoints.size(); i++) {
			//System.out.println(originalVProfilePoints.getX(i) + ", " + originalVProfilePoints.getY(i) + " --- " + processedPoints.getX(i) + ", " + processedPoints.getY(i));
		}
		
		//System.out.println("ecm = "  + MathUtil.ecm(originalVProfilePoints.getYValues(), processedPoints.getYValues()));
	}
	/**
	 * Una VC(S0=0;G0=0.02;Kv=3000;L=80) seguida de una grade con pendiente=0.04672
	 * DesignSpeed = 40
	 * El punto VCE está en s=80, que corresponde al índice i=16 en la XYVectorFunction
	 */
	private VerticalProfile getSampleProfile1() {
		double s0 = 0.0;
		double z0 = 0.0;
		double g0 = 0.02;
		double kv = 3000.0;
		double ends = 80.0;
		VerticalCurveAlignment vc = new VerticalCurveAlignment(s0, z0, g0, kv, ends);		
		Straight r = new Straight(80.0, vc.getY(80.0), vc.getTangent(80.0));
		GradeAlignment grade = new GradeAlignment(r, 80.0, 160.0);

		VerticalProfile profile = new VerticalProfile();
		profile.add(vc);
		profile.add(grade);
		//System.out.println(profile);
		return profile;
	}
	/**
	 * Una VC(S0=0;G0=0.005;Kv=6000;L=250) seguida de una grade con pendiente=0.0468
	 * DesignSpeed = 80
	 * El punto VCE está en s=250, que corresponde al índice i=  en la XYVectorFunction
	 */
	private VerticalProfile getSampleProfile2() {
		double s0 = 0.0;
		double z0 = 0.0;
		double g0 = 0.005;
		double kv = 6000.0;
		double ends = 250.0;
		VerticalCurveAlignment vc = new VerticalCurveAlignment(s0, z0, g0, kv, ends);		
		//System.out.println(vc.getEndZ());
		Straight r = new Straight(250.0, vc.getY(250.0), vc.getTangent(250.0));
		GradeAlignment grade = new GradeAlignment( r, 250.0, 500.0);
		//System.out.println(grade.getStartZ());
		VerticalProfile profile = new VerticalProfile();
		profile.add(vc);
		profile.add(grade);
		//System.out.println(profile);
		return profile;
	}

}
