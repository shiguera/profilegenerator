package com.mlab.pg;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;

import com.mlab.pg.trackprocessor.TrackAverage;
import com.mlab.pg.util.IOUtil;


public class MakeAxis_M607_FromGarmin {

	static Logger LOG = Logger.getLogger(MakeAxis_M607_FromGarmin.class);
	
	public MakeAxis_M607_FromGarmin() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");	
		LOG.debug("main()");
		
		
		String filename1 = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/M607_Asc_2017-03-10.csv";
		File file1 = new File(filename1);
		Assert.assertTrue(file1.exists());
		double[][] track1 = IOUtil.read(file1, ",", 1);
		System.out.println("Track1 length: " + track1.length);
		
		String filename2 = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/M607_Desc_2017-03-10.csv";
		File file2 = new File(filename2);
		Assert.assertTrue(file2.exists());
		double[][] track2 = IOUtil.read(file2, ",", 1);
		System.out.println("Track2 length: " + track2.length);
		
		TrackAverage averager = new TrackAverage();
		double[][] axistrack = averager.average(track1, track2);
		IOUtil.write("/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/M607_Axis_2017_03_10.csv", axistrack, 12, 6, ',');
	}

}
