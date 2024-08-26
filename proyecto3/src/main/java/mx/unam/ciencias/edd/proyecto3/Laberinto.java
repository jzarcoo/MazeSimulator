package mx.unam.ciencias.edd.proyecto3;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.VerticeGrafica;

/**
 * <p>Clase para representar un laberinto. Un laberinto tiene un número de puertas, columnas, renglones, 
 * un cuarto de entrada, un cuarto de salida, una gráfica y una matriz que representan los cuartos del laberinto.
 * Además, tiene un camino de solución, que es generado por el algoritmo de Dijkstra.</p>
 * 
 * <p>Un laberinto se representa por un archivo binario con el siguiente formato:</p>
 * <ul>
 *      <li>Encabezado de 4 bytes: "MAZE".</li>
 *      <li>2 bytes con las dimensiones del laberinto: renglones y columnas (en ese orden).</li>
 *      <li>Un byte por cada cuarto, con las puertas Este, Norte, Oeste, Sur y el puntaje del cuarto.</li>
 * </ul>
 * 
 * <p>Los laberintos pueden ser serializados por un stream de salida y deserializados por un stream de entrada.</p>
 */
public class Laberinto implements Iterable<Cuarto> {
    
    /* Encabezado del archivo de laberinto. */
    private static final byte[] MAZE = new byte[]{0x4d, 0x41, 0x5a, 0x45};
    /* Tamaño del encabezado del archivo de laberinto. */
    private static final int ENCABEZADO = MAZE.length + 2; // 2 bytes para las dimensiones.
    
    /* Número de puertas en el laberinto. */
    private int puertas;
    /* Columnas del laberinto. */
    private int columnas;
    /* Renglones del laberinto. */
    private int renglones;

    /* Grafica que representa el laberinto. */
    private Grafica<Cuarto> grafica;
    /* Camino del laberinto. */
    private Lista<VerticeGrafica<Cuarto>> camino;
    /* Cuartos del laberinto. */
    private Cuarto[][] cuartos;

    /* Entrada del laberinto. */
    private Cuarto entrada;
    /* Salida del laberinto. */
    private Cuarto salida;

    /**
     * Define el estado inicial del laberinto.
     * Después de llamar a este constructor, se debe llamar a {@link Laberinto#deseria()}.
     */
    public Laberinto() {
        grafica = new Grafica<Cuarto>();
    }

    /**
     * Define el estado inicial del laberinto. 
     * Después de llamar a este constructor, se debe llamar a {@link Laberinto#seria()}.
     * @param columnas las columnas del laberinto.
     * @param renglones los renglones del laberinto.
     * @param cuartos los cuartos del laberinto.
     */
    public Laberinto(int columnas, 
                     int renglones,
                     Cuarto[][] cuartos) {
        this.columnas = columnas;
        this.renglones = renglones;
        this.cuartos = cuartos;
    }

    /**
     * Regresa la entrada del laberinto.
     * @return la entrada del laberinto.
     */
    public Cuarto getEntrada() {
        return entrada;
    }

    /**
     * Regresa la salida del laberinto.
     * @return la salida del laberinto.
     */
    public Cuarto getSalida() {
        return salida;
    }

    /**
     * Regresa el camino de solución del laberinto.
     * @return el camino de solución del laberinto.
     */
    public Iterable<VerticeGrafica<Cuarto>> getCamino() {
        return camino;
    }

    /**
     * Regresa un iterador para los cuartos del laberinto.
     * @return un iterador para los cuartos del laberinto.
     */
    public Iterator<Cuarto> iterator() {
        return grafica.iterator();
    }

