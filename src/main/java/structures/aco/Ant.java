package structures.aco;

import org.apache.commons.math3.random.MersenneTwister;
import structures.tsp.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ant {
    /**
     * Tour created by the ant
     */
    public List<Integer> trail;
    boolean[] visited;
    int visitedNumber;
    public double trailLength;
    public Double[][] probabilityMatrix;
    MersenneTwister mt = new MersenneTwister();
    private double probTotal;

    public Ant(int vNo) {
        trail = new ArrayList<>();
        visited = new boolean[vNo];
        Arrays.fill(visited, false);
        probabilityMatrix = new Double[vNo][vNo];
        trailLength = visitedNumber = 0;
    }
    public Ant(Graph graph, int initialCity) {
        this(graph.vNo);
        addCityToTrail(initialCity, graph);
    }

    public void addCityToTrail(int city, Graph graph) {
        trail.add(city);
        visited[city] = true;
        visitedNumber++;
        // update trail length
        if (trail.size() > 1) { // not first city
            trailLength += graph.getEdge(trail.get(trail.size() - 2), city);
        }
        if (trail.size() == graph.vNo - 1) { // last city
            trailLength += graph.getEdge(trail.get(trail.size() - 1), trail.get(0));
        }
    }

    public void goToNextCity(Graph graph) {
        // update probabilities
        calculateProbabilities(graph);

        // choose next city with a certain probability
        int current = trail.get(trail.size() - 1);
        double chance = mt.nextDouble() * probTotal, sum = 0.0;

        for (int j = 1; j < graph.vNo; j++) {
            if (!visited[j]) {
                sum += probabilityMatrix[current][j];
                if (sum >= chance) { // go to this city
                    addCityToTrail(j, graph); break;
                }
            }
        }
    }

    public void calculateProbabilities(Graph graph) {
        int i = trail.get(trail.size() - 1); // get current city
        double sum = 0; // dominator
        for (int j = 1; j < graph.vNo; j++) {
            if (!visited[j] && i != j) {
                sum += Math.pow(graph.getEdgePheromones(i, j), graph.alfa) * graph.getEdgeInverted(i, j);
            }
        }

        probTotal = 0.0; // update sum of all probabilities
        for (int j = 1; j < graph.vNo; j++) { // fill probabilities matrix
            if (!visited[j] && i != j && sum > 0.0) {
                double p = ((Math.pow(graph.getEdgePheromones(i, j), graph.alfa) * graph.getEdgeInverted(i, j)) / sum);
                probabilityMatrix[i][j] = p;
                probTotal += p;
            } else { probabilityMatrix[i][j] = 0.0; }
        }
    }
}
