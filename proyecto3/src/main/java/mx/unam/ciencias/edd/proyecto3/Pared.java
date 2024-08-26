package mx.unam.ciencias.edd.proyecto3;
    
/**
 * <p>Clase que representa una pared entre dos cuartos.</p>
 * 
 * <p>Una pared es empleada para los algoritmos de generación de laberintos:
 * Prim y Kruskal.</p>
 */
public class Pared {

    /* Cuarto extremo 1. */
    private Cuarto cuarto1;
    /* Cuarto extremo 2. */
    private Cuarto cuarto2;

    /**
     * Define el estado inicial de la pared.
     * @param cuarto1 el cuarto extremo 1.
     * @param cuarto2 el cuarto extremo 2.
     */
    public Pared(Cuarto cuarto1, 
                    Cuarto cuarto2) {
        this.cuarto1 = cuarto1;
        this.cuarto2 = cuarto2;
    }

    /**
     * Regresa el cuarto extremo 1.
     * @return el cuarto extremo 1.
     */
    public Cuarto getCuarto1() {
        return cuarto1;
    }

    /**
     * Regresa el cuarto extremo 2.
     * @return el cuarto extremo 2.
     */
    public Cuarto getCuarto2() {
        return cuarto2;
    }
}