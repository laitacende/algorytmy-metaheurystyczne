package utils;

import structures.Cell;
import structures.Graph;

import java.util.ArrayList;
import java.util.List;

public class InvertNeighbourhood extends AbstractNeighbourhood {

    // indices - move to be added to tabu list
    @Override
    public List<Integer> getBestNeighbour(List<Integer> permutation, Graph g, Integer[] indices, Cell[][] tabuList, int percent, int maxCount) {
        currentPermutation = new ArrayList<>(permutation);
        currentDistance = CostFunction.calcCostFunction(currentPermutation, g);
        Double newDistanceBest = currentDistance;
        newPermutationBest = new ArrayList<>();
        size = currentPermutation.size();
        counter = 0; // count permutations without improvement

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
                }
                newDistance += g.getEdge(newPermutation.get(Math.floorMod(i - 1, size)), newPermutation.get(i));

                if (newDistance < newDistanceBest && !tabuList[i][j].val) { // is better and not on tabu list
                    if (newDistance / newDistanceBest > (double) percent / 100) {
                        counter = 0;
                    }
                    newPermutationBest = new ArrayList<>(newPermutation);
                    newDistanceBest = newDistance;
                    indices[0] = i;
                    indices[1] = j;
                } else { // there is no improvement and counting mode is on
                    counter++;
                    if (counter > maxCount) {
                        return newPermutationBest;
                    }
                }
            }
        }
        return newPermutationBest;
    }
}
