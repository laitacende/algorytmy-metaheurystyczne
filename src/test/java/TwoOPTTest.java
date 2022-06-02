import algorithms.KRandom;
import org.junit.Test;
import algorithms.TwoOPT;
import structures.tsp.Graph;
import utils.graph.CostFunction;
import utils.graph.GraphCreator;
import java.util.List;

public class TwoOPTTest {
    @Test
    public void test() {
        Graph g = GraphCreator.randomFullMatrix(5, 10);
        g.printAdjacencyMatrix();

        List<Integer> tour = TwoOPT.twoOpt(g, KRandom.generateRandomCycle(g));

        tour.forEach(System.out::println);
        System.out.println(CostFunction.calcCostFunction(tour, g));
    }

    @Test
    public void test2() {
        // TODO can try to assert if works on predefined matrix (dont know example to do that)
    }
}
