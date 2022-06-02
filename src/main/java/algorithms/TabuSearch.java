package algorithms;

import consts.StopCondType;
import consts.TabuRestartType;
import custom_exceptions.NoNewNeighbourException;
import structures.tabu.BacktrackList;
import structures.tsp.Graph;
import structures.tabu.TabuList;
import utils.graph.CostFunction;
import utils.neighbourhoods.AbstractNeighbourhood;
import utils.neighbourhoods.InsertNeighbourhood;
import utils.neighbourhoods.InvertNeighbourhood;
import utils.neighbourhoods.SwapNeighbourhood;

import java.util.List;

public class TabuSearch {
    private static final int TABU_SIZE = 13;

    // tabu search with extended neighbour result
    public static  List<Integer> tabuSearchENN(Graph graph, AbstractNeighbourhood neighbourhood, StopCondType stopCondType, TabuRestartType restartType, int tabuSize,
                                               int stopCondVal, int improvementPercent, int postImprovementCount, int backtrackMoves, boolean changeNeighbourhood) {
        List<Integer> startTour = ExtendedNearestNeighbour.extendedNearestNeighbour(graph);
        return tabuSearch(graph, startTour, neighbourhood, stopCondType, restartType, tabuSize, stopCondVal, improvementPercent, postImprovementCount, backtrackMoves, changeNeighbourhood);
    }

    public static  List<Integer> tabuSearchENN(Graph graph, StopCondType stopCondType) {
        return tabuSearchENN(graph, new InvertNeighbourhood(), stopCondType, TabuRestartType.NEIGHBOUR,
                15, 10 * graph.vNo, 3, 100, 2, true);
    }

    public static  List<Integer> tabuSearchENN(Graph graph, int tabuSize, int improvementPercent, int backtrackMoves, boolean changeN) {
        return tabuSearchENN(graph, new InvertNeighbourhood(), StopCondType.ITERATIONS_AMOUNT, TabuRestartType.NEIGHBOUR,
                tabuSize, 10 * graph.vNo, improvementPercent, 100, backtrackMoves, changeN);
    }

    public static  List<Integer> tabuSearchENN(Graph graph, boolean changeN, int tabuSize) {
        return tabuSearchENN(graph, new InvertNeighbourhood(), StopCondType.ITERATIONS_AMOUNT, TabuRestartType.BACKTRACK,
                tabuSize, 10 * graph.vNo, 3, 500, 2, changeN);
    }


    // tabu search with KRandom result
    public static  List<Integer> tabuSearchKR(Graph graph, AbstractNeighbourhood neighbourhood, TabuRestartType restartType, boolean changeNeighbourhood) {
        List<Integer> startTour = KRandom.generateRandomCycle(graph);
        return tabuSearch(graph, startTour, neighbourhood, StopCondType.ITERATIONS_AMOUNT, restartType,
                13, 10 * graph.vNo, 100, 50, 2, changeNeighbourhood);
    }



