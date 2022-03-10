import org.junit.Test;
import structures.Graph;
import utils.GraphCreator;

public class GraphCreatorTest {
    @Test
    public void directed() {
        Graph g1 = GraphCreator.randomFullMatrix(5, 10);
        g1.printAdjacencyMatrix();
    }

    @Test
    public void symmetric() {
        Graph g1 = GraphCreator.randomSymmetric(5, 10);
        g1.printAdjacencyMatrix();
    }

    @Test
    public void euclidean() {
        Graph g = GraphCreator.randomEuclidean(3, 10);
        g.printAdjacencyMatrix();
    }
}
