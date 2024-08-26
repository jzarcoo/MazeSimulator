package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
            color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        @Override public String toString() {
	        return ((color == Color.ROJO) ? "R" : "N") + "{" + super.toString() + "}";
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            return color == vertice.color && super.equals(objeto);
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) { super(coleccion); }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
	    return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        return verticeRojinegro(vertice).color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        VerticeRojinegro vertice = verticeRojinegro(ultimoAgregado);
        vertice.color = Color.ROJO;
        rebalanceaAgregar(vertice);
    }

    /**
     * Rebalancea el árbol rojinegro después de agregar un elemento.
     * @param vertice el vértice rojinegro siempre es de color rojo, distinto de null.
     */
    private void rebalanceaAgregar(VerticeRojinegro verticeRojinegro) {
        VerticeRojinegro vertice = verticeRojinegro;
        boolean esBalanceado = false;
        while(!esBalanceado) {   
            // Caso 1
            if (!vertice.hayPadre()) {
                vertice.color = Color.NEGRO;
                esBalanceado = true; // sale del while
                continue;
            }
            // Caso 2
            VerticeRojinegro padre = padre(vertice);
            if (esNegro(padre)) {
                esBalanceado = true; // sale del while
                continue;
            }
            // Caso 3
            VerticeRojinegro abuelo = abuelo(vertice);
            VerticeRojinegro tio = tio(vertice);
            if (esRojo(tio)) {
                tio.color = padre.color = Color.NEGRO;
                abuelo.color = Color.ROJO;
                vertice = abuelo; // recursión
                continue;
            }
            // Caso 4
            if (sonCruzados(vertice, padre)) {
                giraEnSuDireccion(padre);
                // Actualiza referencias
                VerticeRojinegro v = vertice;
                vertice = padre;
                padre = v;
            }
            // Caso 5
            padre.color = Color.NEGRO;
            abuelo.color = Color.ROJO;
            giraEnDireccionContraria(abuelo, vertice);
            esBalanceado = true; // sale del while
        }
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Busca elemento a eliminar
        VerticeRojinegro vertice = verticeRojinegro(busca(elemento));
        if (vertice == null)
            return;
        elementos--;
        // Intercambia
        if (vertice.izquierdo != null && vertice.derecho != null)
            vertice = verticeRojinegro(intercambiaEliminable(vertice));
        // Crea fantasma
        VerticeRojinegro fantasma = null;
        if (vertice.izquierdo == null && vertice.derecho == null) {
            vertice.izquierdo = fantasma = verticeRojinegro(nuevoVertice(null));
            fantasma.padre = vertice;
        }
        VerticeRojinegro hijo = hijo(vertice);
        eliminaVertice(vertice);
        if (esRojo(hijo))
            hijo.color = Color.NEGRO;
        else if(esNegro(vertice))
            rebalanceaEliminar(hijo);
        if (fantasma != null)
            eliminaVertice(fantasma);
    }

    /**
     * Rebalancea el árbol rojinegro después de eliminar un elemento.
     * @param vertice el vértice rojinegro siempre es de color negro.
     */
    private void rebalanceaEliminar(VerticeRojinegro verticeRojinegro) {
        VerticeRojinegro vertice = verticeRojinegro;
        boolean esBalanceado = false;
        while(!esBalanceado) {
            // Caso 1
            if (!vertice.hayPadre()) {
                esBalanceado = true; // sale del while
                continue;
            }
            // Caso 2
            VerticeRojinegro hermano = hermano(vertice);
            VerticeRojinegro padre = padre(vertice);
            if (esRojo(hermano)) {
                padre.color = Color.ROJO;
                hermano.color = Color.NEGRO;
                giraEnMismaDireccion(padre, vertice);
                // Actualiza hermano
                hermano = hermano(vertice);
            }
            // Caso 3	
            VerticeRojinegro izquierdo = izquierdo(hermano);
            VerticeRojinegro derecho = derecho(hermano);
            if (esNegro(padre) && esNegro(hermano) && esNegro(izquierdo) && esNegro(derecho)) {
                hermano.color = Color.ROJO;
                vertice = padre; // recursión
                continue;
            }
            // Caso 4
            if (esRojo(padre) && esNegro(hermano) && esNegro(izquierdo) && esNegro(derecho)) {
                hermano.color = Color.ROJO;
                padre.color = Color.NEGRO;
                esBalanceado = true; // sale del while
                continue;
            }
            // Caso 5
            if ((esIzquierdo(vertice) && esRojo(izquierdo) && esNegro(derecho)) ||
                (esDerecho(vertice) && esRojo(derecho) && esNegro(izquierdo))) {
                VerticeRojinegro hijoRojo = esRojo(izquierdo) ? izquierdo : derecho;
                hijoRojo.color = Color.NEGRO;
                hermano.color = Color.ROJO;
                giraEnDireccionContraria(hermano, vertice);
                // Actualiza hermano
                hermano = hermano(vertice);
            }
            // Caso 6
            hermano.color = padre.color;
            VerticeRojinegro hijoHermano = hijoHermanoContrario(vertice);
            hijoHermano.color = padre.color = Color.NEGRO;
            giraEnMismaDireccion(padre, vertice);
            esBalanceado = true; // sale del while
        }
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * VerticeRojinegro}). Método auxiliar para hacer esta audición en un único lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice.
     * @return el vértice recibido visto como vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    private VerticeRojinegro verticeRojinegro(VerticeArbolBinario<T> vertice) {
        return (VerticeRojinegro)vertice;
    }

    /**
     * Verifica de manera segura si un vértice es rojo.
     * @param vertice el vértice a verificar.
     * @return <code>true</code> si el vértice es rojo, <code>false</code> en otro caso.
     */
    private boolean esRojo(VerticeRojinegro vertice) {
	    return vertice != null && vertice.color == Color.ROJO;
    }

    /**
     * Verifica de manera segura si un vértice es negro.
     * @param vertice el vértice a verificar.
     * @return <code>true</code> si el vértice es negro, <code>false</code> en otro caso.
     */
    private boolean esNegro(VerticeRojinegro vertice) {
	    return vertice == null || vertice.color == Color.NEGRO;
    }

    /**
     * Regresa el padre de un vértice.
     * @param vertice el vertice a tratar.
     * @return el padre de un vértice.
     */
    private VerticeRojinegro padre(Vertice vertice) {
	    return verticeRojinegro(vertice.padre);
    }

    /**
     * Obtiene el tío de un vértice.
     * @param vertice el vertice a tratar.
     * @return el tio de un vértice.
     */
    private VerticeRojinegro tio(Vertice vertice) {
        Vertice abuelo = vertice.padre.padre;
        Vertice padre = vertice.padre;
        if (esIzquierdo(padre))
            return verticeRojinegro(abuelo.derecho);
        return verticeRojinegro(abuelo.izquierdo);
    }

    /**
     * Obtiene el abuelo de un vértice.
     * @param vertice el vertice a tratar.
     * @return el abuelo de un vértice.
     */
    private VerticeRojinegro abuelo(Vertice vertice) {
	    return verticeRojinegro(vertice.padre.padre);
    }

    /**
     * Nos dice si el vértice tiene dirección izquierda.
     * @param vertice el vertice a tratar.
     * @return <code>true</code> si el vértice tiene dirección izquierda,
     *         <code>false</code> en otro caso.
     */
    private boolean esIzquierdo(Vertice vertice) {
	    return vertice.padre.izquierdo == vertice;
    }

    /**
     * Nos dice si el vértice tiene dirección derecha.
     * @param vertice el vertice a tratar.
     * @return <code>true</code> si el vértice tiene dirección derecha,
     *         <code>false</code> en otro caso.
     */
    private boolean esDerecho(Vertice vertice) {
	    return vertice.padre.derecho == vertice;
    }

    /**
     * Nos dice si los vértices están cruzados.
     * @param vertice1 el vértice uno.
     * @param vertice2 el vértice dos.
     * @return <code>true</code> si los vértices están cruzados,
     *         <code>false</code> en otro caso.
     */
    private boolean sonCruzados(Vertice vertice1, Vertice vertice2) {
	    return esIzquierdo(vertice1) != esIzquierdo(vertice2);
    }

    /**
     * Gira sobre un vértice en su dirección correspondiente.
     * @param padre el vértice padre.
     */
    private void giraEnSuDireccion(Vertice vertice) {
        if (esIzquierdo(vertice))
            super.giraIzquierda(vertice);
        else 
            super.giraDerecha(vertice);
    }

    /**
     * Gira sobre el un vértice uno en dirección contraria al vértice dos.
     * @param vertice1 el vértice uno.
     * @param vertice2 el vértice dos.
     */
    private void giraEnDireccionContraria(Vertice vertice1, Vertice vertice2) {
        if (esIzquierdo(vertice2))
            super.giraDerecha(vertice1);
        else 
            super.giraIzquierda(vertice1);
    }

    /**
     * Regresa el hijo del vértice a eliminar.
     * Siempre tiene exactamente un hijo distinto de <code>null</code>.
     * @param el vértice a eliminar.
     * @return el hijo del vértice a eliminar.
     */
    private VerticeRojinegro hijo(Vertice vertice) {
	    return (vertice.derecho == null) ? verticeRojinegro(vertice.izquierdo) : verticeRojinegro(vertice.derecho);
    }

    /**
     * Regresa el hermano de un vértice.
     * @param vertice el vértice.
     * @return el hermano de un vértice.
     */
    private VerticeRojinegro hermano(Vertice vertice) {
	    return (esIzquierdo(vertice)) ? verticeRojinegro(vertice.padre.derecho) : verticeRojinegro(vertice.padre.izquierdo);
    }

    /**
     * Gira sobre el un vértice uno en la misma dirección al vértice dos.
     * @param vertice1 el vértice uno.
     * @param vertice2 el vértice dos.
     */
    private void giraEnMismaDireccion(Vertice vertice1, Vertice vertice2){
        if (esIzquierdo(vertice2))
            super.giraIzquierda(vertice1);
        else 
            super.giraDerecha(vertice1);
    }
    
    /**
     * Regresa el izquierdo del vértice.
     * @param vertice el vértice a tratar.
     * @return el izquierdo del vértice.
     */
    private VerticeRojinegro izquierdo(Vertice vertice) {
	    return verticeRojinegro(vertice.izquierdo);
    }
    
    /**
     * Regresa el derecho del vértice.
     * @param vertice el vértice a tratar.
     * @return el derecho del vértice.
     */
    private VerticeRojinegro derecho(Vertice vertice) {
	    return verticeRojinegro(vertice.derecho);
    }

    /**
     * Regresa al hijo del hermano del vértice con dirección contraria al vértice.
     * @param vertice el vértice a tratar.
     * @return al hijo del hermano del vértice con dirección contraria al vértice.
     */
    private VerticeRojinegro hijoHermanoContrario(Vertice vertice) {
        VerticeRojinegro hermano = hermano(vertice);
        return (esIzquierdo(vertice)) ? verticeRojinegro(hermano.derecho) : verticeRojinegro(hermano.izquierdo);
    }
	
}
