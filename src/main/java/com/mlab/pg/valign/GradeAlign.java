package com.mlab.pg.valign;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.xyfunction.Straight;

/**
 * Representa una alineación vertical en rampa o pendiente
 * 
 * @author shiguera
 *
 */
public class GradeAlign extends AbstractVerticalProfileAlign {

	
	public GradeAlign(DesignSpeed dspeed,Straight straight, double starts, double ends) {
		super(dspeed, straight, starts, ends);
	}

	
	@Override
	public Straight getPolynom2() {
		return (Straight)super.getPolynom2();
	}
	
	@Override
	public double getSlope() {
		return ((Straight)polynom).getSlope();
	}
	/**
	 * En el caso de las alineaciones en rampa o pendiente 
	 * no tiene sentido este parámetro. Devuelve Double.NaN
	 */
	@Override
	public double getKv() {
		return Double.NaN;
	}


	

}
