package mx.unam.ciencias.edd.proyecto3.svg;

/**
 * <p>Clase para representar lineas en SVG. Una Linea cuenta con un grosor, una coordenada x y y
 * del punto final de la línea.</p>
 */
public class Linea extends FiguraBasica {

    /* Coordenada x del punto final de la linea. */
    protected int x2;
    /* Coordenada y del punto final de la linea. */
    protected int y2;
    /* Grosor de la linea */
    protected int grosor;

    /**
     * Define el estado inicial de una linea sin color.
     * @param x1 la coordenada x del punto de inicio de la linea.
     * @param y1 la coordenada y del punto de inicio de la linea.
     * @param x2 la coordenada x del punto final de la linea.
     * @param y2 la coordenada y del punto final de la linea.
     * @param grosor el grosor de la línea.
     */
    public Linea(int x1, 
                 int y1, 
                 int x2, 
                 int y2,
                 int grosor) {
        super(x1, y1);
        this.x2 = x2;
        this.y2 = y2;
        this.grosor = grosor;
    }

    /**
     * Define el estado inicial de una linea con color.
     * @param x1 la coordenada x del punto de inicio de la linea.
     * @param y1 la coordenada y del punto de inicio de la linea.
     * @param x2 la coordenada x del punto final de la linea.
     * @param y2 la coordenada y del punto final de la linea.
     * @param grosor el grosor de la línea.
     * @param color el color de la linea.
     */
    public Linea(int x1, 
                 int y1, 
                 int x2, 
                 int y2,
                 int grosor,
                 String color) {
        super(x1, y1, color);
        this.x2 = x2;
        this.y2 = y2;
        this.grosor = grosor;
    }

    /**
     * Regresa la coordenada x de la línea.
     * @return la coordenada x de la línea.
     */
    @Override 
    public int getX() {
        return Math.min(x, x2);
    }

    /**
     * Regresa la coordenada y de la línea.
     * @return la coordenada y de la línea.
     */
    @Override 
    public int getY() {
        return Math.min(y, y2);
    }

    /**
     * Regresa el ancho de la figura.
     * @return el ancho de la figura.
     */
    @Override
    public int getAncho() {
        return Math.abs(x - x2);
    }

    /**
     * Regresa el alto de la figura.
     * @return el alto de la figura.
     */
    @Override
    public int getAlto() {
        return Math.abs(y - y2);
    }

    /**
     * Regresa el grosor de la linea.
     * @return el grosor de la linea.
     */
    public int getGrosor() {
        return grosor;
    }

    /**
     * Regresa la coordenada x del punto final de la linea.
     * @return la coordenada x del punto final de la linea.
     */
    public int getX2() {
        return x2;
    }

    /**
     * Regresa la coordenada y del punto final de la linea.
     * @return la coordenada y del punto final de la linea.
     */
    public int getY2() {
        return y2;
    }

    /**
     * Regresa el código SVG de la linea.
     * @return el código SVG de la linea.
     */
    @Override
    public String toSVG() {
        return String.format("<line x1='%d' y1='%d' x2='%d' y2='%d' stroke='%s' stroke-width='%d' />",
                                x, y, getX2(), getY2(), getColor(), getGrosor());
    }

}