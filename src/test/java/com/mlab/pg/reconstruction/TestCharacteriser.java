package com.mlab.pg.reconstruction;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.random.RandomFactory;
import com.mlab.pg.valign.GradeAlign;
import com.mlab.pg.valign.GradeProfile;
import com.mlab.pg.valign.GradeProfileAlign;
import com.mlab.pg.valign.VAlign;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.XYVectorFunction;

import junit.framework.Assert;

public class TestCharacteriser {
	
	private final static Logger LOG = Logger.getLogger(TestCharacteriser.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}

	@Test
	public void testGradeAlign() {
		LOG.debug("testGradeAlign()");
		DesignSpeed dspeed = DesignSpeed.DS120;
		GradeAlign grade = RandomFactory.randomGradeAlign(dspeed, 100.0, 1000.0);
		System.out.println(VAlign.CABECERA);
		System.out.println(grade.toString());
		
		GradeProfileAlign galign = grade.derivative();
		System.out.println(VAlign.CABECERA);
		System.out.println(galign.toString());
	
		XYVectorFunction gpsample = galign.getSample(galign.getStartS(), galign.getEndS(), 10);
		System.out.println(gpsample.size());
		int mobileBaseSize = 5;
		double thresholdSlope = 1e-5;
		
		Characteriser characteriser = new Characteriser();
		List<PointType> types = characteriser.characterise(gpsample, mobileBaseSize, thresholdSlope);
		Assert.assertNotNull(types);
		
		for (int i=0; i<mobileBaseSize-1; i++) {
			Assert.assertEquals(PointType.NULL, types.get(i));
		}
		for (int i=mobileBaseSize-1; i<=types.size()-mobileBaseSize; i++) {
			Assert.assertEquals(PointType.GRADE, types.get(i));
		}
		for (int i=types.size()-mobileBaseSize+1; i<types.size(); i++) {
			Assert.assertEquals(PointType.NULL, types.get(i));
		}
		
		
		
	}
}
