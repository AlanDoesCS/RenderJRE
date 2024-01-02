import java.awt.*;
import java.awt.geom.Line2D;
import javax.swing.*;

public class Main {
    public static int RandomInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    public static float RandomFloat(float min, float max) {
        return (float) (Math.random() * (max - min)) + min;
    }
    public static double RandomDouble(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    public static class Cube extends Shape {
        public Cube(double x, double y, double z, double scale, Color colour) {
            super(new double[][]{
                    //Top
                    {1,1,1},{-1,1,1},{1,1,-1},{-1,1,-1},
                    //Bottom
                    {1,-1,1},{-1,-1,1},{1,-1,-1},{-1,-1,-1}
            }, x,y,z,scale, colour);
        }
    }
    public static class Pyramid extends Shape {
        public Pyramid(double x, double y, double z, double scale, Color colour) {
            super(new double[][]{
                    //Top
                    {0,1,0},
                    //Bottom
                    {1,-1,1},{-1,-1,1},{1,-1,-1},{-1,-1,-1}
            }, x,y,z,scale, colour);
        }
    }
    public static class Hexagonal_Prism extends Shape {
        public Hexagonal_Prism(double x, double y, double z, double l, double scale, Color colour) {
            super(new double[][]{
                    //Top
                    {Math.sqrt(3), l/2, 1},
                    {0,l/2,2},
                    {-Math.sqrt(3), l/2, 1},
                    {-Math.sqrt(3), l/2, -1},
                    {0,l/2,-2},
                    {Math.sqrt(3), l/2, -1},

                    //Bottom
                    {Math.sqrt(3), -l/2, 1},
                    {0,-l/2,2},
                    {-Math.sqrt(3), -l/2, 1},
                    {-Math.sqrt(3), -l/2, -1},
                    {0,-l/2,-2},
                    {Math.sqrt(3), -l/2, -1}
            }, x,y,z,scale, colour);
        }
    }

    public static class Renderer {
        public int zoom;
        public double FocalLength;
        public int WindowResX, WindowResY;
        public Renderer(int zoom, double FocalLength, int WindowResX, int WindowResY)
        {
            this.zoom = zoom;
            this.FocalLength = FocalLength;
            this.WindowResX = WindowResX;
            this.WindowResY = WindowResY;
        }

        public double[] project3Dto2D(Vertex vertex, Vertex origin, double objScale) {
            double relative_x = origin.x + objScale * vertex.x;
            double relative_y = origin.y + objScale * vertex.y;
            double relative_z = origin.z + objScale * vertex.z;

            double projected_x = (double) WindowResX /2 + (zoom * relative_x * FocalLength)/(relative_z+FocalLength);
            double projected_y = (double) WindowResY /2 + (zoom * relative_y * FocalLength)/(relative_z+FocalLength);

            return new double[] {projected_x, projected_y};
        }

        public double[][] render(Shape shape) {
            // Get length of rendered vertex array
            int vertex_array_length = shape.vertices.length;

            double[][] vertex_array = new double[vertex_array_length][2]; // convert x,y,z into x,y

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
        Renderer RenderJRE = new Renderer(1000, 2,  1000, 1000);

        // define shape array
        Cube cube1 = new Cube(0, -3, 15, 1, Color.RED);
        cube1.setRotation(0, 10, 0);

        Hexagonal_Prism hex_prism = new Hexagonal_Prism(0, 2, 15, 2, 0.5, Color.ORANGE);
        hex_prism.setRotation(45, 45, 0);

        Shape[] renderedObjs = {
                cube1,
                hex_prism,
                new Pyramid(2, -1, 15, 1, Color.GREEN),
                new Pyramid(0, 4, 20, 0.6, Color.BLUE),
                new Cube(-1, -2, 15, 0.4, Color.MAGENTA)
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

                    double[][] vertex_points = RenderJRE.render(shape);

                    for (double[] point : vertex_points) {
                        g2.drawOval((int) point[0], (int) (RenderJRE.WindowResY-point[1]), 1, 1);
                    }

                    for (double[] point : vertex_points) {
                        for (double[] next_point : vertex_points) {
                            if (next_point != point) {
                                Line2D line = new Line2D.Float((float) point[0], (float) (RenderJRE.WindowResY-point[1]), (float) next_point[0], (float) (RenderJRE.WindowResY-next_point[1]));
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
