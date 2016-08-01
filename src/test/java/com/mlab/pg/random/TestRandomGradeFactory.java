package com.mlab.pg.random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.norma.GradeLimits;
import com.mlab.pg.valign.GradeAlignment;

public class TestRandomGradeFactory {

	
	static Logger LOG = Logger.getLogger(TestRandomGradeFactory.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	
	@Test
	public void testGaussianSlope() {
		LOG.debug("testGaussianSlope()");
		double mean = 0.03;
		double sd = 0.02;
		for(int i=0; i<1000; i++) {
			double slope = RandomGradeFactory.randomGaussianGradeSlope(mean, sd);
			//System.out.println(i + " " + slope);
			if(slope > mean + 3*sd || slope < mean - 3*sd) {
				System.out.println(i + " " + slope);	
			}			
		}
	}
	
	@Test
	public void testRandomGaussianGradeLength() {
		LOG.debug("testRandomGaussianGradeLength()");
		double mean = 500.0;
		double sd = 200.0;
		double min = 50.0;
		double max = 1500.0;
		double sum = 0;
		int numensayos = 1000;
		int countoutsiders = 0;
		for(int i=0; i<numensayos; i++) {
			double length = RandomGradeFactory.randomGaussianGradeLength(mean, sd, min, max);
			if(length > (mean + 3.0*sd) || length < (mean - 3.0*sd)) {
				countoutsiders ++;
				System.out.println(length);
			}
			sum += length;
		}
		double resultmean = sum/numensayos;
		Assert.assertTrue(resultmean<(mean+sd));
		System.out.println("Media = " + resultmean);
		System.out.println("Outsiders = " + countoutsiders);
	}
	@Test
	public void testRandomGradeAlign() {
		LOG.debug("testRandomGradeAlign()");
		DesignSpeed ds = DesignSpeed.DS120;
		double s0 = 0.0;
		double z0 = 0.0;
		GradeAlignment ga = RandomGradeFactory.randomGradeAlignment(ds, s0, z0);
		Assert.assertNotNull(ga);
		//System.out.println(ga);

		ds = DesignSpeed.DS100;
		s0 = 0.0;
		z0 = 0.0;
		ga = RandomGradeFactory.randomGradeAlignment(ds, s0, z0);
		Assert.assertNotNull(ga);
		//System.out.println(ga);

		ds = DesignSpeed.DS80;
		s0 = 0.0;
		z0 = 0.0;
		ga = RandomGradeFactory.randomGradeAlignment(ds, s0, z0);
		Assert.assertNotNull(ga);
		//System.out.println(ga);

		ds = DesignSpeed.DS60;
		s0 = 0.0;
		z0 = 0.0;
		ga = RandomGradeFactory.randomGradeAlignment(ds, s0, z0);
		Assert.assertNotNull(ga);
		//System.out.println(ga);

		ds = DesignSpeed.DS40;
		s0 = 0.0;
		z0 = 0.0;
		ga = RandomGradeFactory.randomGradeAlignment(ds, s0, z0);
		Assert.assertNotNull(ga);
		//System.out.println(ga);

	}	
	@Test
	public void testRandomUpGradeAlign() {
		LOG.debug("testRandomUpGradeAlign()");
		DesignSpeed ds = DesignSpeed.DS120;
		double s0 = 0.0;
		double z0 = 0.0;
		GradeAlignment ga = RandomGradeFactory.randomUpGradeAlignment(ds, s0, z0);
		Assert.assertNotNull(ga);
		Assert.assertTrue(ga.getStartTangent()>0);
		//System.out.println(ga);

		ds = DesignSpeed.DS100;
		s0 = 0.0;
		z0 = 0.0;
		ga = RandomGradeFactory.randomUpGradeAlignment(ds, s0, z0);
		Assert.assertNotNull(ga);
		Assert.assertTrue(ga.getStartTangent()>0);
		//System.out.println(ga);

		ds = DesignSpeed.DS80;
		s0 = 0.0;
		z0 = 0.0;
		ga = RandomGradeFactory.randomUpGradeAlignment(ds, s0, z0);
		Assert.assertNotNull(ga);
		Assert.assertTrue(ga.getStartTangent()>0);
		//System.out.println(ga);

		ds = DesignSpeed.DS60;
		s0 = 0.0;
		z0 = 0.0;
		ga = RandomGradeFactory.randomUpGradeAlignment(ds, s0, z0);
		Assert.assertNotNull(ga);
		Assert.assertTrue(ga.getStartTangent()>0);
		//System.out.println(ga);

		ds = DesignSpeed.DS40;
		s0 = 0.0;
		z0 = 0.0;
		ga = RandomGradeFactory.randomUpGradeAlignment(ds, s0, z0);
		Assert.assertNotNull(ga);
		Assert.assertTrue(ga.getStartTangent()>0);
		//System.out.println(ga);

	}	
	@Test
	public void testRandomDownGradeAlign() {
		LOG.debug("testRandomDownGradeAlign()");
		DesignSpeed ds = DesignSpeed.DS120;
		double s0 = 0.0;
		double z0 = 0.0;
		GradeAlignment ga = RandomGradeFactory.randomDownGradeAlignment(ds, s0, z0);
		Assert.assertNotNull(ga);
		Assert.assertTrue(ga.getStartTangent()<0);
		//System.out.println(ga);

		ds = DesignSpeed.DS100;
		s0 = 0.0;
		z0 = 0.0;
		ga = RandomGradeFactory.randomDownGradeAlignment(ds, s0, z0);
		Assert.assertNotNull(ga);
		Assert.assertTrue(ga.getStartTangent()<0);
		//System.out.println(ga);

		ds = DesignSpeed.DS80;
		s0 = 0.0;
		z0 = 0.0;
		ga = RandomGradeFactory.randomDownGradeAlignment(ds, s0, z0);
		Assert.assertNotNull(ga);
		Assert.assertTrue(ga.getStartTangent()<0);
		//System.out.println(ga);

		ds = DesignSpeed.DS60;
		s0 = 0.0;
		z0 = 0.0;
		ga = RandomGradeFactory.randomDownGradeAlignment(ds, s0, z0);
		Assert.assertNotNull(ga);
		Assert.assertTrue(ga.getStartTangent()<0);
		//System.out.println(ga);

		ds = DesignSpeed.DS40;
		s0 = 0.0;
		z0 = 0.0;
		ga = RandomGradeFactory.randomDownGradeAlignment(ds, s0, z0);
		Assert.assertNotNull(ga);
		Assert.assertTrue(ga.getStartTangent()<0);
		//System.out.println(ga);

	}		
	@Test
	public void testRandomGradeSlope() {
		LOG.debug("testRandomGradeSlope()");
		for(int i=0; i<100;i++) {
			DesignSpeed dspeed = DesignSpeed.DS120;
			GradeLimits limits = new GradeLimits(dspeed);
			double slope = RandomGradeFactory.randomUniformGradeSlope(dspeed);
			Assert.assertTrue(Math.abs(slope) >= limits.getMinSlope());
			Assert.assertTrue(Math.abs(slope) <= limits.getMaxSlope());
			//System.out.println(slope);
			
			dspeed = DesignSpeed.DS100;
			limits = new GradeLimits(dspeed);
			slope = RandomGradeFactory.randomUniformGradeSlope(dspeed);
			Assert.assertTrue(Math.abs(slope) >= limits.getMinSlope());
			Assert.assertTrue(Math.abs(slope) <= limits.getMaxSlope());

			dspeed = DesignSpeed.DS80;
			limits = new GradeLimits(dspeed);
			slope = RandomGradeFactory.randomUniformGradeSlope(dspeed);
			Assert.assertTrue(Math.abs(slope) >= limits.getMinSlope());
			Assert.assertTrue(Math.abs(slope) <= limits.getMaxSlope());

			dspeed = DesignSpeed.DS60;
			limits = new GradeLimits(dspeed);
			slope = RandomGradeFactory.randomUniformGradeSlope(dspeed);
			Assert.assertTrue(Math.abs(slope) >= limits.getMinSlope());
			Assert.assertTrue(Math.abs(slope) <= limits.getMaxSlope());

			dspeed = DesignSpeed.DS40;
			limits = new GradeLimits(dspeed);
			slope = RandomGradeFactory.randomUniformGradeSlope(dspeed);
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
			double length = RandomGradeFactory.randomUniformGradeLength(dspeed);
			//System.out.println(limits.getMaxLength() + " " + limits.getMinLength() + " " + length);
			Assert.assertTrue(length >= limits.getMinLength());
			Assert.assertTrue(length <= limits.getMaxLength());
			//System.out.println(length);

			dspeed = DesignSpeed.DS100;
			limits = new GradeLimits(dspeed);
			length = RandomGradeFactory.randomUniformGradeLength(dspeed);
			//System.out.println(limits.getMaxLength() + " " + limits.getMinLength() + " " + length);
			Assert.assertTrue(length >= limits.getMinLength());
			Assert.assertTrue(length <= limits.getMaxLength());
			//System.out.println(length);

			dspeed = DesignSpeed.DS80;
			limits = new GradeLimits(dspeed);
			length = RandomGradeFactory.randomUniformGradeLength(dspeed);
			//System.out.println(limits.getMaxLength() + " " + limits.getMinLength() + " " + length);
			Assert.assertTrue(length >= limits.getMinLength());
			Assert.assertTrue(length <= limits.getMaxLength());
			//System.out.println(length);

			dspeed = DesignSpeed.DS60;
			limits = new GradeLimits(dspeed);
			length = RandomGradeFactory.randomUniformGradeLength(dspeed);
			//System.out.println(limits.getMaxLength() + " " + limits.getMinLength() + " " + length);
			Assert.assertTrue(length >= limits.getMinLength());
			Assert.assertTrue(length <= limits.getMaxLength());
			//System.out.println(length);

			dspeed = DesignSpeed.DS40;
			limits = new GradeLimits(dspeed);
			length = RandomGradeFactory.randomUniformGradeLength(dspeed);
			//System.out.println(limits.getMaxLength() + " " + limits.getMinLength() + " " + length);
			Assert.assertTrue(length >= limits.getMinLength());
			Assert.assertTrue(length <= limits.getMaxLength());
			//System.out.println(length);

		}
	}

}
