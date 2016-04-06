package com.mlab.pg.valign;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.random.RandomFactory;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class TestVerticalProfile {

	private final static Logger LOG = Logger.getLogger(TestVerticalProfile.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	
	@Test
	public void testConstructor() {
		LOG.debug("testConstructor()");
		VerticalProfile vp = new VerticalProfile(DesignSpeed.DS120);
		GradeAlign g1 = RandomFactory.randomGradeAlign(DesignSpeed.DS120, 0.0, 1000.0);
		Assert.assertTrue(vp.add(g1));
		GradeAlign g2 = RandomFactory.randomGradeAlign(DesignSpeed.DS100, 0.0, 1000.0);
		Assert.assertFalse(vp.add(g2));
	}

	@Test
	public void testGetSample() {
		LOG.debug("testGetSample()");
		VerticalProfile vp = new VerticalProfile(DesignSpeed.DS120);
		GradeAlign g1 = RandomFactory.randomGradeAlign(DesignSpeed.DS120, 0.0, 1000.0);
		vp.add(g1);
		XYVectorFunction function = vp.getSample(g1.getEndS()+1.0, 1000, 105, true);
		Assert.assertNull(function);
		function = vp.getSample(vp.getStartS(), g1.getStartS()-1.0, 105, true);
		Assert.assertNull(function);
		
		function = vp.getSample(vp.getStartS()-1.0, g1.getEndS(), 105, true);
		Assert.assertEquals(vp.getStartS(), function.getStartX(), 0.001);
		function = vp.getSample(vp.getStartS(), g1.getEndS()+1.0, 105, true);
		Assert.assertEquals(vp.getEndS(), function.getEndX(), 0.001);
		
		//LOG.debug(vp.getStartS());
		//LOG.debug(vp.getEndS());
		function = vp.getSample(vp.getStartS(), vp.getEndS(), 100, true);
		Assert.assertEquals(vp.getStartS(), function.getStartX(), 0.001);
		Assert.assertEquals(vp.getEndS(), function.getEndX(), 0.001);

	}
	@Test
	public void testEcm() {
		LOG.debug("testEcm()");
		VerticalProfile vp = new VerticalProfile(DesignSpeed.DS120);
		GradeAlign g1 = RandomFactory.randomGradeAlign(DesignSpeed.DS120, 0.0, 1000.0);
		vp.add(g1);
		double space = (vp.getEndS() - vp.getStartS())/100.0;
		double ecm = vp.ecm(vp, space);
		Assert.assertEquals(0.0, ecm, 0.001);

		VerticalProfile vp2 = new VerticalProfile(DesignSpeed.DS120);
		GradeAlign g2 = RandomFactory.randomGradeAlign(DesignSpeed.DS120, 0.0, 1000.0);
		vp2.add(g2);
		LOG.debug(vp.ecm(vp2, space));

	}

}
