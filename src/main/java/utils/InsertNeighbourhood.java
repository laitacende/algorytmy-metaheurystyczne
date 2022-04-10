package utils;

import structures.Cell;
import structures.Graph;

import java.util.ArrayList;
import java.util.List;

public class InsertNeighbourhood implements INeighbourhood {
    @Override
    public List<Integer> getBestNeighbour(List<Integer> permutation, Graph g, Integer[] indices,  Cell[][] tabuList) {
        List<Integer> newPermutation;
        List<Integer> currentPermutation = new ArrayList<>(permutation);
        Double currentDistance = CostFunction.calcCostFunction(currentPermutation, g);
        Double newDistance;
        Double newDistanceBest = currentDistance;
        List<Integer> newPermutationBest = new ArrayList<>();
        int size = currentPermutation.size();

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


                    if (newDistance < newDistanceBest) {
                        newPermutationBest = new ArrayList<>(newPermutation);
                        newDistanceBest = newDistance;
                        indices[0] = i;
                        indices[1] = j;
                    }
                }
            }
        }
        return newPermutationBest;
    }
}
