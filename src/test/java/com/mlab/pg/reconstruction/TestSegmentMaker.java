package com.mlab.pg.reconstruction;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.xyfunction.XYVectorFunction;

import junit.framework.Assert;

public class TestSegmentMaker {

	
	private final static Logger LOG = Logger.getLogger(TestSegmentMaker.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	
	@Test
	public void testGrade() {
		LOG.debug("testGrade()");
		List<double[]> pts = new ArrayList<double[]>();
		pts.add(new double[] {0.0, 0.04});
		pts.add(new double[] {2.0, 0.04});
		pts.add(new double[] {4.0, 0.04});
		pts.add(new double[] {6.0, 0.04});
		pts.add(new double[] {8.0, 0.04});
		
		XYVectorFunction gp = new XYVectorFunction(pts);
		int mobileBaseSize = 3;
		double thresholdSlope = 1e-5;
		
		SegmentMaker maker = new SegmentMaker(gp, mobileBaseSize, thresholdSlope);
		Assert.assertNotNull(maker.getPointTypes());
		Assert.assertEquals(gp.size(), maker.getPointTypes().size());
		Assert.assertNotNull(maker.getPointTypeSegments());
		Assert.assertEquals(3, maker.getPointTypeSegments().size());
	}

	@Test
	public void testVerticalCurve() {
		LOG.debug("testVerticalCurve()");
		List<double[]> pts = new ArrayList<double[]>();
		pts.add(new double[] {0.0, 0.00});
		pts.add(new double[] {2.0, 2.5e-4});
		pts.add(new double[] {4.0, 5e-4});
		pts.add(new double[] {6.0, 7.5e-4});
		pts.add(new double[] {8.0, 0.001});
		
		XYVectorFunction gp = new XYVectorFunction(pts);
		
		int mobileBaseSize = 3;
		double thresholdSlope = 1e-5;
		
		SegmentMaker maker = new SegmentMaker(gp, mobileBaseSize, thresholdSlope);
		Assert.assertNotNull(maker.getPointTypes());
		Assert.assertEquals(gp.size(), maker.getPointTypes().size());
		Assert.assertNotNull(maker.getPointTypeSegments());
		Assert.assertEquals(3, maker.getPointTypeSegments().size());
	}
	@Test
	public void testVerticalCurveToVerticalCurve() {
		LOG.debug("testVerticalCurveToVerticalCurve()");
		List<double[]> pts = new ArrayList<double[]>();
		pts.add(new double[] {0.0, 0.00});
		pts.add(new double[] {2.0, 2.5e-4});
		pts.add(new double[] {4.0, 5e-4});
		pts.add(new double[] {6.0, 7.5e-4});
		pts.add(new double[] {8.0, 5e-4});
		pts.add(new double[] {10.0, 2.5e-4});
		pts.add(new double[] {12.0, 0.0});
		
		XYVectorFunction gp = new XYVectorFunction(pts);
		int mobileBaseSize = 3;
		double thresholdSlope = 1e-5;
		
		SegmentMaker maker = new SegmentMaker(gp, mobileBaseSize, thresholdSlope);
		Assert.assertNotNull(maker.getPointTypes());
		Assert.assertEquals(gp.size(), maker.getPointTypes().size());
		for(PointType type : maker.getPointTypes()) {
			System.out.println(type);
		}
		Assert.assertNotNull(maker.getPointTypeSegments());
		for(PointTypeSegment segment : maker.getPointTypeSegments()) {
			System.out.println(segment.toString());
		}
		Assert.assertEquals(5, maker.getPointTypeSegments().size());
	}

	@Test
	public void testVerticalCurveEnd() {
		LOG.debug("testVerticalCurveEnd()");
		List<double[]> pts = new ArrayList<double[]>();
		pts.add(new double[] {0.0, 0.04075});
		pts.add(new double[] {2.0, 0.04050});
		pts.add(new double[] {4.0, 0.04025});
		pts.add(new double[] {6.0, 0.04});
		pts.add(new double[] {8.0, 0.04});
		pts.add(new double[] {10.0, 0.04});
		pts.add(new double[] {12.0, 0.04});
		
		XYVectorFunction gp = new XYVectorFunction(pts);
		int mobileBaseSize = 3;
		double thresholdSlope = 1e-5;
		
		SegmentMaker maker = new SegmentMaker(gp, mobileBaseSize, thresholdSlope);
		Assert.assertNotNull(maker.getPointTypes());
		Assert.assertEquals(gp.size(), maker.getPointTypes().size());
		for(PointType type : maker.getPointTypes()) {
			System.out.println(type);
		}
		Assert.assertNotNull(maker.getPointTypeSegments());
		for(PointTypeSegment segment : maker.getPointTypeSegments()) {
			System.out.println(segment.toString());
		}
		Assert.assertEquals(4, maker.getPointTypeSegments().size());	}


}
