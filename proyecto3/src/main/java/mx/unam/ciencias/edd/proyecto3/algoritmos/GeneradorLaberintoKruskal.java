package mx.unam.ciencias.edd.proyecto3.algoritmos;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto3.Cuarto;
import mx.unam.ciencias.edd.proyecto3.EntradaEstandar;
import mx.unam.ciencias.edd.proyecto3.Pared;

/**
 * <p>Clase que genera un laberinto con el algoritmo de Kruskal.</p>
 * 
 * <p>El algoritmo hace lo siguiente:</p>
 * 
 * <ol>
 *    <li>Se llena una lista de paredes y se crea una lista para cada cuarto.</li>
 *    <li>Se elige una pared aleatoria de la lista de paredes y si
 *        los cuartos extremos de la pared no pertenecen a la misma lista,
 *        se quita la pared y se unen las listas.</li>
 *    <li>Se repite el paso 2 hasta que no haya paredes en la lista.</li>
 * </ol>
 */
public class GeneradorLaberintoKruskal extends GeneradorLaberinto {

    /* Lista de listas de cuartos. */
    private Lista<Lista<Cuarto>> cuartos;
    /* Lista de paredes. */
    private Lista<Pared> paredes;

    /**
     * Define el estado inicial del generador de laberintos.
     * @param entradaEstandar la entrada estandar del programa.
     */
    public GeneradorLaberintoKruskal(EntradaEstandar entradaEstandar) {
        super(entradaEstandar);
        cuartos = new Lista<Lista<Cuarto>>();
        paredes = new Lista<Pared>();
    }

    /**
     * Crea un laberinto con el algoritmo de Kruskal.
     * @param x la coordenada x del cuarto.
     * @param y la coordenada y del cuarto.
     */
    @Override
    public void creaLaberintoDesde(int x, int y) {
        while(!paredes.esVacia()) {
            int i = random.nextInt(paredes.getElementos());
            Pared pared = paredes.get(i);
            paredes.elimina(pared);
            Cuarto cuarto1 = pared.getCuarto1();
            Cuarto cuarto2 = pared.getCuarto2();
            Lista<Cuarto> lista1 = encuentraLista(cuarto1);
            Lista<Cuarto> lista2 = encuentraLista(cuarto2);
            if(!lista1.equals(lista2)) {
                quitaPared(cuarto1, cuarto2);
                for(Cuarto cuarto : lista2)
                    lista1.agrega(cuarto);
                cuartos.elimina(lista2);
            }
        }
    }

    /**
     * Llena el laberinto con cuartos.
     * Agrega las paredes a la lista de paredes.
     * Agrega los cuartos a la lista de cuartos.
     */
    @Override
    protected void llenaLaberinto() {
        for(int y = 0; y < renglones; y++)
            for(int x = 0; x < columnas; x++) {
                Cuarto cuarto = creaCuartoAleatorio(x, y);
                laberinto[y][x] = cuarto;
                Lista<Cuarto> lista = new Lista<Cuarto>();
                lista.agrega(cuarto);
                cuartos.agrega(lista);
                if(y > 0)
                    paredes.agrega(new Pared(cuarto, laberinto[y - 1][x]));
                if(x > 0)
                    paredes.agrega(new Pared(cuarto, laberinto[y][x - 1]));
            }
    }

    /**
     * Encuentra la lista que contiene al cuarto.
     * @param cuarto el cuarto.
     * @return la lista que contiene al cuarto.
     */
    private Lista<Cuarto> encuentraLista(Cuarto cuarto) {
        for(Lista<Cuarto> lista : cuartos)
            if(lista.contiene(cuarto))
                return lista;
        return null; // Nunca se llega a este punto.
    }
}