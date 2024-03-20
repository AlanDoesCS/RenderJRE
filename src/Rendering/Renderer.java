package Rendering;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import Levels.*;
import Scene.Camera;
import Scene.lighting.DirectLight;
import Scene.objects.Shape;
import rMath.*;
import Scene.objects.dependencies.*;
import Tools.math;

public class Renderer {
    final double degToRad = Math.PI / 180; // ratio of degrees to radians

    public int zoom;
    public double FOV, AspectRatio, ZFar = 1000, ZNear = 0.1;
    public int WindowResX, WindowResY;
    private ArrayList<String> arguments = new ArrayList<>();
    private LevelHandler levelHandler;
    private DepthBuffer zBuffer;
    public Renderer(int zoom, double FOV, int WindowResX, int WindowResY)
    {
        this.zoom = zoom;
        this.FOV = FOV*degToRad;
        this.WindowResX = WindowResX;
        this.WindowResY = WindowResY;
        this.AspectRatio = (double) WindowResY / WindowResX;
        this.zBuffer = new DepthBuffer(WindowResX, WindowResY);
        this.levelHandler = new LevelHandler();
    }

    public Vertex2D project3Dto2D(Vertex vertex, Vertex origin, double objScale) {
        double relative_x = origin.x + objScale * vertex.x;
        double relative_y = origin.y + objScale * vertex.y;
        double relative_z = origin.z + objScale * vertex.z;

        Vertex2D projected = new Vertex2D();

        double fl = 1/Math.tan(FOV/2);
        double ZNormal = ZFar / (ZFar - ZNear);
        projected.x = (double) WindowResX/2 + zoom * (AspectRatio*fl*relative_x)/relative_z;
        projected.y = (double) WindowResY/2 + (zoom * (fl * relative_y) / relative_z);

        projected.z = relative_z * ZNormal - ZNear * ZNormal;

        return projected;
    }

    public Vertex2D[][] render_triangle_vertices(ArrayList<Triangle> triangles, Shape parent) {
        // Get length of rendered vertex array
        int tri_list_length = triangles.size();

        // [Triangle][rMath.Vertex]
        Vertex2D[][] triangle_array = new Vertex2D[tri_list_length][3]; // convert x,y,z into x,y

        // Project vertices onto 2D plane
        int triangle = 0;
        for (Triangle tri : triangles) {
            int i=0;
            for (Vertex vertex : tri.points) {
                triangle_array[triangle][i] = project3Dto2D(vertex, parent.getOrigin(), parent.getScale());
                i++;
            }
            triangle++;
        }

        return triangle_array;
    }

    public Color diffuseBasic(Color base, DirectLight light, Triangle object) { // base == shape color
        light.direction.normalise();
        Vector3D colorVect = new Vector3D();

        Vector3D normal = new Vector3D();
        normal = normal.normal(object);
        normal.normalise(); // normalise length of the vector

        double diffuseStrength = Math.abs(Math.min(0, light.direction.dot(normal)));

        colorVect.i = math.clamp(0,diffuseStrength * base.getRed(), 255);
        colorVect.j = math.clamp(0,diffuseStrength * base.getGreen(), 255);
        colorVect.k = math.clamp(0,diffuseStrength * base.getBlue(), 255);

        return new Color((int) colorVect.i, (int) colorVect.j, (int) colorVect.k);
    }

    /*
                     A
                     /\
                    /    \
                  B/___     \
                        ----___\C
    */
    public ArrayList<Pixel> drawLine(Pixel a, Pixel b) {
        ArrayList<Pixel> linePixels = new ArrayList<>();

        // always start with left-most pixel
        Pixel source = Tools.math.minX(a, b);
        Pixel target = Tools.math.maxX(a, b);

        double dx = target.getX() - source.getX();
        double dy = target.getY() - source.getY();
        double dz = target.getZ() - source.getZ();

        int dR = target.getColor().getRed() - source.getColor().getRed();
        int dG = target.getColor().getGreen() - source.getColor().getGreen();
        int dB = target.getColor().getBlue() - source.getColor().getBlue();

        while (source.getX() < target.getX()) {
            source.addX(1);
            source.addY(dy/dx);
            source.addZ(dz/dx);

            int cr = source.getColor().getRed() + dR;
            int cg = source.getColor().getGreen() + dG;
            int cb = source.getColor().getBlue() + dB;
            source.setColor( new Color(cr, cg, cb));

            linePixels.add(source);
        }

        return linePixels;
    }
    public ArrayList<Pixel> outlineTriangle(Vertex2D[] triangle, Color outlineColor) {
        ArrayList<Pixel> pixels = new ArrayList<>(3);

        Pixel A = new Pixel(triangle[0], outlineColor);
        Pixel B = new Pixel(triangle[1], outlineColor);
        Pixel C = new Pixel(triangle[2], outlineColor);

        pixels.addAll(drawLine(A, B));
        pixels.addAll(drawLine(A, C));
        pixels.addAll(drawLine(B, C));

        return pixels;
    }
    public ArrayList<Pixel> scanlineTriangle(Vertex2D[] triangle) {
        ArrayList<Pixel> pixels = new ArrayList<>(3);

        Vertex2D A = triangle[0];
        Vertex2D B = triangle[1];
        Vertex2D C = triangle[2];


        return pixels;
    }

    // Argument Handling: ------------------------------------------
    public void renderScene() {
        Camera camera = new Camera(0., 0., 0.);
        zBuffer = new DepthBuffer(WindowResX, WindowResY);

        boolean cel = arguments.contains("cel");
        boolean wireframe = arguments.contains("wire");
        boolean diffuse = arguments.contains("diffuse");
        boolean fill = arguments.contains("fill");

        ArrayList<Shape> sceneObjects = levelHandler.getLevelObjects();

        for (Shape shape : sceneObjects) {
            if (shape.isVisible()) {
                // Perform most efficient sorting algorithm based on input size
                ArrayList<Triangle> triangles = shape.getTriangles();

                Vertex2D[][] Triangle_Points = render_triangle_vertices(triangles, shape);

                // cell shader outline
                if (wireframe) {
                    Color outlineColor = shape.getColour().darker();

                    for (Vertex2D[] triangle : Triangle_Points) {
                        ArrayList<Pixel> pixels = outlineTriangle(triangle, outlineColor);
                        for (Pixel p : pixels) {
                            System.out.println("("+p.getX()+", "+p.getY()+", "+p.getZ()+")");
                            zBuffer.add(p);
                        }

                    }
                }
            }
        }
    }

    // Accessors and Mutators
    public void setArguments(ArrayList<String> arguments) {
        this.arguments = arguments;
    }
    public void setArguments(String arguments) {
        this.arguments = (ArrayList<String>) Arrays.asList(arguments.split(" "));;
    }
    public DepthBuffer getDepthBuffer() {
        return zBuffer;
    }

    // Level handling --------------------------------------------------------------------------------------------------
    public void addLevel(Level level) {
        levelHandler.addLevel(level);
    }
    // Load level by index
    public void loadLevel(int index) {
        levelHandler.setCurrent(index);
    }
    // Load level by name
    public void loadLevel(String name) {
        levelHandler.setCurrent(name);
    }
    // Load level by parent Class
    public void loadLevel(Class<? extends Level> parentClass) {
        levelHandler.setCurrent(parentClass);
    }
    public void loadNextLevel() {
        levelHandler.goToNext();
    }
}