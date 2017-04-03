package com.mlab.pg.reconstruction;

import com.mlab.pg.graphics.FunctionDisplayer;
import com.mlab.pg.reconstruction.strategy.EndingsWithBeginnersAdjuster_Multiparameter;
import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;
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
	public ReconstructorByIntervals(XYVectorFunction originalGradePoints, double startZ, InterpolationStrategyType strategy) {
		super(originalGradePoints, startZ, strategy);
	}
	
	@Override
	public void processIterative() {
		Reconstructor rec = new Reconstructor(originalGradePoints, startZ, strategy.getInterpolationStrategyType());
		rec.processIterative();
		//int bestBaseSize = rec.getBaseSize();
		//double bestThresholdSlope = rec.getThresholdSlope();
		firstResultGradeProfile = rec.getGradeProfile();
		resultVerticalProfile = rec.getVerticalProfile();
		secondResultGradeProfile = new VerticalGradeProfile();
		parameterArray = new ParameterIntervalArray();
		secondResultGradeProfile.add(firstResultGradeProfile.get(0));
		ParameterInterval interval = new ParameterInterval(firstResultGradeProfile.get(0).getStartS(), firstResultGradeProfile.get(0).getEndS(), rec.getBaseSize(), rec.getThresholdSlope());
		parameterArray.add(interval);
		double x1 = secondResultGradeProfile.getEndS();
		double z1 = resultVerticalProfile.get(0).getEndZ();	
		for(int i=1; i<firstResultGradeProfile.size(); i++) {
			double x2 = firstResultGradeProfile.get(i).getEndS();
			XYVectorFunction finterval = originalGradePoints.extract(x1, x2);
			System.out.println("Alignment: " + i + ", Size: " + finterval.size());
			if(finterval.size() < 5) {
				interval = new ParameterInterval(x1, x2, rec.getBaseSize(), rec.getThresholdSlope());
				parameterArray.add(interval);
				secondResultGradeProfile.add(firstResultGradeProfile.get(i));
				ParameterIntervalArray auxarray = new ParameterIntervalArray();
				auxarray.add(interval);
				VerticalGradeProfile auxgprofile = new VerticalGradeProfile();
				auxgprofile.add(firstResultGradeProfile.get(i));
				VerticalProfile auxprofile = integrate(auxgprofile, auxarray, z1);
				z1 = auxprofile.getLastAlign().getEndZ();
			} else {
				int lastalign = secondResultGradeProfile.size()-1;
				//ReconstructionParameters parameters = reconstructInterval(finterval, z1);
				Reconstructor reco = reconstructInterval(finterval, z1);
				interval = new ParameterInterval(x1, x2, reco.getBaseSize(), reco.getThresholdSlope());
				parameterArray.add(interval);				
				z1 = reco.getResultVerticalProfilePoints().getEndY();
			}			
			x1 = x2;
			//z1 = integralVerticalProfilePoints.getY(x1);
		}
		EndingsWithBeginnersAdjuster_Multiparameter adjuster = new EndingsWithBeginnersAdjuster_Multiparameter(originalGradePoints, parameterArray);
		resultGradeProfile = adjuster.adjustEndingsWithBeginnings(secondResultGradeProfile);
		resultVerticalProfile = integrate(resultGradeProfile, parameterArray, startZ);
		resultVerticalProfilePoints = resultVerticalProfile.getSample(startX, endX, separacionMedia, true);
		calculateErrors(integralVerticalProfilePoints, resultVerticalProfilePoints);
		//showResults();
	}
	private VerticalProfile integrate(VerticalGradeProfile gprofile, ParameterIntervalArray parray, double startz) {
		if(gprofile.size()==0) {
			return null;
		}
		double currentStartZ = startz;
		VerticalProfile verticalProfile = new VerticalProfile();
		for(int i=0; i<gprofile.size(); i++) {
			GradeProfileAlignment current = gprofile.get(i);
			double th = parray.getParameters(current.getStartS()).getThresholdSlope();
			
			VAlignment valign = integrate(current, currentStartZ, th);
			verticalProfile.add(valign);
			currentStartZ = valign.getEndZ();
		}
		return verticalProfile;
	}
	private VAlignment integrate(GradeProfileAlignment galign, double startZ, double thresholdslope) {
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
	private Reconstructor reconstructInterval(XYVectorFunction gradepoints, double startz) {
		Reconstructor reco = new Reconstructor(gradepoints, startz, InterpolationStrategyType.EqualArea);
		reco.processIterative();
		
		for(int i=0; i< reco.getGradeProfile().size(); i++) {
			secondResultGradeProfile.add(reco.getGradeProfile().get(i));
		}
		//ReconstructionParameters params = new ReconstructionParameters(reco.getBaseSize(), reco.getThresholdSlope());
		//return params;
		return reco;
	}
	private void showResults() {
		double starts = firstResultGradeProfile.getStartS();
		double ends = firstResultGradeProfile.getEndS();
		double space = originalGradePoints.separacionMedia();
		XYVectorFunction data1 = firstResultGradeProfile.getSample(starts, ends, space, true);
		XYVectorFunction data2 = resultGradeProfile.getSample(starts, ends, space, true);
		FunctionDisplayer displayer = new FunctionDisplayer();
		displayer.showTwoFunctions(data1, data2, "Prueba", "first", "second", "S", "G");
	}
}
