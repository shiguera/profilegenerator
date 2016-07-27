package com.mlab.pg.norma;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.util.MathUtil;

import junit.framework.Assert;

public class TestNormaUtil {

	private static Logger LOG = Logger.getLogger(TestNormaUtil.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}

	@Test
	public void testRectaMinCuadradosCoefRozamiento() {
		LOG.debug("testRectaMinCuadradosCoefRozamiento()");
		double[][] xy = new double[][] {
			{40.0, .432},
			{50.0, .411},
			{60.0, .390},
			{70.0, .369},
			{80.0, .348},
			{90.0, .334},
			{100.0, .320},
			{110.0, .306},
			{120.0, .291},
			{130.0, .277},
			{140.0, .263}
		};
		double[] r = MathUtil.rectaMinimosCuadrados(xy);
		for(int i=0; i<xy.length; i++) {
			double yy = Math.rint((r[0] + r[1]*xy[i][0])*1000.0) / 1000.0;
			System.out.println(xy[i][0] + " " + xy[i][1] + " " + yy + " " + (yy-xy[i][1]));			
		}
		System.out.println("--------------------------------------------------");
		double[] p = MathUtil.parabolaMinimosCuadrados(xy);
		for(int i=0; i<xy.length; i++) {
			double yy = Math.rint((p[0] + p[1]*xy[i][0] + p[2]*xy[i][0]*xy[i][0])*1000.0) / 1000.0;
			System.out.println(xy[i][0] + " " + xy[i][1] + " " + yy + " " + (yy-xy[i][1]));			
		}
		System.out.println(p[0] + " " + p[1] + " " + p[2]);
	}
	
	@Test
	public void testCoefRozamiento() {
		LOG.debug("testCoefRozamiento()");
		double fl = NormaUtil.coefRozamiento(40);
		Assert.assertEquals(.432, fl, 0.005);
		fl = NormaUtil.coefRozamiento(50);
		Assert.assertEquals(.411, fl, 0.005);
		fl = NormaUtil.coefRozamiento(60);
		Assert.assertEquals(.390, fl, 0.005);
		fl = NormaUtil.coefRozamiento(70);
		Assert.assertEquals(.369, fl, 0.005);
		fl = NormaUtil.coefRozamiento(80);
		Assert.assertEquals(.348, fl, 0.005);
		fl = NormaUtil.coefRozamiento(90);
		Assert.assertEquals(.334, fl, 0.005);
		fl = NormaUtil.coefRozamiento(100);
		Assert.assertEquals(.320, fl, 0.005);
		fl = NormaUtil.coefRozamiento(110);
		Assert.assertEquals(.306, fl, 0.005);
		fl = NormaUtil.coefRozamiento(120);
		Assert.assertEquals(.291, fl, 0.005);
		fl = NormaUtil.coefRozamiento(130);
		Assert.assertEquals(.277, fl, 0.005);
		fl = NormaUtil.coefRozamiento(140);
		Assert.assertEquals(.263, fl, 0.005);	
	}
	
