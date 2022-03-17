package algorithms;

import structures.Graph;

import java.util.ArrayList;
import java.util.List;

public class NearestNeighbour {
    public static List<Integer> nearestNeighbour(Graph g, Integer startNode) {
        ArrayList<Integer> tour = new ArrayList<>();
        Boolean[] visited = new Boolean[g.vNo];
        Integer current = startNode;

        for (int i = 0; i < g.vNo; i++) {
            visited[i] = false;
        }

        // start in startNode and mark as visited
        tour.add(current);
        visited[current] = true;

        while (tour.size() != g.vNo - 1) {
            // find the nearest neighbour
            Double minDistance = Double.MAX_VALUE;
            int minNode = 0;
            for (int i = 1; i < g.vNo; i++) {
                if (i != current && !visited[i] && minDistance > g.getEdge(current, i)) {
                    minDistance = g.getEdge(current, i);
                    minNode = i;
                }
            }
            tour.add(minNode);
            current = minNode;
        }

        return tour;
    }
}
