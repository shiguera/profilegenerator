package com.mlab.pg.xyfunction;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

public class TestParabole {

	private final static Logger LOG = Logger.getLogger(TestParabole.class);

	@BeforeClass
	public static void befor() {
		PropertyConfigurator.configure("log4j.properties");
	}
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
	@Test
	public void testParaboleFromStartPointStartTangentAndEndPoint() {
		LOG.debug("testParaboleFromStartPointStartTangentAndEndPoint()");
		// Parabole from startPoint, startTangent and endPoint
		double x0 = 0.0;
		double y0 = 0.0;
		double t0 = 0.0;
		double xf = 1.0;
		double yf = 1.0;
		Parabole parabole = new Parabole(x0, y0, t0, xf, yf);
		Assert.assertNotNull(parabole);
		Assert.assertEquals(0.0, parabole.getA0());
		Assert.assertEquals(0.0, parabole.getA1());
		Assert.assertEquals(1.0, parabole.getA2());
		Assert.assertEquals(4.0, parabole.getY(2.0));
		Assert.assertEquals(4.0, parabole.getTangent(2.0));		
		Assert.assertEquals(2.0, parabole.getTangent(1.0));
		
		x0 = 0.0;
		y0 = 0.0;
		t0 = 0.0;
		xf = 1.0;
		yf = -1.0;
		parabole = new Parabole(x0, y0, t0, xf, yf);
		Assert.assertNotNull(parabole);
		Assert.assertEquals(0.0, parabole.getA0());
		Assert.assertEquals(0.0, parabole.getA1());
		Assert.assertEquals(-1.0, parabole.getA2());
		Assert.assertEquals(-256.0, parabole.getY(-16.0));
		Assert.assertEquals(-4.0, parabole.getTangent(2.0));		
		Assert.assertEquals(-2.0, parabole.getTangent(1.0));
		
	}

	@Test
	public void testParaboleFromStartPointStartTangentAndKv() {
		LOG.debug("testParaboleFromStartPointStartTangentAndKv()");
		double s0 = 0.0;
		double z0 = 0.0;
		double g0 = 0.5;
		double kv = 5000;
		Parabole parab = new Parabole(s0, z0, g0, kv);	
		Assert.assertEquals(0.0, parab.getA0());
		Assert.assertEquals(0.5, parab.getA1());
		Assert.assertEquals(0.0001, parab.getA2());

		s0 = 1000.0;
		z0 = 100.0;
		g0 = 0.03;
		kv = 10000;
		parab = new Parabole(s0, z0, g0, kv);
		Assert.assertEquals(120.0, parab.getA0());
		Assert.assertEquals(-0.07, parab.getA1());
		Assert.assertEquals(0.00005, parab.getA2());

		s0 = 1000.0;
		z0 = 100.0;
		g0 = -0.04;
		kv = -5000;
		parab = new Parabole(s0, z0, g0, kv);
		Assert.assertEquals(40.0, parab.getA0());
		Assert.assertEquals(0.16, parab.getA1());
		Assert.assertEquals(-0.0001, parab.getA2());

	}


	@Test
	public void testGetSForSlope() {
		LOG.debug("testGetSForSlope()");
		Parabole p = new Parabole(0.0, 0.0, 1.0);
		Assert.assertEquals(0.0, p.getSForSlope(0.0));
		Assert.assertEquals(1.0, p.getSForSlope(2.0));
		Assert.assertEquals(-1.0, p.getSForSlope(-2.0));

		p = new Parabole(-3.0, -2.0, 1.5);
		Assert.assertEquals(1.0, p.getSForSlope(1.0));
		Assert.assertEquals(0.0, p.getSForSlope(-2.0));
		Assert.assertEquals(-1.0, p.getSForSlope(-5.0));

	}
}
