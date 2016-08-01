package com.mlab.pg.random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.norma.CrestCurveLimits;
import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.norma.SagCurveLimits;
import com.mlab.pg.norma.VerticalCurveLimits;
import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.VAlignment;
import com.mlab.pg.valign.VerticalCurveAlignment;
import com.mlab.pg.valign.VerticalProfile;



public class TestRandomFactory {

	static Logger LOG = Logger.getLogger(TestRandomFactory.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	// Perfiles completos
	@Test
	public void testRandomVerticalProfile() {
		LOG.debug("testRandomVerticalProfile()");
		// TODO
	}
	@Test
	public void testRandomVerticalProfileBeginningOnType_I() {
		LOG.debug("testRandomVerticalProfileBeginningOnType_I()");
		DesignSpeed dspeed = DesignSpeed.DS120;
		double s0 = 1000.0;
		double z0 = 1000.0;
		VerticalProfile vp = RandomFactory.randomVerticalProfileBeginningOnType_I(dspeed, s0, z0, 1);
		Assert.assertNotNull(vp);
		Assert.assertEquals(3, vp.size());
		VAlignment align = vp.get(0);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		GradeAlignment grade = (GradeAlignment)vp.get(0);
		Assert.assertTrue(grade.getSlope()>0);
		align = vp.get(1);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		VerticalCurveAlignment vc = (VerticalCurveAlignment)vp.get(1);
		Assert.assertTrue(vc.getPolynom2().getKv()<0);
		align = vp.get(2);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(2);
		Assert.assertTrue(grade.getSlope()<0);
		
		dspeed = DesignSpeed.DS80;
		vp = RandomFactory.randomVerticalProfileBeginningOnType_I(dspeed, s0, z0, 2);
		Assert.assertNotNull(vp);
		Assert.assertEquals(6, vp.size());
		align = vp.get(0);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(0);
		Assert.assertTrue(grade.getSlope()>0);
		align = vp.get(1);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		vc = (VerticalCurveAlignment)vp.get(1);
		Assert.assertTrue(vc.getPolynom2().getKv()<0);
		align = vp.get(2);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(2);
		Assert.assertTrue(grade.getSlope()<0);
		align = vp.get(3);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(3);
		Assert.assertTrue(grade.getSlope()<0);
		align = vp.get(4);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		vc = (VerticalCurveAlignment)vp.get(4);
		Assert.assertTrue(vc.getPolynom2().getKv()>0);
		align = vp.get(5);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(5);
		Assert.assertTrue(grade.getSlope()>0);

		dspeed = DesignSpeed.DS40;
		vp = RandomFactory.randomVerticalProfileBeginningOnType_I(dspeed, s0, z0, 3);
		Assert.assertNotNull(vp);
		Assert.assertEquals(9, vp.size());
		align = vp.get(0);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(0);
		Assert.assertTrue(grade.getSlope()>0);
		align = vp.get(1);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		vc = (VerticalCurveAlignment)vp.get(1);
		Assert.assertTrue(vc.getPolynom2().getKv()<0);
		align = vp.get(2);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(2);
		Assert.assertTrue(grade.getSlope()<0);
		align = vp.get(3);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(3);
		Assert.assertTrue(grade.getSlope()<0);
		align = vp.get(4);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		vc = (VerticalCurveAlignment)vp.get(4);
		Assert.assertTrue(vc.getPolynom2().getKv()>0);
		align = vp.get(5);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(5);
		Assert.assertTrue(grade.getSlope()>0);
		align = vp.get(6);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(6);
		Assert.assertTrue(grade.getSlope()>0);
		align = vp.get(7);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		vc = (VerticalCurveAlignment)vp.get(7);
		Assert.assertTrue(vc.getPolynom2().getKv()<0);
		align = vp.get(8);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(8);
		Assert.assertTrue(grade.getSlope()<0);

		
	}
	@Test
	public void testRandomVerticalProfileBeginningOnType_II() {
		LOG.debug("testRandomVerticalProfileBeginningOnType_II()");
		DesignSpeed dspeed = DesignSpeed.DS100;
		double s0 = 1000.0;
		double z0 = 1000.0;
		VerticalProfile vp = RandomFactory.randomVerticalProfileBeginningOnType_II(dspeed, s0, z0, 1);
		Assert.assertNotNull(vp);
		Assert.assertEquals(3, vp.size());
		VAlignment align = vp.get(0);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		GradeAlignment grade = (GradeAlignment)vp.get(0);
		Assert.assertTrue(grade.getSlope()<0);
		align = vp.get(1);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		VerticalCurveAlignment vc = (VerticalCurveAlignment)vp.get(1);
		Assert.assertTrue(vc.getPolynom2().getKv()>0);
		align = vp.get(2);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(2);
		Assert.assertTrue(grade.getSlope()>0);
		
		dspeed = DesignSpeed.DS60;
		vp = RandomFactory.randomVerticalProfileBeginningOnType_II(dspeed, s0, z0, 2);
		Assert.assertNotNull(vp);
		Assert.assertEquals(6, vp.size());
		align = vp.get(0);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(0);
		Assert.assertTrue(grade.getSlope()<0);
		align = vp.get(1);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		vc = (VerticalCurveAlignment)vp.get(1);
		Assert.assertTrue(vc.getPolynom2().getKv()>0);
		align = vp.get(2);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(2);
		Assert.assertTrue(grade.getSlope()>0);
		align = vp.get(3);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(3);
		Assert.assertTrue(grade.getSlope()>0);
		align = vp.get(4);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		vc = (VerticalCurveAlignment)vp.get(4);
		Assert.assertTrue(vc.getPolynom2().getKv()<0);
		align = vp.get(5);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(5);
		Assert.assertTrue(grade.getSlope()<0);

		dspeed = DesignSpeed.DS120;
		vp = RandomFactory.randomVerticalProfileBeginningOnType_II(dspeed, s0, z0, 3);
		Assert.assertNotNull(vp);
		Assert.assertEquals(9, vp.size());
		align = vp.get(0);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(0);
		Assert.assertTrue(grade.getSlope()<0);
		align = vp.get(1);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		vc = (VerticalCurveAlignment)vp.get(1);
		Assert.assertTrue(vc.getPolynom2().getKv()>0);
		align = vp.get(2);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(2);
		Assert.assertTrue(grade.getSlope()>0);
		align = vp.get(3);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(3);
		Assert.assertTrue(grade.getSlope()>0);
		align = vp.get(4);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		vc = (VerticalCurveAlignment)vp.get(4);
		Assert.assertTrue(vc.getPolynom2().getKv()<0);
		align = vp.get(5);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(5);
		Assert.assertTrue(grade.getSlope()<0);
		align = vp.get(6);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(6);
		Assert.assertTrue(grade.getSlope()<0);
		align = vp.get(7);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		vc = (VerticalCurveAlignment)vp.get(7);
		Assert.assertTrue(vc.getPolynom2().getKv()>0);
		align = vp.get(8);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(8);
		Assert.assertTrue(grade.getSlope()>0);

		
	}

	// Perfiles básicos
	@Test
	public void testRandomVerticalProfileType_I_DS120() {
		LOG.debug("testRandomVerrticalProfileType_I_DS120");
		DesignSpeed dspeed = DesignSpeed.DS120;
		double s0 = 0.0;
		double z0 = 1000.0;
		VerticalProfile vp = RandomFactory.randomVerticalProfileType_I(dspeed, s0, z0);
		Assert.assertNotNull(vp);
		Assert.assertEquals(3, vp.size());
		VAlignment align = vp.get(0);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		GradeAlignment grade = (GradeAlignment)vp.get(0);
		Assert.assertTrue(grade.getSlope()>0);
		align = vp.get(1);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		VerticalCurveAlignment vc = (VerticalCurveAlignment)vp.get(1);
		Assert.assertTrue(vc.getPolynom2().getKv()<0);
		align = vp.get(2);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(2);
		Assert.assertTrue(grade.getSlope()<0);
		
		//System.out.println(vp);
	}

	@Test
	public void testRandomVerticalProfileType_I_DS100() {
		LOG.debug("testRandomVerrticalProfileType_I_DS100");
		DesignSpeed dspeed = DesignSpeed.DS100;
		double s0 = 0.0;
		double z0 = 1000.0;
		VerticalProfile vp = RandomFactory.randomVerticalProfileType_I(dspeed, s0, z0);
		Assert.assertNotNull(vp);
		Assert.assertEquals(3, vp.size());
		VAlignment align = vp.get(0);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		GradeAlignment grade = (GradeAlignment)vp.get(0);
		Assert.assertTrue(grade.getSlope()>0);
		align = vp.get(1);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		VerticalCurveAlignment vc = (VerticalCurveAlignment)vp.get(1);
		Assert.assertTrue(vc.getPolynom2().getKv()<0);
		align = vp.get(2);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(2);
		Assert.assertTrue(grade.getSlope()<0);
		
		//System.out.println(vp);
	}
	@Test
	public void testRandomVerticalProfileType_I_DS80() {
		LOG.debug("testRandomVerrticalProfileType_I_DS80");
		DesignSpeed dspeed = DesignSpeed.DS80;
		double s0 = 0.0;
		double z0 = 1000.0;
		VerticalProfile vp = RandomFactory.randomVerticalProfileType_I(dspeed, s0, z0);
		Assert.assertNotNull(vp);
		Assert.assertEquals(3, vp.size());
		VAlignment align = vp.get(0);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		GradeAlignment grade = (GradeAlignment)vp.get(0);
		Assert.assertTrue(grade.getSlope()>0);
		align = vp.get(1);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		VerticalCurveAlignment vc = (VerticalCurveAlignment)vp.get(1);
		Assert.assertTrue(vc.getPolynom2().getKv()<0);
		align = vp.get(2);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(2);
		Assert.assertTrue(grade.getSlope()<0);
		
		Assert.assertNotNull(vp);
	}
	@Test
	public void testRandomVerticalProfileType_I_DS60() {
		LOG.debug("testRandomVerrticalProfileType_I_DS60");
		DesignSpeed dspeed = DesignSpeed.DS60;
		double s0 = 0.0;
		double z0 = 1000.0;
		VerticalProfile vp = RandomFactory.randomVerticalProfileType_I(dspeed, s0, z0);
		Assert.assertNotNull(vp);
		Assert.assertEquals(3, vp.size());
		VAlignment align = vp.get(0);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		GradeAlignment grade = (GradeAlignment)vp.get(0);
		Assert.assertTrue(grade.getSlope()>0);
		align = vp.get(1);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		VerticalCurveAlignment vc = (VerticalCurveAlignment)vp.get(1);
		Assert.assertTrue(vc.getPolynom2().getKv()<0);
		align = vp.get(2);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(2);
		Assert.assertTrue(grade.getSlope()<0);
		
		//Assert.assertNotNull(vp);
	}
	@Test
	public void testRandomVerticalProfileType_I_DS40() {
		LOG.debug("testRandomVerrticalProfileType_I_DS40");
		DesignSpeed dspeed = DesignSpeed.DS40;
		double s0 = 0.0;
		double z0 = 1000.0;
		VerticalProfile vp = RandomFactory.randomVerticalProfileType_I(dspeed, s0, z0);
		Assert.assertNotNull(vp);
		Assert.assertEquals(3, vp.size());
		VAlignment align = vp.get(0);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		GradeAlignment grade = (GradeAlignment)vp.get(0);
		Assert.assertTrue(grade.getSlope()>0);
		align = vp.get(1);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		VerticalCurveAlignment vc = (VerticalCurveAlignment)vp.get(1);
		Assert.assertTrue(vc.getPolynom2().getKv()<0);
		align = vp.get(2);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(2);
		Assert.assertTrue(grade.getSlope()<0);
		
		//Assert.assertNotNull(vp);
	}

	@Test
	public void testRandomVerticalProfileType_II_DS120() {
		LOG.debug("testRandomVerrticalProfileType_II_DS120");
		DesignSpeed dspeed = DesignSpeed.DS120;
		double s0 = 0.0;
		double z0 = 1000.0;
		VerticalProfile vp = RandomFactory.randomVerticalProfileType_II(dspeed, s0, z0);
		Assert.assertNotNull(vp);
		Assert.assertEquals(3, vp.size());
		VAlignment align = vp.get(0);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		GradeAlignment grade = (GradeAlignment)vp.get(0);
		Assert.assertTrue(grade.getSlope()<0);
		align = vp.get(1);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		VerticalCurveAlignment vc = (VerticalCurveAlignment)vp.get(1);
		Assert.assertTrue(vc.getPolynom2().getKv()>0);
		align = vp.get(2);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(2);
		Assert.assertTrue(grade.getSlope()>0);
		
		//System.out.println(vp);
	}
	@Test
	public void testRandomVerticalProfileType_II_DS100() {
		LOG.debug("testRandomVerrticalProfileType_II_DS100");
		DesignSpeed dspeed = DesignSpeed.DS100;
		double s0 = 0.0;
		double z0 = 1000.0;
		VerticalProfile vp = RandomFactory.randomVerticalProfileType_II(dspeed, s0, z0);
		Assert.assertNotNull(vp);
		Assert.assertEquals(3, vp.size());
		VAlignment align = vp.get(0);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		GradeAlignment grade = (GradeAlignment)vp.get(0);
		Assert.assertTrue(grade.getSlope()<0);
		align = vp.get(1);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		VerticalCurveAlignment vc = (VerticalCurveAlignment)vp.get(1);
		Assert.assertTrue(vc.getPolynom2().getKv()>0);
		align = vp.get(2);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(2);
		Assert.assertTrue(grade.getSlope()>0);
		
		//System.out.println(vp);
	}
	@Test
	public void testRandomVerticalProfileType_II_DS80() {
		LOG.debug("testRandomVerrticalProfileType_II_DS80");
		DesignSpeed dspeed = DesignSpeed.DS80;
		double s0 = 0.0;
		double z0 = 1000.0;
		VerticalProfile vp = RandomFactory.randomVerticalProfileType_II(dspeed, s0, z0);
		Assert.assertNotNull(vp);
		Assert.assertEquals(3, vp.size());
		VAlignment align = vp.get(0);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		GradeAlignment grade = (GradeAlignment)vp.get(0);
		Assert.assertTrue(grade.getSlope()<0);
		align = vp.get(1);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		VerticalCurveAlignment vc = (VerticalCurveAlignment)vp.get(1);
		Assert.assertTrue(vc.getPolynom2().getKv()>0);
		align = vp.get(2);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(2);
		Assert.assertTrue(grade.getSlope()>0);
		
		//System.out.println(vp);
	}
	@Test
	public void testRandomVerticalProfileType_II_DS60() {
		LOG.debug("testRandomVerrticalProfileType_II_DS60");
		DesignSpeed dspeed = DesignSpeed.DS60;
		double s0 = 0.0;
		double z0 = 1000.0;
		VerticalProfile vp = RandomFactory.randomVerticalProfileType_II(dspeed, s0, z0);
		Assert.assertNotNull(vp);
		Assert.assertEquals(3, vp.size());
		VAlignment align = vp.get(0);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		GradeAlignment grade = (GradeAlignment)vp.get(0);
		Assert.assertTrue(grade.getStartTangent()<0);
		align = vp.get(1);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		VerticalCurveAlignment vc = (VerticalCurveAlignment)vp.get(1);
		Assert.assertTrue(vc.getPolynom2().getKv()>0);
		align = vp.get(2);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(2);
		Assert.assertTrue(grade.getStartTangent()>0);
		
		//System.out.println(vp);
	}
	@Test
	public void testRandomVerticalProfileType_II_DS40() {
		LOG.debug("testRandomVerrticalProfileType_II_DS40");
		DesignSpeed dspeed = DesignSpeed.DS40;
		double s0 = 0.0;
		double z0 = 1000.0;
		VerticalProfile vp = RandomFactory.randomVerticalProfileType_II(dspeed, s0, z0);
		Assert.assertNotNull(vp);
		Assert.assertEquals(3, vp.size());
		VAlignment align = vp.get(0);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		GradeAlignment grade = (GradeAlignment)vp.get(0);
		Assert.assertTrue(grade.getStartTangent()<0);
		align = vp.get(1);
		Assert.assertTrue(align.getClass().isAssignableFrom(VerticalCurveAlignment.class));
		VerticalCurveAlignment vc = (VerticalCurveAlignment)vp.get(1);
		Assert.assertTrue(vc.getPolynom2().getKv()>0);
		align = vp.get(2);
		Assert.assertTrue(align.getClass().isAssignableFrom(GradeAlignment.class));
		grade = (GradeAlignment)vp.get(2);
		Assert.assertTrue(grade.getStartTangent()>0);
		
		//System.out.println(vp);
	}

	// Vertical Curves
	@Test
	public void testRandomVerticalCurveDS120() {
		LOG.debug("testRandomVerticalCurveDS120()");
		DesignSpeed dspeed = DesignSpeed.DS120;
		double s0= 0.0;
		double z0= 1000.0;
		GradeAlignment grade1 = RandomGradeFactory.randomGradeAlignment(dspeed, s0, z0);
		double g1 = grade1.getStartTangent();
		VerticalCurveAlignment vc = null;
		if(g1>0) {
			vc = RandomFactory.randomCrestCurve(dspeed, grade1.getEndS(),
					grade1.getEndZ(), grade1.getEndTangent(), true);	
		} else {
			vc = RandomFactory.randomSagCurve(dspeed, grade1.getEndS(),
					grade1.getEndZ(), grade1.getEndTangent(), true);	
		}
		Assert.assertNotNull(vc);
		Assert.assertEquals(grade1.getEndS(), vc.getStartS(), 0.001);
		Assert.assertEquals(grade1.getEndZ(), vc.getStartZ(), 0.001);
		Assert.assertEquals(g1, vc.getStartTangent(), 0.001);
		VerticalCurveLimits limits = null;
		if(vc.getPolynom2().getKv()>0) {
			limits = new SagCurveLimits(dspeed);
		} else {
			limits = new CrestCurveLimits(dspeed);
		}
		//System.out.println(vc.getPolynom2().getKv());
		Assert.assertTrue(Math.abs(vc.getPolynom2().getKv()) > limits.getMinKv());
		Assert.assertTrue(Math.abs(vc.getPolynom2().getKv()) < limits.getMaxKv());
		Assert.assertTrue(vc.getLength() >= limits.getMinLength());
		Assert.assertTrue(vc.getLength() < limits.getMaxLength());
		//System.out.println(vc);
	}
	@Test
	public void testRandomVerticalCurveDS100() {
		LOG.debug("testRandomVerticalCurveDS100()");
		DesignSpeed dspeed = DesignSpeed.DS100;
		double s0= 0.0;
		double z0= 1000.0;
		GradeAlignment grade1 = RandomGradeFactory.randomGradeAlignment(dspeed, s0, z0);
		double g1 = grade1.getStartTangent();
		VerticalCurveAlignment vc = null;
		if(g1>0) {
			vc = RandomFactory.randomCrestCurve(dspeed, grade1.getEndS(),
					grade1.getEndZ(), grade1.getEndTangent(), true);	
		} else {
			vc = RandomFactory.randomSagCurve(dspeed, grade1.getEndS(),
					grade1.getEndZ(), grade1.getEndTangent(), true);	
		}
		Assert.assertNotNull(vc);
		Assert.assertEquals(grade1.getEndS(), vc.getStartS(), 0.001);
		Assert.assertEquals(grade1.getEndZ(), vc.getStartZ(), 0.001);
		Assert.assertEquals(g1, vc.getStartTangent(), 0.001);
		VerticalCurveLimits limits = null;
		if(vc.getPolynom2().getKv()>0) {
			limits = new SagCurveLimits(dspeed);
		} else {
			limits = new CrestCurveLimits(dspeed);
		}
		//System.out.println(vc.getPolynom2().getKv());
		Assert.assertTrue(Math.abs(vc.getPolynom2().getKv()) > limits.getMinKv());
		Assert.assertTrue(Math.abs(vc.getPolynom2().getKv()) < limits.getMaxKv());
		Assert.assertTrue(vc.getLength() >= limits.getMinLength());
		Assert.assertTrue(vc.getLength() < limits.getMaxLength());
		//System.out.println(vc);
	}
	@Test
	public void testRandomVerticalCurveDS80() {
		LOG.debug("testRandomVerticalCurveDS80()");
		DesignSpeed dspeed = DesignSpeed.DS80;
		double s0= 0.0;
		double z0= 1000.0;
		GradeAlignment grade1 = RandomGradeFactory.randomGradeAlignment(dspeed, s0, z0);
		double g1 = grade1.getStartTangent();
		VerticalCurveAlignment vc = null;
		if(g1>0) {
			vc = RandomFactory.randomCrestCurve(dspeed, grade1.getEndS(),
					grade1.getEndZ(), grade1.getEndTangent(), true);	
		} else {
			vc = RandomFactory.randomSagCurve(dspeed, grade1.getEndS(),
					grade1.getEndZ(), grade1.getEndTangent(), true);	
		}
		Assert.assertNotNull(vc);
		Assert.assertEquals(grade1.getEndS(), vc.getStartS(), 0.001);
		Assert.assertEquals(grade1.getEndZ(), vc.getStartZ(), 0.001);
		Assert.assertEquals(g1, vc.getStartTangent(), 0.001);
		VerticalCurveLimits limits = null;
		if(vc.getPolynom2().getKv()>0) {
			limits = new SagCurveLimits(dspeed);
		} else {
			limits = new CrestCurveLimits(dspeed);
		}
		//System.out.println(vc.getPolynom2().getKv());
		Assert.assertTrue(Math.abs(vc.getPolynom2().getKv()) > limits.getMinKv());
		Assert.assertTrue(Math.abs(vc.getPolynom2().getKv()) < limits.getMaxKv());
		Assert.assertTrue(vc.getLength() >= limits.getMinLength());
		Assert.assertTrue(vc.getLength() < limits.getMaxLength());
		//System.out.println(vc);
	}
	@Test
	public void testRandomVerticalCurveDS60() {
		LOG.debug("testRandomVerticalCurveDS60()");
		DesignSpeed dspeed = DesignSpeed.DS60;
		double s0= 0.0;
		double z0= 1000.0;
		GradeAlignment grade1 = RandomGradeFactory.randomGradeAlignment(dspeed, s0, z0);
		double g1 = grade1.getStartTangent();
		VerticalCurveAlignment vc = null;
		if(g1>0) {
			vc = RandomFactory.randomCrestCurve(dspeed, grade1.getEndS(),
					grade1.getEndZ(), grade1.getEndTangent(), true);	
		} else {
			vc = RandomFactory.randomSagCurve(dspeed, grade1.getEndS(),
					grade1.getEndZ(), grade1.getEndTangent(), true);	
		}
		Assert.assertNotNull(vc);
		Assert.assertEquals(grade1.getEndS(), vc.getStartS(), 0.001);
		Assert.assertEquals(grade1.getEndZ(), vc.getStartZ(), 0.001);
		Assert.assertEquals(g1, vc.getStartTangent(), 0.001);
		VerticalCurveLimits limits = null;
		if(vc.getPolynom2().getKv()>0) {
			limits = new SagCurveLimits(dspeed);
		} else {
			limits = new CrestCurveLimits(dspeed);
		}
		//System.out.println(vc.getPolynom2().getKv());
		Assert.assertTrue(Math.abs(vc.getPolynom2().getKv()) > limits.getMinKv());
		Assert.assertTrue(Math.abs(vc.getPolynom2().getKv()) < limits.getMaxKv());
		Assert.assertTrue(vc.getLength() >= limits.getMinLength());
		Assert.assertTrue(vc.getLength() < limits.getMaxLength());
		//System.out.println(vc);
	}
	@Test
	public void testRandomVerticalCurveDS40() {
		LOG.debug("testRandomVerticalCurveDS40()");
		DesignSpeed dspeed = DesignSpeed.DS40;
		double s0= 0.0;
		double z0= 1000.0;
		GradeAlignment grade1 = RandomGradeFactory.randomGradeAlignment(dspeed, s0, z0);
		double g1 = grade1.getStartTangent();
		VerticalCurveAlignment vc = null;
		if(g1>0) {
			vc = RandomFactory.randomCrestCurve(dspeed, grade1.getEndS(),
					grade1.getEndZ(), grade1.getEndTangent(), true);	
		} else {
			vc = RandomFactory.randomSagCurve(dspeed, grade1.getEndS(),
					grade1.getEndZ(), grade1.getEndTangent(), true);	
		}
		Assert.assertNotNull(vc);
		Assert.assertEquals(grade1.getEndS(), vc.getStartS(), 0.001);
		Assert.assertEquals(grade1.getEndZ(), vc.getStartZ(), 0.001);
		Assert.assertEquals(g1, vc.getStartTangent(), 0.001);
		VerticalCurveLimits limits = null;
		if(vc.getPolynom2().getKv()>0) {
			limits = new SagCurveLimits(dspeed);
		} else {
			limits = new CrestCurveLimits(dspeed);
		}
		//System.out.println(vc.getPolynom2().getKv());
		Assert.assertTrue(Math.abs(vc.getPolynom2().getKv()) > limits.getMinKv());
		Assert.assertTrue(Math.abs(vc.getPolynom2().getKv()) < limits.getMaxKv());
		Assert.assertTrue(vc.getLength() >= limits.getMinLength());
		Assert.assertTrue(vc.getLength() < limits.getMaxLength());
		//System.out.println(vc);
	}

	@Test
	public void testRandomSagCurveKv() {
		LOG.debug("testRandomSagCurveKv()");
		for(int i=0; i<100; i++) {
			DesignSpeed dspeed = DesignSpeed.DS120;
			VerticalCurveLimits limits = new SagCurveLimits(dspeed);
			double kv = RandomFactory.randomSagCurveKv(dspeed);
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());
			
			dspeed = DesignSpeed.DS100;
			limits = new SagCurveLimits(dspeed);
			kv = RandomFactory.randomSagCurveKv(dspeed);
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());

			dspeed = DesignSpeed.DS80;
			limits = new SagCurveLimits(dspeed);
			kv = RandomFactory.randomSagCurveKv(dspeed);
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());
			
