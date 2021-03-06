package utils.pheromone_updates;

import consts.PheromoneUpdateType;
import structures.aco.Ant;
import structures.tsp.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


public class PheromoneUpdate {
    public static void updatePheromones(
            PheromoneUpdateType updateType, Graph graph,
            Ant ant, ArrayList<Ant> ants, int k,
            double max, boolean delWorst
    ) {
        switch (updateType) {
            case ELITIST -> elitistUpdate(graph, ants, delWorst, max); // offline
            case BY_RANK -> rankBasedUpdate(graph, ants, delWorst, k, max); // offline
            case BY_STEP -> byStepUpdate(graph, ant, max); // online
            case DELAYED -> delayedUpdate(graph, ant, max); // online
        }
    }

    private static void elitistUpdate(Graph graph, ArrayList<Ant> ants, boolean delWorst, double max) {
        List<Ant> elitistAnts = new LinkedList<>();
        int bestTrailLength = Integer.MAX_VALUE;

        for (Ant ant : ants) {
            int currTrailLength = (int) ant.trailLength;
            if (currTrailLength < bestTrailLength) {
                bestTrailLength = currTrailLength;
            }
        }

        for (Ant ant : ants) {
            if ((int) ant.trailLength == bestTrailLength) {
                elitistAnts.add(ant);
            }
        }

        for (Ant elitistAnt : elitistAnts) {
            delayedUpdate(graph, elitistAnt, max);
        }

        if (delWorst) {
            int worstTrailLen = 0;
            for (Ant ant : ants) {
                int currTrailLength = (int) ant.trailLength;
                if (currTrailLength > worstTrailLen) {
                    worstTrailLen = currTrailLength;
                }
            }

            for (Ant ant : ants) {
                if ((int) ant.trailLength == worstTrailLen) {
                    decreaseWorst(graph, ant); break;
                }
            }
        }
    }


    private static void rankBasedUpdate(Graph graph, ArrayList<Ant> ants, boolean delWorst, int k, double max) {
        ants.sort(Comparator.comparingDouble(ant -> ant.trailLength));

        int rank = ants.size();
        for (Ant ant : ants) {
            double amount = (ant.trailLength > 0.0) ? (rank * (1.0 / ant.trailLength)) : 0.0;
            for (int i = 0; i < ant.trail.size(); i++) {
                graph.increasePheromonesOnEdge(amount, ant.trail.get(i), ant.trail.get((i + 1) % ant.trail.size()), max);
            }
            rank --;
        }

//        for (int j = 0; j < k; j++) {
//            double amount = (ants.get(j).trailLength > 0.0) ? (j * (1.0 / ants.get(j).trailLength)) : 0.0;
//            for (int i = 0; i < ants.get(j).trail.size(); i++) {
//                graph.increasePheromonesOnEdge(amount, ants.get(j).trail.get(i), ants.get(j).trail.get((i + 1) % ants.get(j).trail.size()), max);
//            }
//        }

        if (delWorst) {
            int worstTrailLen = 0;
            for (Ant ant : ants) {
                int currTrailLength = (int) ant.trailLength;
                if (currTrailLength > worstTrailLen) {
                    worstTrailLen = currTrailLength;
                }
            }

            for (Ant ant : ants) {
                if ((int) ant.trailLength == worstTrailLen) {
                    decreaseWorst(graph, ant); break;
                }
            }
        }
    }


    private static void byStepUpdate(Graph graph, Ant ant, double max) {
        if (ant.trail.size() > 1) {
            int source, destination;
            if (ant.trail.size() != graph.vNo - 1) {
                source = ant.trail.get(ant.trail.size() - 2);
                destination = ant.trail.get(ant.trail.size() - 1);
            } else {
                source = ant.trail.get(ant.trail.size() - 1);
                destination = ant.trail.get(0);
            }

            // update pheromones on this road
            double amount = (graph.getEdge(source, destination) > 0.0) ? (1.0 / graph.getEdge(source, destination)) : 0.0;
            graph.increasePheromonesOnEdge(amount, source, destination, max);
        }
    }


    private static void delayedUpdate(Graph graph, Ant ant, double max) {
        double amount = (ant.trailLength > 0.0) ? (1.0 / ant.trailLength) : 0.0;
        for (int i = 0; i < ant.trail.size(); i++) {
            graph.increasePheromonesOnEdge(amount, ant.trail.get(i), ant.trail.get((i + 1) % ant.trail.size()), max);
        }
    }

    private static void decreaseWorst(Graph graph, Ant ant) {
        for (int i = 0; i < ant.trail.size(); i++) {
            graph.evaporatePheromonesOnEdge(0.5, ant.trail.get(i), ant.trail.get((i + 1) % ant.trail.size()));
        }
    }
}
