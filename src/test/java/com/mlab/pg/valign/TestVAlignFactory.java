package com.mlab.pg.valign;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.norma.DesignSpeed;

import junit.framework.Assert;

public class TestVAlignFactory {
	
	static Logger LOG = Logger.getLogger(TestVAlignFactory.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}

	@Test
	public void testCreateVCFrom_PointGradeKvAndFinalSlope() {
		LOG.debug("testCreateVCFrom_PointGradeKvAndFinalSlope()");
		DesignSpeed dspeed = DesignSpeed.DS100;
		double s0 = 0.0;
		double z0 = 1000.0;
		double g0 = 0.03;
		double kv = 10000.0;
		double gf = 0.045;
		VerticalCurveAlign vc = VAlignFactory.createVCFrom_PointGradeKvAndFinalSlope(dspeed, s0, z0, g0, kv, gf);
		Assert.assertEquals(150.0, vc.getEndS(), 0.001);
		
		s0 = 150.0;
		z0 = 1005.625;
		g0 = 0.045;
		kv = -20000.0;
		gf = 0.015;
		vc = VAlignFactory.createVCFrom_PointGradeKvAndFinalSlope(dspeed, s0, z0, g0, kv, gf);
		Assert.assertEquals(750.0, vc.getEndS(), 0.001);
		
		
		
		
	}
}
