package structures;

import org.apache.commons.math3.random.MersenneTwister;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ant {
    /**
     * Tour created by the ant
     */
    List<Integer> trail;
    boolean[] visited;
    int visitedNumber;
    /**
     * Amount of pheromones left by the ant on trail
     */
    double pheromones;

    double trailLength;

    public Double[][] probabilityMatrix;

    MersenneTwister mt = new MersenneTwister();

    /**
     * @param pheromonesAmount
     * @param vNo number of cities plus one
     */
    public Ant(double pheromonesAmount, int vNo) {
        trail = new ArrayList<>();
        pheromones = pheromonesAmount;
        visited = new boolean[vNo];
        probabilityMatrix = new Double[vNo][vNo];
        trailLength = 0;
        visitedNumber = 0;
    }

    public void addCityToTrail(int city, Graph graph) {
        trail.add(city);
        visited[city] = true;
        visitedNumber++;
        // update trail length
        if (trail.size() > 1) { // not first city
            trailLength += graph.getEdge(trail.size() - 2, city);
        }
    }

    public void resetAnt() {
        trail.clear();
        Arrays.fill(visited, false);
        trailLength = 0;
        visitedNumber = 0;
    }

    public void goToNextCity(Graph graph) {
        calculateProbabilities(graph);
        // choose next city with a certain probability
        double rand = mt.nextDouble();
        double sum = 0;
        int current = trail.get(trail.size() - 1);
        for (int j = 1; j < graph.vNo; j++) {
            if (sum >= rand) {
                // go to this city
                addCityToTrail(j, graph);
                return;
            }
            if (!visited[j]) {
                sum += probabilityMatrix[current][j];
            }
        }
    }

    public void calculateProbabilities(Graph graph) {
        int i = trail.get(trail.size() - 1); // get current city
        double sum = 0; // dominator
        double numerator = 0;
        for (int j = 1; j < graph.vNo; j++) {
            if (!visited[j] && i != j) {
                sum += Math.pow(graph.getEdgePheromones(i, j), graph.beta) * graph.getEdgeInverted(i, j);
            }
        }

        for (int j = 1; j < graph.vNo; j++) {
            // fill probabilities matrix
            if (!visited[j] && i != j) {
                probabilityMatrix[i][j] = (Math.pow(graph.getEdgePheromones(i, j), graph.alfa) * graph.getEdgeInverted(i, j)) /
                        sum;
            } else {
                probabilityMatrix[i][j] = 0.0;
            }
        }
    }

}