    /**
     * Serializa el laberinto. La serialización del laberinto debe ser aceptada
     * por el método {@link Laberinto#deseria()}.
     * @param os el stream de salida donde escribir el laberinto.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    public void seria(OutputStream os) throws IOException{
        seriaEncabezado(os);
        seriaCuartos(os);
    }

    /**
     * Serializa el encabezado del laberinto.
     * @param os el stream de salida donde escribir el encabezado.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    private void seriaEncabezado(OutputStream os) throws IOException {
        os.write(MAZE);
        os.write(renglones);
        os.write(columnas);
        os.flush();
    }

    /**
     * Serializa los cuartos del laberinto.
     * @param os el stream de salida donde escribir los cuartos.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    private void seriaCuartos(OutputStream os) throws IOException {
        for(Cuarto[] fila : cuartos)
            for(Cuarto cuarto : fila)
                os.write(cuarto.seria());
        os.flush();
    }

    /**
     * Deserializa un laberinto. La seriación producida por el método {@link Laberinto#seria()} 
     * debe ser aceptada por este método.
     * @param is el stream de donde leer el laberinto.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    public void deseria(InputStream is) throws IOException {
        procesaEncabezado(is);
        procesaCuartos(is);
        verificaSolucion();
    }

    /**
     * Procesa el encabezado del laberinto.
     * @param is el stream de donde leer el encabezado.
     */
    private void procesaEncabezado(InputStream is) throws IOException {
        byte[] buffer = leeEncabezado(is);
        verificaEncabezado(buffer);
    }

    /**
     * Lee el encabezado del laberinto.
     * @param is el stream de donde leer el encabezado.
     * @return el buffer con el encabezado.
     * @throws IOException si ocurre un error de entrada o salida.
     * @throws ExcepcionLaberintoInvalido si el tamaño del encabezado es incorrecto.
     */
    private byte[] leeEncabezado(InputStream is) throws IOException {
        byte[] buffer = new byte[ENCABEZADO];
        if(is.read(buffer) != ENCABEZADO)
            throw new ExcepcionLaberintoInvalido("Datos insuficientes para el encabezado.");
        return buffer;
    }

    /**
     * Verifica que el encabezado del laberinto sea válido.
     * @param buffer el buffer con el encabezado.
     */
    private void verificaEncabezado(byte[] buffer) {
        verificaTitulo(buffer);
        verificaDimensiones(buffer);
    }

    /**
     * Verifica que el título del laberinto sea válido.
     * @param buffer el buffer con el encabezado.
     * @throws ExcepcionLaberintoInvalido si el título del laberinto es inválido.
     */
    private void verificaTitulo(byte[] buffer) {
        for(int i = 0; i < MAZE.length; i++)
            if(buffer[i] != MAZE[i])
                throw new ExcepcionLaberintoInvalido("Título inválido.");
    }

    /**
     * Verifica que las dimensiones del laberinto sean válidas.
     * @param buffer el buffer con el encabezado.
     * @throws ExcepcionLaberintoInvalido si las dimensiones del laberinto son inválidas.
     */
    private void verificaDimensiones(byte[] buffer) {
        renglones = buffer[MAZE.length] & 0xFF;
        columnas = buffer[MAZE.length + 1] & 0xFF;
        if(columnas < 2 || columnas > 255)
            throw new ExcepcionLaberintoInvalido("Número inválido de columnas.");
        if(renglones < 2 || renglones > 255)
            throw new ExcepcionLaberintoInvalido("Número inválido de renglones.");
    }

    /**
     * Procesa los cuartos del laberinto.
     * @param is el stream de donde leer los cuartos.
     */
    private void procesaCuartos(InputStream is) throws IOException {
        cuartos = new Cuarto[renglones][columnas];
        byte[] cuartosData = leeCuartos(is);
        creaCuartos(cuartosData);
        verificaCuartosExteriores();
        procesaPuertas();
    }

    /**
     * Lee los cuartos del laberinto.
     * @param is el stream de donde leer los cuartos.
     * @return los datos de los cuartos.
     * @throws IOException si ocurre un error de entrada o salida.
     * @throws ExcepcionLaberintoInvalido si el número de cuartos es inválido
     */
    private byte[] leeCuartos(InputStream is) throws IOException {
        byte[] cuartosData = new byte[renglones * columnas];
        if(is.read(cuartosData) != cuartosData.length)
            throw new ExcepcionLaberintoInvalido("Número insuficiente de cuartos.");
        if(is.read() != -1)
            throw new ExcepcionLaberintoInvalido("Número excesivo de cuartos.");
        return cuartosData;
    }

