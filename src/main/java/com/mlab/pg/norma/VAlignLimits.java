package com.mlab.pg.norma;

/**
 * Límites básicos para las alineaciones verticales. </br>
 * Cada tipo de alineación vertical y en función de su
 * velocidad de diseño, tendrá valores mínimos y máximos
 * para la longitud de la alineación y para la pendiente.
 * 
 * @author shiguera
 *
 */
public interface VAlignLimits {

	
	DesignSpeed getDesignSpeed();
	double getMaxLength();
	double getMinLength();
	double getMaxSlope();
	double getMinSlope();
	double getSlopeIncrements();
	void setSlopeIncrements(double slopeIncrements);
	double getKvIncrements();
	void setKvIncrements(double kvIncrements);

}
