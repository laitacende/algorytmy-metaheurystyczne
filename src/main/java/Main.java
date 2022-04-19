import algorithms.ExtendedNearestNeighbour;
import algorithms.KRandom;
import algorithms.NearestNeighbour;
import algorithms.TwoOPT;
import structures.Graph;
import utils.CostFunction;
import utils.GraphCreator;
import utils.GraphType;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
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
                assert fileWriter != null;
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

    public static void testConstK() {
        Graph g = null;
        int vNo = 10;
        while (vNo <= 500) {
            int i = 1;
            while (i <= 10) {
                System.out.println("i: " + i);
                g = GraphCreator.randomFullMatrix(vNo, 100);
                g.dumpToFile("./kconst/testkconst_" + vNo + "_" + i);
                i++;
            }
            vNo += 10;
        }
        File file = new File("results_kconst.txt");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        long avg = 0;
        long timeAvg = 0;
        vNo = 10;
        while (vNo <= 300) {
            System.out.println("vNo: " + vNo);
            avg = 0;
            timeAvg = 0;
            for (int j = 1; j <= 10; j++) {
                try {
                    g = GraphCreator.fromFile("./kconst/testkconst_" + vNo + "_" + j);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                long startTime = System.currentTimeMillis();
                List<Integer> c = KRandom.kRandom(350, g);
                timeAvg += System.currentTimeMillis() - startTime;
                double cost = CostFunction.calcCostFunction(c, g);
                avg += cost;
            }
            try {
                assert fileWriter != null;
                fileWriter.write(vNo + " " + (avg / 10) + " " + (timeAvg  / 10) + "\n");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            vNo += 10;
        }
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void testFromFile(String fileIn) {
        // tsp and atsp
//        String prefix = "../tsp";
//        File dir = new File(prefix);
//        String[] fileList = dir.list();
        String withoutPrefix = fileIn.substring(8, fileIn.length() - 5);
        withoutPrefix = withoutPrefix.substring(8);
        System.out.println("name: " + withoutPrefix);
        File file = new File("results_atsp.txt");
        FileWriter fileWriter =  null;
        HashMap<String, Integer> solutions;
        int K_OPTIMAL_MULTIPLIER = 350;
        try {
            fileWriter = new FileWriter(file, true); // append to file
            solutions = CostFunction.getSolutionsFromFile("../solutions/solutions_atsp.txt");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Graph g = null;
        List<Integer> tour;
      //  if (fileList != null) {
           // for(String fileName : fileList) {
                try {
                   // g = GraphCreator.fromFile(prefix + "/" + fileName);
                    g = GraphCreator.fromFile(fileIn);
                    //System.out.println(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    //continue;
                }
                if (g != null) {
                    // test algorithms
                    double costKRand = 0;
                    double costNeighbour = 0;
                    double costExtended = 0;
                    double costOPT = 0;

                    long timeKRand = 0;
                    long timeNeighbour = 0;
                    long timeExtended = 0;
                    long timeOPT = 0;

                    long startTime = System.currentTimeMillis();
                    System.out.println("KRandom");
                    tour = KRandom.kRandom(g.vNo * K_OPTIMAL_MULTIPLIER, g);
                    costKRand = CostFunction.calcCostFunction(tour, g);
                    timeKRand = System.currentTimeMillis() - startTime;

                    System.out.println("Nearesteighbour");
                    startTime = System.currentTimeMillis();
                    tour = NearestNeighbour.nearestNeighbour(g, 1);
                    costNeighbour = CostFunction.calcCostFunction(tour, g);
                    timeNeighbour = System.currentTimeMillis() - startTime;

                    System.out.println("ExtendedNearesteighbour");
                    startTime = System.currentTimeMillis();
                    tour = ExtendedNearestNeighbour.extendedNearestNeighbour(g);
                    costExtended = CostFunction.calcCostFunction(tour, g);
                    timeExtended = System.currentTimeMillis() - startTime;

                    System.out.println("TwoOPT");
                    startTime = System.currentTimeMillis();
                    tour = TwoOPT.twoOpt(g);
                    costOPT = CostFunction.calcCostFunction(tour, g);
                    timeOPT = System.currentTimeMillis() - startTime;

                    // fileName numberOfNodes costKRandom timeKRandom
                    // costNeighbour timeNeighbour costExtendedNeighbour
                    // timeExtendedNeighbour costOPT timeOPT
                    // averageDistance optimalSolution
                    try {
                        fileWriter.write(withoutPrefix + " " + g.vNo + " " +
                                costKRand + " " + timeKRand + " " +
                                costNeighbour + " " + timeNeighbour + " " +
                                costExtended + " " + timeExtended + " " +
                                costOPT + " " + timeOPT + " " +
                                g.findAverageDistance() + " " + solutions.get(withoutPrefix) + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
           // }
      //  }
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testForRandom(String fileName, GraphType type, int bound, int start, int end, int increment) {
        File file = new File(fileName);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        int K_OPTIMAL_MULTIPLIER = 350;

        Graph graph = null;
        List<Integer> solution;
        double costKRand;
        double costNearestNeighbour;
        double costExtendedNN;
        double costOPT;
        double costCurrentBest = 0.0;
        double avgBestCost = 0.0;
        double avgDistance = 0.0;

        double avgCostKRand = 0.0;
        double avgCostNearestNeighbour = 0.0;
        double avgCostExtendedNN = 0.0;
        double avgCostOPT = 0.0;

        double avgDeviationKRand = 0.0;
        double avgDeviationNearestNeighbour = 0.0;
        double avgDeviationExtendedNN = 0.0;
        double avgDeviationOPT = 0.0;

        long startTime;
        long avgTimeKRand = 0;
        long avgTimeNearestNeighbour = 0;
        long avgTimeExtendedNN = 0;
        long avgTimeOPT = 0;

        int configTestsNo = 5;
        int vNo = start;
        while (vNo < end) {
            for (int i = 0; i < configTestsNo; i++) {
                switch (type) {
                    case EUC_2D -> graph = GraphCreator.randomEuclidean(vNo, bound);
                    case UPPER_ROW -> graph = GraphCreator.randomSymmetric(vNo, bound);
                    case FULL_MATRIX -> graph = GraphCreator.randomFullMatrix(vNo, bound);
                }
                assert graph != null;
                startTime = System.currentTimeMillis();
                solution = KRandom.kRandom((K_OPTIMAL_MULTIPLIER * vNo), graph);
                avgTimeKRand += System.currentTimeMillis() - startTime; // time in milliseconds
                costKRand = CostFunction.calcCostFunction(solution, graph);
                avgCostKRand += costKRand;
                costCurrentBest = costKRand;

                startTime = System.currentTimeMillis();
                solution = NearestNeighbour.nearestNeighbour(graph, 1);
                avgTimeNearestNeighbour += System.currentTimeMillis() - startTime; // time in milliseconds
                costNearestNeighbour = CostFunction.calcCostFunction(solution, graph);
                avgCostNearestNeighbour += costNearestNeighbour;
                if (costNearestNeighbour < costCurrentBest) { costCurrentBest = costNearestNeighbour; }

                startTime = System.currentTimeMillis();
                solution = ExtendedNearestNeighbour.extendedNearestNeighbour(graph);
                avgTimeExtendedNN += System.currentTimeMillis() - startTime; // time in milliseconds
                costExtendedNN = CostFunction.calcCostFunction(solution, graph);
                avgCostExtendedNN += costExtendedNN;
                if (costExtendedNN < costCurrentBest) { costCurrentBest = costExtendedNN; }

                startTime = System.currentTimeMillis();
                solution = TwoOPT.twoOpt(graph);
                avgTimeOPT = System.currentTimeMillis() - startTime; // time in milliseconds
                costOPT = CostFunction.calcCostFunction(solution, graph);
                avgCostOPT += costOPT;
                if (costOPT < costCurrentBest) { costCurrentBest = costOPT; }

                avgDeviationKRand += (costKRand - costCurrentBest) / costCurrentBest;
                avgDeviationNearestNeighbour += (costNearestNeighbour - costCurrentBest) / costCurrentBest;
                avgDeviationExtendedNN += (costExtendedNN - costCurrentBest) / costCurrentBest;
                avgDeviationOPT += (costOPT - costCurrentBest) / costCurrentBest;
                avgBestCost += costCurrentBest;
                avgDistance += graph.findAverageDistance();
            }
            avgCostKRand /= configTestsNo;
            avgCostNearestNeighbour /= configTestsNo;
            avgCostExtendedNN /= configTestsNo;
            avgCostOPT /= configTestsNo;

            avgTimeKRand /= configTestsNo;
            avgTimeNearestNeighbour /= configTestsNo;
            avgTimeExtendedNN /= configTestsNo;
            avgTimeOPT /= configTestsNo;

            avgDeviationKRand /= configTestsNo;
            avgDeviationNearestNeighbour /= configTestsNo;
            avgDeviationExtendedNN /= configTestsNo;
            avgDeviationOPT /= configTestsNo;
            avgBestCost /= configTestsNo;
            avgDistance /= configTestsNo;

            try {
                assert fileWriter != null;
                fileWriter.write(vNo + " " +
                        avgCostKRand + " " + avgTimeKRand + " " + avgDeviationKRand + " " +
                        avgCostNearestNeighbour + " " + avgTimeNearestNeighbour + " " + avgDeviationNearestNeighbour + " " +
                        avgCostExtendedNN + " " + avgTimeExtendedNN + " " + avgDeviationExtendedNN + " " +
                        avgCostOPT + " " + avgTimeOPT + " " + avgDeviationOPT + " " +
                        avgBestCost + " " + avgDistance + "\n");
            }
            catch (IOException e) {
                e.printStackTrace();
                System.out.println("file writer in in tests for random error");
            }
            avgCostKRand = 0.0;
            avgCostNearestNeighbour = 0.0;
            avgCostExtendedNN = 0.0;
            avgCostOPT = 0.0;

            avgTimeKRand = 0;
            avgTimeNearestNeighbour = 0;
            avgTimeExtendedNN = 0;
            avgTimeOPT = 0;

            avgDeviationKRand = 0.0;
            avgDeviationNearestNeighbour = 0.0;
            avgDeviationExtendedNN = 0.0;
            avgDeviationOPT = 0.0;
            avgBestCost = 0.0;
            avgDistance = 0.0;

            vNo += increment;
        }

        try {
            assert fileWriter != null;
            fileWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("closing file writer error");
        }
    }

    public static void main(String[] args) {
//        if (args.length < 1) {
//            System.err.println("Invalid arguments. Usage [randFull | randUpper | randEuclid] <number of nodes> <upper bound of weights> or <fileName>");
//        } else {
//            Graph g = null;
//            int vNo = 0;
//            int bound = 0;
//            if ("randFull".equals(args[0]) || "randUpper".equals(args[0]) || "randEuclid".equals(args[0])) {
//                try {
//                    vNo = Integer.parseInt(args[1]);
//                    bound = Integer.parseInt(args[2]);
//                } catch (NumberFormatException e) {
//                    System.err.println("Invalid number of nodes.");
//                }
//                if ("randFull".equals(args[0])) {
//                    // generate random graph with full matrix
//                    g = GraphCreator.randomFullMatrix(vNo, bound);
//                } else if ("randUpper".equals(args[0])) {
//                    g = GraphCreator.randomSymmetric(vNo, bound);
//                } else if ("randEuclid".equals(args[0])) {
//                    g = GraphCreator.randomEuclidean(vNo, bound);
//                }
//            } else { // get from file
//                try {
//                    g = GraphCreator.fromFile(args[0]);
//                } catch (IOException e) {
//                    System.err.println("Cannot open file.");
//                }
//            }
//            if (g != null) {
//                // run algorithms on this graph and print the results
//                List<Integer> tour;
//                tour = KRandom.kRandom(10000, g);
//                System.out.println("Solution found by k-random algorithm: " + tour + "\ncost: " + CostFunction.calcCostFunction(tour, g));
//
//                tour = NearestNeighbour.nearestNeighbour(g, 1);
//                System.out.println("Solution found by the nearest neighbour algorithm: " + tour + "\ncost: " + CostFunction.calcCostFunction(tour, g));
//
//                tour = ExtendedNearestNeighbour.extendedNearestNeighbour(g);
//                System.out.println("Solution found by the extended nearest neighbour algorithm: " + tour + "\ncost: " + CostFunction.calcCostFunction(tour, g));
//
//                tour = TwoOPT.twoOpt(g);
//                System.out.println("Solution found by the 2-OPT algorithm: " + tour + "\ncost: " + CostFunction.calcCostFunction(tour, g));
//
//            } else {
//                System.err.println("Cannot create a graph.");
//            }
//        }
    }

}
