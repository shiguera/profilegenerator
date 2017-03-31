package com.mlab.pg;

import java.io.File;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;

import com.mlab.pg.util.IOUtil;
import com.mlab.pg.xyfunction.XYVectorFunction;
import com.mlab.pg.xyfunction.XYVectorFunctionCsvReader;
import com.mlab.pg.xyfunction.XYVectorFunctionReader;


public class M607_Axis_CalculoErroresConIGN {

	static Logger LOG = Logger.getLogger(M607_Axis_CalculoErroresConIGN.class);
	
	public M607_Axis_CalculoErroresConIGN() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		LOG.debug("M607_Axis_CalculoErroresConIGN.main()");
		
		String path = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M607/TracksGarmin/";
		File file1 = new File(path, "M607_EjeCalculado_TramoIGN.csv");
		Assert.assertNotNull(file1);
		double[][] fejed = IOUtil.read(file1, ",", 1);		
		XYVectorFunction feje = new XYVectorFunction(Arrays.asList(fejed));
		System.out.println(feje.size());
		File file2 = new File(path + "M607_EjeIGN.csv");
		Assert.assertNotNull(file2);
		double[][] fignd = IOUtil.read(file2, ",", 1);		
		XYVectorFunction fign = new XYVectorFunction(Arrays.asList(fignd));
		System.out.println(fign.size());
		double ecm = feje.ecm(fign);
		double error=0.0, errormax=0.0, errormed=0.0, length=0.0;
		double xlast = feje.getX(0);
		double ylast = feje.getY(0);
		for(int i=0; i<fejed.length; i++) {
			int nearest = nearestInFunction2(i, fejed, fignd);
			double x1 = fejed[i][0];
			double y1 = fejed[i][1];
			double x2 = fignd[nearest][0];
			double y2 = fignd[nearest][1];
			double dist = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
			length = length + Math.sqrt((x1-xlast)*(x1-xlast) + (y1-ylast)*(y1-ylast));
			xlast = x1;
			ylast = y1;
			error += dist;
			if(dist>errormax) {
				if(dist>20) {
					System.out.println("ERROR " + i);
				}
				errormax=dist;
			}
		}
		errormed = error / feje.size();
		System.out.println("Longitud: " + length);
		System.out.println("Puntos  : " + feje.size());
		System.out.println("ECM : " + ecm);
		System.out.println("Error medio: " + errormed);
		System.out.println("Error max  : " + errormax);
		System.out.println("ECM : " + ecm);
		
	}
	public static int nearestInFunction2(int indexInFunction1, double[][] track1, double[][] track2) {
		double x1 = track1[indexInFunction1][0];
		double y1 = track1[indexInFunction1][1];
		double d = Math.sqrt((x1-track2[0][0])*(x1-track2[0][0]) + (y1-track2[0][1])*(y1-track2[0][1]));
		double dmin = d;
		int indexMin = 0;
		for(int i=1; i<track2.length; i++) {
			d = Math.sqrt((x1-track2[i][0])*(x1-track2[i][0]) + (y1-track2[i][1])*(y1-track2[i][1]));
			if(d<dmin) {
				dmin = d;
				indexMin = i;
			}
		}
		return indexMin;
	}

}
