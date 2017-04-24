package com.mlab.pg.essays.roads.M513.RoadRecorder;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;

import com.mlab.pg.trackprocessor.TrackAverage;
import com.mlab.pg.trackprocessor.TrackUtil;
import com.mlab.pg.util.IOUtil;

public class M513_RoadRecorder_CalculateAxis_3 {

	public M513_RoadRecorder_CalculateAxis_3() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		
		String path = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M513";
		String filename1 = "M513_RoadRecorder_2013-06-27_Axis_1.csv";
		String filename2 = "M513_RoadRecorder_2013-06-27_Axis_2.csv";
		
		File file1 = new File(IOUtil.composeFileName(path, filename1));
		Assert.assertNotNull(file1);
		Assert.assertTrue(file1.exists());
		File file2 = new File(IOUtil.composeFileName(path, filename2));
		Assert.assertNotNull(file2);
		Assert.assertTrue(file2.exists());

		String outfilename = "M513_RoadRecorder_2013-06-27_Axis_2_Inverted.csv";
		String invertedname = TrackUtil.invert(path, filename2, outfilename, 1);
		File file3 = new File(path,invertedname);
		
		double[][] track1 = IOUtil.read(file1, ",", 1);
		double[][] track2 = IOUtil.read(file2, ",",1);
		TrackAverage averager = new TrackAverage();
		double[][] resultTrack = averager.average(track1, track2);
		
		String outfilenamecomplete = IOUtil.composeFileName(path, "M513_RoadRecorder_2013-06-27_Axis_3.csv");
		int result = IOUtil.write(outfilenamecomplete, resultTrack, 12, 6, ',');
		Assert.assertEquals(1, result);
	}
}