    public static  List<Integer> tabuSearch(Graph graph, List<Integer> startTour, AbstractNeighbourhood neighbourhood, StopCondType stopCondType, TabuRestartType restartType,
                                            int tabuSize, int stopCondVal, int improvementPercent, int postImprovementCount, int backtrackMoves, boolean changeNeighbourhood) {

        //Random rand = new Random();
        List<Integer> currentTour = startTour;
        Double currentDistance = CostFunction.calcCostFunction(startTour, graph);
        List<Integer> bestTour = startTour;
        Double bestDistance = currentDistance;

        // To handle tabu list
        TabuList tabuList = new TabuList(graph.vNo, tabuSize);
        Integer[] indexes = new Integer[2];

        // needed for restart with nearest neighbour
        int nextNeighbour = 1;
        int initNode = startTour.get(0);

        // to change neighbours
        int neighbourhoodCounter;
        if (neighbourhood.getClass() == InsertNeighbourhood.class)
            neighbourhoodCounter = 0;
        else if (neighbourhood.getClass() == InvertNeighbourhood.class)
            neighbourhoodCounter = 1;
        else
            neighbourhoodCounter = 2;

        // needed for backtracking restart
        BacktrackList backtrackList = new BacktrackList(50);
        backtrackList.addPermutation(currentTour, neighbourhoodCounter);
        int addNextMove = 0; // start counting


        // to handle stop cond
        long startTime = System.currentTimeMillis();
        int noImprovementCounter = 0;
        int iterationsAmount = 0;

        while (true) {
            try {
                // search for best solution in neighbourhood
                currentTour = neighbourhood.getBestNeighbour(currentTour, graph, indexes, tabuList, bestDistance, improvementPercent, postImprovementCount);
                tabuList.addToTabuList(indexes[0], indexes[1], CostFunction.calcCostFunction(currentTour, graph)); // add move to tabu list
            }
            catch (NoNewNeighbourException ex) {
                // when no new neighbour found due to tabu list
                tabuList.clearTabuList();
                if (restartType == TabuRestartType.RANDOM) {
                    currentTour = KRandom.generateRandomCycle(graph);

                    if (changeNeighbourhood) {
                        neighbourhoodCounter = (neighbourhoodCounter + 1) % 3 ;
                        neighbourhood = changeNeighbourhood(neighbourhoodCounter);
                    }
                }
                else if (restartType == TabuRestartType.NEIGHBOUR) {
                    // get nearest neighbour solution starting with node not yet used
                    if (nextNeighbour == initNode)
                        nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;

                    currentTour = NearestNeighbour.nearestNeighbour(graph, nextNeighbour);
                    nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;

                    if (changeNeighbourhood) {
                        neighbourhoodCounter = (neighbourhoodCounter + 1) % 3 ;
                        neighbourhood = changeNeighbourhood(neighbourhoodCounter);
                    }
                }
                else if (restartType == TabuRestartType.BACKTRACK && addNextMove == -1) {
                    // backtrack to previous 'promising' solution
                    currentTour = backtrackList.getPermutation();
                    if (currentTour != null ) {
                        tabuList = backtrackList.getTabuList(); // restore solution tabu list
                        neighbourhoodCounter = backtrackList.getNeighbourhood(); // restore solution neighbourhood
                        neighbourhood = changeNeighbourhood(neighbourhoodCounter);
                    }
                    else {
                        // get nearest neighbour solution starting with node not yet used
                        if (nextNeighbour == initNode)
                            nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;

                        currentTour = NearestNeighbour.nearestNeighbour(graph, nextNeighbour);
                        nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;

                        if (changeNeighbourhood) {
                            neighbourhoodCounter = (neighbourhoodCounter + 1) % 3 ;
                            neighbourhood = changeNeighbourhood(neighbourhoodCounter);
                        }
                    }
                }
            }

            currentDistance = CostFunction.calcCostFunction(currentTour, graph);

            // if found new best tour save it
            if (currentDistance < bestDistance) {
                //System.out.println("Best dist " + currentDistance + " < " + bestDistance);
                bestTour = currentTour;
                bestDistance = currentDistance;
                noImprovementCounter = 0;

                // if restart type is backtracking and solution is better than the current one
                if (addNextMove == -1 && restartType == TabuRestartType.BACKTRACK) {
                    backtrackList.addPermutation(currentTour, neighbourhoodCounter);
                    addNextMove = 0;
                }
            }
            else {
                noImprovementCounter++;

                // When no good solution found in a really long time
                if (noImprovementCounter > stopCondVal / 5 && stopCondType != StopCondType.NO_IMPROVEMENT) {
                    // reset counter
                    noImprovementCounter = 0;

                    // optional, one of these resets
//                    currentTour = NearestNeighbour.nearestNeighbour(graph, rand.nextInt(graph.vNo - 1) + 1);
//                    currentTour = KRandom.generateRandomCycle(graph);

                    tabuList.clearTabuList();
                    if (restartType == TabuRestartType.RANDOM) {
                        currentTour = KRandom.generateRandomCycle(graph);

                        if (changeNeighbourhood) {
                            neighbourhoodCounter = (neighbourhoodCounter + 1) % 3 ;
                            neighbourhood = changeNeighbourhood(neighbourhoodCounter);
                        }
                    }
                    else if (restartType == TabuRestartType.NEIGHBOUR) {
                        // get nearest neighbour solution starting with node not yet used
                        if (nextNeighbour == initNode)
                            nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;

                        currentTour = NearestNeighbour.nearestNeighbour(graph, nextNeighbour);
                        nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;

                        if (changeNeighbourhood) {
                            neighbourhoodCounter = (neighbourhoodCounter + 1) % 3 ;
                            neighbourhood = changeNeighbourhood(neighbourhoodCounter);
                        }
                    }
                    else if (restartType == TabuRestartType.BACKTRACK && addNextMove == -1) {
                        // backtrack to previous 'promising' solution
                        currentTour = backtrackList.getPermutation();
                        if (currentTour != null ) {
                            tabuList = backtrackList.getTabuList(); // restore solution tabu list
                            neighbourhoodCounter = backtrackList.getNeighbourhood(); // restore solution neighbourhood
                            neighbourhood = changeNeighbourhood(neighbourhoodCounter);
                        }
                        else {
                            // get nearest neighbour solution starting with node not yet used
                            if (nextNeighbour == initNode)
                                nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;

                            currentTour = NearestNeighbour.nearestNeighbour(graph, nextNeighbour);
                            nextNeighbour = (nextNeighbour + 1) % (graph.getSize() - 1) + 1;

                            if (changeNeighbourhood) {
                                neighbourhoodCounter = (neighbourhoodCounter + 1) % 3 ;
                                neighbourhood = changeNeighbourhood(neighbourhoodCounter);
                            }
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
                        //tabuList.printTabuList2();
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