package com.mlab.pg.essays.roads.M325;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;

import com.mlab.pg.xyfunction.XYVectorFunction;
import com.mlab.pg.xyfunction.XYVectorFunctionCsvReader;
import com.mlab.pg.xyfunction.XYVectorFunctionCsvWriter;
import com.mlab.pg.xyfunction.XYVectorFunctionReader;


public class M325_CreateTracks {

	static double space = 20.0;
	static double s0 = 0.0;
	static double sf = 4750;
	
	public M325_CreateTracks() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		
		
		createfileSG();

		createfileSZ();
		
	}
	private static void createfileSG() {
		// Crear fichero SG
		String path = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M325";
		String sgfilename = "M325_ETRS89_SG.csv";
		File sgfile = new File(path, sgfilename);
		Assert.assertTrue(sgfile.exists());

		XYVectorFunctionReader reader = new XYVectorFunctionCsvReader(sgfile, ',', true);
		XYVectorFunction sgf = reader.read();
		Assert.assertNotNull(sgf);

		double s = s0;
		XYVectorFunction newsgf = new XYVectorFunction();
		while(s<sf) {
			double g = sgf.getY(s);
			newsgf.add(new double[]{s, g});
			s = s + space;
		}
		String outfilename = "M325_ETRS89_" + String.format("%02.0fm", space)+ "_SG.csv";
		System.out.println(outfilename);
		File outfile = new File(path, outfilename);
		XYVectorFunctionCsvWriter writer = new XYVectorFunctionCsvWriter(newsgf);
		writer.write(outfile, 12, 6, ',');
	}
	private static void createfileSZ() {
		// Crear fichero SG
		String path = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M325";
		String sgfilename = "M325_ETRS89_SZ.csv";
		File szfile = new File(path, sgfilename);
		Assert.assertTrue(szfile.exists());

		XYVectorFunctionReader reader = new XYVectorFunctionCsvReader(szfile, ',', true);
		XYVectorFunction szf = reader.read();
		Assert.assertNotNull(szf);

		double s = s0;
		XYVectorFunction newszf = new XYVectorFunction();
		while(s<sf) {
			double z = szf.getY(s);
			newszf.add(new double[]{s, z});
			s = s + space;
		}
		String outfilename = "M325_ETRS89_" + String.format("%02.0fm", space)+ "_SZ.csv";
		System.out.println(outfilename);
		File outfile = new File(path, outfilename);
		XYVectorFunctionCsvWriter writer = new XYVectorFunctionCsvWriter(newszf);
		writer.write(outfile, 12, 6, ',');
	}

}
