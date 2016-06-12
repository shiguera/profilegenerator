package com.mlab.pg.reconstruction;

import com.mlab.pg.xyfunction.XYVectorFunction;

public class SegmentMaker {

	XYVectorFunction gradeSample;
	int mobileBaseSize;
	double thresholdSlope;
	PointTypeArray pointTypes;
	PointTypeSegmentArray pointTypeSegments;
	
	public SegmentMaker(XYVectorFunction gradesample, int mobilebasesize, double thresholdslope) {
		this.gradeSample = gradesample;
		this.mobileBaseSize = mobilebasesize;
		this.thresholdSlope = thresholdslope;
		ProfileCharacteriser characteriser = new ProfileCharacteriser();
		this.pointTypes = characteriser.characterise(gradeSample, mobileBaseSize, thresholdSlope); 
		this.pointTypeSegments = new PointTypeSegmentArray(pointTypes);
	}

	public XYVectorFunction getGradeSample() {
		return gradeSample;
	}

	public int getMobileBaseSize() {
		return mobileBaseSize;
	}

	public double getThresholdSlope() {
		return thresholdSlope;
	}

	public PointTypeArray getPointTypes() {
		return pointTypes;
	}

	public PointTypeSegmentArray getPointTypeSegments() {
		return pointTypeSegments;
	}

	
	
	
	
	
}
