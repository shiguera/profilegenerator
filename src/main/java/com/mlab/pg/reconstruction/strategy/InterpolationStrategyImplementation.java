package com.mlab.pg.reconstruction.strategy;

public class InterpolationStrategyImplementation implements InterpolationStrategy {

	InterpolationStrategyType interpolationStrategyType;
	PointCharacteriserStrategy pointCharacteriserStrategy;
	ProcessBorderIntervalsStrategy processBorderIntervalsStrategy;
	
	public InterpolationStrategyImplementation(InterpolationStrategyType interpolatiosStrategy) {

		interpolationStrategyType = interpolatiosStrategy;
		
		if (interpolationStrategyType == InterpolationStrategyType.EqualArea ) {
			pointCharacteriserStrategy = new PointCharacteriserStrategy_EqualArea();
			processBorderIntervalsStrategy = new ProcessBorderIntervalsStrategy_EqualArea();	
		} else if (interpolationStrategyType == InterpolationStrategyType.EqualArea_Multiparameter) {
			pointCharacteriserStrategy = new PointCharacteriserStrategy_Multiparameter();
			processBorderIntervalsStrategy = new ProcessBorderIntervalsStrategy_Multiparameter();
		} else{
			pointCharacteriserStrategy = new PointCharacteriserStrategy_LessSquares();
			processBorderIntervalsStrategy = new ProcessBorderIntervalsStrategy_LessSquares();				
		}
	}

	@Override
	public PointCharacteriserStrategy getPointCharacteriserStrategy() {
		return pointCharacteriserStrategy;
	}

	@Override
	public ProcessBorderIntervalsStrategy getProcessBorderIntervalStrategy() {
		return processBorderIntervalsStrategy;
	}


	@Override
	public InterpolationStrategyType getInterpolationStrategyType() {
		return interpolationStrategyType;
	}

}
