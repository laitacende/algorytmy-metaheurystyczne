package utils;

import structures.Ant;
import structures.Graph;

public class OnlineStepByStepReinforcement implements IReinforcement {

    @Override
    public void updatePheromones(Ant ant, Graph graph) {
        if (ant.trail.size() > 1) {
            int source;
            int destination;
            if (ant.trail.size() != graph.vNo - 1) {
                source = ant.trail.get(ant.trail.size() - 2);
                destination = ant.trail.get(ant.trail.size() - 1);
            } else {
                source = ant.trail.get(ant.trail.size() - 1);
                destination = ant.trail.get(0);
            }

            // update pheromones on this road
            double amount;
            if (graph.getEdge(source, destination) != 0) {
                amount = 1.0 / graph.getEdge(source, destination);
            } else {
                amount = 1.0;
            }
            //System.out.println(amount + "------------------------------------------");
            graph.increasePheromonesOnEdge(amount, source, destination);
        }
    }
}
