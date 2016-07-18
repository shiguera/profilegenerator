package com.mlab.pg.reconstruction;

import com.mlab.pg.xyfunction.XYVectorFunction;

public class SegmentMaker {

	XYVectorFunction gradeSample;
	int mobileBaseSize;
	double thresholdSlope;
	PointTypeArray pointTypes;
	PointTypeSegmentArray pointTypeSegments;
	
	public SegmentMaker(XYVectorFunction gradesample, int mobilebasesize, double thresholdslope) {
		this.gradeSample = gradesample;
		this.mobileBaseSize = mobilebasesize;
		this.thresholdSlope = thresholdslope;
		ProfileCharacteriser characteriser = new ProfileCharacteriser();
		this.pointTypes = characteriser.characterise(gradeSample, mobileBaseSize, thresholdSlope); 
		this.pointTypeSegments = new PointTypeSegmentArray(pointTypes);
	}

	/**
	 * Procesa las sucesiones de segmentos y cuando se encuentra una
	 * sucesión VC - VCVC - VCE - G añade el segmento VCVC al segmento VC
	 */
	public void processVerticalCurveEndings() {
		PointTypeSegmentArray newPointTypeSegments = new PointTypeSegmentArray();
		int lastsegment = pointTypeSegments.size() -1;
		int lastsegmentadded = -1;
		for(int i=0; i<= lastsegment-3; i++) {
			if(pointTypeSegments.get(i).getPointType()==PointType.VERTICAL_CURVE && 
					pointTypeSegments.get(i+1).getPointType()==PointType.VERTICALCURVE_TO_VERTICALCURVE &&
					pointTypeSegments.get(i+2).getPointType()==PointType.VERTICALCURVE_END &&
					pointTypeSegments.get(i+3).getPointType()==PointType.GRADE) {
				// Convierte en VerticalCurve el segmento VerticalCurveToVerticalCurve
				//int firstpoint = pointTypeSegments.get(i+2).getStart();
				int lastPoint = pointTypeSegments.get(i+2).getEnd();
				newPointTypeSegments.add(pointTypeSegments.get(i));
				newPointTypeSegments.add(pointTypeSegments.get(i+2));
				newPointTypeSegments.add(pointTypeSegments.get(i+3));				
				newPointTypeSegments.get(newPointTypeSegments.size()-3).setEnd(lastPoint);
				i = i+3;
			} else {
				newPointTypeSegments.add(pointTypeSegments.get(i));
			}
			lastsegmentadded = i;
		}
		if (lastsegmentadded < lastsegment) {
			for(int i=lastsegmentadded + 1; i<=lastsegment; i++) {
				newPointTypeSegments.add(pointTypeSegments.get(i));
			}			
		}
		pointTypeSegments = newPointTypeSegments.clone();
	}
	/**
	 * Procesa las sucesiones de segmentos y cuando se encuentra una
	 * sucesión G-VCB-VCVC-VC añade el segmento VCVC al segmento VC
	 */
	public void processVerticalCurveBeginnings() {
		PointTypeSegmentArray newPointTypeSegments = new PointTypeSegmentArray();
		int lastsegment = pointTypeSegments.size() -1;
		int lastsegmentadded = -1;
		for(int i=0; i<= lastsegment-3; i++) {
			if(pointTypeSegments.get(i).getPointType()==PointType.GRADE && 
					pointTypeSegments.get(i+1).getPointType()==PointType.VERTICALCURVE_BEGINNING &&
					pointTypeSegments.get(i+2).getPointType()==PointType.VERTICALCURVE_TO_VERTICALCURVE &&
					pointTypeSegments.get(i+3).getPointType()==PointType.VERTICAL_CURVE) {
				// Convierte en VerticalCurve el segmento VerticalCurveToVerticalCurve
				int firstpoint = pointTypeSegments.get(i+2).getStart();
				newPointTypeSegments.add(pointTypeSegments.get(i));
				newPointTypeSegments.add(pointTypeSegments.get(i+1));
				newPointTypeSegments.add(pointTypeSegments.get(i+3));				
				newPointTypeSegments.get(newPointTypeSegments.size()-1).setStart(firstpoint);
				i = i+3;
			} else {
				newPointTypeSegments.add(pointTypeSegments.get(i));
			}
			lastsegmentadded = i;
		}
		if (lastsegmentadded < lastsegment) {
			for(int i=lastsegmentadded + 1; i<=lastsegment; i++) {
				newPointTypeSegments.add(pointTypeSegments.get(i));
			}			
		}
		pointTypeSegments = newPointTypeSegments.clone();
	}
	// Getter -Setter
	public XYVectorFunction getGradeSample() {
		return gradeSample;
	}

	public int getMobileBaseSize() {
		return mobileBaseSize;
	}

	public double getThresholdSlope() {
		return thresholdSlope;
	}

	public PointTypeArray getPointTypes() {
		return pointTypes;
	}

	public PointTypeSegmentArray getPointTypeSegments() {
		return pointTypeSegments;
	}

	
	
	
	
	
}
