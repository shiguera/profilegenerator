package com.mlab.pg.valign;

import java.util.ArrayList;
import java.util.List;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.xyfunction.Polynom2;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

/**
 * Implementación genérica del interface VerticalProfileAlign
 * Incluye el método derivative()
 * 
 * @author shiguera
 *
 */
public abstract class AbstractVerticalProfileAlign implements VerticalProfileAlign {

	protected DesignSpeed designSpeed;
	protected double startS;
	protected double endS;
	protected Polynom2 polynom;

	// Constructor
	protected AbstractVerticalProfileAlign(DesignSpeed dspeed, Polynom2 polynom, double starts, double ends) {
		this.designSpeed = dspeed;
		this.polynom = polynom;
		this.startS = starts;
		this.endS = ends;
		
	}


	
	// Interface XYFunction
	@Override
	public double getY(double x) {
		return polynom.getY(x);
	}
	
	@Override
	public double getTangent(double x) {
		return polynom.getTangent(x);
	}
	
	@Override
	public double getCurvature(double x) {
		return polynom.getCurvature(x);
	}


	// Interface VAlign
	@Override
	public DesignSpeed getDesignSpeed() {
		return designSpeed;
	}
	@Override
	public Polynom2 getPolynom2() {
		return polynom;
	}
	@Override
	public double getStartS() {
		return startS;
	}
	@Override
	public void setStartS(double starts) {
		this.startS = starts;
	}

	@Override
	public double getEndS() {
		return endS;
	}
	@Override
	public void setEndS(double ends) {
		this.endS = ends;
	}

	@Override
	public XYVectorFunction getSample(double startx, double endx, double space) {
		List<double[]> list = new ArrayList<double[]>();
		if( (startx < this.startS) | (startx > this.endS) | 
				(startx == this.endS) ) {
			startx = this.startS;
		}
		if( (endx < startx) | (endx > this.endS) ) {
			endx = this.endS;
		}
		double pos = startx;
		while(pos <= endx) {
			list.add(new double[]{pos, this.polynom.getY(pos)});
			pos += space;
		}
		if (pos<endS) {
			list.add(new double[]{endS, getY(endS)});
		}
		return new XYVectorFunction(list);
	}
	

	// Interface VerticalProfileAlign
	@Override
	public double getStartZ() {
		return polynom.getY(startS);
	}
	@Override
	public double getEndZ() {
		return polynom.getY(endS);
	}
	@Override
	public double getStartTangent() {
		return polynom.getTangent(startS);
	}
	@Override
	public double getEndTangent() {
		return polynom.getTangent(endS);
	}

	/**
	 * Longitud medida a lo largo del eje X
	 */
	@Override
	public double getLength() {
		return endS - startS;
	}

	/**
	 * La implementación corre a cargo de las clases derivadas. Las 
	 * VStraightAlign devolverán el valor de su pendiente. Las 
	 * vertical curves, VParaboleAlign, devolverán Double.NaN 
	 */
	@Override
	public abstract double getSlope();
	
	/**
	 * La implementación corre a cargo de las clases derivadas. Las 
	 * VStraightAlign devolverán Double.NaN. Las 
	 * vertical curves, VParaboleAlign, devolverán el valor 
	 * de su parámetro Kv 
	 */
	@Override
	public abstract double getKv();

	/**
	 * Obtiene la alineación GradeProfileAlign derivada
	 * 
	 * @return Un GradeProfileAlign derivada del VerticalProfileAlign 
	 */
	@Override
	public GradeProfileAlign derivative() {
		GradeProfileAlign galign = null;
		if(getClass().isAssignableFrom(GradeAlign.class)) {
			double g = getSlope();
			Straight r = new Straight(g,0.0);
			galign = new GradeProfileAlign(designSpeed, r, getStartS(), getEndS());
		} else if (getClass().isAssignableFrom(VerticalCurveAlign.class)) {
			Straight r = new Straight(getPolynom2().getA1(), getPolynom2().getA2()*2.0);
			galign = new GradeProfileAlign(designSpeed, r, getStartS(), getEndS());
		}
		return galign;
	}

	@Override
	public String toString() {
		return String.format("%12.6f %12.6f %12.6f %12.6f %12.6f %12.6f %12.6f %12.6f %12.6f %12.6f %10s", 
				startS, polynom.getY(startS), polynom.getTangent(startS),
				getLength(), endS, polynom.getY(endS), polynom.getTangent(endS),
				polynom.getA0(), polynom.getA1(), polynom.getA2(), (Double.isNaN(getKv())?"-":String.format("%9.1f", getKv())));
	}
	
}
