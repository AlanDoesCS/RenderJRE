package Scene.objects;
import Scene.objects.dependencies.*;

import java.awt.Color;
import rMath.Vertex;

public class Plane extends Shape {
    public Plane(double x, double y, double z, double width, double length, double scale, Color colour) {
        super(x, y, z, scale, colour);

        // z axis plane
        Vertex A = new Vertex(1, 0, 1);
        Vertex B = new Vertex(1, 0, -1);
        Vertex C = new Vertex(-1, 0, 1);
        Vertex D = new Vertex(-1, 0, -1);

        this.vertices = new Vertex[] {A,B,C,D};

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