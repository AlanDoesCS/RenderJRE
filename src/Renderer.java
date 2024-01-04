import java.awt.*;
import java.util.ArrayList;

public class Renderer {
    public static double clamp (double min, double value, double max) {
        return Math.max(min, Math.min(max, value));
    }

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

    public Vertex2D project3Dto2D(Vertex vertex, Vertex origin, double objScale) {
        double relative_x = origin.x + objScale * vertex.x;
        double relative_y = origin.y + objScale * vertex.y;
        double relative_z = origin.z + objScale * vertex.z;

        Vertex2D projected = new Vertex2D();

        projected.x = (double) WindowResX /2 + (zoom * relative_x * FocalLength)/(relative_z+FocalLength);
        projected.y = WindowResY - ((double) WindowResY /2) + (zoom * relative_y * FocalLength)/(relative_z+FocalLength);

        return projected;
    }

    public Vertex2D[] render(Shape shape) {
        // Get length of rendered vertex array
        int vertex_array_length = shape.vertices.length;

        Vertex2D[] vertex_array = new Vertex2D[vertex_array_length]; // convert x,y,z into x,y

        // Project 3D vertices onto 2D plane

        int i=0;
        for (Vertex vertex : shape.vertices) {
            vertex_array[i] = project3Dto2D(vertex, shape.origin, shape.scale);
            i++;
        }

        return vertex_array;
    }
    public Vertex2D[][] render_triangles(Shape.Triangle[] triangles, Shape parent) {
        // Get length of rendered vertex array
        int tri_list_length = triangles.length;

        // [Triangle][Vertex]
        Vertex2D[][] triangle_array = new Vertex2D[tri_list_length][3]; // convert x,y,z into x,y

        // Project vertices onto 2D plane
        for (int triangle=0; triangle<tri_list_length; triangle++) {
            Shape.Triangle tri = triangles[triangle];

            int i=0;
            for (Vertex vertex : tri.points) {
                triangle_array[triangle][i] = project3Dto2D(vertex, parent.origin, parent.scale);
                i++;
            }
        }

        return triangle_array;
    }

    public static double distance(Shape shape1, Shape shape2) {
        double dx = shape1.origin.x - shape2.origin.x;
        double dy = shape1.origin.y - shape2.origin.y;
        double dz = shape1.origin.z - shape2.origin.z;

        return Math.sqrt(dx*dx + dy*dy + dz*dz); // return magnitude of displacement vector
    }

    public static double distance(Shape.Triangle triangle, Shape parent, Shape shape2) {
        double dx = parent.origin.x + triangle.midpoint.x - shape2.origin.x;
        double dy = parent.origin.y + triangle.midpoint.y - shape2.origin.y;
        double dz = parent.origin.z + triangle.midpoint.z - shape2.origin.z;

        return Math.sqrt(dx*dx + dy*dy + dz*dz); // return magnitude of displacement vector
    }

    public static int binSearch(Shape[] array, int l, int r, Shape key, Shape reference_shape) { // find element just further away than given shape
        double key_distance = distance(key, reference_shape);

        while (l<=r) {
            int mid = l + (r-l)/2;

            if (array[mid] == key) {
                return mid+1;
            } else if (key_distance > distance(array[mid], reference_shape)) {
                l = mid+1;
            } else {
                r = mid-1;
            }
        }

        return l;
    }
    public static Shape[] binSort(Shape[] array, Shape camera) { // binary insertion sort
        int i, loc, j;
        Shape selected;
        int n = array.length;

        for (i = 1; i < n; i++) {
            j = i - 1;
            selected = array[i];

            // find location where selected should be inserted
            loc = binSearch(array, 0, j, selected, camera);

            // Shift elements along
            while (j >= loc) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = selected;
        }

        return array;
    }

    public static Shape[] order_shapes(Shape[] unsorted, Shape camera) {
        // perform most efficient sorting algorithm depending on size of array
        Shape[] sorted = unsorted;

        if (unsorted.length < 55) {
            // Binary sort
            sorted = binSort(unsorted, camera);
        } else {
            // Merge sort
            System.out.println("Merge sort feature not added yet for large shape quantity");
            return unsorted;
        }
        return sorted;
    }

    // --------------------------- Tris -----------------------------
    public static int binSearch(Shape.Triangle[] array, int l, int r, Shape.Triangle key, Shape object, Shape reference_shape) { // find element just further away than given reference shape
        double key_distance = distance(key, object, reference_shape);

        while (l<=r) {
            int mid = l + (r-l)/2;

            if (array[mid] == key) {
                return mid+1;
            } else if (key_distance > distance(array[mid], object, reference_shape)) {
                l = mid+1;
            } else {
                r = mid-1;
            }
        }

        return l;
    }
    public static Shape.Triangle[] binSort(Shape.Triangle[] array, Shape object, Shape camera) { // binary insertion sort
        int i, loc, j;
        Shape.Triangle selected;
        int n = array.length;

        for (i = 1; i < n; i++) {
            j = i - 1;
            selected = array[i];

            // find location where selected should be inserted
            loc = binSearch(array, 0, j, selected, object, camera);

            // Shift elements along
            while (j >= loc) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = selected;
        }

        return array;
    }

    public static Shape.Triangle[] order_triangles(ArrayList<Shape.Triangle> list, Shape object, Shape camera) {
        // Convert ArrayList to array
        Shape.Triangle[] unsorted = new Shape.Triangle[list.size()];
        for (int i=0; i<list.size(); i++) {
            unsorted[i] = list.get(i);
        }

        // perform most efficient sorting algorithm depending on size of array
        Shape.Triangle[] sorted;
        if (unsorted.length < 55) {
            // Binary sort
            sorted = binSort(unsorted, object, camera);
        } else {
            // Merge sort
            System.out.println("Merge sort feature not added yet for large triangle quantity");
            sorted = binSort(unsorted, object, camera);
        }
        return sorted;
    }

    public Color diffuseBasic(Color base, LightSource light, Shape.Triangle object, double distance) {
        Vector3D colorVect = new Vector3D(light.ambient.i, light.ambient.j, light.ambient.k);

        Vector3D normal = new Vector3D();
        normal = normal.normal(object);
        normal.normalise(); // normalise length of the vector

        double diffuseStrength = Math.abs(light.direction.dot(normal));

        colorVect.i = clamp(0,diffuseStrength * base.getRed(), 255);
        colorVect.j = clamp(0,diffuseStrength * base.getGreen(), 255);
        colorVect.k = clamp(0,diffuseStrength * base.getBlue(), 255);

        return new Color((int) colorVect.i, (int) colorVect.j, (int) colorVect.k);
    }
}