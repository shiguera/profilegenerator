package com.mlab.pg.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.trackprocessor.TrackInverter;

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
		
		double[][] d = IOUtil.read(file, ",", 1);
		Assert.assertNotNull(d);
		
		Assert.assertEquals(15, d.length);
		Assert.assertEquals(2, d[1].length);
		Assert.assertEquals(69.4476700165, d[7][0], 0.001);
		Assert.assertEquals(0.038, d[7][1], 0.001);
		
		// fichero con dos líneas de cabecera
		url = ClassLoader.getSystemResource("xyvectorSample_withHeaders_2.csv");
		file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		d = IOUtil.read(file, ",", 2);
		Assert.assertNotNull(d);
		
		Assert.assertEquals(15, d.length);
		Assert.assertEquals(2, d[1].length);
		Assert.assertEquals(69.4476700165, d[7][0], 0.001);
		Assert.assertEquals(0.038, d[7][1], 0.001);
		
	}
	@Test
	public void testInvert() {
		LOG.debug("testInvert()");
		URL url = ClassLoader.getSystemResource("M607_Desc_1.csv");
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
		
		double[][] track = IOUtil.read(file, ",", 1);
		Assert.assertNotNull(track);
		Assert.assertEquals(485, track.length);
		
		double[][] inverted = IOUtil.invert(track);
		Assert.assertNotNull(inverted);
		Assert.assertEquals(485, inverted.length);
		Assert.assertEquals(track[0][0], inverted[484][0], 0.00001);
		Assert.assertEquals(track[0][1], inverted[484][1], 0.00001);
		Assert.assertEquals(track[0][2], inverted[484][2], 0.00001);
		Assert.assertEquals(track[484][0], inverted[0][0], 0.00001);
		Assert.assertEquals(track[484][1], inverted[0][1], 0.00001);
		Assert.assertEquals(track[484][2], inverted[0][2], 0.00001);
		
	}

}
