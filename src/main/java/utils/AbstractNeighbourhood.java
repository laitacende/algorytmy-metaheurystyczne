package utils;

import structures.Cell;
import structures.Graph;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNeighbourhood {
    List<Integer> newPermutation;
    List<Integer> currentPermutation;
    Double currentDistance;
    Double newDistance;
    Double newDistanceBest;
    List<Integer> newPermutationBest;
    int size;
    int counter;
    public abstract List<Integer> getBestNeighbour(List<Integer> permutation, Graph g,  Integer[] indices,
                                                   Cell[][] tabuList, int percent, int maxCount);
}
