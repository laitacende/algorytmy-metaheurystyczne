package utils.neighbourhoods;

import custom_exceptions.NoNewNeighbourException;
import structures.tsp.Graph;
import structures.tabu.TabuList;
import java.util.List;


public abstract class AbstractNeighbourhood {
    List<Integer> initialPermutation;
    List<Integer> currentBestPermutation;
    List<Integer> newPermutation;

    Double initialDistance;
    Double currentBestDistance;
    Double newDistance;

    int counter;
    int size;

    public abstract List<Integer> getBestNeighbour(List<Integer> permutation, Graph graph, Integer[] indexes, TabuList tabuList,
                                                   Double globalBestDistance, int improvementPercent, int postImprovementCount) throws NoNewNeighbourException;

}
