package com.mlab.pg.reconstruction;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

public class TestSegmentation {
	
private static Logger LOG = Logger.getLogger(TestSegmentation.class);
	
	@BeforeClass
	public static void beforeClass() {
		PropertyConfigurator.configure("log4j.properties");	
	}
	

	@Test
	public void test() {
		LOG.debug("test()");
		PointTypeArray array = new PointTypeArray();
		for(int i=0; i<1049; i++) {
			array.add(PointType.GRADE);
		}
		for(int i=1049; i<1052; i++) {
			array.add(PointType.BORDER_POINT);
		}
		for(int i=1052; i<1550; i++) {
			array.add(PointType.VERTICAL_CURVE);
		}
		array.add(PointType.BORDER_POINT);
		for(int i=1551; i<2649; i++) {
			array.add(PointType.VERTICAL_CURVE);
		}
		for(int i=2649; i<2652; i++) {
			array.add(PointType.BORDER_POINT);
		}
		for(int i=2652; i<3400; i++) {
			array.add(PointType.GRADE);
		}
		Segmentation typearray = new Segmentation(array);
		Assert.assertNotNull(typearray);
		//System.out.println(typearray);
	}
}
