package com.mlab.pg.reconstruction;

import com.mlab.pg.valign.VerticalProfile;

public interface VProfileFilter {
	
	
	VerticalProfile filter(VerticalProfile originalVProfile);

}
