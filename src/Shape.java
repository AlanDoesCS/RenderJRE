import java.awt.*;

public class Shape {
    public Color colour;
    public Vertex origin;
    public Vertex[] vertices;
    public float scale;

    //reduce repetition
    private void init(int[][] vertex_array, int x, int y, int z, float scale, Color colour) {
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

        //Assign colour
        this.colour = colour;
    }

    public Shape(int[][] vertex_array, int x, int y, int z, float scale, Color colour) { // given colour
        init(vertex_array, x, y, z, scale, colour);
    }

    public Shape(int[][] vertex_array, int x, int y, int z, float scale) { // default colour
        init(vertex_array, x, y, z, scale, Color.BLACK);
    }

    public void setScale(float new_scale) {
        this.scale = new_scale;
    }
}