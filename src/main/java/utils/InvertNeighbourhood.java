package utils;

import structures.Graph;

import java.util.ArrayList;
import java.util.List;

public class InvertNeighbourhood implements INeighbourhood {

    // indices - move to be added to tabu list
    @Override
    public List<Integer> getBestNeighbour(List<Integer> permutation, Graph g, Integer[] indices) {
        List<Integer> newPermutation;
        List<Integer> currentPermutation = new ArrayList<>(permutation);
        Double currentDistance = CostFunction.calcCostFunction(currentPermutation, g);
        Double newDistance;
        Double newDistanceBest = currentDistance;
        List<Integer> newPermutationBest = new ArrayList<>();
        int size = currentPermutation.size();

        // invert
        for (int i = 0; i < g.vNo - 2; i++) { // don't consider the last node - there is nothing to invert
            for (int j = i + 1; j < g.vNo - 1; j++) { // check from position 'onwards'
                // change i and j and reverse everything between them
                newPermutation = new ArrayList<>(currentPermutation);
                newDistance = currentDistance;

                // reverse
                for (int k = 0; k <= j - i; k++) {
                    newPermutation.set(i + k, currentPermutation.get(j - k));
                    // modify distance
                    newDistance -= g.getEdge(currentPermutation.get(Math.floorMod(j - k - 1, size)), currentPermutation.get(j - k));
                    newDistance += j - k != i ? g.getEdge(currentPermutation.get(j - k), currentPermutation.get(Math.floorMod(j - k - 1, size))) : 0;
                }
                if (Math.floorMod(j + 1, size) != i) {
                    newDistance -= g.getEdge(currentPermutation.get(j), currentPermutation.get(Math.floorMod(j + 1, size)));
                    newDistance += g.getEdge(newPermutation.get(j), newPermutation.get(Math.floorMod(j + 1, size)));
                    newDistance += g.getEdge(newPermutation.get(Math.floorMod(i - 1, size)), newPermutation.get(i));
                } else {
                    newDistance += g.getEdge(newPermutation.get(Math.floorMod(i - 1, size)), newPermutation.get(i));
                }

                if (newDistance < newDistanceBest) {
                    newPermutationBest = new ArrayList<>(newPermutation);
                    newDistanceBest = newDistance;
                    indices[0] = i;
                    indices[1] = j;
                }
            }
        }
        return newPermutationBest;
    }
}
