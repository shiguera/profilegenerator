package com.mlab.pg.reconstruction;

import com.mlab.pg.graphics.FunctionDisplayer;
import com.mlab.pg.reconstruction.strategy.EndingsWithBeginnersAdjuster_Multiparameter;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategy;
import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.GradeProfileAlignment;
import com.mlab.pg.valign.VAlignment;
import com.mlab.pg.valign.VerticalCurveAlignment;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.Parabole;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class ReconstructorByIntervals extends Reconstructor {

	
	
	protected VerticalGradeProfile firstResultGradeProfile;
	protected VerticalGradeProfile secondResultGradeProfile;
	protected ParameterIntervalArray parameterArray = new ParameterIntervalArray();
	
	/**
	 * Reconstruye un perfil con distinto valor de los parámetros 
	 * baseSize y thresholdSlope en diferentes tramos
	 */
	public ReconstructorByIntervals(XYVectorFunction originalGradePoints, double startZ, InterpolationStrategy strategy) {
		super(originalGradePoints, startZ, strategy);
	}
	
	@Override
	public void processIterative() {
		Reconstructor rec = new Reconstructor(originalGradePoints, startZ, interpolationStrategy);
		rec.processIterative();
		int bestBaseSize = rec.getBaseSize();
		double bestThresholdSlope = rec.getThresholdSlope();
		firstResultGradeProfile = rec.getGradeProfile();
		resultVerticalProfile = rec.getVerticalProfile();
		secondResultGradeProfile = new VerticalGradeProfile();
		parameterArray = new ParameterIntervalArray();
		double x1 = firstResultGradeProfile.get(0).getStartS();
		for(int i=0; i<firstResultGradeProfile.size(); i++) {
			
			double x2 = firstResultGradeProfile.get(i).getEndS();
			double z1 = resultVerticalProfile.get(i).getY(x1);
			if(z1==Double.NaN) {
				System.out.println("P");
			}
			XYVectorFunction finterval = originalGradePoints.extract(x1, x2);
			System.out.println("Alignment: " + i + ", Size: " + finterval.size());
			if(finterval.size() < 5) {
				ParameterInterval interval = new ParameterInterval(x1, x2, rec.getBaseSize(), rec.getThresholdSlope());
				parameterArray.add(interval);
				secondResultGradeProfile.add(firstResultGradeProfile.get(i));
			} else {
				ReconstructionParameters parameters = reconstructInterval(finterval, z1);
				ParameterInterval interval = new ParameterInterval(x1, x2, parameters.getBaseSize(), parameters.getThresholdSlope());
				parameterArray.add(interval);				
			}
			x1 = x2;
		}
		EndingsWithBeginnersAdjuster_Multiparameter adjuster = new EndingsWithBeginnersAdjuster_Multiparameter(originalGradePoints, parameterArray);
		gradeProfile = adjuster.adjustEndingsWithBeginnings(secondResultGradeProfile);
		resultVerticalProfile = integrate();
		calculateErrors();
		//showResults();
	}
	private VerticalProfile integrate() {
		if(gradeProfile.size()==0) {
			return null;
		}
		double currentStartZ = startZ;
		VerticalProfile verticalProfile = new VerticalProfile();
		for(int i=0; i<gradeProfile.size(); i++) {
			GradeProfileAlignment current = gradeProfile.get(i);
			double th = parameterArray.getParameters(current.getStartS()).getThresholdSlope();
			
			VAlignment valign = integrate(current, currentStartZ, th);
			verticalProfile.add(valign);
			currentStartZ = valign.getEndZ();
		}
		return verticalProfile;
	}
	private VAlignment integrate(GradeProfileAlignment galign, double startZ, double thresholdslope) {
		this.thresholdSlope = thresholdslope;
		VAlignment valign = null;
		double s1 = galign.getStartS();
		double g1 = galign.getStartZ();
		double s2 = galign.getEndS();
		double g2 = galign.getEndZ();
		if(Math.abs(galign.getPolynom2().getA1()) < thresholdslope) {
			double a1 = g1;
			double a0 = startZ - s1*g1;
			Straight r = new Straight(a0,a1);
			valign = new GradeAlignment(r, s1, s2);
		} else {
			double a2 = (g2-g1)/2/(s2-s1);
			double a1 = g2 - 2*a2*s2;
			double a0 = startZ - a1*s1 - a2 * s1 * s1;
			Parabole p = new Parabole(a0,a1,a2);
			valign = new VerticalCurveAlignment(p,s1,s2);
		}
		return valign;
	}
	private ReconstructionParameters reconstructInterval(XYVectorFunction gradepoints, double startz) {
		Reconstructor reco = new Reconstructor(gradepoints, startz, InterpolationStrategy.EqualArea);
		reco.processIterative();
		
		for(int i=0; i< reco.getGradeProfile().size(); i++) {
			secondResultGradeProfile.add(reco.getGradeProfile().get(i));
		}
		ReconstructionParameters params = new ReconstructionParameters(reco.getBaseSize(), reco.getThresholdSlope());
		return params;
	}
	private void showResults() {
		double starts = firstResultGradeProfile.getStartS();
		double ends = firstResultGradeProfile.getEndS();
		double space = originalGradePoints.separacionMedia();
		XYVectorFunction data1 = firstResultGradeProfile.getSample(starts, ends, space, true);
		XYVectorFunction data2 = gradeProfile.getSample(starts, ends, space, true);
		FunctionDisplayer displayer = new FunctionDisplayer();
		displayer.showTwoFunctions(data1, data2, "Prueba", "first", "second", "S", "G");
	}
}
