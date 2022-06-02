package algorithms;

import structures.tsp.Graph;
import utils.graph.CostFunction;

import java.util.ArrayList;
import java.util.List;

public class TwoOPT {
    public static List<Integer> twoOpt(Graph g, List<Integer> startPermutation) {
        List<Integer> currentPermutation = new ArrayList<>();
        List<Integer> newPermutation;
        Double currentDistance;
        Double newDistance;
        Double newDistanceBest;
        List<Integer> newPermutationBest = new ArrayList<>();
        boolean improved = true;

        currentPermutation = startPermutation;
        currentDistance = CostFunction.calcCostFunction(currentPermutation, g);

        while (improved) {
            newDistanceBest = Double.MAX_VALUE;
            // invert
            for (int i = 0; i < g.vNo - 2; i++) { // don't consider the last node - there is nothing to invert
                for (int j = i + 1; j < g.vNo - 1; j++) { // check from position 'onwards'
                    // change i and j and reverse everything between them
                    newPermutation = new ArrayList<>(currentPermutation);

                    // reverse
                    for (int k = 0; k <= j - i; k++) {
                        newPermutation.set(i + k, currentPermutation.get(j - k));
                    }

                    // calculate distance
                    newDistance = CostFunction.calcCostFunction(newPermutation, g);

                    if (newDistance < newDistanceBest) {
                        newPermutationBest = new ArrayList<>(newPermutation);
                        newDistanceBest = newDistance;
                    }
                }
            }

            // compare with the best of neighbours
            if (currentDistance > newDistanceBest) {
                currentPermutation = new ArrayList<>(newPermutationBest);
                currentDistance = newDistanceBest;
            }
            else { // stop algorithm
                improved = false;
            }
        }

        return currentPermutation;
    }

    public static List<Integer> twoOptExtended(Graph g) {
        List<Integer> currentSolution, bestSolution;
        Double currentDistance, bestDistance;

        bestSolution = currentSolution = TwoOPT.twoOpt(g, NearestNeighbour.nearestNeighbour(g, 1));
        bestDistance = currentDistance = CostFunction.calcCostFunction(currentSolution, g);

        for (int i = 2; i < g.vNo; i++) {
            currentSolution = TwoOPT.twoOpt(g, NearestNeighbour.nearestNeighbour(g, i));
            currentDistance = CostFunction.calcCostFunction(currentSolution, g);

            if (currentDistance < bestDistance) {
                bestSolution = currentSolution;
                bestDistance = currentDistance;
            }
        }
        return bestSolution;
    }
}