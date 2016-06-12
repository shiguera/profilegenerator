package com.mlab.pg.norma;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.random.RandomFactory;

public class TestRandomFactory {

	private static Logger LOG = Logger.getLogger(TestRandomFactory.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}

	@Test
	public void testRandomDesignSpeed() {
		LOG.debug("testRandomDesigSpeed()");
		int[] vals = new int[DesignSpeed.values().length];
		for(int i=0; i<vals.length; i++) {
			vals[i]=0;
		}
		for(int i=0; i<100;i++) {
			DesignSpeed ds = RandomFactory.randomDesignSpeed();
			int value = ds.ordinal();
			vals[value]++;
		}
		for(int i=0; i<vals.length; i++) {
			Assert.assertTrue(vals[i]>0);
			// LOG.debug("ocurrencias de " + DesignSpeed.values()[i] + ": " + vals[i]);
		}
	}
}
