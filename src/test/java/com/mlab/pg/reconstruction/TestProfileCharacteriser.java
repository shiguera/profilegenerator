package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.random.RandomFactory;
import com.mlab.pg.random.RandomGradeFactory;
import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.GradeProfileAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.XYVectorFunction;

import junit.framework.Assert;

public class TestProfileCharacteriser {
	
	private final static Logger LOG = Logger.getLogger(TestProfileCharacteriser.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}

	/**
	 * Se genera un GradeAlign aleatorio. Se genera el GradeProfileAlign utilizando
	 * el método derivative() de la clase GradeAlign, se extrae una muestra XYVectorFunction
	 * y se analiza con el ProfileCharacteriser. 
	 * Se comprueba que todos los puntos, salvo los iniciales y finales, son del tipo GRADE  
	 */
	@Test
	public void testGradeAlignment() {
		LOG.debug("testGradeAlignment()");
		DesignSpeed dspeed = RandomFactory.randomDesignSpeed();
		GradeAlignment grade = RandomGradeFactory.randomGradeAlignment(dspeed, 100.0, 1000.0);
		//System.out.println(VAlignment.CABECERA);
		//System.out.println(grade.toString());
		
		GradeProfileAlignment galign = grade.derivative();
		//System.out.println(VAlignment.CABECERA);
		//System.out.println(galign.toString());
	
		XYVectorFunction gpsample = galign.getSample(galign.getStartS(), galign.getEndS(), 10);
		//System.out.println(gpsample.size());
		int mobileBaseSize = 5;
		double thresholdSlope = 1e-5;
		
		ProfileCharacteriser characteriser = new ProfileCharacteriser();
		PointTypeArray types = characteriser.characterise(gpsample, mobileBaseSize, thresholdSlope);
		Assert.assertNotNull(types);
		Assert.assertEquals(gpsample.size(), types.size());
		for (int i=0; i<mobileBaseSize-1; i++) {
			Assert.assertEquals(PointType.GRADE, types.get(i));
		}
		for (int i=mobileBaseSize-1; i<=types.size()-mobileBaseSize; i++) {
			Assert.assertEquals(PointType.GRADE, types.get(i));
		}
		for (int i=types.size()-mobileBaseSize+1; i<types.size(); i++) {
			Assert.assertEquals(PointType.GRADE, types.get(i));
		}
		
		
		
	}

	@Test
	public void testVerticalCurveAlignment() {
		LOG.debug("testVerticalCurveAlignment()");
		DesignSpeed dspeed = DesignSpeed.DS120;
		double s0 = 100.0;
		double z0 = 1000.0;
		VerticalProfile verticalprofile = RandomFactory.randomVerticalProfileType_I(dspeed, s0, z0);
		//System.out.println(verticalprofile.toString());
		VerticalGradeProfile gradeProfile = verticalprofile.derivative();
		XYVectorFunction sample =gradeProfile.getSample(s0, gradeProfile.getEndS(), 5, true);
		ProfileCharacteriser characteriser = new ProfileCharacteriser();
		PointTypeArray types = characteriser.characterise(sample, 3, 1e-6);
		Assert.assertNotNull(types);
		Assert.assertEquals(sample.size(), types.size());
		for(int i=0; i<types.size(); i++) {
			Assert.assertTrue(types.get(i) != PointType.NULL);
		}
	}
}
