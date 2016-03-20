package com.mlab.pg.random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;



public class TestRandomFactory {

static Logger LOG = Logger.getLogger(TestRandomFactory.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
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
