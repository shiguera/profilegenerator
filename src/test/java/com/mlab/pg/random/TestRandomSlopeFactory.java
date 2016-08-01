package com.mlab.pg.random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestRandomSlopeFactory {

	
	static Logger LOG = Logger.getLogger(TestRandomSlopeFactory.class);
	
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
			double slope = RandomSlopeFactory.gaussianSlope(mean, sd);
			//System.out.println(i + " " + slope);
			if(slope > mean + 3*sd || slope < mean - 3*sd) {
				System.out.println(i + " " + slope);	
			}			
		}
	}
}
