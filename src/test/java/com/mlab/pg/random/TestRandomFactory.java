package com.mlab.pg.random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.norma.CrestCurveLimits;
import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.norma.GradeLimits;
import com.mlab.pg.norma.SagCurveLimits;
import com.mlab.pg.norma.VerticalCurveLimits;
import com.mlab.pg.valign.GradeAlign;
import com.mlab.pg.valign.VerticalCurveAlign;
import com.mlab.pg.valign.VerticalProfile;



public class TestRandomFactory {

static Logger LOG = Logger.getLogger(TestRandomFactory.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	// VerticalProfiles
	@Test
	public void testRandomVerticalProfileType_I() {
		LOG.debug("testRandomVerrticalProfileType_I");
		DesignSpeed dspeed = DesignSpeed.DS120;
		double s0 = 0.0;
		double z0 = 1000.0;
		int verticalCurvesCount = 3;
		VerticalProfile vp = RandomFactory.randomVerticalProfileType_I(dspeed, s0, z0, verticalCurvesCount);
		//Assert.assertNotNull(vp);
	}
	@Test
	public void testRandomVerticalCurveDS120() {
		LOG.debug("testRandomVerticalCurveDS120()");
		DesignSpeed dspeed = DesignSpeed.DS120;
		double s0= 0.0;
		double z0= 1000.0;
		GradeAlign grade1 = RandomFactory.randomGradeAlign(dspeed, s0, z0);
		double g1 = grade1.getSlope();
		double g2 = Math.abs(RandomFactory.randomGradeSlope(dspeed));
		if(g1>0) {
			g2 = - g2;
		} 
		VerticalCurveAlign vc = RandomFactory.randomVerticalCurve(dspeed, grade1, g2);
		Assert.assertNotNull(vc);
		Assert.assertEquals(grade1.getEndS(), vc.getStartS(), 0.001);
		Assert.assertEquals(grade1.getEndZ(), vc.getStartZ(), 0.001);
		Assert.assertEquals(g1, vc.getStartTangent(), 0.001);
		Assert.assertEquals(g2, vc.getEndTangent(), 0.001);
		VerticalCurveLimits limits = null;
		if(vc.getKv()>0) {
			limits = new SagCurveLimits(dspeed);
		} else {
			limits = new CrestCurveLimits(dspeed);
		}
		//System.out.println(vc.getKv());
		Assert.assertTrue(Math.abs(vc.getKv()) > limits.getMinKv());
		Assert.assertTrue(Math.abs(vc.getKv()) < limits.getMaxKv());
		Assert.assertTrue(vc.getLength() > limits.getMinLength());
		Assert.assertTrue(vc.getLength() < limits.getMaxLength());
		//System.out.println(vc);
	}
	@Test
	public void testRandomVerticalCurveDS100() {
		LOG.debug("testRandomVerticalCurveDS100()");
		DesignSpeed dspeed = DesignSpeed.DS100;
		double s0= 0.0;
		double z0= 1000.0;
		GradeAlign grade1 = RandomFactory.randomGradeAlign(dspeed, s0, z0);
		double g1 = grade1.getSlope();
		double g2 = Math.abs(RandomFactory.randomGradeSlope(dspeed));
		if(g1>0) {
			g2 = - g2;
		} 
		VerticalCurveAlign vc = RandomFactory.randomVerticalCurve(dspeed, grade1, g2);
		Assert.assertNotNull(vc);
		Assert.assertEquals(grade1.getEndS(), vc.getStartS(), 0.001);
		Assert.assertEquals(grade1.getEndZ(), vc.getStartZ(), 0.001);
		Assert.assertEquals(g1, vc.getStartTangent(), 0.001);
		Assert.assertEquals(g2, vc.getEndTangent(), 0.001);
		VerticalCurveLimits limits = null;
		if(vc.getKv()>0) {
			limits = new SagCurveLimits(dspeed);
		} else {
			limits = new CrestCurveLimits(dspeed);
		}
		//System.out.println(vc.getKv());
		Assert.assertTrue(Math.abs(vc.getKv()) > limits.getMinKv());
		Assert.assertTrue(Math.abs(vc.getKv()) < limits.getMaxKv());
		Assert.assertTrue(vc.getLength() > limits.getMinLength());
		Assert.assertTrue(vc.getLength() < limits.getMaxLength());
		//System.out.println(vc);
	}
	@Test
	public void testRandomVerticalCurveDS80() {
		LOG.debug("testRandomVerticalCurveDS80()");
		DesignSpeed dspeed = DesignSpeed.DS80;
		double s0= 0.0;
		double z0= 1000.0;
		GradeAlign grade1 = RandomFactory.randomGradeAlign(dspeed, s0, z0);
		double g1 = grade1.getSlope();
		double g2 = Math.abs(RandomFactory.randomGradeSlope(dspeed));
		if(g1>0) {
			g2 = - g2;
		} 
		VerticalCurveAlign vc = RandomFactory.randomVerticalCurve(dspeed, grade1, g2);
		Assert.assertNotNull(vc);
		Assert.assertEquals(grade1.getEndS(), vc.getStartS(), 0.001);
		Assert.assertEquals(grade1.getEndZ(), vc.getStartZ(), 0.001);
		Assert.assertEquals(g1, vc.getStartTangent(), 0.001);
		Assert.assertEquals(g2, vc.getEndTangent(), 0.001);
		VerticalCurveLimits limits = null;
		if(vc.getKv()>0) {
			limits = new SagCurveLimits(dspeed);
		} else {
			limits = new CrestCurveLimits(dspeed);
		}
		//System.out.println(vc.getKv());
		Assert.assertTrue(Math.abs(vc.getKv()) > limits.getMinKv());
		Assert.assertTrue(Math.abs(vc.getKv()) < limits.getMaxKv());
		Assert.assertTrue(vc.getLength() > limits.getMinLength());
		Assert.assertTrue(vc.getLength() < limits.getMaxLength());
		//System.out.println(vc);
	}
	@Test
	public void testRandomVerticalCurveDS60() {
		LOG.debug("testRandomVerticalCurveDS60()");
		DesignSpeed dspeed = DesignSpeed.DS60;
		double s0= 0.0;
		double z0= 1000.0;
		GradeAlign grade1 = RandomFactory.randomGradeAlign(dspeed, s0, z0);
		double g1 = grade1.getSlope();
		double g2 = Math.abs(RandomFactory.randomGradeSlope(dspeed));
		if(g1>0) {
			g2 = - g2;
		} 
		VerticalCurveAlign vc = RandomFactory.randomVerticalCurve(dspeed, grade1, g2);
		Assert.assertNotNull(vc);
		Assert.assertEquals(grade1.getEndS(), vc.getStartS(), 0.001);
		Assert.assertEquals(grade1.getEndZ(), vc.getStartZ(), 0.001);
		Assert.assertEquals(g1, vc.getStartTangent(), 0.001);
		Assert.assertEquals(g2, vc.getEndTangent(), 0.001);
		VerticalCurveLimits limits = null;
		if(vc.getKv()>0) {
			limits = new SagCurveLimits(dspeed);
		} else {
			limits = new CrestCurveLimits(dspeed);
		}
		//System.out.println(vc.getKv());
		Assert.assertTrue(Math.abs(vc.getKv()) > limits.getMinKv());
		Assert.assertTrue(Math.abs(vc.getKv()) < limits.getMaxKv());
		Assert.assertTrue(vc.getLength() > limits.getMinLength());
		Assert.assertTrue(vc.getLength() < limits.getMaxLength());
		//System.out.println(vc);
	}
	@Test
	public void testRandomVerticalCurveDS40() {
		LOG.debug("testRandomVerticalCurveDS40()");
		DesignSpeed dspeed = DesignSpeed.DS40;
		double s0= 0.0;
		double z0= 1000.0;
		GradeAlign grade1 = RandomFactory.randomGradeAlign(dspeed, s0, z0);
		double g1 = grade1.getSlope();
		double g2 = Math.abs(RandomFactory.randomGradeSlope(dspeed));
		if(g1>0) {
			g2 = - g2;
		} 
		VerticalCurveAlign vc = RandomFactory.randomVerticalCurve(dspeed, grade1, g2);
		Assert.assertNotNull(vc);
		Assert.assertEquals(grade1.getEndS(), vc.getStartS(), 0.001);
		Assert.assertEquals(grade1.getEndZ(), vc.getStartZ(), 0.001);
		Assert.assertEquals(g1, vc.getStartTangent(), 0.001);
		Assert.assertEquals(g2, vc.getEndTangent(), 0.001);
		VerticalCurveLimits limits = null;
		if(vc.getKv()>0) {
			limits = new SagCurveLimits(dspeed);
		} else {
			limits = new CrestCurveLimits(dspeed);
		}
		//System.out.println(vc.getKv());
		Assert.assertTrue(Math.abs(vc.getKv()) > limits.getMinKv());
		Assert.assertTrue(Math.abs(vc.getKv()) < limits.getMaxKv());
		Assert.assertTrue(vc.getLength() > limits.getMinLength());
		Assert.assertTrue(vc.getLength() < limits.getMaxLength());
		//System.out.println(vc);
	}

	// Vertical Curves
	@Test
	public void testRandomSagCurveKv() {
		LOG.debug("testRandomSagCurveKv()");
		for(int i=0; i<100; i++) {
			DesignSpeed dspeed = DesignSpeed.DS120;
			VerticalCurveLimits limits = new SagCurveLimits(dspeed);
			double kv = RandomFactory.randomSagCurveKv(dspeed);
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());
			
			dspeed = DesignSpeed.DS100;
			limits = new SagCurveLimits(dspeed);
			kv = RandomFactory.randomSagCurveKv(dspeed);
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());

			dspeed = DesignSpeed.DS80;
			limits = new SagCurveLimits(dspeed);
			kv = RandomFactory.randomSagCurveKv(dspeed);
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());
			
