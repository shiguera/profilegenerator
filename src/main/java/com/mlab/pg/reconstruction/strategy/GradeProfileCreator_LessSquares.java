package com.mlab.pg.reconstruction.strategy;

import com.mlab.pg.reconstruction.PointType;
import com.mlab.pg.reconstruction.TypeInterval;
import com.mlab.pg.reconstruction.TypeIntervalArray;
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
	public VerticalGradeProfile createGradeProfile(XYVectorFunction originalGradePoints, TypeIntervalArray typeIntervalArray) {
		VerticalGradeProfile gradeProfile = new VerticalGradeProfile();
		for(int i=0; i<typeIntervalArray.size(); i++) {
			TypeInterval currentInterval = typeIntervalArray.get(i);
			int first = currentInterval.getStart();
			int last = currentInterval.getEnd();
			IntegerInterval interval = new IntegerInterval(first, last);
			double[] r = originalGradePoints.rectaMinimosCuadrados(interval);
			Straight straight = new Straight(r[0], r[1]);
			double x1 = originalGradePoints.getX(first);
			double x2 = originalGradePoints.getX(last);
			if(currentInterval.getPointType() == PointType.GRADE) {
				// Hay que forzar que la recta sea horizontal
				// Lo hacemos girándola respecto del punto medio
				double xmed = (x1+x2)/2.0;
				double ymed = originalGradePoints.getY(xmed);
				straight = new Straight(xmed, ymed, 0.0);
			}
			GradeProfileAlignment align = new GradeProfileAlignment(straight, x1, x2);
			//System.out.println(String.format("%f %f", align.getStartZ(), align.getEndZ()));
			gradeProfile.add(align);
		}
		
		return gradeProfile;
	}

}
