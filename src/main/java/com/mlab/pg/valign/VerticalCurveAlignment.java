package com.mlab.pg.valign;

import com.mlab.pg.xyfunction.Parabole;

/**
 * Representa una alineación vertical en acuerdo parabólico
 *  
 * @author shiguera
 *
 */
public class VerticalCurveAlignment extends AbstractVAlignment {

	
	public VerticalCurveAlignment(Parabole parabole, double starts, double ends) {
		super(parabole, starts, ends);
	}
	public VerticalCurveAlignment(double starts, double startz, double startTangent, double kv, double ends) {
		super(new Parabole(starts, startz, startTangent, kv), starts, ends);
	}
	
}
