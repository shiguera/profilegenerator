package com.mlab.pg.valign;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.xyfunction.Polynom2;
import com.mlab.pg.xyfunction.XYFunction;
import com.mlab.pg.xyfunction.XYVectorFunction;

/**
 * Interface para los distintos tipos de alineaciones verticales
 * de carreteras
 * 
 * @author shiguera
 *
 */
public interface VAlign extends XYFunction{
	public static String CABECERA = String.format("%12s %12s %12s %12s %12s %12s %12s %12s %12s %12s", 
			"SE","ZE", "PE", "L", "SS", "ZS", "PS", "a0", "a1x", "a2x^2");
	
	DesignSpeed getDesignSpeed();
	Polynom2 getPolynom2();
	double getStartS();
	double getEndS();
	double getStartZ();
	double getEndZ();
	double getStartTangent();
	double getEndTangent();
	double getLength();
	/**
	 * Devuelve la pendiente de la alineación. Sólo tiene sentido en
	 * el caso de la rampas o pendientes. En el caso de los acuerdos parabólicos 
	 * devolverá Double.NaN
	 * @return
	 */
	double getSlope();
	/**
	 * Devuelve el valor del parámetro de la alineación vertical. 
	 * Solo tiene sentido en el caso de tratarse de una vertical curve.
	 * En el caso de tratarse de una rampa o pendiente, devolverá 
	 * Double.NaN. 
	 * @return
	 */
	double getKv();
	
	XYVectorFunction getSample(double startS, double endS, double space);

	@Override
	public String toString();
}
