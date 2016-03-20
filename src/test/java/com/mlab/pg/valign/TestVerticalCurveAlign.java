package com.mlab.pg.valign;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.mlab.pg.xyfunction.Parabole;

import junit.framework.Assert;

public class TestVerticalCurveAlign {

	private final Logger LOG = Logger.getLogger(TestVerticalCurveAlign.class);

	@Test
	public void test() {
		LOG.debug("test()");
		
		Parabole parabole = new Parabole(0.0, 0.04, 0.0001);
		double startx = 1200.0;
		double endx = 1800.0;
		VerticalCurveAlign p1 = new VerticalCurveAlign(parabole, startx, endx);
		Assert.assertNotNull(p1);
		Assert.assertNotNull(p1.polynom);
		Assert.assertTrue(p1.polynom.getClass().isAssignableFrom(Parabole.class));
		Assert.assertEquals(1200.0, p1.getStartS(), 0.0001);
		Assert.assertEquals(192.0, p1.getStartZ(), 0.0001);
		Assert.assertEquals(1800.0, p1.getEndS(), 0.0001);
		Assert.assertEquals(396.0, p1.getEndZ(), 0.0001);
		Assert.assertEquals(0.28, p1.getStartTangent(), 0.0001);
		Assert.assertEquals(0.40, p1.getEndTangent(), 0.0001);
		Assert.assertEquals(600.0, p1.getLength(), 0.0001);
		

	}

}
