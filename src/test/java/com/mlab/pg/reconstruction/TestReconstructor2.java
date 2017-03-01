package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.random.RandomProfileGenerator;
import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.VerticalCurveAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.Parabole;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class TestReconstructor2 {
	
	Logger LOG = Logger.getLogger(getClass());
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}

	@Test
	public void test() {
		LOG.debug("test()");
		RandomProfileGenerator generator = new RandomProfileGenerator();
		VerticalProfile vprofile = generator.createRandomProfile();
		System.out.println(vprofile);
		VerticalGradeProfile gprofile = vprofile.derivative();
		double starts = 0.0;
		double ends= gprofile.getEndS();
		XYVectorFunction gfunction = gprofile.getSample(starts, ends, 5, true);
		Reconstructor2 reconstructor = null;
		try {
			reconstructor = new Reconstructor2(gfunction, 2, 1e-6, 1000.0);
		} catch (Exception e) {
			LOG.error("Error creating Reconstructor2");
			Assert.fail();
		}
		VerticalProfile resultVProfile = reconstructor.getVerticalProfile();
		System.out.println(resultVProfile);
	}


	
	public VerticalProfile getSampleVerticalProfile_Type_I() {
		LOG.debug("test_VProfile_Type_I");
		Straight r = new Straight(1000.0, 0.085);
		GradeAlignment grade1 = new GradeAlignment(r, 0.0, 964.3);
		Parabole p = new Parabole(928.811801, 0.232647, -0.000077);
		VerticalCurveAlignment vc = new VerticalCurveAlignment(p, 964.3, 2139.9);
		Straight r2 = new Straight(1279.377820, -0.095);
		GradeAlignment grade2 = new GradeAlignment(r2, 2139.9, 3064.0);
		VerticalProfile vp = new VerticalProfile();
		vp.add(grade1);
		vp.add(vc);
		vp.add(grade2);
		return vp;
		
	}
}
