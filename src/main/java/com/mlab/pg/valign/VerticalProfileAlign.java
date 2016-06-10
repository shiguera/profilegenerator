package com.mlab.pg.valign;

/**
 * Interface para las alineaciones de un VerticalProfile
 * 
 * @author shiguera
 *
 */
public interface VerticalProfileAlign extends VAlign {

	double getStartZ();
	double getEndZ();
	double getStartTangent();
	double getEndTangent();
	/**
	 * Devuelve la pendiente de la alineación. Sólo tiene sentido en
	 * el caso de la rampas o pendientes. En el caso de los acuerdos parabólicos 
	 * devolverá Double.NaN
	 * @return pendiente de la alineación
	 */
	double getSlope();
	/**
	 * Devuelve el valor del parámetro de la alineación vertical. 
	 * Solo tiene sentido en el caso de tratarse de una vertical curve.
	 * En el caso de tratarse de una rampa o pendiente, devolverá 
	 * Double.NaN. 
	 * @return Valor de Kv si es una VerticalCurveAlign, NaN si es una GradeAlign
	 */
	double getKv();

	GradeProfileAlign derivative();
}
