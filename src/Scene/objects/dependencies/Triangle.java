package Scene.objects.dependencies;

import Rendering.Pixel;
import rMath.Vertex;

public class Triangle {
    public Vertex[] points;
    public Vertex centroid, midpoint;

    private void init(Vertex[] array) {
        points = array;
        if (array.length > 1) {
            float max_x=0, min_x=0, max_y=0, min_y=0, max_z=0, min_z=0;
            for (int dimension=1; dimension<=3; dimension++) {
                if (dimension==1) {
                    max_x = array[0].x;
                    min_x = array[0].x;
                    for (int i = 1; i < array.length; i++) {
                        max_x = Math.max(max_x, array[i].x);
                        min_x = Math.min(min_x, array[i].x);
                    }
                } else if (dimension==2) {
                    max_y = array[0].y;
                    min_y = array[0].y;
                    for (int i = 1; i < array.length; i++) {
                        max_y = Math.max(max_y, array[i].y);
                        min_y = Math.min(min_y, array[i].y);
                    }
                } else if (dimension==3) {
                    max_z = array[0].z;
                    min_z = array[0].z;
                    for (int i = 1; i < array.length; i++) {
                        max_z = Math.max(max_z, array[i].z);
                        min_z = Math.min(min_z, array[i].z);
                    }
                }
            }
            midpoint = new Vertex((max_x+min_x)/2, (max_y+min_y)/2, (max_z+min_z)/2);

            float sum_x = 0, sum_y = 0, sum_z = 0;
            for (Vertex value : array) {
                sum_x += value.x;
                sum_y += value.y;
                sum_z += value.z;
            }
            centroid = new Vertex(sum_x/array.length, sum_y/array.length, sum_z/ array.length);
        } else if (array.length == 1) {
            centroid = new Vertex(array[0].x,array[0].y,array[0].z);
            midpoint = centroid;
        } else {
            centroid = new Vertex(0,0,0);
            midpoint = centroid;
        }
    }

    public Triangle(Vertex[] array) {
        init(array);
    }

    public Triangle(Vertex vertex, Vertex vertex1, Vertex vertex2) {
        init(new Vertex[] {vertex, vertex1, vertex2});
    }

    public Triangle(Pixel[] triangle) {
        init(new Vertex[] {triangle[0].toVertex(), triangle[1].toVertex(), triangle[2].toVertex()});
    }
}