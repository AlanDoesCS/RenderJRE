import java.awt.*;

public class Shape {
    public static Color colour;
    public Vertex origin;
    public Vertex[] vertices;
    public int scale;

    public Shape(int[][] vertex_array, int x, int y, int z, int scale) { // default colour
        this.scale = scale;
        origin = new Vertex(x,y,z);

        // convert 2D int array into 1D array of vertex Objects
        Vertex[] vertices_temp = new Vertex[vertex_array.length];

        int i=0;
        for (int[] vert : vertex_array) {
            vertices_temp[i] = new Vertex(vert[0], vert[1], vert[2]);
        }
        vertices = vertices_temp;

        // Set colour
        this.colour = Color.BLACK;
    }

    public Shape(int[][] vertex_array, int x, int y, int z, int scale, Color colour) { // Specified colour
        this.scale = scale;
        origin = new Vertex(x,y,z);

        // convert 2D int array into 1D array of vertex Objects
        Vertex[] vertices_temp = new Vertex[vertex_array.length];

        int i=0;
        for (int[] vert : vertex_array) {
            vertices_temp[i] = new Vertex(vert[0], vert[1], vert[2]);
        }
        vertices = vertices_temp;

        // Set colour
        this.colour = colour;
    }

    public void setScale(int new_scale) {
        scale = new_scale;
    }
}