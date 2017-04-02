package com.mlab.pg.trackprocessor;

import java.io.File;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.mlab.pg.MakeAxis_M607_FromGarmin;
import com.mlab.pg.util.IOUtil;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class TrackReporter {

	static Logger LOG = Logger.getLogger(MakeAxis_M607_FromGarmin.class);

	XYVectorFunction functionsz;
	XYVectorFunction functionsg;
	
	
	public TrackReporter(File csvXYZFile, int headlines) {
		
		TrackUtil.generateSGFileFromXYZFile(csvXYZFile.getParent(), csvXYZFile.getName(), headlines);
		
		String filename = TrackUtil.generateSZFileFromXYZFile(csvXYZFile.getParent(), csvXYZFile.getName(), headlines);
		
		double[][] sz = IOUtil.read(new File(csvXYZFile.getParent(),filename), ",", 1);
		functionsz = new XYVectorFunction(Arrays.asList(sz));
	}

	public void printReport() {
		System.out.println("Longitud  : " + getLength());
		System.out.println("Puntos    : " + getPointCount());
		System.out.println("Sep Media : " + getSeparacionMedia());
		System.out.println("H Max     : " + getMaxY());
		System.out.println("H Min     : " + getMinY());
	}
	public double getLength() {
		return functionsz.getEndX()-functionsz.getStartX();
	}
	public int getPointCount() {
		return functionsz.size();
	}
	public double getSeparacionMedia() {
		return functionsz.separacionMedia();
	}
	public double getMaxY() {
		return functionsz.getMaxY();
	}
	public double getMinY() {
		return functionsz.getMinY();
	}

}
