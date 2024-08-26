package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return iterador.next().get();
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T>, Comparable<Vertice> {

        /* El elemento del vértice. */
        private T elemento;
        /* La distancia del vértice. */
        private double distancia;
        /* El diccionario de vecinos del vértice. */
        private Diccionario<T, Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            vecinos = new Diccionario<T, Vecino>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getElementos();
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecinos;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            return Double.compare(distancia, vertice.distancia);
        }
    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            this.vecino = vecino;
            this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            return vecino.get();
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            return vecino.getGrado();
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecino.vecinos();
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino<T> {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica<T>.Vertice v, Grafica<T>.Vecino a);
    }

    /* Vértices. */
    private Diccionario<T, Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        vertices = new Diccionario<T, Vertice>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getElementos();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        if(contiene(elemento))
            throw new IllegalArgumentException("El elemento ya ha sido agregado.");
        vertices.agrega(elemento, new Vertice(elemento));
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        conecta(a, b, 1);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
        if(a.equals(b) || peso <= 0)
            throw new IllegalArgumentException("Argumentos inválidos.");
        Vertice va = vertices.get(a);
        Vertice vb = vertices.get(b);
        if(sonVecinos(va, vb))
            throw new IllegalArgumentException("Los elementos ya están conectados.");
        va.vecinos.agrega(b, new Vecino(vb, peso));
        vb.vecinos.agrega(a, new Vecino(va, peso));
        aristas++;
    }

    /**
     * Nos dice si dos elementos de la gráfica son vecinos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro
     *         caso.
     */
    private boolean sonVecinos(Vertice a, Vertice b) {
        return a.vecinos.contiene(b.get()) && b.vecinos.contiene(a.get());
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        Vertice va = vertices.get(a);
        Vertice vb = vertices.get(b);
        if(!sonVecinos(va, vb))
            throw new IllegalArgumentException("Los elementos no están conectados.");
        desconecta(va, vb);
    }

    /**
     * Elimina la arista que conecta dos vertices de la gráfica. Los elementos
     * deben estar en la gráfica y estar conectados entre ellos.
     * @param a el primer vertices.
     * @param b el segundo vertices.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    private void desconecta(Vertice a, Vertice b) {
        a.vecinos.elimina(b.get());
        b.vecinos.elimina(a.get());
        aristas--;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        return vertices.contiene(elemento);
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        Vertice v = vertices.get(elemento);
        v.vecinos.forEach(u -> desconecta(v, u.vecino));
        vertices.elimina(elemento);
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        return sonVecinos(vertices.get(a), vertices.get(b));
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        Vertice va = vertices.get(a);
        Vertice vb = vertices.get(b);
        if(!sonVecinos(va, vb))
            throw new IllegalArgumentException("Los elementos no están conectados.");
        return va.vecinos.get(b).peso;
    }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
        Vertice va = vertices.get(a);
        Vertice vb = vertices.get(b);
        if(peso <= 0 || !sonVecinos(va, vb))
            throw new IllegalArgumentException("Argumentos inválidos.");
        va.vecinos.get(b).peso = vb.vecinos.get(a).peso = peso;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        return vertices.get(elemento);
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        if(vertices.getElementos() < 2) return true;
        return recorre(vertices.iterator().next().get(), v -> {}, new Pila<Vertice>()) == vertices.getElementos();
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        vertices.forEach(v -> accion.actua(v));
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        recorre(elemento, accion, new Cola<Vertice>());
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        recorre(elemento, accion, new Pila<Vertice>());
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el orden 
     * determinado por BFS o DFS, según la estructura dada, comenzando por el vértice 
     * correspondiente al elemento recibido. Además, regresa el número de vértices
     * visitados.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @param meteSaca la estructura de datos que se utilizará para recorrer la gráfica.
     * @return el número de vértices visitados.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    private int recorre(T elemento, AccionVerticeGrafica<T> accion, MeteSaca<Vertice> meteSaca) {
        Conjunto<Vertice> rojos = new Conjunto<Vertice>();
        Vertice v = vertices.get(elemento);
        meteSaca.mete(v);
        rojos.agrega(v);
        while(!meteSaca.esVacia()) {
            Vertice u = meteSaca.saca();
            accion.actua(u);
            for(Vecino w : u.vecinos)
                if(!rojos.contiene(w.vecino)) {
                    meteSaca.mete(w.vecino);
                    rojos.agrega(w.vecino);
                }
        }
        return rojos.getElementos();
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        vertices.limpia();
        aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        Conjunto<T> visitados = new Conjunto<T>();
        StringBuilder sb = new StringBuilder();
        StringBuilder sbVertices = new StringBuilder();
        StringBuilder sbAristas = new StringBuilder();
        sb.append("{");
        for(Vertice v : vertices) {
            sbVertices.append(v.get() + ", ");
            for(Vecino w : v.vecinos)
                if(!visitados.contiene(w.get()))
                    sbAristas.append("(" + v.get() + ", " + w.get() + "), ");
            visitados.agrega(v.get());
        }
        sb.append(sbVertices.toString());
        sb.append("}, {");
        sb.append(sbAristas.toString());
        return sb.append("}").toString();
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        if (getElementos() != grafica.getElementos() || getAristas() != grafica.getAristas())
            return false;
        for (Vertice vertice : vertices) {
            if (!grafica.contiene(vertice.elemento))
                return false;
            for(Vecino vecino : vertice.vecinos)
                if(!grafica.sonVecinos(vertice.elemento, vecino.get()))
                    return false;
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <code>a</code> y
     *         <code>b</code>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        // Obtenemos los vértices de origen y destino.
        Vertice s = vertices.get(origen);
        Vertice t = vertices.get(destino);
        // Trayectoria trivial.
        if(s.equals(t)) {
            Lista<VerticeGrafica<T>> l = new Lista<VerticeGrafica<T>>();
            l.agrega(s);
            return l;
        }
        // Inicializamos las distancias.
        vertices.forEach(v -> v.distancia = Double.POSITIVE_INFINITY);
        s.distancia = 0;
        // BFS.
        Cola<Vertice> q = new Cola<Vertice>();
        q.mete(s);
        while(!q.esVacia()) {
            Vertice v = q.saca();
            for(Vecino u : v.vecinos)
                if(u.vecino.distancia == Double.POSITIVE_INFINITY) {
                    u.vecino.distancia = v.distancia + 1;
                    q.mete(u.vecino);
                }
        }
        return reconstruyeTrayectoria(t, (v, u) -> u.vecino.distancia == v.distancia - 1);
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <code>origen</code> y
     *         el vértice <code>destino</code>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        // Obtenemos los vértices de origen y destino.
        Vertice s = vertices.get(origen);
        Vertice t = vertices.get(destino);
        // Trayectoria trivial.
        if(s.equals(t)) {
            Lista<VerticeGrafica<T>> l = new Lista<VerticeGrafica<T>>();
            l.agrega(s);
            return l;
        }
        // Inicializamos las distancias.
        vertices.forEach(v -> v.distancia = Double.POSITIVE_INFINITY);
        s.distancia = 0;
        // Dijkstra.
        // Usamos un montículo de arreglo si el número de aristas es mayor a n(n-1)/2 - n.
        // En otro caso, usamos un montículo mínimo.
        int n = vertices.getElementos();
        int b = (((n - 1) * n) >> 1 )- n;
        MonticuloDijkstra<Vertice> m = (aristas > b) ? new MonticuloArreglo<Vertice>(vertices, vertices.getElementos()) : new MonticuloMinimo<Vertice>(vertices, vertices.getElementos());
        while(!m.esVacia()) {
            Vertice v = m.elimina();
            for(Vecino u : v.vecinos)
                if(v.distancia + u.peso < u.vecino.distancia) {
                    u.vecino.distancia = v.distancia + u.peso;
                    m.reordena(u.vecino);
                }
        }
        return reconstruyeTrayectoria(t, (v, u) -> u.vecino.distancia == v.distancia - u.peso);
    }

    /**
     * Reconstruye una trayectoria de vértices a partir de un vértice final y un
     * buscador de camino.
     * @param v el vértice final.
     * @param buscador el buscador de camino.
     * @return una lista con la trayectoria de vértices.
     */
    private Lista<VerticeGrafica<T>> reconstruyeTrayectoria(Vertice v, BuscadorCamino<T> buscador) {
        Lista<VerticeGrafica<T>> l = new Lista<VerticeGrafica<T>>();
        if(v.distancia == Double.POSITIVE_INFINITY) return l;
        l.agrega(v);
        while(v.distancia != 0)
            for(Vecino u : v.vecinos)
                if(buscador.seSiguen(v, u)) {
                    l.agregaInicio(u.vecino);
                    v = u.vecino;
                    break;
                }
        return l;
    }
}
