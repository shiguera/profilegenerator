package com.mlab.pg.xyfunction;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestIntegerInterval {

	private final Logger LOG = Logger.getLogger(TestIntegerInterval.class);

	@BeforeClass
	public static void befor() {
		PropertyConfigurator.configure("log4j.properties");
	}
	@Test
	public void testConstructor() {
		LOG.debug("TestIntegerInterval.testConstructor()");
		IntegerInterval v1 = new IntegerInterval(1,5);
		Assert.assertNotNull(v1);
		Assert.assertEquals(1, v1.getStart());
		Assert.assertEquals(5, v1.getEnd());
		
		IntegerInterval v2 = new IntegerInterval(5, 1);
		Assert.assertNotNull(v2);
		Assert.assertEquals(1, v2.getStart());
		Assert.assertEquals(5, v2.getEnd());
		
		IntegerInterval v3 = new IntegerInterval(5, 5);
		Assert.assertNotNull(v3);
		Assert.assertEquals(5, v3.getStart());
		Assert.assertEquals(5, v3.getEnd());
		
	}

	@Test
	public void testContains_OneIndex() {
		LOG.debug("TestIntegerInterval.testContains_OneIndex()");
		IntegerInterval v1 = new IntegerInterval(1,5);
		Assert.assertNotNull(v1);
		
		Assert.assertTrue(v1.contains(1));
		Assert.assertTrue(v1.contains(2));
		Assert.assertTrue(v1.contains(5));
		
		Assert.assertFalse(v1.contains(-1));
		Assert.assertFalse(v1.contains(6));
		
	}

	@Test
	public void testContains_OneInterval() {
		LOG.debug("TestIntegerInterval.testContains_OneInterval()");
		IntegerInterval v1 = new IntegerInterval(1,5);
		Assert.assertNotNull(v1);

		IntegerInterval v2 = new IntegerInterval(1, 5);
		Assert.assertTrue(v1.contains(v2));

		IntegerInterval v3 = new IntegerInterval(2, 2);
		Assert.assertTrue(v1.contains(v3));
		
		IntegerInterval v4 = new IntegerInterval(1, 4);
		Assert.assertTrue(v1.contains(v4));

		IntegerInterval v5 = new IntegerInterval(0, 4);
		Assert.assertFalse(v1.contains(v5));

		IntegerInterval v6 = new IntegerInterval(1, 6);
		Assert.assertFalse(v1.contains(v6));

		IntegerInterval v7 = new IntegerInterval(0, 6);
		Assert.assertFalse(v1.contains(v7));

	}

	@Test
	public void testContainsInterior() {
		LOG.debug("TestIntegerInterval.testContainsInterior()");
		IntegerInterval v1 = new IntegerInterval(1,5);
		Assert.assertNotNull(v1);
		
		Assert.assertFalse(v1.containsInterior(1));
		Assert.assertTrue(v1.containsInterior(2));
		Assert.assertFalse(v1.containsInterior(5));
		
		Assert.assertFalse(v1.containsInterior(-1));
		Assert.assertFalse(v1.containsInterior(6));
		
	}

	@Test
	public void testContainsInterior_OneInterval() {
		LOG.debug("TestIntegerInterval.testContainsInterior_OneInterval()");
		IntegerInterval v1 = new IntegerInterval(1,5);
		Assert.assertNotNull(v1);

		IntegerInterval v2 = new IntegerInterval(1, 5);
		Assert.assertFalse(v1.containsInterior(v2));

		IntegerInterval v3 = new IntegerInterval(2, 2);
		Assert.assertTrue(v1.containsInterior(v3));
		
		IntegerInterval v4 = new IntegerInterval(1, 4);
		Assert.assertFalse(v1.containsInterior(v4));

		IntegerInterval v5 = new IntegerInterval(0, 4);
		Assert.assertFalse(v1.containsInterior(v5));

		IntegerInterval v6 = new IntegerInterval(1, 6);
		Assert.assertFalse(v1.containsInterior(v6));

		IntegerInterval v7 = new IntegerInterval(0, 6);
		Assert.assertFalse(v1.containsInterior(v7));

	}

	@Test
	public void testIntersects() {
		LOG.debug("TestIntegerInterval.testIntersects()");

		IntegerInterval v1 = new IntegerInterval(1,5);
		Assert.assertNotNull(v1);

		IntegerInterval v2 = new IntegerInterval(2, 3);
		Assert.assertTrue(v1.intersects(v2));

		v2 = new IntegerInterval(0, 3);
		Assert.assertTrue(v1.intersects(v2));

		v2 = new IntegerInterval(0, 1);
		Assert.assertTrue(v1.intersects(v2));

		v2 = new IntegerInterval(2, 6);
		Assert.assertTrue(v1.intersects(v2));

		v2 = new IntegerInterval(-1, 0);
		Assert.assertFalse(v1.intersects(v2));

		v2 = new IntegerInterval(6, 8);
		Assert.assertFalse(v1.intersects(v2));

		v2 = new IntegerInterval(0, 6);
		Assert.assertTrue(v1.intersects(v2));

	}

	@Test
	public void testIntersectsInterior() {
		LOG.debug("TestIntegerInterval.testIntersectsInterior()");

		IntegerInterval v1 = new IntegerInterval(1,5);
		Assert.assertNotNull(v1);

		IntegerInterval v2 = new IntegerInterval(2, 3);
		Assert.assertTrue(v1.intersectsInterior(v2));

		v2 = new IntegerInterval(0, 3);
		Assert.assertTrue(v1.intersectsInterior(v2));

		v2 = new IntegerInterval(0, 1);
		Assert.assertFalse(v1.intersectsInterior(v2));

		v2 = new IntegerInterval(2, 6);
		Assert.assertTrue(v1.intersectsInterior(v2));

		v2 = new IntegerInterval(5, 6);
		Assert.assertFalse(v1.intersectsInterior(v2));

		v2 = new IntegerInterval(-1, 0);
		Assert.assertFalse(v1.intersectsInterior(v2));

		v2 = new IntegerInterval(6, 8);
		Assert.assertFalse(v1.intersectsInterior(v2));

		v2 = new IntegerInterval(0, 6);
		Assert.assertTrue(v1.intersectsInterior(v2));

		v2 = new IntegerInterval(1, 5);
		Assert.assertTrue(v1.intersectsInterior(v2));
	}
	
	@Test
	public void testIntersection() {
		LOG.debug("TestIntegerInterval.testIntersection()");

		IntegerInterval v1 = new IntegerInterval(1,5);
		Assert.assertNotNull(v1);
		
		IntegerInterval v2 = new IntegerInterval(2, 3);
		Assert.assertEquals(v1.intersection(v2), v2);
		
		v2 = new IntegerInterval(0, 1);
		Assert.assertEquals(v1.intersection(v2), new IntegerInterval(1,1));
		
		v2 = new IntegerInterval(0, 2);
		Assert.assertEquals(v1.intersection(v2), new IntegerInterval(1,2));
		
		v2 = new IntegerInterval(4, 6);
		Assert.assertEquals(v1.intersection(v2), new IntegerInterval(4,5));

		v2 = new IntegerInterval(5, 6);
		Assert.assertEquals(v1.intersection(v2), new IntegerInterval(5,5));

		v2 = new IntegerInterval(-1, 0);
		Assert.assertNull(v1.intersection(v2));

		v2 = new IntegerInterval(6, 8);
		Assert.assertNull(v1.intersection(v2));

		v2 = new IntegerInterval(0, 6);
		Assert.assertEquals(v1.intersection(v2), v1);

		v2 = new IntegerInterval(1, 5);
		Assert.assertEquals(v1.intersection(v2), v1);
		Assert.assertEquals(v1.intersection(v2), v2);

	}

	@Test
	public void testMiddlePointForOnePointSegment() {
		LOG.debug("testGetMiddlePointForOnePointSegment()");
		// Caso longitud 1
		IntegerInterval v1 = new IntegerInterval(1,1);
		Assert.assertEquals(1, v1.getMiddlePoint());
		Assert.assertEquals(1,v1.getStart());
		Assert.assertEquals(1,v1.getEnd());		
	}

	@Test
	public void testMiddlePointForOddSegment() {
		LOG.debug("testGetMiddlePointForOddSegment()");
		// Caso longitud impar
		IntegerInterval v3 = new IntegerInterval(1,5);
		Assert.assertEquals(3, v3.getMiddlePoint());
		Assert.assertEquals(1, v3.getStart());
		Assert.assertEquals(5, v3.getEnd());		
	}
	@Test
	public void testGetMiddlePointForEvenSegment() {
		LOG.debug("testGetMiddlePointEvenSegment()");
		// Caso longitud par
		IntegerInterval v2 = new IntegerInterval(1,4);
		Assert.assertEquals(3, v2.getMiddlePoint());
		Assert.assertEquals(1, v2.getStart());
		Assert.assertEquals(4, v2.getEnd());
	}

	@Test
	public void testSize() {
		LOG.debug("testSize()");
		IntegerInterval i = new IntegerInterval(0, 4);
		Assert.assertEquals(5, i.size());
		i = new IntegerInterval(1, 4);
		Assert.assertEquals(4, i.size());
		i = new IntegerInterval(-1, 4);
		Assert.assertEquals(6, i.size());
		i = new IntegerInterval(-1, -4);
		Assert.assertEquals(4, i.size());
		i = new IntegerInterval(-1, 5);
		Assert.assertEquals(7, i.size());
		
		
	}
	@Test
	public void testEquals() {
		LOG.debug("TestIntegerInterval.testEquals()");

		IntegerInterval v1 = new IntegerInterval(1,5);
		Assert.assertNotNull(v1);
		
		IntegerInterval v2 = new IntegerInterval(2, 3);
		Assert.assertFalse(v1.equals(v2));
		
		v2 = new IntegerInterval(5, 1);
		Assert.assertEquals(v1, v2);
		
		v2 = new IntegerInterval(0, 1);
		Assert.assertFalse(v1.equals(v2));
		
		v2 = new IntegerInterval(5, 6);
		Assert.assertFalse(v1.equals(v2));
		
		

	}
}
