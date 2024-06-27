package Rendering;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import Levels.*;
import Scene.Camera;
import Scene.lighting.DirectLight;
import Scene.lighting.Light;
import Scene.objects.Shape;
import rMath.*;
import Scene.objects.dependencies.*;
import Tools.math;

public class Renderer {
    final float degToRad = (float) (Math.PI / 180); // ratio of degrees to radians

    public int zoom;
    public float FOV, AspectRatio, ZFar = 1000f, ZNear = 0.1f;
    public int WindowResX, WindowResY;
    private ArrayList<String> arguments = new ArrayList<>();
    private LevelHandler levelHandler;
    private DepthBuffer zBuffer;
    public Renderer(int zoom, float FOV, int WindowResX, int WindowResY)
    {
        this.zoom = zoom;
        this.FOV = FOV * degToRad;
        this.WindowResX = WindowResX;
        this.WindowResY = WindowResY;
        //this.AspectRatio = (float) WindowResY / WindowResX;
        this.AspectRatio = 1f;
        this.zBuffer = new DepthBuffer(WindowResX, WindowResY);
        this.levelHandler = new LevelHandler();
    }

    public Vertex2D project3Dto2D(Vertex vertex, Vertex origin, float objScale) {
        float relative_x = origin.x + objScale * vertex.x;
        float relative_y = origin.y + objScale * vertex.y;
        float relative_z = origin.z + objScale * vertex.z;

        Vertex2D projected = new Vertex2D();

        float fl = 1/(float) Math.tan(FOV/2);
        float ZNormal = ZFar / (ZFar - ZNear);
        projected.x = (float) WindowResX/2 + zoom * (AspectRatio*fl*relative_x)/relative_z;
        projected.y = (float) WindowResY/2 + (zoom * (fl * relative_y) / relative_z);

        projected.z = relative_z * ZNormal - ZNear * ZNormal;

        return projected;
    }

    public Pixel[][] render_triangles(Shape shape) {
        ArrayList<Triangle> triangles = shape.getTriangles();
        Pixel[][] pixel_array = new Pixel[triangles.size()][3]; // convert x,y,z into x,y

        // Project vertices onto 2D plane
        int triangle = 0;
        for (Triangle tri : triangles) {
            int i=0;
            for (Vertex vertex : tri.points) {
                Pixel p = new Pixel(project3Dto2D(vertex, shape.getOrigin(), shape.getScale()), shape.getColour());
                p.setVertex(vertex);
                pixel_array[triangle][i] = p;
                i++;
            }
            triangle++;
        }

        return pixel_array;
    }

    public Color computeFragmentColor(Color base, ArrayList<Light> lights, Pixel[] triangle) { // base == shape color
        DirectLight light = (DirectLight) lights.getFirst();
        light.direction.normalise();
        Vector3D colorVect = new Vector3D();

        Vector3D normal = new Vector3D();
        normal = normal.normal(Pixel.toTriangle(triangle));
        normal.normalise(); // normalise length of the vector

        float diffuseStrength = Math.min(0, light.direction.dot(normal));

        colorVect.i = math.clamp(0,diffuseStrength * base.getRed(), 255);
        colorVect.j = math.clamp(0,diffuseStrength * base.getGreen(), 255);
        colorVect.k = math.clamp(0,diffuseStrength * base.getBlue(), 255);

        return new Color((int) colorVect.i, (int) colorVect.j, (int) colorVect.k);
    }

    public Color computeVertexColor(Color base, ArrayList<Light> lights, Pixel p) { // base == shape color, p must have vertex property
        assert p.isVertex();
        DirectLight light = (DirectLight) lights.getFirst();
        light.direction.normalise();
        Vector3D colorVect = new Vector3D();

        Vector3D normal = p.getVertexIfValid().toVector3D();
        normal.normalise(); // normalise length of the vector

        float diffuseStrength = Math.abs(Math.min(0, light.direction.dot(normal)));

        colorVect.i = math.clamp(0,diffuseStrength * base.getRed(), 255);
        colorVect.j = math.clamp(0,diffuseStrength * base.getGreen(), 255);
        colorVect.k = math.clamp(0,diffuseStrength * base.getBlue(), 255);

        return new Color((int) colorVect.i, (int) colorVect.j, (int) colorVect.k);
    }

