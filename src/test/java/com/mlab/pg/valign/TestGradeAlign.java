package com.mlab.pg.valign;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.mlab.pg.xyfunction.Straight;

import junit.framework.Assert;

public class TestGradeAlign {

	private final Logger LOG = Logger.getLogger(TestGradeAlign.class);

	@Test
	public void test() {
		LOG.debug("test()");
		
		Straight straight = new Straight(0.0, 0.04);
		double startx = 0.0;
		double endx = 1200.0;
		GradeAlign tangent1 = new GradeAlign(straight, startx, endx);
		Assert.assertNotNull(tangent1);
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

}
