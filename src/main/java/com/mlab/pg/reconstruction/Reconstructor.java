package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;

import com.mlab.pg.util.MathUtil;
import com.mlab.pg.valign.GradeAlignment;
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

	protected XYVectorFunction originalGradePoints;
	protected TypeIntervalArray segmentation;
	protected VerticalGradeProfile gradeProfile;
	protected VerticalProfile verticalProfile;
	protected SegmentMaker2 segmentMaker;
	
	
	public Reconstructor(XYVectorFunction originalGradePoints, int mobilebasesize, double thresholdslope, double startZ) throws NullTypeException {
		this.originalGradePoints = originalGradePoints.clone();
		
		segmentMaker = new SegmentMaker2(originalGradePoints, mobilebasesize, thresholdslope);
		segmentation = segmentMaker.getResultTypeSegmentArray();
		
		gradeProfile = new VerticalGradeProfile();
		for(int i=0; i<segmentation.size(); i++) {
			int first = segmentation.get(i).getStart();
			int last = segmentation.get(i).getEnd();
			IntegerInterval interval = new IntegerInterval(first, last);
			double[] r = originalGradePoints.rectaMinimosCuadrados(interval);
			Straight straight = new Straight(r[0], r[1]);
			GradeProfileAlignment align = new GradeProfileAlignment(straight, originalGradePoints.getX(first), originalGradePoints.getX(last));
			//System.out.println(String.format("%f %f", align.getStartZ(), align.getEndZ()));
			gradeProfile.add(align);
		}
		adjustEndingsWithBeginnings2();
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
		for(int i=1; i<gradeProfile.size(); i++) {
			// Calcular el area bajo los puntos originales			
			double starts = gradeProfile.get(i-1).getEndS();
			double starty = gradeProfile.get(i-1).getEndZ();
			double ends = gradeProfile.get(i).getEndS();
			double area = originalGradePoints.areaEncerrada(starts, ends);
			double newendy = 2*area/(ends-starts) - starty;
			double[] newr = MathUtil.rectaPorDosPuntos(new double[]{starts,  starty}, new double[]{ends, newendy});
			Straight straight = new Straight(newr[0], newr[1]);
			GradeProfileAlignment align = new GradeProfileAlignment(straight, starts, ends);
			gradeProfile.set(i, align);
		}
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
	public VerticalProfile getVerticalProfile() {
		return verticalProfile;
	}
	
	public XYVectorFunction getOriginalPoints() {
		return originalGradePoints;
	}
	public TypeIntervalArray getSegments() {
		return segmentation;
	}
	public VerticalGradeProfile getGradeProfile() {
		return gradeProfile;
	}

}
