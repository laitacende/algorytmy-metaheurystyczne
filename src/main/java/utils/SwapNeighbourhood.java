package utils;

import structures.Graph;

import java.util.ArrayList;
import java.util.List;

public class SwapNeighbourhood implements INeighbourhood {
    @Override
    public List<Integer> getBestNeighbour(List<Integer> permutation, Graph g, Integer[] indices) {
        List<Integer> newPermutation;
        List<Integer> currentPermutation = new ArrayList<>(permutation);
        Double currentDistance = CostFunction.calcCostFunction(currentPermutation, g);
        Double newDistance;
        int size = currentPermutation.size();

        // invert
        for (int i = 0; i < g.vNo - 2; i++) { // don't consider the last node - there is nothing to invert
            for (int j = i + 1; j < g.vNo - 1; j++) { // check from position 'onwards'
                newPermutation = new ArrayList<>(currentPermutation);
                newDistance = currentDistance;
                //System.out.println(currentDistance);
                // swap i and j
                newPermutation.set(j, currentPermutation.get(i));
                newDistance -= g.getEdge(currentPermutation.get(Math.floorMod(i - 1, size)), currentPermutation.get(i));
               // System.out.println(newDistance);
                newDistance -= g.getEdge(currentPermutation.get(i), currentPermutation.get(Math.floorMod(i + 1, size)));
                //System.out.println(newDistance);
                newPermutation.set(i, currentPermutation.get(j));
                if (j != Math.floorMod(i + 1, size)) {
                    newDistance -= g.getEdge(currentPermutation.get(Math.floorMod(j - 1, size)), currentPermutation.get(j));
                }
               // System.out.println(newDistance);
                if (i != Math.floorMod(j + 1, size)) {
                    newDistance -= g.getEdge(currentPermutation.get(j), currentPermutation.get(Math.floorMod(j + 1, size)));
                }
              //  System.out.println(newDistance);

                newDistance += g.getEdge(newPermutation.get(Math.floorMod(i - 1, size)), newPermutation.get(i));
               // System.out.println(newDistance);
                newDistance += g.getEdge(newPermutation.get(i), newPermutation.get(Math.floorMod(i + 1, size)));
               // System.out.println(newDistance);
                if (j != Math.floorMod(i + 1, size)) {
                    newDistance += g.getEdge(newPermutation.get(Math.floorMod(j - 1, size)), newPermutation.get(j));
                }
               // System.out.println(newDistance);
                if (i != Math.floorMod(j + 1, size)) {
                    newDistance += g.getEdge(newPermutation.get(j), newPermutation.get(Math.floorMod(j + 1, size)));
                }
                //System.out.println(newDistance);

                System.out.println("i " + i + " j " + j);
                System.out.println("New: " + newDistance);
                // calculate distance
                System.out.println("Real " + CostFunction.calcCostFunction(newPermutation, g));
//                System.out.println(currentPermutation);
//                System.out.println(newPermutation);
                if (newDistance < currentDistance) {
                    currentPermutation = new ArrayList<>(newPermutation);
                    currentDistance = newDistance;
                    indices[0] = i;
                    indices[1] = j;
                }
            }
        }
        return currentPermutation;
    }
}
