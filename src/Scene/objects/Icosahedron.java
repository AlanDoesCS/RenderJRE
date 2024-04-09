package Scene.objects;
import Scene.objects.dependencies.*;

import java.awt.Color;

import org.json.simple.JSONObject;
import rMath.*;

public class Icosahedron extends Shape {
    private void generateVertices(float width, float length, float height) {
        float phi = 1.618034F; // Golden ratio

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
    public Icosahedron(float x, float y, float z, float scale, Color colour) {
        super(x, y, z, scale, colour);

        generateVertices(1f, 1f, 1f);
    }

    public Icosahedron(float x, float y, float z, JSONObject size, Color color) {
        super(x, y, z, ((Double) size.get("scale")).floatValue(), color);

        generateVertices(((Double) size.get("width")).floatValue(), ((Double) size.get("length")).floatValue(), ((Double) size.get("height")).floatValue());
    }
}
