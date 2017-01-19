package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;

public class TestProfileCharacteriser {
	
	private final static Logger LOG = Logger.getLogger(TestProfileCharacteriser.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}


}
