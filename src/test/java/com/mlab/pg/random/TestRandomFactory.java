package com.mlab.pg.random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;




public class TestRandomFactory {

	static Logger LOG = Logger.getLogger(TestRandomFactory.class);
	
	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	// Perfiles completos
	@Test
	public void testRandomVerticalProfile() {
		LOG.debug("testRandomVerticalProfile()");
		// TODO
	}

	// Perfiles básicos


	// Vertical Curves
	// Grades
		@Test
	public void testRandomSign() {
		LOG.debug("testRandomSign()");
		int countpositives = 0;
		int countnegatives = 0;
		for (int i=0; i<100;i++) {
			double sign = RandomFactory.randomSign();
			Assert.assertTrue(sign==1.0 || sign==-1.0);
			if(sign==1.0) {
				countpositives++;
			} else if (sign==-1.0) {
				countnegatives++;
			} else {
				Assert.fail();
			}
		}
		//System.out.println(countpositives + ", " + countnegatives);	
	}
	@Test
	public void testRandomDoubleByIncrements() {
		LOG.debug("testRandomDoubleByIncrements()");
		double min=0.5;
		double max = 12;
		double steep = 0.5;
		for (int i=0;i<200;i++) {
			double number = RandomFactory.randomDoubleByIncrements(min, max, steep);
			//System.out.print(number + " ");
			Assert.assertTrue(number>=min && number<=max);
		}
		//System.out.println();
	}
	@Test
	public void testRandomDoubleByIncrementsWithNegativeNumbers() {
		LOG.debug("testRandomDoubleByIncrementsWithNegativeNumbers()");
		double min = -12;
		double max = 12;
		double increment = 0.5;
		for (int i=0;i<200;i++) {
			double number = RandomFactory.randomDoubleByIncrements(min, max, increment);
			//System.out.print(number + " ");
			Assert.assertTrue(number>=min && number<=max);
		}
		//System.out.println();
	}
	@Test
	public void testRandomDoubleByIncrements_ExactMatch() {
		LOG.debug("testRandomDoubleByIncrements_ExactMatch()");
		double min= 1.0;
		double max = 2.0;
		double increment = 0.25;
		int[] contador = new int[5];
		for(int i=0; i<5; i++) {
			contador[i] = 0;
		}
		for (int i=0;i<200;i++) {
			double number = RandomFactory.randomDoubleByIncrements(min, max, increment);
			//System.out.print(number + " ");
			Assert.assertTrue(number>=min && number<=max);
			if(number == 1.0) {
				contador[0]++;
			} else if (number==1.25) {
				contador[1]++;
			} else if (number==1.5) {
				contador[2]++;
			} else if (number==1.75) {
				contador[3]++;
			} else if (number==2.0) {
				contador[4]++;
			}
		}
		int numeventos = contador[0] + contador[1] + contador[2] + contador[3] + contador[4];
		Assert.assertEquals(200, numeventos);
		for(int  i=0; i<5; i++) {
			//System.out.println(contador[i]);
		}
	}
	
	@Test
	public void testRandomDoubleByIncrements_NoExactMatch() {
		LOG.debug("testRandomDoubleByIncrementsWithNegativeNumbers()");
		double min= 1.0;
		double max = 2.2;
		double increment = 0.25;
		int[] contador = new int[6];
		for(int i=0; i<5; i++) {
			contador[i] = 0;
		}
		for (int i=0;i<200;i++) {
			double number = RandomFactory.randomDoubleByIncrements(min, max, increment);
			//System.out.print(number + " ");
			Assert.assertTrue(number>=min && number<=max);
			if(number == 1.0) {
				contador[0]++;
			} else if (number==1.25) {
				contador[1]++;
			} else if (number==1.5) {
				contador[2]++;
			} else if (number==1.75) {
				contador[3]++;
			} else if (number==2.0) {
				contador[4]++;
			} else if (number>=2.0) {
				contador[5]++;
			}
		}
		int numeventos = contador[0] + contador[1] + contador[2] + contador[3] + contador[4] + contador[5];
		Assert.assertEquals(200, numeventos);
		Assert.assertEquals(0, contador[5]);
		for(int  i=0; i<6; i++) {
			//System.out.println(contador[i]);
		}

		min= 1.0;
		max = 2.2;
		increment = 0.5;
		contador = new int[4];
		for(int i=0; i<4; i++) {
			contador[i] = 0;
		}
		for (int i=0;i<200;i++) {
			double number = RandomFactory.randomDoubleByIncrements(min, max, increment);
			//System.out.print(number + " ");
			Assert.assertTrue(number>=min && number<=max);
			if(number == 1.0) {
				contador[0]++;
			} else if (number==1.5) {
				contador[1]++;
			} else if (number==2.0) {
				contador[2]++;
			} else if (number>=2.0) {
				contador[3]++;
			}
		}
		numeventos = contador[0] + contador[1] + contador[2] + contador[3];
		Assert.assertEquals(200, numeventos);
		Assert.assertEquals(0, contador[3]);
		for(int  i=0; i<4; i++) {
			//System.out.println(contador[i]);
		}

	}

}
