package com.mlab.pg.reconstruction;

import com.mlab.pg.valign.GradeProfileAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.xyfunction.IntegerInterval;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class GradeProfileCreator_LessSquares implements GradeProfileCreator {

	public GradeProfileCreator_LessSquares() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public VerticalGradeProfile createGradeProfile(XYVectorFunction originalGradePoints,
			TypeIntervalArray typeIntervalArray) {
		VerticalGradeProfile gradeProfile = new VerticalGradeProfile();
		for(int i=0; i<typeIntervalArray.size(); i++) {
			int first = typeIntervalArray.get(i).getStart();
			int last = typeIntervalArray.get(i).getEnd();
			IntegerInterval interval = new IntegerInterval(first, last);
			double[] r = originalGradePoints.rectaMinimosCuadrados(interval);
			Straight straight = new Straight(r[0], r[1]);
			GradeProfileAlignment align = new GradeProfileAlignment(straight, originalGradePoints.getX(first), originalGradePoints.getX(last));
			//System.out.println(String.format("%f %f", align.getStartZ(), align.getEndZ()));
			gradeProfile.add(align);
		}
		
		return gradeProfile;
	}

}
