package com.mlab.pg.valign;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.random.RandomFactory;
import com.mlab.pg.random.RandomGradeFactory;
import com.mlab.pg.xyfunction.Parabole;

import junit.framework.Assert;

public class TestVerticalCurveAlignment {

	private final static Logger LOG = Logger.getLogger(TestVerticalCurveAlignment.class);
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	@Test
	public void test() {
		LOG.debug("test()");
		
		Parabole parabole = new Parabole(0.0, 0.04, 0.0001);
		double startx = 1200.0;
		double endx = 1800.0;
		VerticalCurveAlignment p1 = new VerticalCurveAlignment(parabole, startx, endx);
		Assert.assertNotNull(p1);
		Assert.assertNotNull(p1.getStartS());
		Assert.assertNotNull(p1.getEndS());
		
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

	@Test
	public void testConstructor() {
		LOG.debug("testConstructor()");
		double s0 = 0.0;
		double z0 = 0.0;
		double g0 = 0.02;
		double kv = 3000.0;
		double ends = 80.0;
		VerticalCurveAlignment vc = new VerticalCurveAlignment(s0, z0, g0, kv, ends);
		Assert.assertEquals(0.04672, vc.getEndTangent(), 0.001);
		Assert.assertEquals(80.0, vc.getEndS(), 0.001);
		
		s0 = 0.0;
		z0 = 0.0;
		g0 = 0.005;
		kv = 6000.0;
		ends = 250.0;
		vc = new VerticalCurveAlignment(s0, z0, g0, kv, ends);
		Assert.assertEquals(0.0468, vc.getEndTangent(), 0.001);
		Assert.assertEquals(250.0, vc.getEndS(), 0.001);
		
		
	}
	@Test
	public void testConstructorWithSagCurve() {
		LOG.debug("testConstructorWithSagCurve()");
		double s0 = 0.0;
		double z0 = 1000.0;
		double g0 = 0.03;
		double Kv0 = 10000.0;
		double s1 = 150.0;
		
		VerticalCurveAlignment sag = new VerticalCurveAlignment(s0, z0, g0, Kv0, s1);
		Assert.assertEquals(0.0, sag.getStartS(), 0.001);
		Assert.assertEquals(1000.0, sag.getStartZ(), 0.001);
		Assert.assertEquals(0.03, sag.getStartTangent(), 0.001);
		Assert.assertEquals(1005.625, sag.getEndZ(), 0.001);
		Assert.assertEquals(0.045, sag.getEndTangent(), 0.001);
		Assert.assertEquals(1005.625, sag.getY(150.0), 0.001);
		Assert.assertEquals(0.045, sag.getTangent(150.0), 0.001);
	}
	@Test
	public void testConstructorWithCrestCurve() {
		LOG.debug("testConstructorWithCrestCurve()");
		double s0 = 150.0;
		double z0 = 1005.625;
		double g0 = 0.045;
		double Kv0 = -20000.0;
		double s1 = 750.0;
		
		VerticalCurveAlignment sag = new VerticalCurveAlignment(s0, z0, g0, Kv0, s1);
		Assert.assertEquals(150.0, sag.getStartS(), 0.001);
		Assert.assertEquals(1005.625, sag.getStartZ(), 0.001);
		Assert.assertEquals(0.045, sag.getStartTangent(), 0.001);
		Assert.assertEquals(1023.625, sag.getEndZ(), 0.001);
		Assert.assertEquals(0.015, sag.getEndTangent(), 0.001);
		Assert.assertEquals(1023.625, sag.getY(750.0), 0.001);
		Assert.assertEquals(0.015, sag.getTangent(750.0), 0.001);
	}
	@Test
	public void testDerivative() {
		LOG.debug("testDerivative()");
		DesignSpeed dspeed = DesignSpeed.DS120;
		double s0 = 1000.0;
		double z0 = 1000.0;
		GradeAlignment grade1 = RandomGradeFactory.randomGradeAlignment(dspeed, s0, z0);
		VerticalCurveAlignment align = null;
		if (grade1.getStartTangent() > 0) {
			align = RandomFactory.randomSagCurve(dspeed, grade1.getEndS(),
					grade1.getEndZ(), grade1.getEndTangent(), true);
		} else {
			align = RandomFactory.randomCrestCurve(dspeed, grade1.getEndS(),
					grade1.getEndZ(), grade1.getEndTangent(), true);
		}
		Assert.assertNotNull(align);
		Assert.assertEquals(grade1.getEndS(), align.getStartS(), 0.001);
		Assert.assertEquals(grade1.getEndZ(), align.getStartZ(), 0.001);
		Assert.assertEquals(grade1.getStartTangent(), align.getStartTangent(), 0.001);
		
		GradeProfileAlignment galign = align.derivative();
		Assert.assertNotNull(galign);
		Assert.assertEquals(align.getStartS(), galign.getStartS(), 0.001);
		Assert.assertEquals(align.getStartTangent(), galign.getStartZ(), 0.001);
		Assert.assertEquals(align.getEndS(), galign.getEndS(), 0.001);
		Assert.assertEquals(align.getEndTangent(), galign.getEndZ(), 0.001);
		Assert.assertEquals(align.getLength(), galign.getLength(), 0.001);

		
	}

	@Test
	public void testGetSForSlope() {
		LOG.debug("testGetSForSlope()");
		VerticalCurveAlignment vc = new VerticalCurveAlignment(0.0, 1000.0, 0.03, 10000.0, 150.0);
		Assert.assertEquals(150.0, vc.getPolynom2().getSForSlope(0.045), 0.001);
		Assert.assertEquals(250.0, vc.getPolynom2().getSForSlope(0.055), 0.001);
		
		vc = new VerticalCurveAlignment(150.0, 1005.625, 0.045, -20000.0, 750.0);
		Assert.assertEquals(250.0, vc.getPolynom2().getSForSlope(0.04), 0.001);
		Assert.assertEquals(500.0, vc.getPolynom2().getSForSlope(0.0275), 0.001);
		Assert.assertEquals(750.0, vc.getPolynom2().getSForSlope(0.015), 0.001);
		
	}
}
