package com.mlab.pg.reconstruction;

import com.mlab.pg.valign.VerticalGradeProfile;

public interface EndingsWithBeginnersAdjuster {

	VerticalGradeProfile adjustEndingsWithBeginnings(VerticalGradeProfile gradeProfile);
}
