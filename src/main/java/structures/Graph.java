package structures;

public class Graph {
    public Double[][] adjacencyMatrix;

    public Graph(Integer vNo) {
        adjacencyMatrix = new Double[vNo][vNo];
    }

    public void addEdge(Integer source, Integer destination, Double distance) {
        adjacencyMatrix[source][destination] = distance;
    }

    public Double getEdge(Integer source, Integer destination) {
        return adjacencyMatrix[source][destination];
    }
}
