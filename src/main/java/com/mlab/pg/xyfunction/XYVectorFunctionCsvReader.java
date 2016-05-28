package com.mlab.pg.xyfunction;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

public class XYVectorFunctionCsvReader implements XYVectorFunctionReader {
	private final Logger LOG = Logger.getLogger(XYVectorFunctionCsvReader.class);

	protected File csvFile;
	protected char separator;
	protected boolean skipFirstLine;
	
	public XYVectorFunctionCsvReader(File file, char separator, boolean skipfirstline) {
		this.csvFile = file;
		this.separator = separator;
		this.skipFirstLine = skipfirstline;
	}
	
	@Override
	public XYVectorFunction read() {
		CSVReader reader = null;
		XYVectorFunction result = new XYVectorFunction();
		try {
			reader = new CSVReader(new FileReader(csvFile), separator);
			String[] nextLine;
			if (skipFirstLine) {
				reader.readNext(); // Saltarse las cabeceras
			}
			while ((nextLine = reader.readNext()) != null) {
				double l = Double.parseDouble(nextLine[0].trim());
				double z = Double.parseDouble(nextLine[1].trim());
				result.add(new double[]{l,z});
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
