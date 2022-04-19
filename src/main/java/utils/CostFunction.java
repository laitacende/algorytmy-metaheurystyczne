package utils;
import structures.Graph;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CostFunction {

    // cycle is the tour that the cost is calculated for
    public static Double calcCostFunction(List<Integer> cycle, Graph g) {
        Double sum = 0.0;
        for (int i = 0; i < cycle.size(); i++) {
            if (i + 1 < cycle.size()) {
                sum += g.getEdge(cycle.get(i), cycle.get(i + 1));
            } else { // from the last node to the first one on the cycle
                sum += g.getEdge(cycle.get(i), cycle.get(0));
            }
        }
        return sum;
    }

    public static HashMap<String, Integer> getSolutionsFromFile(String fileName) throws IOException {
        HashMap<String, Integer> solutions = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String name;
        int value;

        String[] data;
        String line;
        while ((line = reader.readLine()) != null){
            data = line.replaceAll("\\s+", "").split(":");
            name = data[0];
            value = Integer.parseInt(data[1]);
            solutions.put(name, value);
        }
        reader.close();
        return solutions;
    }

    // checks if cycle is valid in TSP
    public static boolean isAllowedCycle(List<Integer> cycle, Graph g) {
        boolean isAllowedCycle = cycle.size() == g.getSize() - 1;

        // checking the cycle size

        // checking if there are duplicates
        Set<Integer> set = new HashSet<>(cycle);
        if(set.size() < cycle.size()) {
            isAllowedCycle = false;
        }

        return isAllowedCycle;
    }
}
