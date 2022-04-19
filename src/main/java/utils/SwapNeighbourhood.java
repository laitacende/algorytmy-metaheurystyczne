package utils;

import structures.Graph;
import structures.TabuList;
import java.util.ArrayList;
import java.util.List;


public class SwapNeighbourhood extends AbstractNeighbourhood  {

    @Override
    public List<Integer> getBestNeighbour(List<Integer> permutation, Graph graph, Integer[] indexes, TabuList tabuList, int percent, int maxCount) {
        currentPermutation = new ArrayList<>(permutation);
        currentBestPermutation = new ArrayList<>();

        currentDistance = CostFunction.calcCostFunction(currentPermutation, graph);
        currentBestDistance = currentDistance;

        size = currentPermutation.size();
        counter = 0; // count permutations without improvement

        // swap
        for (int i = 0; i < graph.vNo - 2; i++) { // don't consider the last node - there is nothing to invert
            for (int j = i + 1; j < size; j++) { // check from position 'onwards'

                newPermutation = new ArrayList<>(currentPermutation);
                newDistance = currentDistance;

                // swap i and j
                newPermutation.set(j, currentPermutation.get(i));
                newPermutation.set(i, currentPermutation.get(j));

                // update distance
                newDistance -= graph.getEdge(currentPermutation.get(Math.floorMod(i - 1, size)), currentPermutation.get(i));
                newDistance -= graph.getEdge(currentPermutation.get(i), currentPermutation.get(Math.floorMod(i + 1, size)));
                if (j != Math.floorMod(i + 1, size)) {
                    newDistance -= graph.getEdge(currentPermutation.get(Math.floorMod(j - 1, size)), currentPermutation.get(j));
                }
                if (i != Math.floorMod(j + 1, size)) {
                    newDistance -= graph.getEdge(currentPermutation.get(j), currentPermutation.get(Math.floorMod(j + 1, size)));
                }
                newDistance += graph.getEdge(newPermutation.get(Math.floorMod(i - 1, size)), newPermutation.get(i));
                newDistance += graph.getEdge(newPermutation.get(i), newPermutation.get(Math.floorMod(i + 1, size)));
                if (j != Math.floorMod(i + 1, size)) {
                    newDistance += graph.getEdge(newPermutation.get(Math.floorMod(j - 1, size)), newPermutation.get(j));
                }
                if (i != Math.floorMod(j + 1, size)) {
                    newDistance += graph.getEdge(newPermutation.get(j), newPermutation.get(Math.floorMod(j + 1, size)));
                }

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
