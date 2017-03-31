package com.mlab.pg.trackprocessor;

import java.io.File;

import com.mlab.pg.util.IOUtil;

public class TrackUtil {


	
	/**
	 * Genera el fichero SG a partir del fichero XYZ
	 * 
	 * @return Nombre del fichero, sin el path, si todo va bien
	 *  o cadena vacía si hay errores
	 */
	public static String generateSGFileFromXYZFile(String inpath, String xyzfilename, int infileHeadLines) {
		String infilename = IOUtil.composeFileName(inpath, xyzfilename);
		File infile = new File(infilename);
		if(!infile.exists()) {
			return "";
		}
		double[][] intrack = readXYZTrack(infile, infileHeadLines);
		if(intrack == null || intrack.length == 0) {
			return "";
		}
		double[][] sgtrack = generateSGTrack(intrack);
		
		String outfilename = IOUtil.removeExtension(xyzfilename) + "_SG.csv";
		String outfilecompletename = IOUtil.composeFileName(inpath, outfilename);
		int result = IOUtil.write(outfilecompletename, "S", "G", sgtrack, 12, 6, ',');
		if (result==-1) {
			return "";
		}
		return outfilename;
	}

	/**
	 * Genera el fichero SZ a partir del fichero XYZ
	 * 
	 * @return Nombre del fichero, sin el path, si todo va bien
	 *  o cadena vacía si hay errores
	 */
	public static String generateSZFileFromXYZFile(String inpath, String xyzfilename, int infileHeadLines) {
		String infilename = IOUtil.composeFileName(inpath, xyzfilename);
		File infile = new File(inpath + xyzfilename);
		if(!infile.exists()) {
			return "";
		}
		double[][] intrack = readXYZTrack(infile, infileHeadLines);
		if(intrack == null || intrack.length == 0) {
			return "";
		}
		double[][] sztrack = generateSZTrack(intrack);
		
		String outfilename = IOUtil.removeExtension(xyzfilename) + "_SZ.csv";
		String outfilecompletename = IOUtil.composeFileName(inpath, outfilename);
		int result = IOUtil.write(outfilecompletename, "S", "Z", sztrack, 12, 6, ',');
		if (result==-1) {
			return "";
		}
		return outfilename;
	}

	

	private static double[][] generateSZTrack(double[][] trackXYZ) {
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
		return sz;
	}
	private static double[][] readXYZTrack(File infile, int infileHeadLines) {
		double[][] d = IOUtil.read(infile, "," , 1);
		return d;
	}
	
	private static double[][] generateSGTrack(double[][] trackXYZ) {
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
			return sg;
	}
}
