package com.mlab.pg.valign;

import com.mlab.pg.xyfunction.Parabole;
import com.mlab.pg.xyfunction.Straight;

/**
 * Una alineación de un perfil de pendientes.
 * Siempre son rectas, horizontales o inclinadas según 
 * correspondan a un tramo Grade o a un tramo VerticalCurve
 * 
 * @author shiguera
 *
 */

public class GradeProfileAlignment extends GradeAlignment {

	
	
	public GradeProfileAlignment(Straight straight, double starts, double ends) {
		super(straight, starts, ends);
	}

	/**
	 * Obtiene la alineación resultante de integrar.
	 * Para realizar la integración necesita conocer la cota del primer punto 
	 * de la alineación. 
	 * @param startZ ordenada del primer punto del perfil longitudinal
	 * @return VerticalProfileAlign que integra el GradeProfileAlign
	 */
	public VAlignment integrate(double startZ) {
		VAlignment valign = null;
		double s1 = getStartS();
		double g1 = getStartZ();
		double s2 = getEndS();
		double g2 = getEndZ();
		if(isHorizontal()) {
			double a1 = g1;
			double a0 = startZ - s1*g1;
			Straight r = new Straight(a0,a1);
			valign = new GradeAlignment(r, s1, s2);
		} else {
			double a2 = (g2-g1)/2/(s2-s1);
			double a1 = g2 - 2*a2*s2;
			double a0 = startZ - a1*s1 - a2 * s1 * s1;
			Parabole p = new Parabole(a0,a1,a2);
			valign = new VerticalCurveAlignment(p,s1,s2);
		}
		return valign;
	}
	
	public boolean isHorizontal() {
		return polynom.getA1() == 0;
	}

	
	
	@Override
	public double getCurvature(double x) {
		// TODO Not implemented
		return Double.NaN;
	}
	
	@Override
	public String toString() {
		String cad = String.format("%12.6f %12.6f %12.6f %12.6f %12.6f %12.6f %12.6f %12.6f %12.6f %12.6f", 
				startS, polynom.getY(startS), polynom.getTangent(startS),
				getLength(), endS, polynom.getY(endS), polynom.getTangent(endS),
				polynom.getA0(), polynom.getA1(), polynom.getA2());
		return cad;
	}

	
}
