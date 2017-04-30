package com.mlab.pg.xyfunction;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mlab.pg.util.MathUtil;

/**
 * Extiende XYVector en el sentido de que las parejas de doubles (x,y)
 * representan los valores (x,y) de una determinada función en un intervalo.
 * Se sobrescribe el método add() de forma que cada valor x debe ser mayor 
 * que el anterior.
 * En la extracción de sublistas, el extremo derecho es inclusivo, al contrario
 * de lo que ocurre con XYVector y ArrayList
 * 
 * @author shiguera
 *
 */
public class XYVectorFunction extends XYVector implements XYFunction, InInterval{

	private static final long serialVersionUID = 1L;
	private static Logger LOG = Logger.getLogger(XYVectorFunction.class);
	private double separacionMedia = Double.NaN;
	private double maxY = Double.NaN;
	private double minY = Double.NaN;
	private double meanY = Double.NaN;
	/**
	 * Crea una XYVectorFunction vacía
	 */
	public XYVectorFunction() {
		super();
	}
	/**
	 * Crea una XYVectorFunction a partir de un array de valores {xi, yi}
	 * @param values
	 */
	public XYVectorFunction(List<double[]> values) {
		super(values);
	}

	@Override
	public boolean add(double[] e) {
		if(size()>0 && e[0] <= getX(size()-1)) {
			return false;
		} else {
			minY = Double.NaN;
			maxY = Double.NaN;
			meanY = Double.NaN;
			return super.add(e);			
		}
	}
	
	public boolean containsX(double x) {
		return (size()>0 && x >= getStartX() && x <= getEndX());
	}
	/**
	 * Devuelve el índice que corresponde a una determinada
	 * abscisa x, o el índice inmediatamente anterior
	 * @param x Abscisa de la que se busca el índice
	 * @return Índice correspondiente a la abscisa x, o el 
	 * índice inmediatamente anterior
	 */
	public int previousIndex(double x) {
		if(containsX(x)) {
			if(getStartX()==x) {
				return 0;
			}
			for(int i=1;i<size();i++) {
				if(getX(i)>x) {
					return i-1;
				}
			}
			if(getEndX()==x) {
				return size()-1;
			}
		} 
		return -1;
	}
	/**
	 * Devuelve el indice que corresponde a una determinada abscisa 
	 * o el inmediatamente posterior
	 * @param x Abscisa de la que se busca el índice
	 * @return Índice correspondiente a la abscisa x 
	 * o el inmediatamente posterior
	 */
	public int followingIndex(double x) {
		if(containsX(x)) {
			for(int i=0;i<size();i++) {
				if(getX(i)>=x) {
					return i;
				}
			}
		} 
		return -1;		
	}

	/**
	 * Devuelve el índice del punto más próximo a una determinada abscisa
	 * @param x Abscisa del punto
	 * @return indice cuya abscisa es la más proxima a x
	 */
	public int getNearestIndex(double x) {
		if(x<getX(0)) {
			return 0;
		}
		if(x>getX(size()-1)) {
			return size()-1;
		}
		int previous = previousIndex(x);
		double previousx = getX(previous);
		int following = followingIndex(x);
		double followingx = getX(following);
		if(Math.abs(x - previousx) >= Math.abs(followingx-x)) {
			return following;
		} else {
			return previous;
		}
		
	}
 	public boolean containsInterval(IntegerInterval interval) {
		if(interval.getStart()>=0 && interval.getStart()<size() && 
				interval.getEnd()>=0 && interval.getEnd()<size()) {
			return true;
		}
		return false;
	}
	public List<double[]> getValues(IntegerInterval interval) {
		if(!this.containsInterval(interval)) {
			LOG.error("Error: sample doesn't contain interval "+ interval.toString());
			return null;
		}
		List<double[]> l = new ArrayList<double[]>();
		for(int i=interval.getStart(); i<=interval.getEnd(); i++) {
			l.add(get(i));
		}
		return l;
	}

	public double[][] getValuesAsArray() {
		int start = 0;
		int end = size()-1;
		IntegerInterval interval = new IntegerInterval(start, end);
		return getValuesAsArray(interval);
	}
	public double[][] getValuesAsArray(IntegerInterval interval) {
		if(!this.containsInterval(interval)) {
			return null;
		}
		int size = interval.getEnd() - interval.getStart() +1;
		double[][] d = new double[size][2];
		List<double[]> l = getValues(interval);
		for(int i=0; i<size;i++) {
			d[i] = l.get(i);
		}
		return d;
	}
	
