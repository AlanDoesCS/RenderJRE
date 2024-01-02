public class Vertex {
    public int x, y, z;

    public Vertex[] edges;
    public Vertex(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public void addEdges(Vertex[] others) {
        Vertex[] tmp = new Vertex[edges.length + others.length];
    }
}