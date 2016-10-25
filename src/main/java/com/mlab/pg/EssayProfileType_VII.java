package com.mlab.pg;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.mlab.pg.random.RandomProfileFactory;
import com.mlab.pg.random.RandomProfileType_VII_Factory;

/**
 * Profile type VII = upgrade + crest + sag + upgrade
 * @author shiguera
 *
 */
public class EssayProfileType_VII {
	
	private static Logger LOG = Logger.getLogger(EssayProfileType_VII.class);
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");	
		//LOG.debug("EssayProfileType_VII.main()");
		RandomProfileFactory profileFactory = new RandomProfileType_VII_Factory();
		profileFactory.setMinGradeLength(50.0);
		profileFactory.setMinVerticalCurveLength(50.0);
		
		profileFactory.setGradeLengthIncrement(10.1);
		profileFactory.setVerticalCurveLengthIncrement(10.1);

		EssayFactory essayFactory = new EssayFactory(profileFactory);
		essayFactory.setEssaysCount(1000);
		essayFactory.setThresholdSlope(1.5e-6);

		essayFactory.setDisplayProfiles(false);
		essayFactory.setRandomPointSeparation(false);
		essayFactory.setTryWithLessThresholdSlope(true);
		
		essayFactory.setPointSeparation(10.0);
		essayFactory.setMobileBaseSize(11);
		
		essayFactory.doEssays();
	}

}
