import algorithms.ExtendedNearestNeighbour;
import algorithms.NearestNeighbour;
import algorithms.TwoOPT;
import structures.Graph;
import utils.CostFunction;
import utils.GraphCreator;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Invalid arguments. Usage [randFull | randUpper | randEuclid] <number of nodes> <upper bound of weights> or <fileName>");
        } else {
            Graph g = null;
            int vNo = 0;
            int bound = 0;
            if ("randFull".equals(args[0]) || "randUpper".equals(args[0]) || "randEuclid".equals(args[0])) {
                try {
                    vNo = Integer.parseInt(args[1]);
                    bound = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number of nodes.");
                }
                if ("randFull".equals(args[0])) {
                    // generate random graph with full matrix
                    g = GraphCreator.randomFullMatrix(vNo, bound);
                } else if ("randUpper".equals(args[0])) {
                    g = GraphCreator.randomSymmetric(vNo, bound);
                } else if ("randEuclid".equals(args[0])) {
                    g = GraphCreator.randomEuclidean(vNo, bound);
                }
            } else { // get from file
                try {
                    g = GraphCreator.fromFile(args[0]);
                } catch (IOException e) {
                    System.err.println("Cannot open file.");
                }
            }
            if (g != null) {
                // run algorithms on this graph and print the results
                List<Integer> tour;
                // TODO add k random
                tour = NearestNeighbour.nearestNeighbour(g, 1);
                System.out.println("Solution found by the nearest neighbour algorithm: " + tour + "\ncost: " + CostFunction.calcCostFunction(tour, g));

                tour = ExtendedNearestNeighbour.extendedNearestNeighbour(g);
                System.out.println("Solution found by the extended nearest neighbour algorithm: " + tour + "\ncost: " + CostFunction.calcCostFunction(tour, g));

                tour = TwoOPT.twoOpt(g);
                System.out.println("Solution found by the 2-OPT algorithm: " + tour + "\ncost: " + CostFunction.calcCostFunction(tour, g));

            } else {
                System.err.println("Cannot create a graph.");
            }
        }
//        Graph graph = GraphCreator.fromFile("C:\\Users\\Maurycy\\Documents\\GitHub\\algorytmy-metaheurystyczne\\src\\main\\java\\samples\\t3.txt");
//        graph.printAdjacencyMatrix();
//        graph.printCoordinates();
    }
}
