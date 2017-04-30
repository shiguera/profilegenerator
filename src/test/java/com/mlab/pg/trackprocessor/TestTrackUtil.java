package com.mlab.pg.trackprocessor;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;



public class TestTrackUtil {

	static Logger LOG = Logger.getLogger(TestTrackUtil.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	
	@Test
	public void test_generateSGFileFromXYZFile() {
		LOG.debug("test_generateSGFileFromXYZFile()");
		String inpath = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksLeikaMaria/";
		String xyzfilename = "M607_Leica_Axis_xyz.csv";
		String result = TrackUtil.generateSGFileFromXYZFile(inpath, xyzfilename, 1);
		Assert.assertEquals("M607_Leica_Axis_xyz_SG.csv", result);

	}
	@Test
	public void test_generateSZFileFromXYZFile() {
		LOG.debug("test_generateSZFileFromXYZFile()");
		String inpath = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksLeikaMaria/";
		String xyzfilename = "M607_Leica_Axis_xyz.csv";
		String result = TrackUtil.generateSZFileFromXYZFile(inpath, xyzfilename, 1);
		Assert.assertEquals("M607_Leica_Axis_xyz_SZ.csv", result);
	}

}
