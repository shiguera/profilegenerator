package com.mlab.pg.reconstruction;

import java.io.File;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.VerticalCurveAlignment;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.Straight;

public class TestVerticalProfileWriter {
	Logger LOG = Logger.getLogger(getClass());
	
	@BeforeClass
	public static void beforeClass() {
		PropertyConfigurator.configure("log4j.properties");	
	}

	@Test
	public void testWriteProfile() throws URISyntaxException {
		LOG.debug("testWriteProfile()");
		File file = new File("/home/shiguera/ownCloud/workspace/roads/ProfileGenerator/src/main/resources/pruebasWriteProfile.txt");
		
		VerticalProfile profile = getSampleProfile2();
		VerticalProfileWriter.writeVerticalProfile(file, profile, "Prueba de perfil");
		
	}

	/**
	 * Una VC(S0=0;G0=0.005;Kv=6000;L=250) seguida de una grade con pendiente=0.0468
	 * DesignSpeed = 80
	 * El punto VCE está en s=250, que corresponde al índice i=  en la XYVectorFunction
	 */
	private VerticalProfile getSampleProfile2() {
		double s0 = 0.0;
		double z0 = 0.0;
		double g0 = 0.005;
		double kv = 6000.0;
		double ends = 250.0;
		VerticalCurveAlignment vc = new VerticalCurveAlignment(s0, z0, g0, kv, ends);		
		//System.out.println(vc.getEndZ());
		Straight r = new Straight(250.0, vc.getY(250.0), vc.getTangent(250.0));
		GradeAlignment grade = new GradeAlignment( r, 250.0, 500.0);
		//System.out.println(grade.getStartZ());
		VerticalProfile profile = new VerticalProfile();
		profile.add(vc);
		profile.add(grade);
		//System.out.println(profile);
		return profile;
	}
}
