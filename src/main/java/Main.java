import structures.Graph;
import utils.GraphCreator;

public class Main {
    public static void main(String[] args) {
        Graph g1 = GraphCreator.randomFullMatrix(5, 10);
        g1.printAdjacencyMatrix();
        System.out.println();
        Graph g2 = GraphCreator.randomSymmetric(5, 10);
        g2.printAdjacencyMatrix();
    }
}
