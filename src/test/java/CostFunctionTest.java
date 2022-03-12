import org.junit.Test;
import static org.junit.Assert.*;
import structures.Graph;
import utils.CostFunction;
import utils.GraphType;

import java.util.ArrayList;

public class CostFunctionTest {
    @Test
    public void test1() {
        Graph g = new Graph(4, GraphType.FULL_MATRIX);
        g.addEdge(1, 2, 1.0);
        g.addEdge(1, 3, 2.0);
        g.addEdge(1, 4, 4.0);

        g.addEdge(2, 1, 4.0);
        g.addEdge(2, 3, 3.0);
        g.addEdge(2, 4, 2.0);

        g.addEdge(3, 1, 1.0);
        g.addEdge(3, 2, 2.0);
        g.addEdge(3, 4, 3.0);

        g.addEdge(4, 1, 1.0);
        g.addEdge(4, 2, 2.0);
        g.addEdge(4, 3, 3.0);

        ArrayList<Integer> cycle = new ArrayList<>();
        // 1 -> 4 -> 2 -> 3
        cycle.add(1);
        cycle.add(4);
        cycle.add(2);
        cycle.add(3);

        Double result = CostFunction.calcCostFunction(cycle, g);
        System.out.println(result);
        assertEquals(result, 10.0, 0.0);
    }
}
