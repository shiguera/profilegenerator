package com.mlab.pg.random;

import org.apache.log4j.Logger;

import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.VerticalCurveAlignment;
import com.mlab.pg.valign.VerticalProfile;

/**
 * Genera un perfil aleatorio tipo VII = downgrade + sag + crest + downgrade
 * @author shiguera
 *
 */
public class RandomProfileType_VIII_Factory extends AbstractRandomProfileFactory {
	Logger LOG = Logger.getLogger(RandomProfileType_VIII_Factory.class);
	
	public RandomProfileType_VIII_Factory() {
		this.factoryName = "RandomProfileType_VIII_Factory";
		this.description = "Random essays with profiles type VIII";
	}

	@Override
	public VerticalProfile createRandomProfile() {
		VerticalProfile vp = new VerticalProfile();
		
		// Generar tres pendientes positivas
		double[] slopes = RandomGradeFactory.generateThreeOrderedSlopes(minSlope, maxSlope, slopeIncrement);
		double g1, g2, g3;
		// Echo a suertes si la pendiente mayor es la primera o la última. La pendiente del 
		// medio es la menor en todos los casos
		double n = RandomFactory.randomSign();
		g2 = slopes[0];
		if (n==1) {
			g1 = slopes[1];
			g3 = slopes[2];
		} else {
			g1 = slopes[2];
			g3 = slopes[1];
		}
		// Hago negativas todas las pendientes
		g1=-g1;
		g2=-g2;
		g3=-g3;
		
		// Generar alineacion grade de entrada
		double grade1Length = RandomFactory.randomUniformLength(minGradeLength, maxGradeLength, gradeLengthIncrement);
		GradeAlignment grade1 = new GradeAlignment(s0, z0, g1, grade1Length);
		vp.add(grade1);
		
		// Generar crest curve
		double s1 = grade1.getEndS();
		double z1 = grade1.getEndZ();
		VerticalCurveAlignment vc1 = randomVerticalCurve(s1, z1, g1, g2);
		vp.add(vc1);
		
		// Generar sag curve
		double s2 = vc1.getEndS();
		double z2 = vc1.getEndZ();
		//double Kv1 = Math.abs(vc1.getKv());
		//double Kv2 = Kv1;
		VerticalCurveAlignment vc2 = randomVerticalCurve(s2, z2, g2, g3);
		vp.add(vc2);

		// Generar alineación grade de salida
		double s3 = vc2.getEndS();
		double z3 = vc2.getEndZ();
		GradeAlignment grade2 = randomGrade(s3, z3, g3);
		vp.add(grade2);
		
		return vp;
	}
	
}
