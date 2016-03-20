package com.mlab.pg.xyfunction;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

public class XYSampleCsvReader implements XYSampleReader {
	private final Logger LOG = Logger.getLogger(XYSampleCsvReader.class);

	protected File csvFile;
	protected char separator;
	
	public XYSampleCsvReader(File file, char separator) {
		this.csvFile = file;
		this.separator = separator;
	}
	
	@Override
	public XYSample read() {
		CSVReader reader = null;
		XYSample result = new XYSampleImpl();
		try {
			reader = new CSVReader(new FileReader(csvFile), separator);
			String[] nextLine;
			reader.readNext(); // Saltarse las cabeceras
			while ((nextLine = reader.readNext()) != null) {
				double l = Double.parseDouble(nextLine[0].trim());
				double z = Double.parseDouble(nextLine[1].trim());
				result.getValues().add(new double[]{l,z});
			}
		} catch (Exception e1) {
			LOG.error("read() :" + e1.getMessage());
			result = null;
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOG.error("read() :" + e.getMessage());
				}
			}
		}		
		return result;
	}

}
