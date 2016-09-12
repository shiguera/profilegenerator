package com.mlab.pg.random;

import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.VerticalCurveAlignment;
import com.mlab.pg.valign.VerticalProfile;

/**
 * Genera perfiles aleatorios tipo III = downgrade + sag curve + upgrade
 * @author shiguera
 *
 */
public class RandomProfileType_III_Factory extends AbstractRandomProfileFactory {

	public RandomProfileType_III_Factory() {
		this.factoryName = "RandomProfileType_III_Factory";
		this.description = "Random essays with profiles type III";
	}

	@Override
	public VerticalProfile createRandomProfile() {
		VerticalProfile vp = new VerticalProfile();
		
		// Generar alineacion grade de entrada
		GradeAlignment grade1 = randomDownGrade(s0, z0);
		vp.add(grade1);
		
		// Pendiente de salida de la vertical curve, pendiente de la segunda grade
		double g2 = RandomGradeFactory.randomUniformGradeSlope(minSlope, maxSlope, slopeIncrement);
				
		// Generar vertical curve
		double s1 = grade1.getEndS();
		double z1 = grade1.getEndZ();
		double g1 = grade1.getSlope();
		VerticalCurveAlignment vc = randomVerticalCurve(s1, z1, g1, g2);
		vp.add(vc);

		// Generar alineación grade de salida
		double s2 = vc.getEndS();
		double z2 = vc.getEndZ();
		GradeAlignment grade2 = randomGrade(s2, z2, vc.getEndTangent());
		vp.add(grade2);
		
		return vp;
	}

}
