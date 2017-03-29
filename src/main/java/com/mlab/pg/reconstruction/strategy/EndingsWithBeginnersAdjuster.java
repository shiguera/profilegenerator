package com.mlab.pg.reconstruction.strategy;

import com.mlab.pg.valign.VerticalGradeProfile;

public interface EndingsWithBeginnersAdjuster {

	VerticalGradeProfile adjustEndingsWithBeginnings(VerticalGradeProfile gradeProfile);
}
