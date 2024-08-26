package mx.unam.ciencias.edd.proyecto3;

/**
 * <p>Clase para excepciones de laberintos inválidos.</p>
 */
public class ExcepcionLaberintoInvalido extends IllegalArgumentException {

    /**
     * Constructor vacío.
     */
    public ExcepcionLaberintoInvalido() {}

    /**
     * Constructor que recibe un mensaje para el usuario.
     * @param mensaje un mensaje que verá el usuario cuando ocurra la excepción.
     */
    public ExcepcionLaberintoInvalido(String mensaje) {
        super(mensaje);
    }
}
