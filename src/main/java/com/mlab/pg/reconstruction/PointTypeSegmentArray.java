package com.mlab.pg.reconstruction;

import java.util.ArrayList;

/**
 * Es un ArrayList de PointTypeSegment. Se genera en el constructor, al pasarle un
 * PointTypeArray, que es un ArrayList de tipos de puntos
 * 
 * @author shiguera
 *
 */
public class PointTypeSegmentArray extends ArrayList<PointTypeSegment> {
	
	private static final long serialVersionUID = 1L;

	public PointTypeSegmentArray() {
	
	}
	
	public PointTypeSegmentArray(PointTypeArray pointTypes) {
		PointTypeSegment currentSegment = null;
		
		for(int i=0; i<pointTypes.size(); i++) {
			if(currentSegment == null) {
				currentSegment = new PointTypeSegment(i,i,pointTypes.get(i));
				continue;
			}
			if(pointTypes.get(i)==currentSegment.getPointType()) {
				currentSegment.setEnd(i);
				continue;
			} else {
				add(currentSegment);
				currentSegment = new PointTypeSegment(i,i,pointTypes.get(i));
			}
		}
		add(currentSegment);		
	}
	
	public static PointTypeSegmentArray makeSegments(PointTypeArray pointTypes) {
		PointTypeSegmentArray pointTypeSegments = new PointTypeSegmentArray();
		PointTypeSegment currentSegment = null;
		
		for(int i=0; i<pointTypes.size(); i++) {
			if(currentSegment == null) {
				currentSegment = new PointTypeSegment(i,i,pointTypes.get(i));
				continue;
			}
			if(pointTypes.get(i)==currentSegment.getPointType()) {
				currentSegment.setEnd(i);
				continue;
			} else {
				pointTypeSegments.add(currentSegment);
				currentSegment = new PointTypeSegment(i-1,i,pointTypes.get(i));
			}
		}
		
		pointTypeSegments.add(currentSegment);
		
		return pointTypeSegments;
	}

	/**
	 * Devuelve true si existe algún segmento del tipo Border
	 * @return
	 */
	public boolean hasBorderSegments() {
		for(int i=0; i<size(); i++) {
			if(get(i).getPointType()== PointType.BORDER_POINT) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Devuelve true si hay algún segmento del tipo Null
	 * @return
	 */
	public boolean hasNullSegments() {
		for(int i=0; i<size(); i++) {
			if(get(i).getPointType()== PointType.NULL) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Devuelve true s exsten dos segmentos del tipo Grade consecutivos
	 * @return
	 */
	public boolean hasTwoGradesConsecutive() {
		for(int i=0; i<size()-1; i++) {
			if(get(i).getPointType()==PointType.GRADE && get(i+1).getPointType()==PointType.GRADE) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Para poder generar un GradeProfile a partir de una segmentación no puede haber
	 * dos Grades consecutivas, ni segmentos Null, ni segmentos Border
	 * 
	 * @return
	 */
	public boolean canGenerateGradeProfile() {
		if(hasBorderSegments() || hasNullSegments() || hasTwoGradeConsecutive()) {
			return false;
		}
		return true;
	}
	
	@Override
	public PointTypeSegmentArray clone() {
		PointTypeSegmentArray copy = new PointTypeSegmentArray();
		for(int i=0; i<this.size(); i++) {
			copy.add(this.get(i));
		}
		return copy;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<this.size(); i++) {
			builder.append(this.get(i).toString());
			builder.append("\n");
		}
		return builder.toString();
	}
}
