package com.mlab.pg.trackprocessor;

import java.io.File;

import org.jfree.util.Log;

import com.mlab.pg.util.IOUtil;
import com.mlab.pg.util.MathUtil;

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
			Log.error("File doesn't exist");
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
	public static String generateSGFileFromXYZFile(File xyzfile, int infileHeadLines) {
		if(!xyzfile.exists()) {
			Log.error("File doesn't exist");
			return "";
		}
		double[][] intrack = readXYZTrack(xyzfile, infileHeadLines);
		if(intrack == null || intrack.length == 0) {
			return "";
		}
		double[][] sgtrack = generateSGTrack(intrack);
		
		String outfilename = IOUtil.removeExtension(xyzfile.getName()) + "_SG.csv";
		String outfilecompletename = IOUtil.composeFileName(xyzfile.getParent(), outfilename);
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
		File infile = new File(infilename);
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
	public static String generateSZFileFromXYZFile(File xyzfile, int infileHeadLines) {
		if(!xyzfile.exists()) {
			return "";
		}
		double[][] intrack = readXYZTrack(xyzfile, infileHeadLines);
		if(intrack == null || intrack.length == 0) {
			return "";
		}
		double[][] sztrack = generateSZTrack(intrack);
		
		String outfilename = IOUtil.removeExtension(xyzfile.getName()) + "_SZ.csv";
		String outfilecompletename = IOUtil.composeFileName(xyzfile.getParent(), outfilename);
		int result = IOUtil.write(outfilecompletename, "S", "Z", sztrack, 12, 6, ',');
		if (result==-1) {
			return "";
		}
		return outfilename;
	}

	/**
	 * Invierte un fichero con un double[][] y lo escribe en un fichero de salida
	 * @param path Ruta del fichero de entrada
	 * @param infilename nombre sin extensión del fichero de entrada
	 * @param outfilename nombre sin extensión del fichero de salida
	 * @param headerLineCount lineas de cabecera
	 * 
	 * @return nombre sin extension del fichero de salida
	 */
	public static String invert(String path, String infilename, String outfilename, int headerLineCount) {
		File infile = new File(IOUtil.composeFileName(path, infilename));
		if(!infile.exists()) {
			return "";
		}
		double[][] intrack = IOUtil.read(infile, ",", headerLineCount);
		if(intrack == null || intrack.length == 0) {
			return "";
		}
		double[][] outtrack = MathUtil.invert(intrack);
		String outfilenamecomplete = IOUtil.composeFileName(path, outfilename);
		int result = IOUtil.write(outfilenamecomplete, outtrack, 12, 6, ',');
		if(result == 1){
			return outfilename;
		} else {
			return "";
		}
	}
	/**
	 * Invierte un fichero con un double[][2] y lo escribe en un fichero de salida, 
	 * escribiendo cabeceras de columna
	 * @param path Ruta del fichero de entrada
	 * @param firstheader Cabecera de la primera columna
	 * @param secondheader Cabecera de la segunda columna
	 * @param infilename nombre sin extensión del fichero de entrada
	 * @param outfilename nombre sin extensión del fichero de salida
	 * @param headerLineCount lineas de cabecera
	 * 
	 * @return nombre sin extension del fichero de salida
	 */
	public static String invert(String path, String firstheader, String secondheader, String infilename, String outfilename, int headerLineCount) {
		File infile = new File(IOUtil.composeFileName(path, infilename));
		if(!infile.exists()) {
			return "";
		}
		double[][] intrack = IOUtil.read(infile, ",", headerLineCount);
		if(intrack == null || intrack.length == 0) {
			return "";
		}
		double[][] outtrack = MathUtil.invert(intrack);
		String outfilenamecomplete = IOUtil.composeFileName(path, outfilename);
		int result = IOUtil.write(outfilenamecomplete, firstheader, secondheader, outtrack, 12, 6, ',');
		if(result == 1){
			return outfilename;
		} else {
			return "";
		}
	}

	/**
	 * Invierte un fichero con un double[][3] y lo escribe en un fichero de salida, 
	 * escribiendo cabeceras de columna
	 * @param path Ruta del fichero de entrada
	 * @param firstheader Cabecera de la primera columna
	 * @param secondheader Cabecera de la segunda columna
	 * @param thirdheader Cabecera de la tercera columna del fichero de salida
	 * @param infilename nombre sin extensión del fichero de entrada
	 * @param outfilename nombre sin extensión del fichero de salida
	 * @param headerLineCount lineas de cabecera
	 * 
	 * @return nombre sin extension del fichero de salida
	 */
	public static String invert(String path, String firstheader, String secondheader, String thirdheader, String infilename, String outfilename, int headerLineCount) {
		File infile = new File(IOUtil.composeFileName(path, infilename));
		if(!infile.exists()) {
			return "";
		}
		double[][] intrack = IOUtil.read(infile, ",", headerLineCount);
		if(intrack == null || intrack.length == 0) {
			return "";
		}
		double[][] outtrack = MathUtil.invert(intrack);
		String outfilenamecomplete = IOUtil.composeFileName(path, outfilename);
		int result = IOUtil.write(outfilenamecomplete, firstheader, secondheader, thirdheader, outtrack, 12, 6, ',');
		if(result == 1){
			return outfilename;
		} else {
			return "";
		}
	}

	public static double[][] readXYZTrack(File infile, int infileHeadLines) {
		double[][] d = IOUtil.read(infile, "," , infileHeadLines);
		return d;
	}
	/**
	 * Genera el track SZ a partir del track XYZ
	 * @param trackXYZ
	 * @return
	 */
	public static double[][] generateSZTrack(double[][] trackXYZ) {
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
	
	/**
	 * Genera el track SG a partir del track XYZ
	 * @param trackXYZ
	 * @return
	 */
	public static double[][] generateSGTrack(double[][] trackXYZ) {
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
