package com.mlab.pg.xyfunction;

import java.util.ArrayList;

public class IntegerIntervalArray extends ArrayList<IntegerInterval>{

	private static final long serialVersionUID = 1L;

	public IntegerIntervalArray() {
		super();
	}
	
	public int getFirstIndex() {
		if(this.size()>0) {
			return get(0).getStart();
		} else {
			return -1;
		}
	}
	public boolean constains(int i) {
		for(int j=0; j<this.size(); j++) {
			IntegerInterval interval = get(j);
			if(interval.contains(i)) {
				return true;
			}
		}
		return false;
	}

}
