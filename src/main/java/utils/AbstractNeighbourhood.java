package utils;

import structures.Graph;
import structures.TabuList;
import java.util.List;


public abstract class AbstractNeighbourhood {
    List<Integer> currentPermutation;
    List<Integer> currentBestPermutation;
    List<Integer> newPermutation;

    Double currentDistance;
    Double currentBestDistance;
    Double newDistance;

    int counter;
    int size;

    public abstract List<Integer> getBestNeighbour(List<Integer> permutation, Graph graph, Integer[] indexes,
                                                   TabuList tabuList, int percent, int maxCount);

}
