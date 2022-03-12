package structures;

public class Graph {
    public Double[][] adjacencyMatrix;
    public Integer vNo;

    public Graph(Integer vNo) {
        // vNo + 1 because nodes are enumerated from 1 to vNo
        this.vNo = vNo + 1;
        adjacencyMatrix = new Double[this.vNo][this.vNo];
    }

    public void addEdge(Integer source, Integer destination, Double distance) {
        adjacencyMatrix[source][destination] = distance;
    }

    public Double getEdge(Integer source, Integer destination) {
        return adjacencyMatrix[source][destination];
    }

    public void printAdjacencyMatrix() {
        for (int i = 1; i < adjacencyMatrix.length; i++) {
            for (int j = 1; j < adjacencyMatrix.length; j++) {

               System.out.printf("%6s", String.format("%.2f", adjacencyMatrix[i][j]));
            }
            System.out.println();
        }
    }

    // use when iterating over nodes (don't have to add 1)
    public Integer getSize() {
        return vNo;
    }
}
