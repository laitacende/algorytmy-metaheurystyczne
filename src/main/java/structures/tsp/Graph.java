package structures.tsp;

import consts.GraphType;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Graph {
    public Double[][] adjacencyMatrix;
    public Integer vNo;
    public GraphType type;
    public Coordinates[] coordinates;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ANT-COLONY VARIABLES
    Lock lock = new ReentrantLock();
    public Double[][] inverseAdjacencyMatrix;
    public Double[][] pheromonesMatrix;
    public double alfa = 1.0;
    public double beta = 4.0;


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
        double powDist = Math.pow(distance, beta);
        if (powDist > 0) {
            inverseAdjacencyMatrix[source][destination] = 1.0 / powDist;
        } else { inverseAdjacencyMatrix[source][destination] = 1.0; }
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

    public void printInverseAdjacencyMatrix() {
        for (int i = 1; i < adjacencyMatrix.length; i++) {
            for (int j = 1; j < adjacencyMatrix.length; j++) {
                System.out.printf("%6s", String.format("%.2f", inverseAdjacencyMatrix[i][j]));
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
        FileWriter fileWriter;
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

    public double getEdgePheromones(int source, int destination) {
        try {
            lock.lock();
            return pheromonesMatrix[source][destination];
        } finally { lock.unlock(); }
    }

    public void setPheromonesToEdge(double amount, int source, int destination) {
        try {
            lock.lock();
            pheromonesMatrix[source][destination] = amount;
        } finally { lock.unlock(); }
    }
    public void setPheromonesToEdge(double amount, int source, int destination, double min, double max) {
        try {
            lock.lock();
            pheromonesMatrix[source][destination] = amount;
            if (pheromonesMatrix[source][destination] > max)
                pheromonesMatrix[source][destination] = max;
            if (pheromonesMatrix[source][destination] < min)
                pheromonesMatrix[source][destination] = min;
        } finally { lock.unlock(); }
    }

    public void increasePheromonesOnEdge(double amount, int source, int destination) {
        try {
            lock.lock();
            pheromonesMatrix[source][destination] += amount;
        } finally { lock.unlock(); }
    }
    public void increasePheromonesOnEdge(double amount, int source, int destination, double max) {
        try {
            lock.lock();
            pheromonesMatrix[source][destination] += amount;
            if (pheromonesMatrix[source][destination] > max)
                pheromonesMatrix[source][destination] = max;
        } finally { lock.unlock(); }
    }

    public void evaporatePheromonesOnEdge(double rho, int source, int destination) {
        pheromonesMatrix[source][destination] *= (1 - rho);
    }
    public void evaporatePheromonesOnEdge(double rho, int source, int destination, double min) {
        pheromonesMatrix[source][destination] *= (1 - rho);
        if (pheromonesMatrix[source][destination] < min)
            pheromonesMatrix[source][destination] = min;
    }
}