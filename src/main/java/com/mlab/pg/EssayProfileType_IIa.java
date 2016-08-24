package com.mlab.pg;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.random.EssayFactory;
import com.mlab.pg.random.RandomProfileFactory;
import com.mlab.pg.random.RandomProfileType_IIa_Factory;

public class EssayProfileType_IIa {
	
	private static Logger LOG = Logger.getLogger(EssayProfileType_IIa.class);
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");	
		LOG.debug("EssayProfileType_IIa.main()");
		RandomProfileFactory profileFactory = new RandomProfileType_IIa_Factory();
		EssayFactory essayFactory = new EssayFactory(profileFactory);
		essayFactory.setEssaysCount(1000);
		essayFactory.setPointSeparation(3);
		essayFactory.setMobileBaseSize(10);
		essayFactory.setThresholdSlope(1e-5);
		essayFactory.setDisplayProfiles(false);
		essayFactory.setRandomPointSeparation(false);
		
		essayFactory.doEssays();
	}

}
