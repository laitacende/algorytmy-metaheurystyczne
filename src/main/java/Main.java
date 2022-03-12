import structures.Graph;
import utils.GraphCreator;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Graph graph = GraphCreator.fromFile("C:\\Users\\Maurycy\\Documents\\GitHub\\algorytmy-metaheurystyczne\\src\\main\\java\\samples\\t3.txt");
        graph.printAdjacencyMatrix();
        graph.printCoordinates();
    }
}
