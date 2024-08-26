package mx.unam.ciencias.edd.proyecto3.algoritmos;

import mx.unam.ciencias.edd.proyecto3.EntradaEstandar;

/**
 * <p>Clase que genera un laberinto a partir de un cuarto con el algoritmo de Arbol Binario.</p>
 * 
 * <p>El algoritmo hace lo siguiente:</p>
 * 
 * <ol>
 *     <li>Se recorre cada cuarto del laberinto.</li>
 *     <li>Se elige aleatoriamente si se quita la pared norte o la pared oeste del cuarto.</li>
 * </ol>
 */
public class GeneradorLaberintoArbolBinario extends GeneradorLaberinto {

    /**
     * Define el estado inicial del generador de laberintos.
     * @param entradaEstandar la entrada estandar del programa.
     */
    public GeneradorLaberintoArbolBinario(EntradaEstandar entradaEstandar) {
        super(entradaEstandar);
    }

    /**
     * Crea un laberinto con el algoritmo de Arbol Binario.
     * @param a la coordenada x del cuarto.
     * @param b la coordenada y del cuarto.
     */
    @Override
    public void creaLaberintoDesde(int a, int b) {
        for (int y = 0; y < renglones; y++) 
            for (int x = 0; x < columnas; x++) {
                boolean norte = y > 0 && (x == 0 || random.nextBoolean());
                if (norte)
                    quitaPared(laberinto[y][x], laberinto[y - 1][x]);
                else if (x > 0)
                    quitaPared(laberinto[y][x], laberinto[y][x - 1]);
            }
    }

}