	@Test
	public void testInterpolacionDistanciaInicioProhibicionAdelantamiento() {
		LOG.debug("testInterpolacionDistanciaInicioProhibicionAdelantamiento()");
		double[][] xy = new double[][] {
			{40.0, 50.0},
			{50.0, 75.0},
			{60.0, 100.0},
			{70.0, 130.0},
			{80.0, 165.0},
			{90.0, 205.0},
			{100.0, 250.0}
		};
		double[] r = MathUtil.rectaMinimosCuadrados(xy);
		for(int i=0; i<xy.length; i++) {
			double yy = Math.rint((r[0] + r[1]*xy[i][0])*1000.0) / 1000.0;
			System.out.println(xy[i][0] + " " + xy[i][1] + " " + yy + " " + (yy-xy[i][1]));			
		}
		System.out.println("--------------------------------------------------");
		double[] p = MathUtil.parabolaMinimosCuadrados(xy);
		for(int i=0; i<xy.length; i++) {
			double yy = Math.rint((p[0] + p[1]*xy[i][0] + p[2]*xy[i][0]*xy[i][0])*1000.0) / 1000.0;
			System.out.println(xy[i][0] + " " + xy[i][1] + " " + yy + " " + (yy-xy[i][1]));			
		}
		System.out.println(p[0] + " " + p[1] + " " + p[2]);
	}
	@Test
	public void testDistanciaInicioProhibicionAdelantamiento() {
		LOG.debug("testDistanciaInicioProhibicionAdelantamiento()");
		double maxerror = 1.8;
		double fl = NormaUtil.distanciaInicioProhibicionAdelantamiento(40);
		Assert.assertEquals(50.0, fl, maxerror);
		fl = NormaUtil.distanciaInicioProhibicionAdelantamiento(50);
		Assert.assertEquals(75.0, fl, maxerror);
		fl = NormaUtil.distanciaInicioProhibicionAdelantamiento(60);
		Assert.assertEquals(100.0, fl, maxerror);
		fl = NormaUtil.distanciaInicioProhibicionAdelantamiento(70);
		Assert.assertEquals(130.0, fl, maxerror);
		fl = NormaUtil.distanciaInicioProhibicionAdelantamiento(80);
		Assert.assertEquals(165.0, fl, maxerror);
		fl = NormaUtil.distanciaInicioProhibicionAdelantamiento(90);
		Assert.assertEquals(205.0, fl, maxerror);
		fl = NormaUtil.distanciaInicioProhibicionAdelantamiento(100);
		Assert.assertEquals(250.0, fl, maxerror);
	}

	@Test
	public void testInterpolacionDistanciaFinProhibicionAdelantamiento() {
		LOG.debug("testInterpolacionDistanciaFinProhibicionAdelantamiento()");
		double[][] xy = new double[][] {
			{40.0, 150.0},
			{50.0, 180.0},
			{60.0, 220.0},
			{70.0, 260.0},
			{80.0, 300.0},
			{90.0, 340.0},
			{100.0, 400.0}
		};
		double[] r = MathUtil.rectaMinimosCuadrados(xy);
		for(int i=0; i<xy.length; i++) {
			double yy = Math.rint((r[0] + r[1]*xy[i][0])*1000.0) / 1000.0;
			System.out.println(xy[i][0] + " " + xy[i][1] + " " + yy + " " + (yy-xy[i][1]));			
		}
		System.out.println("--------------------------------------------------");
		double[] p = MathUtil.parabolaMinimosCuadrados(xy);
		for(int i=0; i<xy.length; i++) {
			double yy = Math.rint((p[0] + p[1]*xy[i][0] + p[2]*xy[i][0]*xy[i][0])*1000.0) / 1000.0;
			System.out.println(xy[i][0] + " " + xy[i][1] + " " + yy + " " + (yy-xy[i][1]));			
		}
		System.out.println(p[0] + " " + p[1] + " " + p[2]);
	}

	@Test
	public void testDistanciaFinProhibicionAdelantamiento() {
		LOG.debug("testDistanciaFinProhibicionAdelantamiento()");
		double maxerror = 6.5;
		double fl = NormaUtil.distanciaFinProhibicionAdelantamiento(40);
		Assert.assertEquals(150.0, fl, maxerror);
		fl = NormaUtil.distanciaFinProhibicionAdelantamiento(50);
		Assert.assertEquals(180.0, fl, maxerror);
		fl = NormaUtil.distanciaFinProhibicionAdelantamiento(60);
		Assert.assertEquals(220.0, fl, maxerror);
		fl = NormaUtil.distanciaFinProhibicionAdelantamiento(70);
		Assert.assertEquals(260.0, fl, maxerror);
		fl = NormaUtil.distanciaFinProhibicionAdelantamiento(80);
		Assert.assertEquals(300.0, fl, maxerror);
		fl = NormaUtil.distanciaFinProhibicionAdelantamiento(90);
		Assert.assertEquals(340.0, fl, maxerror);
		fl = NormaUtil.distanciaFinProhibicionAdelantamiento(100);
		Assert.assertEquals(400.0, fl, maxerror);
	}

}
