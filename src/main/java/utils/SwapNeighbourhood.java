package utils;

import structures.Cell;
import structures.Graph;

import java.util.ArrayList;
import java.util.List;

public class SwapNeighbourhood extends AbstractNeighbourhood  {
    @Override
    public List<Integer> getBestNeighbour(List<Integer> permutation, Graph g, Integer[] indices, Cell[][] tabuList, int percent, int maxCount) {
        currentPermutation = new ArrayList<>(permutation);
        currentDistance = CostFunction.calcCostFunction(currentPermutation, g);
        newDistanceBest = currentDistance;
        newPermutationBest = new ArrayList<>();
        size = currentPermutation.size();
        counter = 0; // count permutations without improvement

        // swap
        for (int i = 0; i < g.vNo - 2; i++) { // don't consider the last node - there is nothing to invert
            for (int j = i + 1; j < size; j++) { // check from position 'onwards'
                newPermutation = new ArrayList<>(currentPermutation);
                newDistance = currentDistance;
                // swap i and j
                newPermutation.set(j, currentPermutation.get(i));
                newDistance -= g.getEdge(currentPermutation.get(Math.floorMod(i - 1, size)), currentPermutation.get(i));
                newDistance -= g.getEdge(currentPermutation.get(i), currentPermutation.get(Math.floorMod(i + 1, size)));
                newPermutation.set(i, currentPermutation.get(j));
                if (j != Math.floorMod(i + 1, size)) {
                    newDistance -= g.getEdge(currentPermutation.get(Math.floorMod(j - 1, size)), currentPermutation.get(j));
                }
                if (i != Math.floorMod(j + 1, size)) {
                    newDistance -= g.getEdge(currentPermutation.get(j), currentPermutation.get(Math.floorMod(j + 1, size)));
                }

                newDistance += g.getEdge(newPermutation.get(Math.floorMod(i - 1, size)), newPermutation.get(i));
                newDistance += g.getEdge(newPermutation.get(i), newPermutation.get(Math.floorMod(i + 1, size)));
                if (j != Math.floorMod(i + 1, size)) {
                    newDistance += g.getEdge(newPermutation.get(Math.floorMod(j - 1, size)), newPermutation.get(j));
                }
                if (i != Math.floorMod(j + 1, size)) {
                    newDistance += g.getEdge(newPermutation.get(j), newPermutation.get(Math.floorMod(j + 1, size)));
                }

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
