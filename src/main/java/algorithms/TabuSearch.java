package algorithms;

import structures.Graph;
import structures.TabuList;
import utils.CostFunction;
import utils.AbstractNeighbourhood;

import java.util.*;

public class TabuSearch {
    private static final int TABU_SIZE = 13;

    // tabu search with extended neighbour result
    public static  List<Integer> tabuSearchENN(Graph graph, AbstractNeighbourhood neighbourhood) {
        List<Integer> startTour = ExtendedNearestNeighbour.extendedNearestNeighbour(graph);
        return tabuSearch(graph, startTour, neighbourhood, TabuStopCondType.ITERATIONS_AMOUNT, 10000);
    }

    // tabu search with KRandom result
    public static  List<Integer> tabuSearchKR(Graph graph, AbstractNeighbourhood neighbourhood) {
        List<Integer> startTour = KRandom.generateRandomCycle(graph);
        return tabuSearch(graph, startTour, neighbourhood, TabuStopCondType.ITERATIONS_AMOUNT, 10000);
    }

    public static  List<Integer> tabuSearch(Graph graph, List<Integer> startTour, AbstractNeighbourhood neighbourhood,
                                            TabuStopCondType stopCondType, int stopCondVal) {

        long startTime = System.currentTimeMillis();

        TabuList tabuList = new TabuList(graph.vNo, TABU_SIZE);
        Integer[] indexes = new Integer[2];
        List<Integer> currentTour = startTour;
        List<Integer> bestTour = startTour;
        Double bestDistance = CostFunction.calcCostFunction(startTour, graph);

        int iterationsAmount = 0;
        int noImprovementCounter = 0;

        while (true) {

            // search neighbourhood
            currentTour = neighbourhood.getBestNeighbour(currentTour, graph, indexes, tabuList, bestDistance, 5, 1000);

            // if found new best tour save it
            if (CostFunction.calcCostFunction(currentTour, graph) < CostFunction.calcCostFunction(bestTour, graph)) {
                bestTour = currentTour;
                bestDistance = CostFunction.calcCostFunction(bestTour, graph);
                noImprovementCounter = 0;
            }
            else {
                noImprovementCounter++;

                // When no good solution found in a really long time
                if (noImprovementCounter > stopCondVal) { // might be different condition
                    currentTour = KRandom.generateRandomCycle(graph);
                    continue;
                }
            }

            // add move to tabu list
            tabuList.addToTabuList(indexes[0], indexes[1]);

            // checking whether to stop the algorithm
            switch (stopCondType) {
                case TIME_STOP_COND -> {
                    if ((System.currentTimeMillis() - startTime) >= stopCondVal) {
                        return bestTour;
                    }
                }
                case ITERATIONS_AMOUNT -> {
                    if (iterationsAmount >= stopCondVal) {
                        return bestTour;
                    }
                }
                case NO_IMPROVEMENT -> {
                    if (noImprovementCounter >= stopCondVal) {
                        return bestTour;
                    }
                }
            }

            iterationsAmount++;
        }
    }
}
