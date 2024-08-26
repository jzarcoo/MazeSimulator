package mx.unam.ciencias.edd;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario generaliza el
 * concepto de arreglo, mapeando un conjunto de <em>llaves</em> a una colección
 * de <em>valores</em>.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /* Clase interna privada para entradas. */
    private class Entrada {

        /* La llave. */
        private K llave;
        /* El valor. */
        private V valor;

        /* Construye una nueva entrada. */
        private Entrada(K llave, V valor) {
            this.llave = llave;
            this.valor = valor;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador {

        /* En qué lista estamos. */
        private int indice;
        /* Iterador auxiliar. */
        private Iterator<Entrada> iterador;

        /* Construye un nuevo iterador, auxiliándose de las listas del
         * diccionario. */
        private Iterador() {
            indice = -1;
            mueveIterador();
        }

        /* Nos dice si hay una siguiente entrada. */
        public boolean hasNext() {
            return iterador != null;
        }

        /* Regresa la siguiente entrada. */
        protected Entrada siguiente() {
            if(iterador == null) throw new NoSuchElementException("Iterador nulo.");
            Entrada e = iterador.next();
            if(!iterador.hasNext()) mueveIterador();
            return e;
        }

        /* Mueve el iterador a la siguiente entrada válida. */
        private void mueveIterador() {
            while(++indice < entradas.length) {
                if(entradas[indice] != null) {
                    iterador = entradas[indice].iterator();
                    return;
                }
            }
            iterador = null;
        }
    }

    /* Clase interna privada para iteradores de llaves. */
    private class IteradorLlaves extends Iterador
        implements Iterator<K> {

        /* Regresa el siguiente elemento. */
        @Override public K next() {
            return siguiente().llave;
        }
    }

    /* Clase interna privada para iteradores de valores. */
    private class IteradorValores extends Iterador
        implements Iterator<V> {

        /* Regresa el siguiente elemento. */
        @Override public V next() {
            return siguiente().valor;
        }
    }

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Capacidad mínima; decidida arbitrariamente a 2^6. */
    private static final int MINIMA_CAPACIDAD = 64;

    /* Dispersor. */
    private Dispersor<K> dispersor;
    /* Nuestro diccionario. */
    private Lista<Entrada>[] entradas;
    /* Número de valores. */
    private int elementos;

    /* Truco para crear un arreglo genérico. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked")
    private Lista<Entrada>[] nuevoArreglo(int n) {
        return (Lista<Entrada>[])Array.newInstance(Lista.class, n);
    }

    /**
     * Construye un diccionario con una capacidad inicial y dispersor
     * predeterminados.
     */
    public Diccionario() {
        this(MINIMA_CAPACIDAD, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial definida por el
     * usuario, y un dispersor predeterminado.
     * @param capacidad la capacidad a utilizar.
     */
    public Diccionario(int capacidad) {
        this(capacidad, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial predeterminada, y un
     * dispersor definido por el usuario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(Dispersor<K> dispersor) {
        this(MINIMA_CAPACIDAD, dispersor);
    }

    /**
     * Construye un diccionario con una capacidad inicial y un método de
     * dispersor definidos por el usuario.
     * @param capacidad la capacidad inicial del diccionario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(int capacidad, Dispersor<K> dispersor) {
        this.dispersor = dispersor;
        if(capacidad < MINIMA_CAPACIDAD) capacidad = MINIMA_CAPACIDAD;
        capacidad <<= 1; // duplicamos la capacidad para evitar colisiones.
        // Buscamos la capacidad en potencias de 2.
        int potencia = bitMasSignificativo(capacidad);
        if(potencia != capacidad)
            capacidad = potencia << 1;
        entradas = nuevoArreglo(capacidad);
    }

    /**
     * Nos dice el bit más significativo de un número.
     * Es una implementación del método highestOneBit de la clase Integer.
     * @param n el número del que queremos saber el bit más significativo.
     * @return el bit más significativo del número.
     */
    private int bitMasSignificativo(int n) {
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        return n - (n >>> 1);
    }

    /**
     * Agrega un nuevo valor al diccionario, usando la llave proporcionada. Si
     * la llave ya había sido utilizada antes para agregar un valor, el
     * diccionario reemplaza ese valor con el recibido aquí.
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     * @throws IllegalArgumentException si la llave o el valor son nulos.
     */
    public void agrega(K llave, V valor) {
        if(llave == null || valor == null) throw new IllegalArgumentException("Argumentos nulos.");
        int i = dispersor.dispersa(llave) & (entradas.length - 1);
        Entrada e = buscaEntrada(llave);
        if(e != null) {
            e.valor = valor;
            return;
        }
        if(entradas[i] == null) 
            entradas[i] = new Lista<Entrada>();
        entradas[i].agrega(new Entrada(llave, valor));
        elementos++;
        if(carga() >= MAXIMA_CARGA) reestructura();
    }

    /**
     * Busca la entrada asociada a la llave proporcionada.
     * @param llave la llave a buscar.
     * @return la entrada asociada a la llave, o <code>null</code> si la llave
     *        no está en el diccionario.
     */
    private Entrada buscaEntrada(K llave) {
        if(llave == null) return null;
        int i = dispersor.dispersa(llave) & (entradas.length - 1);
        if(entradas[i] == null) return null;
        for(Entrada e : entradas[i])
            if(e.llave.equals(llave)) return e;
        return null;
    }

    /**
     * Reestructura el diccionario, duplicando su capacidad y reasignando las
     * entradas.
     */
    private void reestructura() {
        Lista<Entrada>[] nuevasEntradas = nuevoArreglo(entradas.length << 1);
        Iterador i = new Iterador();
        while(i.hasNext()) {
            Entrada e = i.siguiente();
            int j = dispersor.dispersa(e.llave) & (nuevasEntradas.length - 1);
            if(nuevasEntradas[j] == null) 
                nuevasEntradas[j] = new Lista<Entrada>();
            nuevasEntradas[j].agrega(e);
        }
        entradas = nuevasEntradas;
    }

    /**
     * Regresa el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no está en el diccionario.
     */
    public V get(K llave) {
        if(llave == null) throw new IllegalArgumentException("Llave nula.");
        Entrada e = buscaEntrada(llave);
        if(e == null) throw new NoSuchElementException("Llave invalida.");
        return e.valor;
    }

    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <code>true</code> si la llave está en el diccionario,
     *         <code>false</code> en otro caso.
     */
    public boolean contiene(K llave) {
        return buscaEntrada(llave) != null;
    }

    /**
     * Elimina el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor a eliminar.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no se encuentra en
     *         el diccionario.
     */
    public void elimina(K llave) {
        if(llave == null) throw new IllegalArgumentException("Llave nula.");
        int i = dispersor.dispersa(llave) & (entradas.length - 1);
        if(entradas[i] == null) throw new NoSuchElementException("Llave no encontrada.");
        Entrada e = buscaEntrada(llave);
        if(e == null) return;
        entradas[i].elimina(e);
        elementos--;
        if(entradas[i].esVacia()) entradas[i] = null;
    }

    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
        int colisiones = 0;
        for(Lista<Entrada> l : entradas)
            if(l != null)
                colisiones += l.getLongitud() - 1;
        return colisiones;
    }

    /**
     * Nos dice el máximo número de colisiones para una misma llave que tenemos
     * en el diccionario.
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
        int max = 0;
        for(Lista<Entrada> l : entradas)
            if(l != null && l.getLongitud() > max)
                max = l.getLongitud();
        return max - 1;
    }

    /**
     * Nos dice la carga del diccionario.
     * @return la carga del diccionario.
     */
    public double carga() {
        return (double) elementos / entradas.length;
    }

    /**
     * Regresa el número de entradas en el diccionario.
     * @return el número de entradas en el diccionario.
     */
    public int getElementos() {
        return elementos;
    }

    /**
     * Nos dice si el diccionario es vacío.
     * @return <code>true</code> si el diccionario es vacío, <code>false</code>
     *         en otro caso.
     */
    public boolean esVacia() {
        return elementos == 0;
    }

    /**
     * Limpia el diccionario de elementos, dejándolo vacío.
     */
    public void limpia() {
        entradas = nuevoArreglo(entradas.length);
        elementos = 0;
    }

    /**
     * Regresa una representación en cadena del diccionario.
     * @return una representación en cadena del diccionario.
     */
    @Override public String toString() {
        if(elementos == 0) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        Iterador i = new Iterador();
        while(i.hasNext()) {
            Entrada e = i.siguiente();
            sb.append(String.format("'%s': '%s', ", e.llave, e.valor));
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Nos dice si el diccionario es igual al objeto recibido.
     * @param o el objeto que queremos saber si es igual al diccionario.
     * @return <code>true</code> si el objeto recibido es instancia de
     *         Diccionario, y tiene las mismas llaves asociadas a los mismos
     *         valores.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Diccionario<K, V> d =
            (Diccionario<K, V>)o;
        if(elementos != d.elementos) return false;
        Iterador i = new Iterador();
        while(i.hasNext()) {
            Entrada e = i.siguiente();
            if(!d.contiene(e.llave) || !d.get(e.llave).equals(e.valor))
                return false;
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar las llaves del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar las llaves del diccionario.
     */
    public Iterator<K> iteradorLlaves() {
        return new IteradorLlaves();
    }

    /**
     * Regresa un iterador para iterar los valores del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar los valores del diccionario.
     */
    @Override public Iterator<V> iterator() {
        return new IteradorValores();
    }
}
