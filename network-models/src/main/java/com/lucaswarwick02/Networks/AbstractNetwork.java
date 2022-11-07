package com.lucaswarwick02.Networks;

import com.lucaswarwick02.Components.Edge;
import com.lucaswarwick02.Components.Node;
import java.util.HashSet;

public abstract class AbstractNetwork {
    HashSet<Node> nodes;
    HashSet<Edge> edges;

    public AbstractNetwork() {
        this.nodes = new HashSet<>();
        this.edges = new HashSet<>();
    }

    public abstract void generateNetwork (int numberOfNodes);

    public double getAverageDegree () {
        return this.getNodes().stream().mapToDouble(Node::getDegree).average().orElse(Double.NaN);
    }

    public HashSet<Node> getNodes () {
        return this.nodes;
    }

    public HashSet<Edge> getEdges () {
        return this.edges;
    }
}
