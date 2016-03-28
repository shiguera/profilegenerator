package com.mlab.pg.valign;

import java.util.ArrayList;

import com.mlab.pg.norma.DesignSpeed;

/**
 * ArrayList de elementos VerticalProfileAlign para una categoría de carretera 
 * definida por su velocidad de proyecto DesignSpeed
 * @author shiguera
 *
 */
public class VerticalProfile extends ArrayList<VerticalProfileAlign>  {

	private static final long serialVersionUID = 1L;

	protected DesignSpeed designSpeed;
	
	public VerticalProfile(DesignSpeed dspeed) {
		super();
		this.designSpeed = dspeed;
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
	public VerticalProfileAlign getFirstAlign() {
		if (size() > 0) {
			return get(0);
		} else {
			return null;
		}
	}
	public VerticalProfileAlign getLastAlign() {
		if (size() > 0) {
			return get(size()-1);
		} else {
			return null;
		}
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

	// Extend ArrayList
	@Override
	public boolean add(VerticalProfileAlign align) {
		if(align.getDesignSpeed()==this.designSpeed) {
			
			return super.add(align);			
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Vertical Profile (DesignSpeed= ");
		builder.append(designSpeed);
		builder.append(")\n");
		builder.append(VAlign.CABECERA);
		builder.append('\n');
		for(int i=0; i<this.size(); i++) {
			builder.append(this.get(i).toString());
			builder.append('\n');
		}
		return builder.toString();
	}

}
