package algorithms;

import consts.PheromoneUpdateType;
import consts.StopCondType;
import org.apache.commons.math3.random.MersenneTwister;
import structures.aco.Ant;
import structures.tsp.Graph;
import utils.graph.CostFunction;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractACO {
    public ArrayList<Ant> ants;
    public MersenneTwister mt = new MersenneTwister();

    /**
     * @param graph problem we are trying to solve
     * @param antsFactor linear dependency with number of cities
     * @param rho pheromones evaporation factor
     * @return best distance found
     *
     */
    public List<Integer> antColonyOptimization(Graph graph, double antsFactor, double rho, StopCondType stopCondType,
                                               int stopCondVal, PheromoneUpdateType updateType, int k,
                                               boolean useMinMax, boolean optimizeWith2OPT) {

        List<Integer> bestTour = new ArrayList<>();
        double bestDistance = Double.MAX_VALUE;
        List<Integer> currentTour;
        double currentDistance;

        // to handle stop cond
        long startTime = System.currentTimeMillis();
        int iterationsAmount = 0;

        initializePheromones(graph);

        // For Min-Max stuff
        double avg = graph.vNo.doubleValue() / 2.0;
        double nSqrPBest = 0.05;

        double min = 0.0;
        double max = 1.0;


        while (true) {
            initializeAnts(graph, antsFactor, k);
            moveAnts(graph, updateType, k, max);
            evaporatePheromones(graph, rho, min);

            // get best tour from ants
            currentTour = getBestTour();
            currentDistance = CostFunction.calcCostFunction(currentTour, graph);
            if (bestDistance > currentDistance) {
                bestTour = currentTour;
                bestDistance = currentDistance;
            }

            if (optimizeWith2OPT) {
                currentTour = TwoOPT.twoOpt(graph, List.copyOf(currentTour));
                currentDistance = CostFunction.calcCostFunction(currentTour, graph);
                if (bestDistance > currentDistance) {
                    bestTour = currentTour;
                    bestDistance = currentDistance;
                }
            }

            if (useMinMax) {
                max = (1.0 / (rho * bestDistance));
                min = max * ((1 - nSqrPBest) / ((avg - 1.0) * nSqrPBest));
            }

            // termination
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
            }
            iterationsAmount++;
        }
    }

    public void evaporatePheromones(Graph graph, double rho, double min) {
        // evaporate for each trail
        for (int i = 1; i < graph.vNo; i++) {
            for (int j = 1; j < graph.vNo; j++) {
                graph.evaporatePheromonesOnEdge(rho, i, j, min);
            }
        }
    }

    public void  initializePheromones(Graph graph) {
        for (int i = 1; i < graph.vNo; i++) {
            for (int j = 1; j < graph.vNo; j++) {
                graph.setPheromonesToEdge(1.0, i, j);
            }
        }
    }

    public abstract void initializeAnts(Graph graph, double antsFactor, int k);

    abstract void moveAnts(Graph graph, PheromoneUpdateType updateType, int k, double max);

    abstract List<Integer> getBestTour();
}
