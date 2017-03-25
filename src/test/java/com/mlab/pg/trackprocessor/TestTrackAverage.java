package com.mlab.pg.trackprocessor;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.util.IOUtil;

public class TestTrackAverage {

	private static final Logger LOG = Logger.getLogger(TestTrackAverage.class);

	@BeforeClass
	public static void beforeClass() {
		PropertyConfigurator.configure("log4j.properties");
		//LOG.debug("beforeClass()");
	}

	@Test
	public void testAverage_M607() {
		LOG.debug("testAverage_M607()");
		
		String path = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/mergetracks/";
		File file = new File(path + "M607_Desc_1.csv");
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
		TrackInverter inverter = new TrackInverter();
		int result = inverter.invert(file, path + "M607_Desc_1_Inverted.csv");
		Assert.assertEquals(1, result);

		
		TrackAverage averager = new TrackAverage();
		File file1 = new File(path + "M607_Asc_1.csv");
		File file2 = new File(path + "M607_Desc_1_Inverted.csv");
		double[][] track1 = IOUtil.read(file1, ",", 1);
		double[][] track2 = IOUtil.read(file2, ",",0);
		double[][] resultTrack = averager.average(track1, track2);
		Assert.assertEquals(track1.length, resultTrack.length);
		String outfilename = path + "M607_Asc_Average_1.csv";
		result = IOUtil.write(outfilename, resultTrack, 12, 6, ',');
		Assert.assertEquals(1, result);
		
	}

	@Test
	public void testAverage_M608() {
		LOG.debug("testAverage_M608()");
		
		String path = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/";
		File file = new File(path + "M608_Desc_2017-03-09.csv");
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
		TrackInverter inverter = new TrackInverter();
		int result = inverter.invert(file, path + "M608_Desc_2017-03-09_Inverted.csv");
		Assert.assertEquals(1, result);

		
		TrackAverage averager = new TrackAverage();
		File file1 = new File(path + "M608_Asc_2017-03-09.csv");
		File file2 = new File(path + "M608_Desc_2017-03-09_Inverted.csv");
		double[][] track1 = IOUtil.read(file1, ",", 1);
		double[][] track2 = IOUtil.read(file2, ",",0);
		double[][] resultTrack = averager.average(track1, track2);
		Assert.assertEquals(track1.length, resultTrack.length);
		String outfilename = path + "M608_Asc_2017-03-09_Axis.csv";
		result = IOUtil.write(outfilename, resultTrack, 12, 6, ',');
		Assert.assertEquals(1, result);
		
		file = new File(outfilename);
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
		TrackReporter reporter = new TrackReporter(file, 0);
		reporter.printReport();
		
	}

	@Test
	public void testAverage_M608_FourTracks() {
		LOG.debug("testAverage_M608_FourTracks()");
		
		String path = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/";
		// Track Descendente
		File file = new File(path + "M608_Desc_2017-03-09.csv");
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
		TrackInverter inverter = new TrackInverter();
		int result = inverter.invert(file, path + "M608_Desc_2017-03-09_Inverted.csv");
		Assert.assertEquals(1, result);
		file = new File(path + "M608_Desc_2017-03-10.csv");
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
		result = inverter.invert(file, path + "M608_Desc_2017-03-10_Inverted.csv");
		Assert.assertEquals(1, result);
		
		TrackAverage averager = new TrackAverage();
		File file1 = new File(path + "M608_Desc_2017-03-09_Inverted.csv");
		File file2 = new File(path + "M608_Desc_2017-03-10_Inverted.csv");
		double[][] track1 = IOUtil.read(file1, ",", 0);
		double[][] track2 = IOUtil.read(file2, ",",0);
		double[][] resultTrack = averager.average(track1, track2);
		Assert.assertEquals(track1.length, resultTrack.length);
		String outfilename = path + "M608_Desc_Average.csv";
		result = IOUtil.write(outfilename, resultTrack, 12, 6, ',');
		Assert.assertEquals(1, result);
		
		file = new File(outfilename);
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
		TrackReporter reporter = new TrackReporter(file, 0);
		reporter.printReport();
	
		// TrackAscendente
		file1 = new File(path + "M608_Asc_2017-03-09.csv");
		Assert.assertNotNull(file1);
		Assert.assertTrue(file1.exists());
		file2 = new File(path + "M608_Asc_2017-03-10.csv");
		Assert.assertNotNull(file2);
		Assert.assertTrue(file2.exists());
		track1 = IOUtil.read(file1, ",", 1);
		track2 = IOUtil.read(file2, ",",1);
		resultTrack = averager.average(track1, track2);
		Assert.assertEquals(track1.length, resultTrack.length);
		outfilename = path + "M608_Asc_Average.csv";
		result = IOUtil.write(outfilename, resultTrack, 12, 6, ',');
		Assert.assertEquals(1, result);
		
		file = new File(outfilename);
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
		reporter = new TrackReporter(file, 0);
		reporter.printReport();
	
		// Eje
		file1 = new File(path + "M608_Asc_Average.csv");
		file2 = new File(path + "M608_Desc_Average.csv");
		track1 = IOUtil.read(file1, ",", 0);
		track2 = IOUtil.read(file2, ",",0);
		resultTrack = averager.average(track1, track2);
		Assert.assertEquals(track1.length, resultTrack.length);
		outfilename = path + "M608_FourTracks_Axis.csv";
		result = IOUtil.write(outfilename, resultTrack, 12, 6, ',');
		Assert.assertEquals(1, result);
		
		file = new File(outfilename);
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
		reporter = new TrackReporter(file, 0);
		reporter.printReport();
	

	}

}
