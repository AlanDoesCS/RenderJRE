public class Vertex {
    public double x, y, z;

    public Vertex[] edges = {};
    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public void addEdges(Vertex[] others) {
        Vertex[] tmp = new Vertex[edges.length + others.length];
    }
}