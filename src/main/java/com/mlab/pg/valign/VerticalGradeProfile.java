package com.mlab.pg.valign;

import java.util.ArrayList;

import org.jfree.util.Log;

import com.mlab.pg.xyfunction.XYVector;
import com.mlab.pg.xyfunction.XYVectorFunction;

/**
 * ArrayList de elementos GradeProfileAlignment 
 * @author shiguera
 *
 */
public class VerticalGradeProfile extends ArrayList<GradeProfileAlignment> {

	private static final long serialVersionUID = 1L;
	
	public VerticalGradeProfile() {
		super();
	}
	
	public GradeProfileAlignment getFirstAlign() {
		if (size() > 0) {
			return get(0);
		} else {
			return null;
		}
	}
	public GradeProfileAlignment getLastAlign() {
		if (size() > 0) {
			return get(size()-1);
		} else {
			return null;
		}
	}
	public GradeProfileAlignment getAlign(double x) {
		if(size()==0 || x<getStartS() || x>getEndS()) {
			return null;
		}
		for(int i=0; i<size(); i++) {
			if(x>=get(i).getStartS() && x<=get(i).getEndS()) {
				return get(i);
			}
		}
		return null;
	}
	public GradeProfileAlignment getAlign(int i) {
		if(i<0 || i>=size()) {
			return null;
		}
		return get(i);
	}

	public int getAlignIndex(double x) {
		if(size()==0 || x<getStartS() || x>getEndS()) {
			return -1;
		}
		for(int i=0; i<size(); i++) {
			if(x>=get(i).getStartS() && x<=get(i).getEndS()) {
				return i;
			}
		}
		return -1;
	}
	public double getStartS() {
		if (size() > 0) {
			return get(0).getStartS();
		} else {
			return Double.NaN;
		}
	}
	public double getEndS() {
		if (size() > 0) {
			return get(size()-1).getEndS();
		} else {
			return Double.NaN;
		}
	}
	public XYVectorFunction getSample(double starts, double ends, double space, boolean includeLastPoint) {
		if(starts>getEndS() || ends<getStartS()) {
			return null;
		}
		if(starts<getStartS()) {
			starts=getStartS();
		}
		if(ends>getEndS()) {
			ends = getEndS();
		}
		XYVectorFunction sample = new XYVectorFunction();
		double x = starts;
		GradeProfileAlignment align = null;
		for(x=starts; x<=ends; x+=space) {
			align = getAlign(x);
			if(align==null) {
				Log.warn("align=null");
			}
			sample.add(new double[]{x, align.getY(x)});
		}
		if(includeLastPoint && sample.getEndX()<ends) {
			sample.add(new double[]{ends, align.getY(ends)});
		}
		return sample;
	}
	
	/**
	 * Calcula el error cuadrático medio entre los puntos de dos perfiles 
	 * de pendientes. 
	 * 
	 * @param gp2 segundo perfil
	 * @param spaceBetweenPoints Separación entre los puntos de los perfiles sobre los
	 * que se medirá el error cuadrático
	 * @return ecm
	 */
	public double ecm(VerticalGradeProfile gp2, double spaceBetweenPoints) {
		
		// Ajustar al mismo punto de inicio
		if (gp2.getStartS() < this.getStartS()) {
			this.getFirstAlign().setStartS(gp2.getStartS());
		} else {
			gp2.getFirstAlign().setStartS(this.getStartS());
		}
		// Ajustar al mismo punto final
		if(gp2.getEndS() > this.getEndS()) {
			this.getLastAlign().setEndS(gp2.getEndS());
		} else {
			gp2.getLastAlign().setEndS(this.getEndS());
		}
		
		XYVector sample1 = getSample(getStartS(), getEndS(), spaceBetweenPoints, true);
		XYVector sample2 = gp2.getSample(gp2.getStartS(), gp2.getEndS(), spaceBetweenPoints, true);

		double ecm = 0.0;
		for(int i=0; i<sample1.size(); i++) {
			double dif = sample1.getY(i)-sample2.getY(i);
			ecm = ecm + dif*dif;
		}
		ecm = ecm / sample1.size();
		return ecm;
	}
	
	/**
	 * Integra un perfil de pendientes obteniendo el perfil longitudinal correspondiente.
	 * @param startZ Altitud del primer punto del perfil.
	 * @return Perfil longitudinal
	 */
	public VerticalProfile integrate(double startZ, double thresholdSlope) {
		if(this.size()==0) {
			return null;
		}
		double currentStartZ = startZ;
		VerticalProfile verticalProfile = new VerticalProfile();
		for(int i=0; i<this.size(); i++) {
			VAlignment valign = this.get(i).integrate(currentStartZ, thresholdSlope);
			verticalProfile.add(valign);
			currentStartZ = valign.getEndZ();
		}
		return verticalProfile;
	}
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Grade Profile \n");
		builder.append(VAlignment.CABECERA);
		builder.append('\n');
		for(int i=0; i<this.size(); i++) {
			builder.append(this.get(i).toString());
			builder.append('\n');
		}
		return builder.toString();
	}

}
