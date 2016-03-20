package com.mlab.pg.xyfunction;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Función XY discreta almacenada en un List<double[]>
 * Para utilizarla:<br/>
 *    XYSampleImpl f = new XYSampleImpl();</br>
 *    f.setValues(listDoubles);<br/>
 * Se supone que los valores de las x están ordenados en orden creciente
 * 
 * @author shiguera
 *
 */
public class XYSampleImpl implements XYSample {
	private final Logger LOG = Logger.getLogger(XYSampleImpl.class);
	
	List<double[]> values;
	
	public XYSampleImpl() {
		this.values = new ArrayList<double[]>();
	}
	public XYSampleImpl(List<double[]> newvalues) {
		this();
		this.setValues(newvalues);
	}
	
	public void setValue(int index, double[] value) {
		values.set(index, value);
	}
	
	/**
	 * Número de puntos de la función
	 * @return
	 */
	public int size() {
		return values.size();
	}
	/**
	 * Devuelve la pareja {x,y} correspondiente a un índice
	 * 
	 * @param index Índice de la pareja {x,y} solicitada
	 * 
	 * @return double[] {x,y}
	 */
	public double[] getValue(int index) {
		return values.get(index);
	}
	public double getX(int index) {
		if(getValue(index)!=null) {
			return values.get(index)[0];
		}
		return Double.NaN;
	}
	@Override
	public double getStartX() {
		if(values.size()>0) {
			return values.get(0)[0];
		}
		return Double.NaN; 
	}
	@Override
	public double getEndX() {
		if(values.size()>0) {
			return values.get(values.size()-1)[0];
		}
		return Double.NaN; 
	}
	@Override
	public double getY(int index) {
		if(getValue(index)!=null) {
			return values.get(index)[1];
		}
		return Double.NaN;
	}
	@Override
	public double getY(double x) {
		if(values.size()>0) {
			if(contains(x)) {
				int previous = getPrevious(x);
				//LOG.debug("Previous: "+previous);
				if(previous == -1) {
					return Double.NaN;
				}
				if(previous == values.size()-1) {
					return getEndY();
				}
				double x0 = getX(previous);
				double y0 = getY(previous);
				//LOG.debug("X0, y0: "+x0+", "+y0);
				double x1 = getX(previous+1);
				double y1 = getY(previous+1);
				//LOG.debug("X1, y1: "+x1+", "+y1);
				double y = y0 + (y1-y0)/(x1-x0)*(x-x0);
				return y;
			}
		}
		return Double.NaN;
	}
	public double getStartY() {
		if(values.size()>0) {
			return values.get(0)[1];
		}
		return Double.NaN; 
	}
	public double getEndY() {
		if(values.size()>0) {
			return values.get(values.size()-1)[1];
		}
		return Double.NaN; 
	}
	public int getPrevious(double x) {
		if(values.size()>0) {
			if(contains(x)) {
				if(getStartX()==x) {
					return 0;
				}
				for(int i=1;i<values.size();i++) {
					if(getX(i)>x) {
						return i-1;
					}
				}
				return values.size()-1;
			} 
		}
		return -1;
	}
	/**
	 * Devuelve true si el valor x está comprendido
	 * en el intervalo de valores x, incluidos los extremos
	 * 
	 * @param x
	 * @return
	 */
	
	public boolean contains(double x) {
		if(values.size()>0) {
			return (x>=getStartX() && x<= getEndX());
		}
		return false;
	}
	@Override
	public boolean containsInterval(IntegerInterval interval) {
		if(interval.getStart()>=0 && interval.getStart()<size() && 
				interval.getEnd()>=0 && interval.getEnd()<size()) {
			return true;
		}
		return false;
	}
	/**
	 * Establece los valores {x, y} de la función
	 * 
	 * @param newvalues List<double[]> con los valores de la serie
	 */
	public void setValues(List<double[]> newvalues) {
		values = new ArrayList<double[]>();
		if(newvalues==null || newvalues.size()==0) {
			return;
		}
		values.addAll(newvalues);
	}
	/**
	 * Devuelve el List de la función. Las modificaciones en el list afectan a la función
	 * @return
	 */
	public List<double[]> getValues() {
		return values;
	}
	/** 
	 * Extrae una lista de valores de la función independiente del
	 * List de la función: las modificaciones en el list no afectan a la función
	 * 
	 * @param interval
	 * @return
	 */
	public List<double[]> getValues(IntegerInterval interval) {
		if(!this.containsInterval(interval)) {
			LOG.error("Error: sample doesn't contain interval "+ interval.toString());
			return null;
		}
		List<double[]> l = new ArrayList<double[]>();
		for(int i=interval.getStart(); i<=interval.getEnd(); i++) {
			l.add(values.get(i));
		}
		return l;
	}
	public List<double[]> getValues(int start, int end) {
		IntegerInterval interval = new IntegerInterval(start, end);
		return getValues(interval);
	}
	public double[][] getValuesAsArray(IntegerInterval interval) {
		if(!this.containsInterval(interval)) {
			return null;
		}
		int size = interval.getEnd() - interval.getStart() +1;
		double[][] d = new double[size][2];
		List<double[]> l = getValues(interval);
		for(int i=0; i<size;i++) {
			d[i] = l.get(i);
		}
		return d;
	}
	
