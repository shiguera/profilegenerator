package com.mlab.pg.valign;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.random.RandomProfileFactory;
import com.mlab.pg.random.RandomProfileType_III_Factory;
import com.mlab.pg.random.RandomProfileType_I_Factory;
import com.mlab.pg.xyfunction.Parabole;
import com.mlab.pg.xyfunction.Straight;



public class TestGradeProfileAlignment {

	private static final Logger LOG = Logger.getLogger(TestGradeProfileAlignment.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	
	@Test
	public void testIntegrateWithGradeAlignment() {
		LOG.debug("testIntegrateWithGradeAlignment");
		double s0 = 0.0;
		double z0 = 0.0;
		double g0 = 0.03;
		double s1 = 1000.0;
		Straight r = new Straight(s0, g0, 0.0);
		GradeProfileAlignment galignment = new GradeProfileAlignment(r, s0, s1);
		VAlignment valignment = galignment.integrate(z0, 1e-6);
		Assert.assertEquals(galignment.getStartS(), valignment.getStartS(), 0.001);
		Assert.assertEquals(galignment.getEndS(), valignment.getEndS(), 0.001);
		Assert.assertEquals(galignment.getStartZ(), valignment.getStartTangent(), 0.001);
		Assert.assertEquals(galignment.getEndZ(), valignment.getEndTangent(), 0.001);	
	}
	@Test
	public void testZWhenIntegrate() {
		LOG.debug("testZWhenIntegrate()");
		for(int i=0; i<1000; i++) {
			System.out.println(i);
			RandomProfileFactory factory = new RandomProfileType_I_Factory();
			VerticalProfile originalVProfile = factory.createRandomProfile();
			VerticalGradeProfile gprofile = originalVProfile.derivative();
			
			double z0 = originalVProfile.getAlign(0).getStartZ();
			VerticalProfile resultVProfile = gprofile.integrate(z0, 1e-6);
	
			VAlignment valign = resultVProfile.getAlign(0);
			GradeAlignment galign = gprofile.getAlign(0);
			Assert.assertNotNull(valign);
			Assert.assertEquals(galign.getStartS(), valign.getStartS(), 0.001);
			Assert.assertEquals(galign.getEndS(), valign.getEndS(), 0.001);
			Assert.assertEquals(galign.getStartZ(), valign.getStartTangent(), 0.001);
			Assert.assertEquals(galign.getEndZ(), valign.getEndTangent(), 0.001);
			Assert.assertEquals(valign.getStartZ(), originalVProfile.getAlign(0).getStartZ(), 0.001);
			Assert.assertEquals(valign.getEndZ(), originalVProfile.getAlign(0).getEndZ(), 0.001);
			
	
			valign = resultVProfile.getAlign(1);
			galign = gprofile.getAlign(1);
			Assert.assertNotNull(valign);
			Assert.assertEquals(galign.getStartS(), valign.getStartS(), 0.001);
			Assert.assertEquals(galign.getEndS(), valign.getEndS(), 0.001);
			Assert.assertEquals(galign.getStartZ(), valign.getStartTangent(), 0.001);
			Assert.assertEquals(galign.getEndZ(), valign.getEndTangent(), 0.001);
			Assert.assertEquals(valign.getStartZ(), originalVProfile.getAlign(1).getStartZ(), 0.001);
			Assert.assertEquals(valign.getEndZ(), originalVProfile.getAlign(1).getEndZ(), 0.001);
			
			valign = resultVProfile.getAlign(2);
			galign = gprofile.getAlign(2);
			Assert.assertNotNull(valign);
			Assert.assertEquals(galign.getStartS(), valign.getStartS(), 0.001);
			Assert.assertEquals(galign.getEndS(), valign.getEndS(), 0.001);
			Assert.assertEquals(galign.getStartZ(), valign.getStartTangent(), 0.001);
			Assert.assertEquals(galign.getEndZ(), valign.getEndTangent(), 0.001);
			Assert.assertEquals(valign.getStartZ(), originalVProfile.getAlign(2).getStartZ(), 0.001);
			Assert.assertEquals(valign.getEndZ(), originalVProfile.getAlign(2).getEndZ(), 0.001);
			
			factory = new RandomProfileType_III_Factory();
			originalVProfile = factory.createRandomProfile();
			gprofile = originalVProfile.derivative();
			z0 = originalVProfile.getAlign(0).getStartZ();
			resultVProfile = gprofile.integrate(z0, 1e-6);
			
			valign = resultVProfile.getAlign(0);
			galign = gprofile.getAlign(0);
			Assert.assertNotNull(valign);
			Assert.assertEquals(galign.getStartS(), valign.getStartS(), 0.001);
			Assert.assertEquals(galign.getEndS(), valign.getEndS(), 0.001);
			Assert.assertEquals(galign.getStartZ(), valign.getStartTangent(), 0.001);
			Assert.assertEquals(galign.getEndZ(), valign.getEndTangent(), 0.001);
			Assert.assertEquals(valign.getStartZ(), originalVProfile.getAlign(0).getStartZ(), 0.001);
			Assert.assertEquals(valign.getEndZ(), originalVProfile.getAlign(0).getEndZ(), 0.001);
			
	
			valign = resultVProfile.getAlign(1);
			galign = gprofile.getAlign(1);
			Assert.assertNotNull(valign);
			Assert.assertEquals(galign.getStartS(), valign.getStartS(), 0.001);
			Assert.assertEquals(galign.getEndS(), valign.getEndS(), 0.001);
			Assert.assertEquals(galign.getStartZ(), valign.getStartTangent(), 0.001);
			Assert.assertEquals(galign.getEndZ(), valign.getEndTangent(), 0.001);
			Assert.assertEquals(valign.getStartZ(), originalVProfile.getAlign(1).getStartZ(), 0.001);
			Assert.assertEquals(valign.getEndZ(), originalVProfile.getAlign(1).getEndZ(), 0.001);
			
			valign = resultVProfile.getAlign(2);
			galign = gprofile.getAlign(2);
			Assert.assertNotNull(valign);
			Assert.assertEquals(galign.getStartS(), valign.getStartS(), 0.001);
			Assert.assertEquals(galign.getEndS(), valign.getEndS(), 0.001);
			Assert.assertEquals(galign.getStartZ(), valign.getStartTangent(), 0.001);
			Assert.assertEquals(galign.getEndZ(), valign.getEndTangent(), 0.001);
			Assert.assertEquals(valign.getStartZ(), originalVProfile.getAlign(2).getStartZ(), 0.001);
			Assert.assertEquals(valign.getEndZ(), originalVProfile.getAlign(2).getEndZ(), 0.001);

		}
	}

	@Test
	public void testIntegrateWithRandomAlignments() {
		LOG.debug("testIntegrateWithRandomAlignments()");
		for(int i=0; i<1000; i++) {
			RandomProfileFactory factory = new RandomProfileType_I_Factory();
			VerticalProfile profile = factory.createRandomProfile();
			VerticalGradeProfile gprofile = profile.derivative();
			
			double z0 = profile.getAlign(0).getStartZ();
			VerticalProfile resultProfile = gprofile.integrate(z0, 1e-6);

			VAlignment align = resultProfile.getAlign(0);
			GradeAlignment galign = gprofile.getAlign(0);
			Assert.assertNotNull(align);
			Assert.assertEquals(galign.getStartS(), align.getStartS(), 0.001);
			Assert.assertEquals(galign.getEndS(), align.getEndS(), 0.001);
			Assert.assertEquals(galign.getStartZ(), align.getStartTangent(), 0.001);
			Assert.assertEquals(galign.getEndZ(), align.getEndTangent(), 0.001);
			Assert.assertEquals(align.getStartZ(), profile.getAlign(0).getStartZ(), 0.001);

			align = resultProfile.getAlign(1);
			galign = gprofile.getAlign(1);
			Assert.assertNotNull(align);
			Assert.assertEquals(galign.getStartS(), align.getStartS(), 0.001);
			Assert.assertEquals(galign.getEndS(), align.getEndS(), 0.001);
			Assert.assertEquals(galign.getStartZ(), align.getStartTangent(), 0.001);
			Assert.assertEquals(galign.getEndZ(), align.getEndTangent(), 0.001);
			Assert.assertEquals(align.getStartZ(), profile.getAlign(1).getStartZ(), 0.001);

			align = resultProfile.getAlign(2);
			galign = gprofile.getAlign(2);
			Assert.assertNotNull(align);
			Assert.assertEquals(galign.getStartS(), align.getStartS(), 0.001);
			Assert.assertEquals(galign.getEndS(), align.getEndS(), 0.001);
			Assert.assertEquals(galign.getStartZ(), align.getStartTangent(), 0.001);
			Assert.assertEquals(galign.getEndZ(), align.getEndTangent(), 0.001);
			Assert.assertEquals(align.getStartZ(), profile.getAlign(2).getStartZ(), 0.001);

			factory = new RandomProfileType_III_Factory();
			profile = factory.createRandomProfile();
			gprofile = profile.derivative();
			
			z0 = profile.getAlign(0).getStartZ();
			resultProfile = gprofile.integrate(z0, 1e-6);

			align = resultProfile.getAlign(0);
			galign = gprofile.getAlign(0);
			Assert.assertNotNull(align);
			Assert.assertEquals(galign.getStartS(), align.getStartS(), 0.001);
			Assert.assertEquals(galign.getEndS(), align.getEndS(), 0.001);
			Assert.assertEquals(galign.getStartZ(), align.getStartTangent(), 0.001);
			Assert.assertEquals(galign.getEndZ(), align.getEndTangent(), 0.001);
			Assert.assertEquals(align.getStartZ(), profile.getAlign(0).getStartZ(), 0.001);

			align = resultProfile.getAlign(1);
			galign = gprofile.getAlign(1);
			Assert.assertNotNull(align);
			Assert.assertEquals(galign.getStartS(), align.getStartS(), 0.001);
			Assert.assertEquals(galign.getEndS(), align.getEndS(), 0.001);
			Assert.assertEquals(galign.getStartZ(), align.getStartTangent(), 0.001);
			Assert.assertEquals(galign.getEndZ(), align.getEndTangent(), 0.001);
			Assert.assertEquals(align.getStartZ(), profile.getAlign(1).getStartZ(), 0.001);

			align = resultProfile.getAlign(2);
			galign = gprofile.getAlign(2);
			Assert.assertNotNull(align);
			Assert.assertEquals(galign.getStartS(), align.getStartS(), 0.001);
			Assert.assertEquals(galign.getEndS(), align.getEndS(), 0.001);
			Assert.assertEquals(galign.getStartZ(), align.getStartTangent(), 0.001);
			Assert.assertEquals(galign.getEndZ(), align.getEndTangent(), 0.001);
			Assert.assertEquals(align.getStartZ(), profile.getAlign(2).getStartZ(), 0.001);

			
			
		}
		
		

	}

	@Test
	public void testGetAlign() {
		LOG.debug("testGetAlign()");
		double starts1 = 1000.0;
		double startz1 = 900.0;
		double startslope1 = 0.03;
		Straight straight1 = new Straight(starts1, startz1, startslope1);
		double ends1 = 1400.0; 
		GradeAlignment grade1 = new GradeAlignment(straight1, starts1, ends1);
		double starts2 = ends1;
		double startz2 = grade1.getEndZ();
		double startslope2 = startslope1;
		double kv = -8000.0;
		Parabole parabole = new Parabole(starts2, startz2, startslope2, kv);
		double length = 400;
		double ends2 = starts2 + length;
		VerticalCurveAlignment vc = new VerticalCurveAlignment(parabole, starts2, ends2);
		double starts3 = vc.getEndS();
		double startz3 = vc.getEndZ();
		double startslope3 = vc.getEndTangent();
		Straight straight2 = new Straight(starts3, startz3, startslope3);
		double ends3 = starts3 + 300.0;
		GradeAlignment grade2 = new GradeAlignment(straight2, starts3, ends3);

		VerticalProfile profile = new VerticalProfile();
		Assert.assertTrue(profile.add(grade1));
		Assert.assertTrue(profile.add(vc));
		Assert.assertTrue(profile.add(grade2));
		
		Assert.assertNull(profile.getAlign(0.0));
		Assert.assertEquals(grade1, profile.getAlign(1000.0));
		Assert.assertEquals(grade1, profile.getAlign(1400.0));
		Assert.assertEquals(vc, profile.getAlign(1400.5));
		Assert.assertEquals(vc, profile.getAlign(1500.0));
		Assert.assertEquals(vc, profile.getAlign(1800.0));
		Assert.assertEquals(grade2, profile.getAlign(1801.0));
		Assert.assertEquals(grade2, profile.getAlign(2100.0));
		Assert.assertNull(profile.getAlign(2101.0));
		
	}
	@Test
	public void testGetAlignIndex() {
		LOG.debug("testGetAlignIndex()");
		//DesignSpeed dspeed = DesignSpeed.DS120;
		double starts1 = 1000.0;
		double startz1 = 900.0;
		double startslope1 = 0.03;
		Straight straight1 = new Straight(starts1, startz1, startslope1);
		double ends1 = 1400.0; 
		GradeAlignment grade1 = new GradeAlignment(straight1, starts1, ends1);
		double starts2 = ends1;
		double startz2 = grade1.getEndZ();
		double startslope2 = startslope1;
		double kv = -8000.0;
		Parabole parabole = new Parabole(starts2, startz2, startslope2, kv);
		double length = 400;
		double ends2 = starts2 + length;
		VerticalCurveAlignment vc = new VerticalCurveAlignment(parabole, starts2, ends2);
		double starts3 = vc.getEndS();
		double startz3 = vc.getEndZ();
		double startslope3 = vc.getEndTangent();
		Straight straight2 = new Straight(starts3, startz3, startslope3);
		double ends3 = starts3 + 300.0;
		GradeAlignment grade2 = new GradeAlignment(straight2, starts3, ends3);

		VerticalProfile profile = new VerticalProfile();
		Assert.assertTrue(profile.add(grade1));
		Assert.assertTrue(profile.add(vc));
		Assert.assertTrue(profile.add(grade2));
		
		Assert.assertEquals(-1, profile.getAlignIndex(0.0));
		Assert.assertEquals(0, profile.getAlignIndex(1000.0));
		Assert.assertEquals(0, profile.getAlignIndex(1400.0));
		Assert.assertEquals(1, profile.getAlignIndex(1400.5));
		Assert.assertEquals(1, profile.getAlignIndex(1500.0));
		Assert.assertEquals(1, profile.getAlignIndex(1800.0));
		Assert.assertEquals(2, profile.getAlignIndex(1801.0));
		Assert.assertEquals(2, profile.getAlignIndex(2100.0));
		Assert.assertEquals(-1, profile.getAlignIndex(2101.0));
		
	}
}