	/**
	 * Extrae una XYFunction compuesta por los valores 
	 * comprendidos desde fromIndex inclusive hasta toIndex inclusivo.
	 * La clase padre XYVector, al igual que ArrayList, no incluye el
	 * extremo derecho en las sublistas. Esta clase sí que lo hace
	 */
	@Override
	public XYVectorFunction subList(int fromIndex, int toIndex) {
		if(!contains(fromIndex) || !contains(toIndex) || (fromIndex>toIndex)) {
			return new XYVectorFunction();
		}
		XYVectorFunction sublist = new XYVectorFunction(super.subList(fromIndex, toIndex));
		// Añado el extremo derecho del intervalo
		sublist.add(get(toIndex));
		return sublist;
	}
	
	/**
	 * Extrae una XYFunction de un subintervalo del original
	 * comprendida entre el índice anterior a x1 y el posterior a x2.
	 * Si x1 es menor que startX(), se coge desde startX().
	 * Si x2 es mayor que endX(), se coge hasta endX().
	 * @param x1 Valor de la x del extremo izquierdo del intervalo
	 * @param x2 Valor de la x del extremo derecho del intervalo
	 * @return XYVectorFunction comprendida en el intervalo
	 */
	public XYVectorFunction extract_old(double x1, double x2) {
		if(x1>x2 || x1>getEndX() || x2<getStartX()) {
			return new XYVectorFunction();
		}
		int i1 = 0;
		if (x1>getStartX()) {
			i1 = previousIndex(x1);	
		}
		int i2 = size()-1;
		if (x2 < getEndX()) {
			i2 = followingIndex(x2);
		}
		XYVectorFunction sublist = subList(i1,i2);
		return sublist;
	}
	public XYVectorFunction extract(double x1, double x2) {
		if(x1>x2 || x1>getEndX() || x2<getStartX()) {
			return new XYVectorFunction();
		}
		int i1 = getNearestIndex(x1);
		int i2 = getNearestIndex(x2);
		XYVectorFunction sublist = subList(i1,i2);
		return sublist;
	}
	
	public double getX(int index) {
		if(!contains(index)) {
			return Double.NaN;
		}
		return get(index)[0];
	}
	// Interface InInterval
	@Override
	public double getStartX() {
		if(size()>0) {
			return get(0)[0];
		} else {
			return Double.NaN;			
		}
	}

	public double[] getFirst() {
		return get(0);
	}
	
	@Override
	public double getEndX() {
		if(size()>0) {
			return get(size()-1)[0];
		} else {
			return Double.NaN;			
		}
	}

	public double[] getLast() {
		return new double[]{getEndX(), getEndY()};
	}
	public double getY(int index) {
		if(!contains(index)) {
			return Double.NaN;
		}
		return get(index)[1];
	}
	// Interface XYFunction
	/**
	 * Devuelve el valor de Y para un valor de x comprendido
	 * en el intervalo. Si la x coincide con una de las de la 
	 * serie devuelve el valor exacto correspondiente. Si la x
	 * es intermedia entre dos de la serie devuelve la 
	 * interpolación lineal de las Y del punto anterior y
	 * el siguiente
	 * @param x valor de la abscisa
	 * @return valor de la ordenada
	 */
	@Override
	public double getY(double x) {
		if(containsX(x)) {
			int previous = previousIndex(x);
			int following =followingIndex(x);
			if(previous==following) {
				return get(previous)[1];
			} else {
				// Punto intermedio: Aproximación lineal
				double x1 = get(previous)[0];
				double x2 = get(following)[0];
				double y1 = get(previous)[1];
				double y2 = get(following)[1];
				double tg_alfa = (y2-y1) / (x2-x1);
				double y = y1 + (x-x1)*tg_alfa;
				return y;
			}
		} else {
			return Double.NaN;			
		}
	}

