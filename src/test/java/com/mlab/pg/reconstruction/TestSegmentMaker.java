package com.mlab.pg.reconstruction;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.random.RandomFactory;
import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.VerticalCurveAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

import junit.framework.Assert;

public class TestSegmentMaker {

	
	private final static Logger LOG = Logger.getLogger(TestSegmentMaker.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	
	@Test
	public void testGrade() throws CloneNotSupportedException {
		LOG.debug("testGrade()");
		List<double[]> pts = new ArrayList<double[]>();
		pts.add(new double[] {0.0, 0.04});
		pts.add(new double[] {2.0, 0.04});
		pts.add(new double[] {4.0, 0.04});
		pts.add(new double[] {6.0, 0.04});
		pts.add(new double[] {8.0, 0.04});
		pts.add(new double[] {10.0, 0.04});
		pts.add(new double[] {12.0, 0.04});
		pts.add(new double[] {14.0, 0.04});
		pts.add(new double[] {16.0, 0.04});
		
		
		XYVectorFunction gp = new XYVectorFunction(pts);
		int mobileBaseSize = 3;
		double thresholdSlope = 1e-5;
		
		SegmentMaker maker = new SegmentMaker(gp, mobileBaseSize, thresholdSlope);
		Assert.assertNotNull(maker.getOriginalPointTypes());
		Assert.assertEquals(gp.size(), maker.getOriginalPointTypes().size());
		for(PointType type : maker.getOriginalPointTypes()) {
			System.out.println(type);
		}
		Assert.assertNotNull(maker.getOriginalSegments());
		for(PointTypeSegment segment : maker.getOriginalSegments()) {
			System.out.println(segment.toString());
		}
		Assert.assertEquals(1, maker.getOriginalSegments().size());
	}

	@Test
	public void testVerticalCurve() throws CloneNotSupportedException {
		LOG.debug("testVerticalCurve()");
		List<double[]> pts = new ArrayList<double[]>();
		pts.add(new double[] {0.0, 0.00});
		pts.add(new double[] {2.0, 2.5e-4});
		pts.add(new double[] {4.0, 5e-4});
		pts.add(new double[] {6.0, 7.5e-4});
		pts.add(new double[] {8.0, 0.001});
		pts.add(new double[] {10.0, 0.00125});
		pts.add(new double[] {12.0, 0.0015});
		pts.add(new double[] {14.0, 0.00175});
		
		
		XYVectorFunction gp = new XYVectorFunction(pts);
		
		int mobileBaseSize = 3;
		double thresholdSlope = 1e-5;
		
		SegmentMaker maker = new SegmentMaker(gp, mobileBaseSize, thresholdSlope);
		Assert.assertNotNull(maker.getOriginalPointTypes());
		Assert.assertEquals(gp.size(), maker.getOriginalPointTypes().size());
		
		for(PointType type : maker.getOriginalPointTypes()) {
			System.out.println(type);
		}
		Assert.assertNotNull(maker.getOriginalSegments());
		for(PointTypeSegment segment : maker.getOriginalSegments()) {
			System.out.println(segment.toString());
		}
		Assert.assertEquals(1, maker.getOriginalSegments().size());	
	}
	@Test
	public void testProcessBorder1() throws CloneNotSupportedException {
		LOG.debug("testProcessBorder1()");
		VerticalProfile profile = getSampleProfile1();
		VerticalGradeProfile gradeprofile = profile.derivative();
		System.out.println(gradeprofile);
		double starts = gradeprofile.getStartS();
		double ends = gradeprofile.getEndS();
		double space = 5.0;
		XYVectorFunction originalGradePoints = gradeprofile.getSample(starts, ends, space, true);
		double thresholdSlope = 1e-5;
		int baseSize = 3;
		SegmentMaker maker = new SegmentMaker(originalGradePoints, baseSize, thresholdSlope);
		System.out.println(maker.originalSegments);
		try {
			maker.processBorderSegments();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		System.out.println(maker.processedSegments);
	}
	@Test
	public void testProcessBorder2() throws CloneNotSupportedException {
		LOG.debug("testProcessBorder2()");
		VerticalProfile profile = getSampleProfile2();
		VerticalGradeProfile gradeprofile = profile.derivative();
		System.out.println(gradeprofile);
		double starts = gradeprofile.getStartS();
		double ends = gradeprofile.getEndS();
		double space = 5.0;
		XYVectorFunction originalGradePoints = gradeprofile.getSample(starts, ends, space, true);
		double thresholdSlope = 1e-5;
		int baseSize = 3;
		SegmentMaker maker = new SegmentMaker(originalGradePoints, baseSize, thresholdSlope);
		System.out.println(maker.originalSegments);
		try {
			maker.processBorderSegments();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(maker.processedSegments);
	}
	@Test
	public void testProcessBorder3() throws CloneNotSupportedException {
		LOG.debug("testProcessBorder3()");
		VerticalProfile profile = RandomFactory.randomVerticalProfileType_I(DesignSpeed.DS100, 0.0, 0.0);
		System.out.println(profile);
		VerticalGradeProfile gradeprofile = profile.derivative();
		//System.out.println(gradeprofile);
		double starts = gradeprofile.getStartS();
		double ends = gradeprofile.getEndS();
		double space = 5.0;
		XYVectorFunction originalGradePoints = gradeprofile.getSample(starts, ends, space, true);
		double thresholdSlope = 1e-5;
		int baseSize = 3;
		SegmentMaker maker = new SegmentMaker(originalGradePoints, baseSize, thresholdSlope);
		System.out.println(maker.originalSegments);
		try {
			maker.processBorderSegments();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		System.out.println(maker.processedSegments);
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
		Straight r = new Straight(250.0, vc.getY(250.0), vc.getTangent(250.0));
		GradeAlignment grade = new GradeAlignment(r, 250.0, 500.0);

		VerticalProfile profile = new VerticalProfile();
		profile.add(vc);
		profile.add(grade);
		return profile;
	}

	


	@Test
	public void testSagCurveCrestCurve() throws CloneNotSupportedException {
		LOG.debug("testSagCurveCrestCurve()");
		
		double s0 = 0.0;
		double z0 = 1000.0;
		double g0 = 0.03;
		double Kv0 = 10000.0;
		double s1 = 150.0;
		VerticalCurveAlignment sag = new VerticalCurveAlignment(s0, z0, g0, Kv0, s1);
		
		double z1 = sag.getEndZ(); // 1005.625
		double g1 = sag.getEndTangent(); // 0.045
		double Kv1 = -20000.0;
		double s2 = 750.0;
		VerticalCurveAlignment crest = new VerticalCurveAlignment(s1, z1, g1, Kv1, s2);
		
		VerticalProfile profile = new VerticalProfile();
		profile.add(sag);
		profile.add(crest);
		VerticalGradeProfile gradeprofile = profile.derivative();
		XYVectorFunction gradesample = gradeprofile.getSample(s0, s2, 5, true);
		
		int mobileBaseSize = 3;
		double thresholdSlope = 1e-5;
		SegmentMaker maker = new SegmentMaker(gradesample, mobileBaseSize, thresholdSlope);
		PointTypeSegmentArray segments = maker.getOriginalSegments();
		
		System.out.println(segments);
		
		
	}

}
