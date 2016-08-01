package com.mlab.pg.valign;

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
public interface VAlignment extends XYFunction {
	
	
	//DesignSpeed getDesignSpeed();
	Polynom2 getPolynom2();
	void setPolynom(Polynom2 polynom);
	
	double getStartS();
	void setStartS(double starts);
	double getEndS();
	void setEndS(double ends);

	double getStartZ();
	double getEndZ();
			
	double getLength();
	
	double getStartTangent();
	double getEndTangent();
	

	GradeProfileAlignment derivative();
	
	XYVectorFunction getSample(double startS, double endS, double space);

	
	public static String CABECERA = String.format("%12s %12s %12s %12s %12s %12s %12s %12s %12s %12s %10s", 
			"SE","ZE", "PE", "L", "SS", "ZS", "PS", "a0", "a1x", "a2x^2", "K");
	
	@Override
	public String toString();
}
