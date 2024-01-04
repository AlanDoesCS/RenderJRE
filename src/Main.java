import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.*;

public class Main {
    public static double clamp (double min, double value, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static int RandomInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    public static float RandomFloat(float min, float max) {
        return (float) (Math.random() * (max - min)) + min;
    }
    public static double RandomDouble(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    public static class Cuboid extends Shape {
        public Cuboid(double x, double y, double z, double width, double length, double height, double scale, Color colour) {
            super(x, y, z, scale, colour);

            Vertex[][][] Vertices = new Vertex[2][2][2]; //split into halves: 8/2 -> 4/2 -> 2/2 -> Specific vertex
            vertices = new Vertex[8];

            int vertex=0;
            int i=0, j, k;
            for (double slice_x = -0.5; slice_x<=0.5; slice_x++) {
                j=0;
                for (double slice_y = -0.5; slice_y<=0.5; slice_y++) {
                    k=0;
                    for (double slice_z = -0.5; slice_z<=0.5; slice_z++) {
                        Vertices[i][j][k] = new Vertex(width*slice_x, height*slice_y, length*slice_z);
                        vertices[vertex]=Vertices[i][j][k];
                        k++;
                        vertex++;
                    }
                    j++;
                }
                i++;
            }

            // 2 left triangles
            triangles.add( new Triangle( Vertices[0][0][0], Vertices[0][0][1], Vertices[0][1][1]) );
            triangles.add( new Triangle( Vertices[0][0][0], Vertices[0][1][0], Vertices[0][1][1]) );
            // 2 right triangles
            triangles.add( new Triangle( Vertices[1][0][0], Vertices[1][0][1], Vertices[1][1][1]) );
            triangles.add( new Triangle( Vertices[1][0][0], Vertices[1][1][0], Vertices[1][1][1]) );
            // 2 bottom triangles
            triangles.add( new Triangle( Vertices[0][0][0], Vertices[0][0][1], Vertices[1][0][0]) );
            triangles.add( new Triangle( Vertices[1][0][1], Vertices[0][0][1], Vertices[1][0][0]) );
            // 2 top triangles
            triangles.add( new Triangle( Vertices[0][1][0], Vertices[0][1][1], Vertices[1][1][0]) );
            triangles.add( new Triangle( Vertices[1][1][1], Vertices[0][1][1], Vertices[1][1][0]) );
            // 2 front triangles
            triangles.add( new Triangle( Vertices[0][0][0], Vertices[0][1][0], Vertices[1][1][0]) );
            triangles.add( new Triangle( Vertices[0][0][0], Vertices[1][0][0], Vertices[1][1][0]) );
            // 2 back triangles
            triangles.add( new Triangle( Vertices[0][0][1], Vertices[0][1][1], Vertices[1][1][1]) );
            triangles.add( new Triangle( Vertices[0][0][1], Vertices[1][0][1], Vertices[1][1][1]) );
        }
    }

    public static class Cube extends Cuboid {
        public Cube(double x, double y, double z, double scale, Color colour) {
            super(x, y, z, 1,1,1, scale, colour);
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
                    double z_cor = Math.sqrt(1+ x_cor*x_cor - y_cor*y_cor);
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
        LightSource light = new LightSource(-2,3,15,10, Color.WHITE);

        //Cube
        Cube cube1 = new Cube(2, -3, 15, 1, Color.RED);
        cube1.setRotation(0, 10, 0);

        //Cuboid
        Cuboid cuboid = new Cuboid(-2, -3, 20, 3.5, 2, 1.1, 1.5, Color.gray);
        cuboid.setRotation(0,0,10);

        // Hexagonal Prism
        Hexagonal_Prism hex_prism = new Hexagonal_Prism(-2, 2.1, 15, 10, 0.5, Color.ORANGE);
        hex_prism.setRotation(90, 0, 0);

        // Camera position
        Shape camera = new Shape(0,0,0,1);

        Shape[] unsortedObjs = {
                cuboid,
                light,
                cube1,
                //hex_prism,
                new Pyramid(2, 0, 15, 1, Color.GREEN),
                new Pyramid(-3, 0, 17.5, 0.6, Color.BLUE),
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

                for (int shape_index = orderedObjs.length-1; shape_index>=0; shape_index--) {
                    Shape shape = orderedObjs[shape_index];

                    // Perform most efficient sorting algorithm based on input size
                    Shape.Triangle[] orderedTriangles = Renderer.order_triangles(shape.triangles, shape, camera);

                    Vertex2D[] vertex_points = RenderJRE.render(shape);
                    Vertex2D[][] Triangle_Points = RenderJRE.render_triangles(orderedTriangles, shape);

                    // Fill shapes
                    for (int tri_index = orderedTriangles.length-1; tri_index>=0; tri_index--) {
                        Color lit = RenderJRE.diffuseBasic(shape.colour, light, orderedTriangles[tri_index], 100); //set distance properly!!!!
                        //g2.setColor(lit);
                        g2.setColor(shape.colour);

                        Vertex2D[] triangle = Triangle_Points[tri_index];
                        int[] xpoints = {(int)triangle[0].x, (int)triangle[1].x, (int)triangle[2].x};
                        int[] ypoints = {(int) (RenderJRE.WindowResY-triangle[0].y), (int) (RenderJRE.WindowResY-triangle[1].y), (int) (RenderJRE.WindowResY-triangle[2].y)};

                        Polygon p = new Polygon(xpoints, ypoints, 3);  // This polygon represents a triangle with the above
                        // fill shape vertices.
                        //g2.fillPolygon(p);

                        // draw wireframe
                        g2.setColor(shape.colour.darker());
                        g2.drawPolygon(p);
                    }
                }
            }
        };

        fr.setBackground(Color.BLACK);
        fr.add(pn);
        fr.setVisible(true);
    }
}
