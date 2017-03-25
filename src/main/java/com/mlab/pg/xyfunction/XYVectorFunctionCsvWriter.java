package com.mlab.pg.xyfunction;

import java.io.File;

import org.apache.log4j.Logger;

import com.mlab.pg.util.IOUtil;

public class XYVectorFunctionCsvWriter {
	private final Logger LOG = Logger.getLogger(XYVectorFunctionCsvWriter.class);
	
	protected XYVectorFunction sample;
	
	public XYVectorFunctionCsvWriter(XYVectorFunction function) {
		this.sample = function;
	}
	
	public boolean write(File csvfile, int width, int precission, char separator) {
		double[][] valarray = sample.getValuesAsArray(new IntegerInterval(0, sample.size()-1));
		LOG.info(valarray.length);
		int result = (IOUtil.write(csvfile.getPath(), valarray
				,width, precission, separator));
		if(result == 1) {
			return true;
		} 
		return false;
	}
	
	public boolean write(File csvfile, int start, int end, int width, int precission, char separator) {
		int result = (IOUtil.write(csvfile.getPath(), sample.getValuesAsArray(new IntegerInterval(start, end))
				,width, precission, separator));
		if(result == 1) {
			return true;
		} 
		return false;
	}
}
