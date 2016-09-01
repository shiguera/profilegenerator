package com.mlab.pg;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.random.RandomProfileFactory;
import com.mlab.pg.random.RandomProfileType_I_Factory;

public class EssayProfileType_I {
	
	private static Logger LOG = Logger.getLogger(EssayProfileType_I.class);
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");	
		LOG.debug("EssayProfileType_I.main()");
		
		RandomProfileFactory profileFactory = new RandomProfileType_I_Factory();
		profileFactory.setMinGradeLength(50.0);
		profileFactory.setMinVerticalCurveLength(50);
		
		EssayFactory essayFactory = new EssayFactory(profileFactory);
		essayFactory.setEssaysCount(1000);
		essayFactory.setThresholdSlope(1e-5);
		
		essayFactory.setDisplayProfiles(false);
		essayFactory.setRandomPointSeparation(false);
		essayFactory.setTryWithLessThresholdSlope(false);
		
		essayFactory.setPointSeparation(7.3);
		essayFactory.setMobileBaseSize(4);
		
		essayFactory.doEssays();
	}

}
