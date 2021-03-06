package com.mlab.pg.reconstruction.strategy;

import com.mlab.pg.xyfunction.XYVectorFunction;

public interface PointCharacteriserStrategy {
	
	double[] calculaRectaAnterior(XYVectorFunction function, int pointindex, int mobilesize);
	double[] calculaRectaPosterior(XYVectorFunction function, int pointindex, int mobilesize);
	

}
