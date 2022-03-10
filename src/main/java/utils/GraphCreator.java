package utils;

import org.apache.commons.math3.random.MersenneTwister;
import structures.Graph;

public class GraphCreator {

    // to obtain directed graph with random distances
    public static Graph randomFullMatrix(Integer vNo, Integer bound) {
        MersenneTwister mt = new MersenneTwister();
        Graph g = new Graph(vNo);
        for (int i = 1; i < vNo + 1; i++) {
            for (int j = 1; j < vNo + 1; j++) {
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
        for (int i = 1; i < vNo + 1; i++) {
            for (int j = 1; j < vNo + 1; j++) {
                if (i == j) {
                    g.addEdge(i, j, -1.0);
                } else if (i > j) {
                    if (bound == 0) {
                        g.addEdge(i, j, (double) mt.nextInt(bound));
                        g.addEdge(j, i, g.getEdge(i, j));
                    } else {
                        g.addEdge(i, j, (double) mt.nextInt());
                        g.addEdge(j, i, g.getEdge(i, j));
                    }
                }
            }
        }
        return g;
    }
}
