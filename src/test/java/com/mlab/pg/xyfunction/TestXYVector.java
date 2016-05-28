package com.mlab.pg.xyfunction;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestXYVector {

	private final static Logger LOG = Logger.getLogger(TestXYVector.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}

	@Test
	public void testConstructor() {
		LOG.debug("testConstructor()");
		XYVector vector = new XYVector();
		double[] d0 = new double[]{0};
		Assert.assertFalse(vector.add(d0));
		double[] d1 = new double[]{0.0,1.0};
		Assert.assertTrue(vector.add(d1));
		Assert.assertEquals(0.0, vector.getX(0),0.001);
		Assert.assertEquals(1.0, vector.getY(0),0.001);
		double[] d2 = new double[]{0.0,1.0,2.0};
		Assert.assertTrue(vector.add(d2));
		Assert.assertEquals(0.0, vector.getX(1),0.001);
		Assert.assertEquals(1.0, vector.getY(1),0.001);

	}
	@Test
	public void testGet() {
		LOG.debug("testGet()");
		XYVector vector = new XYVector();
		Assert.assertNull(vector.get(0));
		Assert.assertNull(vector.get(1));
		Assert.assertNull(vector.get(-1));
		
		vector.add(new double[]{1.0,1.0});
		Assert.assertNotNull(vector.get(0));
		Assert.assertNull(vector.get(1));
		Assert.assertNull(vector.get(-1));
		Assert.assertEquals(1.0, vector.get(0)[0], 0.001);
		Assert.assertEquals(1.0, vector.get(0)[1], 0.001);
		
		vector.add(new double[]{2.0,2.0});
		Assert.assertNotNull(vector.get(0));
		Assert.assertNotNull(vector.get(1));
		Assert.assertNull(vector.get(-1));
		Assert.assertEquals(2.0, vector.get(1)[0], 0.001);
		Assert.assertEquals(2.0, vector.get(1)[1], 0.001);

		
	}
	@Test
	public void testGetXY() {
		LOG.debug("testGetXY()");
		XYVector vector = new XYVector();
		Assert.assertNull(vector.getXY(0));
		Assert.assertNull(vector.getXY(1));
		Assert.assertNull(vector.getXY(-1));

		vector.add(new double[]{1.0,1.0});
		Assert.assertNotNull(vector.getXY(0));
		Assert.assertNull(vector.getXY(1));
		Assert.assertNull(vector.getXY(-1));
		Assert.assertEquals(1.0, vector.getXY(0)[0],0.001);
		Assert.assertEquals(1.0, vector.getXY(0)[1],0.001);

		vector.add(new double[]{2.0,3.0});
		Assert.assertNotNull(vector.getXY(0));
		Assert.assertNotNull(vector.getXY(1));
		Assert.assertNull(vector.getXY(-1));
		Assert.assertEquals(2.0, vector.getXY(1)[0],0.001);
		Assert.assertEquals(3.0, vector.getXY(1)[1],0.001);
				
	}
	@Test
	public void testGetX() {
		LOG.debug("testGetX()");
		XYVector vector = new XYVector();
		Assert.assertEquals(Double.NaN, vector.getX(0), 0.001);
		Assert.assertEquals(Double.NaN, vector.getX(1), 0.001);
		Assert.assertEquals(Double.NaN, vector.getX(-1), 0.001);

		vector.add(new double[]{2.0,2.0});
		Assert.assertEquals(2.0, vector.getX(0), 0.001);
		Assert.assertEquals(Double.NaN, vector.getX(1), 0.001);
		Assert.assertEquals(Double.NaN, vector.getX(-1), 0.001);

		vector.add(new double[]{3.0,3.0});
		Assert.assertEquals(2.0, vector.getX(0), 0.001);
		Assert.assertEquals(3.0, vector.getX(1), 0.001);
		Assert.assertEquals(Double.NaN, vector.getX(-1), 0.001);
				
	}
	@Test
	public void testGetY() {
		LOG.debug("testGetY()");
		XYVector vector = new XYVector();
		Assert.assertEquals(Double.NaN, vector.getY(0), 0.001);
		Assert.assertEquals(Double.NaN, vector.getY(1), 0.001);
		Assert.assertEquals(Double.NaN, vector.getY(-1), 0.001);

		vector.add(new double[]{2.0,2.0});
		Assert.assertEquals(2.0, vector.getY(0), 0.001);
		Assert.assertEquals(Double.NaN, vector.getY(1), 0.001);
		Assert.assertEquals(Double.NaN, vector.getY(-1), 0.001);

		vector.add(new double[]{3.0,3.0});
		Assert.assertEquals(2.0, vector.getY(0), 0.001);
		Assert.assertEquals(3.0, vector.getY(1), 0.001);
		Assert.assertEquals(Double.NaN, vector.getY(-1), 0.001);
				
	}
	@Test
	public void testGetXValues() {
		LOG.debug("testGetXValues()");
		XYVector vector = new XYVector();
		Assert.assertNull(vector.getXValues());
		
		vector.add(new double[]{2.0,2.0});
		Assert.assertEquals(1, vector.getXValues().length);
		Assert.assertEquals(2.0, vector.getXValues()[0],0.001);
		
		vector.add(new double[]{2.0,2.0});
		Assert.assertEquals(2, vector.getXValues().length);
		Assert.assertEquals(2.0, vector.getXValues()[0],0.001);
		Assert.assertEquals(2.0, vector.getXValues()[1],0.001);
		
	}
	@Test
	public void testGetYValues() {
		LOG.debug("testGetYValues()");
		XYVector vector = new XYVector();
		Assert.assertNull(vector.getYValues());
		
		vector.add(new double[]{2.0,2.0});
		Assert.assertEquals(1, vector.getYValues().length);
		Assert.assertEquals(2.0, vector.getYValues()[0],0.001);
		
		vector.add(new double[]{2.0,2.0});
		Assert.assertEquals(2, vector.getYValues().length);
		Assert.assertEquals(2.0, vector.getYValues()[0],0.001);
		Assert.assertEquals(2.0, vector.getYValues()[1],0.001);
		
	}


	@Test
	public void testContains() {
		LOG.debug("testContains()");
		XYVector vector = new XYVector();
		Assert.assertFalse(vector.contains(0));
		Assert.assertFalse(vector.contains(-1));
		Assert.assertFalse(vector.contains(1));
		
		vector.add(new double[]{1.0,1.0});
		Assert.assertTrue(vector.contains(0));
		Assert.assertFalse(vector.contains(-1));
		Assert.assertFalse(vector.contains(1));

		vector.add(new double[]{1.0,1.0});
		Assert.assertTrue(vector.contains(0));
		Assert.assertFalse(vector.contains(-1));
		Assert.assertTrue(vector.contains(1));

		
		
	}
}
