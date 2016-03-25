package com.mlab.pg.xyfunction;

/**
 * Extiende XYVector en el sentido de que las parejas de doubles (x,y)
 * representan los valores (x,y) de una determinada función en un intervalo.
 * Se sobrescribe el método add() de forma que cada valor x debe ser mayor que elanterior.
 * 
 * @author shiguera
 *
 */
public class XYVectorFunction extends XYVector implements XYFunction, InInterval{

	private static final long serialVersionUID = 1L;


	@Override
	public boolean add(double[] e) {
		if(size()>0 && e[0] <= getX(size()-1)) {
			return false;
		} else {
			return super.add(e);			
		}
	}
	
	public boolean containsX(double x) {
		return (size()>0 && x >= getStartX() && x <= getEndX());
	}
	public int getPreviousIndex(double x) {
		if(size()>0) {
			if(containsX(x)) {
				if(getStartX()==x) {
					return 0;
				}
				for(int i=1;i<size();i++) {
					if(getX(i)>x) {
						return i-1;
					}
				}
				return size()-1;
			} 
		}
		return -1;
	}
	// Interface InInterval
	@Override
	public double getStartX() {
		if(size()>0) {
			return get(0)[0];
		} else {
			return Double.NaN;			
		}
	}

	@Override
	public double getEndX() {
		if(size()>0) {
			return get(size()-1)[0];
		} else {
			return Double.NaN;			
		}
	}

	// Interface XYFunction
	@Override
	public double getY(double x) {
		if(contains(x)) {
			
			return get(0)[1];
		} else {
			return Double.NaN;			
		}
	}

	@Override
	public double getTangent(double x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getCurvature(double x) {
		// TODO Auto-generated method stub
		return 0;
	}

}
