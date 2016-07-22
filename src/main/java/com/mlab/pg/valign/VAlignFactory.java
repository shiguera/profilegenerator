package com.mlab.pg.valign;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.xyfunction.Parabole;

public class VAlignFactory {
	
	public static VerticalCurveAlign createVCFrom_PointGradeKvAndFinalSlope(DesignSpeed dspeed, double s0, double z0, double g0, double kv, double gf) {
		Parabole p = new Parabole(s0, z0, g0, kv);
		double sf = p.getSForSlope(gf);
		VerticalCurveAlign vc = new VerticalCurveAlign(dspeed, p, s0, sf);
		return vc;
	}

}
