package com.mlab.pg.essays.roads;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;

import com.mlab.pg.trackprocessor.TrackAverage;
import com.mlab.pg.trackprocessor.TrackUtil;
import com.mlab.pg.util.IOUtil;

public class M513_RoadRecorder_CalculateAxis_1 {

	public M513_RoadRecorder_CalculateAxis_1() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		
		String path = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M513";
		String filename1 = "20130627_132501.csv";
		String filename2 = "20130627_133341.csv";
		
		File file1 = new File(IOUtil.composeFileName(path, filename1));
		Assert.assertNotNull(file1);
		Assert.assertTrue(file1.exists());
		File file2 = new File(IOUtil.composeFileName(path, filename2));
		Assert.assertNotNull(file2);
		Assert.assertTrue(file2.exists());

		String outfilename = "20130627_133341_Inverted.csv";
		String invertedname = TrackUtil.invert(path, filename2, outfilename, 1);
		File file3 = new File(path,invertedname);
		
		double[][] track1 = IOUtil.read(file1, ",", 1);
		double[][] track2 = IOUtil.read(file3, ",",0);
		TrackAverage averager = new TrackAverage();
		double[][] resultTrack = averager.average(track1, track2);
		
		String outfilenamecomplete = IOUtil.composeFileName(path, "M513_RoadRecorder_2013-06-27_Axis_1.csv");
		int result = IOUtil.write(outfilenamecomplete, resultTrack, 12, 6, ',');
		Assert.assertEquals(1, result);
	}
}