    public void drawLine3D(Pixel a, Pixel b) {
        bresenham3D(a, b);
    }
    public void outlineTriangle(Pixel[] tri) {
        drawLine3D(tri[0], tri[1]);
        drawLine3D(tri[0], tri[2]);
        drawLine3D(tri[1], tri[2]);
    }
    public void fillTriangle(Pixel[] tri) {
        EdgeTable[] edgeTables = {
                bresenham3D(tri[0], tri[1], false),
                bresenham3D(tri[0], tri[2], false),
                bresenham3D(tri[1], tri[2], false)
        };

        int[] minmax1 = edgeTables[0].min_max_Keys();
        int[] minmax2 = edgeTables[1].min_max_Keys();
        int[] minmax3 = edgeTables[2].min_max_Keys();

        int minY = Math.min(Math.min(minmax1[0], minmax2[0]), minmax3[0]);
        int maxY = Math.max(Math.max(minmax1[1], minmax2[1]), minmax3[1]);

        for (int y = minY; y<=maxY; y++) {
            Pixel min = new Pixel(Integer.MAX_VALUE, 0, 0);
            Pixel max = new Pixel(Integer.MIN_VALUE, 0, 0);

            for (EdgeTable edgeTable : edgeTables) {
                if (edgeTable.containsKey(y)) {
                    if (edgeTable.get(y).getX() < min.getX()) {
                        min = edgeTable.get(y);
                    }

                    if (edgeTable.get(y).getX() > max.getX()) {
                        max = edgeTable.get(y);
                    }
                }
            }
            drawLine3D(min, max);
        }
    }

    private void fillTriangle(Pixel[] tri, Color colorOverride) {
        EdgeTable[] edgeTables = {
                bresenham3D(tri[0], tri[1], false),
                bresenham3D(tri[0], tri[2], false),
                bresenham3D(tri[1], tri[2], false)
        };

        int[] minmax1 = edgeTables[0].min_max_Keys();
        int[] minmax2 = edgeTables[1].min_max_Keys();
        int[] minmax3 = edgeTables[2].min_max_Keys();

        int minY = Math.min(Math.min(minmax1[0], minmax2[0]), minmax3[0]);
        int maxY = Math.max(Math.max(minmax1[1], minmax2[1]), minmax3[1]);

        for (int y = minY; y<=maxY; y++) {
            Pixel min = new Pixel(Integer.MAX_VALUE, 0, 0);
            Pixel max = new Pixel(Integer.MIN_VALUE, 0, 0);

            for (EdgeTable edgeTable : edgeTables) {
                if (edgeTable.containsKey(y)) {
                    if (edgeTable.get(y).getX() < min.getX()) {
                        min = edgeTable.get(y);
                    }

                    if (edgeTable.get(y).getX() > max.getX()) {
                        max = edgeTable.get(y);
                    }
                }
            }
            // TODO: Better colour setting
            min.setColor(colorOverride);
            max.setColor(colorOverride);
            drawLine3D(min, max);
        }
    }
    private void flatShade(Pixel[][] Triangle_Pixels, Shape parent, ArrayList<Light> lights) {
        for (Pixel[] pixels : Triangle_Pixels) fillTriangle(pixels, computeFragmentColor(parent.getColour(), lights, pixels));
    }
    private void fill(Pixel[][] Triangle_Pixels, Shape parent) { // No shading, just filling colours
        for (Pixel[] pixels : Triangle_Pixels) fillTriangle(pixels, parent.getColour());
    }
    private void wireframe(Pixel[][] Triangle_Pixels) { // No shading, just outline
        for (Pixel[] pixels : Triangle_Pixels) outlineTriangle(pixels);
    }
    private void gouraudShade(Pixel[][] Triangle_Pixels, Shape parent, ArrayList<Light> lights) { // No shading, just filling colours
        for (Pixel[] pixels : Triangle_Pixels) {
            // set vert cols
            for (int i=0; i<pixels.length; i++) {
                pixels[i].setColor(computeVertexColor(parent.getColour(), lights, pixels[i]));
            }
            fillTriangle(pixels);
        }
    }

