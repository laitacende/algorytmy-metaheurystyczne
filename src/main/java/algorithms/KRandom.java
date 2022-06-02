package algorithms;
import structures.tsp.Graph;
import utils.graph.CostFunction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KRandom {
    public static List<Integer> kRandom(int numberOfChecks, Graph graph) {
        List<Integer> bestTour = generateRandomCycle(graph);
        Double bestDistance = CostFunction.calcCostFunction(bestTour, graph);

        int i = 0;
        while (i < numberOfChecks) {
            List<Integer> randomCycle = generateRandomCycle(graph);
            Double randomCycleDistance = CostFunction.calcCostFunction(randomCycle, graph);
            if (randomCycleDistance < bestDistance) {
                bestTour = randomCycle;
                bestDistance = randomCycleDistance;
            }
            i++;
        }
        return bestTour;
    }

    public static List<Integer> generateRandomCycle(Graph graph) {
        ArrayList<Integer> randomSolution = new ArrayList<>();

        for (int i = 1; i < graph.getSize(); i++) {
            randomSolution.add(i);
        }
        Collections.shuffle(randomSolution);

        return randomSolution;
    }
}


