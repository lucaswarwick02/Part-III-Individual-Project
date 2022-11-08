package com.lucaswarwick02.Networks;

import com.lucaswarwick02.Components.Edge;
import com.lucaswarwick02.Components.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractNetwork {
    ArrayList<Node> nodes;
    ArrayList<Edge> edges;

    /**
     * Create an empty underlying network
     */
    public AbstractNetwork() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    /**
     * Generate the Nodes and Edges for the underlying network
     * @param numberOfNodes Number of Nodes in the network
     */
    public abstract void generateNetwork (int numberOfNodes);

    public ArrayList<Node> getNodesFromState (Node.State state) {
        return (ArrayList<Node>) this.getNodes().stream().filter(node -> node.getState() == state).collect(Collectors.toList());
    }

    public List<Node> getNeighbours (Node node) {
        List<Edge> relevantEdges = getEdges().stream().filter(edge -> ((edge.getNode1() == node) || (edge.getNode2() == node))).collect(Collectors.toList());
        List<Node> neighbours = new ArrayList<>();
        for (Edge edge : relevantEdges) {
            if (edge.getNode1() == node) {
                neighbours.add(edge.getNode2());
            }
            else {
                neighbours.add(edge.getNode1());
            }
        }
        return neighbours;
    }

    /**
     * Calculate the average degree of all the Nodes within the underlying network
     * @return Average Degree
     */
    public double getAverageDegree () {
        return this.getNodes().stream().mapToDouble(Node::getDegree).average().orElse(Double.NaN);
    }

    /**
     * Get the Nodes within the underlying network
     * @return
     */
    public ArrayList<Node> getNodes () {
        return this.nodes;
    }

    /**
     * Get the Edges within the underlying network
     * @return
     */
    public ArrayList<Edge> getEdges () {
        return this.edges;
    }
}
