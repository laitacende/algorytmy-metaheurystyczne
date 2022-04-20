package structures;

import java.util.*;

public class BacktrackList {
    Deque<List<Integer>> backtrackListPermutation; // to store previous permutations
    Deque<Coordinates> moves; // to store moves performed immediately after permutation on backtrackListPermutation
    int size;

    public void addToBacktrackList(List<Integer> permutation) {
        backtrackListPermutation.add(permutation);

//        if (backtrackListPermutation.size() > size) {
//            backtrackListPermutation.remove(); // remove at head
//            moves.remove();
//        }
    }

    public void addMove(int x, int y) {
        moves.add(new Coordinates(x, y));
    }

    // get the last added element as it is probably better that the first one in the queue
    public List<Integer> getPermutation() {
        if (backtrackListPermutation.size() > 0) {
            return backtrackListPermutation.removeLast(); // remove tail
        } else {
            return null;
        }
    }

    public Coordinates getMove() {
        if (moves.size() > 0) {
            return moves.removeLast();
        } else {
            return null;
        }
    }

    public BacktrackList(int size) {
        this.size = size;
        backtrackListPermutation = new ArrayDeque<>();
        moves = new ArrayDeque<>();
    }
}
