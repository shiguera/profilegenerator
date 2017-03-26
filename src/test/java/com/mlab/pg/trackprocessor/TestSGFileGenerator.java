package com.mlab.pg.trackprocessor;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.util.IOUtil;


public class TestSGFileGenerator {

	private static final Logger LOG = Logger.getLogger(TestSGFileGenerator.class);

	
	@BeforeClass
	public static void beforeClass() {
		PropertyConfigurator.configure("log4j.properties");
		//LOG.debug("beforeClass()");
	}

	@Test
	public void testGenerateSGFile() {
		String filename = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/mergetracks/M607_Asc_Average_1.csv";
		File filein = new File(filename);
		Assert.assertTrue(filein.exists());
		double[][] intrack = IOUtil.read(filein, ",", 0);
		SGFileGenerator generator = new SGFileGenerator();
		String outfilename = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/mergetracks/M607_Asc_Average_1_SG.csv";
		int result = generator.generateSGFile(intrack, outfilename);
		Assert.assertEquals(1, result);
		SZFileGenerator generator2 = new SZFileGenerator();
		outfilename = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/mergetracks/M607_Asc_Average_1_SZ.csv";
		result = generator2.generateSZFile(intrack, outfilename);
		Assert.assertEquals(1, result);
	
	}

	@Test
	public void testGenerateSGFile_M608() {
		String filename = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/M608_Asc_2017-03-09.csv";
		File filein = new File(filename);
		Assert.assertTrue(filein.exists());
		double[][] intrack = IOUtil.read(filein, ",", 1);
		SGFileGenerator generator = new SGFileGenerator();
		String outfilename = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/M608_Asc_2017-03-09_SG.csv";
		int result = generator.generateSGFile(intrack, outfilename);
		Assert.assertEquals(1, result);
		SZFileGenerator generator2 = new SZFileGenerator();
		outfilename = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/M608_Asc_2017-03-09_SZ.csv";
		result = generator2.generateSZFile(intrack, outfilename);
		Assert.assertEquals(1, result);
	
	}
	@Test
	public void testGenerateSGFile_M607_RoadRecorder() {
		String filename = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M-607/tracksRoadRecorder/M607_Asc_Axis.csv";
		File filein = new File(filename);
		Assert.assertTrue(filein.exists());
		double[][] intrack = IOUtil.read(filein, ",", 1);
		SGFileGenerator generator = new SGFileGenerator();
		String outfilename = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M-607/tracksRoadRecorder/M607_Asc_Axis_SG.csv";
		int result = generator.generateSGFile(intrack, outfilename);
		Assert.assertEquals(1, result);
		SZFileGenerator generator2 = new SZFileGenerator();
		outfilename = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M-607/tracksRoadRecorder/M607_Asc_Axis_SZ.csv";
		result = generator2.generateSZFile(intrack, outfilename);
		Assert.assertEquals(1, result);
	
	}
	@Test
	public void testGenerateSGFile_M607_Leika() {
		String filename = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksLeika/trackLeika_M607_Axis.csv";
		File filein = new File(filename);
		Assert.assertTrue(filein.exists());
		double[][] intrack = IOUtil.read(filein, ",", 1);
		SGFileGenerator generator = new SGFileGenerator();
		String outfilename = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksLeika/trackLeika_M607_Axis_SG.csv";
		int result = generator.generateSGFile(intrack, outfilename);
		Assert.assertEquals(1, result);
		SZFileGenerator generator2 = new SZFileGenerator();
		outfilename = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksLeika/trackLeika_M607_Axis_SZ.csv";
		result = generator2.generateSZFile(intrack, outfilename);
		Assert.assertEquals(1, result);
	
	}

}
