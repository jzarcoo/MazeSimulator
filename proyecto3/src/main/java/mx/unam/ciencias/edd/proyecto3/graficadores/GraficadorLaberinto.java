package mx.unam.ciencias.edd.proyecto3.graficadores;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.VerticeGrafica;
import mx.unam.ciencias.edd.proyecto3.Cuarto;
import mx.unam.ciencias.edd.proyecto3.Direccion;
import mx.unam.ciencias.edd.proyecto3.Laberinto;
import mx.unam.ciencias.edd.proyecto3.svg.Circulo;
import mx.unam.ciencias.edd.proyecto3.svg.GraficadorSVG;
import mx.unam.ciencias.edd.proyecto3.svg.Linea;

/**
 * <p>Clase para representar un graficador de laberintos. Un graficador de laberintos
 * cuenta con un laberinto y un graficador SVG.</p>
 */
public class GraficadorLaberinto {

    /* Tamaño del ancho y alto de un cuarto */
    private static final int TAMANO_CUARTO = 50;
    /* Radio para la entrada y salida del laberinto. */
    private static final int RADIO_CUARTO = 20;
    /* Grosor del camino del laberinto. */
    private static final int GROSOR_CAMINO = 15;
    /* Grosor de la pared del laberinto. */
    private static final int GROSOR_PARED = 5;

    /* Color de la pared del laberinto */
    private static final String COLOR_PARED = "black";
    /* Color del camino del laberinto. */
    private static final String COLOR_CAMINO = "green";
    /* Color de la entrada del laberinto. */
    private static final String COLOR_ENTRADA = "red";
    /* Color de la salida del laberinto. */
    private static final String COLOR_SALIDA = "blue";

    /* Graficador SVG */
    private GraficadorSVG graficadorSVG;
    /* Laberinto a graficar */
    private Laberinto laberinto;

    /**
     * Define el estado inicial del graficador del laberinto.
     * @param laberinto Laberinto a graficar.
     */
    public GraficadorLaberinto(Laberinto laberinto) {
        graficadorSVG = new GraficadorSVG();
        this.laberinto = laberinto;
    }

    /**
     * Grafica el laberinto.
     * @return SVG del laberinto.
     */
    public String graficaLaberinto() {
        graficaParedes();
        graficaCamino();
        graficaEntrada();
        graficaSalida();
        return graficadorSVG.toSVG();
    }

    /**
     * Grafica las paredes del laberinto.
     */
    private void graficaParedes() {
        for(Cuarto cuarto : laberinto) {
            int x = cuarto.getX() * TAMANO_CUARTO;
            int y = cuarto.getY() * TAMANO_CUARTO;
            if(cuarto.hayParedEste())
                graficadorSVG.agrega(creaPared(x + TAMANO_CUARTO, y, x + TAMANO_CUARTO, y + TAMANO_CUARTO));
            if(cuarto.hayParedNorte())
                graficadorSVG.agrega(creaPared(x, y, x + TAMANO_CUARTO, y));
            if(cuarto.hayParedOeste())
                graficadorSVG.agrega(creaPared(x, y, x, y + TAMANO_CUARTO));
            if(cuarto.hayParedSur())
                graficadorSVG.agrega(creaPared(x, y + TAMANO_CUARTO, x + TAMANO_CUARTO, y + TAMANO_CUARTO));
        }
    }

    /**
     * Crea una pared en el laberinto.
     * @param x Coordenada x de la pared.
     * @param y Coordenada y de la pared.
     * @param x2 Coordenada x2 de la pared.
     * @param y2 Coordenada y2 de la pared.
     * @return Pared del laberinto.
     */
    private Linea creaPared(int x, int y, int x2, int y2) {
        return new Linea(x, y, x2, y2, GROSOR_PARED, COLOR_PARED);
    }

    /**
     * Grafica el camino del laberinto.
     */
    private void graficaCamino() {
        int xAnterior = 0;
        int yAnterior = 0;
        Iterable<VerticeGrafica<Cuarto>> camino = laberinto.getCamino();
        if(camino == null)
            return;
        for(VerticeGrafica<Cuarto> vertice : camino) {
            Cuarto cuarto = vertice.get();
            int x = cuarto.getX() * TAMANO_CUARTO + RADIO_CUARTO + GROSOR_PARED;
            int y = cuarto.getY() * TAMANO_CUARTO + RADIO_CUARTO + GROSOR_PARED;
            if(xAnterior != 0 || yAnterior != 0)
                graficadorSVG.agrega(creaCamino(x, y, xAnterior, yAnterior));
            xAnterior = x;
            yAnterior = y;
        }
    }    

    /**
     * Crea un camino en el laberinto.
     * @param x Coordenada x del camino.
     * @param y Coordenada y del camino.
     * @param x2 Coordenada x2 del camino.
     * @param y2 Coordenada y2 del camino.
     * @return Camino del laberinto.
     */
    private Linea creaCamino(int x, int y, int x2, int y2) {
        return new Linea(x, y, x2, y2, GROSOR_CAMINO, COLOR_CAMINO);
    }

    /**
     * Grafica la entrada del laberinto.
     */
    private void graficaEntrada() {
        Cuarto entrada = laberinto.getEntrada();
        if(entrada == null)
            return;
        int x = (entrada.getX() * TAMANO_CUARTO) + TAMANO_CUARTO - GROSOR_PARED;
        int y = (entrada.getY() * TAMANO_CUARTO) + TAMANO_CUARTO - GROSOR_PARED;
        graficadorSVG.agrega(creaEntrada(x, y));
    }

    /**
     * Grafica la salida del laberinto.
     */
    private void graficaSalida() {
        Cuarto salida = laberinto.getSalida();
        if(salida == null)
            return;
        int x = (salida.getX() * TAMANO_CUARTO) + TAMANO_CUARTO - GROSOR_PARED;
        int y = (salida.getY() * TAMANO_CUARTO) + TAMANO_CUARTO - GROSOR_PARED;
        graficadorSVG.agrega(creaSalida(x, y));
    }

    /**
     * Crea una entrada en el laberinto.
     * @param x Coordenada x de la entrada.
     * @param y Coordenada y de la entrada.
     * @return Entrada del laberinto.
     */
    private Circulo creaEntrada(int x, int y) {
        return new Circulo(x, y, RADIO_CUARTO, COLOR_ENTRADA);
    }

    /**
     * Crea una salida en el laberinto.
     * @param x Coordenada x de la salida.
     * @param y Coordenada y de la salida.
     * @return Salida del laberinto.
     */
    private Circulo creaSalida(int x, int y) {
        return new Circulo(x, y, RADIO_CUARTO, COLOR_SALIDA);
    }

}
