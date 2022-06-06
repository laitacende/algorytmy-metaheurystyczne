import algorithms.*;
import consts.PheromoneUpdateType;
import consts.StopCondType;
import structures.tsp.Graph;
import utils.graph.CostFunction;
import utils.graph.GraphCreator;
import consts.GraphType;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void testAnts() {
        Graph g = null;
        int vNo = 10;
        int i = 1;
       /* while (vNo <= 100) {
             i = 1;
            while (i <= 5) {
                System.out.println("i: " + i);
                g = GraphCreator.randomFullMatrix(vNo, 10000);
                g.dumpToFile("./testALL/test" + vNo + "_" + i);
                i++;
            }
            vNo += 10;
        } */
        File file = new File("threads_vol3.txt");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        long avgOnlineDelayed = 0;
        long avgOnlineStepByStep = 0;
        long avgOnlineDelayedP = 0;
        long avgOnlineStepByStepP = 0;
        long avgRanked = 0;
        long avgElitist = 0;
        long avgRankedP = 0;
        long avgElitistP = 0;

        long timeOnlineDelayed = 0;
        long timeOnlineStepByStep = 0;
        long timeOnlineDelayedP = 0;
        long timeOnlineStepByStepP = 0;
        long timeRanked = 0;
        long timeElitist = 0;
        long timeRankedP = 0;
        long timeElitistP = 0;



        vNo = 10;
        int threads = 0;

        while (threads <= 8) {
            System.out.println("threads: " + threads);
            avgOnlineDelayed = 0;
            avgOnlineStepByStep = 0;
            avgOnlineDelayedP = 0;
            avgOnlineStepByStepP = 0;
            avgRanked = 0;
            avgElitist = 0;
            avgRankedP = 0;
            avgElitistP = 0;

            timeOnlineDelayed = 0;
            timeOnlineStepByStep = 0;
            timeOnlineDelayedP = 0;
            timeOnlineStepByStepP = 0;
            timeRanked = 0;
            timeElitist = 0;
            timeRankedP = 0;
            timeElitistP = 0;

            vNo = 10;
            while (vNo <= 50) {
                for (int j = 1; j <= 5; j++) {
                    System.out.println("j: " + j);
                    try {
                        g = GraphCreator.fromFile("./testALL/test" + vNo + "_" + j);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    double cost;
                    if (threads == 0) {
                            List<Integer> c;
                            long startTime;
                        AbstractACO aco = new ACO();
                        System.out.println("Elitist");
                        startTime = System.currentTimeMillis();
                        c = aco.antColonyOptimization(g, 2, 0.5, StopCondType.ITERATIONS_AMOUNT, (int) Math.ceil((g.vNo - 1) * 6), PheromoneUpdateType.ELITIST, 0);
                        timeElitist += System.currentTimeMillis() - startTime;
                        cost = CostFunction.calcCostFunction(c, g);
                        avgElitist += cost;
                    }

                    if (threads != 0) {
                        AbstractACO acoP = new ACOParallel(threads);
                            List<Integer> c;
                             long startTime;
                        System.out.println("Elitist Parallel");
                        startTime = System.currentTimeMillis();
                        c = acoP.antColonyOptimization(g, 2, 0.5, StopCondType.ITERATIONS_AMOUNT, (int) Math.ceil((g.vNo - 1) * 6), PheromoneUpdateType.ELITIST, 0);
                        timeElitistP += System.currentTimeMillis() - startTime;
                        cost = CostFunction.calcCostFunction(c, g);
                        avgElitistP += cost;
                    }

                }
                vNo += 10;
            }
            try {
                assert fileWriter != null;
                fileWriter.write(antsFactor + " " +
                                (avgElitist / 25) + " " + (timeElitist / 25) + " " +
                                (avgElitistP / 25) + " " + (timeElitistP / 25) +
                        "\n");
                fileWriter.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
                threads += 1;
        }
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void testACO() {
        Graph graph = GraphCreator.randomEuclidean(50, 10000);
        ACO aco = new ACO();
        ACOParallel acoPll = new ACOParallel(7);
        List<Integer> solutionACOel = aco.antColonyOptimization(graph, 1, 0.5, StopCondType.ITERATIONS_AMOUNT, 1000, PheromoneUpdateType.BY_RANK, (int) (0.2 * graph.vNo), false, true);
        List<Integer> solutionACOPll = acoPll.antColonyOptimization(graph, 1, 0.5, StopCondType.ITERATIONS_AMOUNT, 1000, PheromoneUpdateType.BY_RANK, (int) (0.2 * graph.vNo),true, true);
        List<Integer> solutionTabu = TabuSearch.tabuSearchENN(graph, StopCondType.ITERATIONS_AMOUNT);
        List<Integer> solutionOPT = TwoOPT.twoOpt(graph, KRandom.generateRandomCycle(graph));
        List<Integer> solutionOPT_EXT = TwoOPT.twoOptExtended(graph);

        System.out.println("elitist basic ACO : " + CostFunction.calcCostFunction(solutionACOel, graph));
        System.out.println("ACO + 2OPT : " + CostFunction.calcCostFunction(solutionACOPll, graph));
        System.out.println("2OPT basic : " + CostFunction.calcCostFunction(solutionOPT, graph));
        System.out.println("Tabu : " + CostFunction.calcCostFunction(solutionTabu, graph));
        System.out.println("2OPT + ENN : " + CostFunction.calcCostFunction(solutionOPT_EXT, graph));
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
        testAnts();
    }

}
