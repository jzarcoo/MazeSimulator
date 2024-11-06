# MazeSimulator

<p>El programa es capaz de <strong>generar y resolver</strong> laberintos representados por un <em>archivo binario</em>. Además, graficarlos en formato <em>SVG</em>.</p>

El programa utiliza el algoritmo de **dijkstra** para resolver el laberinto, y utiliza 7 algoritmos diferentes para generar laberintos aleatoriamente:

- Aldous Broder
- Arbol Binario
- BFS
- DFS
- Hunt And Kill
- Kruskal
- Prim

## Características

<p>Los laberintos son rectangulares y formados por cuartos cuadrados, con puertas Este, Norte, Oeste y Sur.  Cada puerta tiene un puntaje entre 1 y 31; al pasar una puerta que conecte dos cuartos, el explorador del laberinto sufre una penalización proporcional al puntaje: por lo tanto, nos interesa que el explorador <strong>minimice</strong>  el puntaje de las puertas por las que pase.</p>

### Cuartos

<p>Un cuarto puede serializarse y deserializarse en un byte.</p>

- <p>Los 4 bits menos significativos del byte de cada cuarto representan las paredes que tiene el cuarto con sus 4 potenciales vecinos, en orden de menos a más significativo, Este, Norte, Oeste y Sur; el bit en 1 quiere decir que la pared sí existe (o que la puerta no existe), el bit en 0 quiere decir que la pared no existe (o que la puerta sí existe).</p>

- <p>Los 4 bits más significativos del byte de cada cuarto representan el puntaje del cuarto (no de la puerta). Los valores 0000−1111, que corresponden a 0−15 en decimal serán el puntaje de cada cuarto.</p>

## Uso

El programa está desarrollado en Java y utiliza **Maven** para la gestión de dependencias y la compilación.

```sh
$ mvn compile # compila el código
$ mvn test    # corre las pruebas unitarias (opcional)
$ mvn install # genera el archivo proyecto1.jar en el subdirectorio target
```

Para generar un laberinto, el programa se debe invocar como sigue:

```sh
$ java -jar target/proyecto3.jar -g -s 1234 -w 100 -h 100 > ejemplo.mze
```

donde `-s` es un parámetro opcional que representa la semilla del generador de números aleatorios utilizado, `-w` y `-h` indican cuantás columnas y renglones tendrá el laberinto.

Para resolver un laberinto, el programa se debe invocar como sigue:

```sh
$ java -jar target/proyecto3.jar < ejemplo.mze > solucion.svg
```

O, de forma equivalente:

```sh
$ cat ejemplo.mze | java -jar target/proyecto3.jar > solucion.svg
```

## Ejecución

El archivo `proyecto3_test.sh` ejecuta el programa para generar y resolver 7 laberintos, cada uno creado con un algoritmo diferente.
