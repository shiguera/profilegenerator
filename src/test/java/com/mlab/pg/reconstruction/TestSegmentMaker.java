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
		pts.add(new double[] {10.0, 0.04});
		pts.add(new double[] {12.0, 0.04});
		pts.add(new double[] {14.0, 0.04});
		pts.add(new double[] {16.0, 0.04});
		
		
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
		pts.add(new double[] {10.0, 0.00125});
		pts.add(new double[] {12.0, 0.0015});
		pts.add(new double[] {14.0, 0.00175});
		
		
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
		pts.add(new double[] {8.0, 1.0e-3});
		pts.add(new double[] {10.0, 1.25e-3});
		pts.add(new double[] {12.0, 1.0e-3});
		pts.add(new double[] {14.0, 7.5e-4});
		pts.add(new double[] {16.0, 5e-4});
		pts.add(new double[] {18.0, 2.5e-4});
		pts.add(new double[] {20.0, 0.0});
		
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
		Assert.assertEquals(7, maker.getPointTypeSegments().size());
	}

	@Test
	public void testVerticalCurveEnd() {
		LOG.debug("testVerticalCurveEnd()");
		List<double[]> pts = new ArrayList<double[]>();
		pts.add(new double[] {0.0, 0.04075});
		pts.add(new double[] {2.0, 0.04050});
		pts.add(new double[] {4.0, 0.04025});
		pts.add(new double[] {6.0, 0.04});
		pts.add(new double[] {8.0, 0.03975});
		pts.add(new double[] {10.0, 0.03950});
		pts.add(new double[] {12.0, 0.03925});
		pts.add(new double[] {14.0, 0.03900});
		
		pts.add(new double[] {16.0, 0.038750});
		
		pts.add(new double[] {18.0, 0.038750});
		pts.add(new double[] {20.0, 0.038750});
		pts.add(new double[] {22.0, 0.038750});
		pts.add(new double[] {24.0, 0.038750});
		pts.add(new double[] {26.0, 0.038750});
		pts.add(new double[] {28.0, 0.038750});
		pts.add(new double[] {30.0, 0.038750});
		
		
		XYVectorFunction gp = new XYVectorFunction(pts);
		int mobileBaseSize = 3;
		double thresholdSlope = 1e-5;
		
		SegmentMaker maker = new SegmentMaker(gp, mobileBaseSize, thresholdSlope);
		Assert.assertNotNull(maker.getPointTypes());
		Assert.assertEquals(gp.size(), maker.getPointTypes().size());
		Assert.assertTrue(maker.getPointTypes().get(8)==PointType.VERTICALCURVE_END);
		for(PointType type : maker.getPointTypes()) {
			System.out.println(type);
		}
		Assert.assertNotNull(maker.getPointTypeSegments());
		for(PointTypeSegment segment : maker.getPointTypeSegments()) {
			System.out.println(segment.toString());
		}
		Assert.assertEquals(6, maker.getPointTypeSegments().size());		
	}

	@Test
	public void testVerticalCurveBeginning() {
		LOG.debug("testVerticalCurveBeginning()");
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
		
		pts.add(new double[] {20.0, 0.04075});
		
		pts.add(new double[] {22.0, 0.04050});
		pts.add(new double[] {24.0, 0.04025});
		pts.add(new double[] {26.0, 0.04});
		pts.add(new double[] {28.0, 0.03975});
		pts.add(new double[] {30.0, 0.03950});
		pts.add(new double[] {32.0, 0.03925});
		pts.add(new double[] {34.0, 0.03900});
		pts.add(new double[] {36.0, 0.038750});
		
		XYVectorFunction gp = new XYVectorFunction(pts);
		int mobileBaseSize = 4;
		double thresholdSlope = 1e-5;
		
		SegmentMaker maker = new SegmentMaker(gp, mobileBaseSize, thresholdSlope);
		Assert.assertNotNull(maker.getPointTypes());
		Assert.assertEquals(gp.size(), maker.getPointTypes().size());
		Assert.assertTrue(maker.getPointTypes().get(9)==PointType.VERTICALCURVE_BEGINNING);
		for(PointType type : maker.getPointTypes()) {
			System.out.println(type);
		}
		Assert.assertNotNull(maker.getPointTypeSegments());
		for(PointTypeSegment segment : maker.getPointTypeSegments()) {
			System.out.println(segment.toString());
		}
		Assert.assertEquals(6, maker.getPointTypeSegments().size());		
	}
	
}
