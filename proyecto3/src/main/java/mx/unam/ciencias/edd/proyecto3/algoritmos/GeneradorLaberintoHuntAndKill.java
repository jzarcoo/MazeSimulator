package mx.unam.ciencias.edd.proyecto3.algoritmos;

import mx.unam.ciencias.edd.Cola;
import mx.unam.ciencias.edd.Conjunto;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto3.Cuarto;
import mx.unam.ciencias.edd.proyecto3.EntradaEstandar;

/**
 * <p>Clase que genera un laberinto a partir de un cuarto con el algoritmo de Hunt and Kill.</p>
 * 
 * <p>El algoritmo hace lo siguiente:</p>
 * 
 * <ol>
 *      <li>Se elige un cuarto aleatorio y se marca como visitado.</li>
 *      <li>Se elige un vecino sin visitar del cuarto y se crea un camino a través del vecino.</li>
 *      <li>Si todos los vecinos han sido visitados, se regresa al último cuarto que no ha creado 
 *          un camino y se repite el paso 2.</li>
 *      <li>Se repite el paso 3 hasta que todos los cuartos hayan sido visitados.</li>
 * </ol>
 */
public class GeneradorLaberintoHuntAndKill extends GeneradorLaberinto {
    
    /* Conjunto de cuartos visitados. */
    private Conjunto<Cuarto> visitados;
    
    /**
     * Define el estado inicial del generador de laberintos.
     * @param entradaEstandar la entrada estandar del programa.
     */
    public GeneradorLaberintoHuntAndKill(EntradaEstandar entradaEstandar) {
        super(entradaEstandar);
        visitados = new Conjunto<Cuarto>();
    }

    /**
     * Crea un laberinto con el algoritmo de Hunt and Kill.
     * @param x la coordenada x del cuarto.
     * @param y la coordenada y del cuarto.
     */
    @Override
    public void creaLaberintoDesde(int x, int y) {
        Cola<Cuarto> cola = new Cola<Cuarto>();
        Cuarto animal = laberinto[y][x];
        visitados.agrega(animal);
        cola.mete(animal);
        while(!cola.esVacia()) {
            Lista<Cuarto> vecinosSinVisitar = vecinosSinVisitarDe(animal);
            if(!vecinosSinVisitar.esVacia()) {
                int i = random.nextInt(vecinosSinVisitar.getElementos());
                Cuarto vecino = vecinosSinVisitar.get(i);
                quitaPared(animal, vecino);
                cola.mete(vecino);
                visitados.agrega(vecino);
                animal = vecino;
            } else {
                animal = cola.saca();
                Lista<Cuarto> vecinos = vecinosSinVisitarDe(animal);
                if(vecinos.esVacia())
                    continue;
                int i = random.nextInt(vecinos.getElementos());
                Cuarto vecino = vecinos.get(i);
                quitaPared(animal, vecino);
                animal = vecino;

            }
        }
    }

    /**
     * Regresa los vecinos sin visitar de un cuarto.
     * @param cuarto el cuarto.
     * @return los vecinos sin visitar de un cuarto.
     */
    protected Lista<Cuarto> vecinosSinVisitarDe(Cuarto cuarto) {
        Lista<Cuarto> vecinos = vecinosDe(cuarto);
        Lista<Cuarto> vecinosSinVisitar = new Lista<Cuarto>();
        for(Cuarto vecino : vecinos)
            if(!visitados.contiene(vecino))
                vecinosSinVisitar.agrega(vecino);
        return vecinosSinVisitar;
    }

}
