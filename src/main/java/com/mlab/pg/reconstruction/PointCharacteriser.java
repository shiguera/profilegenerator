package com.mlab.pg.reconstruction;

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

	public PointCharacteriser() {
		
	}
	
	public PointType characterise(XYVectorFunction gpsample, int index, int mobileBaseSize, double thresholdSlope, PointCharacteriserStrategy strategy) {
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
