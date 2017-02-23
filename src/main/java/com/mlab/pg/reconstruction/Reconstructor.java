package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;

import com.mlab.pg.valign.GradeProfileAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.IntegerInterval;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

/**
 * Genera un GradeProfile y un VerticalProfile a partir de un XYVectorFunction con los puntos originales {si, gi} 
 * Para ello utiliza un SegmentMaker que genera una Segmentation que solo tiene segmentos
 * del tipo Grade y VerticalCurve.
 * Ofrece una pareja de métodos getGradeProfile() que devuelve el GradeProfile reconstruido y 
 * getVerticalProfile() que devuelve el perfil longitudinal VerticalProfile correspondiente
 * a la integración del GradeProfile
 * 
 * @author shiguera
 *
 */
public class Reconstructor {

	Logger LOG = Logger.getLogger(Reconstructor.class);

	protected XYVectorFunction originalPoints;
	protected TypeIntervalArray segmentation;
	protected VerticalGradeProfile gradeProfile;
	protected VerticalProfile verticalProfile;
	protected SegmentMaker segmentMaker;
	
	
	public Reconstructor(XYVectorFunction originalPoints, int mobilebasesize, double thresholdslope, double startZ) throws NullTypeException {
		this.originalPoints = originalPoints.clone();
		
		segmentMaker = new SegmentMaker(originalPoints, mobilebasesize, thresholdslope);
		segmentation = segmentMaker.getResultSegmentation();
		
		gradeProfile = new VerticalGradeProfile();
		for(int i=0; i<segmentation.size(); i++) {
			int first = segmentation.get(i).getStart();
			int last = segmentation.get(i).getEnd();
			IntegerInterval interval = new IntegerInterval(first, last);
			double[] r = originalPoints.rectaMinimosCuadrados(interval);
			Straight straight = new Straight(r[0], r[1]);
			GradeProfileAlignment align = new GradeProfileAlignment(straight, originalPoints.getX(first), originalPoints.getX(last));
			//System.out.println(String.format("%f %f", align.getStartZ(), align.getEndZ()));
			gradeProfile.add(align);
		}
		adjustEndingsWithBeginnings();
		verticalProfile = gradeProfile.integrate(startZ);
		
	}
	/** 
	 * Ajusta los finales y principios de alineaciones
	 * para que pasen por la misma z. Para ello, cada 
	 * alineación excepto la primera la sustituye por una 
	 * recta paralela que pase por la z final de la 
	 * alineación anterior
	 */
	private void adjustEndingsWithBeginnings() {
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
	}
	public VerticalProfile getVerticalProfile() {
		return verticalProfile;
	}
	
	public XYVectorFunction getOriginalPoints() {
		return originalPoints;
	}
	public TypeIntervalArray getSegments() {
		return segmentation;
	}
	public VerticalGradeProfile getGradeProfile() {
		return gradeProfile;
	}

}
