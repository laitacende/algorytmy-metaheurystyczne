import org.junit.Test;
import algorithms.ExtendedNearestNeighbour;
import algorithms.NearestNeighbour;
import org.junit.Assert;
import structures.tsp.Graph;
import utils.graph.CostFunction;
import utils.graph.GraphCreator;
import consts.GraphType;

import java.util.List;

public class NearestNeighbourTest {
    @Test
    public void test() {
        Graph g = GraphCreator.randomFullMatrix(5, 10);
        g.printAdjacencyMatrix();
        List<Integer> tour = ExtendedNearestNeighbour.extendedNearestNeighbour(g);

        tour.forEach(System.out::println);
        System.out.println(CostFunction.calcCostFunction(tour, g));
    }

    @Test
    public void test2() {
        Graph g = new Graph(4, GraphType.FULL_MATRIX);
        g.addEdge(1, 2, 1.0);
        g.addEdge(1, 3, 2.0);
        g.addEdge(1, 4, 3.0);

        g.addEdge(2, 1, 4.0);
        g.addEdge(2, 3, 5.0);
        g.addEdge(2, 4, 6.0);

        g.addEdge(3, 1, 7.0);
        g.addEdge(3, 2, 8.0);
        g.addEdge(3, 4, 9.0);

        g.addEdge(4, 1, 10.0);
        g.addEdge(4, 2, 11.0);
        g.addEdge(4, 3, 12.0);

        g.addDiagonalValues();

        List<Integer> tour = NearestNeighbour.nearestNeighbour(g, 1);
        Double distance = CostFunction.calcCostFunction(tour, g);

        Assert.assertEquals(distance, 25.0, 0.01);
    }
}
