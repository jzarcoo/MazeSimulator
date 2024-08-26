package mx.unam.ciencias.edd.proyecto3.svg;

/**
 * <p>Clase para representar rectangulos en SVG. Un rectángulo cuenta con un ancho y un alto.</p>
 */
public class Rectangulo extends FiguraBasica {

    /* Ancho del rectangulo. */
    protected int ancho;
    /* Alto del rectangulo. */
    protected int alto;
    
    /**
     * Define el estado inicial de un rectangulo sin color.
     * @param x la coordenada x del rectangulo.
     * @param y la coordenada y del rectangulo.
     * @param ancho el ancho del rectangulo.
     * @param alto el alto del rectangulo.
     * @param color el color del rectangulo.
     */
    public Rectangulo(int x,
                      int y,
                      int ancho,
                      int alto) {
        super(x, y);
        this.ancho = ancho;
        this.alto = alto;
    }
    
    /**
     * Define el estado inicial de un rectangulo con color.
     * @param x la coordenada x del rectangulo.
     * @param y la coordenada y del rectangulo.
     * @param ancho el ancho del rectangulo.
     * @param alto el alto del rectangulo.
     * @param color el color del rectangulo.
     */
    public Rectangulo(int x,
                      int y,
                      int ancho,
                      int alto,
                      String color) {
        super(x, y, color);
        this.ancho = ancho;
        this.alto = alto;
    }

    /**
     * Regresa el ancho de la figura.
     * @return el ancho de la figura.
     */
    @Override
    public int getAncho() {
        return ancho;
    }

    /**
     * Regresa el alto de la figura.
     * @return el alto de la figura.
     */
    @Override
    public int getAlto() {
        return alto;
    }

    /**
     * Regresa el código SVG de la figura.
     * @return el código SVG de la figura.
     */
    @Override
    public String toSVG() {
        return String.format("<rect x='%d' y='%d' width='%d' height='%d' fill='%s'/>",
                                getX(), getY(), getAncho(), getAlto(), getColor());
    }
}