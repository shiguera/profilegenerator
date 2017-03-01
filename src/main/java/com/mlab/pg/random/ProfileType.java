package com.mlab.pg.random;

import java.util.Random;

public enum ProfileType {

	I, IIa, IIb, III, IVa, IVb, V, VI, VII, VIII;
	
	public static ProfileType randomType() {
		Random rnd = new Random();
		int bound = values().length;
		int selected = rnd.nextInt(bound);
		return ProfileType.values()[selected];
	}
	
}
