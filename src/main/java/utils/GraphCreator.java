package utils;

import org.apache.commons.math3.random.MersenneTwister;
import structures.Coordinates;
import structures.Graph;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class GraphCreator {

    // to obtain directed graph with random distances
    public static Graph randomFullMatrix(Integer vNo, Integer bound) {
        MersenneTwister mt = new MersenneTwister();
        Graph g = new Graph(vNo, GraphType.FULL_MATRIX);
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
        Graph g = new Graph(vNo, GraphType.UPPER_ROW);
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
        Graph g = new Graph(vNo, GraphType.EUC_2D);
//
        // get random coordinates
        for (int i = 1; i < vNo + 1; i++) {
            g.coordinates[i] = new Coordinates(mt.nextInt(bound), mt.nextInt(bound));

        }
        // calculate euclidean distances (symmetric matrix)
        fillTheGraph(g);

        return g;
    }

    // to obtain graph from data within a file
    public static Graph fromFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        int size = 0;
        String graphType = "";
        String[] data;

        // gathering graph data
        do {
            data = reader.readLine().replaceAll("\\s", "").split(":");

            if (data[0].equals("DIMENSION")) {
                size = Integer.parseInt(data[1]);
                if (size == 0) {
                    System.out.println("Wrong file or graph size");
                    return null;
                }
            }
            if (data[0].equals("EDGE_WEIGHT_TYPE")) {
                graphType = data[1];
            }

        } while (!(data[0].equals("NODE_COORD_SECTION") || data[0].equals("EDGE_WEIGHT_SECTION")));
        data = reader.readLine().trim().replaceAll("\\s+", " ").split(" ");

        Graph g;
        switch (graphType) {
            case "EUC_2D" -> {
                g = new Graph(size, GraphType.EUC_2D);

                // gathering coordinates
                int i = 1;
                while (!data[0].equals("EOF")) {
                    g.coordinates[i] = new Coordinates(Integer.parseInt(data[1]), Integer.parseInt(data[2]));

                    data = reader.readLine().trim().replaceAll("\\s+", " ").split(" ");
                    i++;
                }

                // calculate euclidean distances and fill the graph
                fillTheGraph(g);
                reader.close();
                return g;
            }
            case "FULL_MATRIX" -> {
                g = new Graph(size, GraphType.UPPER_ROW);

                int i = 1;
                while (!data[0].equals("EOF")) {

                    for (int j = 1; j < g.getSize(); j++) {
                        if (i == j) {
                            g.addEdge(i, j, -1.0);
                        }
                        else {
                            g.addEdge(i, j, Double.parseDouble(data[j - 1]));
                        }
                    }
                    data = reader.readLine().trim().replaceAll("\\s+", " ").split(" ");
                    i++;
                }
                return g;
            }
            case "UPPER_ROW" -> {
                g = new Graph(size, GraphType.FULL_MATRIX);

                int i = 1;
                while (!data[0].equals("EOF")) {

                    data = reader.readLine().trim().replaceAll("\\s+", " ").split(" ");
                    i++;
                }
                return g;
            }
        }

        return null;
    }

    static void fillTheGraph(Graph g) {
        for (int i = 1; i < g.getSize(); i++) {
            for (int j = 1; j < g.getSize(); j++) {
                //System.out.println(i + " " + j);
                if (i == j) {
                    g.addEdge(i, j, -1.0);
                }
                else if (i > j) {
                    // calculate distance
                    Double r1 = (double) g.coordinates[i].x - g.coordinates[j].x;
                    Double r2 = (double) g.coordinates[i].y - g.coordinates[j].y;
                    //System.out.println(r1 + " " + r2);
                    Double d = Math.sqrt(r1 * r1 + r2 * r2);
                    g.addEdge(i, j, d);
                    g.addEdge(j, i, d);
                }
            }
        }
    }
}
