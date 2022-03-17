import algorithms.NearestNeighbour;
import org.junit.Test;
import structures.Graph;
import utils.GraphCreator;

import java.util.List;

public class NearestNeighbourTest {
    @Test
    public void test1() {
        Graph g = GraphCreator.randomFullMatrix(5, 10);
        g.printAdjacencyMatrix();
        List<Integer> tour = NearestNeighbour.nearestNeighbour(g, 1);

        tour.forEach(System.out::println);
    }
}
