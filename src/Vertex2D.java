public class Vertex2D {
    public double x, y;
    public double z; // used for ordering vertices

    public Vertex2D[] edges = {};
    public Vertex2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Vertex2D() {} // temporarily unset x an y values

    public void scaleAdd(double scale, Vector3D vector, Vertex2D start_vertex) {
        this.x = start_vertex.x + scale * vector.i;
        this.y = start_vertex.y + scale * vector.j;
    }

    public void scaleAdd(double scale, Vector3D vector) {
        scaleAdd(scale, vector, this);
    }

    public void addEdges(Vertex2D[] others) {
        Vertex2D[] tmp = new Vertex2D[edges.length + others.length];
    }
}