package structures;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;


public class TabuList {
    private final TabuListElement[][] tabuList;
    private final Queue<TabuListElement> tabuQueue;
    private final int tabuSize;

    public TabuList(int listSize, int tabuSize) {
        this.tabuList = new TabuListElement[listSize][listSize];
        this.tabuQueue = new ArrayDeque<>();
        this.tabuSize = tabuSize;

        initializeTabuList();
    }

    // to make copy of list passed as an argument
    public TabuList(TabuList list) {
        this.tabuList = new TabuListElement[list.tabuList.length][list.tabuList.length];
        this.tabuQueue = new ArrayDeque<>();
        this.tabuSize = list.tabuSize;

        initializeTabuList(list);
    }


    public void addToTabuList(int indexA, int indexB) {
        // if already on the list return
        if (tabuList[indexA][indexB].value) return;

        tabuList[indexA][indexB].value = true;
        tabuQueue.add(tabuList[indexA][indexB]);

        // if overflows the size remove oldest element
        if (tabuQueue.size() > tabuSize) {
            Objects.requireNonNull(tabuQueue.poll()).value = false;
        }
    }

    public void removeFromTabuList(int indexA, int indexB) {
        tabuList[indexA][indexB].value = false;
        tabuQueue.remove(tabuList[indexA][indexB]);
    }

    public boolean isOnTabuList(int indexA, int indexB) {
        return tabuList[indexA][indexB].value;
    }

    public int getCurrentTabuListSize() {
        return tabuQueue.size();
    }

    public int getMaxTabuListSize() {
        return tabuSize;
    }

    public void resetTabuList() {
        while (!tabuQueue.isEmpty()) {
            tabuQueue.poll().value = false;
        }
    }

    private void initializeTabuList() {
        for (int i = 0; i < tabuList.length; i++) {
            for (int j = 0; j < tabuList.length; j++) {
                tabuList[i][j] = new TabuListElement(false);
            }
        }
    }

    private void initializeTabuList(TabuList list) {
        for (int i = 0; i < tabuList.length; i++) {
            for (int j = 0; j < tabuList.length; j++) {
                tabuList[i][j] = new TabuListElement(list.isOnTabuList(i, j));
                if (list.isOnTabuList(i, j)) {
                    addToTabuList(i, j);
                }
            }
        }
    }

    public void printTabuList() {
        for (TabuListElement[] tabuListElements : tabuList) {
            for (int j = 0; j < tabuList.length; j++) {
                System.out.print(tabuListElements[j].value + ", ");
            }
            System.out.println();
        }
    }

    /**
     * An element of the tabu list
     */
    private static class TabuListElement {
        Boolean value;

        TabuListElement(Boolean value) {
            this.value = value;
        }
    }
}
