package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la cola.
     * @return una representación en cadena de la cola.
     */
    @Override public String toString() {
		Nodo n = cabeza;
		StringBuffer sb = new StringBuffer();
		while (n != null) {
			sb.append(n.elemento + ",");
			n = n.siguiente;
		}
		return sb.toString();
    }

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
		if (elemento == null)
			throw new IllegalArgumentException("Elemento inválido.");
		Nodo n = new Nodo(elemento);
		if (cabeza == null)
			cabeza = rabo = n;
		else {
			rabo.siguiente = n;
			rabo = n;
		}
    }
}
