package Tools;

import Scene.objects.Shape;
import Scene.objects.dependencies.Triangle;

public class sort_and_search {
    // Shapes ----------------------------------------------------------------------------------------------------------
    public static int binSearch(Shape[] array, int l, int r, Shape key, Shape reference_shape) { // find element just further away than given shape
        double key_distance = math.distance(key, reference_shape);

        while (l<=r) {
            int mid = l + (r-l)/2;

            if (array[mid] == key) {
                return mid+1;
            } else if (key_distance > math.distance(array[mid], reference_shape)) {
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

    // Triangles -------------------------------------------------------------------------------------------------------
    public int binSearch(Triangle[] array, int l, int r, Triangle key, Shape object, Shape reference_shape) { // find element just further away than given reference shape
        double key_distance = math.distance(key, object, reference_shape); // distance(key, object, reference_shape);

        while (l<=r) {
            int mid = l + (r-l)/2;

            if (array[mid] == key) {
                return mid+1;
            } else if (key_distance > math.distance(array[mid], object, reference_shape)) {
                l = mid+1;
            } else {
                r = mid-1;
            }
        }

        return l;
    }
    public static Triangle[] binSort(Triangle[] array, Shape object, Shape camera) { // binary insertion sort
        int i, loc, j;
        Triangle selected;
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
}
