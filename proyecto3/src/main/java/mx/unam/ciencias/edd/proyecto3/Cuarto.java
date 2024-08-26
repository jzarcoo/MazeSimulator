package mx.unam.ciencias.edd.proyecto3;

/**
 * <p>Clase para representar un cuarto en un laberinto. Un cuarto tiene una
 * coordenada x, y, un puntaje y paredes en las direcciones Este, Norte, Oeste
 * y Sur.</p>
 * 
 * <p>Los 4 bits menos significativos del byte de cada cuarto representan las
 * paredes que tiene el cuarto con sus 4 potenciales vecinos, en orden de menos
 * a más significativo, Este, Norte, Oeste y Sur; el bit en 1 quiere decir que
 * la pared sí existe (o que la puerta no existe), el bit en 0 quiere decir
 * que la pared no existe (o que la puerta sí existe).</p>
 * 
 * <p>Los 4 bits más significativos del byte de cada cuarto representan el puntaje
 * del cuarto (no de la puerta). Los valores 0000−1111, que corresponden a
 * 0−15 en decimal serán el puntaje de cada cuarto.</p>
 * 
 * <p>Un cuarto puede serializarse y deserializarse en un byte.</p>
 */
public class Cuarto {

    /* Coordenada x del cuarto. */
    private int x;
    /* Coordenada y del cuarto. */
    private int y;
    /* Puntaje del cuarto. */
    private int puntaje;
    /* Pared Este del cuarto. */
    private boolean paredEste;
    /* Pared Norte del cuarto. */
    private boolean paredNorte;
    /* Pared Oeste del cuarto. */
    private boolean paredOeste;
    /* Pared Sur del cuarto. */
    private boolean paredSur;

