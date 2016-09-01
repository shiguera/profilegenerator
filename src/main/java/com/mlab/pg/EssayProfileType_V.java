package com.mlab.pg;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.random.RandomProfileFactory;
import com.mlab.pg.random.RandomProfileType_V_Factory;

/**
 * Profile type V= upgrade + crest + crest + upgrade
 * @author shiguera
 *
 */
public class EssayProfileType_V {
	
	private static Logger LOG = Logger.getLogger(EssayProfileType_V.class);
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");	
		LOG.debug("EssayProfileType_V.main()");
		RandomProfileFactory profileFactory = new RandomProfileType_V_Factory();
		profileFactory.setMinGradeLength(50.0);
		profileFactory.setMinVerticalCurveLength(50.0);
		
		EssayFactory essayFactory = new EssayFactory(profileFactory);
		essayFactory.setEssaysCount(1000);
		essayFactory.setThresholdSlope(1.5e-5);
		essayFactory.setDisplayProfiles(false);
		essayFactory.setRandomPointSeparation(false);
		essayFactory.setTryWithLessThresholdSlope(false);
		
		essayFactory.setPointSeparation(1.8);
		essayFactory.setMobileBaseSize(33);
		
		essayFactory.doEssays();
	}

}
