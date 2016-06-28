package com.mlab.pg.reconstruction;

import java.util.ArrayList;

public class PointTypeSegmentArray extends ArrayList<PointTypeSegment> {
	
	private static final long serialVersionUID = 1L;

	public PointTypeSegmentArray() {
	
	}
	
	public PointTypeSegmentArray(PointTypeArray pointTypes) {
		PointTypeSegment currentSegment = null;
		
		for(int i=0; i<pointTypes.size(); i++) {
			if(currentSegment == null) {
				currentSegment = new PointTypeSegment(i,i,pointTypes.get(i));
				continue;
			}
			if(pointTypes.get(i)==currentSegment.getPointType()) {
				currentSegment.setEnd(i);
				continue;
			} else {
				add(currentSegment);
				currentSegment = new PointTypeSegment(i,i,pointTypes.get(i));
			}
		}
		add(currentSegment);		
	}
	
	public static PointTypeSegmentArray makeSegments(PointTypeArray pointTypes) {
		PointTypeSegmentArray pointTypeSegments = new PointTypeSegmentArray();
		PointTypeSegment currentSegment = null;
		
		for(int i=0; i<pointTypes.size(); i++) {
			if(currentSegment == null) {
				currentSegment = new PointTypeSegment(i,i,pointTypes.get(i));
				continue;
			}
			if(pointTypes.get(i)==currentSegment.getPointType()) {
				currentSegment.setEnd(i);
				continue;
			} else {
				pointTypeSegments.add(currentSegment);
				currentSegment = new PointTypeSegment(i,i,pointTypes.get(i));
			}
		}
		
		pointTypeSegments.add(currentSegment);
		
		return pointTypeSegments;
	}

	@Override
	public PointTypeSegmentArray clone() {
		PointTypeSegmentArray copy = new PointTypeSegmentArray();
		for(int i=0; i<this.size(); i++) {
			copy.add(this.get(i));
		}
		return copy;
	}
}
