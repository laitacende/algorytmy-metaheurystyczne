package structures;

import utils.GraphType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Graph {
    public Double[][] adjacencyMatrix;
    /**
     * Matrix with inverted distances (used for Ant Colony Optimization)
     */
    public Double[][] inverseAdjacencyMatrix;
    public Integer vNo;
    public GraphType type;
    public Coordinates[] coordinates;

    Lock lock = new ReentrantLock();

    /**
     * Matrix of pheromones on roads from city i to j used in Ant Colony Optimization
     */
    public Double[][] pheromonesMatrix;

    /**
     * Parameters to calculate probability
     * TODO find the best values
     */
    public double alfa;
    public double beta;



    public Graph(Integer vNo, GraphType type) {
        // vNo + 1 because nodes are enumerated from 1 to vNo
        this.vNo = vNo + 1;
        adjacencyMatrix = new Double[this.vNo][this.vNo];
        pheromonesMatrix = new Double[this.vNo][this.vNo];
        inverseAdjacencyMatrix = new Double[this.vNo][this.vNo];
        this.type = type;
        if (type == GraphType.EUC_2D) {
            coordinates = new Coordinates[this.vNo];
        }
    }

    public void addEdge(Integer source, Integer destination, Double distance) {
        adjacencyMatrix[source][destination] = distance;
        inverseAdjacencyMatrix[source][destination] = 1 / Math.pow(distance, beta);
    }

    public Double getEdge(Integer source, Integer destination) {
        return adjacencyMatrix[source][destination];
    }

    public Double getEdgeInverted(Integer source, Integer destination) {
        return inverseAdjacencyMatrix[source][destination];
    }

    public void addDiagonalValues() {
        for (int i = 1; i < adjacencyMatrix.length; i++) {
            for (int j = 1; j < adjacencyMatrix.length; j++) {
                if (i == j) {
                    this.addEdge(i, j, -1.0);
                }
            }
        }
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

    public void dumpToFile(String fileName) {
       // File file = new File("src\\main\\java\\samples\\" + fileName);
        File file = new File(fileName);
        FileWriter fileWriter =  null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write("DIMENSION : " + (vNo - 1) + "\n");
            fileWriter.write("EDGE_WEIGHT_TYPE : " + type.toString() + "\n");
            if (type == GraphType.EUC_2D) {
                fileWriter.write("NODE_COORD_SECTION\n");
                for (int i = 1; i < vNo; i++) {
                    // write node_number x y
                    fileWriter.write(i + " " + coordinates[i].x + " " + coordinates[i].y + "\n");
                }
            } else {
                fileWriter.write("EDGE_WEIGHT_SECTION\n");
                if (type == GraphType.FULL_MATRIX) {
                    for (int i = 1; i < vNo; i++) {
                        for (int j = 1; j < vNo; j++) {
                            if (j != vNo - 1) { // last in line
                                fileWriter.write(getEdge(i, j).toString() + " ");
                            } else {
                                fileWriter.write(getEdge(i, j).toString() + "\n");
                            }
                        }
                    }
                } else { // symmetric
                    for (int i = 1; i < vNo; i++) {
                        for (int j = i + 1; j < vNo; j++) {
                            if (j == vNo - 1) {
                                fileWriter.write(getEdge(i, j).toString() + "\n");
                            } else {
                                fileWriter.write(getEdge(i, j).toString() + " ");
                            }
                        }
                    }
                }
            }
            fileWriter.write("EOF");
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void printCoordinates() {
        for (int i = 1; i < vNo; i++) {
            System.out.println(i + " x: " + coordinates[i].x + " y: " + coordinates[i].y);
        }
    }

    public double findAverageDistance() {
        double avg = 0.0;
        for (int i = 1; i < vNo; i++) {
           for (int j = 1; j < vNo; j++) {
               if ( i != j) {
                   avg += adjacencyMatrix[i][j];
               }
           }
        }
        return avg / (vNo * vNo);
    }

    public void setPheromonesToEdge(double amount, int source, int destination) {
        try {
            lock.lock();
            pheromonesMatrix[source][destination] = amount;
        } finally {
            lock.unlock();
        }
    }

    public double getEdgePheromones(int source, int destination) {
        try {
            lock.lock();
            return pheromonesMatrix[source][destination];
        } finally {
            lock.unlock();
        }
    }

    public void increasePheromonesOnEdge(double amount, int source, int destination) {
        try {
            lock.lock();
            pheromonesMatrix[source][destination] += amount;
        } finally {
            lock.unlock();
        }
    }

    public void evaporatePheromonesOnEdge(double amount, int source, int destination) {
        pheromonesMatrix[source][destination] *= (1 - amount);
    }
}
