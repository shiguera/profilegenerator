package com.mlab.pg.valign;

import java.util.ArrayList;
import java.util.List;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.xyfunction.Parabole;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

/**
 * Una alineación de un perfil de pendientes.
 * Siempre son rectas, horizontales o inclinadas según 
 * correspondan a un tramo Grade o a un tramo VerticalCurve
 * 
 * @author shiguera
 *
 */

public class GradeProfileAlign implements VAlign {

	DesignSpeed designSpeed;
	Straight straight;
	double startS;
	double endS;
	
	public GradeProfileAlign(DesignSpeed dspeed, Straight straight, double starts, double ends) {
		this.designSpeed = dspeed;
		this.straight = straight;
		this.startS = starts;
		this.endS = ends;
	}

	public double getStartGrade() {
		return straight.getY(startS);
	}
	public double getEndGrade() {
		return straight.getY(endS);
	}

	/**
	 * Obtiene la alineación resultante de integrar.
	 * Para realizar la integración necesita conocer la cota del primer punto 
	 * de la alineación. 
	 * @param galign
	 * @param startZ
	 * @return
	 */
	public VerticalProfileAlign integrate(double startZ) {
		VerticalProfileAlign valign = null;
		double s1 = getStartS();
		double g1 = getStartGrade();
		double s2 = getEndS();
		double g2 = getEndGrade();
		if(isHorizontal()) {
			double a1 = g1;
			double a0 = startZ - s1*g1;
			Straight r = new Straight(a0,a1);
			valign = new GradeAlign(designSpeed, r, s1, s2);
		} else {
			double a2 = (g2-g1)/2/(s2-s1);
			double a1 = g2 - 2*a2*s2;
			double a0 = startZ - a1*s1 - a2 * s1 * s1;
			Parabole p = new Parabole(a0,a1,a2);
			valign = new VerticalCurveAlign(designSpeed, p,s1,s2);
		}
		return valign;
	}
	
	public boolean isHorizontal() {
		return straight.getA1() == 0;
	}
	// Interface XYFunction
	@Override
	public double getY(double x) {
		return straight.getY(x);
	}

	@Override
	public double getTangent(double x) {
		return straight.getTangent(x);
	}

	@Override
	public double getCurvature(double x) {
		return 0;
	}

	// Interface VAlign
	@Override
	public double getStartS() {
		return startS;
	}

	@Override
	public double getEndS() {
		return endS;
	}

	@Override
	public double getLength() {
		return getEndS() - getStartS();
	}
	@Override
	public DesignSpeed getDesignSpeed() {
		return designSpeed;
	}

	@Override
	public Straight getPolynom2() {
		return straight;
	}

	@Override
	public XYVectorFunction getSample(double starts, double ends, double space) {
		List<double[]> list = new ArrayList<double[]>();
		if( (starts < this.startS) | (starts > this.endS) | 
				(starts == this.endS) ) {
			starts = this.startS;
		}
		if( (ends < starts) | (ends > this.endS) ) {
			ends = this.endS;
		}
		double pos = starts;
		while(pos <= ends) {
			list.add(new double[]{pos, this.straight.getY(pos)});
			pos += space;
		}
		if (pos<endS) {
			list.add(new double[]{endS, getY(endS)});
		}
		return new XYVectorFunction(list);
	}

	
}
