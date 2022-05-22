package structures;

import org.apache.commons.math3.random.MersenneTwister;
import utils.IReinforcement;
import utils.OnlineDelayedReinforcement;
import utils.OnlineStepByStepReinforcement;

import java.util.ArrayList;
import java.util.List;

public class Subcolony extends Thread {
    public List<Ant> subAnts;
    Graph graph;
    private MersenneTwister mt = new MersenneTwister();

    public IReinforcement reinforcement;

    public Subcolony(Graph graph) {
        subAnts = new ArrayList<>();
        this.graph = graph;
    }
    public void addAntToSubcolony(Ant ant) {
        subAnts.add(ant);
        subAnts.get(subAnts.size() - 1).resetAnt();
        subAnts.get(subAnts.size() - 1).addCityToTrail(mt.nextInt(graph.vNo - 1) + 1, graph);
    }

    private void moveAnts() {
        for (Ant ant : subAnts) {
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
//            for (Ant ant : subAnts) {
//                for (int i = 0; i < graph.vNo; i++) {
//                    ant.goToNextCity(graph);
//                }
//                reinforcement.updatePheromones(ant, graph);
//            }
//        } else {
//            for (int i = 0; i < graph.vNo; i++) {
//                for (Ant ant : subAnts) {
//                    ant.goToNextCity(graph);
//                }
//            }
//        }
    }

    @Override
    public void run() {
        moveAnts();
    }
}
