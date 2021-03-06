package com.mlab.pg.essays.roads.M607.Garmin;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;

import com.mlab.pg.trackprocessor.TrackAverage;
import com.mlab.pg.trackprocessor.TrackUtil;
import com.mlab.pg.util.IOUtil;

public class M607_Garmin_CalculateAxis {

	public M607_Garmin_CalculateAxis() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		
		String path = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksGarmin";
		String filename1 = "M607_Asc_2017-03-09.csv";
		String filename2 = "M607_Desc_2017-03-09.csv";
		
		File file1 = new File(IOUtil.composeFileName(path, filename1));
		Assert.assertNotNull(file1);
		Assert.assertTrue(file1.exists());
		File file2 = new File(IOUtil.composeFileName(path, filename2));
		Assert.assertNotNull(file2);
		Assert.assertTrue(file2.exists());

//		String outfilename = "M607_Desc_2017-03-09_Inverted.csv";
//		String invertedname = TrackUtil.invert(path, filename2, outfilename, 1);
//		File file3 = new File(path,invertedname);
		
		double[][] track1 = IOUtil.read(file1, ",", 1);
		double[][] track2 = IOUtil.read(file2, ",",1);
		TrackAverage averager = new TrackAverage();
		double[][] resultTrack = averager.average(track1, track2);
		
		String outfilenamecomplete = IOUtil.composeFileName(path, "M607_Asc_2017-03-09_Axis.csv");
		int result = IOUtil.write(outfilenamecomplete, resultTrack, 12, 6, ',');
		Assert.assertEquals(1, result);
	}
}
