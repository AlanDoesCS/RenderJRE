package Rendering;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import Levels.Level;
import Levels.LevelHandler;
import Scene.lighting.DirectLight;
import Scene.objects.Shape;
import rMath.*;
import Scene.objects.dependencies.*;
import Tools.output;
import Tools.math;
import Tools.sort_and_search;

public class Renderer {
    final double degToRad = Math.PI / 180; // ratio of degrees to radians

    public int zoom;
    public double FOV, AspectRatio, ZFar = 1000, ZNear = 0.1;
    public int WindowResX, WindowResY;
    private String[] arguments = {};
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

    public Vertex2D[][] render_triangles(Triangle[] triangles, Shape parent) {
        // Get length of rendered vertex array
        int tri_list_length = triangles.length;

        // [Triangle][rMath.Vertex]
        Vertex2D[][] triangle_array = new Vertex2D[tri_list_length][3]; // convert x,y,z into x,y

        // Project vertices onto 2D plane
        for (int triangle=0; triangle<tri_list_length; triangle++) {
            Triangle tri = triangles[triangle];

            int i=0;
            for (Vertex vertex : tri.points) {
                triangle_array[triangle][i] = project3Dto2D(vertex, parent.getOrigin(), parent.getScale());
                i++;
            }
        }

        return triangle_array;
    }

    public static Shape[] order_shapes(Shape[] unsorted, Shape camera) {
        // perform most efficient sorting algorithm depending on size of array
        Shape[] sorted;

        if (unsorted.length < 55) {
            // Binary sort
            sorted = sort_and_search.binSort(unsorted, camera);
        } else {
            // Merge sort
            output.warnMessage("Merge sort feature not added yet for large shape quantity");
            return sort_and_search.binSort(unsorted, camera);
        }
        return sorted;
    }

    // --------------------------- Tris -----------------------------
    public Triangle[] order_triangles(ArrayList<Triangle> list, Shape object, Shape camera) {
        // remove triangles with z component normals facing away
        Vector3D normal_test = new Vector3D();

        Iterator<Triangle> itr = list.iterator();
        while (itr.hasNext()) {
            Triangle t = itr.next();
            normal_test = normal_test.normal(t);
            if (normal_test.k > 0) {
                itr.remove();
            }
        }

        // Convert ArrayList to array
        Triangle[] unsorted = new Triangle[list.size()];
        for (int i=0; i<list.size(); i++) {
            unsorted[i] = list.get(i);
        }

        // perform most efficient sorting algorithm depending on size of array
        Triangle[] sorted;
        if (unsorted.length < 55) {
            // Binary sort
            sorted = sort_and_search.binSort(unsorted, object, camera);
        } else {
            // Merge sort
            output.warnMessage("Merge sort feature not added yet for large triangle quantity");
            sorted = sort_and_search.binSort(unsorted, object, camera);
        }
        return sorted;
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

    // Argument Handling: ------------------------------------------
    public void renderScene(String arguments, Shape[] unsortedObjs, Shape camera, DirectLight light) {
        // format arguments
        String[] args = arguments.replace(" ", "").toLowerCase().split("-");

        // Fancy way of checking for the presence of an argument
        boolean cel = Arrays.asList(args).contains("cel");
        boolean wireframe = Arrays.asList(args).contains("wire");
        boolean diffuse = Arrays.asList(args).contains("diffuse");
        boolean fill = Arrays.asList(args).contains("fill");

        // Perform most efficient sorting algorithm based on input size
        Shape[] orderedObjs = order_shapes(unsortedObjs, camera);

        for (int shape_index = orderedObjs.length - 1; shape_index >= 0; shape_index--) {
            Shape shape = orderedObjs[shape_index];

            // Perform most efficient sorting algorithm based on input size
            Triangle[] orderedTriangles = order_triangles(shape.getTriangles(), shape, camera);

            Vertex2D[][] Triangle_Points = render_triangles(orderedTriangles, shape);

            // draw shapes

            // cell shader outline
            if (cel) {
                g2.setColor(shape.getColour().darker());
                g2.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                for (int tri_index = orderedTriangles.length - 1; tri_index >= 0; tri_index--) {
                    Vertex2D[] triangle = Triangle_Points[tri_index];
                    int[] xpoints = {(int) triangle[0].x, (int) triangle[1].x, (int) triangle[2].x};
                    int[] ypoints = {(int) (WindowResY - triangle[0].y), (int) (WindowResY - triangle[1].y), (int) (WindowResY - triangle[2].y)};

                    Polygon p = new Polygon(xpoints, ypoints, 3);  // This polygon represents a triangle with the above

                    g2.setColor(shape.getColour().darker());
                    g2.drawPolygon(p);
                }
            }

            for (int tri_index = orderedTriangles.length - 1; tri_index >= 0; tri_index--) {
                g2.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                if (diffuse) {
                    Color lit = diffuseBasic(shape.getColour(), light, orderedTriangles[tri_index]);
                    g2.setColor(lit);
                } else if (fill) { // diffuse takes priority over fill
                    g2.setColor(shape.getColour());
                }

                Vertex2D[] triangle = Triangle_Points[tri_index];
                int[] xpoints = {(int) triangle[0].x, (int) triangle[1].x, (int) triangle[2].x};
                int[] ypoints = {(int) (WindowResY - triangle[0].y), (int) (WindowResY - triangle[1].y), (int) (WindowResY - triangle[2].y)};

                Polygon p = new Polygon(xpoints, ypoints, 3);  // This polygon represents a triangle with the above

                // fill shape vertices.
                if (fill || diffuse) {
                    g2.fillPolygon(p);
                }

                // draw wireframe
                if (wireframe) {
                    g2.setColor(shape.getColour().darker());
                    g2.drawPolygon(p);
                }
            }
        }
    }

    // Accessors and Mutators
    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }
    public void setArguments(String arguments) {
        this.arguments = arguments.split(" ");
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
}