package Scene.objects;
import Scene.objects.dependencies.*;

import java.awt.Color;
import java.util.ArrayList;

import rMath.Vertex;

public class Shape {
    final double degToRad = Math.PI / 180; // ratio of degrees to radians
    Color colour;
    Vertex origin;
    Vertex[] vertices;
    ArrayList<Triangle> triangles = new ArrayList<>();
    double scale;

    //reduce repetition
    void init(double[][] vertex_array, double x, double y, double z, double scale, Color colour) {

        this.scale = scale;
        origin = new Vertex(x,y,z);

        // convert 2D int array into 1D array of vertex Objects
        Vertex[] vertices_temp = new Vertex[vertex_array.length];

        int i=0;
        for (double[] vert : vertex_array) {
            vertices_temp[i] = new Vertex(vert[0], vert[1], vert[2]);
            i++;
        }
        vertices = vertices_temp;

        int vert_len = vertices.length;
        double[][][] unique_tris = new double[vert_len][vert_len][vert_len];

        if (vertices.length > 2) {
            for (int v1=0; v1<vertices.length; v1++) {
                for (int v2=0; v2<vertices.length; v2++) {
                    for (int v3=0; v3<vertices.length; v3++) {
                        if (v1!=v2 && v1!=v3 && v2!=v3 && unique_tris[v1][v2][v3] == 0.0) {
                            triangles.add(new Triangle(new Vertex[]{vertices[v1], vertices[v2], vertices[v3]}));

                            unique_tris[v1][v2][v3] = 1.0;
                            unique_tris[v1][v3][v2] = 1.0;
                            unique_tris[v2][v1][v3] = 1.0;
                            unique_tris[v2][v3][v1] = 1.0;
                            unique_tris[v3][v1][v2] = 1.0;
                            unique_tris[v3][v2][v1] = 1.0;
                        }
                    }
                }
            }
        }

        //Assign colour
        this.colour = colour;
    }

    public Shape(double[][] vertex_array, double x, double y, double z, double scale, Color colour) { // given colour
        init(vertex_array, x, y, z, scale, colour);
    }

    public Shape(double[][] vertex_array, double x, double y, double z, double scale) { // default colour
        init(vertex_array, x, y, z, scale, Color.BLACK);
    }
    public Shape(double x, double y, double z, double scale, Color colour) { // Empty vertices - Intended only for temporary purposes
        init(new double[][]{}, x, y, z, scale, colour);
    }
    public Shape(double x, double y, double z, double scale) { // Empty vertices - Intended only for temporary purposes
        init(new double[][]{}, x, y, z, scale, Color.BLACK);
    }

    public void setScale(double new_scale) {
        this.scale = new_scale;
    }
    // Shape rotation using rotation matrices
    private void rotateX(double theta_x) {
        for (Vertex vertex : vertices) {
            double newY = (vertex.y * Math.cos(theta_x)) + (vertex.z * -Math.sin(theta_x));
            double newZ = (vertex.y * Math.sin(theta_x)) + (vertex.z * Math.cos(theta_x));

            vertex.y = newY;
            vertex.z = newZ;
        }
    }

    private void rotateY(double theta_y) {
        for (Vertex vertex : vertices) {
            double newX = (vertex.x * Math.cos(theta_y)) + (vertex.z * Math.sin(theta_y));
            double newZ = (vertex.x * -Math.sin(theta_y)) + (vertex.z * Math.cos(theta_y));

            vertex.x = newX;
            vertex.z = newZ;
        }
    }

    private void rotateZ(double theta_z) {
        for (Vertex vertex : vertices) {
            double newX = (vertex.x * Math.cos(theta_z)) + (vertex.y * -Math.sin(theta_z));
            double newY = (vertex.x * Math.sin(theta_z)) + (vertex.y * Math.cos(theta_z));

            vertex.x = newX;
            vertex.y = newY;
        }
    }

    public void setRotation(double theta_x, double theta_y, double theta_z) {
        if (theta_x != 0) {
            rotateX(theta_x*degToRad);
        }
        if (theta_y != 0) {
            rotateY(theta_y*degToRad);
        }
        if (theta_z != 0) {
            rotateZ(theta_z*degToRad);
        }
    }

    // -------------------------------------------------------------------------------------------------------------- //

    // Accessors and Mutators

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public Vertex getOrigin() {
        return origin;
    }

    public void setOrigin(Vertex origin) {
        this.origin = origin;
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public void setVertices(Vertex[] vertices) {
        this.vertices = vertices;
    }

    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }

    public void setTriangles(ArrayList<Triangle> triangles) {
        this.triangles = triangles;
    }

    public double getScale() {
        return scale;
    }
}
