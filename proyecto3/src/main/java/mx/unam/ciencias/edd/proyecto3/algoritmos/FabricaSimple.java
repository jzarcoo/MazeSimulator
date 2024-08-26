package mx.unam.ciencias.edd.proyecto3.algoritmos;

import mx.unam.ciencias.edd.proyecto3.EntradaEstandar;

/**
 * <p>Clase que fabrica generadores de laberintos con base en la entrada estandar. La fabrica simple 
 * elige un generador de laberintos con base en el tamaño del laberinto.</p>
 */
public class FabricaSimple {

    /* Número de algoritmos generadores. */
    private static final int NUM_ALGORITMOS = 7;

    /* Rango para cada algoritmo generador. */
    private static final int RANGO = 255 / NUM_ALGORITMOS;

    /* Constructor privado para evitar instanciacion. */
    private FabricaSimple() {}
    
    /**
     * Crea un generador de laberintos.
     * @param ee la entrada estandar del programa.
     * @return un generador de laberintos.
     */
    public static GeneradorLaberinto creaGeneradorLaberinto(EntradaEstandar ee) {
        int columnas = ee.getColumnas();
        int renglones = ee.getRenglones();
        if(columnas < RANGO && renglones < RANGO)
            return new GeneradorLaberintoKruskal(ee);
        else if(columnas < RANGO * 2 && renglones < RANGO * 2)
            return new GeneradorLaberintoDFS(ee);
        else if(columnas < RANGO * 3 && renglones < RANGO * 3)
            return new GeneradorLaberintoAldousBroder(ee);
        else if(columnas < RANGO * 4 && renglones < RANGO * 4)
            return new GeneradorLaberintoHuntAndKill(ee);
        else if(columnas < RANGO * 5 && renglones < RANGO * 5)
            return new GeneradorLaberintoBFS(ee);
        else if(columnas < RANGO * 6 && renglones < RANGO * 6)
            return new GeneradorLaberintoPrim(ee);
        else
            return new GeneradorLaberintoArbolBinario(ee);
    }
}