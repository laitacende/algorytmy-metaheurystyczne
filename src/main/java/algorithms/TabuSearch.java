package algorithms;

import structures.Cell;
import structures.Graph;
import utils.INeighbourhood;

import java.util.*;

public class TabuSearch {
    // tabu search with extended neighbour result
    public static  List<Integer> tabuSearchENN(Graph g, INeighbourhood neighbourhood) {
        List<Integer> startTour = ExtendedNearestNeighbour.extendedNearestNeighbour(g);
        return tabuSearch(g, startTour, neighbourhood);
    }

    public static  List<Integer> tabuSearch(Graph g, List<Integer> startTour, INeighbourhood neighbourhood) {
        // create matrix - tabu list and queue to remember the order
        Cell[][] tabuList = new Cell[g.vNo][g.vNo]; // indices from 1 to n
        for (int i = 0; i < g.vNo; i++ ) {
            for (int j = 0; j < g.vNo; j++) {
                tabuList[i][j].val = false; // not on tabu list
            }
        }
        Queue<Cell> queueTabu = new ArrayDeque<>();
        int tabuSize = 13; // test it
        Integer[] indices = new Integer[2]; // indices to be pushed to tabu list

        List<Integer> currentTour = startTour;

        // search neighbourhood
        currentTour = neighbourhood.getBestNeighbour(currentTour, g, indices, tabuList);
        // add move to tabu list
        tabuList[indices[0]][indices[1]].val = false;
        queueTabu.add( tabuList[indices[0]][indices[1]]);
        // check size of tabu list
        if (queueTabu.size() > tabuSize) {
            queueTabu.poll().val = false; // delete from tabu list
        }

        return currentTour;
    }
}
