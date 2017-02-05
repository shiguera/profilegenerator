package com.mlab.pg.graphics;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.mlab.pg.xyfunction.XYVectorFunction;

public class Charter {
	
	private static Logger LOG = Logger.getLogger(Charter.class);
	
	
	XYSeriesCollection seriesCollection;
	String name, xLabel, yLabel;
	
	public Charter(String name, String xlabel, String ylabel) {
		LOG.debug("Charter()");
		this.name = name;
		this.xLabel = xlabel;
		this.yLabel = ylabel;
		seriesCollection = new XYSeriesCollection();
		
		
		
	}
	
	public void addXYVectorFunction(XYVectorFunction func) {
		LOG.debug("addXYVectorFunction()");
		XYSeries series = new XYSeries(String.format("%d", getSeriesCount()));
		for(int i=0; i<func.size(); i++) {
			series.add(func.getX(i), func.getY(i));
		}
		seriesCollection.addSeries(series);
	}
	
	public ChartPanel getChartPanel() {
		LOG.debug("getchartPanel()");
		JFreeChart chart = ChartFactory.createXYLineChart(
				name, // chart title
				xLabel, // x axis label
				yLabel, // y axis label
				seriesCollection, // data
				PlotOrientation.VERTICAL,
				true, // include legend
				true, // tooltips
				false // urls		
		);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 500));
		return chartPanel; 
	}
	
	public int getSeriesCount() {
		LOG.debug("getSeriesCount()");
		return seriesCollection.getSeriesCount();
	}
	
	public XYSeries getSeries(int i) {
		LOG.debug("getSeries()");
		return seriesCollection.getSeries(i);
	}
	

}
