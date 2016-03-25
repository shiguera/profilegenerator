package com.mlab.pg.xyfunction;

import java.util.ArrayList;

/**
 * ArrayList de parejas de valores (x,y)
 * @author shiguera
 *
 */
public class XYVector extends ArrayList<double[]>  {

	private static final long serialVersionUID = 1L;

	@Override
	public double[] get(int index) {
		if(contains(index)) {
			return super.get(index);
		} else {
			return null;
		}
	}
	@Override
	public boolean add(double[] e) {
		if(e.length >= 2) {
			return super.add(new double[]{e[0],e[1]});
		} else {
			return false;
		}
	}
	
	public double getX(int i) {
		if (contains(i)) {
			return this.get(i)[0];
		} else {
			return Double.NaN;
		}
	}

	public double getY(int i) {
		if (contains(i)) {
			return this.get(i)[1];
		} else {
			return Double.NaN;
		}
	}
	
	public double[] getXY(int i) {
		if(contains(i)) {
			return get(i);
		} else {
			return null;
		}
	}
	
	public double[] getXValues() {
		if(size()==0) {
			return null;
		}
		double[] xvalues = new double[this.size()];
		for(int i=0; i<size(); i++) {
			xvalues[i] = this.get(i)[0];
		}
		return xvalues;
	}
	public double[] getYValues() {
		if(size()==0) {
			return null;
		}
		double[] yvalues = new double[this.size()];
		for(int i=0; i<size(); i++) {
			yvalues[i] = this.get(i)[1];
		}
		return yvalues;
	}

	/**
	 * Devuelve true si el índice i es uno de los índices
	 * válidos del ArrayList
	 * @param i
	 * @return
	 */
	public boolean contains(int i) {
		return (i>=0 && i<size());
	}
}
