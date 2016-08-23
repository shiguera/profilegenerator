package com.mlab.pg.random;

import com.mlab.pg.valign.VerticalProfile;

public interface RandomProfileFactory {

	VerticalProfile createRandomProfile();

	
	void setMinKv(double minKv);

	double getMinKv();

	void setMaxKv(double maxKv);

	double getMaxKv();

	void setVerticalCurveLengthIncrement(double verticalCurveLengthIncrement);

	double getVerticalCurveLengthIncrement();

	void setMaxVerticalCurveLength(double maxVerticalCurveLength);

	double getMaxVerticalCurveLength();

	void setMinVerticalCurveLength(double minVerticalCurveLength);

	double getMinVerticalCurveLength();

	void setGradeLengthIncrement(double gradeLengthIncrement);

	double getGradeLengthIncrement();

	void setMaxGradeLength(double maxGradeLength);

	double getMaxGradeLength();

	void setMinGradeLength(double minGradeLength);

	double getMinGradeLength();

	void setSlopeIncrement(double gradeIncrement);

	double getSlopeIncrement();

	void setMaxSlope(double maxGrade);

	double getMaxSlope();

	void setMinSlope(double minGrade);

	double getMinSlope();

	void setZ0(double z0);

	double getZ0();

	void setS0(double s0);

	double getS0();

	

}
