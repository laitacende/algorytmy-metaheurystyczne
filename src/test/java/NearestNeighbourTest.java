import algorithms.ExtendedNearestNeighbour;
import algorithms.NearestNeighbour;
import algorithms.TwoOPT;
import org.junit.Test;
import structures.Graph;
import utils.CostFunction;
import utils.GraphCreator;
import utils.GraphType;

import java.util.List;

public class NearestNeighbourTest {
    @Test
    public void test1() {
        Graph g = GraphCreator.randomFullMatrix(5, 10);

        g.printAdjacencyMatrix();
        List<Integer> tour = NearestNeighbour.nearestNeighbour(g, 1);
        List<Integer> tour2 = TwoOPT.twoOpt(g);
        List<Integer> tour3 = ExtendedNearestNeighbour.extendedNearestNeighbour(g);


        tour.forEach(System.out::println);
        System.out.println(CostFunction.calcCostFunction(tour, g));
        System.out.println();
        tour2.forEach(System.out::println);
        System.out.println(CostFunction.calcCostFunction(tour2, g));
        System.out.println();
        tour3.forEach(System.out::println);
        System.out.println(CostFunction.calcCostFunction(tour3, g));
    }

    @Test
    public void test2() {
        Graph g = GraphCreator.randomFullMatrix(5, 10);
        g.printAdjacencyMatrix();
        List<Integer> tour = ExtendedNearestNeighbour.extendedNearestNeighbour(g);

        tour.forEach(System.out::println);
    }
}
