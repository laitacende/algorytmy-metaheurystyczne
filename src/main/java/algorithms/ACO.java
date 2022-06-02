package algorithms;

import consts.PheromoneUpdateType;
import structures.aco.Ant;
import structures.tsp.Graph;
import utils.pheromone_updates.PheromoneUpdate;
import java.util.ArrayList;
import java.util.List;

// Ant Colony Optimization
public class ACO extends AbstractACO {
    @Override
    public void initializeAnts(Graph graph, double antsFactor) {
        // create ants
        ants = new ArrayList<>();
        for (int i = 0; i < ((graph.vNo - 1) *  antsFactor); i++) {
            ants.add(new Ant(graph, (mt.nextInt(graph.vNo - 1) + 1)));
        }
    }

    @Override
    public void moveAnts(Graph graph, PheromoneUpdateType updateType) {
        for (Ant ant : ants) {
            for (int i = 0; i < graph.vNo; i++) {
                ant.goToNextCity(graph);
                if (updateType == PheromoneUpdateType.BY_STEP) {
                    PheromoneUpdate.updatePheromones(updateType, graph, ant, null, false);
                }
            }
            if (updateType == PheromoneUpdateType.DELAYED) {
                PheromoneUpdate.updatePheromones(updateType, graph, ant, null, false);
            }
        }

        if (updateType == PheromoneUpdateType.ELITIST) {
            PheromoneUpdate.updatePheromones(updateType, graph, null, ants, false);
        }

        if (updateType == PheromoneUpdateType.BY_RANK) {
            PheromoneUpdate.updatePheromones(updateType, graph, null, ants, false);
        }
    }

    @Override
    List<Integer> getBestTour() {
        double best = Double.MAX_VALUE;
        List<Integer> bestTour = null;
        // find ant with best tour length
        for (Ant ant: ants) {
            if (ant.trailLength < best) {
                best = ant.trailLength;
                bestTour = ant.trail;
            }
        }
        return bestTour;
    }
}
