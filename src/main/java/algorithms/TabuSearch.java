package algorithms;

import custom_exceptions.NoNewNeighbourException;
import structures.BacktrackList;
import structures.Coordinates;
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
        return tabuSearch(graph, startTour, neighbourhood, TabuStopCondType.ITERATIONS_AMOUNT,
                TabuRestartType.BACKTRACK, 10, 5, 100, 10);
    }

    // tabu search with KRandom result
    public static  List<Integer> tabuSearchKR(Graph graph, AbstractNeighbourhood neighbourhood) {
        List<Integer> startTour = KRandom.generateRandomCycle(graph);
        return tabuSearch(graph, startTour, neighbourhood, TabuStopCondType.ITERATIONS_AMOUNT,
                TabuRestartType.BACKTRACK, 1000, 5, 100, 0);
    }

    public static  List<Integer> tabuSearch(Graph graph, List<Integer> startTour, AbstractNeighbourhood neighbourhood,
                                            TabuStopCondType stopCondType, TabuRestartType restartType, int stopCondVal, int percent, int maxCount,
                                            int backTrackVal) {

        long startTime = System.currentTimeMillis();

        TabuList tabuList = new TabuList(graph.vNo, TABU_SIZE);
        Integer[] indexes = new Integer[2];
        List<Integer> currentTour = startTour;
        List<Integer> bestTour = startTour;
        Double bestDistance = CostFunction.calcCostFunction(startTour, graph);
        // needed for restart with nearest neighbour
        int nextNeighbour = 1;
        int initNode = startTour.get(0);

        // needed for backtracking restart
        BacktrackList backtrackList = new BacktrackList(TABU_SIZE);
        int addNextMove = -1;

        backtrackList.addToBacktrackList(currentTour);
        addNextMove = 0;

        int iterationsAmount = 0;
        int noImprovementCounter = 0;

        while (true) {
            // search neighbourhood
            try {
                currentTour = neighbourhood.getBestNeighbour(currentTour, graph, indexes, tabuList, bestDistance, percent, maxCount);

                // if restart type is backtracking and solution is 'promising'
                if (restartType == TabuRestartType.BACKTRACK && (Math.abs(neighbourhood.getCurrentBestDistance() - bestDistance)) / bestDistance > (double) backTrackVal / 100) {
                    backtrackList.addToBacktrackList(currentTour);
                    addNextMove = 0;
                }

                // add move to tabu list
                tabuList.addToTabuList(indexes[0], indexes[1]);
                // == 1 - iteration after adding permutation to backtrack list
                if (restartType == TabuRestartType.BACKTRACK && addNextMove == 1) {
                    backtrackList.addMove(indexes[0], indexes[1]);
                    addNextMove = -1;
                }

                if (addNextMove != -1) {
                    addNextMove++;
                }
            }
            catch (NoNewNeighbourException e) {
                // when no new neighbour found due to tabu list
                // clear tabu list
                tabuList.clearTabuList();
                if (restartType == TabuRestartType.RANDOM) {
                    // start from new, random solution
                    currentTour = KRandom.generateRandomCycle(graph);
                } else if (restartType == TabuRestartType.NEIGHBOUR) {
                    // get nearest neighbour solution starting with node not yet used
                    if (nextNeighbour == initNode) {
                        nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;
                    }
                    currentTour = NearestNeighbour.nearestNeighbour(graph, nextNeighbour);
                    nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;
                } else if (restartType == TabuRestartType.BACKTRACK && addNextMove == -1) { // backtrack to previous 'promising' solution
                    // get permutation from backtracking list and add move that was performed after on tabu list
                    // to avoid following the same path again
                    currentTour = backtrackList.getPermutation();
                    if (currentTour != null ) {
                        Coordinates move = backtrackList.getMove();
                        tabuList.addToTabuList(move.x, move.y);
                    } else {
                        currentTour = KRandom.generateRandomCycle(graph);
                    }
                }
            }

            // if found new best tour save it
            if (CostFunction.calcCostFunction(currentTour, graph) < bestDistance) {
                bestTour = currentTour;
                bestDistance = CostFunction.calcCostFunction(bestTour, graph);
                noImprovementCounter = 0;
            }
            else {
                noImprovementCounter++;

                // When no good solution found in a really long time
                if (noImprovementCounter > stopCondVal && stopCondType == TabuStopCondType.NO_IMPROVEMENT) {
                    // clear tabu list
                    tabuList.clearTabuList();
                    if (restartType == TabuRestartType.RANDOM) {
                        // start from new, random solution
                        currentTour = KRandom.generateRandomCycle(graph);
                    } else if (restartType == TabuRestartType.NEIGHBOUR) {
                        // get nearest neighbour solution starting with node not yet used
                        if (nextNeighbour == initNode) {
                            nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;
                        }
                        currentTour = NearestNeighbour.nearestNeighbour(graph, nextNeighbour);
                        nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;
                    } else if (restartType == TabuRestartType.BACKTRACK) { // backtrack to previous 'promising' solution
                        // get permutation from backtracking list and add move that was performed after on tabu list
                        // to avoid following the same path again
                        currentTour = backtrackList.getPermutation();
                        if (currentTour != null) {
                            Coordinates move = backtrackList.getMove();
                            tabuList.addToTabuList(move.x, move.y);
                        } else {
                            currentTour = KRandom.generateRandomCycle(graph);
                        }
                    }
                }
            }



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
