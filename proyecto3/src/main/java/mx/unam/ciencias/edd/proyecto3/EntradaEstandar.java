package mx.unam.ciencias.edd.proyecto3;

/**
 * <p>Clase para manejar la entrada estándar del programa. La entrada estándar
 * se compone de banderas y valores que definen la generación de un laberinto, tales como:
 * la semilla, las columnas y los renglones del laberinto.</p>
 * 
 * <p>Las banderas que se deben ingresar son:</p>
 * <ul>
 *      <li>-g: para generar un laberinto.</li>
 *      <li>-w: para ingresar el número de columnas del laberinto.</li>
 *      <li>-h: para ingresar el número de renglones del laberinto.</li>
 *      <li>-s: para ingresar la semilla del laberinto.</li>
 * </ul>
 * 
 * <p>La bandera -s es opcional, si no se ingresa, se asigna la semilla actual (el reloj de la computadora).</p>
 */
public class EntradaEstandar {

    /**
     * <p>Enumeracion para representar las banderas de la aplicacion de laberintos.</p>
     */
    private enum Bandera {

        /* Modo para generar. */
        GENERA("-g"),
        /* Semilla para generar. */
        SEMILLA("-s"),
        /* Columnas del laberinto. */
        COLUMNAS("-w"),
        /* Reglones del laberinto. */
        RENGLONES("-h");

        /* Nombre de la bandera. */
        private String nombre;

        /**
         * Define el estado inicial de la bandera.
         * @param nombre el nombre de la bandera.
         */
        private Bandera(String nombre) {
            this.nombre = nombre;
        }

        /**
         * Descifra una bandera a partir de su nombre.
         * @param nombre el nombre de la bandera.
         * @return la bandera correspondiente al nombre o,
         *          <code>null</code> si no existe.
         * @throws ExcepcionLaberintoInvalido si la bandera no existe.
         */
        public static Bandera getBandera(String nombre) {
            for(Bandera b : Bandera.values())
                if(b.nombre.equals(nombre))
                    return b;
            throw new ExcepcionLaberintoInvalido("Bandera inválida: " + nombre);
        }

    }

    /* Nos dice si estamos generando un laberinto. */
    private boolean isGenerando;
    /* Semilla del laberinto. */
    private long semilla;
    /* Columnas del laberinto. */
    private int columnas;
    /* Renglones del laberinto. */
    private int renglones;

    /**
     * Define el estado inicial de la entrada estándar.
     * @param args los argumentos de la línea de comandos.
     */
    public EntradaEstandar(String[] args) {
        int i = -1;
        while(++i < args.length) {
            Bandera b = Bandera.getBandera(args[i]);
            switch (b) {
                case GENERA:
                    procesaGenera();
                    break;
                case SEMILLA:
                    procesaSemilla(args, ++i);
                    break;
                case COLUMNAS:
                    procesaColumnas(args, ++i);
                    break;
                case RENGLONES:
                    procesaRenglones(args, ++i);
                    break;
            }
        }
        verificaEntrada();
    }

    /**
     * Procesa la bandera GENERA.
     * @throws ExcepcionLaberintoInvalido si ya se está generando un laberinto.
     */
    private void procesaGenera() {
        if(isGenerando)
            throw new ExcepcionLaberintoInvalido("No puedes generar dos laberintos al mismo tiempo.");
        isGenerando = true;
    }

    /**
     * Procesa la bandera SEMILLA.
     * @param args el arreglo de cadenas de la línea de comandos.
     * @param i el índice actual del arreglo de cadenas donde se encuentra la semilla.
     */
    private void procesaSemilla(String[] args, int i) {
        if(semilla != 0)
            throw new ExcepcionLaberintoInvalido("No puedes ingresar dos veces la semilla.");
        if(i >= args.length)
            throw new ExcepcionLaberintoInvalido("Debes ingresar un valor para la semilla.");
        semilla = verificaLong(args[i], "la semilla");
    }

    /**
     * Procesa la bandera COLUMNAS.
     * @param args el arreglo de cadenas de la línea de comandos.
     * @param i el índice actual del arreglo de cadenas donde se encuentra el número de columnas.
     */
    private void procesaColumnas(String[] args, int i) {
        columnas = verificaDimension(args, 
                                i, 
                                "el número de columnas", 
                                columnas);
    }

