package com.mlab.pg.trackprocessor;

import java.io.File;

import com.mlab.pg.util.IOUtil;

public class TrackInverter {

	public TrackInverter() {
		
	}
	
	public int invert(File infile, String outpath) {
		double[][] result =  IOUtil.read(infile, ",",1);
		int isok  = IOUtil.write(outpath, result, 12, 6, ',');
		return isok;
	}

}
