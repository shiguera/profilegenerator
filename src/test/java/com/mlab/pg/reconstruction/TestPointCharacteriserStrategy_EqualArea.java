package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.xyfunction.XYVectorFunction;

import junit.framework.Assert;

public class TestPointCharacteriserStrategy_EqualArea {
	
	private final static Logger LOG = Logger.getLogger(TestPointCharacteriserStrategy_EqualArea.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}

	@Test
	public void testCalculaRectaAnterior() {
		LOG.debug("testCalculaRectaAnterior()");
		XYVectorFunction f = new XYVectorFunction();
		f.add(new double[]{1.0, 1.0});
		f.add(new double[]{1.5, 1.75});
		f.add(new double[]{2.0, 2.5});
		f.add(new double[]{2.5, 3.25});
		f.add(new double[]{3.0, 4.0});
		PointCharacteriserStrategy_EqualArea strategy = new PointCharacteriserStrategy_EqualArea();
		double[] r = strategy.calculaRectaAnterior(f, 4, 5);
		Assert.assertEquals(-0.5, r[0], 0.0001);
		Assert.assertEquals(1.5, r[1], 0.0001);
		
		f = new XYVectorFunction();
		f.add(new double[]{1.0, 3.0});
		f.add(new double[]{1.5, 2.333});
		f.add(new double[]{2.0, 1.667});
		f.add(new double[]{3.0, 0.333});
		f.add(new double[]{4.0, -1.0});
		r = strategy.calculaRectaAnterior(f, 4, 5);
		Assert.assertEquals(13.0/3.0, r[0], 0.001);
		Assert.assertEquals(-4.0/3.0, r[1], 0.001);		
	}
	
	@Test
	public void testCalculaRectaPosterior() {
		LOG.debug("testCalculaRectaPosterior()");
		XYVectorFunction f = new XYVectorFunction();
		f.add(new double[]{1.0, 1.0});
		f.add(new double[]{1.5, 1.75});
		f.add(new double[]{2.0, 2.5});
		f.add(new double[]{2.5, 3.25});
		f.add(new double[]{3.0, 4.0});
		PointCharacteriserStrategy_EqualArea strategy = new PointCharacteriserStrategy_EqualArea();
		double[] r = strategy.calculaRectaPosterior(f, 0, 5);
		Assert.assertEquals(-0.5, r[0], 0.0001);
		Assert.assertEquals(1.5, r[1], 0.0001);
		
		f = new XYVectorFunction();
		f.add(new double[]{1.0, 3.0});
		f.add(new double[]{1.5, 2.333});
		f.add(new double[]{2.0, 1.667});
		f.add(new double[]{3.0, 0.333});
		f.add(new double[]{4.0, -1.0});
		r = strategy.calculaRectaPosterior(f, 0, 5);
		Assert.assertEquals(13.0/3.0, r[0], 0.001);
		Assert.assertEquals(-4.0/3.0, r[1], 0.001);
		
		
	}


}
