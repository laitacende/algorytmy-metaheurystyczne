package algorithms;

import structures.Graph;
import utils.CostFunction;

import java.util.ArrayList;
import java.util.List;

public class ExtendedNearestNeighbour {
    public static List<Integer> extendedNearestNeighbour(Graph g) {
        Double bestSum = Double.MAX_VALUE;
        List<Integer> bestTour = new ArrayList<>();
        List<Integer> tour;
        Double sum;

        for (int i = 1; i < g.vNo; i++) {
            tour = NearestNeighbour.nearestNeighbour(g, i);
            sum = CostFunction.calcCostFunction(tour, g);
            if (sum < bestSum) {
                bestSum = sum;
                bestTour = tour;
            }
        }
        return bestTour;
    }
}
