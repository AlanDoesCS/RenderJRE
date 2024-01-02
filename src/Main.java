import java.awt.*;
import java.awt.geom.Line2D;
import javax.swing.*;

public class Main {
    public static class Cube extends Shape {
        public Cube(int x, int y, int z, int size) {
            super(new int[][]{
                    //Top
                    {1,1,1},{-1,1,1},{1,1,-1},{-1,1,-1},
                    //Bottom
                    {1,-1,1},{-1,-1,1},{1,-1,-1},{-1,-1,-1}
            }, x,y,z,size);
        }
    }
    public static class Pyramid extends Shape {
        public Pyramid(int x, int y, int z, int size) {
            super(new int[][]{
                    //Top
                    {0,1,0},
                    //Bottom
                    {1,-1,1},{-1,-1,1},{1,-1,-1},{-1,-1,-1}
            }, x,y,z,size);
        }
    }

    public static class Renderer {
        public int zoom;
        public int WindowResX;
        public int WindowResY;
        public Renderer(int zoom, int WindowResX, int WindowResY)
        {
            this.zoom = zoom;
            this.WindowResX = WindowResX;
            this.WindowResY = WindowResY;
        }

        public int[] project3Dto2D(Vertex vertex, Vertex origin, int objScale) {
            int relative_x = origin.x + objScale * vertex.x;
            int relative_y = origin.y + objScale * vertex.y;
            int relative_z = origin.z + objScale * vertex.z;

            int new_x = WindowResX/2 + (zoom * relative_x)/relative_z;
            int new_y = WindowResY/2 + (zoom * relative_y)/relative_z;

            return new int[] {new_x, new_y};
        }

        public int[][] render(Shape shape) {
            // Get length of rendered vertex array
            int vertex_array_length = shape.vertices.length;

            int[][] vertex_array = new int[vertex_array_length][2]; // convert x,y,z into x,y

            // Project vertices onto 2D plane
            int i=0;

            for (Vertex vertex : shape.vertices) {
                vertex_array[i] = project3Dto2D(vertex, shape.origin, shape.scale);
                i++;
            }

            return vertex_array;
        }
    }
    public static void main(String[] args) {
        //create renderer Object
        Renderer RenderJRE = new Renderer(1500, 1920, 1080);

        // define shapes
        Cube Object_1 = new Cube(-2, -1, 15, 1);
        Pyramid Object_2 = new Pyramid(2, -1, 15, 1);
        Pyramid Object_3 = new Pyramid(0, 3, 20, 1);

        Shape[] renderedObjs = {Object_1, Object_2, Object_3};

        JFrame fr = new JFrame();
        fr.setBounds(10, 10, RenderJRE.WindowResX, RenderJRE.WindowResY);
        fr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel pn = new JPanel() {
            @Override
            public void paint(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(2));

                for (Shape shape : renderedObjs) {
                    int[][] vertex_points = RenderJRE.render(shape);

                    for (int[] point : vertex_points) {
                        g2.drawOval(point[0], RenderJRE.WindowResY-point[1], 1, 1);
                    }

                    for (int[] point : vertex_points) {
                        for (int[] next_point : vertex_points) {
                            if (next_point != point) {
                                Line2D line = new Line2D.Float(point[0], RenderJRE.WindowResY-point[1], next_point[0], RenderJRE.WindowResY-next_point[1]);
                                g2.draw(line);
                            }
                        }
                    }
                }
            }
        };

        fr.setBackground(Color.WHITE);
        fr.add(pn);
        fr.setVisible(true);
    }
}
