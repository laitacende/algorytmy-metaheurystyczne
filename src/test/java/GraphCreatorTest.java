import org.junit.Test;
import structures.Graph;
import utils.GraphCreator;

public class GraphCreatorTest {
    @Test
    public void directed() {
        Graph g1 = GraphCreator.randomFullMatrix(5, 10);
        g1.printAdjacencyMatrix();
        g1.dumpToFile("t1.txt");
    }

    @Test
    public void symmetric() {
        Graph g1 = GraphCreator.randomSymmetric(5, 10);
        g1.printAdjacencyMatrix();
        g1.dumpToFile("t2.txt");
    }

    @Test
    public void euclidean() {
        Graph g = GraphCreator.randomEuclidean(3, 10);
        g.printAdjacencyMatrix();
        System.out.println();
        g.printCoordinates();
        g.dumpToFile("t3.txt");
    }
}