	/**
	 * Devuelve las parejas de coordenadas (x,y) como ArrayList<double[]>.
	 * 
	 * @return Devuelve un ArrayList<double[]> con las parejas de coordenadas
	 * de los puntos.
	 * 
	 */
	@Override
	public ArrayList<double[]> asArrayList() {
		ArrayList<double[]> result = new ArrayList<double[]>();
		for(int i=0; i< this.size(); i++) {
			result.add(new double[] {this.getX(i), this.getY(i)});
		}
		return result;
	}
	
	/**
	 * Devuelve la tangente del punto anterior
	 * 
	 */
	public double getTangent(double x) {
		int previous = getPrevious(x);
		if(previous == -1) {
			return Double.NaN;
		}
		return getTangent(previous);
	}
	/**
	 * Calcula la curvatura del punto anterior
	 */
	public double getCurvature(double x) {
		int previous = getPrevious(x);
		if(previous == -1) {
			return Double.NaN;
		}
		return getCurvature(previous);		
	}
	/**
	 * Calcula la tangente con la expresión:<br>
	 * t = (Y(index+1)-Y(index)) / (X(index+1-X(index))
	 * Al último punto le asigna la misma pendiente que al penúltimo.
	 */
	public double getTangent(int index) {
		int last = size()-1;
		if(index <0 || index > last) {
			return Double.NaN;
		}
		if(index == last) {
			index--;
		}
		double incx = getX(index+1) - getX(index);
		if(incx == 0.0) {
			return Double.POSITIVE_INFINITY;
		}
		double incy = getY(index+1) - getY(index);		
		return incy / incx;
	}
	/**
	 * Calcula la curvatura en un punto de una poligonal 
	 * conocidos los dos siguientes puntos
	 * Utiliza la expresión:<p>
	 * chi = incTheta / incS = (theta(i+1)-theta(i))/sqrt((x(i+1)-x(i))^2+(y(i+1)-y(i))^2)<p>
	 * donde theta(i) = arctg((y(i+1)-y(i))/(x(i+1)-x(i))<p>
	 * A los dos últimos puntos les asigna el mismo valor de curvatura que al anteúltimo
	 */
	public double getCurvature(int index) {
		int last = size()-1;
		if(index < 0 || index > last) {
			return Double.NaN;
		}
		if(index == last) {
			index--;
		}
		if(index == last -1) {
			index--;
		}
		double thetai = getTangent(index);
		double thetaii = getTangent(index+1);
		double inctheta = thetaii - thetai;
		double incx = getX(index+1) - getX(index);
		if(incx == 0) {
			return Double.POSITIVE_INFINITY;
		}
		return inctheta / incx;
	}
	public void empty() {
		values = new ArrayList<double[]>();
	}
	/**
	 * Devuelve un XYSampleImpl con los valores correspondientes al intervalo solicitado.
	 * Los extremos inferior y superior del intervalo se ajustan al punto 'floor' 
	 * de 'start' y 'end' en la XYSample original.  
	 * @param start
	 * @param end
	 * @return
	 */
	public XYSample getFunctionInInterval(double start, double end) {
		int i1 = getPrevious(start);
		if(i1 == -1) {
			i1 = 0;
		}
		int i2 = getPrevious(end);
		if(i2 == -1) {
			i2 = size()-1;
		}
		return new XYSampleImpl(getValues(i1, i2));
	}
	/**
	 * Devuelve un XYSampleImpl con los valores correspondientes al intervalo solicitado.
	 * @param start
	 * @param end
	 * @return
	 */
	public XYSample getFunctionInInterval(int start, int end) {
		return new XYSampleImpl(getValues(start, end));
	}
	@Override
	public double[] getYValues() {
		double[] yvalues = new double[values.size()];
		for(int i=0; i<size();i++) {
			yvalues[i]=getY(i);
		}
		return yvalues;
	}
	@Override
	public double[] getYValues(IntegerInterval segment) {
		int resultsize = segment.getEnd() - segment.getStart() + 1;
		double[] yvalues = new double[resultsize];
		for(int i=0; i<resultsize;i++) {
			yvalues[i]=getY(segment.getStart()+i);
		}
		return yvalues;
	}


}
