package utils;

import custom_exceptions.NoNewNeighbourException;
import structures.Graph;
import structures.TabuList;
import java.util.ArrayList;
import java.util.List;


public class InsertNeighbourhood extends AbstractNeighbourhood {

    @Override
    public List<Integer> getBestNeighbour(List<Integer> permutation, Graph graph, Integer[] indexes, TabuList tabuList,
                                          Double globalBestDistance, int improvementPercent, int postImprovementCount) throws NoNewNeighbourException {

        initialPermutation = new ArrayList<>(permutation);
        currentBestPermutation = new ArrayList<>(permutation);

        initialDistance = CostFunction.calcCostFunction(initialPermutation, graph);
        currentBestDistance = initialDistance;

        size = initialPermutation.size();
        counter = Integer.MIN_VALUE;

        // insertion
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                // skip all below when i = j
                if (i == j) continue;

                newPermutation = new ArrayList<>(initialPermutation);
                newDistance = initialDistance;

                newDistance -= graph.getEdge(initialPermutation.get(Math.floorMod(i - 1, size)), initialPermutation.get(i));
                newDistance -= graph.getEdge(initialPermutation.get(i), initialPermutation.get(Math.floorMod(i + 1, size)));
                Integer inserted = initialPermutation.get(i);

                // get ith element
                if (i < j) { // shift from i + 1 to jth to left
                    if (Math.floorMod(j + 1, size) != i) {
                        newDistance -= graph.getEdge(initialPermutation.get(j), initialPermutation.get(Math.floorMod(j + 1, size)));
                    }

                    int k = i + 1;
                    while (k <= j) {
                        newPermutation.set(k - 1, initialPermutation.get(k));
                        k++;
                    }

                    newPermutation.set(j, inserted);
                    newDistance += graph.getEdge(newPermutation.get(Math.floorMod(i - 1, size)), newPermutation.get(i));
                }
                else { // shift from jth to ith right
                    if (Math.floorMod(j - 1, size) != i) {
                        newDistance -= graph.getEdge(initialPermutation.get(Math.floorMod(j - 1, size)), initialPermutation.get(j));
                    }
                    int k = j;
                    while (k < i) {
                        newPermutation.set(k + 1, initialPermutation.get(k));
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


                // if permutation is better than global best solution -  use regardless
                if (newDistance < globalBestDistance) {
                    currentBestPermutation = new ArrayList<>(newPermutation);
                    currentBestDistance = newDistance;
                    indexes[0] = i;
                    indexes[1] = j;
                }
                else if (newDistance < currentBestDistance && !tabuList.isOnTabuList(i, j)) {
                    // if found decent improvement start counter
                    if (((currentBestDistance - newDistance) / currentBestDistance) > ((double) improvementPercent / 100.0)) {
                        counter = 0;
                    }

                    // reset counter when found better sollution in some steps
                    if (counter > 0) {
                        counter = 0;
                    }

                    currentBestPermutation = new ArrayList<>(newPermutation);
                    currentBestDistance = newDistance;
                    indexes[0] = i;
                    indexes[1] = j;
                }
                else {
                    counter++;
                    if (counter > postImprovementCount) {
                        return currentBestPermutation;  // when there is no improvement - stop
                    }
                }
            }
        }

        // when no new neighbour found
        if (currentBestPermutation.equals(initialPermutation))
            throw new NoNewNeighbourException();

        return currentBestPermutation;
    }
}