			dspeed = DesignSpeed.DS60;
			limits = new SagCurveLimits(dspeed);
			kv = RandomFactory.randomSagCurveKv(dspeed);
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());
			
			dspeed = DesignSpeed.DS40;
			limits = new SagCurveLimits(dspeed);
			kv = RandomFactory.randomSagCurveKv(dspeed);
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());

			
		}
	}
	@Test
	public void testRandomCrestCurveKv() {
		LOG.debug("testRandomCrestCurveKv()");
		for(int i=0; i<100; i++) {
			DesignSpeed dspeed = DesignSpeed.DS120;
			VerticalCurveLimits limits = new CrestCurveLimits(dspeed);
			double kv = Math.abs(RandomFactory.randomCrestCurveKv(dspeed));
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());
			
			dspeed = DesignSpeed.DS100;
			limits = new CrestCurveLimits(dspeed);
			kv = Math.abs(RandomFactory.randomCrestCurveKv(dspeed));
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());

			dspeed = DesignSpeed.DS80;
			limits = new CrestCurveLimits(dspeed);
			kv = Math.abs(RandomFactory.randomCrestCurveKv(dspeed));
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());
			
			dspeed = DesignSpeed.DS60;
			limits = new CrestCurveLimits(dspeed);
			kv = Math.abs(RandomFactory.randomCrestCurveKv(dspeed));
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());
			
			dspeed = DesignSpeed.DS40;
			limits = new CrestCurveLimits(dspeed);
			kv = Math.abs(RandomFactory.randomCrestCurveKv(dspeed));
			//System.out.println(limits.getMinKv()+", " + kv);
			Assert.assertTrue(kv>=limits.getMinKv());
			Assert.assertTrue(kv<=limits.getMaxKv());

		}
	}

	// Grades
		@Test
	public void testRandomSign() {
		LOG.debug("testRandomSign()");
		int countpositives = 0;
		int countnegatives = 0;
		for (int i=0; i<100;i++) {
			double sign = RandomFactory.randomSign();
			Assert.assertTrue(sign==1.0 || sign==-1.0);
			if(sign==1.0) {
				countpositives++;
			} else if (sign==-1.0) {
				countnegatives++;
			} else {
				Assert.fail();
			}
		}
		//System.out.println(countpositives + ", " + countnegatives);	
	}
	@Test
	public void testRandomDoubleByIncrements() {
		LOG.debug("testRandomDoubleByIncrements()");
		double min=0.5;
		double max = 12;
		double steep = 0.5;
		for (int i=0;i<200;i++) {
			double number = RandomFactory.randomDoubleByIncrements(min, max, steep);
			//System.out.print(number + " ");
			Assert.assertTrue(number>=min && number<=max);
		}
		//System.out.println();
	}
	@Test
	public void testRandomDoubleByIncrementsWithNegativeNumbers() {
		LOG.debug("testRandomDoubleByIncrementsWithNegativeNumbers()");
		double min = -12;
		double max = 12;
		double increment = 0.5;
		for (int i=0;i<200;i++) {
			double number = RandomFactory.randomDoubleByIncrements(min, max, increment);
			//System.out.print(number + " ");
			Assert.assertTrue(number>=min && number<=max);
		}
		//System.out.println();
	}
	@Test
	public void testRandomDoubleByIncrements_ExactMatch() {
		LOG.debug("testRandomDoubleByIncrements_ExactMatch()");
		double min= 1.0;
		double max = 2.0;
		double increment = 0.25;
		int[] contador = new int[5];
		for(int i=0; i<5; i++) {
			contador[i] = 0;
		}
		for (int i=0;i<200;i++) {
			double number = RandomFactory.randomDoubleByIncrements(min, max, increment);
			//System.out.print(number + " ");
			Assert.assertTrue(number>=min && number<=max);
			if(number == 1.0) {
				contador[0]++;
			} else if (number==1.25) {
				contador[1]++;
			} else if (number==1.5) {
				contador[2]++;
			} else if (number==1.75) {
				contador[3]++;
			} else if (number==2.0) {
				contador[4]++;
			}
		}
		int numeventos = contador[0] + contador[1] + contador[2] + contador[3] + contador[4];
		Assert.assertEquals(200, numeventos);
		for(int  i=0; i<5; i++) {
			System.out.println(contador[i]);
		}
	}
	
	@Test
	public void testRandomDoubleByIncrements_NoExactMatch() {
		LOG.debug("testRandomDoubleByIncrementsWithNegativeNumbers()");
		double min= 1.0;
		double max = 2.2;
		double increment = 0.25;
		int[] contador = new int[6];
		for(int i=0; i<5; i++) {
			contador[i] = 0;
		}
		for (int i=0;i<200;i++) {
			double number = RandomFactory.randomDoubleByIncrements(min, max, increment);
			//System.out.print(number + " ");
			Assert.assertTrue(number>=min && number<=max);
			if(number == 1.0) {
				contador[0]++;
			} else if (number==1.25) {
				contador[1]++;
			} else if (number==1.5) {
				contador[2]++;
			} else if (number==1.75) {
				contador[3]++;
			} else if (number==2.0) {
				contador[4]++;
			} else if (number>=2.0) {
				contador[5]++;
			}
		}
		int numeventos = contador[0] + contador[1] + contador[2] + contador[3] + contador[4] + contador[5];
		Assert.assertEquals(200, numeventos);
		Assert.assertEquals(0, contador[5]);
		for(int  i=0; i<6; i++) {
			System.out.println(contador[i]);
		}

		min= 1.0;
		max = 2.2;
		increment = 0.5;
		contador = new int[4];
		for(int i=0; i<4; i++) {
			contador[i] = 0;
		}
		for (int i=0;i<200;i++) {
			double number = RandomFactory.randomDoubleByIncrements(min, max, increment);
			//System.out.print(number + " ");
			Assert.assertTrue(number>=min && number<=max);
			if(number == 1.0) {
				contador[0]++;
			} else if (number==1.5) {
				contador[1]++;
			} else if (number==2.0) {
				contador[2]++;
			} else if (number>=2.0) {
				contador[3]++;
			}
		}
		numeventos = contador[0] + contador[1] + contador[2] + contador[3];
		Assert.assertEquals(200, numeventos);
		Assert.assertEquals(0, contador[3]);
		for(int  i=0; i<4; i++) {
			System.out.println(contador[i]);
		}

	}

	// DesignSpeed
	@Test
	public void testRandomDesignSpeed() {
		LOG.debug("testRandomDesigSpeed()");
		int[] vals = new int[DesignSpeed.values().length];
		for(int i=0; i<vals.length; i++) {
			vals[i]=0;
		}
		for(int i=0; i<100;i++) {
			DesignSpeed ds = RandomFactory.randomDesignSpeed();
			int value = ds.ordinal();
			vals[value]++;
		}
		for(int i=0; i<vals.length; i++) {
			Assert.assertTrue(vals[i]>0);
			// LOG.debug("ocurrencias de " + DesignSpeed.values()[i] + ": " + vals[i]);
		}
	}
}
