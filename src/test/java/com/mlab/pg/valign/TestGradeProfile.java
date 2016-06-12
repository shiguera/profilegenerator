package com.mlab.pg.valign;

import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.random.RandomFactory;

public class TestGradeProfile {

	static Logger LOG = Logger.getLogger(TestGradeProfile.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	
	@Test
	public void testIntegrate() {
		int numberOfTests = 100;
		double maxEcm = 0.0;
		double minEcm = 0.0;
		Random rnd = new Random();
		for(int i=0; i<numberOfTests; i++) {
			DesignSpeed ds = RandomFactory.randomDesignSpeed();
			double s0 = rnd.nextDouble()*10000;
			double z0 = rnd.nextDouble()*5000;
			int vertexCount = 1 + rnd.nextInt(10);
			VerticalProfile vp = RandomFactory.randomVerticalProfile(ds, s0, z0, vertexCount);
			if(vp == null) {
				LOG.debug(vertexCount);
				LOG.debug(s0);
				LOG.debug(z0);
				LOG.debug(ds);
				Assert.fail();
				return;
			}
			GradeProfile gp = vp.derivative();
			VerticalProfile vpresult = gp.integrate(z0);
			double ecm = vp.ecm(vpresult, 2.0);
			if(i>0) {
				if(ecm > maxEcm) {
					maxEcm = ecm;
				}
				if (ecm < minEcm) {
					minEcm = ecm;
				}
			} else {
				maxEcm = ecm;
				minEcm = ecm;
			}
			
		}
		LOG.debug("maxEcm = " + maxEcm);
		LOG.debug("minEcm = " + minEcm);
	}
}
