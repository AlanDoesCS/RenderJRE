import java.awt.*;
import javax.swing.*;

public class Main {
    public static double clamp (double min, double value, double max) {
        return Math.max(min, Math.min(max, value));
    }
    public static double round (double value, int decimal_places) {
        double tens = Math.pow(10, decimal_places);
        return Math.round(value * tens) / tens;
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

    public static class Icosahedron extends Shape {
        public Icosahedron(double x, double y, double z, double scale, Color colour) {
            super(x, y, z, scale, colour);

            double phi = (1 + Math.sqrt(5)) / 2; // Golden ratio

            Vertex A = new Vertex(0, 1, phi);
            Vertex B = new Vertex(0, -1, phi);
            Vertex C = new Vertex(0, 1, -phi);
            Vertex D = new Vertex(0, -1, -phi);

            Vertex E = new Vertex(1, phi, 0);
            Vertex F = new Vertex(-1, phi, 0);
            Vertex G = new Vertex(1, -phi, 0);
            Vertex H = new Vertex(-1, -phi, 0);

            Vertex I = new Vertex(phi, 0, 1);
            Vertex J = new Vertex(-phi, 0, 1);
            Vertex K = new Vertex(phi, 0, -1);
            Vertex L = new Vertex(-phi, 0, -1);

            vertices = new Vertex[]{A,B,C,D,E,F,G,H,I,J,K,L};

            for (Vertex v : vertices) { // change to fill 1x1 space
                v.x /= 2;
                v.y /= 2;
                v.z /= 2;
            }

            Vertex[][] faces = { // triangles
                    {C, F, E}, {A, E, F},
                    {A, I, E}, {E, K, C},
                    {C, L, F}, {F, J, A},
                    {A, J, B}, {B, I, A},
                    {E, I, K}, {G, K, I},
                    {C, K, D}, {D, L, C},
                    {F, L, J}, {H, J, L},
                    {J, H, B}, {L, D, H},
                    {K, G, D}, {I, G, B},
                    {D, G, H}, {B, H, G}
            };

            for (Vertex[] face : faces) {
                triangles.add(new Triangle(face[0], face[1], face[2]));
            }
        }
    }

    public static class Cuboid extends Shape {
        public Cuboid(double x, double y, double z, double width, double length, double height, double scale, Color colour) {
            super(x, y, z, scale, colour);

            Vertex A = new Vertex(1, 1, 1);
            Vertex B = new Vertex(1, 1, -1);
            Vertex C = new Vertex(1, -1, 1);
            Vertex D = new Vertex(1, -1, -1);

            Vertex E = new Vertex(-1, 1, 1);
            Vertex F = new Vertex(-1, 1, -1);
            Vertex G = new Vertex(-1, -1, 1);
            Vertex H = new Vertex(-1, -1, -1);

            vertices = new Vertex[]{A,B,C,D,E,F,G,H};

            for (Vertex v : vertices) { // change to fill 1x1 space
                v.x *= width/2;
                v.y *= height/2;
                v.z *= length/2;
            }

            Vertex[][] faces = { // triangles
                    {E, A, B}, {B, F, E},
                    {D, H, F}, {F, B, D},
                    {B, A, C}, {C, D, B},
                    {A, E, G}, {G, C, A},
                    {E, F, H}, {H, G, E},
                    {C, G, H}, {H, D, C}
            };

            for (Vertex[] face : faces) {
                triangles.add(new Triangle(face[0], face[1], face[2]));
            }
        }
    }

    public static class Cube extends Cuboid {
        public Cube(double x, double y, double z, double scale, Color colour) {
            super(x, y, z, 1,1,1, scale, colour);
        }
    }

    public static class Plane extends Shape {
        public Plane(double x, double y, double z, double width, double length, double scale, Color colour) {
            super(x, y, z, scale, colour);

            // z axis plane
            Vertex A = new Vertex(1, 0, 1);
            Vertex B = new Vertex(1, 0, -1);
            Vertex C = new Vertex(-1, 0, 1);
            Vertex D = new Vertex(-1, 0, -1);

            vertices = new Vertex[] {A,B,C,D};

            for (Vertex v : vertices) { // change to fill 1x1 space
                v.x *= width/2;
                v.z *= length/2;
            }

            Vertex[][] faces = { // triangles
                    {C, A, B}, {B, D, C},
            };

            for (Vertex[] face : faces) {
                triangles.add(new Triangle(face[0], face[1], face[2]));
            }
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
                    {Math.sqrt(3), l/2, -1},
                    {-Math.sqrt(3), l/2, 1},
                    {-Math.sqrt(3), l/2, -1},
                    {0,l/2,2},
                    {0,l/2,-2},

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
        public Sphere(double x, double y, double z, double scale, int num_of_points, Color colour) {
            super(x, y, z, scale); // temporarily empty

            double[][] sphere = new double[num_of_points][3];

            for (int i = 0; i < num_of_points; i++) {
                double phi = Math.acos(2 * Math.random() - 1); // Latitude angle
                double theta = 2 * Math.PI * Math.random(); // Longitude angle

                // Convert spherical coordinates to Cartesian coordinates
                double x_cor = Math.sin(phi) * Math.cos(theta);
                double y_cor = Math.sin(phi) * Math.sin(theta);
                double z_cor = Math.cos(phi);

                // Store the Cartesian coordinates in the array
                sphere[i][0] = x_cor;
                sphere[i][1] = y_cor;
                sphere[i][2] = z_cor;
            }

            init(sphere, x,y,z,scale,colour);
        }
    }

    public static void main(String[] args) {
        //create renderer Object
        Renderer RenderJRE = new Renderer(1000, 2,  1000, 1000);

        //Light above
        DirectLight light = new DirectLight(1,-3,-2, Color.WHITE);

        //Cube
        Cube cube1 = new Cube(2, -3, 15, 1, Color.RED);
        cube1.setRotation(0, 10, 0);

        //Cuboid
        Cuboid cuboid = new Cuboid(-2, -3, 20, 3.5, 2, 1.1, 1.5, Color.gray);
        cuboid.setRotation(0,0,0);

        // Hexagonal Prism
        Hexagonal_Prism hex_prism = new Hexagonal_Prism(-2, 2.1, 15, 10, 0.5, Color.ORANGE);
        hex_prism.setRotation(0, 0, 0);

        // Icosahedron
        Icosahedron icosahedron = new Icosahedron(2.1, 1.2, 17, 1, Color.pink);

        // Plane
        Plane plane = new Plane(0, -2.5, 10, 3, 1, 1, Color.YELLOW);

        // Camera position
        Shape camera = new Shape(0,0,0,1);

        Shape[] unsortedObjs = {
                icosahedron,
                cuboid,
                cube1,
                plane,
                //hex_prism,
                new Pyramid(2.5, 0, 15, 1, Color.GREEN),
                //new Pyramid(-3, 0, 17.5, 0.6, Color.BLUE),
                //new Cube(0, 0, 20, 0.4, Color.MAGENTA)
        };

        JFrame fr = new JFrame();
        fr.setBounds(10, 10, RenderJRE.WindowResX, RenderJRE.WindowResY);
        fr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel pn = new JPanel() {
            @Override
            public void paint(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                // Perform most efficient sorting algorithm based on input size
                Shape[] orderedObjs = Renderer.order_shapes(unsortedObjs, camera);

                for (int shape_index = orderedObjs.length-1; shape_index>=0; shape_index--) {
                    Shape shape = orderedObjs[shape_index];

                    // Perform most efficient sorting algorithm based on input size
                    Shape.Triangle[] orderedTriangles = Renderer.order_triangles(shape.triangles, shape, camera);

                    Vertex2D[][] Triangle_Points = RenderJRE.render_triangles(orderedTriangles, shape);

                    // Fill shapes
                    for (int tri_index = orderedTriangles.length-1; tri_index>=0; tri_index--) {
                        Color lit = RenderJRE.diffuseBasic(shape.colour, light, orderedTriangles[tri_index]);
                        g2.setColor(lit);
                        //g2.setColor(shape.colour);

                        Vertex2D[] triangle = Triangle_Points[tri_index];
                        int[] xpoints = {(int)triangle[0].x, (int)triangle[1].x, (int)triangle[2].x};
                        int[] ypoints = {(int) (RenderJRE.WindowResY-triangle[0].y), (int) (RenderJRE.WindowResY-triangle[1].y), (int) (RenderJRE.WindowResY-triangle[2].y)};

                        Polygon p = new Polygon(xpoints, ypoints, 3);  // This polygon represents a triangle with the above
                        // fill shape vertices.
                        g2.fillPolygon(p);

                        // draw wireframe
                        g2.setColor(shape.colour.darker());
                        //g2.drawPolygon(p);
                    }
                }
            }
        };

        fr.setBackground(Color.BLACK);
        fr.add(pn);
        fr.setVisible(true);
    }
}
