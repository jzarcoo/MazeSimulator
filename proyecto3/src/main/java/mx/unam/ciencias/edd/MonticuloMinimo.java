package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends Comparable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return indice < elementos;
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if(indice >= elementos)
                throw new NoSuchElementException("No hay elemento siguiente.");
            return arbol[indice++];
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Los índices. */
    private Diccionario<T, Integer> indices;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new Comparable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        arbol = nuevoArreglo(100);
        indices = new Diccionario<T, Integer>();
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        arbol = nuevoArreglo(n);
        indices = new Diccionario<T, Integer>();
        for(T e : iterable) {
            arbol[elementos] = e;
            indices.agrega(e, elementos++);
        }
        for (int i = (n >> 1) - 1; i >= 0; i--)
            acomodaAbajo(i);
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        if(elementos == arbol.length) {
            T[] nuevo = nuevoArreglo(elementos << 1);
            for(int i = 0; i < elementos; i++)
                nuevo[i] = arbol[i];
            arbol = nuevo;
        }
        arbol[elementos] = elemento;
        indices.agrega(elemento, elementos);
        acomodaArriba(elementos++);
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        if(elementos == 0)
            throw new IllegalStateException("El montículo es vacío.");
        return eliminaElemento(0);
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        if(!contiene(elemento)) return;
        int i = indices.get(elemento);
        if(i < 0 || i >= elementos) return;
        eliminaElemento(i);
    }

    /* Elimina el elemento en el índice. */
    private T eliminaElemento(int indice) {
        T e = arbol[indice];
        intercambia(indice, --elementos);
        indices.agrega(arbol[elementos], -1);
        reordena(arbol[indice]);
        return e;
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        if(!indices.contiene(elemento)) return false;
        int i = indices.get(elemento);
        if (i < 0 || i >= elementos) return false;
        return arbol[i].equals(elemento);
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <code>true</code> si ya no hay elementos en el montículo,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean esVacia() {
        return elementos == 0;
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        elementos = 0;
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        if(!contiene(elemento)) return;
        int i = indices.get(elemento);
        acomodaArriba(i);
        acomodaAbajo(i);
    }

    /**
     * Acomoda el montículo hacia arriba.
     * Se llama a este método si el valor del elemento del vértice fue decrementado.
     * @param i el índice del vértice que se va a acomodar.
     */
    private void acomodaArriba(int i) {
        if(i == 0) return;
        int p = (i - 1) >> 1;
        if(arbol[i].compareTo(arbol[p]) < 0) {
            intercambia(i, p);
            acomodaArriba(p);
        }
    }

    /* Acomoda hacia abajo (min-heapify); ve si el nodo actual (i) es mayor que
     * alguno de sus hijos (2*i+1, 2*i+2). Si así es, reemplaza el nodo con el
     * hijo menor, y recursivamente hace lo mismo con el hijo menor (que tiene
     * el valor del que era su padre). */
    private void acomodaAbajo(int i) {
        if(i >= elementos) return;
        int min = i;
        int izq = 2*i + 1;
        int der = 2*i + 2;
        if (izq < elementos && arbol[izq].compareTo(arbol[min]) < 0)
            min = izq;
        if (der < elementos && arbol[der].compareTo(arbol[min]) < 0)
            min = der;
        if (min != i) {
            intercambia(i, min);
            acomodaAbajo(min);
        }
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        return elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if(i < 0 || i >= elementos)
            throw new NoSuchElementException("Indíce inválido.");
        return arbol[i];
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < elementos; i++)
            sb.append(arbol[i] + ", ");
        return sb.toString();
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;
        if(elementos != monticulo.elementos) return false;
        for(int i = 0; i < elementos; i++)
            if(!arbol[i].equals(monticulo.arbol[i]))
                return false;
        return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {
        MonticuloMinimo<T> monticulo = new MonticuloMinimo<T>(coleccion);
        Lista<T> ordenada = new Lista<T>();
        while(!monticulo.esVacia())
            ordenada.agrega(monticulo.elimina());
        return ordenada;
    }

    /**
     * Intercambia los elementos en los índices i y j.
     * @param i índice del primer elemento.
     * @param j índice del segundo elemento.
     */
    private void intercambia(int i, int j) {
        if(i == j) return;
        T aux = arbol[i];
        arbol[i] = arbol[j];
        arbol[j] = aux;
        indices.agrega(arbol[i], i);
        indices.agrega(arbol[j], j);
    }
}
