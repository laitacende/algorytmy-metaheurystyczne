package algorithms;

import consts.PheromoneUpdateType;
import structures.aco.Ant;
import structures.aco.Subcolony;
import structures.tsp.Graph;
import java.util.List;

public class ACOParallel extends AbstractACO {
    private final int numberOfSubcolonies;
    private Subcolony[] subcolonies;

    public ACOParallel(int numberOfSubcolonies) {
        this.numberOfSubcolonies = numberOfSubcolonies;
    }

    @Override
    public void initializeAnts(Graph graph, double antsFactor, int k) {
        if (subcolonies == null) {
            // create subcolonies
            subcolonies = new Subcolony[numberOfSubcolonies];
        }
        int size = (int) ((graph.vNo - 1) * antsFactor) / numberOfSubcolonies;
        // add equal number of ants to subcolonies
        for (int i = 0; i < numberOfSubcolonies; i++) {
            subcolonies[i] = new Subcolony(graph, k);
            for (int j = 0; j < size; j++) {
                subcolonies[i].addAntToSubcolony(new Ant(graph.vNo));
            }
        }

        int difference = (graph.vNo - 1) - size * numberOfSubcolonies;
        if (difference != 0) {
            // add additional ants to the first subcolony
            for (int i = 0; i < difference; i++) {
                subcolonies[0].addAntToSubcolony(new Ant(graph.vNo));
            }
        }
    }

    @Override
    void moveAnts(Graph graph, PheromoneUpdateType updateType, int k, double max) {
        // set reinforcement strategy
        for (Subcolony subcolony : subcolonies) {
            subcolony.updateType = updateType;
            subcolony.max = max;
        }
        // start threads
        for (Subcolony subcolony : subcolonies) { subcolony.start(); }

        // wait for every thread to finish
        for (Subcolony subcolony : subcolonies) {
            try { subcolony.join(); }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    List<Integer> getBestTour() {
        double best = Double.MAX_VALUE;
        List<Integer> bestTour = null;

        // find ant with best tour length
        for (int i = 0; i < numberOfSubcolonies; i++) {
            for (Ant ant: subcolonies[i].subAnts) {
                if (ant.trailLength < best) {
                    best = ant.trailLength;
                    bestTour = ant.trail;
                }
            }
        }

        return bestTour;
    }
}
