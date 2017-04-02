package com.mlab.pg;

import java.io.File;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jfree.ui.RefineryUtilities;
import org.junit.Assert;

import com.mlab.pg.graphics.Charter;
import com.mlab.pg.trackprocessor.TrackAverage;
import com.mlab.pg.trackprocessor.TrackReporter;
import com.mlab.pg.trackprocessor.TrackUtil;
import com.mlab.pg.util.IOUtil;
import com.mlab.pg.xyfunction.XYVectorFunction;
import com.mlab.pg.xyfunction.XYVectorFunctionCsvReader;
import com.mlab.pg.xyfunction.XYVectorFunctionReader;

public class M633_CalculateAxis {

	static Logger LOG = Logger.getLogger(M633_CalculateAxis.class);
	
	public M633_CalculateAxis() {

	}

	public static void main(String[] args) {
		
		PropertyConfigurator.configure("log4j.properties");

		//processTrack_1();
		processTrack_2();	
		processBarometerTrack();

	}
	
	private static void processTrack_1() {
		String path = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M-633/";
		File file = new File(path + "20140219_115202_xyz.csv");
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
		String resultname = TrackUtil.invert(path, "20140219_115202_xyz.csv", "20140219_115202_xyz_Inverted.csv",1);
		Assert.assertEquals("20140219_115202_xyz.csv", resultname);

		
		TrackAverage averager = new TrackAverage();
		File file1 = new File(path + "20140219_113213_xyz.csv");
		TrackReporter reporter = new TrackReporter(file1, 1);
		reporter.printReport();
		File file2 = new File(path + "20140219_115202_xyz_Inverted.csv");
		reporter = new TrackReporter(file2, 0);
		reporter.printReport();
		double[][] track1 = IOUtil.read(file1, ",", 1);
		double[][] track2 = IOUtil.read(file2, ",",0);
		double[][] resultTrack = averager.average(track1, track2);
		Assert.assertEquals(track1.length, resultTrack.length);
		String outfilename = path + "M633_RoadRecorder_Axis_1.csv";
		int result = IOUtil.write(outfilename, resultTrack, 12, 6, ',');
		Assert.assertEquals(1, result);
		
		File fileout = new File(outfilename);
		Assert.assertNotNull(fileout);
		Assert.assertTrue(fileout.exists());
		reporter = new TrackReporter(fileout, 0);
		reporter.printReport();		
	}

	private static void processTrack_2() {
		String path = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M-633/";
		File file = new File(path + "20140219_121352_xyz.csv");
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
		String resultname = TrackUtil.invert(path, "20140219_121352_xyz.csv", "20140219_121352_xyz_Inverted.csv", 1);
		Assert.assertEquals("20140219_121352_xyz_Inverted.csv", resultname);

		
		TrackAverage averager = new TrackAverage();
		File file1 = new File(path + "20140219_120337_xyz.csv");
		TrackReporter reporter = new TrackReporter(file1, 1);
		reporter.printReport();
		File file2 = new File(path + "20140219_121352_xyz_Inverted.csv");
		reporter = new TrackReporter(file2, 0);
		reporter.printReport();
		double[][] track1 = IOUtil.read(file1, ",", 1);
		double[][] track2 = IOUtil.read(file2, ",",0);
		double[][] resultTrack = averager.average(track1, track2);
		Assert.assertEquals(track1.length, resultTrack.length);
		String outfilename = path + "M633_RoadRecorder_Axis_2.csv";
		int result = IOUtil.write(outfilename, resultTrack, 12, 6, ',');
		Assert.assertEquals(1, result);
		
		File fileout = new File(outfilename);
		Assert.assertNotNull(fileout);
		Assert.assertTrue(fileout.exists());
		reporter = new TrackReporter(fileout, 0);
		reporter.printReport();		
	}

	private static void processBarometerTrack() {
		String path = "/home/shiguera/ownCloud/tesis/2016-2017/Datos/M-633/";
		File file = new File(path + "20140219_121352_xyzbarom.csv");
		Assert.assertTrue(file.exists());
		TrackReporter reporter = new TrackReporter(file, 1);
		reporter.printReport();
		File file2 = new File(path + "20140219_121352_xyzbarom_SZ.csv");
		Assert.assertTrue(file2.exists());
		XYVectorFunctionReader reader = new XYVectorFunctionCsvReader(file2, ',',true);
		XYVectorFunction f1 = reader.read();
		File file3 = new File(path + "M633_RoadRecorder_Axis_2_SZ.csv");
		reader = new XYVectorFunctionCsvReader(file3, ',',true);
		XYVectorFunction f2 = reader.read();

		showPressureProfile(f1);
		showAltitudeProfile(f2);
	}
	private static void showAltitudeProfile(XYVectorFunction f1) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		Charter charter = new Charter("M-633: Altitude (m) (measured with RoadRecorder)", "S", "H (m)");
        		//charter.addXYVectorFunction(f1, "Presión barométrica");
        		charter.addXYVectorFunction(f1, "Altitude");        		
        		
        		
            	JFrame frame = new JFrame("Charter");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        		frame.setContentPane(charter.getChartPanel());
        		//charter.getChart().getXYPlot().getRangeAxis().setRange(700.0, 850.0);
        		frame.pack();
        		RefineryUtilities.centerFrameOnScreen(frame);
        		frame.setVisible(true);
        		        		
            }
        });
		
	}
	private static void showPressureProfile(XYVectorFunction f1) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		Charter charter = new Charter("M-633: Pressure (MPA) (measured with RoadRecorder)", "S", "P (MPa)");
        		//charter.addXYVectorFunction(f1, "Presión barométrica");
        		charter.addXYVectorFunction(f1, "Pressure");        		
        		
        		
            	JFrame frame = new JFrame("Charter");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        		frame.setContentPane(charter.getChartPanel());
        		//charter.getChart().getXYPlot().getRangeAxis().setRange(700.0, 850.0);
        		frame.pack();
        		RefineryUtilities.centerFrameOnScreen(frame);
        		frame.setVisible(true);
        		        		
            }
        });
		
	}

}
