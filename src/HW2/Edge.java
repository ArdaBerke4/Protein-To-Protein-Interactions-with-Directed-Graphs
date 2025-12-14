package HW2;

public class Edge {
    private String source;
    private String target;
    private double score;

//
    public Edge(String source, String target, double score) {
        this.source = source;
        this.target = target;
        this.score = score;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return source + " -> " + target + " [" + score + "]";
    }
}
