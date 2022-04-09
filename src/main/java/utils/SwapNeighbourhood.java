package utils;

import structures.Graph;

import java.util.ArrayList;
import java.util.List;

public class SwapNeighbourhood implements INeighbourhood {
    @Override
    public List<Integer> getBestNeighbour(List<Integer> permutation, Graph g, Integer[] indices) {
        List<Integer> newPermutation;
        List<Integer> currentPermutation = new ArrayList<>(permutation);
        Double currentDistance = CostFunction.calcCostFunction(currentPermutation, g);
        Double newDistance;
        int size = currentPermutation.size();

        // invert
        for (int i = 0; i < g.vNo - 2; i++) { // don't consider the last node - there is nothing to invert
            for (int j = i + 1; j < g.vNo - 1; j++) { // check from position 'onwards'
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

                if (newDistance < currentDistance) {
                    currentPermutation = new ArrayList<>(newPermutation);
                    currentDistance = newDistance;
                    indices[0] = i;
                    indices[1] = j;
                }
            }
        }
        return currentPermutation;
    }
}
