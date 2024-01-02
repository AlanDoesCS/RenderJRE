import java.awt.*;

public class Shape {
    final double degToRad = Math.PI / 180; // ratio of degrees to radians
    Color colour;
    Vertex origin;
    Vertex[] vertices;
    double scale;
    int[] rotation = {0, 0, 0};

    //reduce repetition
    private void init(double[][] vertex_array, double x, double y, double z, double scale, Color colour) {
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

        //Assign colour
        this.colour = colour;
    }

    public Shape(double[][] vertex_array, double x, double y, double z, double scale, Color colour) { // given colour
        init(vertex_array, x, y, z, scale, colour);
    }

    public Shape(double[][] vertex_array, int x, int y, int z, double scale) { // default colour
        init(vertex_array, x, y, z, scale, Color.BLACK);
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
        /*
        if (theta_x != 0) {
            rotateX(theta_x*intToRad);
        }
        if (theta_y != 0) {
            rotateY(theta_y*intToRad);
        }
        if (theta_z != 0) {
            rotateZ(theta_z*intToRad);
        }
        */
        rotateX(theta_x* degToRad);
        rotateY(theta_y* degToRad);
        rotateZ(theta_z* degToRad);
    }
}