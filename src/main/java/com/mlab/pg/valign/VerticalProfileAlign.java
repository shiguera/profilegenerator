package com.mlab.pg.valign;

public interface VerticalProfileAlign extends VAlign {

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

	GradeProfileAlign derivative();
}
