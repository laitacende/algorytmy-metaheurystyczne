package structures;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Queue;


public class TabuList {
    private final TabuListElement[][] tabuList;
    private final Queue<TabuListElement> tabuQueue;
    private final ArrayList<Double> costFunValues;
    private final int tabuSize;

    public TabuList(int listSize, int tabuSize) {
        this.tabuList = new TabuListElement[listSize][listSize];
        this.tabuQueue = new ArrayDeque<>();
        this.costFunValues = new ArrayList<>();
        this.tabuSize = tabuSize;

        initializeTabuList();
    }

    // to make copy of list passed as an argument
    public TabuList(TabuList list) {
        this.tabuList = new TabuListElement[list.tabuList.length][list.tabuList.length];
        this.tabuQueue = new ArrayDeque<>(list.tabuQueue);
        this.costFunValues = new ArrayList<>(list.costFunValues);
        this.tabuSize = list.tabuSize;

        initializeTabuList(list);
    }


    public void addToTabuList(int indexA, int indexB, Double distance) {
        // if already on the list return
        if (tabuList[indexA][indexB].value) return;

        tabuList[indexA][indexB].value = true;
        tabuQueue.add(tabuList[indexA][indexB]);
        // if overflows the size remove the oldest element
        if (tabuQueue.size() > tabuSize) {
            Objects.requireNonNull(tabuQueue.poll()).value = false;
        }

        costFunValues.add(distance);
        if(costFunValues.size() > tabuSize) {
            costFunValues.remove(0);
        }

    }

    public void removeFromTabuList(int indexA, int indexB) {
        tabuList[indexA][indexB].value = false;
        tabuQueue.remove(tabuList[indexA][indexB]);
    }

    public boolean isOnTabuList(int indexA, int indexB, double distance) {
//        for (Double value : this.costFunValues) {
//            if (Math.abs(value - distance) < 0.0001) {
//                return true;
//            }
//        }
        return tabuList[indexA][indexB].value;
    }

    public int getCurrentTabuListSize() {
        return tabuQueue.size();
    }

    public int getMaxTabuListSize() {
        return tabuSize;
    }

    public void clearTabuList() {
        for (TabuListElement[] tabuListElements : tabuList) {
            for (int j = 0; j < tabuList.length; j++) {
                tabuListElements[j].value = false;
            }
        }
        tabuQueue.clear();
    }

    private void initializeTabuList() {
        for (int i = 0; i < tabuList.length; i++) {
            for (int j = 0; j < tabuList.length; j++) {
                tabuList[i][j] = new TabuListElement(false, i, j);
            }
        }
    }

    private void initializeTabuList(TabuList list) {
        for (int i = 0; i < this.tabuList.length; i++) {
            for (int j = 0; j < this.tabuList.length; j++) {
                this.tabuList[i][j] = new TabuListElement(list.tabuList[i][j].value, i , j);
            }
        }
        initializeQueue(list);
    }

    private void initializeQueue(TabuList list) {
       // Queue<TabuListElement> tmp = new ArrayDeque<>(list.tabuQueue);
        for (TabuListElement element : list.tabuQueue) {
            this.tabuQueue.add(tabuList[element.x][element.y]);
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

    public void printTabuList2() {
        for (int i = 0; i < tabuList.length; i++) {
            for (int j = 0; j < tabuList.length; j++) {
                if(tabuList[i][j].value) {
                    System.out.print("(" + i + ", " + j + ")  ; ");
                }
            }
        }
        System.out.println();
    }

    /**
     * An element of the tabu list
     */
    private static class TabuListElement {
        Boolean value;
        int x; // coordinates in array
        int y;

        TabuListElement(Boolean value, int x, int y) {
            this.value = value;
            this.x = x;
            this.y = y;
        }
    }
}
