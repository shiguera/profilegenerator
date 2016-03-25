package com.mlab.pg.xyfunction;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

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

}
