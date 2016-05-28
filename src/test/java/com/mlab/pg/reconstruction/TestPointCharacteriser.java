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
		PointType type = ch.characterise(2, gp, 3, 1e-5);
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
		PointType type = ch.characterise(2, gp, 3, 1e-5);
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
		PointType type = ch.characterise(3, gp, 3, 1e-5);
		Assert.assertTrue(type.equals(PointType.VERTICALCURVE_TO_VERTICALCURVE));
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
		PointType type = ch.characterise(3, gp, 3, 1e-5);
		Assert.assertTrue(type.equals(PointType.VERTICALCURVE_END));
	}


}
