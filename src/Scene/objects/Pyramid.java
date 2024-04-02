package Scene.objects;
import Scene.objects.dependencies.*;

import org.json.simple.JSONObject;
import rMath.Vertex;
import java.awt.Color;

public class Pyramid extends Shape {
    private void generateVertices(float width, float length, float height) {
        Vertex A = new Vertex(1, -1, 1);
        Vertex B = new Vertex(1, -1, -1);
        Vertex C = new Vertex(-1, -1, 1);
        Vertex D = new Vertex(-1, -1, -1);

        Vertex E = new Vertex(0, 1, 0);

        vertices = new Vertex[]{A,B,C,D,E};

        for (Vertex v : vertices) { // change to fill 1x1 space
            v.x *= width/2;
            v.y *= height/2;
            v.z *= length/2;
        }

        Vertex[][] faces = { // triangles
                {E, A, B}, {E, B, D},
                {E, D, C}, {E, C, A},
                {B, A, C}, {C, D, B},
        };

        for (Vertex[] face : faces) {
            triangles.add(new Triangle(face[0], face[1], face[2]));
        }
    }
    public Pyramid(float x, float y, float z, float scale, Color colour) {
        super(x, y, z, scale, colour);

        generateVertices(1f, 1f, 1f);
    }

    public Pyramid(float x, float y, float z, JSONObject size, Color color) {
        super(x, y, z, (float) size.get("scale"), color);

        generateVertices((float) size.get("width"), (float) size.get("length"), (float) size.get("height"));
    }
}