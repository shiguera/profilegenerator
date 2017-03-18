package com.mlab.pg.reconstruction;

import com.mlab.pg.util.MathUtil;
import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.GradeProfileAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class EndingsWithBeginnersAdjuster_EqualArea implements EndingsWithBeginnersAdjuster{

	double thresholdSlope;
	XYVectorFunction originalGradePoints;
	VerticalGradeProfile gradeProfile;
	
	public EndingsWithBeginnersAdjuster_EqualArea(XYVectorFunction originalGradePoints, double thresholdSlope) {
		this.thresholdSlope = thresholdSlope;
		this.originalGradePoints = originalGradePoints;
		
	}

	/** 
	 * Ajusta los finales y principios de alineaciones
	 * para que pasen por la misma z. Para ello, cada 
	 * alineación excepto la primera la sustituye por una 
	 * recta con el primer punto el mismo, pero girada
	 * para que el area encerrada sea el mismo que el area
	 * encerrada bajo el perfil de pendientes original.
	 * La primera alineación solo la desplaza paralelamente para 
	 * conseguir el mismo area
	 */
	@Override
	public VerticalGradeProfile adjustEndingsWithBeginnings( VerticalGradeProfile gradeProfile) {
		this.gradeProfile = gradeProfile;
		
		adjustFirsAlignment();
		
		for(int i=1; i<gradeProfile.size(); i++) {
			GradeProfileAlignment current = gradeProfile.get(i);
			GradeProfileAlignment previous = gradeProfile.get(i-1);
			double s1 = previous.getStartS();
			double g1 = previous.getStartZ();
			double s2 = previous.getEndS();
			double g2 = previous.getEndZ();
			double s3 = current.getEndS();
			double g3 = current.getEndZ();
			double area = 0.5*(g1+g2)*(s2-s1) + 0.5*(g2+g3)*(s3-s2);
			double a2 = current.getPolynom2().getA2();
			double newg2, newg3;
			
			if(Math.abs(current.getSlope()) < thresholdSlope) {
				// Si la recta es horizontal la muevo paralelamente hasta el vertice anterior
				newg2 = (2*area - g1*(s2-s1))/(-s1-s2+2*s3);
				newg3 = newg2;
			} else if (Math.abs(previous.getSlope()) < thresholdSlope) {
				// Si la recta anterior es horizontal calculo la recta que pasa 
				// por el final de la anterior y tiene el mismo area
				int i1 = originalGradePoints.getNearestIndex(s2);
				int i2 = originalGradePoints.getNearestIndex(s3);
				double[] r = originalGradePoints.rectaPosteriorEqualArea(i1, i2);
				newg2 = r[0] + r[1]*s2;
				newg3 = r[0] + r[1]*s3;
			} else {
				// Traslado la recta paralelamente y muevo el vertice 
				// común con la recta anterior para que de el mismo area
				double[][] A = new double[][]{{s3-s1, s3-s2, 0}, {1, 0, -1}, {0, 1, -1}}; // Incognitas newg2, newg3, newa2
				double[] C = new double[]{2*area-g1*(s2-s1), a2*s2, a2*s3};
				double[] sol = MathUtil.solve(A, C);
				newg2 = sol[0];
				newg3 = sol[1];
				//double newa2 = sol[2];
			}
			Straight newr1 = new Straight(new double[]{s1,g1}, new double[]{s2, newg2});
			gradeProfile.set(i-1, new GradeProfileAlignment(newr1, s1,s2));
			Straight newr2 = new Straight(new double[]{s2, newg2}, new double[]{s3, newg3});
			gradeProfile.set(i, new GradeProfileAlignment(newr2, s2,s3));
		}
		return gradeProfile;
	}

	private void adjustFirsAlignment() {
		GradeProfileAlignment currentAlignment = gradeProfile.get(0);
		double starts = currentAlignment.getStartS();
		double ends = currentAlignment.getEndS();
		double area0 = originalGradePoints.areaEncerrada(starts, ends);
		double A1 = currentAlignment.getPolynom2().getA1();
		double newA0 = area0/(ends -starts) -  A1 * (starts + ends) / 2;
		Straight newr = new Straight(newA0, A1);
		gradeProfile.set(0, new GradeProfileAlignment(newr, starts,ends));
	}

}
