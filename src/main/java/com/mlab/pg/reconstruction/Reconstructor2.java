package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;

import com.mlab.pg.valign.GradeProfileAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class Reconstructor2 {
	
	Logger LOG = Logger.getLogger(Reconstructor2.class);
	
	XYVectorFunction gradeFunction;
	int baseSize;
	double thresholdSlope;
	double startZ;
	
	BorderPointsExtractor extractor;
	VerticalGradeProfile gradeProfile;

	VerticalProfile verticalProfile;
	
	public Reconstructor2(XYVectorFunction gradefunction, int basesize, double thresholdslope, double startz) throws NullTypeException {
		this.gradeFunction = gradefunction;
		this.baseSize = basesize;
		this.thresholdSlope = thresholdslope;
		this.startZ = startz;
		
		extractor = null;
		try {
			extractor = new BorderPointsExtractor(gradefunction, baseSize, thresholdSlope);		
			System.out.println(extractor.getBorderPointIndexes());
		} catch(NullTypeException e) {
			throw e;
		}
		
		generateGradeProfile();
		verticalProfile = gradeProfile.integrate(startZ);
	}

	private void generateGradeProfile() {
		LOG.debug("Reconstructor2.generateGradeProfile()");
		gradeProfile = new VerticalGradeProfile();
		for(int i=1; i<extractor.getBorderPointIndexes().size(); i++) {
			int first = extractor.getBorderPointIndexes().get(i-1);
			double[] r = gradeFunction.rectaMinimosCuadrados(first, i);
			Straight straight = new Straight(r[0], r[1]);
			double starts = gradeFunction.getX(first);
			double ends = gradeFunction.getX(extractor.getBorderPointIndexes().get(i));
			GradeProfileAlignment galign = new GradeProfileAlignment(straight, starts, ends);
			gradeProfile.add(galign);
		}
	}

	// Getters
	public VerticalGradeProfile getGradeProfile() {
		return gradeProfile;
	}

	public VerticalProfile getVerticalProfile() {
		return verticalProfile;
	}

}
