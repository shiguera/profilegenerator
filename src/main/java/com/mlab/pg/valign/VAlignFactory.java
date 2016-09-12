package com.mlab.pg.valign;

import com.mlab.pg.xyfunction.Parabole;

public class VAlignFactory {
	
	public static VerticalCurveAlignment createVCFrom_PointGradeKvAndFinalSlope(double s0, double z0, double g0, double kv, double gf) {
		Parabole p = new Parabole(s0, z0, g0, kv);
		double sf = Math.rint(p.getSForSlope(gf)*100.0)/100.0;
		VerticalCurveAlignment vc = new VerticalCurveAlignment(p, s0, sf);
		return vc;
	}

}
