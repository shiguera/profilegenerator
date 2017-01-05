package com.mlab.pg.essays;

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
		//LOG.debug("EssayProfileType_V.main()");
		RandomProfileFactory profileFactory = new RandomProfileType_V_Factory();
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
		
		essayFactory.setPointSeparation(2.0);
		essayFactory.setMobileBaseSize(6);
		
		essayFactory.doEssays();
	}

}
