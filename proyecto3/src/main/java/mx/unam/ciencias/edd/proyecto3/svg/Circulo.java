package mx.unam.ciencias.edd.proyecto3.svg;

/**
 * <p>Clase para representar círculos. Un circulo cuenta con un radio.</p>
 */
public class Circulo extends FiguraBasica {

    /* Radio del círculo. */
    protected int radio;

    /**
     * Define el estado inicial de un circulo sin color.
     * @param x la coordenada x del círculo.
     * @param y la coordenada y del círculo.
     * @param radio el radio del círculo.
     */
    public Circulo(int x,
                   int y,
                   int radio) {
        super(x, y);
        this.radio = radio;
    }

    /**
     * Define el estado inicial de un circulo con color.
     * @param x la coordenada x del círculo.
     * @param y la coordenada y del círculo.
     * @param radio el radio del círculo.
     * @param color el color del círculo.
     */
    public Circulo(int x,
                   int y,
                   int radio,
                   String color) {
        super(x, y, color);
        this.radio = radio;
    }

    /**
     * Regresa la coordenada x del círculo.
     * @return la coordenada x del círculo.
     */
    @Override 
    public int getX() {
        return x - radio;
    }

    /**
     * Regresa la coordenada y del círculo.
     * @return la coordenada y del círculo.
     */
    @Override 
    public int getY() {
        return y - radio;
    }

    /**
     * Regresa el ancho de la figura.
     * @return el ancho de la figura.
     */
    @Override 
    public int getAncho() {
        return radio;
    }

    /**
     * Regresa el alto de la figura.
     * @return el alto de la figura.
     */
    @Override 
    public int getAlto() {
        return radio;
    }

    /**
     * Regresa el radio del círculo.
     * @return el radio del círculo.
     */
    public int getRadio() {
        return radio;
    }

    /**
     * Regresa el código SVG de la figura.
     * @return el código SVG de la figura.
     */
    @Override 
    public String toSVG() {
        return String.format("<circle cx='%d' cy='%d' r='%d' fill='%s'/>",
                                getX(), getY(), getRadio(), getColor());
    }
}