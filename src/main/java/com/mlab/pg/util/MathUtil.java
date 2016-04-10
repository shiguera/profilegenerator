package com.mlab.pg.util;

import java.util.List;


public class MathUtil {
	/**
	 * Devuelve true si un numero entero es par
	 * @param i número entero a evaluar
	 * @return true si el número entero es par
	 */
	public static boolean isEven(int i) {
		return (i % 2) == 0;
	}
	/**
	 * Calcula el valor mínimo de una lista de Doubles. Si la
	 * lista es nula o de tamaño cero devuelve NaN
	 * 
	 * @param values
	 * @return
	 */
	static public double min(List<Double> values) {
		if(values==null || values.size()==0) {
			return Double.NaN;
		}
		double min = values.get(0);
		for(Double d: values) {
			if (d < min) {
				min =d;
			}
		}
		return min;
	}
	/**
	 * Calcula el valor mánimo de una lista de Doubles. Si la
	 * lista es nula o de tamaño cero devuelve NaN
	 * 
	 * @param values
	 * @return
	 */
	static public double max(List<Double> values) {
		if(values==null || values.size()==0) {
			return Double.NaN;
		}
		double max = values.get(0);
		for(Double d: values) {
			if (d > max) {
				max =d;
			}
		}
		return max;
	}

	// Aproximaciones mínimos cuadrados

	/**
	 * Calcula la recta que mejor se aproxima por mínimos cuadrados
	 * a los puntos dados.
	 * @param xy double[][] coordenadas de los puntos
	 * @return double[] coeficientes a,b de la recta y= a+ bx
	 */
	static public double[] rectaMinimosCuadrados(double[][] xy) {
		int n= xy.length;
		double a[][]= new double[2][2];
		double c[] = new double[2];
		double x[] = new double[n];
		x=copy(column(xy,0));
		double y[] = new double[n];
		y=copy(column(xy,1));
		
		a[0][0]=n;
		a[0][1]=sum(x);
		a[1][0]=a[0][1];
		a[1][1]=sumOfSquares(x);
		c[0]=sum(y);
		c[1]=sumOfProducts(x,y);
		
		double[] result= new double[2];
		result=solve(a,c);
		return result;
	}
	/**
	 * Calcula la parábola que mejor se aproxima por mínimos cuadrados
	 * a los puntos dados.
	 * @param xy double[][] coordenadas de los puntos
	 * @return double[] con los coef. a,b,c de la ecuación de la parabola y= a+bx+cx^2
	 */
	static public double[] parabolaMinimosCuadrados(double[][] xy) {
		//Galib.log("Galib.parabolaMinimosCuadrados()");
		//Galib.disp(xy, 12, 6, true);
		int n= xy.length;
		double a[][]= new double[3][3];
		double c[] = new double[3];
		double x[] = new double[3];
		x=copy(column(xy,0));
		double y[] = new double[n];
		y=copy(column(xy,1));

		a[0][0]=n;
		a[0][1]=sum(x);
		a[0][2]=sumOfSquares(x);		
		a[1][0]=a[0][1];
		a[1][1]=a[0][2];
		a[1][2]=sumOfPower(x,3);
		a[2][0]=a[0][2];
		a[2][1]=a[1][2];
		a[2][2]=sumOfPower(x,4);

		c[0]=sum(y);
		c[1]=sumOfProducts(x,y);
		c[2]=sumOfX2Y(x,y);

		double[] result=new double[3];
		result=solve(a,c);
		return result;
	}
	
	// Agregados
	/**
	 * Calcula la media de una serie de valores
	 * @param values
	 * @return
	 */
	static public double average(double[] values) {
		int size = values.length;
		double sum = 0.0;
		for(double value: values) {
			sum = sum + value;
		}
		return sum / (double)size;
	}
	/**
	 * Extrae como vector una columna de una matriz
	 * @param a
	 * @param col
	 * @return
	 */
	static public double[] column(double[][] a, int col) {
		double[] column = new double[a.length];
		for(int i=0; i<a.length; i++) {
			column[i]=a[i][col];
		}
		return column;
	}
	static public double sum(double[] x) {
		double suma=0.0;
		for(int i=0; i<x.length; i++) {
			suma+=x[i];
		}
		return suma;
	}
	/**
	 * Suma de los productos de x*y
	 * @param x
	 * @param y
	 * @return
	 */
	static public double sumOfProducts(double[] x, double[] y) {
		double suma = 0.0;
		for(int i=0; i< x.length; i++) {
			suma+=x[i]*y[i];
		}
		return suma;
	}
	/**
	 * Suma de los cuadrados de x
	 * @param x
	 * @return
	 */
	static public double sumOfSquares(double[] x) {
		double suma=0.0;
		for(int i=0; i<x.length; i++) {
			suma+=x[i]*x[i];
		}
		return suma;
	}

	static public double[] solve(double a[][], double c[]) {
        // Resuelve un sistema lineal de ecuaciones
		int n = a.length, i, j, k;
        double suma;
        double[][] aa=copy(a);
        double[] cc=copy(c);
        double[] x= new double[n];
        
        for (k = 0; k <= n - 2; k++) {
            for (i = k + 1; i <= (n - 1); i++) {
                cc[i] -= aa[i][k] * cc[k] / aa[k][k];
                for (j = n - 1; j >= k; j--)
                    aa[i][j] -= aa[i][k] * aa[k][j] / aa[k][k];
            }
        }
        for (k = n - 1; k >= 0; k--) {
            suma = 0;
            for (j = k + 1; j < n; j++)
                suma += aa[k][j] * x[j];
            x[k] = (cc[k] - suma) / aa[k][k];
        }
        return x;
    }
	/**
	 * Suma de las potencias 'power' de los elementos de un vector.<br/>
	 * Por ejemplo, si x[]={1,2,3} y power=2, el método nos devolverá:<br>
	 * sumOfPower()=1²+2²+3²
	 * @param x
	 * @param power
	 * @return
	 */
	static public double sumOfPower(double[] x, int power) {
		double suma = 0.0;
		for(int i=0; i< x.length; i++) {
			double prod=1.0;
			for(int j=0; j<power; j++) {
				prod=prod*x[i];
			}
			suma+=prod;
		}
		return suma;
	}
	/**
	 * Suma de los cuadrados de x multiplicados por y
	 * @param x
	 * @param y
	 * @return
	 */
	static public double sumOfX2Y(double[] x, double[] y) {
		double suma = 0.0;
		for(int i=0; i< x.length; i++) {
			suma+=x[i]*x[i]*y[i];
		}
		return suma;
	}
	/**
	 * Copia un vector
	 * @param source
	 * @return
	 */
	static public double[] copy(double[] source) {
		double[] dest= new double[source.length];
		for(int i=0; i<source.length; i++) {
			dest[i]=source[i];
		}
		return dest;
	}
	/**
	 * Copia una matriz
	 * @param source
	 * @return
	 */
	static public double[][] copy(double[][] source) {
		double[][] dest= new double[source.length][source[0].length];
		for(int i=0; i<source.length; i++) {
			for(int j=0; j<source[0].length; j++) {
				dest[i][j]=source[i][j];
			}
		}
		return dest;
	}


}
