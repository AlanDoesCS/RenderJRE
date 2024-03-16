package Scene.objects;
import Scene.objects.dependencies.*;

import rMath.Vertex;
import java.awt.Color;

public class Pyramid extends Shape {
    public Pyramid(double x, double y, double z, double scale, Color colour) {
        super(x, y, z, scale, colour);

        Vertex A = new Vertex(1, -1, 1);
        Vertex B = new Vertex(1, -1, -1);
        Vertex C = new Vertex(-1, -1, 1);
        Vertex D = new Vertex(-1, -1, -1);

        Vertex E = new Vertex(0, 1, 0);

        vertices = new Vertex[]{A,B,C,D,E};

        Vertex[][] faces = { // triangles
                {E, A, B}, {E, B, D},
                {E, D, C}, {E, C, A},
                {B, A, C}, {C, D, B},
        };

        for (Vertex[] face : faces) {
            triangles.add(new Triangle(face[0], face[1], face[2]));
        }
    }
}