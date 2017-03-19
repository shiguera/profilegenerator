package com.mlab.pg.reconstruction;

import com.mlab.pg.valign.VAlignment;
import com.mlab.pg.valign.VerticalProfile;

public class CheckEndingsWithBeginnings implements CheckProfile {

	double TOL = 1e-4;
	
	public CheckEndingsWithBeginnings() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean checkProfile(VerticalProfile vprofile) {
		for(int i=1; i<vprofile.size(); i++) {
			VAlignment current = vprofile.get(i);
			VAlignment previous= vprofile.get(i-1);
			
			double previousEndS = previous.getEndS();
			double currentStartS = current.getStartS();
			if(Math.abs(currentStartS-previousEndS)>TOL) {
				return false;
			}
			
			double previousEndZ = previous.getEndZ();
			double currentStartZ = current.getStartZ();
			if(Math.abs(currentStartZ - previousEndZ)>TOL) {
				return false;
			}

		}
		
		return true;
	}

}
