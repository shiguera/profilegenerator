package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;

import com.mlab.pg.valign.GradeProfileAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class GradeProfileCreator_EqualArea implements GradeProfileCreator {

	Logger LOG = Logger.getLogger(GradeProfileCreator_EqualArea.class);
	
	XYVectorFunction originalGradePoints;
	TypeIntervalArray typeIntervalArray;
	VerticalGradeProfile gradeProfile;
	double thresholdSlope ;
	public GradeProfileCreator_EqualArea(double thresholdSlope) {
		this.thresholdSlope = thresholdSlope;
	}

	// Solución con recta paralela a la de mínimos cuadrados
	@Override
	public VerticalGradeProfile createGradeProfile(XYVectorFunction originalGradePoints, TypeIntervalArray typeIntervalArray) {
		this.originalGradePoints = originalGradePoints;
		this.typeIntervalArray = typeIntervalArray;
		
		gradeProfile = new VerticalGradeProfile();
		
		for(int i=0; i<typeIntervalArray.size(); i++) {
			TypeInterval currentInterval = typeIntervalArray.get(i);
			int first = currentInterval.getStart();
			double s1 = originalGradePoints.getX(first);
			int last = currentInterval.getEnd();
			double s2 = originalGradePoints.getX(last);
			double area = originalGradePoints.areaEncerrada(first, last);
			double[] r = null;
			Straight straight = null;
			if(currentInterval.getPointType() == PointType.GRADE) {
				r = originalGradePoints.rectaHorizontalEqualArea(first, last);
				straight = new Straight(r[0], 0.0);
			} else {
				r = originalGradePoints.rectaMinimosCuadrados(first, last);
				double newa0 = area /(s2-s1) - 0.5*r[1]*(s1+s2);
				straight = new Straight(newa0, r[1]);
			}
			GradeProfileAlignment align = new GradeProfileAlignment(straight, s1, s2);
			//System.out.println(String.format("%f %f", align.getStartZ(), align.getEndZ()));
			gradeProfile.add(align);
		}
		gradeProfile = filterGrades(gradeProfile);
		gradeProfile = filterTwoGrades(gradeProfile);
		return gradeProfile;
	}

	private VerticalGradeProfile filterGrades(VerticalGradeProfile gprofile) {
		LOG.debug("filterGrades()");
		//System.out.println("Before: " + gprofile.size());
		VerticalGradeProfile result = new VerticalGradeProfile();
		int counter = 0;
		for(int i=0; i<gprofile.size(); i++) {
			GradeProfileAlignment current = gprofile.get(i);
			if(Math.abs(current.getSlope())<thresholdSlope) {
				//System.out.println("Filtering..." + i);
				counter++;
				double s1 = current.getStartS();
				int i1 = originalGradePoints.getNearestIndex(s1);
				double s2 = current.getEndS();
				int i2 = originalGradePoints.getNearestIndex(s2);
				double[] r = originalGradePoints.rectaHorizontalEqualArea(i1, i2);
				Straight straight = new Straight(r[0], 0.0);
				GradeProfileAlignment align = new GradeProfileAlignment(straight, s1, s2);
				result.add(align);
			} else {
				result.add(current);
			}
		}
		//System.out.println("Filtered: " + counter);
		//System.out.println("After filter: " + result.size());
		return result;
	}

	// Comprueba si hay dos alineaciones seguidas con pendiente menor que thresholdSlope, 
	// y si es así las une en una única y horizontal
	private VerticalGradeProfile filterTwoGrades(VerticalGradeProfile gprofile) {
		LOG.debug("filterTwoGrades()");
		//System.out.println("Before: " + gprofile.size());
		VerticalGradeProfile result = new VerticalGradeProfile();
		VerticalGradeProfile processGP = new VerticalGradeProfile();
		processGP.addAll(gprofile);
		boolean changes = true;
		int counter = 0;
		int filteredCounter = 0;
		while(changes) {
			counter++;
			//System.out.println("Ronda filtro: " + counter);
			changes = false;
			result = new VerticalGradeProfile();
			result.add(processGP.get(0));
			for(int i=1; i<processGP.size(); i++) {
				GradeProfileAlignment current = processGP.get(i);
				GradeProfileAlignment previous = result.getLastAlign();
				if(Math.abs(current.getSlope())<thresholdSlope && Math.abs(previous.getSlope()) < thresholdSlope) {
					//System.out.println("Filtering...");
					filteredCounter++;
					double s1 = previous.getStartS();
					int i1 = originalGradePoints.getNearestIndex(s1);
					double s2 = current.getEndS();
					int i2 = originalGradePoints.getNearestIndex(s2);
					double[] r = originalGradePoints.rectaHorizontalEqualArea(i1, i2);
					Straight straight = new Straight(r[0], 0.0);
					GradeProfileAlignment align = new GradeProfileAlignment(straight, s1, s2);
					result.remove(previous);
					result.add(align);
					changes = true;
				} else {
					result.add(current);
				}
			}
			processGP = new VerticalGradeProfile();
			processGP.addAll(result);
		}
		//System.out.println("Filtered: " + filteredCounter);
		//System.out.println("After: " + result.size());
		return result;
	}

	// Solución con recta pasando por el primer punto y que ocupe el mismo area
//	@Override
//	public VerticalGradeProfile createGradeProfile(XYVectorFunction originalGradePoints, TypeIntervalArray typeIntervalArray) {
//		VerticalGradeProfile gradeProfile = new VerticalGradeProfile();
//		for(int i=0; i<typeIntervalArray.size(); i++) {
//			int first = typeIntervalArray.get(i).getStart();
//			int last = typeIntervalArray.get(i).getEnd();
//			double[] r = originalGradePoints.rectaPosteriorEqualArea(first, last);
//			Straight straight = new Straight(r[0], r[1]);
//			GradeProfileAlignment align = new GradeProfileAlignment(straight, originalGradePoints.getX(first), originalGradePoints.getX(last));
//			//System.out.println(String.format("%f %f", align.getStartZ(), align.getEndZ()));
//			gradeProfile.add(align);
//		}
//		
//		return gradeProfile;
//	}

	// Solución con recta de pendiente media de la de m.c, la anterior y la posterior y que ocupe el mismo area
//	@Override
//	public VerticalGradeProfile createGradeProfile(XYVectorFunction originalGradePoints, TypeIntervalArray typeIntervalArray) {
//		VerticalGradeProfile gradeProfile = new VerticalGradeProfile();
//		for(int i=0; i<typeIntervalArray.size(); i++) {
//			int first = typeIntervalArray.get(i).getStart();
//			double s1 = originalGradePoints.getX(first);
//			int last = typeIntervalArray.get(i).getEnd();
//			double s2 = originalGradePoints.getX(last);
//			double area = originalGradePoints.areaEncerrada(first, last);
//			double[] r1 = originalGradePoints.rectaAnteriorEqualArea(first, last);
//			double[] r2 = originalGradePoints.rectaPosteriorEqualArea(first, last);
//			double[] r3 = originalGradePoints.rectaMinimosCuadrados(first, last);
//			double pdte = (r1[1]+r2[1]+r3[1])/3.0;
//			double newa0 = area /(s2-s1) - 0.5*pdte*(s1+s2);			
//			Straight straight = new Straight(newa0, pdte);
//			GradeProfileAlignment align = new GradeProfileAlignment(straight, originalGradePoints.getX(first), originalGradePoints.getX(last));
//			//System.out.println(String.format("%f %f", align.getStartZ(), align.getEndZ()));
//			gradeProfile.add(align);
//		}
//		
//		return gradeProfile;
//	}

}
