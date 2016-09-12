package com.mlab.pg.random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.VerticalCurveAlignment;
import com.mlab.pg.valign.VerticalProfile;

public class TestRandomProfileType_V_Factory {
	
	static Logger LOG = Logger.getLogger(TestRandomProfileType_V_Factory.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}

	@Test
	public void testRandomProfileType_V() {
		LOG.debug("testRandomProfileType_V()");
		RandomProfileFactory factory = new RandomProfileType_V_Factory();
		for(int i=0; i<1000; i++) {
			VerticalProfile vp = factory.createRandomProfile();
			Assert.assertNotNull(vp);
			Assert.assertEquals(4, vp.size());
			//System.out.println(vp);
	
			GradeAlignment grade1 = (GradeAlignment)vp.getAlign(0);
			Assert.assertNotNull(grade1);
			Assert.assertTrue(grade1.getClass().isAssignableFrom(GradeAlignment.class));
			Assert.assertEquals(factory.getS0(), grade1.getStartS(), 0.001);
			Assert.assertEquals(factory.getZ0(), grade1.getStartZ(), 0.001);
			Assert.assertTrue(grade1.getLength() >= factory.getMinGradeLength());
			Assert.assertTrue(grade1.getLength() <= factory.getMaxGradeLength());		
			Assert.assertTrue(grade1.getSlope()>0);
			Assert.assertTrue(grade1.getSlope() >= factory.getMinSlope());
			Assert.assertTrue(grade1.getSlope() <= factory.getMaxSlope());
			
			VerticalCurveAlignment vc1 = (VerticalCurveAlignment)vp.getAlign(1);
			Assert.assertNotNull(vc1);
			Assert.assertTrue(vc1.getClass().isAssignableFrom(VerticalCurveAlignment.class));
			Assert.assertEquals(grade1.getEndS(), vc1.getStartS(), 0.001);
			Assert.assertEquals(grade1.getEndZ(), vc1.getStartZ(), 0.001);
			Assert.assertEquals(grade1.getEndTangent(), vc1.getStartTangent(), 0.001);
			Assert.assertTrue(vc1.getLength() > 0);
			Assert.assertTrue(vc1.getLength() <=  factory.getMaxVerticalCurveLength());
			Assert.assertTrue(Math.abs(vc1.getKv()) >= factory.getMinKv());
			Assert.assertTrue(Math.abs(vc1.getKv()) <= factory.getMaxKv());
			Assert.assertTrue(Math.abs(vc1.getEndTangent()) <= factory.getMaxSlope());
			Assert.assertTrue(Math.abs(vc1.getEndTangent()) >= factory.getMinSlope());
	
			VerticalCurveAlignment vc2 = (VerticalCurveAlignment)vp.getAlign(2);
			Assert.assertNotNull(vc2);
			Assert.assertTrue(vc2.getClass().isAssignableFrom(VerticalCurveAlignment.class));
			Assert.assertEquals(vc1.getEndS(), vc2.getStartS(), 0.001);
			Assert.assertEquals(vc1.getEndZ(), vc2.getStartZ(), 0.001);
			Assert.assertEquals(vc1.getEndTangent(), vc2.getStartTangent(), 0.001);
			Assert.assertTrue(vc2.getLength() > 0);
			double length = Math.rint(vc2.getLength()*10.0)/10.0;
			Assert.assertTrue(length >=  factory.getMinVerticalCurveLength());
			Assert.assertTrue(length <=  factory.getMaxVerticalCurveLength());
			Assert.assertTrue(Math.abs(vc2.getKv()) >= factory.getMinKv());
			Assert.assertTrue(Math.abs(vc2.getKv()) <= factory.getMaxKv());
			double endtangent = Math.rint(vc2.getEndTangent()*1000.0) / 1000.0;
			Assert.assertTrue(Math.abs(endtangent) <= factory.getMaxSlope());
			Assert.assertTrue(Math.abs(endtangent) >= factory.getMinSlope());
	
			GradeAlignment grade2 = (GradeAlignment) vp.getAlign(3);
			Assert.assertNotNull(grade2);
			Assert.assertTrue(grade1.getClass().isAssignableFrom(GradeAlignment.class));
			Assert.assertEquals(vc2.getEndS(), grade2.getStartS(), 0.001);
			Assert.assertEquals(vc2.getEndZ(), grade2.getStartZ(), 0.001);
			Assert.assertEquals(vc2.getEndTangent(), grade2.getSlope(), 0.001);		
			length = Math.rint(grade2.getLength()*10.0)/10.0;
			Assert.assertTrue(length >= factory.getMinGradeLength());
			Assert.assertTrue(length <= factory.getMaxGradeLength());		
			Assert.assertTrue(grade2.getSlope() > 0);
			Assert.assertTrue(Math.abs(grade2.getSlope()) >= factory.getMinSlope());
			Assert.assertTrue(Math.abs(grade2.getSlope()) <= factory.getMaxSlope());
		}				
	}
}
