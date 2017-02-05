package com.mlab.pg.graphics;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.xyfunction.XYVectorFunction;
import com.mlab.pg.xyfunction.XYVectorFunctionCsvReader;

import junit.framework.Assert;

public class TestCharter {

	private static Logger LOG = Logger.getLogger(TestCharter.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");	
		LOG.debug("before()");
	}

	@Test
	public void testCharterConstructor() {
		LOG.debug("testCharterConstructor()");
		Charter charter = new Charter("Gráfico de pruebas", "X", "Y");
		Assert.assertNotNull(charter);
		
		XYVectorFunction data = readData();
		Assert.assertNotNull(data);
		
		charter.addXYVectorFunction(data);
		Assert.assertEquals(1, charter.getSeriesCount());
		Assert.assertEquals(charter.getSeries(0).getItemCount(), data.size());
		
		
	}
	
	private XYVectorFunction readData() {
		LOG.debug("readData()");
		URL url = ClassLoader.getSystemResource("N-320_xyvector_fragment.csv");
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		
		return data;
	}
}

