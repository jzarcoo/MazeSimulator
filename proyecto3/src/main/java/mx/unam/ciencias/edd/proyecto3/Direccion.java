package mx.unam.ciencias.edd.proyecto3;

/**
 * <p>Enumeracion para representar las direcciones de los cuartos de un laberinto. Una dirección
 * cuenta con un nombre, un cambio en el renglón y un cambio en la columna en la matriz de cuartos.</p>
 */
public enum Direccion {

    /* Dirección norte. */
    NORTE("Norte", -1, 0),
    /* Dirección sur. */
    SUR("Sur", 1, 0),
    /* Dirección este. */
    ESTE("Este", 0, 1),
    /* Dirección oeste. */
    OESTE("Oeste", 0, -1);

    /* Nombre de la dirección. */
    private String nombre;
    /* Cambio en el renglón. */
    private int cambioRenglon;
    /* Cambio en la columna. */
    private int cambioColumna;

    /**
     * Define el estado inicial de la dirección.
     * @param nombre el nombre de la dirección.
     * @param cambioRenglon el cambio en la fila.
     * @param cambioColumna el cambio en la columna.
     */
    private Direccion(String nombre, 
                      int cambioRenglon, 
                      int cambioColumna) {
        this.nombre = nombre;
        this.cambioRenglon = cambioRenglon;
        this.cambioColumna = cambioColumna;
    }

    /**
     * Regresa el cambio en la columna.
     * @return el cambio en la columna.
     */
    public int getCambioColumna() {
        return cambioColumna;
    }

    /**
     * Regresa el cambio en la fila.
     * @return el cambio en la fila.
     
     */
    public int getCambioRenglon() {
        return cambioRenglon;
    }
}