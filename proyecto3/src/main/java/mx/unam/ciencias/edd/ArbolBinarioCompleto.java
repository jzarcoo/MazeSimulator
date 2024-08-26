package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        private Iterador() {
            cola = new Cola<Vertice>();
            if (raiz != null)
                cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
	        return !cola.esVacia();
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            Vertice v = cola.saca();
            if (v.izquierdo != null)
                cola.mete(v.izquierdo);
            if (v.derecho != null)
                cola.mete(v.derecho);
            return v.elemento;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null)
            throw new IllegalArgumentException("Elemento inválido.");
        elementos++;
        Vertice nuevo = nuevoVertice(elemento);
        if (raiz == null) {
            raiz = nuevo;
            return;
        }
        Vertice v = getPadreDelNuevo(raiz);
        nuevo.padre = v;
        if (v.izquierdo != null)
            v.derecho = nuevo;
        else
            v.izquierdo = nuevo;
    }

    /**
     * Reconstruye la ruta de la raíz al padre del nuevo vértice en tiempo O(logn).
     * @param v el vértice raíz por donde comenzamos.
     */
    private Vertice getPadreDelNuevo(Vertice v){
        int nivelAnterior = altura() - 1;
        for (int i = nivelAnterior; i > 0; i--) {
            int digito = elementos >> i;
            int bin = digito & 1;
            v = (bin == 0) ? v.izquierdo : v.derecho;
        }
        return v;
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        if (elemento == null || raiz == null)
            return;
        Vertice verticeAEliminar = null;
        Vertice ultimo = raiz;
        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(raiz);
        while (!cola.esVacia()) {
            Vertice v = cola.saca();
            ultimo = v;
            if (v.elemento.equals(elemento))
                verticeAEliminar = v;
            if (v.izquierdo != null)
                cola.mete(v.izquierdo);
            if (v.derecho != null)
                cola.mete(v.derecho);
        }
        desconecta(verticeAEliminar, ultimo);
    }

    /**
     * Desconecta un vértice del árbol.
     * @parm verticeAEliminar el vértice a desconectar.
     * @param ultimo el ultimo vértice del árbol, para intercambiar de ser necesario.
     */
    private void desconecta(Vertice verticeAEliminar, Vertice ultimo) {
        if (verticeAEliminar == null)
            return;
        elementos--;
        if (elementos == 0) {
            raiz = null;
            return;
        }
        verticeAEliminar.elemento = ultimo.elemento;
        if (ultimo.padre.izquierdo == ultimo)
            ultimo.padre.izquierdo = null;
        else
            ultimo.padre.derecho = null;
    }


    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        int n = elementos;
        int h = -1;
        while (n > 0) {
            n >>= 1;
            h++;
        }
        return h;
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        if (raiz == null)
            return;
        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(raiz);
        while (!cola.esVacia()) {
            Vertice v = cola.saca();
            accion.actua(v);
            if (v.izquierdo != null)
                cola.mete(v.izquierdo);
            if (v.derecho != null)
                cola.mete(v.derecho);
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