    /**
     * Procesa la bandera RENGLONES.
     * @param args el arreglo de cadenas de la línea de comandos.
     * @param i el índice actual del arreglo de cadenas donde se encuentra el número de renglones.
     */
    private void procesaRenglones(String[] args, int i) {
        renglones = verificaDimension(args, 
                                i, 
                                "el número de renglones", 
                                renglones);
    }

    /** 
     * Verifica que el valor de una dimensión del laberinto sea válida y la regresa.
     * @param args el arreglo de cadenas de la línea de comandos.
     * @param i el índice actual del arreglo de cadenas donde se encuentra el valor.
     * @param campo el campo al que pertenece el valor.
     * @param actual el valor actual del campo.
     * @return el valor de la dimensión si es válido.
     * @throws ExcepcionLaberintoInvalido si:
     *         <ul>
     *              <li>Ya se ingresó un valor para la dimensión.</li>
     *              <li>No se ingresó un valor para la dimensión.</li>
     *              <li>El valor de la dimensión es menor a 2 o mayor a 255.</li>
     *        </ul>
     */
    private int verificaDimension(String[] args, int i, String campo, int actual) {
        if(actual != 0)
            throw new ExcepcionLaberintoInvalido("No puedes ingresar dos veces el valor de " + campo + ".");
        if(i >= args.length)
            throw new ExcepcionLaberintoInvalido("Debes ingresar un valor para " + campo + ".");
        int n = verificaEntero(args[i], campo);
        if(n < 2 || n > 255)
            throw new ExcepcionLaberintoInvalido("Valor inválido para " + campo + ".");
        return n;
    }

    /**
     * Verifica que la cadena sea un entero y lo regresa.
     * @param entero el entero a verificar.
     * @param campo el campo al que pertenece el entero.
     * @return el entero si es válido.
     * @throws ExcepcionLaberintoInvalido si la cadena no es un entero.
     */
    private int verificaEntero(String entero, String campo) {
        try{
            return Integer.parseInt(entero);
        }catch(NumberFormatException nfe) {
            throw new ExcepcionLaberintoInvalido("Debes ingresar un número entero para " + campo + ".");
        }
    }

    /**
     * Verifica que la cadena sea un long y lo regresa.
     * @param n el long a verificar.
     * @param campo el campo al que pertenece el long.
     * @return el long si es válido.
     * @throws ExcepcionLaberintoInvalido si la cadena no es un long.
     */
    private long verificaLong(String n, String campo) {
        try{
            return Long.parseLong(n);
        }catch(NumberFormatException nfe) {
            throw new ExcepcionLaberintoInvalido("Debes ingresar un número long para " + campo + ".");
        }
    }

    /**
     * Verifica que se hayan ingresado todos los datos necesarios para generar un laberinto.
     * @throws ExcepcionLaberintoInvalido si:
     *         <ul>
     *            <li>No se ingresó la bandera -g.</li>
     *            <li>No se ingresó el número de columnas.</li>
     *            <li>No se ingresó el número de renglones.</li>
     *         </ul>
     */
    private void verificaEntrada() {
        if(!isGenerando)
            throw new ExcepcionLaberintoInvalido("Debes ingresar la bandera -g para generar un laberinto.");
        if(columnas == 0)
            throw new ExcepcionLaberintoInvalido("Debes ingresar el número de columnas.");
        if(renglones == 0)
            throw new ExcepcionLaberintoInvalido("Debes ingresar el número de renglones.");
        if(semilla == 0)
            semilla = System.currentTimeMillis(); 
    }

    /**
     * Regresa si se está generando un laberinto.
     * @return <code>true</code> si se está generando un laberinto, <code>false</code> en otro caso.
     */
    public boolean isGenerando() {
        return isGenerando;
    }

    /**
     * Regresa la semilla del laberinto.
     * @return la semilla del laberinto.
     */
    public long getSemilla() {
        return semilla;
    }

    /**
     * Regresa las columnas del laberinto.
     * @return las columnas del laberinto.
     */
    public int getColumnas() {
        return columnas;
    }

    /**
     * Regresa los renglones del laberinto.
     * @return los renglones del laberinto.
     */
    public int getRenglones() {
        return renglones;
    }
}