package Scene.objects;
import Scene.objects.dependencies.*;

import java.awt.Color;

import org.json.simple.JSONObject;
import rMath.*;

public class Icosahedron extends Shape {
    private void generateVertices(double width, double length, double height) {
        double phi = (1 + Math.sqrt(5)) / 2; // Golden ratio

        Vertex A = new Vertex(0, 1, phi);
        Vertex B = new Vertex(0, -1, phi);
        Vertex C = new Vertex(0, 1, -phi);
        Vertex D = new Vertex(0, -1, -phi);

        Vertex E = new Vertex(1, phi, 0);
        Vertex F = new Vertex(-1, phi, 0);
        Vertex G = new Vertex(1, -phi, 0);
        Vertex H = new Vertex(-1, -phi, 0);

        Vertex I = new Vertex(phi, 0, 1);
        Vertex J = new Vertex(-phi, 0, 1);
        Vertex K = new Vertex(phi, 0, -1);
        Vertex L = new Vertex(-phi, 0, -1);

        vertices = new Vertex[]{A,B,C,D,E,F,G,H,I,J,K,L};

        for (Vertex v : vertices) { // change to fill 1x1 space
            v.x /= 2;
            v.y /= 2;
            v.z /= 2;
        }

        Vertex[][] faces = { // triangles
                {C, F, E}, {A, E, F},
                {A, I, E}, {E, K, C},
                {C, L, F}, {F, J, A},
                {A, J, B}, {B, I, A},
                {E, I, K}, {G, K, I},
                {C, K, D}, {D, L, C},
                {F, L, J}, {H, J, L},
                {J, H, B}, {L, D, H},
                {K, G, D}, {I, G, B},
                {D, G, H}, {B, H, G}
        };

        for (Vertex[] face : faces) {
            triangles.add(new Triangle(face[0], face[1], face[2]));
        }
    }
    public Icosahedron(double x, double y, double z, double scale, Color colour) {
        super(x, y, z, scale, colour);

        generateVertices(1., 1., 1.);
    }

    public Icosahedron(double x, double y, double z, JSONObject size, Color color) {
        super(x, y, z, (double) size.get("scale"), color);

        generateVertices((double) size.get("width"), (double) size.get("length"), (double) size.get("height"));
    }
}
