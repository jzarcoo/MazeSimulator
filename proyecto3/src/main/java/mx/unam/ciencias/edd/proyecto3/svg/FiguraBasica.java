package mx.unam.ciencias.edd.proyecto3.svg;

/**
 * <p>Clase abstracta para representar figuras basicas en SVG. Una figura
 * tiene una coordenada x y una coordenada y, asimismo cuenta con un color. 
 * En caso de no recibir un color, la figura define al color negro por 
 * defecto.</p>
 * 
 * <p>Las figuras basicas implementan la interfaz {@link Figura}, y 
 * por tanto, define a los siguientes métodos:</p>
 * <ul>
 *      <li>{@link #getX()}</li>
 *      <li>{@link #getY()}</li>
 *      <li>{@link #getAncho()}</li>
 *      <li>{@link #getAlto()}</li>
 *      <li>{@link #toSVG()}</li>
 * </ul>
 */
public abstract class FiguraBasica implements Figura {

    /* Coordenada x de la figura. */
    protected int x;
    /* Coordenada y de la figura. */
    protected int y;
    /* Color de la figura. */
    protected String color;

    /**
     * Define el estado inicial de una figura basica sin color.
     * @param x la coordenada x de la figura.
     * @param y la coordenada y de la figura.
     */
    public FiguraBasica(int x, 
                        int y) {
        this.x = x;
        this.y = y;
        this.color = "black";
    }

    /**
     * Define el estado inicial de una figura básica con color.
     * @param x la coordenada x de la figura.
     * @param y la coordenada y de la figura.
     * @param color el color de la figura.
     */
    public FiguraBasica(int x,
                        int y,
                        String color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * Regresa la coordenada x de la figura.
     * @return la coordenada x de la figura.
     */
    @Override 
    public int getX() {
        return x;
    }

    /**
     * Regresa la coordenada y de la figura.
     * @return la coordenada y de la figura.
     */
    @Override 
    public int getY() {
        return y;
    }

    /**
     * Regresa el ancho de la figura.
     * @return el ancho de la figura.
     */
    @Override 
    public int getAncho() {
        return 0;
    }

    /**
     * Regresa el alto de la figura.
     * @return el alto de la figura.
     */
    @Override 
    public int getAlto() {
        return 0;
    }

    /**
     * Regresa el color de la figura.
     * @return el color de la figura.
     */
    @Override
    public String getColor() {
        return color;
    }

    /**
     * Regresa el código SVG de la figura.
     * @return el código SVG de la figura.
     */
    @Override 
    public abstract String toSVG();
}