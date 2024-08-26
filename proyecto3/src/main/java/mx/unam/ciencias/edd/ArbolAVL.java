package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            super(elemento);
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            return altura;
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            int alturaIzquierdo = (izquierdo == null) ? -1 : izquierdo.altura();
            int alturaDerecho = (derecho == null) ? -1 : derecho.altura();
            int balance = alturaIzquierdo - alturaDerecho;
            return super.toString() + " " + altura + "/" +  balance;
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;
            return (altura == vertice.altura) && super.equals(objeto);
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        rebalancea(verticeAVL(ultimoAgregado.padre));
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        if (elemento == null)
            return;
        // Busca elemento a eliminar
        Vertice vertice = vertice(busca(elemento));
        if (vertice == null)
            return;
        elementos--;
        // Vértice con a lo más un hijo
        if (vertice.izquierdo != null && vertice.derecho != null)
            vertice = intercambiaEliminable(vertice);
        eliminaVertice(vertice);
        rebalancea(verticeAVL(vertice.padre));
    }

    /**
     * Método auxiliar que rebalancea el árbol AVL después de una eliminación o
     * inserción.
     * @param vertice el vértice AVL a partir del cual rebalanceamos.
     */
    private void rebalancea(VerticeAVL vertice) {
        if (vertice == null)
            return;
        int alturaIzquierdo = altura(vertice.izquierdo);
        int alturaDerecho = altura(vertice.derecho);
        int max = (alturaIzquierdo >= alturaDerecho) ? alturaIzquierdo : alturaDerecho;
        vertice.altura = 1 + max;
        int balance = altura(vertice.izquierdo) - altura(vertice.derecho);
        if (balance == -2) {
            VerticeAVL hijo = verticeAVL(vertice.derecho);
            VerticeAVL nieto = verticeAVL(hijo.izquierdo);
            int balanceHijo = altura(nieto) - altura(hijo.derecho);
            if (balanceHijo == 1) {
                super.giraDerecha(hijo);
                nieto.altura = vertice.altura - 1;
                hijo.altura = vertice.altura - 2;
                hijo = nieto;
                nieto = verticeAVL(hijo.izquierdo);
            }
            int hV = vertice.altura;
            int hX = altura(nieto);
            super.giraIzquierda(vertice);
            vertice.altura = (hX == hV - 2) ? hV - 1 : hV - 2;
            hijo.altura = (vertice.altura == hV - 1) ? hV : hV - 1;
        } else if (balance == 2) {
            VerticeAVL hijo = verticeAVL(vertice.izquierdo);
            VerticeAVL nieto = verticeAVL(hijo.derecho);
            int balanceHijo = altura(hijo.izquierdo) - altura(nieto);
            if (balanceHijo == -1) {
                super.giraIzquierda(hijo);
                nieto.altura = vertice.altura - 1;
                hijo.altura = vertice.altura -2;
                hijo = nieto;
                nieto = verticeAVL(hijo.derecho);
            }
            int hV = vertice.altura;
            int hY = altura(nieto);
            super.giraDerecha(vertice);
            vertice.altura = (hY == hV - 2) ? hV - 1 : hV - 2;
            hijo.altura = (vertice.altura == hV - 1) ? hV : hV - 1;
        }
        rebalancea(verticeAVL(vertice.padre));
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * VerticeAVL}). Método auxiliar para hacer esta audición en un único lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice.
     * @return el vértice recibido visto como vértice avl.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeAVL}.
     */
    private VerticeAVL verticeAVL(VerticeArbolBinario<T> vertice) {
        return (VerticeAVL) vertice;
    }

    /**
     * Método auxiliar que calcula de manera segura la altura de un vértice AVL.
     * @param vertice el vértice avl del cual queremos calcular la altura.
     * @return la altura del vértice.
     */
    private int altura(VerticeArbolBinario<T> vertice) {
        return (vertice == null) ? -1 : vertice.altura();
    }

}
