package com.mlab.pg.reconstruction;

import com.mlab.pg.valign.VAlignment;
import com.mlab.pg.valign.VerticalCurveAlignment;
import com.mlab.pg.valign.VerticalProfile;

public class VProfileFilter_ShortAlignments implements VProfileFilter {

	protected double minLength;
	public VProfileFilter_ShortAlignments(double minLength) {
		this.minLength = minLength;
	}

	@Override
	public VerticalProfile filter(VerticalProfile originalVProfile) {
		int countCases = 0;
		for(int i=1; i<originalVProfile.size()-1; i++) {
			VAlignment current = originalVProfile.get(i);
			double length = current.getLength();
			if(current.getClass().isAssignableFrom(VerticalCurveAlignment.class)) {
				if(length < minLength) {
					VAlignment previous = originalVProfile.get(i-1);
					VAlignment following = originalVProfile.get(i+1);
					if(previous.getClass().isAssignableFrom(VerticalCurveAlignment.class) && following.getClass().isAssignableFrom(VerticalCurveAlignment.class)) {
						countCases ++;
					}
				}
			}
		}
		System.out.println("Short VC cases between two VC:" + countCases);
		return null;
	}

}
