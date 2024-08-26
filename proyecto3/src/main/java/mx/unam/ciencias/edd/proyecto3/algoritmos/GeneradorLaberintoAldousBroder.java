package mx.unam.ciencias.edd.proyecto3.algoritmos;

import mx.unam.ciencias.edd.Conjunto;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto3.Cuarto;
import mx.unam.ciencias.edd.proyecto3.EntradaEstandar;

/**
 * <p>Clase que genera un laberinto a partir de un cuarto con el algoritmo de Aldous-Broder.</p>
 * 
 * <p>El algoritmo hace lo siguiente:</p>
 * 
 * <ol>
 *    <li>Se elige un cuarto aleatorio.</li>
 *    <li>Mientras no se hayan visitado todos los cuartos.
 *        Se elige un vecino aleatorio del cuarto y si no ha sido visitado:
 *        <ul>
 *            <li>Se quita la pared entre el cuarto y el vecino.</li>
 *            <li>Se marca el vecino como visitado.</li>
 *       </ul>
 *  </li>
 * </ol>
 */
public class GeneradorLaberintoAldousBroder extends GeneradorLaberinto {

    /**
     * Define el estado inicial del generador de laberintos.
     * @param entradaEstandar la entrada estandar del programa.
     */
    public GeneradorLaberintoAldousBroder(EntradaEstandar entradaEstandar) {
        super(entradaEstandar);
    }

    /**
     * Crea un laberinto con el algoritmo de Aldous-Broder.
     * @param x la coordenada x del cuarto.
     * @param y la coordenada y del cuarto.
     */
    @Override
    public void creaLaberintoDesde(int x, int y) {
        Conjunto<Cuarto> visitados = new Conjunto<Cuarto>();
        int totalCuartos = renglones * columnas;
        int cuartosVisitados = 0;
        Cuarto cuarto = laberinto[y][x];
        while(cuartosVisitados < totalCuartos) {
            Lista<Cuarto> vecinos =  vecinosDe(cuarto);
            int i = random.nextInt(vecinos.getElementos());
            Cuarto vecino = vecinos.get(i);
            if(!visitados.contiene(vecino)) {
                visitados.agrega(vecino);
                cuartosVisitados++;
                quitaPared(cuarto, vecino);
            }
            cuarto = vecino;
        }
    }

}
