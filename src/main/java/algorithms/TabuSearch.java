package algorithms;

import custom_exceptions.NoNewNeighbourException;
import structures.BacktrackList;
import structures.Graph;
import structures.TabuList;
import utils.*;
import java.util.*;

public class TabuSearch {
    private static final int TABU_SIZE = 13;

    // tabu search with extended neighbour result
    public static  List<Integer> tabuSearchENN(Graph graph,AbstractNeighbourhood neighbourhood, TabuStopCondType stopCondType, TabuRestartType restartType,
                                               int stopCondVal, int improvementPercent, int postImprovementCount, int backtrackMoves, boolean changeNeighbourhood) {
        List<Integer> startTour = ExtendedNearestNeighbour.extendedNearestNeighbour(graph);
        return tabuSearch(graph, startTour, neighbourhood, stopCondType, restartType, stopCondVal, improvementPercent, postImprovementCount, backtrackMoves, changeNeighbourhood);
    }

    public static  List<Integer> tabuSearchENN(Graph graph, TabuStopCondType stopCondType) {
        return tabuSearchENN(graph, new InvertNeighbourhood(), stopCondType, TabuRestartType.BACKTRACK,
                10 * graph.vNo, 3, 100, 3, true);
    }

    public static  List<Integer> tabuSearchENN(Graph graph, int improvementPercent, int backtrackMoves, boolean changeN) {
        return tabuSearchENN(graph, new InvertNeighbourhood(), TabuStopCondType.ITERATIONS_AMOUNT, TabuRestartType.BACKTRACK,
                10 * graph.vNo, improvementPercent, 100, backtrackMoves, changeN);
    }

    public static  List<Integer> tabuSearchENN(Graph graph, boolean changeN) {
        return tabuSearchENN(graph, new InvertNeighbourhood(), TabuStopCondType.ITERATIONS_AMOUNT, TabuRestartType.BACKTRACK,
                10 * graph.vNo, 3, 100, 3, changeN);
    }


    // tabu search with KRandom result
    public static  List<Integer> tabuSearchKR(Graph graph, AbstractNeighbourhood neighbourhood, TabuRestartType restartType, boolean changeNeighbourhood) {
        List<Integer> startTour = KRandom.generateRandomCycle(graph);
        return tabuSearch(graph, startTour, neighbourhood, TabuStopCondType.ITERATIONS_AMOUNT,
                restartType, 10 * graph.vNo, 100, 50, 2, changeNeighbourhood);
    }



