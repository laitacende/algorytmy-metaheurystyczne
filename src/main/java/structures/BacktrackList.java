package structures;

import java.util.*;

public class BacktrackList {
    Deque<List<Integer>> backtrackListPermutation; // to store previous permutations
    Deque<TabuList> tabuLists; // to store tabu lists after some iterations are performed
    List<Integer> buffer;
    int size;

    public BacktrackList(int size) {
        this.size = size;
        backtrackListPermutation = new ArrayDeque<>();
        tabuLists = new ArrayDeque<>();
    }


    public void addPermutation(List<Integer> permutation) {
        // add to buffer
        buffer = new ArrayList<>(permutation); // copy
    }

    // called after a few iterations after calling add Permutation
    public void addTabuList(TabuList list) {
        tabuLists.add(new TabuList(list)); // copy tabu list
        backtrackListPermutation.add(buffer);

        if (tabuLists.size() > size) {
            tabuLists.remove(); // remove at head (the oldest)
            backtrackListPermutation.pop();
        }
    }

    // get the last added element as it is probably better that the first one in the queue
    public List<Integer> getPermutation() {
        if (backtrackListPermutation.size() > 0) {
            return backtrackListPermutation.pop(); // remove tail
        }
        else {
            return null;
        }
    }

    public TabuList getTabuList() {
        if (tabuLists.size() > 0) {
            return tabuLists.pop();
        }
        else {
            return null;
        }
    }
}
