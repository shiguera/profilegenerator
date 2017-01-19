package com.mlab.pg.valign;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;

public class TestVerticalProfile {

	private final static Logger LOG = Logger.getLogger(TestVerticalProfile.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
}
