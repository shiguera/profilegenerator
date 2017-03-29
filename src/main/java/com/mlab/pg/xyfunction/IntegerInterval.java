package com.mlab.pg.xyfunction;

/**
 * Representa un intervalo de números naturales con inicio en 'start'
 * y final en 'end'. 'start' y 'end' Pueden tener el mismo valor, pero no
 * puede ser 'end' menor que 'start'.
 * 
 * @author shiguera
 *
 */
public class IntegerInterval {
	protected int start;
	protected int end;
	
	// Constructor
	/**
	 * Constructor a partir de dos enteros. 
	 * Si el parámetro 'start' es mayor que el parámetro 'end', el constructor 
	 * los guarda en el orden correcto haciendo this.start = end y 
	 * this.end = start
	 * Los parámetros pueden ser iguales.
	 * @param start Valor del extremo izquierdo del intervalo
	 * @param end Valor del extremo derecho del intervalo
	 */
	public IntegerInterval(int start, int end) {
		if(start <= end) {
			this.start = start;
			this.end= end;			
		} else {
			this.start = end;
			this.end = start;
		}
	}

	// Getters and Setters
	/**
	 * Getter para la propiedad entera 'start'
	 * @return valor del extremo izquierdo del intervalo
	 */
	public int getStart() {
		return start;
	}

	/** 
	 * Establece el valor del índice from si el valor pasado
	 * es menor o igual que to
	 * 
	 * @param newStart Nuevo valor para el extremo izquierdo del intervalo 
	 */
	public void setStart(int newStart) {
		if(newStart<=end) {
			this.start = newStart;			
		}
	}

	/**
	 * Getter para la propiedad entera 'end'
	 * @return Valor del extremo derecho del intervalo
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * Establece el valor del índice 'end', si el valor pasado es mayor o igual que
	 * el índice 'start' existente
	 * @param newEnd Valor de extremo derecho del intervalo
	 */
	public void setEnd(int newEnd) {
		if(newEnd >= start) {
			this.end = newEnd;
		}
	}
	
	
	/**
	 * Indica si el index está comprendido en el segmento, incluidos los extremos
	 * @param index Valor entero que se quiere comprobar si está comprendido en el intervalo
	 * @return true si index mayor o igual que start y index menor o igual que end
	 */
	public boolean contains(int index) {
		if(index >= start && index <= end) {
			return true;
		}
		return false;
	}
	
	/**
	 * Indica si el 'index' es interior al intervalo, excluidos los extremos
	 * @param index indice a evaluear
	 * @return true si index mayor que start y index menor que end
	 */
	public boolean containsInterior(int index) {
		if(index > start && index < end) {
			return true;
		}
		return false;
	}
	
	/**
	 * Indica si el segmento 'other' está contenido en este (incluidos
	 * los extremos)
	 * @param other Intervalo que se quiere comprobar si está comprendido en el intervalo
	 * @return true, false
	 */
	public boolean contains(IntegerInterval other) {
		if(contains(other.getStart()) && contains(other.getEnd())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Indica si un segmento es interior a otro
	 * @param other Intervalo que se quiere comprobar si está comprendido en el intervalo
	 * @return true, false
	 */
	public boolean containsInterior(IntegerInterval other) {
		if(containsInterior(other.getStart()) && containsInterior(other.getEnd())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Devuelve true si los intervalos tienen algún punto en común
	 * @param other Intervalo que se quiere comprobar si intersecta
	 * @return true, false
	 */
	public boolean intersects(IntegerInterval other) {
		if(contains(other.getStart()) || contains(other.getEnd()) 
				|| other.contains(start) || other.contains(end)) {
			return true;
		} 
		return false;
	}

	/**
	 * Devuelve true si la parte interior de los intervalos, esto es, 
	 * excluidos los extremos, tienen algún punto en común
	 * @param other Intervalo que se quiere evaluar
	 * @return true, false
	 */
	public boolean intersectsInterior(IntegerInterval other) {
		if(containsInterior(other.getStart()) || containsInterior(other.getEnd()) 
				|| other.containsInterior(start) || other.containsInterior(end) ||
				(other.getStart()==start && other.getEnd()==end)) {
			return true;
		} 
		return false;
	}

	/**
	 * Calcula la intersección de un segmento con otro segmento
	 * 
	 * @param other Intervalo a intersectar
	 * @return El FromToIndex intersección o nulo.
	 */
	public IntegerInterval intersection(IntegerInterval other) {
		if(!intersects(other)) {
			return null;
		}
		
		if(contains(other)) {
			return new IntegerInterval(other.getStart(), other.getEnd());
		} else if (other.contains(this)) {
			return new IntegerInterval(start, end);
		} else {
			int newstart = other.getStart();
			if(start>other.getStart()) {
				newstart = start;
			}
			int newend = other.getEnd();
			if(end<other.getEnd()) {
				newend = end;
			}
			return new IntegerInterval(newstart, newend);
		}
	}
	
	/**
	 * Devuelve el punto medio del segmento. Si el número de puntos del
	 * segmento es impar, devuelve justo el punto central. Si el 
	 * número de puntos del segmento es par, devuelve el punto que está
	 * en la mitad superior, esto es, deja más pequeña la mitad inferior.
	 * @return valor del punto medio
	 */
	public int getMiddlePoint() {
		int length = getEnd() - getStart() +1;
		if(length==1) {
			return getStart();
		} else {
			return getStart() + length/2;
		}
	}
	@Override
	public boolean equals(Object other) {
		if (!other.getClass().isAssignableFrom(IntegerInterval.class)) {
			return false;
		}
		IntegerInterval i = (IntegerInterval)other;
		return (i.getStart()==start && i.getEnd()==end);
	}

	/**
	 * Devuelve el número de puntos del segmento
	 * @return número depuntos del segmento
	 */
	public int size() {
		return (getEnd() - getStart() + 1);
	}
	
	@Override
	public String toString() {
		return start+", "+end;
	}
}
