# MazeSimulator

<p>The program is capable of <strong>generating and solving</strong> mazes represented by a <em>binary file</em>. Additionally, it can render them in <em>SVG</em> format.</p>

The program uses the **Dijkstra** algorithm to solve the maze and implements seven different algorithms for randomly generating mazes:

- Aldous Broder
- Binary Tree
- BFS
- DFS
- Hunt And Kill
- Kruskal
- Prim

## Features

<p>The mazes are rectangular and consist of square rooms with East, North, West, and South doors. Each door has a score between 1 and 31. When passing through a door connecting two rooms, the maze explorer incurs a penalty proportional to the door's score. Therefore, the goal is for the explorer to <strong>minimize</strong> the scores of the doors they pass through.</p>

### Rooms

<p>A room can be serialized and deserialized into a single byte.</p>

- <p>The 4 least significant bits of the byte for each room represent the walls connected to its 4 potential neighbors, ordered from least to most significant: East, North, West, and South. A bit set to 1 indicates the presence of a wall (or the absence of a door), while a bit set to 0 indicates the absence of a wall (or the presence of a door).</p>

- <p>The 4 most significant bits of the byte for each room represent the room's score (not the door's). The values 0000–1111, corresponding to 0–15 in decimal, will be the score for each room.</p>

## Usage

The program is developed in Java and uses **Maven** for dependency management and compilation.

```sh
$ mvn compile # compiles the code
$ mvn test    # runs unit tests (optional)
$ mvn install # generates the proyecto1.jar file in the target subdirectory
```

To generate a maze, invoke the program as follows:

```sh
$ java -jar target/proyecto3.jar -g -s 1234 -w 100 -h 100 > example.mze
```

where `-s` is an optional parameter that specifies the seed for the random number generator, and `-w` and `-h` indicate the number of columns and rows in the maze.

To solve a maze, invoke the program as follows:

```sh
$ java -jar target/proyecto3.jar < example.mze > solution.svg
```

Alternatively:

```sh
$ cat example.mze | java -jar target/proyecto3.jar > solution.svg
```

## Execution

The `proyecto3_test.sh` script runs the program to generate and solve seven mazes, each created with a different algorithm.
