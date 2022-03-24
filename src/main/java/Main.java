import algorithms.ExtendedNearestNeighbour;
import algorithms.KRandom;
import algorithms.NearestNeighbour;
import algorithms.TwoOPT;
import structures.Graph;
import utils.CostFunction;
import utils.GraphCreator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void testK() {
        Graph g = null;
        int vNo = 10;
        while (vNo <= 500) {
            int i = 1;
            while (i <= 10) {
                System.out.println("i: " + i);
                g = GraphCreator.randomFullMatrix(vNo, 100);
                g.dumpToFile("testk_" + vNo + "_" + i);
                i++;
            }

            vNo += 10;
        }
        File file = new File("results_k.txt");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        long avg = 0;
        long timeAvg = 0;
        vNo = 10;
        int i = 1;
        while (i <= 50) {
            System.out.println("i: " + i);
            avg = 0;
            timeAvg = 0;
            vNo = 10;
            while (vNo <= 200) {
                System.out.println("vNo: " + vNo);
                for (int j = 1; j <= 10; j++) {
                    try {
                        g = GraphCreator.fromFile("testk_" + vNo + "_" + j);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    long startTime = System.currentTimeMillis();
                    List<Integer> c = KRandom.kRandom(g.vNo * 10 * i, g);
                    timeAvg += System.currentTimeMillis() - startTime;
                    double cost = CostFunction.calcCostFunction(c, g);
                    avg += cost;
                }
                vNo += 10;
            }
            try {
                fileWriter.write(i + " " + avg + " " + timeAvg + "\n");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void testFromFile() {
        // tsp and atsp
        File dir = new File("../tsp");
        String[] fileList = dir.list();
        File file = new File("results_tsp.txt");
        FileWriter fileWriter =  null;
        
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Graph g;
        List<Integer> tour;
        if (fileList != null) {
           for(String fileName : fileList) {
               try {
                   g = GraphCreator.fromFile("../tsp/" + fileName);
               } catch (IOException e) {
                   e.printStackTrace();
                   continue;
               }
               // test algorithms
               double costKRand = 0;
               double costNeighbour = 0;
               double costExtended = 0;
               double costOPT = 0;

               long timeKRand = 0;
               long timeNeighbour = 0;
               long timeExtended = 0;
               long timeOPT = 0;

               assert g != null;
               long startTime = System.currentTimeMillis();
               tour = KRandom.kRandom(g.vNo * 100, g);
               costKRand = CostFunction.calcCostFunction(tour, g);
               timeKRand = System.currentTimeMillis() - startTime;

               startTime = System.currentTimeMillis();
               tour = NearestNeighbour.nearestNeighbour(g, 1);
               costNeighbour = CostFunction.calcCostFunction(tour, g);
               timeNeighbour = System.currentTimeMillis() - startTime;

               startTime = System.currentTimeMillis();
               tour = ExtendedNearestNeighbour.extendedNearestNeighbour(g);
               costExtended = CostFunction.calcCostFunction(tour, g);
               timeExtended = System.currentTimeMillis() - startTime;

               startTime = System.currentTimeMillis();
               tour = TwoOPT.twoOpt(g);
               costOPT = CostFunction.calcCostFunction(tour, g);
               timeOPT = System.currentTimeMillis() - startTime;

               // find optimum (if there is available) TODO



               try {
                    fileWriter.write(fileName + " " + g.vNo + " " +
                            costKRand + " " + timeKRand + " " +
                            costNeighbour + " " + timeNeighbour + " " +
                            costExtended + " " + timeExtended + " " +
                            costOPT + " " + timeOPT + " " +
                            g.findAverageDistance() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
           }
       }
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
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
                tour = KRandom.kRandom(10000, g);
                System.out.println("Solution found by k-random algorithm: " + tour + "\ncost: " + CostFunction.calcCostFunction(tour, g));

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
    }
}
