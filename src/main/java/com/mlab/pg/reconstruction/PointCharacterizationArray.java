package com.mlab.pg.reconstruction;

import java.util.ArrayList;
import java.util.Collection;

public class PointCharacterizationArray extends ArrayList<PointCharacterization> {

	private static final long serialVersionUID = 1L;

	public PointCharacterizationArray() {
		super();
	}

	public PointCharacterizationArray(int initialCapacity) {
		super(initialCapacity);
	}

	public PointCharacterizationArray(Collection<? extends PointCharacterization> c) {
		super(c);
	}

	
}
