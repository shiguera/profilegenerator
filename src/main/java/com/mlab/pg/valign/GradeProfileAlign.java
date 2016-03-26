package com.mlab.pg.valign;

import java.util.ArrayList;
import java.util.List;

import com.mlab.pg.norma.DesignSpeed;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

/**
 * Una alineación de un perfil de pendientes.
 * Siempre son rectas, horizontales o inclinadas según 
 * correspondan a un tramo Grade o a un tramo VerticalCurve
 * 
 * @author shiguera
 *
 */

public class GradeProfileAlign implements VAlign {

	DesignSpeed designSpeed;
	Straight straight;
	double startS;
	double startGrade;
	double endS;
	double endGrade;

	public double getStartGrade() {
		return startGrade;
	}
	public double getEndGrade() {
		return endGrade;
	}

	// Interface XYFunction
	@Override
	public double getY(double x) {
		return straight.getY(x);
	}

	@Override
	public double getTangent(double x) {
		return straight.getTangent(x);
	}

	@Override
	public double getCurvature(double x) {
		return 0;
	}

	// Interface VAlign
	@Override
	public double getStartS() {
		return startS;
	}

	@Override
	public double getEndS() {
		return endS;
	}

	@Override
	public DesignSpeed getDesignSpeed() {
		return designSpeed;
	}

	@Override
	public Straight getPolynom2() {
		return straight;
	}

	@Override
	public XYVectorFunction getSample(double starts, double ends, double space) {
		List<double[]> list = new ArrayList<double[]>();
		if( (starts < this.startS) | (starts > this.endS) | 
				(starts == this.endS) ) {
			starts = this.startS;
		}
		if( (ends < starts) | (ends > this.endS) ) {
			ends = this.endS;
		}
		double pos = starts;
		while(pos <= ends) {
			list.add(new double[]{pos, this.straight.getY(pos)});
			pos += space;
		}
		if (pos<endS) {
			list.add(new double[]{endS, getY(endS)});
		}
		return new XYVectorFunction(list);
	}

	
}
