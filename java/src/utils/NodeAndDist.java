package utils;

public class NodeAndDist {
    private int dist;
    private Node node;

    public NodeAndDist(Node node, int dist) {
        this.node = node;
        this.dist = dist;
    }

    public Node getNode() {
        return node;
    }

    public int getDist() {
        return dist;
    }

    @Override
    public String toString() {
        return this.node + ": " + this.dist;
    }
}