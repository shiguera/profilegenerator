package com.mlab.pg.trackprocessor;

import org.apache.log4j.Logger;

import com.mlab.pg.util.IOUtil;

public class SGFileGenerator {

	Logger LOG = Logger.getLogger(getClass());
	
	public SGFileGenerator() {

	}

	public int generateSGFile(double[][] trackXYZ, String outfilename) {
		int pointCount = trackXYZ.length;
		double[][] sg = new double[pointCount][2];
		sg[0] = new double[]{0.0, 0.0};
		for(int i=1; i<pointCount; i++) {
			double x1 = trackXYZ[i-1][0];
			double y1 = trackXYZ[i-1][1];
			double z1 = trackXYZ[i-1][2];
			double x2 = trackXYZ[i][0];
			double y2 = trackXYZ[i][1];
			double z2 = trackXYZ[i][2];
			double incs = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
			double s = sg[i-1][0] + incs;
			double incz = z2-z1;
			double g = incz / incs;
			sg[i] = new double[]{s,g};
		}
		sg[0] = new double[]{0.0, sg[1][1]};
		
		int result = IOUtil.write(outfilename, sg, 12, 6, ',');
		return result;
	}
	
}
