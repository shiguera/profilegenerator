package com.mlab.pg.valign;

import java.util.ArrayList;

import com.mlab.pg.xyfunction.XYVector;
import com.mlab.pg.xyfunction.XYVectorFunction;

/**
 * ArrayList de elementos VerticalProfileAlign para una categoría de carretera 
 * definida por su velocidad de proyecto DesignSpeed
 * 
 * @author shiguera
 *
 */
public class VerticalProfile extends ArrayList<VAlignment>  {

	private static final long serialVersionUID = 1L;

	public VerticalProfile() {
		super();
	}
	
	public boolean add(VerticalProfile vp) {
		if (vp == null || vp.size() == 0) {
			return false;
		}
		boolean globalResult = true;
		for(int i=0; i<vp.size(); i++) {
			boolean partialResult = this.add(vp.get(i));
			if (partialResult == false) {
				globalResult = false;
			}
		}
		return globalResult;
	}
	public VAlignment getFirstAlign() {
		if (size() > 0) {
			return get(0);
		} else {
			return null;
		}
	}
	public VAlignment getLastAlign() {
		if (size() > 0) {
			return get(size()-1);
		} else {
			return null;
		}
	}
	public VAlignment getAlign(double x) {
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
	public VAlignment getAlign(int i) {
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
	public double getLength() {
		if (size() > 0) {
			return getEndS() - getStartS();
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
		VAlignment align = null;
		for(x=starts; x<=ends; x+=space) {
			align = getAlign(x);
			sample.add(new double[]{x, align.getY(x)});
		}
		if(includeLastPoint && sample.getEndX()<ends) {
			sample.add(new double[]{ends, align.getY(ends)});
		}
		return sample;
	}
	// Extend ArrayList
	@Override
	public boolean add(VAlignment align) {
		return super.add(align);			
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Vertical Profile \n");
		builder.append(VAlignment.CABECERA);
		builder.append('\n');
		for(int i=0; i<this.size(); i++) {
			builder.append(this.get(i).toString());
			builder.append('\n');
		}
		return builder.toString();
	}
	
	public String toString2() {
		StringBuilder builder = new StringBuilder();
		builder.append("Vertical Profile \n");
		builder.append(VAlignment.CABECERA2);
		builder.append('\n');
		for(int i=0; i<this.size(); i++) {
			builder.append(this.get(i).toString2());
			builder.append('\n');
		}
		return builder.toString();
	}

	/**
	 * Calcula el error cuadrático medio entre los puntos de dos perfiles longitudinales. 
	 * 
	 * @param vp2
	 * @param spaceBetweenPoints Separación entre los puntos de los perfiles sobre los
	 * que se medirá el error cuadrático
	 * @return ecm
	 */
	public double ecm(VerticalProfile vp2, double spaceBetweenPoints) {
		
		// Ajustar al mismo punto de inicio
		if (vp2.getStartS() < this.getStartS()) {
			this.getFirstAlign().setStartS(vp2.getStartS());
		} else {
			vp2.getFirstAlign().setStartS(this.getStartS());
		}
		// Ajustar al mismo punto final
		if(vp2.getEndS() > this.getEndS()) {
			this.getLastAlign().setEndS(vp2.getEndS());
		} else {
			vp2.getLastAlign().setEndS(this.getEndS());
		}
		
		XYVector sample1 = getSample(getStartS(), getEndS(), spaceBetweenPoints, true);
		XYVector sample2 = vp2.getSample(vp2.getStartS(), vp2.getEndS(), spaceBetweenPoints, true);

		double ecm = 0.0;
		for(int i=0; i<sample1.size(); i++) {
			double dif = sample1.getY(i)-sample2.getY(i);
			ecm = ecm + dif*dif;
		}
		ecm = ecm / sample1.size();
		return ecm;
	}

	public VerticalGradeProfile derivative() {
		VerticalGradeProfile gprofile =new VerticalGradeProfile();
		for(int i=0; i<size(); i++) {
			GradeProfileAlignment galign = (GradeProfileAlignment) getAlign(i).derivative();
			gprofile.add(galign);
		}
		return gprofile;
	}

}
