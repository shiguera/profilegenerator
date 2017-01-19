package com.mlab.pg.valign;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;

public class TestGradeProfile {

	static Logger LOG = Logger.getLogger(TestGradeProfile.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	
}