	public double getEndY() {
		if(size()>0) {
			return getY(size()-1);
		} else {
			return Double.NaN;			
		}
	}
	/**
	 * Calcula la tangente mediante:
	 * tangent = (y(i+1) - y(i))/(x(i+1)-x(i))
	 * Si el punto es intermedio a un segmento dentro de la serie,
	 * la tangente es la misma que la del punto anterior
	 * Si el punto coincide con el último de la serie, 
	 * la tangente tambien es la misma que la del punto anterior 
	 *  @param x valor de la abscisa
	 *  @return valor de la tangente
	 */
	@Override
	public double getTangent(double x) {
		if(size()==1) {
			return Double.NaN;
		}
		if(containsX(x)) {
			int previous = previousIndex(x);
			if(previous==size()-1) {
				previous = size()-2;
			}
			int following = followingIndex(x);
			if(previous==following) {
				following=following+1;
			}
			double x1 = getX(previous);
			double y1 = getY(previous);
			double x2 = getX(following);
			double y2 = getY(following);
			double incx = x2 - x1;
			double incy = y2 - y1;
			return incy/incx;
		}
		return Double.NaN;
	}

	/**
	 * TODO: No implementado
	 */
	@Override
	public double getCurvature(double x) {
		// TODO Auto-generated method stub
		return Double.NaN;
	}
	
	// Aproximaciones mínimos cuadrados
	/**
	 * Calcula la recta que mejor ajusta por mínimos cuadrados los puntos de la
	 * XYVectorFunction 'function' comprendidos en el intervalo [start, end], incluyendo
	 * los extremos.
	 * 
	 * @param start Índice del primer punto de la XYSample que se quiere incluir en
	 * la recta ajustada
	 * @param end Índice del último punto de la XYVectorFunction que se quiere incluir en 
	 * la recta ajustada
	 * 
	 * @return Coeficientes [a0, a1] de la recta que mejor ajusta por mínimos cuadrados a los 
	 * puntos del intervalo en la forma y = a0 + a1x
	 */
	public double[] rectaMinimosCuadrados(int start, int end) {
		IntegerInterval interval = new IntegerInterval(start, end);
		return rectaMinimosCuadrados(interval);
	}
	public double[] rectaMinimosCuadrados(IntegerInterval interval) {
		if(!containsInterval(interval)) {
			return null;
		}
		double[][] xy = getValuesAsArray(interval);
		return MathUtil.rectaMinimosCuadrados(xy);
	}

	/**
	 * Parábola por mínimos cuadrados
	 * @param start índice inicial
	 * @param end índice final
	 * @return vector con los coeficientes 
	 */
	public double[] parabolaMinimosCuadrados(int start, int end) {
		IntegerInterval interval = new IntegerInterval(start, end);
		return parabolaMinimosCuadrados(interval);
	}
	public double[] parabolaMinimosCuadrados(IntegerInterval interval) {
		if(!containsInterval(interval)) {
			return null;
		}
		double[][] xy = getValuesAsArray(interval);
		return MathUtil.parabolaMinimosCuadrados(xy);
	}

	/**
	 * Calcula el área encerrada entre los puntos y el eje X según la foŕmula del trapecio
	 * @param i1 Indice del extremo izquierdo del intervalo
	 * @param i2 Indice del extremo derecho del intervalo
	 * @return Area encerrada
	 */
	public double areaEncerrada(int i1, int i2) {
		double sumaarea= 0.0;
		for (int i= i1; i<i2; i++) {
			double x1 = getX(i);
			double y1 = getY(i);
			double x2 = getX(i+1);
			double y2 = getY(i+1);
			double area = 0.5*(y1+y2)*(x2-x1);
			sumaarea = sumaarea + area;
		}
		return sumaarea;
	}
	/**
	 * Calcula el area encerrada entre dos abscisas según la fórmula del trapecio
	 * @param startx abscisa inicial
	 * @param endx abscisa final
	 * @return Area encerrada
	 */
	public double areaEncerrada(double startx, double endx) {
		int left = getNearestIndex(startx);
		int right = getNearestIndex(endx);
		return areaEncerrada(left, right);
	}
	
	/**
	 * Recta que pasa por (s2,z2) y tiene igual area encerrada
	 * que los puntos entre i1 y i2
	 * @param i1
	 * @param i2
	 * @return
	 */
	public double[] rectaAnteriorEqualArea(int i1, int i2) {
		double area = areaEncerrada(i1, i2);
		double s1 = getX(i1);
		double s2 = getX(i2);
		double z2 = getY(i2);
		double[][] A = new double[][] {{(s2 - s1), (s1*s2 - s1*s1)}, {1, s2}};
		double[] C = new double[] {2*area - z2 * (s2 - s1), z2};
		double[] r = MathUtil.solve(A, C);
		return r;
	}

