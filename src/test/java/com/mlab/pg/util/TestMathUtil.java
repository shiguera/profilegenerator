package com.mlab.pg.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestMathUtil {

	private static final Logger LOG = Logger.getLogger(TestMathUtil.class);

	
	@BeforeClass
	public static void beforeClass() {
		PropertyConfigurator.configure("log4j.properties");
		LOG.debug("beforeClass()");
	}

	@Test
	public void testEcm() {
		LOG.debug("testEcm()");
		double[] y1 = new double[]{1.0, 2.0};
		double[] y2 = new double[]{2.0};
		Assert.assertEquals(Double.NaN, MathUtil.ecm(y1, y2), 0.001);
		
		y1 = new double[]{1.0};
		y2 = new double[]{2.0};
		Assert.assertEquals(1.0, MathUtil.ecm(y1, y2), 0.001);
		
		y1 = new double[]{1.0, 2.0};
		y2 = new double[]{2.0, 3.0};
		Assert.assertEquals(1.0, MathUtil.ecm(y1, y2), 0.001);
		
		y1 = new double[]{1.0, 2.0};
		y2 = new double[]{2.0, 3.0};
		Assert.assertEquals(1.0, MathUtil.ecm(y1, y2), 0.001);

		y1 = new double[]{1.0, 2.0, 3.0};
		y2 = new double[]{2.0, 3.0, 4.0};
		Assert.assertEquals(1.0, MathUtil.ecm(y1, y2), 0.001);
	}
	
	@Test
	public void testNorm() {
		LOG.debug("testNorm()");
		double[] v = new double[]{0,0};
		Assert.assertEquals(0.0, MathUtil.norm(v),0.001);
		v = new double[]{1,0};
		Assert.assertEquals(1.0, MathUtil.norm(v),0.001);
		v = new double[]{1,1};
		Assert.assertEquals(Math.sqrt(2), MathUtil.norm(v),0.001);
		v = new double[]{-2,-2};
		Assert.assertEquals(Math.sqrt(8), MathUtil.norm(v),0.001);
		v = new double[]{2,-3};
		Assert.assertEquals(Math.sqrt(13), MathUtil.norm(v),0.001);
	}
	
	@Test
	public void testUnitVectorForStraight() {
		LOG.debug("testUnitVectorForStraight()");
		double[] p1 = new double[] {0,0};
		double[] p2 = new double[] {1,1};
		double[] r = MathUtil.rectaPorDosPuntos(p1, p2);
		double[] v = MathUtil.unitVectorForStraight(r);
		Assert.assertEquals(Math.sqrt(2)/2, v[0], 0.001);
		Assert.assertEquals(Math.sqrt(2)/2, v[1], 0.001);
		
		p1 = new double[] {0,0};
		p2 = new double[] {1,-1};
		r = MathUtil.rectaPorDosPuntos(p1, p2);
		v = MathUtil.unitVectorForStraight(r);
		Assert.assertEquals(Math.sqrt(2)/2, v[0], 0.001);
		Assert.assertEquals(-Math.sqrt(2)/2, v[1], 0.001);
		
		p1 = new double[] {0,0};
		p2 = new double[] {-1,1};
		r = MathUtil.rectaPorDosPuntos(p1, p2);
		v = MathUtil.unitVectorForStraight(r);
		Assert.assertEquals(Math.sqrt(2)/2, v[0], 0.001);
		Assert.assertEquals(-Math.sqrt(2)/2, v[1], 0.001);
		
		p1 = new double[] {0,0};
		p2 = new double[] {-1,-1};
		r = MathUtil.rectaPorDosPuntos(p1, p2);
		v = MathUtil.unitVectorForStraight(r);
		Assert.assertEquals(Math.sqrt(2)/2, v[0], 0.001);
		Assert.assertEquals(Math.sqrt(2)/2, v[1], 0.001);
		
		p1 = new double[] {1,1};
		p2 = new double[] {2,2};
		r = MathUtil.rectaPorDosPuntos(p1, p2);
		v = MathUtil.unitVectorForStraight(r);
		Assert.assertEquals(Math.sqrt(2)/2, v[0], 0.001);
		Assert.assertEquals(Math.sqrt(2)/2, v[1], 0.001);
		
		p1 = new double[] {1,1};
		p2 = new double[] {2,0};
		r = MathUtil.rectaPorDosPuntos(p1, p2);
		v = MathUtil.unitVectorForStraight(r);
		Assert.assertEquals(Math.sqrt(2)/2, v[0], 0.001);
		Assert.assertEquals(-Math.sqrt(2)/2, v[1], 0.001);
		

		
		
	}
	@Test
	public void testRectaPorDosPuntos() {
		LOG.debug("testRectaPorDosPuntos()");
		// Recta por el origen a 45 grados
		double[] p1 = new double[] {0,0};
		double[] p2 = new double[] {1,1};
		double[] r = MathUtil.rectaPorDosPuntos(p1, p2);
		Assert.assertEquals(0.0, r[0], 0.001);
		Assert.assertEquals(1.0, r[1], 0.001);
		
		// Recta horizontal por el origen
		p1 = new double[] {0,0};
		p2 = new double[] {1,0};
		r = MathUtil.rectaPorDosPuntos(p1, p2);
		Assert.assertEquals(0.0, r[0], 0.001);
		Assert.assertEquals(0.0, r[1], 0.001);

		// Recta horizontal por otro punto
		p1 = new double[] {0,-3};
		p2 = new double[] {1,-3};
		r = MathUtil.rectaPorDosPuntos(p1, p2);
		Assert.assertEquals(-3.0, r[0], 0.001);
		Assert.assertEquals(0.0, r[1], 0.001);

		// Recta vertical por el origen
		p1 = new double[] {0,0};
		p2 = new double[] {0,1};
		r = MathUtil.rectaPorDosPuntos(p1, p2);
		Assert.assertNull(r);
		
		// Recta vertical por otro punto
		p1 = new double[] {-3,0};
		p2 = new double[] {-3,3};
		r = MathUtil.rectaPorDosPuntos(p1, p2);
		Assert.assertNull(r);
		
		// Recta inclinada, ángulo primer cuadrante
		p1 = new double[] {1,0};
		p2 = new double[] {2,0.5};
		r = MathUtil.rectaPorDosPuntos(p1, p2);
		Assert.assertEquals(-0.5, r[0], 0.001);
		Assert.assertEquals(0.5, r[1], 0.001);

		// Recta inclinada, ángulo segundo cuadrante
		p1 = new double[] {-1,0};
		p2 = new double[] {-2,0.5};
		r = MathUtil.rectaPorDosPuntos(p1, p2);
		Assert.assertEquals(-0.5, r[0], 0.001);
		Assert.assertEquals(-0.5, r[1], 0.001);

		// Recta inclinada, ángulo tercer cuadrante
		p1 = new double[] {1,0};
		p2 = new double[] {0,-0.5};
		r = MathUtil.rectaPorDosPuntos(p1, p2);
		Assert.assertEquals(-0.5, r[0], 0.001);
		Assert.assertEquals(0.5, r[1], 0.001);

		// Recta inclinada, ángulo cuarto cuadrante
		p1 = new double[] {1,0};
		p2 = new double[] {2,-0.5};
		r = MathUtil.rectaPorDosPuntos(p1, p2);
		Assert.assertEquals(0.5, r[0], 0.001);
		Assert.assertEquals(-0.5, r[1], 0.001);
	}
	
	@Test
	public void testAngleBetweenStraightsWhenZeroAngle() {
		LOG.debug("testAngleBetweenStraightsWhenZeroAngle()");
		
	
	}	
	
	@Test
	public void testIsEven() {
		LOG.debug("testIsEven()");
		int i = 1;
		Assert.assertFalse(MathUtil.isEven(i));
		i = -1;
		Assert.assertFalse(MathUtil.isEven(i));
		i = 0;
		Assert.assertTrue(MathUtil.isEven(i));
		i = 2;
		Assert.assertTrue(MathUtil.isEven(i));
		i = 100;
		Assert.assertTrue(MathUtil.isEven(i));
		i = -102;
		Assert.assertTrue(MathUtil.isEven(i));

	}


	@Test
	public void testSolve() {
		LOG.debug("testSolve()");
		double[][] A = new double[][]{{1,-1},{2,-3}};
		double[] C = new double[]{0, -1};
		double[] x = MathUtil.solve(A, C);
		Assert.assertEquals(x[0], 1.0,1e-6);
		Assert.assertEquals(x[1], 1.0,1e-6);
		A = new double[][] {{0.750702, 0.044623, 0.602134},
				{0.322266, 0.402230, 0.496250},
				{0.257659, 0.576478, 0.440777}};
		C = new double[]{0.39128, 0.87051, 0.91372};
		x = MathUtil.solve(A, C);
		Assert.assertEquals(x[0], -1.26126,1e-5);
		Assert.assertEquals(x[1], 0.47656,1e-5);
		Assert.assertEquals(x[2], 2.18697,1e-5);
	}
	
	@Test
	public void testAreaBajoRecta() {
		LOG.debug("testAreaBajoRecta()");
		double[] r= new double[]{1,1};
		double x1 = 2;
		double x2 = 3;
		double s = MathUtil.areaBajoRecta(r, x1, x2);
		Assert.assertEquals(3.5, s, 0.0001);
		
		r= new double[]{-1,-1};
		x1 = 2;
		x2 = 3;
		s = MathUtil.areaBajoRecta(r, x1, x2);
		Assert.assertEquals(-3.5, s, 0.0001);

		r= new double[]{1,-1};
		x1 = 2;
		x2 = 3;
		s = MathUtil.areaBajoRecta(r, x1, x2);
		Assert.assertEquals(-1.5, s, 0.0001);
		

		r= new double[]{3,-2};
		x1 = 0;
		x2 = 5;
		s = MathUtil.areaBajoRecta(r, x1, x2);
		Assert.assertEquals(-10, s, 0.0001);
		
	}
}
