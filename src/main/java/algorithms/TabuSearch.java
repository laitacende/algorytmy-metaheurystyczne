package algorithms;

import custom_exceptions.NoNewNeighbourException;
import structures.BacktrackList;
import structures.Coordinates;
import structures.Graph;
import structures.TabuList;
import utils.*;

import java.util.*;

public class TabuSearch {
    private static final int TABU_SIZE = 13;

    // tabu search with extended neighbour result
    public static  List<Integer> tabuSearchENN(Graph graph, AbstractNeighbourhood neighbourhood, TabuRestartType restartType,
                                               boolean changeNeighbourhood, int tabuListSize) {
        List<Integer> startTour = ExtendedNearestNeighbour.extendedNearestNeighbour(graph);
        return tabuSearch(graph, startTour, neighbourhood, TabuStopCondType.ITERATIONS_AMOUNT,
                restartType, 1000, 5, 100, 10, changeNeighbourhood, tabuListSize);
    }

    // tabu search with KRandom result
    public static  List<Integer> tabuSearchKR(Graph graph, AbstractNeighbourhood neighbourhood, TabuRestartType restartType,
                                              boolean changeNeighbourhood, int tabuListSize) {
        List<Integer> startTour = KRandom.generateRandomCycle(graph);
        return tabuSearch(graph, startTour, neighbourhood, TabuStopCondType.ITERATIONS_AMOUNT,
                restartType, 1000, 5, 100, 10, changeNeighbourhood, tabuListSize);
    }

    public static  List<Integer> tabuSearch(Graph graph, List<Integer> startTour, AbstractNeighbourhood neighbourhood,
                                            TabuStopCondType stopCondType, TabuRestartType restartType, int stopCondVal, int percent, int maxCount,
                                            int backtrackMoves, boolean changeNeighbourhood, int tabuListSize) {

        long startTime = System.currentTimeMillis();

        Random rand = new Random();

        TabuList tabuList = new TabuList(graph.vNo, tabuListSize);
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

        backtrackList.addPermutation(currentTour);
        addNextMove = 0; // start counting

        int iterationsAmount = 0;
        int noImprovementCounter = 0;

        int neighbourhoodCounter = 0;
        if (neighbourhood.getClass() == InsertNeighbourhood.class) {
            neighbourhoodCounter = 0;
        } else if (neighbourhood.getClass() == InvertNeighbourhood.class) {
            neighbourhoodCounter = 1;
        } else if (neighbourhood.getClass() == SwapNeighbourhood.class) {
            neighbourhoodCounter = 2;
        }

        while (true) {
            // search neighbourhood
            try {
                currentTour = neighbourhood.getBestNeighbour(currentTour, graph, indexes, tabuList, bestDistance, percent, maxCount);

                // add move to tabu list
                tabuList.addToTabuList(indexes[0], indexes[1]);

            }
            catch (NoNewNeighbourException e) {
                // when no new neighbour found due to tabu list
                // clear tabu list
                tabuList.clearTabuList();
                if (restartType == TabuRestartType.RANDOM) {
                    // start from new, random solution
                    //currentTour = NearestNeighbour.nearestNeighbour(graph, rand.nextInt(graph.vNo - 1) + 1);
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
                        tabuList = backtrackList.getTabuList();
                    } else {
                        currentTour = NearestNeighbour.nearestNeighbour(graph, rand.nextInt(graph.vNo - 1) + 1);
                    }
                }
                if (changeNeighbourhood) {
                    neighbourhoodCounter = (neighbourhoodCounter + 1) % 3 ;
                    if (neighbourhoodCounter == 0) {
                        neighbourhood = new InsertNeighbourhood();
                    } else if (neighbourhoodCounter == 1) {
                        neighbourhood = new InvertNeighbourhood();
                    } else if (neighbourhoodCounter == 2) {
                        neighbourhood = new SwapNeighbourhood();
                    }
                }
            }

            // if found new best tour save it
            if (CostFunction.calcCostFunction(currentTour, graph) < bestDistance) {
                bestTour = currentTour;
                bestDistance = CostFunction.calcCostFunction(bestTour, graph);
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
                if (noImprovementCounter > stopCondVal && stopCondType == TabuStopCondType.NO_IMPROVEMENT) {
                    // clear tabu list
                    tabuList.clearTabuList();
                    if (restartType == TabuRestartType.RANDOM) {
                        // start from new, random solution
                        //currentTour = NearestNeighbour.nearestNeighbour(graph, rand.nextInt(graph.vNo - 1) + 1);
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
                        if (currentTour != null ) {
                            tabuList = backtrackList.getTabuList();
                        } else {
                            currentTour = NearestNeighbour.nearestNeighbour(graph, rand.nextInt(graph.vNo - 1) + 1);
                        }
                    }

                    if (changeNeighbourhood) {
                        neighbourhoodCounter = (neighbourhoodCounter + 1) % 3 ;
                        if (neighbourhoodCounter == 0) {
                            neighbourhood = new InsertNeighbourhood();
                        } else if (neighbourhoodCounter == 1) {
                            neighbourhood = new InvertNeighbourhood();
                        } else if (neighbourhoodCounter == 2) {
                            neighbourhood = new SwapNeighbourhood();
                        }
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

}
