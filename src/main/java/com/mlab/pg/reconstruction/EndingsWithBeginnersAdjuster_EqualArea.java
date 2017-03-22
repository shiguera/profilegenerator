package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;

import com.mlab.pg.util.MathUtil;
import com.mlab.pg.valign.GradeProfileAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class EndingsWithBeginnersAdjuster_EqualArea implements EndingsWithBeginnersAdjuster{

	Logger LOG = Logger.getLogger(getClass());
	
	double thresholdSlope;
	XYVectorFunction originalGradePoints;
	VerticalGradeProfile gradeProfile;
	
	public EndingsWithBeginnersAdjuster_EqualArea(XYVectorFunction originalGradePoints, double thresholdSlope) {
		//LOG.debug("EndingsWithBeginnersAdjuster_EqualArea()");
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
	public VerticalGradeProfile adjustEndingsWithBeginnings( VerticalGradeProfile gradeprofile) {
		//LOG.debug("adjustEndingsWithBeginnings");
		VerticalGradeProfile process  = new VerticalGradeProfile();
		process.addAll(gradeprofile);
		boolean changes = true;
		while(changes) {
			changes = false;
			process = adjustFirsAlignment(process);
			
			for(int i=1; i<process.size(); i++) {
				if (checkTwoGrades(process)) {
					//LOG.warn("WARNING: TWO GRADES");		
					process = filterTwoGrades(process);
					changes = true;
					break;
				}

				GradeProfileAlignment current = process.get(i);
				GradeProfileAlignment previous = process.get(i-1);
				double g21 = previous.getEndZ();
				double g22 = current.getStartZ();
				if(g21 != g22) {
					double s1 = previous.getStartS();
					double g1 = previous.getStartZ();
					double s2 = previous.getEndS();
					double s3 = current.getEndS();
					double g3 = current.getEndZ();
					double area = 0.5*(g1+g21)*(s2-s1) + 0.5*(g22+g3)*(s3-s2);
					double newg2, newg3;
					
					double slope1 = Math.abs(previous.getSlope());
					double slope2 = Math.abs(current.getSlope());
					
					if (slope1<thresholdSlope && slope2<thresholdSlope) {
						// Las dos rectas son horizontales
						LOG.error("Error, dos rectas horizontales, no debería pasar ");
						newg2 = g3;
						newg3 = g3;
					} else if(slope2 < thresholdSlope) {
						// Si la recta es horizontal la muevo paralelamente hasta el vertice anterior
						//System.out.println("adjust: caso 1");
						newg2 = (2*area - g1*(s2-s1))/(-s1-s2+2*s3);
						newg3 = newg2;
					} else if (Math.abs(previous.getSlope()) < thresholdSlope) {
						// Si la recta anterior es horizontal calculo la recta que pasa 
						// por el final de la anterior y tiene el mismo area
						//System.out.println("adjust: caso 2");
						newg2 = g21;
						newg3 = g22-g21+g3;
					} else {
						// Traslado la recta paralelamente y muevo el vertice 
						// común con la recta anterior para que de el mismo area
						//System.out.println("adjust: caso 3");
						newg3 = (2*area -(g1+g22-g3)*(s2-s1) - (g22-g3)*(s3-s2)) / (-s1 -s2 + 2*s3);
						newg2 = g22 - g3 + newg3;
						//double newa2 = sol[2];
					}
					Straight newr1 = new Straight(new double[]{s1,g1}, new double[]{s2, newg2});
					process.set(i-1, new GradeProfileAlignment(newr1, s1,s2));
					Straight newr2 = new Straight(new double[]{s2, newg2}, new double[]{s3, newg3});
					process.set(i, new GradeProfileAlignment(newr2, s2,s3));	
				}	
			}
		}
		gradeProfile = new VerticalGradeProfile();
		gradeProfile.addAll(process);
		return gradeProfile;
	}

	private boolean checkTwoGrades(VerticalGradeProfile gprofile) {
		boolean result = false;
		for(int i=1; i<gprofile.size(); i++) {
			GradeProfileAlignment previous = gprofile.get(i-1);
			GradeProfileAlignment current = gprofile.get(i);
			double slope1 = Math.abs(previous.getSlope());
			double slope2 =Math.abs(current.getSlope());
			if (slope1 < thresholdSlope && slope2<thresholdSlope) {
				return true;
			}
		}
		return result;
	}
	private VerticalGradeProfile filterTwoGrades(VerticalGradeProfile profile) {
		//LOG.debug("filterTwoGrades()");
		VerticalGradeProfile process = new VerticalGradeProfile();
		process.addAll(profile);
		//System.out.println("Before filter: " + process.size());
		VerticalGradeProfile result = new VerticalGradeProfile();
		boolean changes = true;
		while(changes) {
			changes = false;
			result = new VerticalGradeProfile();
			result.add(process.get(0));
			for(int i=1; i<process.size(); i++) {
				GradeProfileAlignment current = process.get(i);
				GradeProfileAlignment previous = result.getLastAlign();
				double slope1 = previous.getSlope();
				double slope2 = current.getSlope();
				if(Math.abs(slope1)<thresholdSlope && Math.abs(slope2)<thresholdSlope) {
					// Si las dos rectas son horizontales las cambio por una sola de igual area
					double s1 = previous.getStartS();
					double g1 = previous.getStartZ();
					double s2 = previous.getEndS();
					double g21 = previous.getEndZ();
					double g22 = current.getStartZ();
					double s3 = current.getEndS();
					double g3 = current.getEndZ();
					double area = 0.5*(g1+g21)*(s2-s1) + 0.5*(g22+g3)*(s3-s2);
					double newg1 = area/(s3-s1);
					Straight straight = new Straight(new double[]{s1, newg1}, new double[]{s3,newg1});
					GradeProfileAlignment align = new GradeProfileAlignment(straight, s1, s3);
					result.remove(previous);
					result.add(align);
					changes = true;
				} else {
					result.add(current);
				}
			}
			process = new VerticalGradeProfile();
			process.addAll(result);
		}

		//System.out.println("After filter: " + result.size());
		return result;
	}
	private VerticalGradeProfile adjustFirsAlignment(VerticalGradeProfile profile) {
		GradeProfileAlignment currentAlignment = profile.get(0);
		double starts = currentAlignment.getStartS();
		double ends = currentAlignment.getEndS();
		double area0 = originalGradePoints.areaEncerrada(starts, ends);
		double A1 = currentAlignment.getPolynom2().getA1();
		double newA0 = area0/(ends -starts) -  A1 * (starts + ends) / 2;
		Straight newr = new Straight(newA0, A1);
		profile.set(0, new GradeProfileAlignment(newr, starts,ends));
		return profile;
	}

}
