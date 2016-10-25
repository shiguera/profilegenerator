package com.mlab.pg;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.random.RandomProfileFactory;
import com.mlab.pg.random.RandomProfileType_IVb_Factory;

public class EssayProfileType_IVb {
	
	private static Logger LOG = Logger.getLogger(EssayProfileType_IVb.class);
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");	
		LOG.debug("EssayProfileType_IVb.main()");
		RandomProfileFactory profileFactory = new RandomProfileType_IVb_Factory();
		profileFactory.setMinGradeLength(50.0);
		profileFactory.setMinVerticalCurveLength(50.0);
		
		profileFactory.setGradeLengthIncrement(10.1);
		profileFactory.setVerticalCurveLengthIncrement(10.1);

		EssayFactory essayFactory = new EssayFactory(profileFactory);
		essayFactory.setEssaysCount(1000);
		essayFactory.setThresholdSlope(1.5e-5);

		essayFactory.setDisplayProfiles(false);
		essayFactory.setRandomPointSeparation(false);
		essayFactory.setTryWithLessThresholdSlope(true);
		
		essayFactory.setPointSeparation(10.0);
		essayFactory.setMobileBaseSize(11);
		
		essayFactory.doEssays();
	}

}
