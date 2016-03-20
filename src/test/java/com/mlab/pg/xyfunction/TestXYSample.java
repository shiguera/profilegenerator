package com.mlab.pg.xyfunction;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;

public class TestXYSample {

	private final Logger LOG = Logger.getLogger(TestXYSample.class);
	
	@Test
	public void test() {
		LOG.debug("test()");
		XYSample f = new XYSampleImpl();
		Assert.assertNotNull(f);
		
		URL url = ClassLoader.getSystemResource("M607VerticalProfile_AlzadoProyecto_1m.csv");
		XYSampleCsvReader reader = new XYSampleCsvReader(new File(url.getPath()), ',');
		f = reader.read();
		
		Assert.assertNotNull(f);	
		Assert.assertEquals(12807, f.size());
		System.out.println("Size = " + f.size());

		Assert.assertEquals(0.0, f.getStartX());
		//System.out.println("startStation = " + f.getStartX());
		
		Assert.assertEquals(12803.0, f.getEndX());
		//System.out.println("Size = " + f.getEndX());
		

	}
	@Test
	public void testContainsInterval() {
		LOG.debug("testContainsInterval()");
		List<double[]> v1 = new ArrayList<double[]>();
		v1.add(new double[] {0.0,0.0});
		v1.add(new double[] {2.0,2.0});
		v1.add(new double[] {4.0,0.0});
		XYSampleImpl f1 = new XYSampleImpl(v1);
		
		Assert.assertTrue(f1.containsInterval(new IntegerInterval(0,2)));
		Assert.assertTrue(f1.containsInterval(new IntegerInterval(1,2)));
		Assert.assertTrue(f1.containsInterval(new IntegerInterval(2,2)));
		Assert.assertTrue(f1.containsInterval(new IntegerInterval(1,1)));
		Assert.assertTrue(f1.containsInterval(new IntegerInterval(0,0)));
		
		Assert.assertFalse(f1.containsInterval(new IntegerInterval(0,3)));
		Assert.assertFalse(f1.containsInterval(new IntegerInterval(-1,1)));
		Assert.assertFalse(f1.containsInterval(new IntegerInterval(-1,5)));
		
	}
	@Test
	public void testGetX() {
		LOG.debug("testGetX()");
		List<double[]> v1 = new ArrayList<double[]>();
		v1.add(new double[] {0.0,0.0});
		v1.add(new double[] {2.0,2.0});
		v1.add(new double[] {4.0,4.0});
		XYSampleImpl f1 = new XYSampleImpl(v1);
		Assert.assertNotNull(f1);
		
		//System.out.println("getY(3.0) = "+f1.getY(3.0));
		Assert.assertEquals(3.0, f1.getY(3.0));
		Assert.assertEquals(Double.NaN, f1.getY(5.0));
		Assert.assertEquals(1.0, f1.getY(1.0));
		Assert.assertEquals(0.0, f1.getY(0.0));
		Assert.assertEquals(4.0, f1.getY(4.0));
		Assert.assertEquals(Double.NaN, f1.getY(-1.0));
		
	}

	@Test
	public void testGetValues() {
		LOG.debug("testGetValues()");
		List<double[]> v1 = new ArrayList<double[]>();
		v1.add(new double[] {0.0,0.0});
		v1.add(new double[] {2.0,2.0});
		v1.add(new double[] {4.0,0.0});
		XYSampleImpl f1 = new XYSampleImpl(v1);
		
		List<double[]> l = f1.getValues();
		Assert.assertEquals(3, l.size());
		
		List<double[]> l2 = f1.getValues(1,2);
		Assert.assertNotNull(l2);
		Assert.assertEquals(2, l2.size());
		l2.clear();
		Assert.assertEquals(3, f1.size());
		l.clear();
		Assert.assertEquals(0, f1.size());				
	}
//	@Test
//	public void testWrite() {
//		LOG.debug("testWrite()");
//		List<double[]> v1 = new ArrayList<double[]>();
//		v1.add(new double[] {0.0,0.0});
//		v1.add(new double[] {2.0,2.0});
//		v1.add(new double[] {4.0,4.0});
//		XYSampleImpl f1 = new XYSampleImpl(v1);
//		Assert.assertNotNull(f1);
//		
//		URL url = ClassLoader.getSystemResource("test.csv");
//		XYSampleCsvWriter writer = new XYSampleCsvWriter(f1);
//		Assert.assertTrue(writer.write(new File("test.csv"),0,1, 12, 6, ','));
//
//	}
	
