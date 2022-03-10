package utils;

import structures.Graph;

import java.util.List;

public class CostFunction {

    // cycle is the tour that the cost is calculated for
    public static Double calcCostFunction(List<Integer> cycle, Graph g) {
        Double sum = 0.0;
        for (int i = 0; i < cycle.size(); i++) {
            if (i + 1 < cycle.size()) {
                sum += g.getEdge(cycle.get(i), cycle.get(i + 1));
            } else { // from the last node to the first one on the cycle
                sum += g.getEdge(cycle.get(i), cycle.get(0));
            }
        }
        return sum;
    }
}
