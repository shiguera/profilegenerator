package com.mlab.pg.essays.roads.M607.RoadRecorder;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;

import com.mlab.pg.trackprocessor.TrackAverage;
import com.mlab.pg.trackprocessor.TrackUtil;
import com.mlab.pg.util.IOUtil;

public class M607_RoadRecorder_CalculateAxis {

	public M607_RoadRecorder_CalculateAxis() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		
		String path = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksRoadRecorder";
		String filename1 = "M607_Asc_1.csv";
		String filename2 = "M607_Desc_1.csv";
		
		File file1 = new File(IOUtil.composeFileName(path, filename1));
		Assert.assertNotNull(file1);
		Assert.assertTrue(file1.exists());
		File file2 = new File(IOUtil.composeFileName(path, filename2));
		Assert.assertNotNull(file2);
		Assert.assertTrue(file2.exists());


		double[][] track1 = IOUtil.read(file1, ",", 1);
		double[][] track2 = IOUtil.read(file2, ",",1);
		TrackAverage averager = new TrackAverage();
		double[][] resultTrack = averager.average(track1, track2);
		
		String outfilenamecomplete = IOUtil.composeFileName(path, "M607_RoadRecorder_Axis_2T.csv");
		int result = IOUtil.write(outfilenamecomplete, resultTrack, 12, 6, ',');
		Assert.assertEquals(1, result);
	}
}
