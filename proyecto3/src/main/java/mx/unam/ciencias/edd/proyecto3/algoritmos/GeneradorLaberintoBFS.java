package mx.unam.ciencias.edd.proyecto3.algoritmos;

import mx.unam.ciencias.edd.Conjunto;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto3.EntradaEstandar;
import mx.unam.ciencias.edd.proyecto3.Cuarto;

/**
 * <p>Clase que genera un laberinto a partir de un cuarto con el algoritmo de búsqueda en amplitud (BFS).</p>
 * 
 * <p>El algoritmo hace lo siguiente:</p>
 * 
 * <ol>
 *     <li>Se elige un cuarto aleatorio y se marca como visitado.</li>
 *     <li>Se elige un cuarto aleatorio de la lista de cuartos y se elimina de la lista.</li>
 *     <li>Por cada vecino del cuarto, si no ha sido visitado:
 *          <ul>
 *              <li>Se quita la pared entre el cuarto y el vecino.</li>
 *              <li>Se agrega el vecino a la lista de cuartos.</li>
 *              <li>Se marca el vecino como visitado.</li>
 *          </ul>
 *     </li>
 * </ol>
 */
public class GeneradorLaberintoBFS extends GeneradorLaberinto {
    
    /**
    * Define el estado inicial del generador de laberintos.
    * @param entradaEstandar la entrada estandar del programa.
    */
    public GeneradorLaberintoBFS(EntradaEstandar entradaEstandar) {
        super(entradaEstandar);
    }

    /**
     * Crea un laberinto a partir de un cuarto con el algoritmo de búsqueda en amplitud (BFS).
     * @param x la coordenada x del cuarto.
     * @param y la coordenada y del cuarto.
     */
    @Override
    public void creaLaberintoDesde(int x, int y) {
        Conjunto<Cuarto> visitados = new Conjunto<Cuarto>();
        Lista<Cuarto> lista = new Lista<Cuarto>();
        lista.agrega(laberinto[y][x]);
        visitados.agrega(laberinto[y][x]);
        while(!lista.esVacia()) {
            int i = random.nextInt(lista.getElementos());
            Cuarto cuarto = lista.get(i);
            lista.elimina(cuarto);
            Lista<Cuarto> vecinos = vecinosDe(cuarto);
            for(Cuarto vecino : vecinos)
                if(!visitados.contiene(vecino)) {
                    quitaPared(cuarto, vecino);
                    lista.agrega(vecino);
                    visitados.agrega(vecino);
                }
        }
    }
}