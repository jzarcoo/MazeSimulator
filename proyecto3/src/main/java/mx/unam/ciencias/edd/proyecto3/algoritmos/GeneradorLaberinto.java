package mx.unam.ciencias.edd.proyecto3.algoritmos;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto3.Cuarto;
import mx.unam.ciencias.edd.proyecto3.Direccion;
import mx.unam.ciencias.edd.proyecto3.EntradaEstandar;
import mx.unam.ciencias.edd.proyecto3.Laberinto;

/**
 * <p>Clase abstracta que genera un laberinto. Un generador de laberintos tiene un  número de renglones y columnas, un
 * RNG, un laberinto con cuartos, junto con una entrada y una salida.</p>
 * 
 * <p>Las clases que hereden de GeneradorLaberinto deben implementar el método {@link GeneradorLaberinto#creaLaberintoDesde(int, int)}.</p>
 */
public abstract class GeneradorLaberinto {

    /* Rango de puntaje del cuarto. */
    protected static final int RANGO_PUNTAJE = 15;

    /* RNG. */
    protected Random random;
    /* Renglones del laberinto. */
    protected int renglones;
    /* Columnas del laberinto. */
    protected int columnas;

    /* Cuartos del laberinto. */
    protected Cuarto[][] laberinto;
    /* Cuarto de entrada. */
    protected Cuarto entrada;
    /* Cuarto de salida. */
    protected Cuarto salida;

    /**
     * Define el estado inicial del generador de laberintos.
     * @param entradaEstandar la entrada estandar del programa.
     */
    protected GeneradorLaberinto(EntradaEstandar entradaEstandar) {
        this.random = new Random(entradaEstandar.getSemilla());
        this.columnas = entradaEstandar.getColumnas();
        this.renglones = entradaEstandar.getRenglones();
        this.laberinto = new Cuarto[renglones][columnas];
    }

    /**
     * Crea el laberinto.
     */
    public void creaLaberinto() {
        creaEntrada();
        creaSalida();
        llenaLaberinto();
        creaLaberintoDesde(entrada.getX(), entrada.getY());
    }

    /**
     * Crea el cuarto de entrada.
     * @return el cuarto de entrada.
     */
    protected void creaEntrada() {
        entrada = creaCuartoConPuertaExterior();
    }

    /**
     * Crea el cuarto de salida.
     * @return el cuarto de salida.
     */
    protected void creaSalida() {
        do {
            salida = creaCuartoConPuertaExterior();
        }while (entrada.mismaPosicionCon(salida));
    }

    /**
     * Crea cuarto con puerta al exterior.
     * @return el cuarto con puerta al exterior.
     */
    protected Cuarto creaCuartoConPuertaExterior() {
        Direccion[] direcciones = Direccion.values();
        int i = random.nextInt(direcciones.length);
        Direccion posicion = direcciones[i];
        switch(posicion) {
            case NORTE:
                return new Cuarto(random.nextInt(columnas), 0, true, false, true, true);
            case SUR:
                return new Cuarto(random.nextInt(columnas), renglones - 1, true, true, true, false);
            case ESTE:
                return new Cuarto(columnas - 1, random.nextInt(renglones), false, true, true, true);
            case OESTE:
                return new Cuarto(0, random.nextInt(renglones), true, true, false, true);
            default:
                return null;
        }
    }

    /**
     * Llena el laberinto con cuartos.
     */
    protected void llenaLaberinto() {
        for(int y = 0; y < renglones; y++)
            for(int x = 0; x < columnas; x++)
                laberinto[y][x] = creaCuartoAleatorio(x, y);
    }

    /**
     * Crea un cuarto con puntaje aleatorio.
     * @param x la coordenada x del cuarto.
     * @param y la coordenada y del cuarto.
     * @return el cuarto aleatorio.
     */
    protected Cuarto creaCuartoAleatorio(int x, int y) {
        Cuarto cuarto = creaCuarto(x, y);
        int puntaje = random.nextInt(RANGO_PUNTAJE);
        cuarto.setPuntaje(puntaje);
        return cuarto;
    }

    /**
     * Crea un cuarto con todas sus paredes. A excepción de la entrada y la salida.
     * @param x la coordenada x del cuarto.
     * @param y la coordenada y del cuarto.
     * @return el cuarto.
     */
    protected Cuarto creaCuarto(int x, int y) {
        Cuarto cuarto = null;
        if(x == entrada.getX() && y == entrada.getY()) {
            cuarto = entrada;
        } else if(x == salida.getX() && y == salida.getY()) {
            cuarto = salida;
        } else {
            cuarto = new Cuarto(x, y, true, true, true, true);
        }
        return cuarto;
    }

    /**
     * Regresa la lista de vecinos de un cuarto en direccion norte, sur, este y oeste.
     * @return la lista de vecinos del cuarto.
     */
    protected Lista<Cuarto> vecinosDe(Cuarto cuarto) {
        Lista<Cuarto> vecinos = new Lista<Cuarto>();
        int x = cuarto.getX();
        int y = cuarto.getY();
        Direccion[] direcciones = Direccion.values();
        for(Direccion direccion : direcciones) {
            int nx = x + direccion.getCambioColumna();
            int ny = y + direccion.getCambioRenglon();
            if(nx >= 0 && nx < columnas && ny >= 0 && ny < renglones)
                vecinos.agrega(laberinto[ny][nx]);
        }
        return vecinos;
    }

    /**
     * Quita la pared entre dos cuartos.
     * @param cuarto el cuarto al que se le quitara la pared.
     * @param vecino el cuarto vecino al que se le quitara la pared.
     */
    protected void quitaPared(Cuarto cuarto, Cuarto vecino) {
        int x = cuarto.getX();
        int y = cuarto.getY();
        int vx = vecino.getX();
        int vy = vecino.getY();
        if(x == vx) {
            if(y < vy) {
                cuarto.setParedSur(false);
                vecino.setParedNorte(false);
            } else {
                cuarto.setParedNorte(false);
                vecino.setParedSur(false);
            }
        } else {
            if(x < vx) {
                cuarto.setParedEste(false);
                vecino.setParedOeste(false);
            } else {
                cuarto.setParedOeste(false);
                vecino.setParedEste(false);
            }
        }
    }

    /**
     * Crea un laberinto a partir de un cuarto.
     * @param x la coordenada x del cuarto.
     * @param y la coordenada y del cuarto.
     */
    public abstract void creaLaberintoDesde(int x, int y);

    /**
     * Genera un laberinto y lo escribe en un flujo de salida.
     * @param os el flujo de salida donde escribir el laberinto.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    public void generaLaberinto(OutputStream os) throws IOException {
        new Laberinto(columnas, renglones, laberinto).seria(os);
    }

}
