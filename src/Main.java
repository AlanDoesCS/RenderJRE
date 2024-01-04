import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
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

    public static class Sphere extends Shape {
        public Sphere(double x, double y, double z, double scale, int resolution, Color colour) {
            super(x, y, z, scale); // temporarily empty

            double[][] sphere = new double[resolution * resolution][3];

            int i=0;
            for (double x_cor=-1; x_cor<1.; x_cor+=2./resolution) {
                for (double y_cor=-1; y_cor<1.; y_cor+=2./resolution) {
                    double z_cor = 0;
                    sphere[i] = new double[]{x, y, z};
                    i++;
                }
            }
            init(sphere, x,y,z,scale,colour);
        }
    }

    public static void main(String[] args) {
        //create renderer Object
        Renderer RenderJRE = new Renderer(1000, 2,  1000, 1000);

        //Light above
        LightSource light = new LightSource(-2,3,15,1, Color.WHITE);

        //Cube
        Cube cube1 = new Cube(0, -3, 15, 1, Color.RED);
        cube1.setRotation(0, 10, 0);

        // Hexagonal Prism
        Hexagonal_Prism hex_prism = new Hexagonal_Prism(0, 0, 30, 2, 0.5, Color.ORANGE);
        hex_prism.setRotation(45, 45, 0);

        // Camera position
        Shape camera = new Shape(0,0,0,1);

        Shape[] unsortedObjs = {
                light,
                cube1,
                hex_prism,
                new Pyramid(0, 0, 15, 1, Color.GREEN),
                new Pyramid(0, 0, 17.5, 0.6, Color.BLUE),
                new Cube(0, 0, 20, 0.4, Color.MAGENTA)
        };

        JFrame fr = new JFrame();
        fr.setBounds(10, 10, RenderJRE.WindowResX, RenderJRE.WindowResY);
        fr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel pn = new JPanel() {
            @Override
            public void paint(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Perform most efficient sorting algorithm based on input size
                Shape[] orderedObjs = Renderer.order_shapes(unsortedObjs, camera);
                g2.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                for (int shape_index = orderedObjs.length-1; shape_index>=0; shape_index--) {
                    Shape shape = orderedObjs[shape_index];

                    // Perform most efficient sorting algorithm based on input size
                    Shape.Triangle[] orderedTriangles = Renderer.order_triangles(shape.triangles, camera);

                    double[][] vertex_points = RenderJRE.render(shape);
                    double[][][] Triangle_Points = RenderJRE.render_triangles(shape);

                    g2.setColor(shape.colour.darker());

                    for (double[] point : vertex_points) {
                        for (double[] next_point : vertex_points) {
                            if (next_point != point) {
                                float WindowP1_PosY = (float) (RenderJRE.WindowResY-point[1]);
                                float WindowP2_PosY = (float) (RenderJRE.WindowResY-next_point[1]);

                                Line2D line = new Line2D.Float((float) point[0], WindowP1_PosY, (float) next_point[0], WindowP2_PosY);
                                g2.draw(line);
                            }
                        }
                    }

                    g2.setColor(shape.colour);

                    for (double[][] triangle : Triangle_Points) {
                        int[] xpoints = {(int)triangle[0][0], (int)triangle[1][0], (int)triangle[2][0]};
                        int[] ypoints = {(int) (RenderJRE.WindowResY-triangle[0][1]), (int) (RenderJRE.WindowResY-triangle[1][1]), (int) (RenderJRE.WindowResY-triangle[2][1])};

                        Polygon p = new Polygon(xpoints, ypoints, 3);  // This polygon represents a triangle with the above
                        //   vertices.
                        g2.fillPolygon(p);
                    }
                }
            }
        };

        fr.setBackground(Color.BLACK);
        fr.add(pn);
        fr.setVisible(true);
    }
}
