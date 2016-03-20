package com.mlab.pg.xyfunction;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;

public class TestPolinom2 {

	private final Logger LOG = Logger.getLogger(TestPolinom2.class);

	@Test
	public void testConstructorAndGettersSetters() {
		LOG.debug("TestPolinom2.Test()");
		
		Polynom2 p = new Polynom2(0,0,0);
		Assert.assertNotNull(p);
		Assert.assertEquals(0.0, p.getY(100.0));
		Assert.assertEquals(0.0, p.getTangent(100.0));
		Assert.assertEquals(0.0, p.getCurvature(100.0));

		p.setA0(1.0);
		Assert.assertEquals(1.0, p.getY(100.0));
		Assert.assertEquals(0.0, p.getTangent(100.0));
		Assert.assertEquals(0.0, p.getCurvature(100.0));
		
		p.setA1(1.0);
		Assert.assertEquals(101.0, p.getY(100.0));
		Assert.assertEquals(1.0, p.getTangent(100.0));
		Assert.assertEquals(0.0, p.getCurvature(100.0));

		p.setA2(1.0);		
		Assert.assertEquals(10101.0, p.getY(100.0));
		Assert.assertEquals(201.0, p.getTangent(100.0));
		Assert.assertEquals(2.46e-7, p.getCurvature(100.0), 0.01e-7);
	}

	
}
