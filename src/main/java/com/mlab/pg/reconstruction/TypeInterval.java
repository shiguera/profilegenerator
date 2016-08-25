package com.mlab.pg.reconstruction;

import com.mlab.pg.xyfunction.IntegerInterval;

/**
 * Es una extensión de IntegerInterval con un atributo extra
 * que indica el PointType del segmento
 * 
 * @author shiguera
 *
 */
public class TypeInterval extends IntegerInterval {

	protected PointType pointType;
	
	public TypeInterval(int start, int end, PointType type) {
		super(start, end);
		this.pointType = type;
	}
	
	public TypeInterval copy() {
		return new TypeInterval(start, end, pointType);
	}
	
	public PointType getPointType() {
		return pointType;
	}
	
	public void setPointType(PointType pointType) {
		this.pointType = pointType;
	}
	
	@Override
	public String toString() {
		return String.format("(%d, %d, %s)", start, end, pointType) ;
	}
	
	
}
