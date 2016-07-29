package com.mlab.pg.random;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestStdRandom {
	static Logger LOG = Logger.getLogger(TestStdRandom.class);

	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}

	@Test
	public void test() {
		int n = 10;
		StdRandom.setSeed(new Date().getTime());
		double[] probabilities = { 0.5, 0.3, 0.1, 0.1 };
		int[] frequencies = { 5, 3, 1, 1 };
		String[] a = "A B C D E F G".split(" ");

		System.out.println("seed = " + StdRandom.getSeed());
		for (int i = 0; i < n; i++) {
			System.out.printf("%2d ", StdRandom.uniform(100));
			System.out.printf("%8.5f ", StdRandom.uniform(10.0, 99.0));
			System.out.printf("%5b ", StdRandom.bernoulli(0.5));
			System.out.printf("%7.5f ", StdRandom.gaussian(9.0, 0.2));
			System.out.printf("%1d ", StdRandom.discrete(probabilities));
			System.out.printf("%1d ", StdRandom.discrete(frequencies));
			StdRandom.shuffle(a);
			for (String s : a)
				System.out.print(s);
			System.out.println();
		}
	}
}
