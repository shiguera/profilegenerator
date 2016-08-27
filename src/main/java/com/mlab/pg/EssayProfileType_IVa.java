package com.mlab.pg;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.random.RandomProfileFactory;
import com.mlab.pg.random.RandomProfileType_IVa_Factory;

public class EssayProfileType_IVa {
	
	private static Logger LOG = Logger.getLogger(EssayProfileType_IVa.class);
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");	
		LOG.debug("EssayProfileType_IIa.main()");
		RandomProfileFactory profileFactory = new RandomProfileType_IVa_Factory();
		EssayFactory essayFactory = new EssayFactory(profileFactory);
		essayFactory.setEssaysCount(10000);
		essayFactory.setPointSeparation(10);
		essayFactory.setMobileBaseSize(5);
		essayFactory.setThresholdSlope(1.5e-5);
		essayFactory.setDisplayProfiles(false);
		essayFactory.setRandomPointSeparation(false);
		
		essayFactory.doEssays();
	}

}