			dspeed = DesignSpeed.DS60;
			limits = new SagCurveLimits(dspeed);
			kv = RandomFactory.randomSagCurveKv(dspeed);
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());
			
			dspeed = DesignSpeed.DS40;
			limits = new SagCurveLimits(dspeed);
			kv = RandomFactory.randomSagCurveKv(dspeed);
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());

			
		}
	}
	@Test
	public void testRandomCrestCurveKv() {
		LOG.debug("testRandomCrestCurveKv()");
		for(int i=0; i<100; i++) {
			DesignSpeed dspeed = DesignSpeed.DS120;
			VerticalCurveLimits limits = new CrestCurveLimits(dspeed);
			double kv = Math.abs(RandomFactory.randomCrestCurveKv(dspeed));
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());
			
			dspeed = DesignSpeed.DS100;
			limits = new CrestCurveLimits(dspeed);
			kv = Math.abs(RandomFactory.randomCrestCurveKv(dspeed));
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());

			dspeed = DesignSpeed.DS80;
			limits = new CrestCurveLimits(dspeed);
			kv = Math.abs(RandomFactory.randomCrestCurveKv(dspeed));
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());
			
			dspeed = DesignSpeed.DS60;
			limits = new CrestCurveLimits(dspeed);
			kv = Math.abs(RandomFactory.randomCrestCurveKv(dspeed));
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());
			
			dspeed = DesignSpeed.DS40;
			limits = new CrestCurveLimits(dspeed);
			kv = Math.abs(RandomFactory.randomCrestCurveKv(dspeed));
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());

		}
	}
	// Grades
	@Test
	public void testRandomGradeAlign() {
		LOG.debug("testRandomGradeAlign()");
		DesignSpeed ds = DesignSpeed.DS120;
		double s0 = 0.0;
		double z0 = 0.0;
		GradeAlign ga = RandomFactory.randomGradeAlign(ds, s0, z0);
		Assert.assertNotNull(ga);
		//System.out.println(ga);

		ds = DesignSpeed.DS100;
		s0 = 0.0;
		z0 = 0.0;
		ga = RandomFactory.randomGradeAlign(ds, s0, z0);
		Assert.assertNotNull(ga);
		//System.out.println(ga);

		ds = DesignSpeed.DS80;
		s0 = 0.0;
		z0 = 0.0;
		ga = RandomFactory.randomGradeAlign(ds, s0, z0);
		Assert.assertNotNull(ga);
		//System.out.println(ga);

		ds = DesignSpeed.DS60;
		s0 = 0.0;
		z0 = 0.0;
		ga = RandomFactory.randomGradeAlign(ds, s0, z0);
		Assert.assertNotNull(ga);
		//System.out.println(ga);

		ds = DesignSpeed.DS40;
		s0 = 0.0;
		z0 = 0.0;
		ga = RandomFactory.randomGradeAlign(ds, s0, z0);
		Assert.assertNotNull(ga);
		//System.out.println(ga);

	}	
	
	@Test
	public void testRandomGradeSlope() {
		LOG.debug("testRandomGradeSlope()");
		for(int i=0; i<100;i++) {
			DesignSpeed dspeed = DesignSpeed.DS120;
			GradeLimits limits = new GradeLimits(dspeed);
			double slope = RandomFactory.randomGradeSlope(dspeed);
			Assert.assertTrue(Math.abs(slope) >= limits.getMinSlope());
			Assert.assertTrue(Math.abs(slope) <= limits.getMaxSlope());
			//System.out.println(slope);
			
			dspeed = DesignSpeed.DS100;
			limits = new GradeLimits(dspeed);
			slope = RandomFactory.randomGradeSlope(dspeed);
			Assert.assertTrue(Math.abs(slope) >= limits.getMinSlope());
			Assert.assertTrue(Math.abs(slope) <= limits.getMaxSlope());

			dspeed = DesignSpeed.DS80;
			limits = new GradeLimits(dspeed);
			slope = RandomFactory.randomGradeSlope(dspeed);
			Assert.assertTrue(Math.abs(slope) >= limits.getMinSlope());
			Assert.assertTrue(Math.abs(slope) <= limits.getMaxSlope());

			dspeed = DesignSpeed.DS60;
			limits = new GradeLimits(dspeed);
			slope = RandomFactory.randomGradeSlope(dspeed);
			Assert.assertTrue(Math.abs(slope) >= limits.getMinSlope());
			Assert.assertTrue(Math.abs(slope) <= limits.getMaxSlope());

			dspeed = DesignSpeed.DS40;
			limits = new GradeLimits(dspeed);
			slope = RandomFactory.randomGradeSlope(dspeed);
			Assert.assertTrue(Math.abs(slope) >= limits.getMinSlope());
			Assert.assertTrue(Math.abs(slope) <= limits.getMaxSlope());

		}		
	}
	@Test
	public void testRandomGradeLength() {
		LOG.debug("testRandomGradeLength()");
		for(int i=0; i<100; i++) {
			DesignSpeed dspeed = DesignSpeed.DS120;
			GradeLimits limits = new GradeLimits(dspeed);
			double length = RandomFactory.randomGradeLength(dspeed);
			Assert.assertTrue(length >= limits.getMinLength());
			Assert.assertTrue(length <= limits.getMaxLength());
			//System.out.println(length);

			dspeed = DesignSpeed.DS100;
			limits = new GradeLimits(dspeed);
			length = RandomFactory.randomGradeLength(dspeed);
			Assert.assertTrue(length >= limits.getMinLength());
			Assert.assertTrue(length <= limits.getMaxLength());
			//System.out.println(length);

			dspeed = DesignSpeed.DS80;
			limits = new GradeLimits(dspeed);
			length = RandomFactory.randomGradeLength(dspeed);
			Assert.assertTrue(length >= limits.getMinLength());
			Assert.assertTrue(length <= limits.getMaxLength());
			//System.out.println(length);

			dspeed = DesignSpeed.DS60;
			limits = new GradeLimits(dspeed);
			length = RandomFactory.randomGradeLength(dspeed);
			Assert.assertTrue(length >= limits.getMinLength());
			Assert.assertTrue(length <= limits.getMaxLength());
			//System.out.println(length);

			dspeed = DesignSpeed.DS40;
			limits = new GradeLimits(dspeed);
			length = RandomFactory.randomGradeLength(dspeed);
			Assert.assertTrue(length >= limits.getMinLength());
			Assert.assertTrue(length <= limits.getMaxLength());
			//System.out.println(length);

		}
	}
	@Test
	public void testRandomSign() {
		LOG.debug("testRandomSign()");
		int countpositives = 0;
		int countnegatives = 0;
		for (int i=0; i<100;i++) {
			double sign = RandomFactory.randomSign();
			Assert.assertTrue(sign==1.0 || sign==-1.0);
			if(sign==1.0) {
				countpositives++;
			} else if (sign==-1.0) {
				countnegatives++;
			} else {
				Assert.fail();
			}
		}
		//System.out.println(countpositives + ", " + countnegatives);	
	}
	@Test
	public void testRandomDoubleByIncrements() {
		LOG.debug("testRandomDoubleByIncrements()");
		double min=0.5;
		double max = 12;
		double steep = 0.5;
		for (int i=0;i<200;i++) {
			double number = RandomFactory.randomDoubleByIncrements(min, max, steep);
			//System.out.print(number + " ");
			Assert.assertTrue(number>=min && number<=max);
		}
		//System.out.println();
	}
	@Test
	public void testRandomDoubleByIncrementsWithNegativeNumbers() {
		LOG.debug("testRandomDoubleByIncrementsWithNegativeNumbers()");
		double min = -12;
		double max = 12;
		double steep = 0.5;
		for (int i=0;i<200;i++) {
			double number = RandomFactory.randomDoubleByIncrements(min, max, steep);
			//System.out.print(number + " ");
			Assert.assertTrue(number>=min && number<=max);
		}
		//System.out.println();
	}
}
