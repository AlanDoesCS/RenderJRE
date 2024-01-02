import java.awt.*;

public class Shape {
    public static Vertex origin;
    public static Vertex[] vertices;
    public static int scale;

    public Shape(int[][] vertex_array, int x, int y, int z, int scale) { // default colour
        this.scale = scale;
        origin = new Vertex(x,y,z);

        // convert 2D int array into 1D array of vertex Objects
        Vertex[] vertices_temp = new Vertex[vertex_array.length];

        int i=0;
        for (int[] vert : vertex_array) {
            vertices_temp[i] = new Vertex(vert[0], vert[1], vert[2]);
            i++;
        }
        vertices = vertices_temp;
    }

    public static void setScale(int new_scale) {
        scale = new_scale;
    }
}