package utils;

import structures.Cell;
import structures.Graph;

import java.util.List;

public interface INeighbourhood {
    public List<Integer> getBestNeighbour(List<Integer> permutation, Graph g,  Integer[] indices, Cell[][] tabuList);
}
