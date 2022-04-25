package utils;

import custom_exceptions.NoNewNeighbourException;
import structures.Graph;
import structures.TabuList;
import java.util.ArrayList;
import java.util.List;


public class InvertNeighbourhood extends AbstractNeighbourhood {

    @Override
    public List<Integer> getBestNeighbour(List<Integer> permutation, Graph graph, Integer[] indexes, TabuList tabuList,
                                          Double globalBestDistance, int improvementPercent, int postImprovementCount) throws NoNewNeighbourException {

        initialPermutation = new ArrayList<>(permutation);
        currentBestPermutation = new ArrayList<>(permutation);

        initialDistance = CostFunction.calcCostFunction(initialPermutation, graph);
        currentBestDistance = initialDistance;

        size = initialPermutation.size();
        counter = Integer.MIN_VALUE;

        // invert
        for (int i = 0; i < graph.vNo - 2; i++) { // don't consider the last node - there is nothing to invert
            for (int j = i + 1; j < graph.vNo - 1; j++) { // check from position 'onwards'

                // change i and j and reverse everything between them
                newPermutation = new ArrayList<>(initialPermutation);
                newDistance = initialDistance;

                // reverse
                for (int k = 0; k <= j - i; k++) {
                    newPermutation.set(i + k, initialPermutation.get(j - k));

                    // update distance live
                    newDistance -= graph.getEdge(initialPermutation.get(Math.floorMod(j - k - 1, size)), initialPermutation.get(j - k));
                    newDistance += j - k != i ? graph.getEdge(initialPermutation.get(j - k), initialPermutation.get(Math.floorMod(j - k - 1, size))) : 0;
                }
                // update distance live
                if (Math.floorMod(j + 1, size) != i) {
                    newDistance -= graph.getEdge(initialPermutation.get(j), initialPermutation.get(Math.floorMod(j + 1, size)));
                    newDistance += graph.getEdge(newPermutation.get(j), newPermutation.get(Math.floorMod(j + 1, size)));
                }
                newDistance += graph.getEdge(newPermutation.get(Math.floorMod(i - 1, size)), newPermutation.get(i));


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
        if (currentBestPermutation.equals(initialPermutation)) {
            throw new NoNewNeighbourException();
        }

        return currentBestPermutation;
    }
}
