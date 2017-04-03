package com.mlab.pg.reconstruction.strategy;

public interface InterpolationStrategy {

	InterpolationStrategyType getInterpolationStrategyType();
	PointCharacteriserStrategy getPointCharacteriserStrategy();
	ProcessBorderIntervalsStrategy getProcessBorderIntervalStrategy();
	
}
