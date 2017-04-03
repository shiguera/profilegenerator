package com.mlab.pg.reconstruction.strategy;

import com.mlab.pg.reconstruction.ParameterIntervalArray;
import com.mlab.pg.reconstruction.TypeIntervalArray;
import com.mlab.pg.xyfunction.XYVectorFunction;

public interface ProcessBorderIntervalsStrategy {
	
	TypeIntervalArray processBorderIntervals(XYVectorFunction originalgradepoints, TypeIntervalArray typeIntervalArray, int basesize, double thresholdslope);
	TypeIntervalArray processBorderIntervals(XYVectorFunction originalgradepoints, ParameterIntervalArray parameterarray);

	
	TypeIntervalArray getOriginalTypeIntervalArray();
	TypeIntervalArray getResultTypeIntervalArray();
	
}
