package com.mlab.pg.reconstruction;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.xyfunction.XYVectorFunction;

import junit.framework.Assert;

public class TestPointCharacteriser {

	private static Logger LOG = Logger.getLogger(TestPointCharacteriser.class);
	
	@BeforeClass
	public static void beforeClass() {
		PropertyConfigurator.configure("log4j.properties");	
	}
	
	@Test
	public void testGradePoint() {
		LOG.debug("testGradePoint()");
		List<double[]> pts = new ArrayList<double[]>();
		pts.add(new double[] {0.0, 0.04});
		pts.add(new double[] {2.0, 0.04});
		pts.add(new double[] {4.0, 0.04});
		pts.add(new double[] {6.0, 0.04});
		pts.add(new double[] {8.0, 0.04});
		
		XYVectorFunction gp = new XYVectorFunction(pts);
		
		PointCharacteriser ch = new PointCharacteriser();
		PointType type = ch.characterise(gp, 2, 3, 1e-5);
		Assert.assertTrue(type.equals(PointType.GRADE));
	}
	@Test
	public void testVerticalCurvePoint() {
		LOG.debug("testVerticalCurvePoint()");
		List<double[]> pts = new ArrayList<double[]>();
		pts.add(new double[] {0.0, 0.00});
		pts.add(new double[] {2.0, 2.5e-4});
		pts.add(new double[] {4.0, 5e-4});
		pts.add(new double[] {6.0, 7.5e-4});
		pts.add(new double[] {8.0, 0.001});
		
		XYVectorFunction gp = new XYVectorFunction(pts);
		
		PointCharacteriser ch = new PointCharacteriser();
		PointType type = ch.characterise(gp, 2, 3, 1e-5);
		Assert.assertTrue(type.equals(PointType.VERTICAL_CURVE));
	}
	@Test
	public void testVerticalCurveToVerticalCurvePoint() {
		LOG.debug("testVerticalCurveToVerticalCurvePoint()");
		List<double[]> pts = new ArrayList<double[]>();
		pts.add(new double[] {0.0, 0.00});
		pts.add(new double[] {2.0, 2.5e-4});
		pts.add(new double[] {4.0, 5e-4});
		pts.add(new double[] {6.0, 7.5e-4});
		pts.add(new double[] {8.0, 5e-4});
		pts.add(new double[] {10.0, 2.5e-4});
		pts.add(new double[] {12.0, 0.0});
		
		XYVectorFunction gp = new XYVectorFunction(pts);
		
		PointCharacteriser ch = new PointCharacteriser();
		PointType type = ch.characterise(gp, 2, 3, 1e-5);
		Assert.assertTrue(type.equals(PointType.BORDER_POINT));
	}

	@Test
	public void testVerticalCurveEndPoint() {
		LOG.debug("testVerticalCurveEndPoint()");
		List<double[]> pts = new ArrayList<double[]>();
		pts.add(new double[] {0.0, 0.04075});
		pts.add(new double[] {2.0, 0.04050});
		pts.add(new double[] {4.0, 0.04025});
		pts.add(new double[] {6.0, 0.04});
		pts.add(new double[] {8.0, 0.04});
		pts.add(new double[] {10.0, 0.04});
		pts.add(new double[] {12.0, 0.04});
		
		XYVectorFunction gp = new XYVectorFunction(pts);
		
		PointCharacteriser ch = new PointCharacteriser();
		PointType type = ch.characterise(gp, 3, 3, 1e-5);
		Assert.assertTrue(type.equals(PointType.BORDER_POINT));
	}


	@Test
	public void testAnglesBetweenStraights() {
		LOG.debug("testAnglesBetweenStraights()");
		// TODO Incompleto, dejado  medias
		List<double[]> pts = new ArrayList<double[]>();
		pts.add(new double[] {0.0, 0.04075});
		pts.add(new double[] {2.0, 0.04075});
		pts.add(new double[] {4.0, 0.04075});
		pts.add(new double[] {6.0, 0.04075});
		pts.add(new double[] {8.0, 0.04075});
		pts.add(new double[] {10.0, 0.04075});
		pts.add(new double[] {12.0, 0.04075});
		pts.add(new double[] {14.0, 0.04075});
		pts.add(new double[] {16.0, 0.04075});
		pts.add(new double[] {18.0, 0.04075});
		
		pts.add(new double[] {20.0, 0.04075}); // i=10
		
		pts.add(new double[] {22.0, 0.04050});
		pts.add(new double[] {24.0, 0.04025});
		pts.add(new double[] {26.0, 0.04});
		pts.add(new double[] {28.0, 0.03975});
		pts.add(new double[] {30.0, 0.03950});
		pts.add(new double[] {32.0, 0.03925});
		pts.add(new double[] {34.0, 0.03900});
		pts.add(new double[] {36.0, 0.038750});
		
		XYVectorFunction gp = new XYVectorFunction(pts);
		
		
	}
}
