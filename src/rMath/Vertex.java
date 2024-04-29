package rMath;

public class Vertex {
    public float x, y, z;

    public Vertex[] edges = {};
    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex(Vector3D coordinate) {
        set(coordinate.i, coordinate.j, coordinate.k);
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void scaleAdd(float scale, Vector3D vector, Vertex start_vertex) {
        this.x = start_vertex.x + scale * vector.i;
        this.y = start_vertex.y + scale * vector.j;
        this.z = start_vertex.z + scale * vector.k;
    }

    public void scaleAdd(float scale, Vector3D vector) {
        scaleAdd(scale, vector, this);
    }

    public void addEdges(Vertex[] others) {
        Vertex[] tmp = new Vertex[edges.length + others.length];
    }
}