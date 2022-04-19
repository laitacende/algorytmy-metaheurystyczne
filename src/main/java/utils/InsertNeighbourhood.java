package utils;

import custom_exceptions.NoNewNeighbourException;
import structures.Graph;
import structures.TabuList;
import java.util.ArrayList;
import java.util.List;


public class InsertNeighbourhood extends AbstractNeighbourhood {

    @Override
    public List<Integer> getBestNeighbour(List<Integer> permutation, Graph graph, Integer[] indexes, TabuList tabuList,
                                          Double globalBestDistance, int percent, int maxCount) throws NoNewNeighbourException {

        currentPermutation = new ArrayList<>(permutation);
        currentBestPermutation = new ArrayList<>(permutation);

        currentDistance = CostFunction.calcCostFunction(currentPermutation, graph);
        currentBestDistance = currentDistance;

        size = currentPermutation.size();
        counter = 0; // count permutations without improvement

        // insertion
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                // skip all below when i = j
                if (i == j) continue;

                newPermutation = new ArrayList<>(currentPermutation);
                newDistance = currentDistance;
                newDistance -= graph.getEdge(currentPermutation.get(Math.floorMod(i - 1, size)), currentPermutation.get(i));
                newDistance -= graph.getEdge(currentPermutation.get(i), currentPermutation.get(Math.floorMod(i + 1, size)));
                Integer inserted = currentPermutation.get(i);

                // get ith element
                if (i < j) { // shift from i + 1 to jth to left
                    if (Math.floorMod(j + 1, size) != i) {
                        newDistance -= graph.getEdge(currentPermutation.get(j), currentPermutation.get(Math.floorMod(j + 1, size)));
                    }

                    int k = i + 1;
                    while (k <= j) {
                        newPermutation.set(k - 1, currentPermutation.get(k));
                        k++;
                    }

                    newPermutation.set(j, inserted);
                    newDistance += graph.getEdge(newPermutation.get(Math.floorMod(i - 1, size)), newPermutation.get(i));
                }
                else { // shift from jth to ith right
                    if (Math.floorMod(j - 1, size) != i) {
                        newDistance -= graph.getEdge(currentPermutation.get(Math.floorMod(j - 1, size)), currentPermutation.get(j));
                    }
                    int k = j;
                    while (k < i) {
                        newPermutation.set(k + 1, currentPermutation.get(k));
                        k++;
                    }
                    newPermutation.set(j, inserted);
                    newDistance += graph.getEdge(newPermutation.get(i), newPermutation.get(Math.floorMod(i + 1, size)));
                }
                // update distance
                if (Math.floorMod(i - 1, size) != j || i > j) {
                    newDistance += graph.getEdge(newPermutation.get(j), newPermutation.get(Math.floorMod(j + 1, size)));
                }
                if (Math.floorMod(i + 1, size) != j || j > i) {
                    newDistance += graph.getEdge(newPermutation.get(Math.floorMod(j - 1, size)), newPermutation.get(j));
                }

                // if permutation is better than global best solution add regardless
                if (newDistance < globalBestDistance) {
                    currentBestPermutation = new ArrayList<>(newPermutation);
                    currentBestDistance = newDistance;
                    indexes[0] = i;
                    indexes[1] = j;
                }
                // if permutation is better and not on tabu list
                else if (newDistance < currentBestDistance && !tabuList.isOnTabuList(i, j)) {
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

        // when no new neighbour found
        if (currentBestPermutation.equals(currentPermutation)) throw new NoNewNeighbourException();

        return currentBestPermutation;
    }
}
