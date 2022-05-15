package algorithms;

import org.apache.commons.math3.random.MersenneTwister;
import structures.Ant;
import structures.Graph;
import utils.AbstractNeighbourhood;

import java.util.ArrayList;
import java.util.List;

// Ant Colony Optimization
public class ACO {
    private static Ant[] ants;
    private static MersenneTwister mt = new MersenneTwister();

    /**
     * @param graph
     * @param antsFactor linear dependency with number of cities
     * @param rho pheromones evaporation factor
     * @return
     */
    public static List<Integer> antColonyOptimization(Graph graph, double antsFactor, double rho) {
        List<Integer> tour = new ArrayList<>();

        // TODO initialize pheromones on each trail (maybe all the same)

        while (true) { // TODO add termination criterion
            initializeAnts(graph, antsFactor);
            moveAnts(graph);
            evaporatePheromones(graph, rho);
            // TODO add pheromones to trails (different strategies) (reinforcement - before or after evaporation?)
        }

        return tour;
    }

    private static void initializeAnts(Graph graph, double antsFactor) {
        // create ants
        if (ants.length == 0)
            ants = new Ant[(int) (graph.vNo * antsFactor)];
        // give each ant a random starting point
        for (Ant ant: ants) {
            ant.resetAnt();
            ant.addCityToTrail(mt.nextInt(graph.vNo - 1) + 1, graph);
        }
    }

    private static void moveAnts(Graph graph) {
        for (int i = 0; i < graph.vNo; i++) {
            for (Ant ant : ants) { // TODO maybe divide them into subcolonies and make threads
                ant.goToNextCity(graph);
            }
        }
    }

    private static void evaporatePheromones(Graph graph, double rho) {
        // evaporate for each trail
        for (int i = 1; i < graph.vNo; i++) {
            for (int j = 1; j < graph.vNo; j++) {
                graph.evaporatePheromonesOnEdge(rho, i, j);
            }
        }
    }
}
