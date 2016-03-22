package com.mlab.pg.valign;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.random.RandomFactory;

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

}