    /**
     * Crea los cuartos del laberinto.
     * @param cuartosData los datos de los cuartos.
     */
    private void creaCuartos(byte[] cuartosData) {
        int i = 0;
        for(int y = 0; y < renglones; y++)
            for(int x = 0; x < columnas; x++) {
                byte b = cuartosData[i++];
                Cuarto cuarto = new Cuarto(x, y, b);
                cuartos[y][x] = cuarto;
                grafica.agrega(cuarto);
                if(cuarto.isFrontera(columnas, renglones) && 
                   cuarto.tienePuertaExterior(columnas, renglones))
                    setCuartoExterior(cuarto);
                puertas += cuarto.getNumeroPuertas();
            }
        // Calcula el número de puertas.
        // Cada puerta se cuenta dos veces.
        // Además, la entrada y salida tienen una puerta cada una.
        puertas = (puertas - 2) >>> 1;
    }

    /**
     * Establece un cuarto como cuarto exterior.
     * @param cuarto el cuarto a establecer como cuarto exterior.
     * @throws ExcepcionLaberintoInvalido si hay más de dos cuartos en la frontera.
     */
    private void setCuartoExterior(Cuarto cuarto) {
        if(entrada == null)
            entrada = cuarto;
        else if(salida == null)
            salida = cuarto;
        else
            throw new ExcepcionLaberintoInvalido("Más de dos cuartos en la frontera.");
    }

    /**
     * Verifica que los cuartos exteriores del laberinto sean válidos.
     * @throws ExcepcionLaberintoInvalido si no se ingresó un cuarto de entrada o salida.
     */
    private void verificaCuartosExteriores() {
        if(entrada == null)
            throw new ExcepcionLaberintoInvalido("No hay cuarto de entrada.");
        if(salida == null)
            throw new ExcepcionLaberintoInvalido("No hay cuarto de salida.");
    }

    /**
     * Procesa las puertas del laberinto.
     * @throws ExcepcionLaberintoInvalido si los cuartos son inconsistentes en sus puertas.
     */
    private void procesaPuertas() {
        int puertasCreadas = creaPuertas();
        if(puertasCreadas != puertas)
            throw new ExcepcionLaberintoInvalido("Los cuartos son inconsistentes en sus puertas.");
    }

    /**
     * Crea las puertas del laberinto.
     * @return el número de puertas creadas.
     */
    private int creaPuertas() {
        int c = 0;
        for(Cuarto cuarto : grafica) {
            if(!cuarto.hayParedEste())
                c += conectaCuarto(cuarto, Direccion.ESTE);
            if(!cuarto.hayParedNorte())
                c += conectaCuarto(cuarto, Direccion.NORTE);
            if(!cuarto.hayParedOeste())
                c += conectaCuarto(cuarto, Direccion.OESTE);
            if(!cuarto.hayParedSur())
                c += conectaCuarto(cuarto, Direccion.SUR);
        }
        return c;
    }

    /**
     * Conecta un cuarto con otro.
     * El puntaje de la conexión es la suma de los puntajes de los cuartos más uno.
     * @param cuarto el cuarto a conectar.
     * @param direccion la dirección a la que conectar.
     * @return 1 si la conexión ya existía, 0 en otro caso.
     */
    private int conectaCuarto(Cuarto cuarto, Direccion direccion) {
        int x = cuarto.getX() + direccion.getCambioColumna();
        int y = cuarto.getY() + direccion.getCambioRenglon();
        if(x < 0 || x >= columnas || y < 0 || y >= renglones)
            return 0;
        Cuarto vecino = cuartos[y][x];
        int puntaje = 1 + cuarto.getPuntaje() + vecino.getPuntaje();
        try {
            grafica.conecta(cuarto, vecino, puntaje);
            return 0;
        }catch(IllegalArgumentException iae) {
            return 1;
        }
    }

    /**
     * Verifica que el laberinto tenga solución.
     * @throws ExcepcionLaberintoInvalido si el laberinto no tiene solución.
     */
    private void verificaSolucion() {
        camino = grafica.dijkstra(entrada, salida);
        if(camino.esVacia())
            throw new ExcepcionLaberintoInvalido("El laberinto no tiene solución.");
    }

}