    public static  List<Integer> tabuSearch(Graph graph, List<Integer> startTour, AbstractNeighbourhood neighbourhood, TabuStopCondType stopCondType, TabuRestartType restartType,
                                            int stopCondVal, int improvementPercent, int postImprovementCount, int backtrackMoves, boolean changeNeighbourhood) {

        Random rand = new Random();
        List<Integer> currentTour = startTour;
        Double currentDistance = CostFunction.calcCostFunction(startTour, graph);
        List<Integer> bestTour = startTour;
        Double bestDistance = currentDistance;

        // To handle tabu list
        TabuList tabuList = new TabuList(graph.vNo, TABU_SIZE);
        Integer[] indexes = new Integer[2];

        // needed for restart with nearest neighbour
        int nextNeighbour = 1;
        int initNode = startTour.get(0);

        // needed for backtracking restart
        BacktrackList backtrackList = new BacktrackList(20);
        backtrackList.addPermutation(currentTour);
        int addNextMove = 0; // start counting

        // to change neighbours
        int neighbourhoodCounter;
        if (neighbourhood.getClass() == InsertNeighbourhood.class)
            neighbourhoodCounter = 0;
        else if (neighbourhood.getClass() == InvertNeighbourhood.class)
            neighbourhoodCounter = 1;
        else
            neighbourhoodCounter = 2;

        // to handle stop cond
        long startTime = System.currentTimeMillis();
        int noImprovementCounter = 0;
        int iterationsAmount = 0;

        while (true) {
            try {
                // search for best solution in neighbourhood
                currentTour = neighbourhood.getBestNeighbour(currentTour, graph, indexes, tabuList, bestDistance, improvementPercent, postImprovementCount);
                tabuList.addToTabuList(indexes[0], indexes[1]); // add move to tabu list
            }
            catch (NoNewNeighbourException ex) {
                // when no new neighbour found due to tabu list
                tabuList.resetTabuList();
                if (restartType == TabuRestartType.RANDOM) {
                    currentTour = KRandom.generateRandomCycle(graph);
                }
                else if (restartType == TabuRestartType.NEIGHBOUR) {
                    // get nearest neighbour solution starting with node not yet used
                    if (nextNeighbour == initNode)
                        nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;

                    currentTour = NearestNeighbour.nearestNeighbour(graph, nextNeighbour);
                    nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;
                }
                else if (restartType == TabuRestartType.BACKTRACK && addNextMove == -1) {
                    // backtrack to previous 'promising' solution
                    currentTour = backtrackList.getPermutation();
                    if (currentTour != null ) {
                        tabuList = backtrackList.getTabuList(); // restore solution tabu list
                    }
                    else {
                        // get nearest neighbour solution starting with node not yet used
                        if (nextNeighbour == initNode)
                            nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;

                        currentTour = NearestNeighbour.nearestNeighbour(graph, nextNeighbour);
                        nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;
                    }
                }
                if (changeNeighbourhood) {
                    neighbourhoodCounter = (neighbourhoodCounter + 1) % 3 ;
                    neighbourhood = changeNeighbourhood(neighbourhoodCounter);
                }
            }

            currentDistance = CostFunction.calcCostFunction(currentTour, graph);

            // if found new best tour save it
            if (currentDistance < bestDistance) {
                bestTour = currentTour;
                bestDistance = currentDistance;
                noImprovementCounter = 0;

                // if restart type is backtracking and solution is better than the current one
                if (addNextMove == -1 && restartType == TabuRestartType.BACKTRACK) {
                    backtrackList.addPermutation(currentTour);
                    addNextMove = 0;
                }
            }
            else {
                noImprovementCounter++;

                // When no good solution found in a really long time
                if (noImprovementCounter > stopCondVal / 5 && stopCondType != TabuStopCondType.NO_IMPROVEMENT) {
                    // reset counter
                    noImprovementCounter = 0;

                    // optional, one of these resets
//                    currentTour = NearestNeighbour.nearestNeighbour(graph, rand.nextInt(graph.vNo - 1) + 1);
//                    currentTour = KRandom.generateRandomCycle(graph);

                    tabuList.resetTabuList();
                    if (restartType == TabuRestartType.RANDOM) {
                        currentTour = KRandom.generateRandomCycle(graph);
                    }
                    else if (restartType == TabuRestartType.NEIGHBOUR) {
                        // get nearest neighbour solution starting with node not yet used
                        if (nextNeighbour == initNode)
                            nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;

                        currentTour = NearestNeighbour.nearestNeighbour(graph, nextNeighbour);
                        nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;
                    }
                    else if (restartType == TabuRestartType.BACKTRACK && addNextMove == -1) {
                        // backtrack to previous 'promising' solution
                        currentTour = backtrackList.getPermutation();
                        if (currentTour != null ) {
                            tabuList = backtrackList.getTabuList(); // restore solution tabu list
                        }
                        else {
                            // get nearest neighbour solution starting with node not yet used
                            if (nextNeighbour == initNode)
                                nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;

                            currentTour = NearestNeighbour.nearestNeighbour(graph, nextNeighbour);
                            nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;
                        }
                    }

                    if (changeNeighbourhood) {
                        neighbourhoodCounter = (neighbourhoodCounter + 1) % 3 ;
                        neighbourhood = changeNeighbourhood(neighbourhoodCounter);
                    }
                }
            }

            if (addNextMove != -1) {
                addNextMove++;
            }
            // there were sufficient iterations to add new value to backtracking list
            if (restartType == TabuRestartType.BACKTRACK && addNextMove == backtrackMoves) {
                backtrackList.addTabuList(tabuList);
                addNextMove = -1;
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

    private static AbstractNeighbourhood changeNeighbourhood(int neighbourhoodCounter) {
        if (neighbourhoodCounter == 0) {
             return new InsertNeighbourhood();
        }
        else if (neighbourhoodCounter == 1) {
            return new InvertNeighbourhood();
        }
        else {
            return new SwapNeighbourhood();
        }
    }
}
