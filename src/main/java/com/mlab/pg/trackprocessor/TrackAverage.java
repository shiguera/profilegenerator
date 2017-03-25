package com.mlab.pg.trackprocessor;

public class TrackAverage {

	double[][] track1;
	double[][] track2;
	public TrackAverage() {

	}

	
	public double[][] average(double[][] track1, double[][] track2) {
		this.track1 = track1;
		this.track2 = track2;
		double[][] resultTrack = new double[track1.length][3];
		for(int i=0; i<track1.length; i++) {
			int nearestIndex = nearestInTrack2(i);
			double[] averagePoint = averagePoint(i, nearestIndex);
			resultTrack[i] = new double[]{averagePoint[0], averagePoint[1], averagePoint[2]};
		}
		return resultTrack;
	}

	private double[] averagePoint(int indexInTrack1, int indexInTrack2) {
		double x = (track1[indexInTrack1][0] + track2[indexInTrack2][0])/2.0;
		double y = (track1[indexInTrack1][1] + track2[indexInTrack2][1])/2.0;
		double z = (track1[indexInTrack1][2] + track2[indexInTrack2][2])/2.0;
		return new double[]{x, y, z};
	}
	
	private int nearestInTrack2(int indexInTrack1) {
		double x1 = track1[indexInTrack1][0];
		double y1 = track1[indexInTrack1][1];
		double d = Math.sqrt((x1-track2[0][0])*(x1-track2[0][0]) + (y1-track2[0][1])*(y1-track2[0][1]));
		double dmin = d;
		int indexMin = 0;
		for(int i=1; i<track2.length; i++) {
			d = Math.sqrt((x1-track2[i][0])*(x1-track2[i][0]) + (y1-track2[i][1])*(y1-track2[i][1]));
			if(d<dmin) {
				dmin = d;
				indexMin = i;
			}
		}
		return indexMin;
	}
}
