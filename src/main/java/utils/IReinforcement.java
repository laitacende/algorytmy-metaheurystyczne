package utils;

import structures.Ant;
import structures.Graph;

public interface IReinforcement {
    void updatePheromones(Ant ant, Graph graph);
}
