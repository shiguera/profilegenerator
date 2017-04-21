package com.mlab.pg.essays.roads.M607.Garmin;

import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.trackprocessor.TrackUtil;

public class InvertDescTrack {

	public InvertDescTrack() {

	
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		String path ="/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksGarmin/";
		String infilename = "M607_Desc_2017-03-09.csv";
		String outfilename = "M607_Desc_2017-03-09_Inverted.csv";
		String result = TrackUtil.invert(path, "X", "Y", "Z", infilename, outfilename, 1);
		System.out.println("Result: " + result);
	}
}
