import org.junit.Assert;
import org.junit.Test;
import structures.Graph;
import utils.GraphCreator;

import java.io.IOException;

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

    @Test
    public void fromFile() throws IOException {
//        Graph g = GraphCreator.randomEuclidean(5, 10);
//        g.dumpToFile("from_file.txt");
//
//        Graph g2 = GraphCreator.fromFile("src\\main\\java\\samples\\from_file.txt");
//        assert g2 != null;
//
//        g.printAdjacencyMatrix();
//        System.out.println();
//        System.out.println();
//        g2.printAdjacencyMatrix();
    }
}
