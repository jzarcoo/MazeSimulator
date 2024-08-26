package mx.unam.ciencias.edd.proyecto3.svg;

/**
 * <p>Clase para representar puntos en SVG.</p>
 */
public class Punto extends FiguraBasica {
    
    /**
     * Define el estado inicial de un punto sin color.
     * @param x la coordenada x del punto.
     * @param y la coordenada y del punto.
     */
    public Punto(int x,
                 int y) {
        super(x, y);
    }
    
    /**
     * Define el estado inicial de un punto con color.
     * @param x la coordenada x del punto.
     * @param y la coordenada y del punto.
     * @param color el color del punto.
     */
    public Punto(int x,
                 int y,
                 String color) {
        super(x, y, color);
    }

    /**
     * Regresa el código SVG del punto.
     * @return el código SVG del punto.
     */
    @Override
    public String toSVG() {
        return String.format("<circle cx='%d' cy='%d' r='1' fill='%s'/>",
                              getX(), getY(), getColor());
    }
}