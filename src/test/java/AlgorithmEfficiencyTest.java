import org.junit.Test;
import algorithms.ExtendedNearestNeighbour;
import algorithms.KRandom;
import algorithms.NearestNeighbour;
import algorithms.TwoOPT;
import structures.tsp.Graph;
import utils.graph.CostFunction;
import utils.graph.GraphCreator;
import java.util.List;

public class AlgorithmEfficiencyTest {
    @Test
    public void test() {
        Graph g = GraphCreator.randomFullMatrix(10, 100);

        g.printAdjacencyMatrix();
        List<Integer> tour = NearestNeighbour.nearestNeighbour(g, 1);
        List<Integer> tour2 = TwoOPT.twoOpt(g, KRandom.generateRandomCycle(g));
        List<Integer> tour3 = ExtendedNearestNeighbour.extendedNearestNeighbour(g);
        List<Integer> tour4 = KRandom.kRandom(100, g);


        tour.forEach(System.out::println);
        System.out.println(CostFunction.calcCostFunction(tour, g));
        System.out.println();
        tour2.forEach(System.out::println);
        System.out.println(CostFunction.calcCostFunction(tour2, g));
        System.out.println();
        tour3.forEach(System.out::println);
        System.out.println(CostFunction.calcCostFunction(tour3, g));
        System.out.println();
        tour4.forEach(System.out::println);
        System.out.println(CostFunction.calcCostFunction(tour4, g));
    }
}
