package structures.aco;

import consts.PheromoneUpdateType;
import org.apache.commons.math3.random.MersenneTwister;
import structures.tsp.Graph;
import utils.pheromone_updates.PheromoneUpdate;
import java.util.ArrayList;


public class Subcolony extends Thread {
    Graph graph;
    public ArrayList<Ant> subAnts;
    public PheromoneUpdateType updateType;
    private final MersenneTwister mt = new MersenneTwister();
    public double max = 1.0;
    public int k;


    public Subcolony(Graph graph, int k) {
        subAnts = new ArrayList<>();
        this.graph = graph;
        this.k = k;
    }

    public void addAntToSubcolony(Ant ant) {
        ant.addCityToTrail((mt.nextInt(graph.vNo - 1) + 1), graph);
        subAnts.add(ant);
    }

    private void moveAnts(int k, double max) {
        for (Ant ant : subAnts) {
            for (int i = 0; i < graph.vNo; i++) {
                ant.goToNextCity(graph);
                if (updateType == PheromoneUpdateType.BY_STEP) {
                    PheromoneUpdate.updatePheromones(updateType, graph, ant, null, 0, max, false);
                }
            }
            if (updateType == PheromoneUpdateType.DELAYED) {
                PheromoneUpdate.updatePheromones(updateType, graph, ant, null, 0, max, false);
            }
        }

        if (updateType == PheromoneUpdateType.ELITIST) {
            PheromoneUpdate.updatePheromones(updateType, graph, null, subAnts, 0, max,false);
        }

        if (updateType == PheromoneUpdateType.BY_RANK) {
            PheromoneUpdate.updatePheromones(updateType, graph, null, subAnts, k, max, false);
        }
    }

    @Override
    public void run() {
        moveAnts(k, max);
    }
}
