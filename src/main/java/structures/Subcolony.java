package structures;

import org.apache.commons.math3.random.MersenneTwister;

import java.util.ArrayList;
import java.util.List;

public class Subcolony extends Thread {
    public Ant[] subAnts;
    Graph graph;
    private MersenneTwister mt = new MersenneTwister();

    int current = 0;

    public Subcolony(Graph graph, int size) {
        subAnts = new Ant[size];
        this.graph = graph;
    }
    public void addAntToSubcolony(Ant ant) {
        subAnts[current] = ant;
        subAnts[current].resetAnt();
        subAnts[current].addCityToTrail(mt.nextInt(graph.vNo - 1) + 1, graph);
        current++;
    }

    private void moveAnts() {
        for (int i = 0; i < graph.vNo; i++) {
            for (Ant ant : subAnts) {
                ant.goToNextCity(graph);
            }
        }
    }

    @Override
    public void run() {
        moveAnts();
    }
}
