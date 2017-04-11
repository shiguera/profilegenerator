package com.mlab.pg;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;

import com.mlab.pg.graphics.FunctionDisplayer;
import com.mlab.pg.util.IOUtil;
import com.mlab.pg.valign.GradeAlignment;
import com.mlab.pg.valign.VAlignment;
import com.mlab.pg.valign.VerticalCurveAlignment;
import com.mlab.pg.valign.VerticalProfile;
import com.mlab.pg.xyfunction.Parabole;
import com.mlab.pg.xyfunction.Straight;
import com.mlab.pg.xyfunction.XYVectorFunction;

public class PruebaProyecto {

	static Logger LOG = Logger.getLogger(PruebaProyecto.class);
	static VerticalProfile vprofile;
	
	public PruebaProyecto() {
		String path ="/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/Proyecto";
		String filename = "AlignmentM607_modificado.csv";
		File file = new File(path, filename);
		Assert.assertTrue(file.exists());
		double[][] d = IOUtil.read(file, ",", 1);
		LOG.debug("Filas  leidas: " + d.length);
		
		vprofile = createProfile(d);
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		LOG.debug("PruebaProyecto.main()");
		String path ="/home/shiguera/ownCloud/tesis/2016-2017/Datos/EnsayosTesis/M607/Proyecto";
		String filename = "AlignmentM607_modificado.csv";
		File file = new File(path, filename);
		Assert.assertTrue(file.exists());
		double[][] d = IOUtil.read(file, ",", 1);
		LOG.debug("Filas  leidas: " + d.length);
		
		VerticalProfile vprofile = createProfile(d);
		showProfile(vprofile);
		
		String outfilename = IOUtil.composeFileName(path, "M607_Proyecto_SZ.csv");
		IOUtil.write(outfilename, getVProfilePoints().getValuesAsArray(), 12,6,',');
	}
	
	private static VerticalProfile createProfile(double[][] d) {
		vprofile = new VerticalProfile();
		double z0 = d[0][1];
		for(int i=0; i<d.length; i++) {
			double se = 27072 + d[i][0];
			double ze = d[i][1];
			double ge = d[i][2];
			double l = d[i][3];
			double kv = d[i][4];
			double ss = 27072 + d[i][5];
			double zs = d[i][6];
			double gs = d[i][7];
			Assert.assertEquals(ss, se+l, 0.001);
			VAlignment align = null;
			if(kv==0.0) {
				//System.out.println("Grade");
				Assert.assertEquals(zs, z0+ge*l, 0.001);
				Straight r = new Straight(new double[]{se,ze}, new double[]{ss,zs});
				align = new GradeAlignment(r, se, ss);
			} else {
				//System.out.println("VC");		
				Parabole p = new Parabole(se, ze, ge,kv );
				Assert.assertEquals(zs, p.getY(ss), 0.001);
				Assert.assertEquals(gs, p.getTangent(ss), 0.001);
				align  = new VerticalCurveAlignment(p, se, ss); 
			}
			vprofile.add(align);
			z0 = zs;
		}
		System.out.println("VProfile alignments: " + vprofile.size());
		return vprofile;
	}
	
	private static void showProfile(VerticalProfile vprofile) {
		XYVectorFunction f = getVProfilePoints();
		FunctionDisplayer fd = new FunctionDisplayer();
		fd.showFunction(f, "Prueba", "Proyecto", "s", "z");
	}

	public static VerticalProfile getVprofile() {
		return vprofile;
	}

	public static XYVectorFunction getVProfilePoints() {
		double starts = vprofile.getStartS();
		double ends = vprofile.getEndS();
		XYVectorFunction f = vprofile.getSample(starts, ends, 1.0, true);
		return f;
	}
	

}
