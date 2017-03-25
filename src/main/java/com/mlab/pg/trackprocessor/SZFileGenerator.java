package com.mlab.pg.trackprocessor;

import org.apache.log4j.Logger;

import com.mlab.pg.util.IOUtil;

public class SZFileGenerator {

	Logger LOG = Logger.getLogger(getClass());
	
	public SZFileGenerator() {

	}

	public int generateSZFile(double[][] trackXYZ, String outfilename) {
		int pointCount = trackXYZ.length;
		double[][] sz = new double[pointCount][2];
		sz[0] = new double[]{0.0, trackXYZ[0][2]};
		for(int i=1; i<pointCount; i++) {
			double x1 = trackXYZ[i-1][0];
			double y1 = trackXYZ[i-1][1];
			double x2 = trackXYZ[i][0];
			double y2 = trackXYZ[i][1];
			double z2 = trackXYZ[i][2];
			double incs = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
			double s = sz[i-1][0] + incs;
			sz[i] = new double[]{s,z2};
		}
		
		int result = IOUtil.write(outfilename, sz, 12, 6, ',');
		return result;
	}
	
}
