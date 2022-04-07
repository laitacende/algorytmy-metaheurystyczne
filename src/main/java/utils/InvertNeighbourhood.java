package utils;

import structures.Graph;

import java.util.ArrayList;
import java.util.List;

public class InvertNeighbourhood implements INeighbourhood {

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
                // change i and j and reverse everything between them
                newPermutation = new ArrayList<>(currentPermutation);
                newDistance = currentDistance -
                        (i != 0 ? g.getEdge(i - 1, i)
                                : g.getEdge(size -  1, i))
                        - (j != size - 1 ? g.getEdge(j, j + 1)
                                : g.getEdge(j, 0))
                        + (i == 0 && j == size - 1 ? g.getEdge(j, 0) : 0); // subtract connecting edges
                // reverse
                for (int k = 0; k <= j - i; k++) {
                    newPermutation.set(i + k, currentPermutation.get(j - k));
                    // modify distance
                    newDistance -= (j - k != 0 ? g.getEdge(j - k - 1, j - k)
                                    : 0);
                    newDistance += (i + k != 0 ? g.getEdge(i + k - 1, i + k) : 0);
                }
                // add 'cycle' edge
                newDistance += g.getEdge(size - 1, 0);

                // calculate distance
               // newDistance = CostFunction.calcCostFunction(newPermutation, g);
                // TODO check if this calculation is ok


                if (newDistance < currentDistance) {
                    currentPermutation = new ArrayList<>(newPermutation);
                    currentDistance = newDistance;
                }
            }
        }
        return currentPermutation;
    }
}
