package com.mlab.pg.valign;

import java.util.ArrayList;
import java.util.List;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.xyfunction.Polynom2;
import com.mlab.pg.xyfunction.XYSampleImpl;
import com.mlab.pg.xyfunction.XYVectorFunction;

public abstract class AbstractVAlign implements VAlign{

	protected DesignSpeed designSpeed;
	protected double startX;
	protected double endX;
	protected Polynom2 polynom;

	// Constructor
	protected AbstractVAlign(DesignSpeed dspeed, Polynom2 polynom, double startx, double endx) {
		this.designSpeed = dspeed;
		this.polynom = polynom;
		this.startX = startx;
		this.endX = endx;
		
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

	public double getStartS() {
		return startX;
	}

	public double getEndS() {
		return endX;
	}

	public double getStartZ() {
		return polynom.getY(startX);
	}

	public double getEndZ() {
		return polynom.getY(endX);
	}

	public double getStartTangent() {
		return polynom.getTangent(startX);
	}

	public double getEndTangent() {
		return polynom.getTangent(endX);
	}

	/**
	 * Longitud medida a lo largo del eje X
	 */
	public double getLength() {
		return endX - startX;
	}

	/**
	 * La implementación corre a cargo de las clases derivadas. Las 
	 * VStraightAlign devolverán el valor de su pendiente. Las 
	 * vertical curves, VParaboleAlign, devolverán Double.NaN 
	 */
	public abstract double getSlope();
	
	/**
	 * La implementación corre a cargo de las clases derivadas. Las 
	 * VStraightAlign devolverán Double.NaN. Las 
	 * vertical curves, VParaboleAlign, devolverán el valor 
	 * de su parámetro Kv 
	 */
	public abstract double getKv();
	 
	
	@Override
	public XYVectorFunction getSample(double startx, double endx, double space) {
		List<double[]> list = new ArrayList<double[]>();
		if( (startx < this.startX) | (startx > this.endX) | 
				(startx == this.endX) ) {
			startx = this.startX;
		}
		if( (endx < startx) | (endx > this.endX) ) {
			endx = this.endX;
		}
		double pos = startx;
		while(pos <= endx) {
			list.add(new double[]{pos, this.polynom.getY(pos)});
			pos += space;
		}
		return new XYVectorFunction(list);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		return builder.toString();
	}
}
