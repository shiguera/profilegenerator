package com.mlab.pg.reconstruction;

import com.mlab.pg.util.MathUtil;
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
			// Calcular el area bajo los puntos originales			
			double starts = gradeProfile.get(i-1).getEndS();
			double starty = gradeProfile.get(i-1).getEndZ();
			double ends = gradeProfile.get(i).getEndS();
			double area = originalGradePoints.areaEncerrada(starts, ends);
			double newendy = 2*area/(ends-starts) - starty;
			double[] newr = MathUtil.rectaPorDosPuntos(new double[]{starts,  starty}, new double[]{ends, newendy});
			Straight straight = new Straight(newr[0], newr[1]);
			if(Math.abs(straight.getA1()) < thresholdSlope) {
				straight.setA1(0.0);
			}
			GradeProfileAlignment align = new GradeProfileAlignment(straight, starts, ends);
			gradeProfile.set(i, align);
		}
		return null;
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
