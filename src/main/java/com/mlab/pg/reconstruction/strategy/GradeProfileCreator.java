package com.mlab.pg.reconstruction.strategy;

import com.mlab.pg.reconstruction.TypeIntervalArray;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.xyfunction.XYVectorFunction;

public interface GradeProfileCreator {
	
	VerticalGradeProfile createGradeProfile(XYVectorFunction originalGradePoints, TypeIntervalArray typeIntervalArray);

}
