package com.mlab.pg.reconstruction;

import java.util.ArrayList;

public class ParameterIntervalArray extends ArrayList<ParameterInterval> {

	private static final long serialVersionUID = 1L;

	public ParameterIntervalArray() {
		super();
	}
	
	public ReconstructionParameters getParameters(double x) {
		for(int i=0; i<size(); i++) {
			ParameterInterval interval = get(i);
			if(interval.contains(x)) {
				return interval.getParameters();
			}
		}
		return null;
	}
	
	

}
