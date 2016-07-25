package com.mlab.pg.reconstruction;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.random.RandomFactory;
import com.mlab.pg.valign.Grade;
import com.mlab.pg.valign.GradeProfile;
import com.mlab.pg.valign.VerticalCurve;
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
	public void testGrade() {
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
		Assert.assertEquals(3, maker.getOriginalSegments().size());
	}

	@Test
	public void testVerticalCurve() {
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
		Assert.assertEquals(3, maker.getOriginalSegments().size());	
	}
	@Test
	public void testProcessBorder1() {
		LOG.debug("testProcessBorder1()");
		VerticalProfile profile = getSampleProfile1();
		GradeProfile gradeprofile = profile.derivative();
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
			maker.processBorderPoints();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		System.out.println(maker.processedSegments);
	}
	@Test
	public void testProcessBorder2() {
		LOG.debug("testProcessBorder2()");
		VerticalProfile profile = getSampleProfile2();
		GradeProfile gradeprofile = profile.derivative();
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
			maker.processBorderPoints();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(maker.processedSegments);
	}
	@Test
	public void testProcessBorder3() {
		LOG.debug("testProcessBorder3()");
		VerticalProfile profile = RandomFactory.randomVerticalProfileType_I(DesignSpeed.DS100, 0.0, 0.0);
		System.out.println(profile);
		GradeProfile gradeprofile = profile.derivative();
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
			maker.processBorderPoints();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		System.out.println(maker.processedSegments);
	}
	@Test
	public void testVerticalCurveToVerticalCurve() {
//		LOG.debug("testVerticalCurveToVerticalCurve()");
//		List<double[]> pts = new ArrayList<double[]>();
//		pts.add(new double[] {0.0, 0.00});
//		pts.add(new double[] {2.0, 2.5e-4});
//		pts.add(new double[] {4.0, 5e-4});
//		pts.add(new double[] {6.0, 7.5e-4});
//		pts.add(new double[] {8.0, 1.0e-3});
//		pts.add(new double[] {10.0, 1.25e-3});
//		pts.add(new double[] {12.0, 1.0e-3});
//		pts.add(new double[] {14.0, 7.5e-4});
//		pts.add(new double[] {16.0, 5e-4});
//		pts.add(new double[] {18.0, 2.5e-4});
//		pts.add(new double[] {20.0, 0.0});
//		
//		XYVectorFunction gp = new XYVectorFunction(pts);
//		int mobileBaseSize = 3;
//		double thresholdSlope = 1e-5;
//		
//		SegmentMaker maker = new SegmentMaker(gp, mobileBaseSize, thresholdSlope);
//		Assert.assertNotNull(maker.getPointTypes());
//		Assert.assertEquals(gp.size(), maker.getPointTypes().size());
//		for(PointType type : maker.getPointTypes()) {
//			System.out.println(type);
//		}
//		Assert.assertNotNull(maker.getPointTypeSegments());
//		for(PointTypeSegment segment : maker.getPointTypeSegments()) {
//			System.out.println(segment.toString());
//		}
//		Assert.assertEquals(7, maker.getPointTypeSegments().size());
	}

	@Test
	public void testVerticalCurveEnd() {
//		LOG.debug("testVerticalCurveEnd()");
//		List<double[]> pts = new ArrayList<double[]>();
//		pts.add(new double[] {0.0, 0.04075});
//		pts.add(new double[] {2.0, 0.04050});
//		pts.add(new double[] {4.0, 0.04025});
//		pts.add(new double[] {6.0, 0.04});
//		pts.add(new double[] {8.0, 0.03975});
//		pts.add(new double[] {10.0, 0.03950});
//		pts.add(new double[] {12.0, 0.03925});
//		pts.add(new double[] {14.0, 0.03900});
//		
//		pts.add(new double[] {16.0, 0.038750});
//		
//		pts.add(new double[] {18.0, 0.038750});
//		pts.add(new double[] {20.0, 0.038750});
//		pts.add(new double[] {22.0, 0.038750});
//		pts.add(new double[] {24.0, 0.038750});
//		pts.add(new double[] {26.0, 0.038750});
//		pts.add(new double[] {28.0, 0.038750});
//		pts.add(new double[] {30.0, 0.038750});
//		
//		
//		XYVectorFunction gp = new XYVectorFunction(pts);
//		int mobileBaseSize = 3;
//		double thresholdSlope = 1e-5;
//		
//		SegmentMaker maker = new SegmentMaker(gp, mobileBaseSize, thresholdSlope);
//		Assert.assertNotNull(maker.getPointTypes());
//		Assert.assertEquals(gp.size(), maker.getPointTypes().size());
//		Assert.assertTrue(maker.getPointTypes().get(8)==PointType.BORDER_POINT);
//		for(PointType type : maker.getPointTypes()) {
//			System.out.println(type);
//		}
//		Assert.assertNotNull(maker.getPointTypeSegments());
//		for(PointTypeSegment segment : maker.getPointTypeSegments()) {
//			System.out.println(segment.toString());
//		}
//		Assert.assertEquals(6, maker.getPointTypeSegments().size());		
	}

	@Test
	public void testVerticalCurveBeginning() {
//		LOG.debug("testVerticalCurveBeginning()");
//		List<double[]> pts = new ArrayList<double[]>();
//		pts.add(new double[] {0.0, 0.04075});
//		pts.add(new double[] {2.0, 0.04075});
//		pts.add(new double[] {4.0, 0.04075});
//		pts.add(new double[] {6.0, 0.04075});
//		pts.add(new double[] {8.0, 0.04075});
//		pts.add(new double[] {10.0, 0.04075});
//		pts.add(new double[] {12.0, 0.04075});
//		pts.add(new double[] {14.0, 0.04075});
//		pts.add(new double[] {16.0, 0.04075});
//		pts.add(new double[] {18.0, 0.04075});
//		
//		pts.add(new double[] {20.0, 0.04075});
//		
//		pts.add(new double[] {22.0, 0.04050});
//		pts.add(new double[] {24.0, 0.04025});
//		pts.add(new double[] {26.0, 0.04});
//		pts.add(new double[] {28.0, 0.03975});
//		pts.add(new double[] {30.0, 0.03950});
//		pts.add(new double[] {32.0, 0.03925});
//		pts.add(new double[] {34.0, 0.03900});
//		pts.add(new double[] {36.0, 0.038750});
//		
//		XYVectorFunction gp = new XYVectorFunction(pts);
//		int mobileBaseSize = 4;
//		double thresholdSlope = 1e-5;
//		
//		SegmentMaker maker = new SegmentMaker(gp, mobileBaseSize, thresholdSlope);
//		Assert.assertNotNull(maker.getPointTypes());
//		Assert.assertEquals(gp.size(), maker.getPointTypes().size());
//		Assert.assertTrue(maker.getPointTypes().get(9)==PointType.BORDER_POINT);
//		for(PointType type : maker.getPointTypes()) {
//			System.out.println(type);
//		}
//		Assert.assertNotNull(maker.getPointTypeSegments());
//		for(PointTypeSegment segment : maker.getPointTypeSegments()) {
//			System.out.println(segment.toString());
//		}
//		Assert.assertEquals(6, maker.getPointTypeSegments().size());		
	}
	
	/**
	 * Una VC(S0=0;G0=0.02;Kv=3000;L=80) seguida de una grade con pendiente=0.04672
	 * DesignSpeed = 40
	 * El punto VCE está en s=80, que corresponde al índice i=16 en la XYVectorFunction
	 */
	private VerticalProfile getSampleProfile1() {
		DesignSpeed dspeed = DesignSpeed.DS40;
		double s0 = 0.0;
		double z0 = 0.0;
		double g0 = 0.02;
		double kv = 3000.0;
		double ends = 80.0;
		VerticalCurve vc = new VerticalCurve(dspeed, s0, z0, g0, kv, ends);		
		Straight r = new Straight(80.0, vc.getY(80.0), vc.getTangent(80.0));
		Grade grade = new Grade(dspeed, r, 80.0, 160.0);

		VerticalProfile profile = new VerticalProfile(dspeed);
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
		DesignSpeed dspeed = DesignSpeed.DS80;
		double s0 = 0.0;
		double z0 = 0.0;
		double g0 = 0.005;
		double kv = 6000.0;
		double ends = 250.0;
		VerticalCurve vc = new VerticalCurve(dspeed, s0, z0, g0, kv, ends);		
		Straight r = new Straight(80.0, vc.getY(80.0), vc.getTangent(250.0));
		Grade grade = new Grade(dspeed, r, 250.0, 500.0);

		VerticalProfile profile = new VerticalProfile(dspeed);
		profile.add(vc);
		profile.add(grade);
		return profile;
	}

	
	/**
	 * Test con una VC(S0=0;G0=0.02;Kv=3000;L=80) seguida de una grade con pendiente=0.04672
	 * El punto VCE está en s=80, que corresponde al índice i=16 en la XYVectorFunction
	 */
	@Test
	public void testProcessVerticalCurveEndings_ProfileSample1() {
//		LOG.debug("testProcessVerticalCurveEndings_ProfileSample1()");
//		
//		VerticalProfile profile = this.getSampleProfile1();
//		System.out.println(profile.toString());
//		
//		GradeProfile gradeProfile = profile.derivative();
//		XYVectorFunction sample = gradeProfile.getSample(0.0, 160.0, 5, true);
//		// System.out.println(lastGrade);
//		// System.out.println(sample.size()-1);
//
//		int mobileBaseSize = 4;
//		System.out.println("mobileBaseSize = " + mobileBaseSize);
//		double thresholdSlope = 1e-5;		
//		System.out.println("thresholdSlope = " + thresholdSlope);
//		
//		
//		SegmentMaker maker = new SegmentMaker(sample, mobileBaseSize, thresholdSlope);
//		Assert.assertNotNull(maker.getPointTypes());
//		Assert.assertEquals(sample.size(), maker.getPointTypes().size());
//		for(PointType type : maker.getPointTypes()) {
//			System.out.println(type);
//		}
//		Assert.assertNotNull(maker.getPointTypeSegments());
//		System.out.println("\nBefore processing segments");
//		for(PointTypeSegment segment : maker.getPointTypeSegments()) {
//			System.out.println(segment.toString());
//		}
//		// Comprobar que se sacan seis segmentos: NULL - VC - VCVC - VCE - G 
//		Assert.assertEquals(6, maker.getPointTypeSegments().size());		
//		// Comprobar que el verdadero punto VCE es identificado como tal
//		Assert.assertEquals(PointType.BORDER_POINT, maker.getPointTypes().get(16));
//		
//		maker.processVerticalCurveEndings();
//		System.out.println("\nAfter processing segments");
//		for(PointTypeSegment segment : maker.getPointTypeSegments()) {
//			System.out.println(segment.toString());
//		}
//		Assert.assertEquals(5, maker.getPointTypeSegments().size());		
	}
	@Test
	public void testProcessVerticalCurveEndings_ProfileSample2() {
//		LOG.debug("testProcessVerticalCurveEndings_ProfileSample2()");
//		
//		VerticalProfile profile = this.getSampleProfile2();
//		System.out.println(profile.toString());
//		
//		GradeProfile gradeProfile = profile.derivative();
//		XYVectorFunction sample = gradeProfile.getSample(0.0, 500.0, 5, true);
//		// System.out.println(lastGrade);
//		// System.out.println(sample.size()-1);
//
//		int mobileBaseSize = 4;
//		System.out.println("mobileBaseSize = " + mobileBaseSize);
//		double thresholdSlope = 1e-5;		
//		System.out.println("thresholdSlope = " + thresholdSlope);
//		
//		
//		SegmentMaker maker = new SegmentMaker(sample, mobileBaseSize, thresholdSlope);
//		Assert.assertNotNull(maker.getPointTypes());
//		Assert.assertEquals(sample.size(), maker.getPointTypes().size());
//		for(PointType type : maker.getPointTypes()) {
//			System.out.println(type);
//		}
//		Assert.assertNotNull(maker.getPointTypeSegments());
//		System.out.println("\nBefore processing segments");
//		for(PointTypeSegment segment : maker.getPointTypeSegments()) {
//			System.out.println(segment.toString());
//		}
//		// Comprobar que se sacan seis segmentos: NULL - VC - VCVC - VCE - G 
//		Assert.assertEquals(6, maker.getPointTypeSegments().size());		
//		// Comprobar que el verdadero punto VCE es identificado como tal
//		Assert.assertEquals(PointType.BORDER_POINT, maker.getPointTypes().get(51));
////		
//		maker.processVerticalCurveEndings();
//		System.out.println("\nAfter processing segments");
//		for(PointTypeSegment segment : maker.getPointTypeSegments()) {
//			System.out.println(segment.toString());
//		}
//		Assert.assertEquals(5, maker.getPointTypeSegments().size());		
	}

	@Test
	public void testProcessVerticalCurveBeginnings() {
//		LOG.debug("testProcessVerticalCurveBeginningsing()");
//		List<double[]> pts = new ArrayList<double[]>();
//
//		pts.add(new double[] {0.0, 0.04075});
//		pts.add(new double[] {2.0, 0.04075});
//		pts.add(new double[] {4.0, 0.04075});
//		pts.add(new double[] {6.0, 0.04075});
//		pts.add(new double[] {8.0, 0.04075});
//		pts.add(new double[] {10.0, 0.04075});
//		pts.add(new double[] {12.0, 0.04075});
//		pts.add(new double[] {14.0, 0.04075});
//		pts.add(new double[] {16.0, 0.04075});
//		pts.add(new double[] {18.0, 0.04075});
//		
//		pts.add(new double[] {20.0, 0.04075});
//		
//		pts.add(new double[] {22.0, 0.04050});
//		pts.add(new double[] {24.0, 0.04025});
//		pts.add(new double[] {26.0, 0.04});
//		pts.add(new double[] {28.0, 0.03975});
//		pts.add(new double[] {30.0, 0.03950});
//		pts.add(new double[] {32.0, 0.03925});
//		pts.add(new double[] {34.0, 0.03900});
//		pts.add(new double[] {36.0, 0.038750});
//		
//		XYVectorFunction gp = new XYVectorFunction(pts);
//		int mobileBaseSize = 4;
//		double thresholdSlope = 1e-5;
//		
//		SegmentMaker maker = new SegmentMaker(gp, mobileBaseSize, thresholdSlope);
//		Assert.assertNotNull(maker.getPointTypes());
//		Assert.assertEquals(gp.size(), maker.getPointTypes().size());
//		Assert.assertTrue(maker.getPointTypes().get(9)==PointType.BORDER_POINT);
//		for(PointType type : maker.getPointTypes()) {
//			System.out.println(type);
//		}
//		Assert.assertNotNull(maker.getPointTypeSegments());
//		for(PointTypeSegment segment : maker.getPointTypeSegments()) {
//			System.out.println(segment.toString());
//		}
//		Assert.assertEquals(6, maker.getPointTypeSegments().size());		
//		
//		maker.processVerticalCurveBeginnings();
//		Assert.assertEquals(5, maker.getPointTypeSegments().size());		
//		for(PointTypeSegment segment : maker.getPointTypeSegments()) {
//			System.out.println(segment.toString());
//		}
	}


	@Test
	public void testSagCurveCrestCurve() {
		LOG.debug("testSagCurveCrestCurve()");
		
		DesignSpeed dspeed = DesignSpeed.DS100;
		double s0 = 0.0;
		double z0 = 1000.0;
		double g0 = 0.03;
		double Kv0 = 10000.0;
		double s1 = 150.0;
		VerticalCurve sag = new VerticalCurve(dspeed, s0, z0, g0, Kv0, s1);
		
		double z1 = sag.getEndZ(); // 1005.625
		double g1 = sag.getEndTangent(); // 0.045
		double Kv1 = -20000.0;
		double s2 = 750.0;
		VerticalCurve crest = new VerticalCurve(dspeed, s1, z1, g1, Kv1, s2);
		
		VerticalProfile profile = new VerticalProfile(dspeed);
		profile.add(sag);
		profile.add(crest);
		GradeProfile gradeprofile = profile.derivative();
		XYVectorFunction gradesample = gradeprofile.getSample(s0, s2, 5, true);
		
		int mobileBaseSize = 3;
		double thresholdSlope = 1e-5;
		SegmentMaker maker = new SegmentMaker(gradesample, mobileBaseSize, thresholdSlope);
		PointTypeSegmentArray segments = maker.getOriginalSegments();
		
		System.out.println(segments);
		
		
	}

}
