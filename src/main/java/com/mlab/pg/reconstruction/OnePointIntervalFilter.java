package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;

public class OnePointIntervalFilter {

	Logger LOG = Logger.getLogger(getClass());
	
	TypeIntervalArray filteredTypeIntervalArray;
	/**
	 * Filtra un TypeIntervalArray: Los tramos que son de un solo punto
	 * los convierte en tipo BorderPoint
	 */
	public OnePointIntervalFilter(TypeIntervalArray originalTypeIntervalArray) {
		filteredTypeIntervalArray = new TypeIntervalArray();
		for(int i=0; i<originalTypeIntervalArray.size(); i++) {
			TypeInterval currentInterval = originalTypeIntervalArray.get(i);
			if(i>0 && i<originalTypeIntervalArray.size()-1 && currentInterval.size() == 1 && currentInterval.getPointType() != PointType.BORDER_POINT) {
				TypeInterval previousInterval = originalTypeIntervalArray.get(i-1);
				if(previousInterval.getPointType()==PointType.BORDER_POINT) {
					filteredTypeIntervalArray.get(filteredTypeIntervalArray.size()-1).setEnd(currentInterval.getEnd());
				} else {
					TypeInterval followingInterval = originalTypeIntervalArray.get(i+1);
					if(followingInterval.getPointType()==PointType.BORDER_POINT) {
						TypeInterval newInterval = followingInterval.copy();
						newInterval.setStart(currentInterval.getStart());
						filteredTypeIntervalArray.add(newInterval);
						i++;
					} else {
						TypeInterval newInterval = currentInterval.copy();
						filteredTypeIntervalArray.add(newInterval);
					}
				}
			} else {
				filteredTypeIntervalArray.add(currentInterval);
			}
		}
	}
	public TypeIntervalArray getFilteredTypeIntervalArray() {
		return filteredTypeIntervalArray;
	}

}
