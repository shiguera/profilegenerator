package com.mlab.pg.reconstruction;

import java.util.Collections;

import org.apache.log4j.Logger;

import com.mlab.pg.util.MathUtil;
import com.mlab.pg.valign.GradeProfileAlignment;
import com.mlab.pg.valign.VAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class GradeProfileFilter {

	
	Logger LOG = Logger.getLogger(getClass());

	double MIN_ALIGNMENT_LENGTH = 50.0;
	
	VerticalGradeProfile originalGradeProfile;
	XYVectorFunction originalGradePoints;
	VerticalGradeProfile resultGradeProfile;
	double startZ;
	double thresholdSlope;
	
	private XYVectorFunction verticalProfilePoints;
	private double meanSeparation;
	
	public GradeProfileFilter(VerticalGradeProfile gradeprofile, XYVectorFunction originalgradePoints, double startz, double thresholdSlope) {
		this.originalGradeProfile = gradeprofile;
		this.originalGradePoints = originalgradePoints;
		this.startZ = startz;
		this.thresholdSlope = thresholdSlope;
		this.meanSeparation = originalGradePoints.separacionMedia();
		
		
		verticalProfilePoints = originalGradePoints.integrate(startZ);
		
		filter();
	}
	private void adjustFirsAlignment() {
		GradeProfileAlignment currentAlignment = resultGradeProfile.get(0);
		double starts = currentAlignment.getStartS();
		double ends = currentAlignment.getEndS();
		double area0 = originalGradePoints.areaEncerrada(starts, ends);
		double A1 = currentAlignment.getPolynom2().getA1();
		double newA0 = area0/(ends -starts) -  A1 * (starts + ends) / 2;
		Straight newr = new Straight(newA0, A1);
		resultGradeProfile.set(0, new GradeProfileAlignment(newr, starts,ends));
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
	private void adjustEndingsWithBeginnings2() {
		
		adjustFirsAlignment();
		
		for(int i=1; i<resultGradeProfile.size(); i++) {
			// Calcular el area bajo los puntos originales			
			double starts = resultGradeProfile.get(i-1).getEndS();
			double starty = resultGradeProfile.get(i-1).getEndZ();
			double ends = resultGradeProfile.get(i).getEndS();
			double area = originalGradePoints.areaEncerrada(starts, ends);
			double newendy = 2*area/(ends-starts) - starty;
			double[] newr = MathUtil.rectaPorDosPuntos(new double[]{starts,  starty}, new double[]{ends, newendy});
			Straight straight = new Straight(newr[0], newr[1]);
			GradeProfileAlignment align = new GradeProfileAlignment(straight, starts, ends);
			resultGradeProfile.set(i, align);
		}
	
	}
	
	private void filter() {
		if (originalGradeProfile.size()<2) {
			resultGradeProfile = new VerticalGradeProfile();
			resultGradeProfile.addAll(originalGradeProfile);
			return;
		}
		VerticalGradeProfile processGradeProfile = new VerticalGradeProfile(); 
		processGradeProfile.addAll(originalGradeProfile);
		boolean changes = true;
		while(changes) {
			changes = false;
			resultGradeProfile = new VerticalGradeProfile();
			resultGradeProfile.add(processGradeProfile.get(0));
			for(int i=1; i<processGradeProfile.size(); i++) {
				GradeProfileAlignment currentAlignment = processGradeProfile.get(i);
				GradeProfileAlignment previousAlignment = resultGradeProfile.getLastAlign();
				
				if(previousAlignment.getLength() < MIN_ALIGNMENT_LENGTH) {
					double originalEcm = calculateOriginalEcm(previousAlignment, currentAlignment);
					GradeProfileAlignment joined = joinAlignments(previousAlignment, currentAlignment);
					VAlignment vpJoined = joined.integrate(verticalProfilePoints.getY(joined.getStartS()), thresholdSlope);
					XYVectorFunction joinedPoints = vpJoined.getSample(joined.getStartS(), joined.getEndS(), meanSeparation);
	
					XYVectorFunction newpoints = verticalProfilePoints.extract(joinedPoints.getStartX(), joinedPoints.getEndX());
					System.out.println("newPointsSize: " + newpoints.size());
					double newEcm = newpoints.ecm(joinedPoints);
					System.out.println("OriginalEcm = " + originalEcm + ", NewEcm= " + newEcm);
					if(newEcm < originalEcm) {
						resultGradeProfile.remove(resultGradeProfile.size()-1);
						resultGradeProfile.add(joined);
						changes = true;
					} else {
						resultGradeProfile.add(currentAlignment);					
					}
				} else {
					resultGradeProfile.add(currentAlignment);
				}
			}
			adjustEndingsWithBeginnings2();
			processGradeProfile = new VerticalGradeProfile();
			processGradeProfile.addAll(resultGradeProfile);
		}
	}
	private double calculateOriginalEcm(GradeProfileAlignment alignment1, GradeProfileAlignment alignment2) {
		VerticalGradeProfile gprofile = new VerticalGradeProfile();
		gprofile.add(alignment1);
		gprofile.add(alignment2);
		VerticalProfile vp = gprofile.integrate(verticalProfilePoints.getY(gprofile.getStartS()), thresholdSlope);
		
		XYVectorFunction points = vp.getSample(gprofile.getStartS(), gprofile.getEndS(), meanSeparation, true);
		XYVectorFunction points2 = verticalProfilePoints.extract(gprofile.getStartS(), gprofile.getEndS());
		return points2.ecm(points);
		
	}
	private GradeProfileAlignment joinAlignments(GradeProfileAlignment alignment1, GradeProfileAlignment alignment2) {
		
		XYVectorFunction points = new XYVectorFunction();
		XYVectorFunction sample1 =alignment1.getSample(alignment1.getStartS(), alignment1.getEndS(), meanSeparation);
		for(int i=0; i<sample1.size(); i++) {
			points.add(new double[]{sample1.getX(i), sample1.getY(i)});
		}
		XYVectorFunction sample2 =alignment2.getSample(alignment2.getStartS() + meanSeparation, alignment2.getEndS(), meanSeparation);
		for(int i=0; i<sample2.size(); i++) {
			points.add(new double[]{sample2.getX(i), sample2.getY(i)});
		}
		double[] r = points.rectaPosteriorEqualArea(0, points.size()-1);
		Straight rr = new Straight(r[0], r[1]);
		GradeProfileAlignment joined = new GradeProfileAlignment(rr, points.getStartX(), points.getEndX()); 
		return joined;
	}

	public VerticalGradeProfile getResultGradeProfile() {
		return resultGradeProfile;
	}

}
