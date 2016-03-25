package com.mlab.pg.valign;

import com.mlab.pg.norma.DesignSpeed;

/**
 * Clase derivada de VerticalProfile que solo admite alineaciones tipo GradeAlign
 * @author shiguera
 *
 */
public class GradeProfile extends VerticalProfile {

	private static final long serialVersionUID = 1L;

	public GradeProfile(DesignSpeed dspeed) {
		super(dspeed);
		
	}

	@Override
	public boolean add(VAlign align) {
		if(align.getClass().isAssignableFrom(GradeAlign.class)) {
			return super.add(align);
		} else {
			return false;
		}
	}
}
