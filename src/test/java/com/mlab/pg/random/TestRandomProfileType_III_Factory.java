package com.mlab.pg.random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.VerticalCurveAlignment;
import com.mlab.pg.valign.VerticalProfile;

public class TestRandomProfileType_III_Factory {
	
	static Logger LOG = Logger.getLogger(TestRandomProfileType_III_Factory.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}


	@Test
	public void testRandomProfileType_III() {
		LOG.debug("testRandomProfileType_III()");
		for(int i=0; i<1000; i++) {
			RandomProfileFactory factory = new RandomProfileType_III_Factory();
			VerticalProfile vp = factory.createRandomProfile();
			Assert.assertNotNull(vp);
			Assert.assertEquals(3, vp.size());
	
			GradeAlignment grade1 = (GradeAlignment)vp.getAlign(0);
			Assert.assertNotNull(grade1);
			Assert.assertTrue(grade1.getClass().isAssignableFrom(GradeAlignment.class));
			Assert.assertEquals(factory.getS0(), grade1.getStartS(), 0.001);
			Assert.assertEquals(factory.getZ0(), grade1.getStartZ(), 0.001);
			Assert.assertTrue(grade1.getLength() >= factory.getMinGradeLength());
			Assert.assertTrue(grade1.getLength() <= factory.getMaxGradeLength());		
			Assert.assertTrue(grade1.getSlope() < 0);
			Assert.assertTrue(Math.abs(grade1.getSlope()) >= factory.getMinSlope());
			Assert.assertTrue(Math.abs(grade1.getSlope()) <= factory.getMaxSlope());
			
			VerticalCurveAlignment vc = (VerticalCurveAlignment)vp.getAlign(1);
			Assert.assertNotNull(vc);
			Assert.assertTrue(vc.getClass().isAssignableFrom(VerticalCurveAlignment.class));
			Assert.assertEquals(grade1.getEndS(), vc.getStartS(), 0.001);
			Assert.assertEquals(grade1.getEndZ(), vc.getStartZ(), 0.001);
			Assert.assertEquals(grade1.getEndTangent(), vc.getStartTangent(), 0.001);
			Assert.assertTrue(vc.getLength() > 0);
			double length = Math.rint(vc.getLength()*10.0)/10.0;
			if(length <  factory.getMinVerticalCurveLength()) {
				System.out.println(vc.getLength());
				System.out.println(factory.getMinVerticalCurveLength());
			}
			Assert.assertTrue(length >=  factory.getMinVerticalCurveLength());
			if(length >  factory.getMaxVerticalCurveLength()) {
				System.out.println(vc.getLength());
				System.out.println(factory.getMaxVerticalCurveLength());
			}
			Assert.assertTrue(length <=  factory.getMaxVerticalCurveLength());
			Assert.assertTrue(Math.abs(vc.getKv()) >= factory.getMinKv());
			Assert.assertTrue(Math.abs(vc.getKv()) <= factory.getMaxKv());
			double endtangent = Math.rint(vc.getStartTangent()*1000.0) / 1000.0;
			Assert.assertTrue(Math.abs(endtangent) <= factory.getMaxSlope());
			Assert.assertTrue(Math.abs(endtangent) >= factory.getMinSlope());
			
			GradeAlignment grade2 = (GradeAlignment) vp.getAlign(2);
			Assert.assertNotNull(grade2);
			Assert.assertTrue(grade1.getClass().isAssignableFrom(GradeAlignment.class));
			Assert.assertEquals(vc.getEndS(), grade2.getStartS(), 0.001);
			Assert.assertEquals(vc.getEndZ(), grade2.getStartZ(), 0.001);
			Assert.assertEquals(vc.getEndTangent(), grade2.getSlope(), 0.001);		
			length = Math.rint(grade2.getLength()*10.0)/10.0;
			if(length < factory.getMinGradeLength()) {
				System.out.println(grade2.getLength() + ", " + factory.getMinGradeLength());
			}
			Assert.assertTrue(length >= factory.getMinGradeLength());
			if(length > factory.getMaxGradeLength()) {
				System.out.println(length + ", " + factory.getMaxGradeLength());
			}
			Assert.assertTrue(grade2.getLength() <= factory.getMaxGradeLength());		
			Assert.assertTrue(grade2.getSlope() > 0);
			//System.out.println(grade2.getSlope() + ", " + factory.getMinSlope() + ", " + factory.getMaxSlope());
			double slope = Math.rint(grade2.getSlope()*1000)/1000;
			//System.out.println(slope + ", " + grade2.getSlope() + ", " + factory.getMinSlope() + ", " + factory.getMaxSlope());
			Assert.assertTrue(Math.abs(slope) >= factory.getMinSlope());
			Assert.assertTrue(Math.abs(slope) <= factory.getMaxSlope());
		}
	}
}
