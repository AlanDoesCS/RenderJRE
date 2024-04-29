package Rendering;

import Scene.objects.dependencies.Triangle;
import rMath.Vector3D;
import rMath.Vertex;
import rMath.Vertex2D;

import java.awt.*;

public class Pixel {
    private Color color;
    private Vector3D coordinate;
    private boolean isVertex = false;
    private Vertex vertexIfValid = null;

    public Pixel(Vector3D coordinate, Color color) {
        this.coordinate = coordinate;
        this.color = color;
    }

    public Pixel(Vertex2D vertex2D, Color color) {
        this.coordinate = new Vector3D(vertex2D.x, vertex2D.y, vertex2D.z);
        this.color = color;
    }

    public Pixel(int x1, int y1, int z1, Color color) {
        this.coordinate = new Vector3D(x1, y1, z1);
        this.color = color;
    }
    public Pixel(int x1, int y1, int z1) {
        this.coordinate = new Vector3D(x1, y1, z1);
        this.color = Color.white;
    }

    public static Triangle toTriangle(Pixel[] triangle) {
        return new Triangle(triangle);
    }

    public Color getColor() {
        return color;
    }
    public Vector3D getCoordinate() {
        return coordinate;
    }
    public float getX() {
        return coordinate.i;
    }
    public float getY() {
        return coordinate.j;
    }
    public float getZ() {
        return coordinate.k;
    }
    public void addX(float value) {
        coordinate.i += value;
    }
    public void addY(float value) {
        coordinate.j += value;
    }
    public void addZ(float value) {
        coordinate.k += value;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public void setCoordinate(Vector3D coordinate) {
        this.coordinate = coordinate;
    }
    public void overwriteIfCloser(Pixel pixel) {
        if (pixel.getZ() < getZ()) {
            setCoordinate(pixel.coordinate);
            setColor(pixel.color);
        }
    }
    public String toString() {
        return "Pixel: "+getCoordinate().toString()+", Color: "+getColor();
    }
    public void setVertex(Vertex v) {
        vertexIfValid = v;
        isVertex = true;
    }

    public Vertex getVertexIfValid() {
        return vertexIfValid;
    }
}