    /**
     * Define el estado inicial de un cuarto.
     * @param x la coordenada x del cuarto.
     * @param y la coordenada y del cuarto.
     */
    public Cuarto(int x,
                  int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Define el estado inicial de un cuarto.
     * @param x la coordenada x del cuarto.
     * @param y la coordenada y del cuarto.
     * @param paredEste la pared Este del cuarto.
     * @param paredNorte la pared Norte del cuarto.
     * @param paredOeste la pared Oeste del cuarto.
     * @param paredSur la pared Sur del cuarto.
     */
    public Cuarto(int x, 
                    int y, 
                    boolean paredEste,
                    boolean paredNorte,
                    boolean paredOeste,
                    boolean paredSur) {
          this(x, y);
          this.paredEste = paredEste;
          this.paredNorte = paredNorte;
          this.paredOeste = paredOeste;
          this.paredSur = paredSur;
    }    

    /**
     * Define el estado inicial de un cuarto.
     * @param x la coordenada x del cuarto.
     * @param y la coordenada y del cuarto.
     * @param b la información del cuarto.
     */
    public Cuarto(int x, 
                  int y, 
                  byte b) {
        this.x = x;
        this.y = y;
        deseria(b);
    }

    /**
     * Regresa el número de puertas que tiene el cuarto.
     * @return el número de puertas que tiene el cuarto.
     */
    public int getNumeroPuertas() {
        int n = 0;
        if (!paredEste) n++;
        if (!paredNorte) n++;
        if (!paredOeste) n++;
        if (!paredSur) n++;
        return n;
    }

    /**
     * Nos dice si un cuarto está en el borde del laberinto.
     * @param columnas el número de columnas del laberinto.
     * @param renglones el número de renglones del laberinto.
     * @return <code>true</code> si el cuarto está en el borde del laberinto,
     *       <code>false</code> en otro caso.
     */
    public boolean isFrontera(int columnas, int renglones) {
        return getX() == 0 || getY() == 0 || getX() == columnas - 1 || getY() == renglones - 1;
    }

    /**
     * Nos dice si el cuarto tiene una puerta exterior, es decir, si el cuarto
     * está en el borde del laberinto y no tiene pared.
     * @param columnas el número de columnas del laberinto.
     * @param renglones el número de renglones del laberinto.
     * @return <code>true</code> si el cuarto tiene una puerta exterior,
     *        <code>false</code> en otro caso.
     */
    public boolean tienePuertaExterior(int columnas, int renglones) {
        return (getX() == 0 && !paredOeste) ||
               (getY() == 0 && !paredNorte) ||
               (getX() == columnas - 1 && !paredEste) ||
               (getY() == renglones - 1 && !paredSur);
    }

    /**
     * Regresa el puntaje del cuarto.
     * @return el puntaje del cuarto.
     */
    public int getPuntaje() {
        return puntaje;
    }

    /**
     * Define el puntaje del cuarto.
     * @param puntaje el nuevo puntaje del cuarto.
     * @throws IllegalArgumentException si el puntaje no es un número entre 0 y 15.
     */
    public void setPuntaje(int puntaje) {
        if(puntaje < 0 || puntaje > 15) throw new IllegalArgumentException("El puntaje debe ser un número entre 0 y 15.");
        this.puntaje = puntaje;
    }

    /**
     * Nos dice si hay una pared en la dirección este.
     * @return <code>true</code> si hay una pared en la dirección este,
     *       <code>false</code> en otro caso.
     */
    public boolean hayParedEste() {
        return paredEste;
    }
    
    /**
     * Define la pared Este del cuarto.
     * @param paredEste la nueva pared Este del cuarto.
     */
    public void setParedEste(boolean paredEste) {
        this.paredEste = paredEste;
    }

    /**
     * Nos dice si hay una pared en la dirección norte.
     * @return <code>true</code> si hay una pared en la dirección norte,
     *       <code>false</code> en otro caso.
     */
    public boolean hayParedNorte() {
        return paredNorte;
    }

    /**
     * Define la pared Norte del cuarto.
     * @param paredNorte la nueva pared Norte del cuarto.
     */
    public void setParedNorte(boolean paredNorte) {
        this.paredNorte = paredNorte;
    }

    /**
     * Nos dice si hay una pared en la dirección oeste.
     * @return <code>true</code> si hay una pared en la dirección oeste,
     *       <code>false</code> en otro caso.
     */
    public boolean hayParedOeste() {
        return paredOeste;
    }

    /**
     * Define la pared Oeste del cuarto.
     * @param paredOeste la nueva pared Oeste del cuarto.
     */
    public void setParedOeste(boolean paredOeste) {
        this.paredOeste = paredOeste;
    }

    /**
     * Nos dice si hay una pared en la dirección sur.
     * @return <code>true</code> si hay una pared en la dirección sur,
     *       <code>false</code> en otro caso.
     */
    public boolean hayParedSur() {
        return paredSur;
    }

    /**
     * Define la pared Sur del cuarto.
     * @param paredSur la nueva pared Sur del cuarto.
     */
    public void setParedSur(boolean paredSur) {
        this.paredSur = paredSur;
    }

    /**
     * Regresa la coordenada x del cuarto.
     * @return la coordenada x del cuarto.
     */
    public int getX() {
        return x;
    }

    /**
     * Regresa la coordenada y del cuarto.
     * @return la coordenada y del cuarto.
     */
    public int getY() {
        return y;
    }

    /**
     * Nos dice si el cuarto se encuentra en la misma posición que el 
     * que manda a llamar el método.
     * @param cuarto el cuarto con el que se comparará la posición.
     * @return <code>true</code> si el cuarto se encuentra en la misma
     *        posición que el que manda a llamar el método, <code>false</code>
     *       en otro caso.
     */
    public boolean mismaPosicionCon(Cuarto cuarto) {
        return x == cuarto.getX() && y == cuarto.getY();
    }

    /**
     * Deserializa un byte para obtener la información del cuarto. La
     * serialización producida por el método {@link Cuarto#seria()} debe ser
     * aceptada por este método.
     * @param b el byte a deserializar.
     */
    public void deseria(byte b) {
        // 4 bits menos significativos del byte
        paredEste = (b & 1) == 1;
        paredNorte = (b & 2) == 2;
        paredOeste = (b & 4) == 4;
        paredSur = (b & 8) == 8;
        // 4 bits más significativos del byte
        puntaje = (b >> 4) & 0xF;
    }

    /**
     * Serializa la información del cuarto en un byte. La serialización
     * producida por este método debe ser aceptada por el método
     * {@link Cuarto#deseria()}.
     * @return la información del cuarto serializada en un byte.
     */
    public byte seria() {
        byte b = 0;
        // 4 bits menos significativos del byte
        if (paredEste) b |= 1;
        if (paredNorte) b |= 2;
        if (paredOeste) b |= 4;
        if (paredSur) b |= 8;
        // 4 bits más significativos del byte
        b |= puntaje << 4;
        return b;
    }

    /**
     * Regresa una representación en cadena del cuarto.
     * @return una representación en cadena del cuarto.
     */
    @Override
    public String toString() {
        return new StringBuilder()
            .append("Cuarto{x=").append(x)
            .append(", y=").append(y)
            .append(", puntaje=").append(puntaje)
            .append(", paredEste=").append(paredEste)
            .append(", paredNorte=").append(paredNorte)
            .append(", paredOeste=").append(paredOeste)
            .append(", paredSur=").append(paredSur)
            .append("}")
            .toString();
    }

}