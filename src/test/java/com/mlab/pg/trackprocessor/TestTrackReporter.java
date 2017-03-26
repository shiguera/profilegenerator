package com.mlab.pg.trackprocessor;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestTrackReporter {

	private static final Logger LOG = Logger.getLogger(TestTrackReporter.class);

	@BeforeClass
	public static void beforeClass() {
		PropertyConfigurator.configure("log4j.properties");
		//LOG.debug("beforeClass()");
	}
	@Test
	public void testTracReporter_M607Axis() {
		LOG.debug("testTracReporter_M607Axis()");
		File file1 = new File("/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/mergetracks/M607_Asc_1.csv");
		Assert.assertTrue(file1.exists());
		File file2 = new File("/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/mergetracks/M607_Desc_1_Inverted.csv");
		Assert.assertTrue(file2.exists());
		File file3 = new File("/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/mergetracks/M607_Asc_Average_1.csv");
		Assert.assertTrue(file3.exists());
		
		TrackReporter reporter = new TrackReporter(file1, 1);
		reporter.printReport();
		reporter = new TrackReporter(file2, 0);
		reporter.printReport();
		reporter = new TrackReporter(file3, 0);
		reporter.printReport();		
	}

	@Test
	public void testTracReporter_M607Axis_RoadRecorder() {
		LOG.debug("testTracReporter_M607Axis_RoadRecorder()");
		File file1 = new File("/home/shiguera/ownCloud/tesis/2016-2017/Datos/M-607/tracksRoadRecorder/M607_Asc_1.csv");
		Assert.assertTrue(file1.exists());
		File file2 = new File("/home/shiguera/ownCloud/tesis/2016-2017/Datos/M-607/tracksRoadRecorder/M607_Desc_1_Inverted.csv");
		Assert.assertTrue(file2.exists());
		File file3 = new File("/home/shiguera/ownCloud/tesis/2016-2017/Datos/M-607/tracksRoadRecorder/M607_Asc_Axis.csv");
		Assert.assertTrue(file3.exists());
		
		TrackReporter reporter = new TrackReporter(file1, 1);
		reporter.printReport();
		reporter = new TrackReporter(file2, 0);
		reporter.printReport();
		reporter = new TrackReporter(file3, 0);
		reporter.printReport();		
	}
	@Test
	public void testTracReporter_M608Axis() {
		LOG.debug("testTracReporter_M608Axis()");
		File file1 = new File("/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/M608_Asc_2017-03-09.csv");
		Assert.assertTrue(file1.exists());
		File file2 = new File("/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/M608_Desc_2017-03-09_Inverted.csv");
		Assert.assertTrue(file2.exists());
		File file3 = new File("/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/M608_Asc_2017-03-09_Axis.csv");
		Assert.assertTrue(file3.exists());
		
		TrackReporter reporter = new TrackReporter(file1, 1);
		reporter.printReport();
		reporter = new TrackReporter(file2, 0);
		reporter.printReport();
		reporter = new TrackReporter(file3, 0);
		reporter.printReport();		
	}
	@Test
	public void testTracReporter_M608Axis2() {
		LOG.debug("testTracReporter_M608Axis2()");
		File file1 = new File("/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/M608_Asc_2017-03-10.csv");
		Assert.assertTrue(file1.exists());
		File file2 = new File("/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/M608_Desc_2017-03-10_Inverted.csv");
		Assert.assertTrue(file2.exists());
		File file3 = new File("/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/M608_Asc_Average.csv");
		Assert.assertTrue(file3.exists());
		File file4 = new File("/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/M608_Desc_Average.csv");
		Assert.assertTrue(file4.exists());
		File file5 = new File("/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/M608_FourTracks_Axis.csv");
		Assert.assertTrue(file5.exists());
		
		TrackReporter reporter = new TrackReporter(file1, 1);
		reporter.printReport();
		reporter = new TrackReporter(file2, 0);
		reporter.printReport();
		reporter = new TrackReporter(file3, 0);
		reporter.printReport();		
		reporter = new TrackReporter(file4, 0);
		reporter.printReport();		
		reporter = new TrackReporter(file5, 0);
		reporter.printReport();		

	}

}
