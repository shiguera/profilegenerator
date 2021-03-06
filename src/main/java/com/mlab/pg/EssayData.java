package com.mlab.pg;

import com.mlab.pg.reconstruction.strategy.InterpolationStrategyType;

public class EssayData {

	
	String essayName;
	String carretera;
	String sentido;
	String graphTitle;
	String inPath;
	String outPath;
	String xyzFileName;
	String szFileName;
	String sgFileName;
	String reportFileName;
	
	double startS=-1.0, endS=-1.0, startZ=0.0;
	
	InterpolationStrategyType interpolationStrategy;
	double SHORT_ALIGNMENT_LENGTH =50.0;

	
	public EssayData() {
	
	}

	// Getters
	public String getInPath() {
		return inPath;
	}

	public void setInPath(String inPath) {
		this.inPath = inPath;
	}

	public String getOutPath() {
		return outPath;
	}

	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}

	public String getSzFileName() {
		return szFileName;
	}

	public void setSzFileName(String szFileName) {
		this.szFileName = szFileName;
	}

	public String getSgFileName() {
		return sgFileName;
	}

	public void setSgFileName(String sgFileName) {
		this.sgFileName = sgFileName;
	}

	public String getEssayName() {
		return essayName;
	}

	public void setEssayName(String essayName) {
		this.essayName = essayName;
	}

	public String getGraphTitle() {
		return graphTitle;
	}

	public void setGraphTitle(String graphTitle) {
		this.graphTitle = graphTitle;
	}

	public String getXyzFileName() {
		return xyzFileName;
	}

	public void setXyzFileName(String xyzFileName) {
		this.xyzFileName = xyzFileName;
	}

	public InterpolationStrategyType getInterpolationStrategy() {
		return interpolationStrategy;
	}

	public void setInterpolationStrategy(InterpolationStrategyType interpolationStrategy) {
		this.interpolationStrategy = interpolationStrategy;
	}

	public double getStartS() {
		return startS;
	}

	public void setStartS(double startS) {
		this.startS = startS;
	}

	public double getEndS() {
		return endS;
	}

	public void setEndS(double endS) {
		this.endS = endS;
	}

	public double getStartZ() {
		return startZ;
	}

	public void setStartZ(double startZ) {
		this.startZ = startZ;
	}

	public String getReportFileName() {
		return reportFileName;
	}

	public void setReportFileName(String reportFileName) {
		this.reportFileName = reportFileName;
	}

	public double getSHORT_ALIGNMENT_LENGTH() {
		return SHORT_ALIGNMENT_LENGTH;
	}

	public void setSHORT_ALIGNMENT_LENGTH(double sHORT_ALIGNMENT_LENGTH) {
		SHORT_ALIGNMENT_LENGTH = sHORT_ALIGNMENT_LENGTH;
	}

	public String getCarretera() {
		return carretera;
	}

	public void setCarretera(String carretera) {
		this.carretera = carretera;
	}

	public String getSentido() {
		return sentido;
	}

	public void setSentido(String sentido) {
		this.sentido = sentido;
	}

}
