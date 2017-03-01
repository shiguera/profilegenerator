package com.mlab.pg.random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.valign.VerticalProfile;

public class TestRandomProfileGenerator {

	Logger LOG = Logger.getLogger(getClass());
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	
	@Test
	public void test() {
		LOG.debug("test()");
		RandomProfileGenerator generator = new RandomProfileGenerator();
		VerticalProfile vprofile = generator.createRandomProfile();
		System.out.println(vprofile);
	}

}
