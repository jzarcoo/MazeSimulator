package mx.unam.ciencias.edd.proyecto3.algoritmos;

import mx.unam.ciencias.edd.Conjunto;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto3.EntradaEstandar;
import mx.unam.ciencias.edd.proyecto3.Cuarto;

/**
 * <p>Clase que genera un laberinto a partir de un cuarto con el algoritmo de búsqueda en profundidad (DFS).</p>
 * 
 * <p>Puede llegar a volcarse en un stack overflow si el laberinto es muy grande.</p>
 * 
 * <p>El algoritmo hace lo siguiente:</p>
 * 
 * <ol>
 *    <li>Se elige un cuarto aleatorio y se marca como visitado.</li>
 *    <li>Se elige un vecino aleatorio del cuarto y si no ha sido visitado, se crea
 *        un camino a través del vecino.</li>
 *    <li>Si todos los vecinos han sido visitados, se regresa al último cuarto que no
 *        ha creado un camino y se repite el paso 2.</li>
 *    <li>Se repite el paso 3 hasta que todos los cuartos hayan sido visitados.</li>
 * </ol>
 */
public class GeneradorLaberintoDFS extends GeneradorLaberinto {

    /* Conjunto de cuartos visitados. */
    private Conjunto<Cuarto> visitados;
    
    /**
     * Define el estado inicial del generador de laberintos.
     * @param entradaEstandar la entrada estandar del programa.
     */
    public GeneradorLaberintoDFS(EntradaEstandar entradaEstandar) {
        super(entradaEstandar);
        visitados = new Conjunto<Cuarto>();
    }

    /**
     * Crea un laberinto a partir de un cuarto con el algoritmo de búsqueda en profundidad (DFS).
     * @param x la coordenada x del cuarto.
     * @param y la coordenada y del cuarto.
     */
    @Override
    public void creaLaberintoDesde(int x, int y) {
        visitados.agrega(laberinto[y][x]);
        Lista<Cuarto> vecinos = vecinosDe(laberinto[y][x]);
        while(!vecinos.esVacia()) {
            int i = random.nextInt(vecinos.getElementos());
            Cuarto vecino = vecinos.get(i);
            if(!visitados.contiene(vecino)) {
                quitaPared(laberinto[y][x], vecino);
                creaLaberintoDesde(vecino.getX(), vecino.getY());
            }
            vecinos.elimina(vecino);
        }
    }
}