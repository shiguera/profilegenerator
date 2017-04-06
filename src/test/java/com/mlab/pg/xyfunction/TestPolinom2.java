package com.mlab.pg.xyfunction;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestPolinom2 {

	private final static Logger LOG = Logger.getLogger(TestPolinom2.class);
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}

	@Test
	public void testConstructorAndGettersSetters() {
		LOG.debug("TestPolinom2.Test()");
		
		Polynom2 p = new Polynom2(0.0,0.0,0.0);
		Assert.assertNotNull(p);
		Assert.assertEquals(0.0, p.getY(100.0),0.001);
		Assert.assertEquals(0.0, p.getTangent(100.0),0.001);
		Assert.assertEquals(0.0, p.getCurvature(100.0),0.001);

		p.setA0(1.0);
		Assert.assertEquals(1.0, p.getY(100.0),0.001);
		Assert.assertEquals(0.0, p.getTangent(100.0),0.001);
		Assert.assertEquals(0.0, p.getCurvature(100.0),0.001);
		
		p.setA1(1.0);
		Assert.assertEquals(101.0, p.getY(100.0),0.001);
		Assert.assertEquals(1.0, p.getTangent(100.0),0.001);
		Assert.assertEquals(0.0, p.getCurvature(100.0),0.001);

		p.setA2(1.0);		
		Assert.assertEquals(10101.0, p.getY(100.0),0.001);
		Assert.assertEquals(201.0, p.getTangent(100.0),0.001);
		Assert.assertEquals(2.46e-7, p.getCurvature(100.0), 0.01e-7);
	}

	@Test
	public void test_asXml() {
		LOG.debug("test_asXml()");
		Polynom2 p = new Polynom2(-1.0,3.2,0.0);
		System.out.println(p.asXml());
		p = new Polynom2(-1.0,3.2e-7,-1.7e-5);
		System.out.println(p.asXml());

	}
	
	
}
