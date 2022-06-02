import org.junit.Test;
import algorithms.ExtendedNearestNeighbour;
import algorithms.KRandom;
import org.junit.Assert;
import structures.tsp.Graph;
import utils.graph.CostFunction;
import utils.graph.GraphCreator;
import java.util.List;

public class KRandomTest {
    @Test
    public void test(){
        Graph g = GraphCreator.randomFullMatrix(5, 10);
        g.printAdjacencyMatrix();

        List<Integer> tour = ExtendedNearestNeighbour.extendedNearestNeighbour(g);

        tour.forEach(System.out::println);
        System.out.println(CostFunction.calcCostFunction(tour, g));
    }

    @Test
    public void test2() {
        Graph g = GraphCreator.randomFullMatrix(10, 50);
        g.printAdjacencyMatrix();

        List<Integer> tour1 = KRandom.kRandom(5, g);
        List<Integer> tour2 = KRandom.kRandom(1000, g);

        Double distance1 = CostFunction.calcCostFunction(tour1, g);
        Double distance2 = CostFunction.calcCostFunction(tour2, g);

        System.out.println(distance1);
        System.out.println(distance2);

        Assert.assertTrue(distance2 <= distance1);
    }
}
