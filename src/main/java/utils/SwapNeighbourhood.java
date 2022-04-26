package utils;

import custom_exceptions.NoNewNeighbourException;
import structures.Graph;
import structures.TabuList;
import java.util.ArrayList;
import java.util.List;


public class SwapNeighbourhood extends AbstractNeighbourhood  {

    @Override
    public List<Integer> getBestNeighbour(List<Integer> permutation, Graph graph, Integer[] indexes, TabuList tabuList,
                                          Double globalBestDistance, int improvementPercent, int postImprovementCount) throws NoNewNeighbourException {

        initialPermutation = new ArrayList<>(permutation);
        currentBestPermutation = new ArrayList<>(permutation);

        initialDistance = CostFunction.calcCostFunction(initialPermutation, graph);
        currentBestDistance = Double.MAX_VALUE;

        size = initialPermutation.size();
        counter = Integer.MIN_VALUE;

        // swap
        for (int i = 0; i < graph.vNo - 2; i++) { // don't consider the last node - there is nothing to invert
            for (int j = i + 1; j < size; j++) { // check from position 'onwards'

                newPermutation = new ArrayList<>(initialPermutation);
                newDistance = initialDistance;

                // swap i and j
                newPermutation.set(j, initialPermutation.get(i));
                newPermutation.set(i, initialPermutation.get(j));

                // update distance
                newDistance -= graph.getEdge(initialPermutation.get(Math.floorMod(i - 1, size)), initialPermutation.get(i));
                newDistance -= graph.getEdge(initialPermutation.get(i), initialPermutation.get(Math.floorMod(i + 1, size)));
                if (j != Math.floorMod(i + 1, size)) {
                    newDistance -= graph.getEdge(initialPermutation.get(Math.floorMod(j - 1, size)), initialPermutation.get(j));
                }
                if (i != Math.floorMod(j + 1, size)) {
                    newDistance -= graph.getEdge(initialPermutation.get(j), initialPermutation.get(Math.floorMod(j + 1, size)));
                }
                newDistance += graph.getEdge(newPermutation.get(Math.floorMod(i - 1, size)), newPermutation.get(i));
                newDistance += graph.getEdge(newPermutation.get(i), newPermutation.get(Math.floorMod(i + 1, size)));
                if (j != Math.floorMod(i + 1, size)) {
                    newDistance += graph.getEdge(newPermutation.get(Math.floorMod(j - 1, size)), newPermutation.get(j));
                }
                if (i != Math.floorMod(j + 1, size)) {
                    newDistance += graph.getEdge(newPermutation.get(j), newPermutation.get(Math.floorMod(j + 1, size)));
                }


                // if permutation is better than global best solution -  use regardless
                if (newDistance.intValue() < globalBestDistance.intValue()) {
                    currentBestPermutation = new ArrayList<>(newPermutation);
                    currentBestDistance = newDistance;
                    indexes[0] = i;
                    indexes[1] = j;
                }
                else if (newDistance < currentBestDistance && !tabuList.isOnTabuList(i, j, newDistance)) {
                    // if found decent improvement start counter
                    if (((currentBestDistance - newDistance) / currentBestDistance) > ((double) improvementPercent / 100.0) && currentBestDistance != Double.MAX_VALUE) {
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
