package com.mlab.pg.xyfunction;

import java.util.Random;

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
	public void testGetYValues2() {
		LOG.debug("testGetYValues2()");
		XYVector vector = new XYVector();
		Assert.assertNull(vector.getYValues());
		
		Random rnd = new Random();
		for(int i=0; i<100; i++) {
			vector.add(new double[]{rnd.nextDouble(), rnd.nextDouble()});
		}
		
		double[] values = vector.getYValues(-1, 1);
		Assert.assertNull(values);	
		
		values = vector.getYValues(1, 0);
		Assert.assertNull(values);	
		
		values = vector.getYValues(-1, 2);
		Assert.assertNull(values);	
		
		values = vector.getYValues(0, vector.size()-1);
		Assert.assertEquals(vector.size(), values.length, 0.001);	
		
		values = vector.getYValues(0, 10);
		Assert.assertEquals(11, values.length, 0.001);	

		values = vector.getYValues(10, 20);
		Assert.assertEquals(11, values.length, 0.001);	

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

	@Test
	public void testSubList() {
		LOG.debug("testSubList()");
		XYVector vector = new XYVector();
		vector.add(new double[]{-2.0,10.0});
		vector.add(new double[]{-1.5,11.0});
		vector.add(new double[]{0.0,12.0});
		vector.add(new double[]{1.5,13.0});
		vector.add(new double[]{2.0,14.0});

		// Al extraer una sublista, el extremo derecho es exclusivo, no se incluye en la extracción
		XYVector sublist = vector.subList(0, 4);
		Assert.assertEquals(4, sublist.size()); 
		
		// La lista devuelta es un objeto diferente de la lista original
		// si variamos su contenido la lista original no se altera
		sublist.set(0, new double[]{-3.0,10.0});
		Assert.assertEquals(-3.0, sublist.getX(0), 0.001);
		Assert.assertEquals(-2.0, vector.getX(0), 0.001);
	}
}
