public class Vector3D {
    public double i, j, k;

    public Vector3D(double i, double j, double k) {
        this.i = i;
        this.j = j;
        this.k = k;
    }

    public Vector3D(double[] array) {
        this.i = array[0];
        this.j = array[1];
        this.k = array[2];
    }

    public Vector3D() {}

    public double length() {
        return Math.sqrt(i*i + j*j + k*k);
    }

    public Vector3D cross(Vector3D v, Vector3D w) {
        Vector3D result = new Vector3D();

        result.i = v.j*w.k - v.k*w.j;
        result.j = v.k*w.i - v.i*w.k;
        result.k = v.i*w.j - v.j*w.i;

        return result;
    }

    public void set_cross(Vector3D v, Vector3D w) { // sets the vector to be equal to the cross product of 2 vectors
        this.i = v.j*w.k - v.k*w.j;
        this.j = v.k*w.i - v.i*w.k;
        this.k = v.i*w.j - v.j*w.i;
    }

    public double dot(Vector3D v, Vector3D w) {
        return v.i*w.i + v.j*w.j + v.k;
    }

    public double dot(Vector3D w) {
        return dot(this, w);
    }

    public void sub(Vector3D v) {
        this.i -= v.i;
        this.j -= v.j;
        this.k -= v.k;
    }

    public void set_sub(Vertex v, Vertex w) { // sets the vector to be equal to one vertex - another vertex
        this.i -= v.x - w.x;
        this.j -= v.y - w.y;
        this.k -= v.z - w.z;
    }

    public double angle(Vector3D v, Vector3D w) { // returns angle between two vectors
        return Math.acos( dot(v, w) / (v.length() * w.length()));
    }
}
