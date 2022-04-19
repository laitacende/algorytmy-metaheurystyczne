package utils;

import structures.Graph;
import structures.TabuList;
import java.util.ArrayList;
import java.util.List;


public class InvertNeighbourhood extends AbstractNeighbourhood {

    @Override
    public List<Integer> getBestNeighbour(List<Integer> permutation, Graph graph, Integer[] indexes, TabuList tabuList, int percent, int maxCount) {
        currentPermutation = new ArrayList<>(permutation);
        currentBestPermutation = new ArrayList<>();

        currentDistance = CostFunction.calcCostFunction(currentPermutation, graph);
        currentBestDistance = currentDistance;

        size = currentPermutation.size();
        counter = 0; // count permutations without improvement

        // invert
        for (int i = 0; i < graph.vNo - 2; i++) { // don't consider the last node - there is nothing to invert
            for (int j = i + 1; j < graph.vNo - 1; j++) { // check from position 'onwards'

                // change i and j and reverse everything between them
                newPermutation = new ArrayList<>(currentPermutation);
                newDistance = currentDistance;

                // reverse
                for (int k = 0; k <= j - i; k++) {
                    newPermutation.set(i + k, currentPermutation.get(j - k));

                    // update distance
                    newDistance -= graph.getEdge(currentPermutation.get(Math.floorMod(j - k - 1, size)), currentPermutation.get(j - k));
                    newDistance += j - k != i ? graph.getEdge(currentPermutation.get(j - k), currentPermutation.get(Math.floorMod(j - k - 1, size))) : 0;
                }

                // update distance
                if (Math.floorMod(j + 1, size) != i) {
                    newDistance -= graph.getEdge(currentPermutation.get(j), currentPermutation.get(Math.floorMod(j + 1, size)));
                    newDistance += graph.getEdge(newPermutation.get(j), newPermutation.get(Math.floorMod(j + 1, size)));
                }
                newDistance += graph.getEdge(newPermutation.get(Math.floorMod(i - 1, size)), newPermutation.get(i));


                // if permutation is better and not on tabu list
                if (newDistance < currentBestDistance && !tabuList.isOnTabuList(i, j)) {
                    // if there is improvement - reset counter
                    if (newDistance / currentBestDistance > (double) percent / 100) { counter = 0; }

                    currentBestPermutation = new ArrayList<>(newPermutation);
                    currentBestDistance = newDistance;
                    indexes[0] = i;
                    indexes[1] = j;
                }
                else {
                    counter++;

                    // when there is no improvement - stop
                    if (counter > maxCount) {
                        return currentBestPermutation;
                    }
                }
            }
        }

        return currentBestPermutation;
    }
}
