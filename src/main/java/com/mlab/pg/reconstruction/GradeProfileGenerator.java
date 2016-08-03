package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;

import com.mlab.pg.valign.GradeProfileAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.IntegerInterval;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

/**
 * Genera un GradeProfile a partir de un XYVectorFunction con los puntos originales {si, gi} 
 * y un PointTypeSegmentArray procesado por SegmentMaker y que solo tiene segmentos
 * del tipo Grade y VerticalCurve.
 * Ofrece un metodo getVerticalProfile() que integra el perfil de pendientes y devuelve 
 * el VerticalProfile correspondiente
 * 
 * @author shiguera
 *
 */
public class GradeProfileGenerator {

	Logger LOG = Logger.getLogger(GradeProfileGenerator.class);

	protected XYVectorFunction originalPoints;
	protected PointTypeSegmentArray segments;
	protected VerticalGradeProfile gradeProfile;
	protected VerticalProfile verticalProfile;
	protected SegmentMaker maker;
	
	
	public GradeProfileGenerator(XYVectorFunction originalPoints, int mobilebasesize, double thresholdslope, double startZ) {
		this.originalPoints = originalPoints.clone();
		try {
			maker = new SegmentMaker(originalPoints, mobilebasesize, thresholdslope);
		} catch(CloneNotSupportedException e) {
			LOG.debug("Error en constructor de SegmentMaker " + e.getLocalizedMessage());
		}
		
		this.segments = maker.getProcessedSegments();
		
		gradeProfile = new VerticalGradeProfile();
		for(int i=0; i<segments.size(); i++) {
			int first = segments.get(i).getStart();
			int last = segments.get(i).getEnd();
			IntegerInterval interval = new IntegerInterval(first, last);
			double[] r = originalPoints.rectaMinimosCuadrados(interval);
			Straight straight = new Straight(r[0], r[1]);
			GradeProfileAlignment align = new GradeProfileAlignment(straight, originalPoints.getX(first), originalPoints.getX(last));
			gradeProfile.add(align);
		}
		
		verticalProfile = gradeProfile.integrate(startZ);
		
	}

	public VerticalProfile getVerticalProfile() {
		return verticalProfile;
	}
	
	public XYVectorFunction getOriginalPoints() {
		return originalPoints;
	}
	public PointTypeSegmentArray getSegments() {
		return segments;
	}
	public VerticalGradeProfile getGradeProfile() {
		return gradeProfile;
	}

}
