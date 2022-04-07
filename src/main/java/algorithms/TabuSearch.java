package algorithms;

import structures.Cell;
import structures.Graph;

import java.util.*;

public class TabuSearch {
    // tabu search with extended neighbour result
    public static  List<Integer> tabuSearchENN(Graph g) {
        List<Integer> startTour = ExtendedNearestNeighbour.extendedNearestNeighbour(g);
        return tabuSearch(g, startTour);
    }

    public static  List<Integer> tabuSearch(Graph g, List<Integer> startTour) {
        // create matrix - tabu list and queue to remember the order
        Cell[][] tabuList = new Cell[g.vNo][g.vNo]; // indices from 1 to n
        Queue<Cell> queueTabu = new ArrayDeque<>();

        List<Integer> currentTour = startTour;

        // search neighbourhood


        return currentTour;
    }
}
