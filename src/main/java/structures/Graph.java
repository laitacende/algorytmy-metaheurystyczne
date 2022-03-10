package structures;

import java.util.Arrays;

public class Graph {
    public Double[][] adjacencyMatrix;

    public Graph(Integer vNo) {
        // vNo + 1 because nodes are enumerated from 1 to vNo
        adjacencyMatrix = new Double[vNo + 1][vNo + 1];
    }

    public void addEdge(Integer source, Integer destination, Double distance) {
        adjacencyMatrix[source][destination] = distance;
    }

    public Double getEdge(Integer source, Integer destination) {
        return adjacencyMatrix[source][destination];
    }

    public void printAdjacencyMatrix() {
      //  System.out.print(Arrays.deepToString(adjacencyMatrix));
        for (int i = 1; i < adjacencyMatrix.length; i++) {
            for (int j = 1; j < adjacencyMatrix.length; j++) {
                System.out.print(String.format("%6s", adjacencyMatrix[i][j].toString()));
            }
            System.out.println();
        }
    }
}
