package algorithms;

import structures.Ant;
import structures.Graph;
import utils.IReinforcement;
import utils.OnlineDelayedReinforcement;
import utils.OnlineStepByStepReinforcement;

import java.util.ArrayList;
import java.util.List;

// Ant Colony Optimization
public class ACO extends AbstractACO {

    @Override
    public void initializeAnts(Graph graph, double antsFactor) {
        // create ants
        if (ants == null)
            ants = new Ant[(int) ((graph.vNo - 1) * antsFactor)];
            //ants = new Ant[1];
        // give each ant a random starting point
        for (int i = 0; i < ants.length; i++) {
            ants[i] = new Ant(1.0, graph.vNo); // TODO find out amount of pheromones
            ants[i].resetAnt();
            ants[i].addCityToTrail(mt.nextInt(graph.vNo - 1) + 1, graph);
        }
    }

    @Override
    public void moveAnts(Graph graph, IReinforcement reinforcement) {
        for (Ant ant : ants) {
            for (int i = 0; i < graph.vNo; i++) {
                ant.goToNextCity(graph);
                if (reinforcement instanceof OnlineStepByStepReinforcement) {
                    reinforcement.updatePheromones(ant, graph);
                }
            }
            if (reinforcement instanceof OnlineDelayedReinforcement) {
                reinforcement.updatePheromones(ant, graph);
            }
        }
//        if (reinforcement instanceof OnlineDelayedReinforcement) {
//            for (Ant ant : ants) {
//                for (int i = 0; i < graph.vNo; i++) {
//                    ant.goToNextCity(graph);
//                }
//                reinforcement.updatePheromones(ant, graph);
//            }
//        } else {
//            for (int i = 0; i < graph.vNo; i++) {
//                for (Ant ant : ants) {
//                    ant.goToNextCity(graph);
//                }
//            }
//        }
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
