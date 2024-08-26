package mx.unam.ciencias.edd.proyecto3;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import mx.unam.ciencias.edd.proyecto3.algoritmos.FabricaSimple;
import mx.unam.ciencias.edd.proyecto3.algoritmos.GeneradorLaberinto;
import mx.unam.ciencias.edd.proyecto3.graficadores.GraficadorLaberinto;

/**
 * <p>Proyecto 3: Estructuras de datos.</p>
 * 
 * <p>El programa es capaz de generar y resolver laberintos.</p>
 * 
 * <p>Los laberintos son rectangulares y formados por cuartos cuadrados, con puertas Este, Norte, Oeste y Sur. 
 * Cada puerta tiene un puntaje entre 1 y 31; al pasar una puerta que conecte dos cuartos, el explorador del 
 * laberinto sufre una penalización proporcional al puntaje: por lo tanto, nos interesa que el explorador 
 * minimice el puntaje de las puertas por las que pase.</p>
 */
public class Proyecto3 {
    
    /* Codigo de terminacion por error de uso. */
    private static final int ERROR_USO = 1;
	/* Codigo de terminacion por error de lectura. */
	private static final int ERROR_LECTURA = 2;
    
    /* Imprime en pantalla como debe usarse el programa y lo termina. */
    private static void uso() {
      	System.out.println("Uso para generar laberinto: java -jar target/proyecto3.jar -g [-s <semilla>] -w <columnas> -h <renglones>");
      	System.out.println("Uso para resolver laberinto: java -jar target/proyecto3.jar [<archivo>]");
      	System.exit(ERROR_USO);
    }

    /**
     * Método principal del programa.
     * @param args los argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
		if(args.length > 1)
			generaLaberinto(args);
		else
			resuelveLaberinto(args);
	}

	/**
	 * Resuelve un laberinto.
	 * @param args los argumentos de la línea de comandos.
	 */
	private static void resuelveLaberinto(String[] args) {
		Laberinto laberinto = new Laberinto();
		try{
			InputStream is = (args.length == 0) ? System.in : new FileInputStream(args[0]);
			laberinto.deseria(is);
		}catch(IOException ioe) {
			System.err.println("Error de lectura: " + ioe.getMessage() + "\n");
			System.exit(ERROR_LECTURA);
		}catch(ExcepcionLaberintoInvalido eli) {
			System.err.println(eli.getMessage() + "\n");
			uso();
		}
		GraficadorLaberinto graficadorLaberinto = new GraficadorLaberinto(laberinto);
		System.out.println(graficadorLaberinto.graficaLaberinto());
	}

	/**
	 * Genera un laberinto.
	 * @param args los argumentos de la línea de comandos.
	 */
	private static void generaLaberinto(String[] args) {
		try {
            EntradaEstandar entradaEstandar = new EntradaEstandar(args);
            GeneradorLaberinto generadorLaberinto = FabricaSimple.creaGeneradorLaberinto(entradaEstandar);
			generadorLaberinto.creaLaberinto();
            generadorLaberinto.generaLaberinto(System.out);
        } catch (ExcepcionLaberintoInvalido eli) {
            System.err.println(eli.getMessage() + "\n");
            uso();
        } catch (IOException ioe) {
            System.err.println("Error de lectura: " + ioe.getMessage() + "\n");
            System.exit(ERROR_LECTURA);
        }
	}
	
}
