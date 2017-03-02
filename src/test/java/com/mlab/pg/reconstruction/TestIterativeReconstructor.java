package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestIterativeReconstructor {
	private final static Logger LOG = Logger.getLogger(TestIterativeReconstructor.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}

	@Test
	public void test() {
		LOG.debug("test()");
		double maxlength = 50.0;
		double sepmedia = 10.0;
		System.out.println(maxlength +" - " + sepmedia + " - " + (int)Math.rint(maxlength/sepmedia));

		maxlength = 50.0;
		sepmedia = 11.0;
		System.out.println(maxlength +" - " + sepmedia + " - " + (int)Math.rint(maxlength/sepmedia));

		maxlength = 50.0;
		sepmedia = 7.0;
		System.out.println(maxlength +" - " + sepmedia + " - " + (int)Math.rint(maxlength/sepmedia));

	}

}