	@Test
	public void testGetTangent() {
		LOG.debug("testGetTangent()");
		List<double[]> v1 = new ArrayList<double[]>();
		v1.add(new double[] {0.0,0.0});
		v1.add(new double[] {2.0,2.0});
		v1.add(new double[] {4.0,4.0});
		XYSampleImpl f1 = new XYSampleImpl(v1);
		Assert.assertNotNull(f1);
		
		Assert.assertEquals(1.0, f1.getTangent(1));
		Assert.assertEquals(1.0, f1.getTangent(2.0));
		Assert.assertEquals(1.0, f1.getTangent(2));
		Assert.assertEquals(1.0, f1.getTangent(4.0));
		Assert.assertEquals(1.0, f1.getTangent(3.0));
		Assert.assertEquals(Double.NaN, f1.getTangent(10));
		Assert.assertEquals(Double.NaN, f1.getTangent(10.0));
	}
	@Test
	public void testGetCurvature() {
		LOG.debug("testGetCurvature()");
		List<double[]> v1 = new ArrayList<double[]>();
		v1.add(new double[] {0.0,0.0});
		v1.add(new double[] {2.0,2.0});
		v1.add(new double[] {4.0,4.0});		
		XYSampleImpl f1 = new XYSampleImpl(v1);
		Assert.assertNotNull(f1);
		Assert.assertEquals(0.0, f1.getCurvature(1));
		Assert.assertEquals(0.0, f1.getCurvature(2.0));
		Assert.assertEquals(0.0, f1.getCurvature(2));
		Assert.assertEquals(0.0, f1.getCurvature(4.0));
		Assert.assertEquals(0.0, f1.getCurvature(3.0));
		Assert.assertEquals(Double.NaN, f1.getCurvature(10));
		Assert.assertEquals(Double.NaN, f1.getCurvature(10.0));
	}

	@Test
	public void testSetValues() {
		LOG.debug("testGetCurvature()");
		List<double[]> v1 = new ArrayList<double[]>();
		v1.add(new double[] {0.0,0.0});
		v1.add(new double[] {2.0,2.0});
		v1.add(new double[] {4.0,4.0});		
		XYSampleImpl f1 = new XYSampleImpl();
		f1.setValues(v1);
		Assert.assertEquals(0.0, f1.getX(0));
		Assert.assertEquals(0.0, f1.getY(0));
		Assert.assertEquals(2.0, f1.getX(1));
		Assert.assertEquals(2.0, f1.getY(1));
		Assert.assertEquals(4.0, f1.getX(2));
		Assert.assertEquals(4.0, f1.getY(2));
		
	}

	@Test
	public void testGetYValues() {
		LOG.debug("testGetYValues()");
		List<double[]> v1 = new ArrayList<double[]>();
		v1.add(new double[] {0.0,0.0});
		v1.add(new double[] {2.0,2.0});
		v1.add(new double[] {4.0,4.0});
		v1.add(new double[] {6.0,6.0});
		XYSampleImpl f1 = new XYSampleImpl(v1);
		Assert.assertNotNull(f1);
		
		double[] values = f1.getYValues(new IntegerInterval(1,2));
		Assert.assertEquals(2.0,  values[0]);
		Assert.assertEquals(4.0,  values[1]);

		values = f1.getYValues();
		Assert.assertEquals(0.0,  values[0]);
		Assert.assertEquals(2.0,  values[1]);
		Assert.assertEquals(4.0,  values[2]);
		Assert.assertEquals(6.0,  values[3]);

	}

	@Test
	public void testAsArrayList() {
		LOG.debug("testAsArrayList()");
		List<double[]> v1 = new ArrayList<double[]>();
		v1.add(new double[] {0.0,0.0});
		v1.add(new double[] {2.0,2.0});
		v1.add(new double[] {4.0,4.0});
		v1.add(new double[] {6.0,6.0});
		XYSampleImpl f1 = new XYSampleImpl(v1);
		Assert.assertNotNull(f1);
		
		ArrayList<double[]> arraylist = f1.asArrayList();

		Assert.assertNotNull(arraylist);
		Assert.assertEquals(f1.size(), arraylist.size());
		for (int i=0; i<f1.size(); i++) {
			Assert.assertEquals(f1.getX(i), arraylist.get(i)[0], 0.001);
			Assert.assertEquals(f1.getY(i), arraylist.get(i)[1], 0.001);
		}
		
	}

	
}
