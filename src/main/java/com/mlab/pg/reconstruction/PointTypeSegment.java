package com.mlab.pg.reconstruction;

import com.mlab.pg.xyfunction.IntegerInterval;

/**
 * Es una extensión del intervalo entero IntegerInterval con un atributo extra
 * que indica el tipo de PointType del segmento
 * 
 * @author shiguera
 *
 */
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
	
	@Override
	protected PointTypeSegment clone() throws CloneNotSupportedException {
		return new PointTypeSegment(start, end, pointType);
	}

}
