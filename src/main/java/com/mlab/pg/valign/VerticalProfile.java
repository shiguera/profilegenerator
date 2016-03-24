package com.mlab.pg.valign;

import java.util.ArrayList;

import com.mlab.pg.norma.DesignSpeed;

/**
 * ArrayList de elementos VAlign para una categoría de carretera 
 * definida por su velocidad de proyecto DesignSpeed
 * @author shiguera
 *
 */
public class VerticalProfile extends ArrayList<VAlign> {

	private static final long serialVersionUID = 1L;

	protected DesignSpeed designSpeed;
	
	public VerticalProfile(DesignSpeed dspeed) {
		super();
		this.designSpeed = dspeed;
	}
	
	@Override
	public boolean add(VAlign align) {
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
