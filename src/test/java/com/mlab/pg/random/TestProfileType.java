package com.mlab.pg.random;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

public class TestProfileType {

	Logger LOG = Logger.getLogger(getClass());
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	
	@Test
	public void testRandomType() {
		LOG.debug("testRandomType()");
		int numberOfTypes = ProfileType.values().length;
		int[] results = new int[numberOfTypes];
		Arrays.fill(results, 0);
		int numberOfTests = 1000;
		for(int i=0; i<numberOfTests; i++) {
			ProfileType type = ProfileType.randomType();
			results[type.ordinal()]++;
		}
		double frec = numberOfTests / numberOfTypes;
		for(int i=0; i<results.length; i++) {
			if(results[i] > frec + 0.25*frec || results[i] < frec-0.25*frec) {
				LOG.error("Desviación excesiva de frecuencia en " + ProfileType.values()[i]);
				Assert.fail();
			}
		}
	}

}
