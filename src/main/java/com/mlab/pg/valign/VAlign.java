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
	void setStartS(double starts);
	double getEndS();
	void setEndS(double ends);
	double getLength();

	XYVectorFunction getSample(double startS, double endS, double space);

	@Override
	public String toString();
}
