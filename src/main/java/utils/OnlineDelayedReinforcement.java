package utils;

import structures.Ant;
import structures.Graph;

public class OnlineDelayedReinforcement implements IReinforcement {
    @Override
    public void updatePheromones(Ant ant, Graph graph) {
        // update pheromones based on quality of this tour
        int size = ant.trail.size();
        double amount;
        if (ant.trailLength != 0) {
            amount = 1.0 / ant.trailLength;
        } else {
            amount = 1.0;
        }
        // iterate over edges of the tour
        for (int i = 0; i < size; i++) {
            graph.increasePheromonesOnEdge(amount, ant.trail.get(i), ant.trail.get((i + 1) % size));
        }
    }

}
