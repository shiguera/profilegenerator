package com.mlab.pg.random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.norma.GradeLimits;
import com.mlab.pg.valign.GradeAlign;



public class TestRandomFactory {

static Logger LOG = Logger.getLogger(TestRandomFactory.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
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
