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
		
		double[][] track = IOUtil.read(csvXYZFile, ",", headlines);
		
		String path = csvXYZFile.getParent();
		
		String name = csvXYZFile.getName();
		String namewithoutext = String.copyValueOf(name.toCharArray(), 0, name.length()-4);
		

		String sgfilename = path + "/" + namewithoutext + "_SG.csv";
		System.out.println(sgfilename);
		SGFileGenerator sggenerator = new SGFileGenerator();
		sggenerator.generateSGFile(track, sgfilename);
		
		String szfilename = path + "/" + namewithoutext + "_SZ.csv";
		System.out.println(szfilename);
		SZFileGenerator szgenerator = new SZFileGenerator();
		szgenerator.generateSZFile(track, szfilename);
		
		double[][] sz = IOUtil.read(new File(szfilename), ",", 0);
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
