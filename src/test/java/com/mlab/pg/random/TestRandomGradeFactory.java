package com.mlab.pg.random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestRandomGradeFactory {

	
	static Logger LOG = Logger.getLogger(TestRandomGradeFactory.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	
	@Test
	public void testGenerateThreeOrderedSlopes() {
		LOG.debug("testGenerateThreeOrderedSlopes()");
		RandomProfileType_I_Factory factory = new RandomProfileType_I_Factory();
		for(int i=0; i<100; i++) {
			double[] slopes = RandomGradeFactory.generateThreeOrderedSlopes(factory.getMinSlope(), factory.getMaxSlope(), factory.getSlopeIncrement());
			Assert.assertTrue(slopes[0] < slopes[1]);
			Assert.assertTrue(slopes[1] < slopes[2]);
			Assert.assertTrue(slopes[0] >= factory.getMinSlope());
			Assert.assertTrue(slopes[2] <= factory.getMaxSlope());			
		}
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
				//System.out.println(i + " " + slope);	
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
				//System.out.println(length);
			}
			sum += length;
		}
		double resultmean = sum/numensayos;
		Assert.assertTrue(resultmean<(mean+sd));
		//System.out.println("Media = " + resultmean);
		//System.out.println("Outsiders = " + countoutsiders);
	}
	
}
