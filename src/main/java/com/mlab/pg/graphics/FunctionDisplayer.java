package com.mlab.pg.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;

import javax.swing.JFrame;

import org.jfree.ui.RefineryUtilities;

import com.mlab.pg.xyfunction.XYVectorFunction;

public class FunctionDisplayer {

	float strokeWidth1=2.0f, strokeWidth2 = 2.0f;
	Paint seriesPaint1 = Color.BLUE;
	Paint seriesPaint2 = Color.RED;
	
	public FunctionDisplayer() {

	}

	public void showFunction(XYVectorFunction data, String graphTitle, String seriesTitle, String xlabel, String ylabel) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		Charter charter = new Charter(graphTitle, xlabel, ylabel);
        		charter.addXYVectorFunction(data, seriesTitle);
        		
            	JFrame frame = new JFrame("graphTitle");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        		frame.setContentPane(charter.getChartPanel());
        		charter.getChart().getXYPlot().getRangeAxis().setRange(data.getMinY(), data.getMaxY());
        		
        		charter.getChart().getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(strokeWidth1));
	    		charter.getChart().getXYPlot().getRenderer().setSeriesPaint(0, seriesPaint1);
	    		
	    		frame.setPreferredSize(new Dimension(1400, 750));
        		frame.pack();
        		RefineryUtilities.centerFrameOnScreen(frame);
        		frame.setVisible(true);        		        		
            }
        });		
	}
	public void showTwoFunctions(XYVectorFunction data1, XYVectorFunction data2, String graphTitle, String series1Title, String series2Title, String xlabel, String ylabel) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	    		Charter charter = new Charter(graphTitle, xlabel, ylabel);
	    		charter.addXYVectorFunction(data1, series1Title);
	    		charter.addXYVectorFunction(data2, series2Title);
	    		
	        	JFrame frame = new JFrame("Charter");
	            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    		frame.setContentPane(charter.getChartPanel());
        		charter.getChart().getXYPlot().getRangeAxis().setRange(getMinY(data1, data2), getMaxY(data1, data2));
	    		
        		charter.getChart().getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(strokeWidth1));
	    		charter.getChart().getXYPlot().getRenderer().setSeriesPaint(0, seriesPaint1);
	    		
	    		charter.getChart().getXYPlot().getRenderer().setSeriesStroke(1, new BasicStroke(strokeWidth2));
	    		charter.getChart().getXYPlot().getRenderer().setSeriesPaint(1, seriesPaint2);

	    		frame.setPreferredSize(new Dimension(1400, 750));
	    		frame.pack();
	    		RefineryUtilities.centerFrameOnScreen(frame);
	    		frame.setVisible(true);	        		
	        }
		});
	}
	private double getMinY(XYVectorFunction data1, XYVectorFunction data2) {
		double ymin = data1.getMinY();
		double y2min = data2.getMinY();
		if (y2min < ymin) {
			ymin = y2min;
		}
		return ymin;
	}
	private double getMaxY(XYVectorFunction data1, XYVectorFunction data2) {
		double ymax = data1.getMaxY();
		double y2max = data2.getMaxY();
		if (y2max > ymax) {
			ymax = y2max;
		}
		return ymax;
	}

}
