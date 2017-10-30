package com.mlab.pg.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.swing.JFrame;

import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.ui.RefineryUtilities;

import com.mlab.pg.xyfunction.XYVectorFunction;

public class FunctionDisplayer {

	float strokeWidth1=1.0f, strokeWidth2 = 2.0f;
	Paint seriesPaint1 = Color.BLACK;
	Paint seriesPaint2 = Color.black;
	Paint rangeAxisFontColor = Color.BLACK;
	
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
        		charter.getChart().getXYPlot().getRangeAxis().setRange(getMinY(data), getMaxY(data));
        		
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
	    		charter.addXYVectorFunction(data2, series2Title);
	    		charter.addXYVectorFunction(data1, series1Title);
	    		
	        	JFrame frame = new JFrame("Charter");
	            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    		frame.setContentPane(charter.getChartPanel());
        		charter.getChart().getXYPlot().getRangeAxis().setRange(getMinY(data1, data2), getMaxY(data1, data2));
	    		
        		setBackgroundColor(charter);
        		setAxisFont(charter);
        		
        		setSeriesStroke(charter);
        		setDashedLineInSeries(charter);
	    		setAxisDecimalFormat(charter);
	    		
	    		frame.setPreferredSize(new Dimension(1400, 750));
	    		frame.pack();
	    		RefineryUtilities.centerFrameOnScreen(frame);
	    		frame.setVisible(true);	        		
	        }
		});
	}	
	private void setDashedLineInSeries(Charter charter) {
		charter.getChart().getXYPlot().getRenderer().setSeriesStroke(
	            1, 
	            new BasicStroke(
	                1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 
	                1.0f, new float[] {2.0f, 2.0f}, 0.0f
	            )
	        );
	}
	private void setAxisFont(Charter charter) {
	    Axis axis = charter.getChart().getXYPlot().getRangeAxis();
		axis.setLabelPaint(rangeAxisFontColor);
	    axis.setTickLabelPaint(rangeAxisFontColor);
	    Axis axis2 = charter.getChart().getXYPlot().getDomainAxis();
		axis2.setLabelPaint(rangeAxisFontColor);
	    axis2.setTickLabelPaint(rangeAxisFontColor);
	    Font font = new Font("Dialog", Font.PLAIN, 12);
        axis2.setTickLabelFont(font);
	}
	private void setSeriesStroke(Charter charter) {
		charter.getChart().getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(strokeWidth2));
		charter.getChart().getXYPlot().getRenderer().setSeriesPaint(0, seriesPaint2);
		
		charter.getChart().getXYPlot().getRenderer().setSeriesStroke(1, new BasicStroke(strokeWidth1));
		charter.getChart().getXYPlot().getRenderer().setSeriesPaint(1, seriesPaint1);

	}
	private void setBackgroundColor(Charter charter) {
		charter.getChart().getXYPlot().setBackgroundPaint(Color.white);
	}
	private void setAxisDecimalFormat(Charter charter) {
		NumberAxis na1 = (NumberAxis)charter.getChart().getXYPlot().getRangeAxis();
		NumberAxis na2 = (NumberAxis)charter.getChart().getXYPlot().getDomainAxis();
        DecimalFormat df = new DecimalFormat("#0");
        DecimalFormatSymbols custom=new DecimalFormatSymbols();
        custom.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(custom);
        na1.setNumberFormatOverride(df);
        DecimalFormat df2 = new DecimalFormat("#0");
        df2.setDecimalFormatSymbols(custom);
        na2.setNumberFormatOverride(df2);
	}

	private double getMinY(XYVectorFunction data) {
		// TODO Falta resolver bien cuando ymin==0
		double ymin = data.getMinY();
		if(ymin>0) {
			return ymin*0.97;
		} else {
			return ymin + 0.03*ymin;
		} 
	}
	private double getMaxY(XYVectorFunction data) {
		// TODO Falta resolver bien cuando ymax==0
		double ymax = data.getMaxY();
		if(ymax>0) {
			return 1.03*ymax;
		} else {
			return ymax - 0.03*ymax;
		}
	}
	private double getMinY(XYVectorFunction data1, XYVectorFunction data2) {
		// TODO Falta resolver bien cuando ymin==0
		double ymin = data1.getMinY();
		double y2min = data2.getMinY();
		if (y2min < ymin) {
			ymin = y2min;
		}
		if(ymin>0) {
			return ymin*0.97;
		} else {
			return ymin + 0.03*ymin;
		}
	}
	private double getMaxY(XYVectorFunction data1, XYVectorFunction data2) {
		// TODO Falta resolver bien cuando ymax==0
		double ymax = data1.getMaxY();
		double y2max = data2.getMaxY();
		if (y2max > ymax) {
			ymax = y2max;
		}
		if(ymax>0) {
			return 1.03*ymax;
		} else {
			return ymax - 0.03*ymax;
		}
	}

}
