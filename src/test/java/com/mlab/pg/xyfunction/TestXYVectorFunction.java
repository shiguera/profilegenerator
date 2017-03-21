package com.mlab.pg.xyfunction;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestXYVectorFunction {

	private final static Logger LOG = Logger.getLogger(TestXYVectorFunction.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	
	@Test
	public void testAdd() {
		LOG.debug("testAdd()");
		XYVectorFunction vector = new XYVectorFunction();
		Assert.assertTrue(vector.add(new double[]{-1.0,2.0}));
		Assert.assertFalse(vector.add(new double[]{-2.0,2.0}));
		Assert.assertTrue(vector.add(new double[]{1.0,2.0}));
		Assert.assertTrue(vector.add(new double[]{2.0,2.0}));
		Assert.assertFalse(vector.add(new double[]{-1.0,2.0}));
		Assert.assertFalse(vector.add(new double[]{1.5,2.0}));
		
		
	}

	@Test
	public void testContainsX() {
		LOG.debug("testContainsX()");
		XYVectorFunction vector = new XYVectorFunction();
		Assert.assertFalse(vector.containsX(0.0));
		
		vector.add(new double[]{-1.0,0.0});
		Assert.assertTrue(vector.containsX(-1.0));
		Assert.assertFalse(vector.containsX(1.0));
		Assert.assertFalse(vector.containsX(0.0));
		
		vector.add(new double[]{0.0,0.0});
		Assert.assertTrue(vector.containsX(-1.0));
		Assert.assertTrue(vector.containsX(0.0));
		Assert.assertTrue(vector.containsX(-0.5));
		Assert.assertFalse(vector.containsX(1.0));
		Assert.assertFalse(vector.containsX(-2.0));

		vector.add(new double[]{1.0,0.0});
		Assert.assertTrue(vector.containsX(-1.0));
		Assert.assertTrue(vector.containsX(0.0));
		Assert.assertTrue(vector.containsX(-0.5));
		Assert.assertTrue(vector.containsX(0.5));
		Assert.assertTrue(vector.containsX(1.0));
		Assert.assertFalse(vector.containsX(2.0));
		Assert.assertFalse(vector.containsX(-2.0));

	
		
	}

	@Test
	public void testPreviousIndex() {
		LOG.debug("testPreviousIndex()");
		XYVectorFunction vector = new XYVectorFunction();
		vector.add(new double[]{-2.0,10.0});
		vector.add(new double[]{-1.5,11.0});
		vector.add(new double[]{0.0,12.0});
		vector.add(new double[]{1.5,13.0});
		vector.add(new double[]{2.0,14.0});

		// x no contenido
		Assert.assertEquals(-1, vector.previousIndex(-2.5));
		Assert.assertEquals(-1, vector.previousIndex(2.5));
		
		// x coincide con el extremo izquierdo
		Assert.assertEquals(0, vector.previousIndex(-2.0));
		
		// x es un punto intermedio
		Assert.assertEquals(1, vector.previousIndex(-0.50));
		Assert.assertEquals(2, vector.previousIndex(0.50));
		
		// x coincide con un punto intermedio
		Assert.assertEquals(2, vector.previousIndex(0.0));
		
		// x coincide con el extremo derecho y size()>1
		Assert.assertEquals(4, vector.previousIndex(2.0));

		// x coincide con el extremo derecho y el tamaño es 1
		vector = new XYVectorFunction();
		vector.add(new double[]{1.0,1.0});
		Assert.assertEquals(0, vector.previousIndex(1.0));

	}
	@Test
	public void testFollowingIndex() {
		LOG.debug("testFollowingIndex()");
		XYVectorFunction vector = new XYVectorFunction();
		vector.add(new double[]{-2.0,10.0});
		vector.add(new double[]{-1.0,11.0});
		vector.add(new double[]{0.0,12.0});
		vector.add(new double[]{1.0,13.0});
		vector.add(new double[]{2.0,14.0});
		
		// x no contenido
		Assert.assertEquals(-1, vector.followingIndex(-2.5));
		Assert.assertEquals(-1, vector.followingIndex(2.5));
		
		// x coincide con el extremo izquierdo
		Assert.assertEquals(0, vector.followingIndex(-2.0));
		
		// x es un punto intermedio
		Assert.assertEquals(2, vector.followingIndex(-0.5));
		Assert.assertEquals(3, vector.followingIndex(0.5));
		
		// x coincide con un punto intermedio
		Assert.assertEquals(2, vector.followingIndex(0.0));
		
		// x coincide con el extremo derecho
		Assert.assertEquals(4, vector.followingIndex(2.0));

		// x coincide con el extremo derecho y el tamaño es 1
		vector = new XYVectorFunction();
		vector.add(new double[]{1.0,1.0});
		Assert.assertEquals(0, vector.followingIndex(1.0));

	}
	@Test
	public void testGetY() {
		LOG.debug("testGetY()");
		XYVectorFunction vector = new XYVectorFunction();
		vector.add(new double[]{-2.0,10.0});
		vector.add(new double[]{-1.0,11.0});
		vector.add(new double[]{0.0,12.0});
		vector.add(new double[]{1.0,13.0});
		vector.add(new double[]{2.0,14.0});

		// x coincide extremo izquierdo
		Assert.assertEquals(10.0, vector.getY(-2.0), 0.001);
		
		// x intermedio entre primer y segundo punto
		Assert.assertEquals(10.5, vector.getY(-1.5), 0.001);
		
		// x coincide con punto intermedio
		Assert.assertEquals(12.0, vector.getY(0.0), 0.001);
		
		// x intermedio entre penúltimo y último punto
		Assert.assertEquals(13.5, vector.getY(1.5), 0.001);
		
		// x coincide con el último punto
		Assert.assertEquals(14.0, vector.getY(2.0), 0.001);		
	}
	@Test
	public void testGetTangent() {
		LOG.debug("testGetTangent()");
		XYVectorFunction vector = new XYVectorFunction();
		vector.add(new double[]{-2.0,10.0});
		vector.add(new double[]{-1.0,11.0});
		vector.add(new double[]{0.0,13.0});
		vector.add(new double[]{1.0,16.0});
		vector.add(new double[]{2.0,20.0});
		
		// X no incluido en el intervalo
		Assert.assertEquals(Double.NaN, vector.getTangent(-2.5), 0.001);
		Assert.assertEquals(Double.NaN, vector.getTangent(2.5), 0.001);
		
		// x coincide extremo izquierdo
		Assert.assertEquals((11.0-10.0), vector.getTangent(-2.0), 0.001);
		
		// x intermedio entre primer y segundo punto
		Assert.assertEquals((13.0-11.0), vector.getTangent(-0.5), 0.001);
		
		// x coincide con punto intermedio
		Assert.assertEquals((16.0-13.0), vector.getTangent(0.0), 0.001);
		
		// x intermedio entre penúltimo y último punto
		Assert.assertEquals(20.0-16.0, vector.getTangent(1.5), 0.001);

		// x coincide con el último punto
		Assert.assertEquals(20.0-16.0, vector.getTangent(2.0), 0.001);		
	}

	@Test
	public void testSubList() {
		LOG.debug("testSubList()");
		XYVectorFunction vector = new XYVectorFunction();
		vector.add(new double[]{-2.0,10.0});
		vector.add(new double[]{-1.5,11.0});
		vector.add(new double[]{0.0,12.0});
		vector.add(new double[]{1.5,13.0});
		vector.add(new double[]{2.0,14.0});
		
		XYVectorFunction sublist = vector.subList(0, 4);
		// Comprobar que incluyeelextremo derecho del intervalo
		Assert.assertEquals(5, sublist.size());
		Assert.assertEquals(2.0, sublist.getEndX(), 0.001);
		
		// Comprobar que si los índices no están en la lista
		// devuelve una lista vacía 
		sublist = vector.subList(0, 6);
		Assert.assertEquals(0, sublist.size());
		sublist = vector.subList(-1, 3);
		Assert.assertEquals(0, sublist.size());
		
	}
	
	
	@Test
	public void testExtract() {
		LOG.debug("testExtract()");
		XYVectorFunction vector = new XYVectorFunction();
		vector.add(new double[]{-2.0,10.0});
		vector.add(new double[]{-1.5,11.0});
		vector.add(new double[]{0.0,12.0});
		vector.add(new double[]{1.5,13.0});
		vector.add(new double[]{2.0,14.0});
		
		XYVectorFunction f = vector.extract(-3.0, 2.5);
		Assert.assertEquals(-2.0,f.getStartX(), 0.001);
		Assert.assertEquals(2.0,f.getEndX(), 0.001);
		Assert.assertEquals(5,f.size());

		f = vector.extract(-3.0, -1.4);
		Assert.assertEquals(-2.0,f.getStartX(), 0.001);
		Assert.assertEquals(0.0,f.getEndX(), 0.001);
		Assert.assertEquals(3,f.size());
		
		

	}

	@Test
	public void testCloneGenerateDifferentInstance() {
		LOG.debug("testCloneGenerateDifferentInstance()");
		XYVectorFunction vector = new XYVectorFunction();
		vector.add(new double[]{-2.0,10.0});
		vector.add(new double[]{-1.5,11.0});
		vector.add(new double[]{0.0,12.0});
		vector.add(new double[]{1.5,13.0});
		vector.add(new double[]{2.0,14.0});
		
		int size = vector.size();
		
		XYVectorFunction f2 = vector.clone();
		Assert.assertEquals(size, f2.size());
		vector.remove(0);
		Assert.assertEquals(size-1, vector.size());
		Assert.assertEquals(size, f2.size());
		
	}
}
