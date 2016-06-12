package com.mlab.pg.reconstruction;

import com.mlab.pg.xyfunction.IntegerInterval;

public class PointTypeSegment extends IntegerInterval {

	protected PointType pointType;
	public PointType getPointType() {
		return pointType;
	}
	public void setPointType(PointType pointType) {
		this.pointType = pointType;
	}
	public PointTypeSegment(int start, int end, PointType type) {
		super(start, end);
		this.pointType = type;
	}
	
	@Override
	public String toString() {
		return String.format("(%d, %d, %s)", start, end, pointType) ;
	}

}
