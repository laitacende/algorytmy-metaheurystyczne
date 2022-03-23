import org.junit.Test;
import algorithms.TwoOPT;
import structures.Graph;
import utils.CostFunction;
import utils.GraphCreator;
import java.util.List;

public class TwoOPTTest {
    @Test
    public void test() {
        Graph g = GraphCreator.randomFullMatrix(5, 10);
        g.printAdjacencyMatrix();

        List<Integer> tour = TwoOPT.twoOpt(g);

        tour.forEach(System.out::println);
        System.out.println(CostFunction.calcCostFunction(tour, g));
    }

    @Test
    public void test2() {
        // TODO can try to assert if works on predefined matrix (dont know example to do that)
    }
}
