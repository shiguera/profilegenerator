package com.mlab.pg;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.random.RandomProfileFactory;
import com.mlab.pg.random.RandomProfileType_IIb_Factory;

public class EssayProfileType_IIb {
	
	private static Logger LOG = Logger.getLogger(EssayProfileType_IIb.class);
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");	
		LOG.debug("EssayProfileType_IIb.main()");
		RandomProfileFactory profileFactory = new RandomProfileType_IIb_Factory();
		EssayFactory essayFactory = new EssayFactory(profileFactory);
		essayFactory.setEssaysCount(2000);
		essayFactory.setPointSeparation(8);
		essayFactory.setMobileBaseSize(6);
		essayFactory.setThresholdSlope(1.5e-5);
		essayFactory.setDisplayProfiles(false);
		essayFactory.setRandomPointSeparation(false);
		
		essayFactory.doEssays();
	}

}
