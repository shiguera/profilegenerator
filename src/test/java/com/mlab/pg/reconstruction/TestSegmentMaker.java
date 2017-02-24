package com.mlab.pg.reconstruction;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.VAlignFactory;
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
	public void testGrade() throws NullTypeException {
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
			//System.out.println(type);
		}
		Assert.assertNotNull(maker.getOriginalTypeSegmentArray());
		for(TypeInterval segment : maker.getOriginalTypeSegmentArray()) {
			//System.out.println(segment.toString());
		}
		Assert.assertEquals(1, maker.getOriginalTypeSegmentArray().size());
	}

	@Test
	public void testVerticalCurve() throws NullTypeException {
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
			//System.out.println(type);
		}
		Assert.assertNotNull(maker.getOriginalTypeSegmentArray());
		for(TypeInterval segment : maker.getOriginalTypeSegmentArray()) {
			//System.out.println(segment.toString());
		}
		Assert.assertEquals(1, maker.getOriginalTypeSegmentArray().size());	
	}
	@Test
	public void testProcessBorder1() throws NullTypeException {
		LOG.debug("testProcessBorder1()");
		VerticalProfile profile = getSampleProfile1();
		VerticalGradeProfile gradeprofile = profile.derivative();
		//System.out.println(gradeprofile);
		double starts = gradeprofile.getStartS();
		double ends = gradeprofile.getEndS();
		double space = 5.0;
		XYVectorFunction originalGradePoints = gradeprofile.getSample(starts, ends, space, true);
		double thresholdSlope = 1e-5;
		int baseSize = 3;
		SegmentMaker maker = new SegmentMaker(originalGradePoints, baseSize, thresholdSlope);
		//System.out.println(maker.getOriginalSegmentation());
		//System.out.println(maker.getResultSegmentation());
	}
	@Test
	public void testProcessBorder2() throws NullTypeException {
		LOG.debug("testProcessBorder2()");
		VerticalProfile profile = getSampleProfile2();
		VerticalGradeProfile gradeprofile = profile.derivative();
		//System.out.println(gradeprofile);
		double starts = gradeprofile.getStartS();
		double ends = gradeprofile.getEndS();
		double space = 5.0;
		XYVectorFunction originalGradePoints = gradeprofile.getSample(starts, ends, space, true);
		double thresholdSlope = 1e-5;
		int baseSize = 3;
		SegmentMaker maker = new SegmentMaker(originalGradePoints, baseSize, thresholdSlope);
		//System.out.println(maker.getOriginalSegmentation());		
		//System.out.println(maker.getResultSegmentation());
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
	public void testSampleProfileTypeIIa_1() throws NullTypeException {
		LOG.debug("testSampleProfileTypeIIa_1");
		VerticalProfile vp = getSampleProfileTypeIIa_1();
		VerticalGradeProfile gradeProfile = vp.derivative();
		double starts = gradeProfile.getStartS();
		double ends = gradeProfile.getEndS();
		double space = 1.0;
		XYVectorFunction originalGradePoints = gradeProfile.getSample(starts, ends, space, true);
		double thresholdSlope = 1e-6;
		int baseSize = 3;
		SegmentMaker maker = new SegmentMaker(originalGradePoints, baseSize, thresholdSlope);
		//System.out.println(maker.getOriginalSegmentation());
		//System.out.println(maker.getResultSegmentation());
		Assert.assertEquals(4, maker.getResultTypeSegmentArray().size());
	}
	private VerticalProfile getSampleProfileTypeIIa_1() {
		VerticalProfile vp = new VerticalProfile();
		GradeAlignment grade1 = new GradeAlignment(0.0, 1000.0, 0.06, 1050.0);
		vp.add(grade1);
		VerticalCurveAlignment vc1 = VAlignFactory.createVCFrom_PointGradeKvAndFinalSlope(1050.0, 1063.0, 0.06, -50000.0, 0.05);
		vp.add(vc1);
		VerticalCurveAlignment vc2 = VAlignFactory.createVCFrom_PointGradeKvAndFinalSlope(1550.0, 1090.0, 0.05, -55000.0, 0.03);
		vp.add(vc2);
		GradeAlignment grade2 = new GradeAlignment(2650.0, 1134.0, 0.03, 750.0);
		vp.add(grade2);
		return vp;
	}

	@Test
	public void testSampleProfileTypeIIa_2() throws NullTypeException {
		LOG.debug("testSampleProfileTypeIIa_2");
		VerticalProfile vp = getSampleProfileTypeIIa_2();
		VerticalGradeProfile gradeProfile = vp.derivative();
		double starts = gradeProfile.getStartS();
		double ends = gradeProfile.getEndS();
		double space = 7.0;
		XYVectorFunction originalGradePoints = gradeProfile.getSample(starts, ends, space, true);
		double thresholdSlope = 1e-6;
		int baseSize = 4;
		SegmentMaker maker = new SegmentMaker(originalGradePoints, baseSize, thresholdSlope);
		//System.out.println(maker.getOriginalSegmentation());
		//System.out.println(maker.getResultSegmentation());
		Assert.assertEquals(4, maker.getResultTypeSegmentArray().size());
	}	
	private VerticalProfile getSampleProfileTypeIIa_2() {
		VerticalProfile vp = new VerticalProfile();
		GradeAlignment grade1 = new GradeAlignment(0.0, 1000.0, 0.06, 50.0);
		vp.add(grade1);
		VerticalCurveAlignment vc1 = VAlignFactory.createVCFrom_PointGradeKvAndFinalSlope(50.0, 1003.0, 0.06, -32500.0, 0.04);
		vp.add(vc1);
		VerticalCurveAlignment vc2 = VAlignFactory.createVCFrom_PointGradeKvAndFinalSlope(700.0, 1035.0, 0.04, -55000.0, 0.03);
		vp.add(vc2);
		GradeAlignment grade2 = new GradeAlignment(1250.0, 1054.0, 0.03, 1250.0);
		vp.add(grade2);
		return vp;
	}


	@Test
	public void testSagCurveCrestCurve() throws NullTypeException {
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
		TypeIntervalArray segments = maker.getOriginalTypeSegmentArray();
		
		//System.out.println(segments);
		
		
	}

}
