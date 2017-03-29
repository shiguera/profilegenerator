package com.mlab.pg.reconstruction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.VAlignFactory;
import com.mlab.pg.valign.VerticalCurveAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.valign.VerticalProfileWriter;
import com.mlab.pg.xyfunction.XYVectorFunction;


public class TestTypeIntervalArrayGenerator {

	
	private final static Logger LOG = Logger.getLogger(TestTypeIntervalArrayGenerator.class);
	double MIN_LENGTH = 40.0;
	static InterpolationStrategy lessSquaresStrategy;
	static InterpolationStrategy equalAreaSquaresStrategy;
	static int mobileBaseSize;
	static double thresholdSlope;
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
		lessSquaresStrategy = InterpolationStrategy.LessSquares;
		equalAreaSquaresStrategy = InterpolationStrategy.EqualArea;
		mobileBaseSize = 3;
		thresholdSlope = 1e-5;
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
		
		TypeIntervalArrayGenerator maker = new TypeIntervalArrayGenerator(gp, mobileBaseSize, thresholdSlope, InterpolationStrategy.LessSquares, MIN_LENGTH);
		Assert.assertNotNull(maker.getResultTypeIntervalArray());
//		for(TypeInterval interval: maker.getResultTypeIntervalArray()) {
//			System.out.println(interval);
//		}
		Assert.assertEquals(1, maker.getResultTypeIntervalArray().size());
		Assert.assertEquals(PointType.GRADE, maker.getResultTypeIntervalArray().get(0).getPointType());

		maker = new TypeIntervalArrayGenerator(gp, mobileBaseSize, thresholdSlope, InterpolationStrategy.EqualArea, MIN_LENGTH);
		Assert.assertNotNull(maker.getResultTypeIntervalArray());
		Assert.assertEquals(1, maker.getResultTypeIntervalArray().size());
		Assert.assertEquals(PointType.GRADE, maker.getResultTypeIntervalArray().get(0).getPointType());

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
		
