package com.mlab.pg;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jfree.data.xy.XYSeries;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mlab.pg.reconstruction.PointCharacteriserStrategy_LessSquareAproximation;
import com.mlab.pg.reconstruction.Reconstructor;
import com.mlab.pg.valign.VerticalGradeProfile;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.XYVectorFunction;
import com.mlab.pg.xyfunction.XYVectorFunctionCsvReader;



/**
 * Pruebasde reconstrucción sobre los ficheros de inventario
 * de la M-320 procedentes del MFOM y aportados por María
 * @author shiguera
 *
 */
public class TestReconstructor_M320 {
	
	static Logger LOG = Logger.getLogger(TestReconstructor_M320.class);
	
	@BeforeClass
	public static void beforeClass() {
		PropertyConfigurator.configure("log4j.properties");
		LOG.debug("beforeClass()");
	}

	
	@Test
	public void testM320() {
		LOG.debug("testM320()");
		URL url = ClassLoader.getSystemResource("N-320_xyvector.csv");
		File file = new File(url.getPath());
		Assert.assertNotNull(file);
		
		XYVectorFunctionCsvReader reader = new XYVectorFunctionCsvReader(file, ',', true);
		XYVectorFunction data = reader.read();
		Assert.assertNotNull(data);
		Reconstructor rec = null;
		try {
			rec = new Reconstructor(data, 6, 1e-5, 0.0, new PointCharacteriserStrategy_LessSquareAproximation());			
		} catch(Exception e) {
			LOG.error("testM320() ERROR: Error creating Reconstructor");
			System.exit(-1);
		}
	
		VerticalGradeProfile gradeProfile = rec.getGradeProfile();
		Assert.assertNotNull(gradeProfile);
		System.out.println(gradeProfile.toString());

		VerticalProfile profile = rec.getVerticalProfile();
		Assert.assertNotNull(profile);
		System.out.println(profile.toString());
		
		XYVectorFunction func = gradeProfile.getSample(gradeProfile.getStartS(), gradeProfile.getEndS(), 5, true);
		drawChart(func);
		
	}
	
	private void drawChart(XYVectorFunction func) {
		
		XYSeries series1 = new XYSeries("First");
		for(int i=0; i<func.size();i++) {
			series1.add(func.getX(i), func.getY(i));
		}
		Assert.assertNotNull(series1);
		Assert.assertEquals(func.size(), series1.getItemCount());
	}
}
