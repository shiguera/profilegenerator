package com.mlab.pg.xyfunction;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

public class TestXYVectorFunctionCSVReader {

	private static Logger LOG = Logger.getLogger(TestXYVectorFunctionCSVReader.class);
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
		
	}
	@Test
	public void test() {
		LOG.debug("test()");
		XYVectorFunction f = new XYVectorFunction();
		Assert.assertNotNull(f);
		
		URL url = ClassLoader.getSystemResource("M607VerticalProfile_AlzadoProyecto_1m.csv");
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(new File(url.getPath()), ',', true);
		f = reader.read();
		
		Assert.assertNotNull(f);	
		Assert.assertEquals(12807, f.size());
		//System.out.println("Size = " + f.size());

		Assert.assertEquals(0.0, f.getStartX());
		//System.out.println("startStation = " + f.getStartX());
		
		Assert.assertEquals(12803.0, f.getEndX());
		//System.out.println("Size = " + f.getEndX());
		

	}

}
