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
	public int previousIndex(double x) {
		if(containsX(x)) {
			if(getStartX()==x) {
				return -1;
			}
			for(int i=1;i<size();i++) {
				if(getX(i)>=x) {
					return i-1;
				}
			}
			if(getEndX()==x && size()>1) {
				return size()-2;
			}
		} 
		return -1;
	}
	public int followingIndex(double x) {
		if(containsX(x)) {
			if(getStartX()==x && size()>1) {
				return 1;
			}
			for(int i=0;i<size()-1;i++) {
				if(getX(i)==x) {
					return i+1;
				}
				if(getX(i)>x) {
					return i;
				}
			}
			if(getEndX()>x) {
				return size()-1;
			}
			if(getEndX()==x) {
				return -1;
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
	/**
	 * Devuelve el valor de Y para un valor de x comprendido
	 * en el intervalo. Si la x coincide con una de las de la 
	 * serie devuelve el valor exacto correspondiente. Si la x
	 * es intermedia entre dos de la serie devuelve la 
	 * interpolación lineal de las Y del punto anterior y
	 * el siguiente
	 */
	@Override
	public double getY(double x) {
		if(containsX(x)) {
			if(getStartX()==x) {
				return get(0)[1];
			}
			if(getEndX()==x) {
				return get(size()-1)[1];
			}
			int previous = previousIndex(x);
			int following =followingIndex(x);
			if(following-previous==1) {
				// Punto intermedio: Aproximación lineal
				return (get(previous)[1]+get(following)[1])/2.0;
			}
			// Punto exacto
			return get(previous+1)[1];
		} else {
			return Double.NaN;			
		}
	}

	/**
	 * Calcula la tangente según la fórmula:<br/>
	 * tangent = inc(Y)/inc(X) <br/>
	 * Si el punto coincide con uno de la serie, excepto el último,
	 *  el valor es exacto. El último punto no tiene tangente.
	 *  Si el punto es intermedio, devuelve la interpolación 
	 *  lineal de la tangente del punto anterior y el siguiente
	 */
	@Override
	public double getTangent(double x) {
		if(containsX(x)) {
			int following = followingIndex(x);
			if(following ==-1) {
				return Double.NaN;
			}
			double x1 = x;
			double y1 = getY(x);
			double x2 = getX(following);
			double y2 = getY(following);
			double incx = x2 - x1;
			double incy = y2 - y1;
			return incy/incx;
		}
		return Double.NaN;
	}

	/**
	 * TODO: No implementado
	 */
	@Override
	public double getCurvature(double x) {
		// TODO Auto-generated method stub
		return Double.NaN;
	}

}
