package com.mlab.pg.xyfunction;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

public class TestStraight {

	private final static Logger LOG = Logger.getLogger(TestStraight.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}


	@Test
	public void test() {
		LOG.debug("TestStraight.test()");
		Straight r = new Straight(1,1);
		Assert.assertNotNull(r);
		Assert.assertEquals(1.0, r.getA0());
		Assert.assertEquals(1.0, r.getA1());
		Assert.assertEquals(0.0, r.getA2());
		Assert.assertEquals(2.0, r.getY(1.0));
		Assert.assertEquals(1.0, r.getTangent(1.0));
		Assert.assertEquals(0.0, r.getCurvature(1.0));
		
	}

}
