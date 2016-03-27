package com.mlab.pg.valign;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.random.RandomFactory;

import junit.framework.Assert;

public class TestGradeProfileAlign {

	private static final Logger LOG = Logger.getLogger(TestGradeProfileAlign.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	
	@Test
	public void testIntegrate() {
		LOG.debug("testIntegrate()");
		DesignSpeed dspeed = DesignSpeed.DS120;
		double s0 = 1000.0;
		double z0 = 1000.0;
		GradeAlign grade1 = RandomFactory.randomGradeAlign(dspeed, s0, z0);
		
		GradeProfileAlign galign = grade1.derivative();
		
		double startZ = z0;
		GradeAlign align = (GradeAlign) galign.integrate(startZ);
		Assert.assertNotNull(align);
		Assert.assertEquals(galign.getStartS(), align.getStartS(), 0.001);
		Assert.assertEquals(galign.getEndS(), align.getEndS(), 0.001);
		Assert.assertEquals(galign.getStartGrade(), align.getStartTangent(), 0.001);
		Assert.assertEquals(galign.getEndGrade(), align.getEndTangent(), 0.001);
		Assert.assertEquals(align.getStartZ(), z0, 0.001);
		

		
		
		double g2 = RandomFactory.randomGradeSlope(dspeed);
		if (grade1.getSlope() > 0) {
			g2 = - Math.abs(g2);
		} else {
			g2 = Math.abs(g2);
		}
		
		VerticalCurveAlign vcalign = RandomFactory.randomVerticalCurve(dspeed, grade1, g2);
		Assert.assertNotNull(align);
		galign = vcalign.derivative();
		
		VerticalCurveAlign vcintegrate = (VerticalCurveAlign) galign.integrate(vcalign.getStartZ()); 
		Assert.assertEquals(vcalign.getStartS(), vcintegrate.getStartS(), 0.001);
		Assert.assertEquals(vcalign.getStartZ(), vcintegrate.getStartZ(), 0.001);
		Assert.assertEquals(vcalign.getStartTangent(), vcintegrate.getStartTangent(), 0.001);
		Assert.assertEquals(vcalign.getEndS(), vcintegrate.getEndS(), 0.001);
		Assert.assertEquals(vcalign.getEndZ(), vcintegrate.getEndZ(), 0.001);
		Assert.assertEquals(vcalign.getEndTangent(), vcintegrate.getEndTangent(), 0.001);
		
		

	}
}
