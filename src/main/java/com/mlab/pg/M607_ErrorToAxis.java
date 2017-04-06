package com.mlab.pg;

import java.io.File;

import com.mlab.pg.util.IOUtil;
import com.mlab.pg.util.MathUtil;

public class M607_ErrorToAxis {

	public M607_ErrorToAxis() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		String path = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/TracksLeikaMaria";
		String outfilenamecomplete = IOUtil.composeFileName(path, "M607_Leika_Axis_xyz.csv");
		double[][] trackaxis = IOUtil.read(new File(outfilenamecomplete), ",", 0);
		String axisfilenamecomplete = IOUtil.composeFileName(path, "m607_axis_real.csv");
		double[][] axis = IOUtil.read(new File(axisfilenamecomplete), ",", 0);
		
		double d = 0.0;
		for(int i=0; i<trackaxis.length; i++) {
			double[] P = trackaxis[i];
			double dist = MathUtil.distancePointToPoliline(P, axis);
			d = d + dist;
		}
		d = d / trackaxis.length;
		
		System.out.println(d);
		
	}

}
