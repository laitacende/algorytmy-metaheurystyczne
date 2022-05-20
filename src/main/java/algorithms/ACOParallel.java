package algorithms;

import structures.Ant;
import structures.Graph;
import structures.Subcolony;
import utils.IReinforcement;

import java.util.List;

public class ACOParallel extends AbstractACO {
    private Subcolony[] subcolonies;
    private int numberOfSubcolonies = 5;

    public ACOParallel(int number) {
        numberOfSubcolonies = number;
    }

    @Override
    public void initializeAnts(Graph graph, double antsFactor) {
        if (subcolonies == null) {
            // create subcolonies
            subcolonies = new Subcolony[numberOfSubcolonies];
        }
        int size = (int) ((graph.vNo - 1) * antsFactor) / numberOfSubcolonies;
        // add equal number of ants to subcolonies
        for (int i = 0; i < numberOfSubcolonies; i++) {
            subcolonies[i] = new Subcolony(graph);
            for (int j = 0; j < size; j++) {
                subcolonies[i].addAntToSubcolony(new Ant(1.0, graph.vNo));
            }
        }

        int difference = (graph.vNo - 1) - size * numberOfSubcolonies;
        if (difference != 0) { // add additional ants to the first subcolony
            for (int i = 0; i < difference; i++) {
                subcolonies[0].addAntToSubcolony(new Ant(1.0, graph.vNo));
            }
        }
    }

    @Override
    void moveAnts(Graph graph, IReinforcement reinforcement) {
        // set reinforcement strategy
        for (Subcolony subcolony : subcolonies) {
            subcolony.reinforcement = reinforcement;
        }
        // start threads
        for (Subcolony subcolony : subcolonies) {
            subcolony.start();
        }

        // wait for every thread to finish
        for (Subcolony subcolony : subcolonies) {
            try {
                subcolony.join();
            } catch (InterruptedException e) {
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
