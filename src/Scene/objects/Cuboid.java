package Scene.objects;
import Scene.objects.dependencies.*;

import rMath.*;
import java.awt.Color;

public class Cuboid extends Shape {
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
