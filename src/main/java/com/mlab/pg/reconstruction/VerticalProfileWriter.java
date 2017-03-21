package com.mlab.pg.reconstruction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import org.apache.log4j.Logger;

import com.mlab.pg.valign.VAlignment;
import com.mlab.pg.valign.VerticalProfile;

public class VerticalProfileWriter {

	static Logger LOG = Logger.getLogger(VerticalProfileWriter.class);
	
	public VerticalProfileWriter() {
		// TODO Auto-generated constructor stub
	}

	public static int writeVerticalProfile(File file, VerticalProfile profile, String title) {
		StringBuffer cad = new StringBuffer();
		cad.append(title + "\n");
		cad.append(VAlignment.CABECERA2 + "\n");
		for(int i=0; i<profile.size(); i++) {
			cad.append(profile.get(i).toString2() + " ");
			if(profile.get(i).getPolynom2().getA2() == 0) {
				cad.append("GRADE \n");
			} else {
				cad.append("VERTICAL CURVE \n");
			}
		}
		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(cad+"\n");			
			writer.close();
		} catch (FileNotFoundException fe) {
			LOG.info("File "+file.getPath()+" not found.\n"+fe.getMessage());
			return -1;
		} catch (NumberFormatException ne) {
			LOG.info("Number format error. "+ne.getMessage());
			return -2;
		} catch (Exception e) {
			LOG.info("Unidentified error. "+e.getMessage());
			return -3;
		}
		return 1;
	}

}
