package com.mlab.pg.util;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestIoUtil {

	private static final Logger LOG = Logger.getLogger(TestIoUtil.class);

	
	@BeforeClass
	public static void beforeClass() {
		PropertyConfigurator.configure("log4j.properties");
		LOG.debug("beforeClass()");
	}

	@Test
	public void testRead() {
		LOG.debug("testRead()");
		
		// Fichero con una línea de cabecera
		URL url = ClassLoader.getSystemResource("xyvectorSample_withHeaders.csv");
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		double[][] d = Ioutil.read(file, ",", 1);
		Assert.assertNotNull(d);
		
		Assert.assertEquals(15, d.length);
		Assert.assertEquals(2, d[1].length);
		Assert.assertEquals(69.4476700165, d[7][0], 0.001);
		Assert.assertEquals(0.038, d[7][1], 0.001);
		
		// fichero con dos líneas de cabecera
		url = ClassLoader.getSystemResource("xyvectorSample_withHeaders_2.csv");
		file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		d = Ioutil.read(file, ",", 2);
		Assert.assertNotNull(d);
		
		Assert.assertEquals(15, d.length);
		Assert.assertEquals(2, d[1].length);
		Assert.assertEquals(69.4476700165, d[7][0], 0.001);
		Assert.assertEquals(0.038, d[7][1], 0.001);
		
	}
	
}
