package Scene.objects;
import Levels.Level;
import Scene.objects.dependencies.*;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

import rMath.*;
import Scene.lighting.shaders.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class Shape {
    static final float degToRad = (float) (Math.PI / 180); // ratio of degrees to radians
    String id;
    Color colour;
    Vertex origin;
    Vertex[] vertices;
    ArrayList<Triangle> triangles = new ArrayList<>();
    Shader.Types shaderType;
    Level parent;
    float scale = 1;
    boolean visibility = true;

    // initialisation handlers
    public static Shape of(JSONObject object, Level parent) {
        Shape shape;
        JSONObject size = (JSONObject) object.get("size");
        JSONArray coordinates = (JSONArray) object.get("coordinate");
        JSONArray rotation = (JSONArray) object.get("rotation");
        Object colorObj = object.get("color");

        // coordinate handling
        float x = ((Double) coordinates.get(0)).floatValue();
        float y = ((Double) coordinates.get(1)).floatValue();
        float z = ((Double) coordinates.get(2)).floatValue();

        // color handling
        Color color;

        if (colorObj instanceof String) {
            // Color stored as a string (e.g., "BLACK")
            try {
                Field field = Class.forName("java.awt.Color").getField((String) colorObj);
                color = (Color)field.get(null);
            } catch (Exception e) {
                color = null; // Not defined
            }
        } else if (colorObj instanceof JSONArray colorArray) {
            // Color stored as an array (e.g., [255, 255, 255])
            int red = ((Long) colorArray.get(0)).intValue();
            int green = ((Long) colorArray.get(1)).intValue();
            int blue = ((Long) colorArray.get(2)).intValue();
            color = new Color(red, green, blue);
        } else {
            throw new IllegalArgumentException("Invalid color format");
        }

        shape = switch ((String) object.get("type")) {
            case ("Cube") -> new Cube(x, y, z, size, color, parent);
            case ("Cuboid") -> new Cuboid(x, y, z, size, color, parent);
            case ("Hexagonal Prism") -> new Hexagonal_Prism(x, y, z, size, color, parent);
            case ("Icosahedron") -> new Icosahedron(x, y, z, size, color, parent);
            case ("Plane") -> new Plane(x, y, z, size, color, parent);
            case ("Pyramid") -> new Pyramid(x, y, z, size, color, parent);

            //TODO: implement GLTF and OBJ formats
            case ("OBJ") -> new Cube(x, y, z, size, color, parent);
            case ("GLTF") -> new Cube(x, y, z, size, color, parent);
            default -> throw new IllegalArgumentException("Attempted to create illegal or unsupported object type");
        };

        shape.setId((String) object.get("id"));
        shape.setVisibility((boolean) object.get("visible"));
        shape.setShader(Shader.get((String) object.get("shading")));

        return shape;
    }

    void init(float[][] vertex_array, float x, float y, float z, float scale, Color colour, Level parent) {
        this.parent = parent;

        this.scale = scale;
        origin = new Vertex(x,y,z);

        // convert 2D int array into 1D array of vertex Objects
        Vertex[] vertices_temp = new Vertex[vertex_array.length];

        int i=0;
        for (float[] vert : vertex_array) {
            vertices_temp[i] = new Vertex(vert[0], vert[1], vert[2]);
            i++;
        }
        vertices = vertices_temp;

        int vert_len = vertices.length;
        float[][][] unique_tris = new float[vert_len][vert_len][vert_len];

        if (vertices.length > 2) {
            for (int v1=0; v1<vertices.length; v1++) {
                for (int v2=0; v2<vertices.length; v2++) {
                    for (int v3=0; v3<vertices.length; v3++) {
                        if (v1!=v2 && v1!=v3 && v2!=v3 && unique_tris[v1][v2][v3] == 0.0) {
                            triangles.add(new Triangle(new Vertex[]{vertices[v1], vertices[v2], vertices[v3]}));

                            unique_tris[v1][v2][v3] = 1f;
                            unique_tris[v1][v3][v2] = 1f;
                            unique_tris[v2][v1][v3] = 1f;
                            unique_tris[v2][v3][v1] = 1f;
                            unique_tris[v3][v1][v2] = 1f;
                            unique_tris[v3][v2][v1] = 1f;
                        }
                    }
                }
            }
        }

        //Assign colour
        this.colour = colour;
    }

    public Shape(float[][] vertex_array, float x, float y, float z, float scale, Color colour, Level parent) { // given colour
        init(vertex_array, x, y, z, scale, colour, parent);
    }

    public Shape(float[][] vertex_array, float x, float y, float z, float scale, Level parent) { // default colour
        init(vertex_array, x, y, z, scale, Color.BLACK, parent);
    }
    public Shape(float x, float y, float z, float scale, Color colour, Level parent) { // Empty vertices - Intended only for temporary purposes
        init(new float[][]{}, x, y, z, scale, colour, parent);
    }
    public Shape(float x, float y, float z, float scale, Level parent) { // Empty vertices - Intended only for temporary purposes
        init(new float[][]{}, x, y, z, scale, Color.BLACK, parent);
    }

    public void setScale(float new_scale) {
        this.scale = new_scale;
        parent.updateShape(this);
    }
    // Shape rotation using rotation matrices
    private void rotateX(float theta_x) {
        for (Vertex vertex : vertices) {
            float newY = (float) ((vertex.y * Math.cos(theta_x)) + (vertex.z * -Math.sin(theta_x)));
            float newZ = (float) ((vertex.y * Math.sin(theta_x)) + (vertex.z * Math.cos(theta_x)));

            vertex.y = newY;
            vertex.z = newZ;
        }
        parent.updateShape(this);
    }

    private void rotateY(float theta_y) {
        for (Vertex vertex : vertices) {
            float newX = (float) ((vertex.x * Math.cos(theta_y)) + (vertex.z * Math.sin(theta_y)));
            float newZ = (float) ((vertex.x * -Math.sin(theta_y)) + (vertex.z * Math.cos(theta_y)));

            vertex.x = newX;
            vertex.z = newZ;
        }
        parent.updateShape(this);
    }

    private void rotateZ(float theta_z) {
        for (Vertex vertex : vertices) {
            float newX = (float) ((vertex.x * Math.cos(theta_z)) + (vertex.y * -Math.sin(theta_z)));
            float newY = (float) ((vertex.x * Math.sin(theta_z)) + (vertex.y * Math.cos(theta_z)));

            vertex.x = newX;
            vertex.y = newY;
        }
        parent.updateShape(this);
    }

    private void applyTransform(Matrix M) {
        for (Vertex vertex : vertices) {
            vertex.set(rMath.Matrix.Multiply(M, vertex));
        }
        parent.updateShape(this);
    }

    public void setRotation(float theta_x, float theta_y, float theta_z) {
        if (theta_x != 0) {
            rotateX(theta_x*degToRad);
        }
        if (theta_y != 0) {
            rotateY(theta_y*degToRad);
        }
        if (theta_z != 0) {
            rotateZ(theta_z*degToRad);
        }
        parent.updateShape(this);
    }

    // -------------------------------------------------------------------------------------------------------------- //

    // Accessors and Mutators

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public Vertex getOrigin() {
        return origin;
    }

    public void setOrigin(Vertex origin) {
        this.origin = origin;
        parent.updateShape(this);
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }

    public float getScale() {
        return scale;
    }

    public boolean isVisible() {
        return visibility;
    }
    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private void setShader(Shader.Types shaderType) {
        this.shaderType = shaderType;
    }
    private void updateShader(Shader.Types shaderType) {
        setShader(shaderType);
        parent.updateShape(this);
    }

    public Shader.Types getShaderType() {
        return shaderType;
    }

    public void updateVisibility(boolean visibility) {
        setVisibility(visibility);
        parent.updateShape(this);
    }

    public void updateColour(Color color) {
        setColour(color);
        parent.updateShape(this);
    }

    public String toString() {
        return "Shape, ID: "+this.id+", COLOUR: "+this.getColour();
    }
}
