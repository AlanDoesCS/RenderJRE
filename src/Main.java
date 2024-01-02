import java.awt.*;
import java.awt.geom.Line2D;
import javax.swing.*;

public class Main {
    public static class Cube extends Shape {
        public Cube(int x, int y, int z, float scale, Color colour) {
            super(new int[][]{
                    //Top
                    {1,1,1},{-1,1,1},{1,1,-1},{-1,1,-1},
                    //Bottom
                    {1,-1,1},{-1,-1,1},{1,-1,-1},{-1,-1,-1}
            }, x,y,z,scale, colour);
        }
    }
    public static class Pyramid extends Shape {
        public Pyramid(int x, int y, int z, float scale, Color colour) {
            super(new int[][]{
                    //Top
                    {0,1,0},
                    //Bottom
                    {1,-1,1},{-1,-1,1},{1,-1,-1},{-1,-1,-1}
            }, x,y,z,scale, colour);
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

        public float[] project3Dto2D(Vertex vertex, Vertex origin, float objScale) {
            float relative_x = origin.x + objScale * vertex.x;
            float relative_y = origin.y + objScale * vertex.y;
            float relative_z = origin.z + objScale * vertex.z;

            float new_x = (float) WindowResX /2 + (zoom * relative_x)/relative_z;
            float new_y = (float) WindowResY /2 + (zoom * relative_y)/relative_z;

            return new float[] {new_x, new_y};
        }

        public float[][] render(Shape shape) {
            // Get length of rendered vertex array
            int vertex_array_length = shape.vertices.length;

            float[][] vertex_array = new float[vertex_array_length][2]; // convert x,y,z into x,y

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
        Shape[] renderedObjs = {
                new Cube(-2, -1, 15, 1, Color.RED),
                new Pyramid(2, -1, 15, 1, Color.GREEN),
                new Pyramid(0, 3, 20, 2.1f, Color.BLUE),
                new Cube(-1, -2, 15, 1.5f, Color.MAGENTA)
        };

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
                    g2.setColor(shape.colour);

                    float[][] vertex_points = RenderJRE.render(shape);

                    for (float[] point : vertex_points) {
                        g2.drawOval((int) point[0], (int) (RenderJRE.WindowResY-point[1]), 1, 1);
                    }

                    for (float[] point : vertex_points) {
                        for (float[] next_point : vertex_points) {
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
