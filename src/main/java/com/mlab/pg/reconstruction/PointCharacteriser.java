package com.mlab.pg.reconstruction;

import com.mlab.pg.reconstruction.strategy.PointCharacteriserStrategy;
import com.mlab.pg.xyfunction.XYVectorFunction;

/**
 * Caracteriza un punto de un perfil de pendientes según uno de los tipos básicos
 * definidos en PointType: Grade, VerticalCurve, BorderPoint, para unos valores
 * determinados del mobileBaseSize y thresholdSlope 
 * 
 * @author shiguera
 *
 */
public class PointCharacteriser {

	PointCharacteriserStrategy strategy;
	public PointCharacteriser(PointCharacteriserStrategy strategy) {
		this.strategy = strategy;
	}
	
	/**
	 * Caracteriza un punto de una muestra de pendientes para unos valores únicos de baseSize y thresholdSlope, en función de la
	 * pendiente de las rectas anterior y posterior, que se calculan de diferente manera según 
	 * la estrategia de interpolación utilizada
	 * 
	 * @param gpsample puntos del perfil de penientes
	 * @param index índice del punto a caracterizar dentro de gpsample
	 * @param mobileBaseSize tamaño de las rectas de interpolación
	 * @param thresholdSlope pendiente límite
	 * 
	 * @return PointType con la caracterización del elemento
	 */
	public PointType characterise(XYVectorFunction gpsample, int index, int mobileBaseSize, double thresholdSlope) {
		double[] r1 = strategy.calculaRectaAnterior(gpsample, index, mobileBaseSize);
		double[] r2 = strategy.calculaRectaPosterior(gpsample, index, mobileBaseSize);
		if (r1 == null || r2 == null || r1.length <2 || r2.length <2) {
			return PointType.NULL;
		}
		
		if(isHorizontal(r1, thresholdSlope)) {
			if(isHorizontal(r2, thresholdSlope)) {
				return PointType.GRADE;
			} else {
				return PointType.BORDER_POINT;
			}
		} else {
			if(isHorizontal(r2, thresholdSlope)) {
				return PointType.BORDER_POINT;
			} else {
				if(isEqualSlope(r1, r2, thresholdSlope)) {
					return PointType.VERTICAL_CURVE;
				} else {
					return PointType.BORDER_POINT;
				}
			}
		}
	}
	/**
	 * Caracteriza un punto de una muestra de pendientes probando entre varios valores 
	 * de baseSize y de thresholdSlopes y eligiendo la combinación cuyas rectas de 
	 * interpoación den lugar a un menor ecm con los puntos de la muestra
	 * 
	 * @param gradepoints muestra de pendientes
	 * @param index índice del punto a caracterizar dentro de la muestra
	 * @param basesizes colección de tamaños de baseSize
	 * @param slopes colección de pendientes límite
	 * 
	 * @return PointCharacterization con el tipo de punto asignado,el valor de baseSize 
	 * y de thresholdSlope elegidos 
	 */
	public PointCharacterization characterise(XYVectorFunction gradepoints, int index, int[] basesizes, double[] slopes) {
		double sep = gradepoints.separacionMedia();
		int numpruebas = basesizes.length * slopes.length;
		double[] ecms = new double[numpruebas];
		for(int i=0; i<basesizes.length; i++) {
			for(int j=0; j<slopes.length; j++) {
				int basesize = basesizes[i];
				int first = index - basesize + 1;
				int last = i + basesize -1;
				// Compruebo que los extremos de las rectas están en la muestra
				// Si no están asigno Double.NaN al ecm
				if (first<0 || last>gradepoints.size()) {
					ecms[i*basesizes.length + j] = Double.NaN;
					continue;
				}
				// Preparo la sample original para calcular el ecm
				XYVectorFunction originalsample = gradepoints.subList(first, last);
				// Preparo la recsample para los puntos de las rectas
				XYVectorFunction recsample = new XYVectorFunction();
				// Calculo la recta anterior
				double[] r1 =strategy.calculaRectaAnterior(gradepoints, index, basesize);
				// Añado los puntos de la recta anterior a la recsample
				for(int k=first; k<i; k++) {
					double xx = gradepoints.getX(k);
					double yy = gradepoints.getY(k);
					recsample.add(new double[]{xx, yy});
				}
				// Calculo la recta posterior
				double[] r2 =strategy.calculaRectaAnterior(gradepoints, index, basesize);
				// Añado los puntos de la recta posterior a la recsample
				for(int k=i; k<=last; k++) {
					double xx = gradepoints.getX(k);
					double yy = gradepoints.getY(k);
					recsample.add(new double[]{xx, yy});
				}
				// Calculo y guardo el ecm
				double ecm = originalsample.ecm(recsample);
				ecms[i*basesizes.length + j] = ecm;				
			}
		}
		// Calculo el mínimo ecm
		double min = ecms[0];
		int minindex = 0;
		for(int i=1; i<ecms.length; i++) {
			if(ecms[i]<min) {
				min = ecms[i];
				minindex = i;
			}
		}
		int basesize = basesizes[minindex / basesizes.length];
		double thresholdslope = slopes[minindex % slopes.length];
		PointType type = characterise(gradepoints, index, basesize, thresholdslope);
		PointCharacterization ch = new PointCharacterization(type, basesize, thresholdslope);
		return ch;
	}
	private boolean isHorizontal(double[] r, double threshold) {
		if(Math.abs(r[1])<threshold) {
			return true;
		} else {
			return false;
		}
	}
	private boolean isEqualSlope(double[] r1, double[] r2, double threshold) {
		double slope1 = r1[1];
		double slope2 = r2[1];
		if(Math.abs(slope1 - slope2)< threshold) {
			return true;
		} else {
			return false;
		}
	}
	
	
}
