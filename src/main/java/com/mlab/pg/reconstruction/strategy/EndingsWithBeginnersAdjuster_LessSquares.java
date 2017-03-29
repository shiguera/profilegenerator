package com.mlab.pg.reconstruction.strategy;

import com.mlab.pg.valign.GradeProfileAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.xyfunction.Straight;

public class EndingsWithBeginnersAdjuster_LessSquares implements EndingsWithBeginnersAdjuster{

	VerticalGradeProfile gradeProfile;
	
	public EndingsWithBeginnersAdjuster_LessSquares() {
		
	}

	/** 
	 * Ajusta los finales y principios de alineaciones
	 * para que pasen por la misma z. Para ello, cada 
	 * alineación excepto la primera la sustituye por una 
	 * recta paralela que pase por la z final de la 
	 * alineación anterior
	 */
	
@Override
	public VerticalGradeProfile adjustEndingsWithBeginnings( VerticalGradeProfile gradeprofile) {
		this.gradeProfile = gradeprofile;
		
		for(int i=1; i<gradeProfile.size(); i++) {
			double lastx = gradeProfile.get(i-1).getEndS();
			double lastg = gradeProfile.get(i-1).getEndZ();
			double r1 = gradeProfile.get(i).getPolynom2().getA1();
			double newr0 = lastg - r1*lastx;
			Straight straight = new Straight(newr0, r1);
			GradeProfileAlignment align = new GradeProfileAlignment(straight, lastx, gradeProfile.get(i).getEndS());
			gradeProfile.set(i, align);
			//System.out.println(String.format("%f %f", align.getStartZ(), align.getEndZ()));
		}
		return gradeProfile;
	}


}
