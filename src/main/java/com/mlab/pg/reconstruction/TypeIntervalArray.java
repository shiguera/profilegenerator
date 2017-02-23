package com.mlab.pg.reconstruction;

import java.util.ArrayList;

/**
 * Es un ArrayList de TypeInterval. Se genera en el constructor, al pasarle un
 * PointTypeArray, que es un ArrayList de tipos de puntos
 * 
 * @author shiguera
 *
 */
public class TypeIntervalArray extends ArrayList<TypeInterval> {
	
	private static final long serialVersionUID = 1L;

	public TypeIntervalArray() {
		
	}
	
	public TypeIntervalArray(PointTypeArray pointTypes) {
		TypeInterval currentSegment = new TypeInterval(0,0,pointTypes.get(0));
		
		for(int i=1; i<pointTypes.size(); i++) {
			currentSegment.setEnd(i);
			if(pointTypes.get(i)==currentSegment.getPointType()) {
				continue;
			} else {
				add(currentSegment);
				currentSegment = new TypeInterval(i,i,pointTypes.get(i));
			}
		}
		add(currentSegment);		
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
		if(hasBorderSegments() || hasNullSegments() || hasTwoGradesConsecutive()) {
			return false;
		}
		return true;
	}
	
	
	public TypeIntervalArray copy() {
		TypeIntervalArray copy = new TypeIntervalArray();
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
