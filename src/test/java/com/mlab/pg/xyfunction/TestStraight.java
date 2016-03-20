package com.mlab.pg.xyfunction;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;

public class TestStraight {

	private final Logger LOG = Logger.getLogger(TestStraight.class);

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
