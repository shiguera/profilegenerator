package com.mlab.pg.random;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import com.mlab.pg.valign.VerticalProfile;

public class RandomProfileGenerator extends AbstractRandomProfileFactory {
	
	Logger LOG = Logger.getLogger(RandomProfileGenerator.class);

	public RandomProfileGenerator() {
		
	}

	@Override
	public VerticalProfile createRandomProfile() {
		LOG.debug("createRandomProfile()");
		RandomProfileFactory factory = null;
		ProfileType type = ProfileType.randomType();
		LOG.debug("ProfileType generated : Type " + type.name());
		switch (type) {
		case I: 
			factory = new RandomProfileType_I_Factory();
			break;
		case IIa: 
			factory = new RandomProfileType_IIa_Factory();
			break;
		case IIb: 
			factory = new RandomProfileType_IIb_Factory();
			break;
		case III: 
			factory = new RandomProfileType_III_Factory();
			break;
		case IVa: 
			factory = new RandomProfileType_IVa_Factory();
			break;
		case IVb: 
			factory = new RandomProfileType_IVb_Factory();
			break;
		case V: 
			factory = new RandomProfileType_V_Factory();
			break;
		case VI: 
			factory = new RandomProfileType_VI_Factory();
			break;
		case VII: 
			factory = new RandomProfileType_VII_Factory();
			break;
		case VIII: 
			factory = new RandomProfileType_VIII_Factory();
			break;
		
		}			
		return factory.createRandomProfile();
	}
}
