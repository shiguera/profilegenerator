package com.mlab.pg.util;

import java.util.List;

import com.mlab.pg.xyfunction.Polynom2;


public class MathUtil {

	
	// Error cuadratico medio
	/**
	 * Calcula el ECM entre un polinomio y una serie de puntos. Para evaluar las y delpolinomio
	 * utiliza las x de los puntos  
	 * @param poly a0 + a1x + a2 x*x
	 * @param xyvalues 
	 * @return
	 */
	public static double ecmPolynomToPoints(Polynom2 poly, double[][] xyvalues) {
		int count = xyvalues.length;
		double[] ypoly = new double[count];
		double[] ypoints = new double[count];
		for(int i=0; i<count; i++) {
			ypoly[i] = poly.getY(xyvalues[i][0]);
			ypoints[i] = xyvalues[i][1];
		}
		return ecm(ypoly, ypoints);
	}
	/**
	 * Calcula el ECM entre dos colecciones de valores y
	 * @param y1
	 * @param y2
	 * @return
	 */
	public static double ecm(double[] y1, double[] y2) {
		if(y1.length != y2.length) {
			return Double.NaN;
		}
		double ecm = 0.0;
		for(int i=0; i<y1.length; i++) {
			ecm = ecm + (y2[i]-y1[i])*(y2[i]-y1[i]);
		}
		ecm = ecm / y1.length;
		return ecm;
	}
	
	// rectas en el plano
	
	/**
	 * Recta dados un punto y una pendiente. Devuelve la recta como
	 * double[] en la forma y = double[0] + double[1]*x
	 * 
	 */
	public static double[] rectaPtoPendiente(double x1, double y1, double m) {
		double[] r = new double[]{y1 - m*x1, m};
		return r;
	}
	/**
	 * Calcula la ecuación de una recta que pasa por dos puntos
	 * @param p1 coordenadas [p1x, p1y] del primer punto de la recta
	 * @param p2 coordenadas [p2x, p2y] del segundo punto de la recta
	 * @return Recta en la forma [a0, a1] => y = a0 + a1*x. Si la recta es
	 * vertical devuelve null
	 */
	public static double[] straightByTwoPoints(double[] p1,double[] p2) {
		// Comprobar si es vertical
		double denom = p2[0] - p1[0];
		if(denom == 0.0) {
			return null;
		}
		double m = (p2[1] - p1[1]) / (p2[0] - p1[0]);
		double a0 = p1[1] - m * p1[0];
		double a1 = m;
		return new double[] {a0,a1};
	}
	
	/**
	 * Calcula el vector unitario correspondiente a una recta. 
	 * El vector tiene módulo 1 y está en el primer o cuarto cuadrante (-90 < alfa < 90)
	 * 
	 * @param r recta en la forma [a0,a1] => y=a0 + a1*x
	 * @return double[] con las componentes del vector ajustado al primer o cuarto cuadrante
	 */
	public static double[] unitVectorForStraight(double[] r) {
		// Ver si es horizontal
		if ( r[1] == 0.0) {
			return new double[] {1,0};
		}
		double u = 0.0;
		double v = 0.0;
		if(r[0] == 0.0) {
			u = 1.0;
			v = r[1];
		} else {
			u = r[0]/r[1];
			v = r[0];			
		}
		if(u<0) {
			u = -1.0*u;
			v=-1.0*u;
		}
		double mod = MathUtil.norm(new double[]{u,v});
		return new double[] {u/mod, v/mod};
	}

	/**
	 * Calcula el módulo de un vector
	 * @param v Componentes del vector
	 * @return modulo del vector = sqrt(v1*v1 + v2*v2)
	 */
	public static double norm(double[] v) {
		return Math.sqrt(v[0]*v[0] + v[1]*v[1]);
	}
	/**
	 * Calcula el producto escalar de dos vectores
	 * @param v1 componentes del primer vector
	 * @param v2 componentes del segundo vector
	 */
	public static double dot(double[] v1, double[] v2) {
		return v1[0]*v2[0] + v1[1]*v2[1];
	}
	
	/**
	 * Calcula el ángulo menor formado entre dos rectas
	 */
	public static double angleInRadiansBetweenTwoStraights(double[] r1, double[] r2) {
		double ang = 0.0;
		
		return ang;
	}
	
	public static double angleInDegreesBetweenTwoStraights(double[] r1, double[] r2) {
		double radians = MathUtil.angleInRadiansBetweenTwoStraights(r1, r2);
		return (180 / Math.PI)*radians;
	}
	
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
	 * @param values vector original
	 * @return double mínimo de la lista
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
	 * @param values vector original
	 * @return double máximo de la lista
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
	 * @param values vector de valores a promediar
	 * @return double media de los valores
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
	 * @param a Matriz original
	 * @param col índice de la columna a extraer
	 * @return array con la columna extraída
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
	 * @param x vector con los valores x
	 * @param y vector con los valores y
	 * @return double suma de los productos de xi por yi
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
	 * @param x vector con los valores x
	 * @return double suma de los cuadrados de los xi
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
	 * Suma de las potencias 'power' de los elementos de un vector.
	 * Por ejemplo, si x[]={1,2,3} y power=2, el método nos devolverá:
	 * sumOfPower()=1²+2²+3²
	 * @param x vector con los valores x
	 * @param power potencia a la que se quiere elevar los términos xi
	 * @return double suma de las potencias power de los elementos xi
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
	 * @param x vector con los valores x
	 * @param y vector con los valores y
	 * @return double suma de los cuadrados de xi multiplicados por los yi
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
	 * @param source vector original
	 * @return vector copia
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
	 * @param source matriz original
	 * @return matriz copia
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
