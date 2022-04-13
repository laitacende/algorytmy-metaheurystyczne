package utils;

import structures.Cell;
import structures.Graph;

import java.util.ArrayList;
import java.util.List;

public class InsertNeighbourhood extends AbstractNeighbourhood {
    @Override
    public List<Integer> getBestNeighbour(List<Integer> permutation, Graph g, Integer[] indices,  Cell[][] tabuList, int percent, int maxCount) {
        currentPermutation = new ArrayList<>(permutation);
        currentDistance = CostFunction.calcCostFunction(currentPermutation, g);
        newDistanceBest = currentDistance;
        newPermutationBest = new ArrayList<>();
        size = currentPermutation.size();
        counter = 0; // count permutations without improvement

        // insertion
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    newPermutation = new ArrayList<>(currentPermutation);
                    newDistance = currentDistance;
                    Integer inserted = currentPermutation.get(i);
                    newDistance -= g.getEdge(currentPermutation.get(Math.floorMod(i - 1, size)), currentPermutation.get(i));
                    newDistance -= g.getEdge(currentPermutation.get(i), currentPermutation.get(Math.floorMod(i + 1, size)));
                    // get ith element
                    if (i < j) { // shift from i + 1 to jth to left
                        if (Math.floorMod(j + 1, size) != i) {
                            newDistance -= g.getEdge(currentPermutation.get(j), currentPermutation.get(Math.floorMod(j + 1, size)));
                        }
                        int k = i + 1;
                        while (k <= j) {
                            newPermutation.set(k - 1, currentPermutation.get(k));
                            k++;
                        }
                        newPermutation.set(j, inserted);
                        newDistance += g.getEdge(newPermutation.get(Math.floorMod(i - 1, size)), newPermutation.get(i));
                    } else { // shift from jth to ith right
                        if (Math.floorMod(j - 1, size) != i) {
                            newDistance -= g.getEdge(currentPermutation.get(Math.floorMod(j - 1, size)), currentPermutation.get(j));
                        }
                        int k = j;
                        while (k < i) {
                            newPermutation.set(k + 1, currentPermutation.get(k));
                            k++;
                        }
                        newPermutation.set(j, inserted);
                        newDistance += g.getEdge(newPermutation.get(i), newPermutation.get(Math.floorMod(i + 1, size)));
                    }
                    if (Math.floorMod(i - 1, size) != j || i > j) {
                        newDistance += g.getEdge(newPermutation.get(j), newPermutation.get(Math.floorMod(j + 1, size)));
                    }
                    if (Math.floorMod(i + 1, size) != j || j > i) {
                        newDistance += g.getEdge(newPermutation.get(Math.floorMod(j - 1, size)), newPermutation.get(j));
                    }

                    if (newDistance < newDistanceBest && !tabuList[i][j].val) {
                        if (newDistance / newDistanceBest > (double) percent / 100) {
                            counter = 0;
                        }
                        newPermutationBest = new ArrayList<>(newPermutation);
                        newDistanceBest = newDistance;
                        indices[0] = i;
                        indices[1] = j;
                    } else {
                        counter++;
                        if (counter > maxCount) {
                            return newPermutationBest;
                        }
                    }
                }
            }
        }
        return newPermutationBest;
    }
}
