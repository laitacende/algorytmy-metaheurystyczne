package utils;

import org.apache.commons.math3.random.MersenneTwister;
import structures.Coordinates;
import structures.Graph;

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
                    if (bound == 0) {
                        g.addEdge(i, j, (double) mt.nextInt());
                        g.addEdge(j, i, g.getEdge(i, j));
                    } else {
                        g.addEdge(i, j, (double) mt.nextInt(bound));
                        g.addEdge(j, i, g.getEdge(i, j));
                    }
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

        // get random coordinates
        for (int i = 1; i < vNo + 1; i++) {
            g.coordinates[i] = new Coordinates(mt.nextInt(bound), mt.nextInt(bound));
            // System.out.println(i + " x: " + coordinates[i].x + " y: " + coordinates[i].y);
        }

        // calculate euclidean distances (symmetric matrix)
        for (int i = 1; i < g.getSize(); i++) {
            for (int j = 1; j < g.getSize(); j++) {
                if (i == j) {
                    g.addEdge(i, j, -1.0);
                } else if (i > j) {
                    // calculate distance
                    Double r1 = (double) g.coordinates[i].x - g.coordinates[j].x;
                    Double r2 = (double) g.coordinates[i].y - g.coordinates[j].y;
                    //System.out.println(r1 + " " + r2);
                    Double d = (double) Math.round(Math.sqrt(r1 * r1 + r2 * r2)); // round to the nearest int
                    g.addEdge(i, j, d);
                    g.addEdge(j, i, d);
                }
            }
        }

        return g;
    }
}
