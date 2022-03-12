package utils;

import org.apache.commons.math3.random.MersenneTwister;
import structures.Graph;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class GraphCreator {

    // to obtain directed graph with random distances
    public static Graph randomFullMatrix(Integer vNo, Integer bound) {
        MersenneTwister mt = new MersenneTwister();
        Graph g = new Graph(vNo);
        for (int i = 1; i < g.getSize(); i++) {
            for (int j = 1; j < g.getSize(); j++) {
                if (i == j) {
                    g.addEdge(i, j, -1.0);
                } else {
                    if (bound == 0) { // bound not specified, get full range
                        g.addEdge(i, j, (double) mt.nextInt());
                    } else {
                        g.addEdge(i, j, (double) mt.nextInt(bound));
                    }
                }
            }
        }
        return g;
    }

    // to obtain undirected graph with random distances
    public static Graph randomSymmetric(Integer vNo, Integer bound) {
        MersenneTwister mt = new MersenneTwister();
        Graph g = new Graph(vNo);
        for (int i = 1; i < g.getSize(); i++) {
            for (int j = 1; j < g.getSize(); j++) {
                if (i == j) {
                    g.addEdge(i, j, -1.0);
                } else if (i > j) {
                    if (bound == 0)
                        g.addEdge(i, j, (double) mt.nextInt());
                    else
                        g.addEdge(i, j, (double) mt.nextInt(bound));

                    g.addEdge(j, i, g.getEdge(i, j));
                }
            }
        }
        return g;
    }

    // to obtain graph with euclidean distances
    // here bound is for x, y coordinates
    public static Graph randomEuclidean(Integer vNo, Integer bound) {
        MersenneTwister mt = new MersenneTwister();
        Graph g = new Graph(vNo);
        Integer[] coordinatesX = new Integer[g.getSize()];
        Integer[] coordinatesY = new Integer[g.getSize()];

        // get random coordinates
        for (int i = 1; i < vNo + 1; i++) {
            coordinatesX[i] = mt.nextInt(bound);
            coordinatesY[i] = mt.nextInt(bound);

            // System.out.println(i + " x: " + coordinatesX[i] + " y: " + coordinatesY[i]);
        }
        // calculate euclidean distances (symmetric matrix)
        fillTheGraph(g, coordinatesX, coordinatesY);

        return g;
    }

    // to obtain graph from data within a file
    public static Graph fromFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        int size = 0;
        String[] data;

        // gathering graph size
        do {
            data = reader.readLine().replaceAll("\\s", "").split(":");

            if (data[0].equals("DIMENSION")) {
                size = Integer.parseInt(data[1]);
                if (size == 0) {
                    System.out.println("Wrong file or graph size");
                    return null;
                }
            }
        } while (!data[0].equals("NODE_COORD_SECTION"));
        data = reader.readLine().trim().replaceAll("\\s+", " ").split(" ");


        Graph g = new Graph(size);
        Integer[] coordinatesX = new Integer[g.getSize()];
        Integer[] coordinatesY = new Integer[g.getSize()];

        // gathering coordinates
        int i = 1;
        while (!data[0].equals("EOF")) {
            coordinatesX[i] = Integer.parseInt(data[1]);
            coordinatesY[i] = Integer.parseInt(data[2]);

            data = reader.readLine().trim().replaceAll("\\s+", " ").split(" ");
            i++;
        }

        // calculate euclidean distances and fill the graph
        fillTheGraph(g, coordinatesX, coordinatesY);
        reader.close();

        return g;
    }

    static void fillTheGraph(Graph g, Integer[] coordinatesX, Integer[] coordinatesY) {
        for (int i = 1; i < g.getSize(); i++) {
            for (int j = 1; j < g.getSize(); j++) {
                //System.out.println(i + " " + j);
                if (i == j) {
                    g.addEdge(i, j, -1.0);
                }
                else if (i > j) {
                    // calculate distance
                    Double r1 = (double) coordinatesX[i] - coordinatesX[j];
                    Double r2 = (double) coordinatesY[i] - coordinatesY[j];
                    //System.out.println(r1 + " " + r2);
                    Double d = Math.sqrt(r1 * r1 + r2 * r2);
                    g.addEdge(i, j, d);
                    g.addEdge(j, i, d);
                }
            }
        }
    }
}
