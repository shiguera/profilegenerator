package com.mlab.pg.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import org.apache.log4j.Logger;


public class Ioutil {
	private static final Logger LOG = Logger.getLogger(Ioutil.class);
	/**
	 * Lee una matriz de doubles desde un fichero CSV
	 * @param file Nombre del fichero
	 * @param delimiter Delimitador de campos
	 * @return Matriz con los doubles leidos
	 */
	static public double[][] read(File file, String delimiter) {
		LOG.info("Util.read("+file.getPath()+", "+delimiter+")");
		ArrayList<double[]> arrvpoint=new ArrayList<double[]>();
		BufferedReader reader;
		String line="";	
		int numcolumns=0;
		double d[];
		try {
			reader = new BufferedReader(new FileReader(file));
			line="";
			while((line=reader.readLine()) != null) {
				String[] arr=line.split(delimiter);
				d = new double[arr.length];
				for(int i=0; i<d.length; i++) {
					d[i]=Double.parseDouble(arr[i].trim());
				}
				arrvpoint.add(d);
				numcolumns=d.length; //
			}
			reader.close();
		} catch (FileNotFoundException fe) {
			LOG.info("File "+file.getPath()+" not found.\n"+fe.getMessage());
			return null;
		} catch (NumberFormatException ne) {
			LOG.info("Number format error. "+ne.getMessage());
			return null;
		} catch (Exception e) {
			LOG.info("Unidentified error. "+e.getMessage());
			return null;
		}
		double[][] result= new double[arrvpoint.size()][numcolumns];
		return arrvpoint.toArray(result);
	}
	/**
	 *  Escribe una matriz de doubles en un fichero CSV 
	 * @param filename nombre del fichero
	 * @param d matriz de doubles
	 * @param width anchura de campo
	 * @param precission número de decimales
	 * @param separator separador de campos en el CSV
	 * @return 1 si todo va bien, negativo si hay error
	 */
	static public int write(String filename, double[][] d, 
			int width, int precission, char separator) {
		//LOG.info("write()");
		BufferedWriter writer;
		String line="";	
		try {
			writer = new BufferedWriter(new FileWriter(filename));
			for(int i=0; i< d.length; i++) {
				line="";
				for(int j=0; j<d[i].length; j++) {
					line += doubleToString(d[i][j], width, precission).trim();
					if(j != d[i].length-1) {
						line += String.valueOf(separator);
					}
				}
				//LOG.info(line);
				writer.write(line+"\n");
			}
			writer.close();
		} catch (FileNotFoundException fe) {
			LOG.info("File "+filename+" not found.\n"+fe.getMessage());
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
	/**
	 * Escribe una cadena de texto en un fichero
	 * @param filename String Nombre del fichero
	 * @param cad String Cadena de texto a escribir
	 * @return 1 si todo va bien, negativo o cero en caso contrario
	 */
	static public int write(String filename, String cad) {
		//LOG.info("write()");
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(filename));
			writer.write(cad+"\n");			
			writer.close();
		} catch (FileNotFoundException fe) {
			LOG.info("File "+filename+" not found.\n"+fe.getMessage());
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
	/**
	 * Formatea un double a los digitos y precisión deseados, 
	 * sustituyendo la coma decimal por el punto decimal.
	 * Los números se redondeán al número de decimales pedido.
	 * @param value double valor
	 * @param digits número total de dígitos
	 * @param decimals número de decimales
	 * @return cadena con el número formateado xxx.xxx
	 * Si value es NaN o infinito, o digitsmenr oigual que 0 o decimals menor que 0 
	 * arroja IllegalArgumentException
	 */
	public static String doubleToString(double value, int digits, int decimals) {
		// System.out.println(value);
		if(Double.isNaN(value) || Double.isInfinite(value) || digits <= 0 || decimals <0) {
			throw new IllegalArgumentException();
		}
		StringBuilder builder = new StringBuilder();
		builder.append("%");
		builder.append(String.format("%d", digits));;
		builder.append(".");
		builder.append(String.format("%d", decimals));;
		builder.append("f");
		
		String cad = String.format(builder.toString(), value).replace(',', '.').trim();
		return cad;
	}

}
