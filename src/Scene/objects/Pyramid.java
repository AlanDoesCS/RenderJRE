package Scene.objects;
import Levels.Level;
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

    public Pyramid(float x, float y, float z, JSONObject size, Color color, Level parent) {
        super(x, y, z, ((Double) size.get("scale")).floatValue(), color, parent);

        generateVertices(((Double) size.get("width")).floatValue(), ((Double) size.get("length")).floatValue(), ((Double) size.get("height")).floatValue());
    }
}