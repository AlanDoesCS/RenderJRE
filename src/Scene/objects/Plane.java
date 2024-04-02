package Scene.objects;
import Scene.objects.dependencies.*;

import java.awt.Color;

import org.json.simple.JSONObject;
import rMath.Vertex;

public class Plane extends Shape {
    private void generateVertices(float width, float length) {
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
    public Plane(float x, float y, float z, float width, float length, float scale, Color colour) {
        super(x, y, z, scale, colour);

        generateVertices(width, length);
    }

    public Plane(float x, float y, float z, JSONObject size, Color color) {
        super(x, y, z, (float) size.get("scale"), color);

        generateVertices((float) size.get("width"), (float) size.get("length"));
    }
}