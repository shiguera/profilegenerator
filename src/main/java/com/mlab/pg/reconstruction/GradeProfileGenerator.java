package com.mlab.pg.reconstruction;

import com.mlab.pg.valign.OldGradeProfile;
import com.mlab.pg.valign.GradeProfileAlign;
import com.mlab.pg.valign.OldVerticalProfile;
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

	protected XYVectorFunction originalPoints;
	protected PointTypeSegmentArray segments;
	protected OldGradeProfile gradeProfile;
	
	public GradeProfileGenerator(XYVectorFunction originalPoints, PointTypeSegmentArray segments) {
		this.originalPoints = originalPoints.clone();
		this.segments = segments.clone();
		gradeProfile = new OldGradeProfile(null);
		for(int i=0; i<segments.size(); i++) {
			int first = segments.get(i).getStart();
			int last = segments.get(i).getEnd();
			IntegerInterval interval = new IntegerInterval(first, last);
			double[] r = originalPoints.rectaMinimosCuadrados(interval);
			Straight straight = new Straight(r[0], r[1]);
			GradeProfileAlign align = new GradeProfileAlign(null, straight, originalPoints.getX(first), originalPoints.getX(last));
			gradeProfile.add(align);
		}
	}

	public OldVerticalProfile getVerticalProfile(double startz) {
		return gradeProfile.integrate(startz);
	}
	
	public XYVectorFunction getOriginalPoints() {
		return originalPoints;
	}
	public PointTypeSegmentArray getSegments() {
		return segments;
	}
	public OldGradeProfile getGradeProfile() {
		return gradeProfile;
	}

}
