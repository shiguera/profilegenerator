package com.mlab.pg.xyfunction;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;

public class TestParabole {

	private final Logger LOG = Logger.getLogger(TestParabole.class);

	@Test
	public void test() {
		LOG.debug("TestParabole.test()");
		
		Parabole r = new Parabole(1,1,1);
		Assert.assertNotNull(r);
		Assert.assertEquals(1.0, r.getA0());
		Assert.assertEquals(1.0, r.getA1());
		Assert.assertEquals(1.0, r.getA2());
		Assert.assertEquals(3.0, r.getY(1.0));
		Assert.assertEquals(3.0, r.getTangent(1.0));
		Assert.assertEquals(0.0632, r.getCurvature(1.0), 0.0001);
		Assert.assertEquals(0.5, r.getKv());

		Assert.assertTrue(r.isConcave());
		Assert.assertFalse(r.isConvex());
		
		Parabole p1 = new Parabole(1.0,1.0,0.0);
		Assert.assertEquals(true, Double.isNaN(p1.getKv()));
		Assert.assertFalse(p1.isConcave());
		Assert.assertFalse(p1.isConvex());
		
	}

}
