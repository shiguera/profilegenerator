package com.mlab.pg.random;

import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.VerticalCurveAlignment;
import com.mlab.pg.valign.VerticalProfile;

/**
 * Genera perfiles aleatorios tipo IVa = downgrade + sag curve + downgrade
 * @author shiguera
 *
 */
public class RandomProfileType_IIa_Factory extends AbstractRandomProfileFactory {

	public RandomProfileType_IIa_Factory() {
		this.factoryName = "RandomProfileType_IIa_Factory";
		this.description = "Random essays with profiles type IIa";
	}

	@Override
	public VerticalProfile createRandomProfile() {
		VerticalProfile vp = new VerticalProfile();
		
		// Generar dos pendientes positivas, una mayor que otra
		double g1 = RandomGradeFactory.randomUniformGradeSlope(minSlope, maxSlope, slopeIncrement);
		double g2 = RandomGradeFactory.randomUniformGradeSlope(minSlope, maxSlope, slopeIncrement);
		while (g2 == g1) {
			g2 = RandomGradeFactory.randomUniformGradeSlope(minSlope, maxSlope, slopeIncrement);
		}
		double gmax = g1;
		double gmin = g2;
		if(g2 > g1) {
			gmax = g2;
			gmin = g1;
		} 
		g1 = -gmin;
		g2 = -gmax;
		
		// Generar alineacion grade de entrada
		GradeAlignment grade1 = randomGrade(s0, z0, g1);
		vp.add(grade1);
		
		// Generar vertical curve
		double s1 = grade1.getEndS();
		double z1 = grade1.getEndZ();
		g1 = grade1.getSlope();
		VerticalCurveAlignment vc = randomVerticalCurve(s1, z1, g1, g2);
		vp.add(vc);

		// Generar alineación grade de salida
		double s2 = vc.getEndS();
		double z2 = vc.getEndZ();
		GradeAlignment grade2 = randomGrade(s2, z2, g2);
		vp.add(grade2);
		
		return vp;
	}

}
