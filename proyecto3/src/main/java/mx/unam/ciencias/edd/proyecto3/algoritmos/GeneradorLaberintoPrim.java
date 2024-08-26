package mx.unam.ciencias.edd.proyecto3.algoritmos;

import mx.unam.ciencias.edd.Conjunto;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto3.Cuarto;
import mx.unam.ciencias.edd.proyecto3.EntradaEstandar;
import mx.unam.ciencias.edd.proyecto3.Pared;

/**
 * <p>Clase que genera un laberinto a partir de un cuarto con el algoritmo de Prim.</p>
 * 
 * <p>El algoritmo hace lo siguiente:</p>
 * 
 * <ol>
 *      <li>Se elige un cuarto aleatorio y se marca como visitado.</li>
 *      <li>Se agregan las paredes de los vecinos no visitados a una lista de paredes.</li>
 *      <li>Se elige una pared aleatoria de la lista de paredes y se elimina de la lista.</li>
 *      <li>Si alguno de los cuartos extremos de la pared no ha sido visitado, se quita la pared
 *          y se marca el cuarto como visitado.</li>
 *      <li>Se repite el paso 3 hasta que no haya paredes en la lista.</li>
 * </ol>
 */
public class GeneradorLaberintoPrim extends GeneradorLaberinto {

    /* Conjunto de cuartos visitados. */
    private Conjunto<Cuarto> visitados;
    /* Lista de paredes. */
    private Lista<Pared> paredes;

    /**
     * Define el estado inicial del generador de laberintos.
     * @param entradaEstandar la entrada estandar del programa.
     */
    public GeneradorLaberintoPrim(EntradaEstandar entradaEstandar) {
        super(entradaEstandar);
        visitados = new Conjunto<Cuarto>();
        paredes = new Lista<Pared>();
    }

    /**
     * Crea un laberinto a partir de un cuarto con el algoritmo de Prim.
     * @param x la coordenada x del cuarto.
     * @param y la coordenada y del cuarto.
     */
    @Override
    public void creaLaberintoDesde(int x, int y) {
        marca(laberinto[y][x]);
        while(!paredes.esVacia()) {
            int i = random.nextInt(paredes.getElementos());
            Pared pared = paredes.get(i);
            paredes.elimina(pared);
            Cuarto cuarto1 = pared.getCuarto1();
            Cuarto cuarto2 = pared.getCuarto2();
            if(!visitados.contiene(cuarto1) || !visitados.contiene(cuarto2)) {
                quitaPared(cuarto1, cuarto2);
                if(!visitados.contiene(cuarto1))
                    marca(cuarto1);
                if(!visitados.contiene(cuarto2))
                    marca(cuarto2);
            }
        }
    }
    

    /**
     * Agrega un cuarto a los visitados y sus paredes, de sus vecinos
     * sin visitar, a la lista de paredes.
     * @param cuarto el cuarto.
     */
    private void marca(Cuarto cuarto) {
        visitados.agrega(cuarto);
        Lista<Cuarto> vecinos = vecinosDe(cuarto);
        for(Cuarto vecino : vecinos)
            if(!visitados.contiene(vecino))
                paredes.agrega(new Pared(cuarto, vecino));
    }

}
