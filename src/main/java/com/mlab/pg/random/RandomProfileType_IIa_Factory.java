package com.mlab.pg.random;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.VerticalCurveAlignment;
import com.mlab.pg.valign.VerticalProfile;

/**
 * Genera un perfil aleatorio tipo II a = upgrade + crest + crest + upgrade
 * @author shiguera
 *
 */
public class RandomProfileType_IIa_Factory extends AbstractRandomProfileFactory {
	Logger LOG = Logger.getLogger(RandomProfileType_IIa_Factory.class);
	
	@Override
	public VerticalProfile createRandomProfile() {
		VerticalProfile vp = new VerticalProfile();
		
		// Generar tres pendientes positivas
		double[] slopes = RandomGradeFactory.generateThreeOrderedSlopes(minSlope, maxSlope, slopeIncrement);
		
		// Generar alineacion grade de entrada
		double grade1Length = RandomFactory.randomUniformLength(minGradeLength, maxGradeLength, gradeLengthIncrement);
		GradeAlignment grade1 = new GradeAlignment(s0, z0, slopes[2], grade1Length);
		vp.add(grade1);
		
		// Generar crest 1
		double s1 = grade1.getEndS();
		double z1 = grade1.getEndZ();
		double g1 = slopes[2];
		double g2 = slopes[1];
		VerticalCurveAlignment vc1 = randomVerticalCurve(s1, z1, g1, g2);
		vp.add(vc1);
		
		// Generar crest 2
		double s2 = vc1.getEndS();
		double z2 = vc1.getEndZ();
		double g3 = slopes[0];
		double Kv1 = Math.abs(vc1.getKv());
		double Kv2 = Kv1;
		VerticalCurveAlignment vc2 =  null;
		// Fuerzo una diferencia de al menos el 10% en el valor de las Kv
		while(Math.abs((Math.abs(Kv2)- Kv1)/Kv1) < 0.1) {
			//System.out.println(Kv1 + ", " + Kv2);
			vc2 = randomVerticalCurve(s2, z2, g2, g3);
			Kv2 = vc2.getKv();
		}
		vp.add(vc2);

		// Generar alineación grade de salida
		double s3 = vc2.getEndS();
		double z3 = vc2.getEndZ();
		GradeAlignment grade2 = randomGrade(s3, z3, g3);
		vp.add(grade2);
		
		return vp;
	}
	
}
