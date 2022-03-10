package utils;

import org.apache.commons.math3.random.MersenneTwister;
import structures.Graph;

public class GraphCreator {
    public static Graph randomFullMatrix(Integer vNo, Integer bound) {
        MersenneTwister mt = new MersenneTwister();
        Graph g = new Graph(vNo);
        for (int i = 0; i < vNo; i++) {
            for (int j = 0; j < vNo; j++) {
                if (i == j) {
                    g.addEdge(i, j, -1.0);
                }
//                g.addEdge(i, j, M);
            }
        }
    }
}
