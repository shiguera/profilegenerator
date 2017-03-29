package com.mlab.pg.reconstruction.strategy;

import com.mlab.pg.reconstruction.TypeIntervalArray;
import com.mlab.pg.xyfunction.XYVectorFunction;

public interface ProcessBorderIntervalsStrategy {
	
	TypeIntervalArray processBorderIntervals(XYVectorFunction originalgradepoints, int basesize, double thresholdslope, PointCharacteriserStrategy strategy);

	TypeIntervalArray getOriginalTypeIntervalArray();
	TypeIntervalArray getResultTypeIntervalArray();
	
}
