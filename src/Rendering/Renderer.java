package Rendering;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Levels.*;
import Scene.Camera;
import Scene.lighting.DirectLight;
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

        float diffuseStrength = Math.abs(Math.min(0, light.direction.dot(normal)));

        colorVect.i = math.clamp(0,diffuseStrength * base.getRed(), 255);
        colorVect.j = math.clamp(0,diffuseStrength * base.getGreen(), 255);
        colorVect.k = math.clamp(0,diffuseStrength * base.getBlue(), 255);

        return new Color((int) colorVect.i, (int) colorVect.j, (int) colorVect.k);
    }

    public void drawLine2D(Pixel a, Pixel b) {
        // always start with left-most pixel
        Pixel source = Tools.math.minX(a, b);
        Pixel target = Tools.math.maxX(a, b);
        zBuffer.add(source);
        zBuffer.add(target);

    }
    public void drawLine3D(Pixel a, Pixel b) {
        // always start with left-most pixel
        Pixel source = Tools.math.minX(a, b);
        Pixel target = Tools.math.maxX(a, b);
        zBuffer.add(source);
        zBuffer.add(target);
        bresenham3D(source, target);
    }
    public void outlineTriangle(Vertex2D[] triangle, Color outlineColor) {
        Pixel A = new Pixel(triangle[0], outlineColor);
        Pixel B = new Pixel(triangle[1], outlineColor);
        Pixel C = new Pixel(triangle[2], outlineColor);

        drawLine3D(A, B);
        drawLine3D(A, C);
        drawLine3D(B, C);
    }
    public void scanlineTriangle(Vertex2D[] triangle) {
        Vertex2D A = triangle[0];
        Vertex2D B = triangle[1];
        Vertex2D C = triangle[2];
    }

    // Argument Handling: ------------------------------------------
    public void renderScene() {
        Camera camera = new Camera(0f, 0f, 0f);
        zBuffer = new DepthBuffer(WindowResX, WindowResY);

        boolean cel = arguments.contains("cel");
        boolean wireframe = arguments.contains("wire");
        boolean diffuse = arguments.contains("diffuse");
        boolean fill = arguments.contains("fill");

        ArrayList<Shape> sceneObjects = levelHandler.getLevelObjects();

        for (Shape shape : sceneObjects) {
            if (shape.isVisible()) {
                ArrayList<Triangle> triangles = shape.getTriangles();

                Vertex2D[][] Triangle_Points = render_triangle_vertices(triangles, shape);

                // cell shader outline
                if (wireframe) {
                    Color outlineColor = shape.getColour();

                    for (Vertex2D[] triangle : Triangle_Points) {
                        outlineTriangle(triangle, outlineColor);
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

    /*
    This code is an adapted version of the contributions by ishankhandelwals and Anant Agarwal to GeeksForGeeks.org
     */

    public void bresenham3D(Pixel a, Pixel b) {
        int x1= (int) a.getX(), y1= (int) a.getY(), z1= (int) a.getZ(), x2= (int) b.getX(), y2= (int) b.getY(), z2= (int) b.getZ();

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int dz = Math.abs(z2 - z1);
        int xs;
        int ys;
        int zs;
        if (x2 > x1) {
            xs = 1;
        } else {
            xs = -1;
        }
        if (y2 > y1) {
            ys = 1;
        } else {
            ys = -1;
        }
        if (z2 > z1) {
            zs = 1;
        } else {
            zs = -1;
        }

        // Driving axis is X-axis
        if (dx >= dy && dx >= dz) {
            int p1 = 2 * dy - dx;
            int p2 = 2 * dz - dx;
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
                zBuffer.add(new Pixel(x1, y1, z1));
            }

            // Driving axis is Y-axis"
        } else if (dy >= dx && dy >= dz) {
            int p1 = 2 * dx - dy;
            int p2 = 2 * dz - dy;
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
                zBuffer.add(new Pixel(x1, y1, z1));
            }

            // Driving axis is Z-axis"
        } else {
            int p1 = 2 * dy - dz;
            int p2 = 2 * dx - dz;
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
                zBuffer.add(new Pixel(x1, y1, z1));
            }
        }
    }
}