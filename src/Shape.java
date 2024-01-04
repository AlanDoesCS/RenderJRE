import java.awt.*;
import java.util.ArrayList;

public class Shape {
    public class Triangle {
        public Vertex[] points;
        public Vertex midpoint;

        public Triangle(Vertex[] array) {
            points = array;
            if (array.length > 1) {
                double sum_x = 0, sum_y = 0, sum_z = 0;
                for (int vertex=0; vertex < array.length; vertex++) {
                    sum_x += array[vertex].x;
                    sum_y += array[vertex].y;
                    sum_z += array[vertex].z;
                }

                midpoint = new Vertex(sum_x/array.length, sum_y/array.length, sum_z/ array.length);
            } else if (array.length == 1) {
                midpoint = new Vertex(array[0].x,array[0].y,array[0].z);
            } else {
                midpoint = new Vertex(0,0,0);
            }
        }
    }

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
    public Shape(double x, double y, double z, double scale) { // Empty vertices - Intended only for temporary purposes
        init(new double[][]{}, x, y, z, scale, Color.BLACK);
    }

    public void setScale(double new_scale) {
        this.scale = new_scale;
    }
    // Shape rotation using rotation matrices
    private void rotateX(double theta_x) { // theta is in radians
        for (Vertex vertex : vertices) {
            // vertex.x stays the same
            vertex.y = (vertex.y * Math.cos(theta_x)) + (vertex.z * Math.sin(theta_x));
            vertex.z = (vertex.y * -Math.sin(theta_x)) + (vertex.z * Math.cos(theta_x));
        }
    }

    private void rotateY(double theta_y) { // theta is in radians
        for (Vertex vertex : vertices) {
            vertex.x = (vertex.x * Math.cos(theta_y)) + (vertex.z * -Math.sin(theta_y));
            // vertex.y stays the same
            vertex.z = (vertex.x * Math.sin(theta_y)) + (vertex.z * Math.cos(theta_y));
        }
    }

    private void rotateZ(double theta_z) { // theta is in radians
        for (Vertex vertex : vertices) {
            vertex.x = (vertex.x * Math.cos(theta_z)) + (vertex.y * Math.sin(theta_z));
            vertex.y = (vertex.x * -Math.sin(theta_z))+ (vertex.y * Math.cos(theta_z));
            // vertex.z stays the same
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
}