		TypeIntervalArrayGenerator maker = new TypeIntervalArrayGenerator(gp, mobileBaseSize, thresholdSlope, lessSquaresStrategy, MIN_LENGTH);
		TypeIntervalArray result = maker.getResultTypeIntervalArray();
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());	
		Assert.assertEquals(PointType.VERTICAL_CURVE, result.get(0).getPointType());
	}
	

	

	@Test
	public void testSampleProfileTypeV_1() {
		LOG.debug("testSampleProfileTypeV_1");
		VerticalProfile vp = getSampleProfileTypeV_1();
		System.out.println(vp.toString2());
		VerticalProfileWriter.writeVerticalProfile(new File("TestProfile.txt"), vp, "Vertical Profile");
		VerticalGradeProfile gradeProfile = vp.derivative();
		double starts = gradeProfile.getStartS();
		double ends = gradeProfile.getEndS();
		double space = 1.0;
		XYVectorFunction originalGradePoints = gradeProfile.getSample(starts, ends, space, true);

		// El perfil tiene 4 alineaciones. Las dos VC tienenparámetros de 50000 (thSlope=2e-5) y 55000(thSlope=1.82e-5)
		// Con pendiente límite 1e-5 no distingue entre las dos vertical curves
		thresholdSlope = 1e-5;
		TypeIntervalArrayGenerator maker = new TypeIntervalArrayGenerator(originalGradePoints, mobileBaseSize, thresholdSlope, lessSquaresStrategy, MIN_LENGTH);
		TypeIntervalArray result = maker.getResultTypeIntervalArray();
		//System.out.println(result);
		Assert.assertEquals(3, result.size());
		Assert.assertEquals(PointType.GRADE, result.get(0).getPointType());
		Assert.assertEquals(PointType.VERTICAL_CURVE, result.get(1).getPointType());
		Assert.assertEquals(PointType.GRADE, result.get(2).getPointType());
				
		maker = new TypeIntervalArrayGenerator(originalGradePoints, mobileBaseSize, thresholdSlope, equalAreaSquaresStrategy, MIN_LENGTH);
		result = maker.getResultTypeIntervalArray();
		//System.out.println(result);
		Assert.assertEquals(3, result.size());
		Assert.assertEquals(PointType.GRADE, result.get(0).getPointType());
		Assert.assertEquals(PointType.VERTICAL_CURVE, result.get(1).getPointType());
		Assert.assertEquals(PointType.GRADE, result.get(2).getPointType());
		Reconstructor rec = new Reconstructor(originalGradePoints, vp.getAlign(0).getStartZ(), equalAreaSquaresStrategy);
		rec.processUnique(3, thresholdSlope);
		System.out.println(rec.getVerticalProfile().toString2());
		
		// Con pendiente límite 1e-6 sí que distingue entre las dos vertical curves
		thresholdSlope = 1e-6;
		maker = new TypeIntervalArrayGenerator(originalGradePoints, mobileBaseSize, thresholdSlope, lessSquaresStrategy, MIN_LENGTH);
		result = maker.getResultTypeIntervalArray();
		//System.out.println(result);
		Assert.assertEquals(4, result.size());
		Assert.assertEquals(PointType.GRADE, result.get(0).getPointType());
		Assert.assertEquals(PointType.VERTICAL_CURVE, result.get(1).getPointType());
		Assert.assertEquals(PointType.VERTICAL_CURVE, result.get(2).getPointType());
		Assert.assertEquals(PointType.GRADE, result.get(3).getPointType());
				
		maker = new TypeIntervalArrayGenerator(originalGradePoints, mobileBaseSize, thresholdSlope, equalAreaSquaresStrategy, MIN_LENGTH);
		result = maker.getResultTypeIntervalArray();
		//System.out.println(result);
		Assert.assertEquals(4, result.size());
		Assert.assertEquals(PointType.GRADE, result.get(0).getPointType());
		Assert.assertEquals(PointType.VERTICAL_CURVE, result.get(1).getPointType());
		Assert.assertEquals(PointType.VERTICAL_CURVE, result.get(2).getPointType());
		Assert.assertEquals(PointType.GRADE, result.get(3).getPointType());
		rec = new Reconstructor(originalGradePoints, vp.getAlign(0).getStartZ(), equalAreaSquaresStrategy);
		rec.processUnique(3, thresholdSlope);
		System.out.println(rec.getVerticalProfile().toString());
		
	}
	private VerticalProfile getSampleProfileTypeV_1() {
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
	public void testSampleProfileTypeV_2() {
		LOG.debug("testSampleProfileTypeV_2");
		VerticalProfile vp = getSampleProfileTypeV_2();
		VerticalGradeProfile gradeProfile = vp.derivative();
		double starts = gradeProfile.getStartS();
		double ends = gradeProfile.getEndS();
		double space = 2.0;
		XYVectorFunction originalGradePoints = gradeProfile.getSample(starts, ends, space, true);

		TypeIntervalArrayGenerator maker = new TypeIntervalArrayGenerator(originalGradePoints, mobileBaseSize, thresholdSlope, lessSquaresStrategy, MIN_LENGTH);
		TypeIntervalArray result = maker.getResultTypeIntervalArray();
		System.out.println(result);
		Assert.assertEquals(4, result.size());
		Assert.assertEquals(PointType.GRADE, result.get(0).getPointType());
		Assert.assertEquals(PointType.VERTICAL_CURVE, result.get(1).getPointType());
		Assert.assertEquals(PointType.VERTICAL_CURVE, result.get(2).getPointType());
		Assert.assertEquals(PointType.GRADE, result.get(3).getPointType());

		maker = new TypeIntervalArrayGenerator(originalGradePoints, mobileBaseSize, thresholdSlope, equalAreaSquaresStrategy, MIN_LENGTH);
		result = maker.getResultTypeIntervalArray();
		System.out.println(result);
		Assert.assertEquals(4, result.size());
		Assert.assertEquals(PointType.GRADE, result.get(0).getPointType());
		Assert.assertEquals(PointType.VERTICAL_CURVE, result.get(1).getPointType());
		Assert.assertEquals(PointType.VERTICAL_CURVE, result.get(2).getPointType());
		Assert.assertEquals(PointType.GRADE, result.get(3).getPointType());

	}	
	private VerticalProfile getSampleProfileTypeV_2() {
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
	public void testSagCurveCrestCurve() {
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
		TypeIntervalArrayGenerator maker = new TypeIntervalArrayGenerator(gradesample, mobileBaseSize, thresholdSlope, lessSquaresStrategy, MIN_LENGTH);
		TypeIntervalArray segments = maker.getResultTypeIntervalArray();
		
		//System.out.println(segments);
		
		
	}

}
