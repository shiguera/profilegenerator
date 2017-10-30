package com.mlab.pg.graphics;

import java.awt.Font;
import java.io.File;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;

import com.mlab.pg.xyfunction.XYVectorFunction;

public class Charter {
	
	private static Logger LOG = Logger.getLogger(Charter.class);
	
	//private String DEFAULT_DIRECTORY = "/home/shiguera/ownCloud/tesis/2016-2017/TesisMain/img/reconstruccion";
	private String DEFAULT_DIRECTORY = "/home/shiguera/Dropbox/Tesis_SHiguera/Tesis/Articles/ReconstruccionVertical/CACAIE/VersionJulio2017/Latex";
	
	XYSeriesCollection seriesCollection;
	String name, xLabel, yLabel;
	JFreeChart chart =null;
	
	public Charter(String name, String xlabel, String ylabel) {
		//LOG.debug("Charter()");
		this.name = name;
		this.xLabel = xlabel;
		this.yLabel = ylabel;
		seriesCollection = new XYSeriesCollection();
		
		
		
	}
	
	public void addXYVectorFunction(XYVectorFunction func, String name) {
		//LOG.debug("addXYVectorFunction()");
		XYSeries series = new XYSeries(name);
		for(int i=0; i<func.size(); i++) {
			series.add(func.getX(i), func.getY(i));
		}
		seriesCollection.addSeries(series);
	}
	
	public ChartPanel getChartPanel() {
		//LOG.debug("getchartPanel()");
		chart = ChartFactory.createXYLineChart(
				name, // chart title
				xLabel, // x axis label
				yLabel, // y axis label
				seriesCollection, // data
				PlotOrientation.VERTICAL,
				false, // include legend
				true, // tooltips
				false // urls		
		);
		
		Font font = new Font("Dialog", Font.PLAIN, 15);
        chart.getXYPlot().getDomainAxis().setTickLabelFont(font);
        chart.getXYPlot().getRangeAxis().setTickLabelFont(font);

        LegendTitle legend = new LegendTitle(chart.getXYPlot().getRenderer());
        Font font3 = new Font("Dialog", Font.PLAIN, 20); 
        legend.setItemFont(font3); 
        legend.setPosition(RectangleEdge.BOTTOM); 
        chart.addLegend(legend); 


		//XYPlot plot = chart.getXYPlot();
		
		// Cuando activo estas dos siguientes líneas, parece no hacer nada
		// El elemento ToolTipGenerator por defecto funciona igual
		// XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		// renderer.setLegendItemToolTipGenerator(new StandardXYSeriesLabelGenerator("Legend {0}, {1}, {2}"));
		
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 500));
		//chartPanel.setDefaultDirectoryForSaveAs(new File(DEFAULT_DIRECTORY));
		chartPanel.setDefaultDirectoryForSaveAs(new File(DEFAULT_DIRECTORY));
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
	
	public JFreeChart getChart() {
		return chart;
	}

	
}