    // Argument Handling: ------------------------------------------
    public void renderScene() {
        Camera camera = new Camera(0f, 0f, 0f, levelHandler.getCurrent());
        zBuffer = new DepthBuffer(WindowResX, WindowResY);

        ArrayList<Shape> sceneObjects = levelHandler.getLevelObjects();

        ArrayList<Light> lights = levelHandler.getLevelLights();

        for (Shape shape : sceneObjects) {
            if (shape.isVisible()) {
                Pixel[][] Triangle_Pixels = render_triangles(shape);

                switch (shape.getShaderType()) {
                    case FLAT:
                        flatShade(Triangle_Pixels, shape, lights);
                        break;
                    case GOURAUD:
                        gouraudShade(Triangle_Pixels, shape, lights);
                        break;
                    case PHONG:
                        flatShade(Triangle_Pixels, shape, lights);
                        break;
                    case WIREFRAME:
                        wireframe(Triangle_Pixels);
                        break;
                    case FILL:
                        fill(Triangle_Pixels, shape);
                    default:
                        flatShade(Triangle_Pixels, shape, lights);
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

    /*
    This code is an adapted version of the contributions by ishankhandelwals and Anant Agarwal to GeeksForGeeks.org
     */

    public EdgeTable bresenham3D(Pixel a, Pixel b, boolean shouldDraw) {
        EdgeTable edgeTable = new EdgeTable();

        int x1 = (int) a.getX(), y1 = (int) a.getY(), z1 = (int) a.getZ();
        int x2 = (int) b.getX(), y2 = (int) b.getY(), z2 = (int) b.getZ();

        int r1 = a.getColor().getRed(), g1 = a.getColor().getGreen(), b1 = a.getColor().getBlue();
        int r2 = b.getColor().getRed(), g2 = b.getColor().getGreen(), b2 = b.getColor().getBlue();

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int dz = Math.abs(z2 - z1);

        int xs = x2 > x1 ? 1 : -1;
        int ys = y2 > y1 ? 1 : -1;
        int zs = z2 > z1 ? 1 : -1;

        int p1, p2;

        // Driving axis is X-axis
        if (dx >= dy && dx >= dz) {
            p1 = 2 * dy - dx;
            p2 = 2 * dz - dx;
            while (x1 != x2) {
                x1 += xs;
                if (p1 >= 0) {
                    y1 += ys;
                    p1 -= 2 * dx;
                }
                if (p2 >= 0) {
                    z1 += zs;
                    p2 -= 2 * dx;
                }
                p1 += 2 * dy;
                p2 += 2 * dz;
                int interpolatedR = interpolate(r1, r2, x1, x2);
                int interpolatedG = interpolate(g1, g2, x1, x2);
                int interpolatedB = interpolate(b1, b2, x1, x2);

                Pixel px = new Pixel(x1, y1, z1, new Color(interpolatedR, interpolatedG, interpolatedB));
                if (shouldDraw) zBuffer.add(px);
                edgeTable.put(y1, px);
            }
        } else if (dy >= dx && dy >= dz) { // Driving axis is Y-axis
            p1 = 2 * dx - dy;
            p2 = 2 * dz - dy;
            while (y1 != y2) {
                y1 += ys;
                if (p1 >= 0) {
                    x1 += xs;
                    p1 -= 2 * dy;
                }
                if (p2 >= 0) {
                    z1 += zs;
                    p2 -= 2 * dy;
                }
                p1 += 2 * dx;
                p2 += 2 * dz;
                int interpolatedR = interpolate(r1, r2, y1, y2);
                int interpolatedG = interpolate(g1, g2, y1, y2);
                int interpolatedB = interpolate(b1, b2, y1, y2);

                Pixel px = new Pixel(x1, y1, z1, new Color(interpolatedR, interpolatedG, interpolatedB));
                if (shouldDraw) zBuffer.add(px);
                edgeTable.put(y1, px);
            }
        } else { // Driving axis is Z-axis
            p1 = 2 * dy - dz;
            p2 = 2 * dx - dz;
            while (z1 != z2) {
                z1 += zs;
                if (p1 >= 0) {
                    y1 += ys;
                    p1 -= 2 * dz;
                }
                if (p2 >= 0) {
                    x1 += xs;
                    p2 -= 2 * dz;
                }
                p1 += 2 * dy;
                p2 += 2 * dx;
                int interpolatedR = interpolate(r1, r2, z1, z2);
                int interpolatedG = interpolate(g1, g2, z1, z2);
                int interpolatedB = interpolate(b1, b2, z1, z2);

                Pixel px = new Pixel(x1, y1, z1, new Color(interpolatedR, interpolatedG, interpolatedB));
                if (shouldDraw) zBuffer.add(px);
                edgeTable.put(y1, px);
            }
        }
        return edgeTable;
    }
    public EdgeTable bresenham3D(Pixel a, Pixel b) {return bresenham3D(a, b, true);}

    // Function to interpolate color component
    private int interpolate(int start, int end, int step, int totalSteps) {
        return (int) math.clamp(0, 255, start + (float) ((end - start) * step) / totalSteps);
    }
}