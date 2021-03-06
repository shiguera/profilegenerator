package com.mlab.pg.valign;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.xyfunction.Straight;

import junit.framework.Assert;

public class TestGradeAlignment {

	private final static Logger LOG = Logger.getLogger(TestGradeAlignment.class);
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	@Test
	public void test() {
		LOG.debug("test()");
		
		Straight straight = new Straight(0.0, 0.04);
		double startx = 0.0;
		double endx = 1200.0;
		GradeAlignment tangent1 = new GradeAlignment(straight, startx, endx);
		Assert.assertNotNull(tangent1);
		Assert.assertNotNull(tangent1.getStartS());
		Assert.assertNotNull(tangent1.getEndS());
						
		Assert.assertNotNull(tangent1.polynom);
		System.out.println(tangent1.polynom.getClass().getName());
		Assert.assertTrue(tangent1.polynom.getClass().isAssignableFrom(Straight.class));
		Assert.assertEquals(0.0, tangent1.getStartS(), 0.0001);
		Assert.assertEquals(0.0, tangent1.getStartZ(), 0.0001);
		Assert.assertEquals(1200.0, tangent1.getEndS(), 0.0001);
		Assert.assertEquals(48.0, tangent1.getEndZ(), 0.0001);
		Assert.assertEquals(0.04, tangent1.getStartTangent(), 0.0001);
		Assert.assertEquals(0.04, tangent1.getEndTangent(), 0.0001);
		Assert.assertEquals(1200.0, tangent1.getLength(), 0.0001);
		

	}

	@Test
	public void testConstructorPuntoPdteLongitud() {
		LOG.debug("testConstructorPuntoPdteLongitud()");
		double starts = 0.0;
		double startz = 1000.0;
		double slope = 0.03;
		double length = 1200.0;
		GradeAlignment grade = new GradeAlignment(starts, startz, slope, length);
		Assert.assertEquals(0.0, grade.getStartS(), 0.001);
		Assert.assertEquals(1000.0, grade.getStartZ(), 0.001);
		Assert.assertEquals(1200.0, grade.getEndS(), 0.001);
		Assert.assertEquals(1036.0, grade.getEndZ(), 0.001);
		
	}
	@Test
	public void testDerivative() {
		// With up grade
		double starts =0.0;
		double startz = 900.0;
		double slope = 0.05;
		Straight r = new Straight(starts, startz, slope);
		double ends = 1000.0;
		GradeAlignment align = new GradeAlignment(r, starts, ends);

		GradeProfileAlignment galign = align.derivative();
		Assert.assertEquals(galign.startS, starts, 0.001);
		Assert.assertEquals(galign.endS, ends, 0.001);
		Assert.assertEquals(galign.getStartZ(), 0.05, 0.001);
		Assert.assertEquals(galign.getEndZ(), 0.05, 0.001);

		// With down grade
		starts =100.0;
		startz = 900.0;
		slope = -0.03;
		r = new Straight(starts, startz, slope);
		ends = 1300.0;
		align = new GradeAlignment(r, starts, ends);

		galign = align.derivative();
		Assert.assertEquals(galign.startS, starts, 0.001);
		Assert.assertEquals(galign.endS, ends, 0.001);
		Assert.assertEquals(galign.getStartZ(), -0.03, 0.001);
		Assert.assertEquals(galign.getEndZ(), -0.03, 0.001);

	}
}
