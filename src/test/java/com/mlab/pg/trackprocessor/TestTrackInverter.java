package com.mlab.pg.trackprocessor;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestTrackInverter {

	private static final Logger LOG = Logger.getLogger(TestTrackInverter.class);

	
	@BeforeClass
	public static void beforeClass() {
		PropertyConfigurator.configure("log4j.properties");
		//LOG.debug("beforeClass()");
	}

	@Test 
	public void testTrackInverter() {
		LOG.debug("testTrackInverter()");
		URL url = ClassLoader.getSystemResource("M607_Desc_1.csv");
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
		String resultname = TrackUtil.invert(file.getParent(), file.getName(),"M607_Desc_1_inverted.csv", 1);
		Assert.assertEquals("M607_Desc_1_inverted.csv", resultname);
	}

}
