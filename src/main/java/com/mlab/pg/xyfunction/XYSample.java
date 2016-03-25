package com.mlab.pg.xyfunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una colección de parejas de valores {xi,yi}
 * correspondientes a una muestra de cierta función y=f(x)
 * 
 * @author shiguera
 *
 */
public interface XYSample extends XYFunction, InInterval {


	/**
	 * Devuelve la coordenada x correspondiente al índice 'index' de la XYSample
	 * (El método getX() está implementado en el interface XYFunction del que deriva 
	 * XYSample)
	 * @param index
	 * @return
	 */
	double getX(int index);
	/**
	 * Devuelve la Y del punto en la posición 'index'
	 * @param index
	 * @return
	 */
	public double getY(int index);
	/**
	 * Reemplaza los valores (x,y) del elemento i del XYSample
	 * @param index Indice del elemento a sustituir
	 * @param value double[] con la x y la y del punto
	 */
	
	/**
	 * Devuelve la pareja de valores xi,yi del punto 'index' de la función
	 * @param index
	 * @return
	 */
	double[] getValue(int index);
	/**
	 * Establece los valores (x,y) del punto 'index' de la XYSample
	 * @param index
	 * @param value
	 */
	public void setValue(int index, double[] value);
	
	/**
	 * Devuelve una List con la colección de parejas de valores {xi,yi}
	 * @return
	 */
	List<double[]> getValues();
	
	/**
	 * Establece todos los valores (x,y) de la XYSample
	 * @param newvalues
	 */
	public void setValues(List<double[]> newvalues);
	
	/**
	 * Devuelve un Array de doubles con los valores Y de la XYSample
	 * @return
	 */
	double[] getYValues();

	/**
	 * Devuelve un Array de doubles con los valores y de la XYSample
	 * comprendidos en un intervalo de índices determinado
	 * @param segment
	 * @return
	 */
	double[] getYValues(IntegerInterval segment);

	/**
	 * Devuelve una Array de doubles con la colección de parejas de valores {xi,yi}
	 * comprendidos en el intervalo 'interval'
	 * @return
	 */
	double[][] getValuesAsArray(IntegerInterval interval);
	
	/**
	 * Devuelve las parejas de coordenadas (x,y) como ArrayList<double[]>.
	 * 
	 * @return Devuelve un ArrayList<double[]> con las parejas de coordenadas
	 * de los puntos.
	 * 
	 */
	public ArrayList<double[]> asArrayList();

	/**
	 * Devuelve una XYSample comprendida entre los puntos de abcisa 'start' y 'end'.
	 * @param start
	 * @param end
	 * @return
	 */
	XYSample getFunctionInInterval(double start, double end);

	/**
	 * Devuelve una XYSample comprendida entre los índices 'start' y 'end'.
	 * @param start
	 * @param end
	 * @return
	 */
	XYSample getFunctionInInterval(int start, int end);
	
	/**
	 * Devuelve true si el IntegerInterval argumento está contenido
	 * en el rango de la XYSample
	 * @param interval
	 * @return
	 */
	boolean containsInterval(IntegerInterval interval);
	
	/**
	 * Devuelve el número de parejas de valores de la XYSample
	 * @return
	 */
	int size();

	/**
	 * Vacía el XYSample
	 */
	void empty();
	
}
