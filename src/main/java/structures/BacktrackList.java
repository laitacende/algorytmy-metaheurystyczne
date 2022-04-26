package structures;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class BacktrackList {
    Deque<List<Integer>> backtrackListPermutation; // to store previous permutations
    Deque<TabuList> tabuLists; // to store tabu lists after some iterations are performed
    Deque<Integer> neighbourhoods; // to store type of neighbourhood for stored permutation
    List<Integer> permutationsBuffer;
    Integer neighbourhoodsBuffer;
    int size;

    public BacktrackList(int size) {
        this.size = size;
        backtrackListPermutation = new ArrayDeque<>();
        tabuLists = new ArrayDeque<>();
        neighbourhoods = new ArrayDeque<>();
    }


    public void addPermutation(List<Integer> permutation, Integer neighbourhoodType) {
        // add to permutationsBuffer
        permutationsBuffer = new ArrayList<>(permutation); // copy
        neighbourhoodsBuffer = neighbourhoodType;
    }

    // called after a few iterations after calling add Permutation
    public void addTabuList(TabuList list) {
        tabuLists.add(new TabuList(list)); // copy tabu list
        backtrackListPermutation.add(permutationsBuffer);
        neighbourhoods.add(neighbourhoodsBuffer);

        if (tabuLists.size() > size) {
            tabuLists.remove(); // remove at head (the oldest)
            backtrackListPermutation.pop();
            neighbourhoods.pop();
        }
    }

    // get the last added element as it is probably better that the first one in the queue
    public List<Integer> getPermutation() {
        if (backtrackListPermutation.size() > 0) {
            return backtrackListPermutation.poll(); // remove tail
        }
        else {
            return null;
        }
    }

    public Integer getNeighbourhood() {
        if (neighbourhoods.size() > 0) {
            return neighbourhoods.poll(); // remove tail
        }
        else {
            return null;
        }
    }

    public TabuList getTabuList() {
        if (tabuLists.size() > 0) {
            return tabuLists.poll();
        }
        else {
            return null;
        }
    }
}
