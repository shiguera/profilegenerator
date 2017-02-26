package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.VerticalCurveAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class TestBorderPointsExtractor {

	private final static Logger LOG = Logger.getLogger(TestBorderPointsExtractor.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	
	@Test
	public void testWithSampleProfile1() throws NullTypeException {
		LOG.debug("testWithSampleProfile1()");
		VerticalProfile profile = getSampleProfile1();
		VerticalGradeProfile gradeprofile = profile.derivative();
		//System.out.println(gradeprofile);
		double starts = gradeprofile.getStartS();
		double ends = gradeprofile.getEndS();
		double space = 5.0;
		XYVectorFunction originalGradePoints = gradeprofile.getSample(starts, ends, space, true);
		double thresholdSlope = 1e-5;
		int baseSize = 3;
		BorderPointsExtractor extractor = new BorderPointsExtractor(originalGradePoints, baseSize, thresholdSlope);
		System.out.println(extractor.getBorderPointIndexes());
		System.out.println(extractor.getResultTypeIntervalArray());
		
		SegmentMaker maker = new SegmentMaker(originalGradePoints, baseSize, thresholdSlope);
		System.out.println(maker.getResultTypeSegmentArray());
	}
	/**
	 * Una VC(S0=0;G0=0.02;Kv=3000;L=80) seguida de una grade con pendiente=0.04672
	 * DesignSpeed = 40
	 * El punto VCE está en s=80, que corresponde al índice i=16 en la XYVectorFunction
	 */
	private VerticalProfile getSampleProfile1() {
		LOG.debug("getSampleProfile1()");
		double s0 = 0.0;
		double z0 = 0.0;
		double g0 = 0.02;
		double kv = 3000.0;
		double ends = 80.0;
		VerticalCurveAlignment vc = new VerticalCurveAlignment(s0, z0, g0, kv, ends);		
		Straight r = new Straight(80.0, vc.getY(80.0), vc.getTangent(80.0));
		GradeAlignment grade = new GradeAlignment(r, 80.0, 160.0);

		VerticalProfile profile = new VerticalProfile();
		profile.add(vc);
		profile.add(grade);
		return profile;
	}
}