	/**
	 * Recta que pasa por (s1,z1) y tiene igual area encerrada
	 * que los puntos entre i1 y i2
	 * @param i1
	 * @param i2
	 * @return
	 */
	public double[] rectaPosteriorEqualArea(int i1, int i2) {
		double area = areaEncerrada(i1, i2);
		double s1 = getX(i1);
		double z1 = getY(i1);
		double s2 = getX(i2);
		double[][] A = new double[][] {{(s2 - s1), (s2*s2 - s1*s2)}, {1, s1}};
		double[] C = new double[] {2*area - z1 * (s2 - s1), z1};
		double[] r = MathUtil.solve(A, C);
		return r;
	}

	public double[] rectaHorizontalEqualArea(int i1, int i2) {
		double area = areaEncerrada(i1, i2);
		double s1 = getX(i1);
		double s2 = getX(i2);
		double g1 = area/(s2-s1);
		double[] r = new double[]{g1, 0.0};
		return r;
	}
	public double separacionMedia() {
		if(Double.isNaN(separacionMedia)) {
			double suma = 0.0;
			for (int i=1; i<size(); i++) {
				double sep = getX(i) - getX(i-1);
				suma = suma + sep;
			}
			separacionMedia = suma/(size()-1);
		}
		return separacionMedia;
	}
	
	public double ecm(XYVectorFunction other) {
		double ecm = 0.0;
		for(int i=0; i<size(); i++) {
			double x1 = getX(i);
			double y1 = getY(i);
			double y2 = other.getY(x1);
			if(Double.isNaN(y2)) {
				continue;
			}
			ecm = ecm + (y2-y1)*(y2-y1);
		}
		ecm = ecm / size();
		return ecm;
	}
	public double errorAbsolutoMedio(XYVectorFunction other) {
		double error = 0.0;
		for(int i=0; i<size(); i++) {
			double x1 = getX(i);
			double y1 = getY(i);
			double y2 = other.getY(x1);
			if(Double.isNaN(y2)) {
				continue;
			}
			error = error + Math.abs(y2-y1);
		}
		error = error / size();
		return error;
	}
	public XYVectorFunction integrate(double startY) {
		if(size()<2) {
			return null;
		}
		XYVectorFunction result = new XYVectorFunction();
		double previousY = startY;
		result.add(new double[]{getX(0), startY});
		for(int i=1; i<size()-1; i++) {
			double y1 = getY(i);
			double y2 = getY(i+1);
			double s = getX(i+1) - getX(i);
			double area = 0.5 * (y1 + y2) * s;
			double integral = previousY + area;
			result.add(new double[]{getX(i), integral});
			previousY = integral;
		}
		return result;
	}
	
	@Override
	public XYVectorFunction clone() {
		IntegerInterval interval = new IntegerInterval(0, this.size()-1);
		return new XYVectorFunction(this.getValues(interval));
	}

	public double getMaxY() {
		if (Double.isNaN(maxY) && size()>0) {
			maxY = getY(0);
			for(int i=1; i<size(); i++) {
				if(getY(i)>maxY) {
					maxY = getY(i);
				}
			}
		}
		return maxY;
	}
	public double getMinY() {
		if(Double.isNaN(minY) && size()>0) {
			minY = getY(0);
			for(int i=1; i<size(); i++) {
				if(getY(i)<minY) {
					minY = getY(i);
				}
			}
		}
		return minY;			
	}

	public double getMeanY() {
		if(Double.isNaN(meanY) && size()>0) {
			double suma = getY(0);
			for(int i=0; i<size(); i++) {
				suma += getY(i);
			}
			meanY=suma/size();
		}
		return meanY;
	}
	
	public int nearestInFunction2(int indexInFunction1, XYVectorFunction f2) {
		double[][] track1 = getValuesAsArray();
		double[][] track2 = f2.getValuesAsArray();
		double x1 = track1[indexInFunction1][0];
		double y1 = track1[indexInFunction1][1];
		double d = Math.sqrt((x1-track2[0][0])*(x1-track2[0][0]) + (y1-track2[0][1])*(y1-track2[0][1]));
		double dmin = d;
		int indexMin = 0;
		for(int i=1; i<track2.length; i++) {	
			d = Math.sqrt((x1-track2[i][0])*(x1-track2[i][0]) + (y1-track2[i][1])*(y1-track2[i][1]));
			if(d<dmin) {
				dmin = d;
				indexMin = i;
			}
		}
		return indexMin;
	}
}
