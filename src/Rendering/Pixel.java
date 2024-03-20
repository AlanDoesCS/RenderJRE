package Rendering;

import rMath.Vector3D;
import rMath.Vertex2D;

import java.awt.*;

public class Pixel {
    private Color color;
    private Vector3D coordinate;

    public Pixel(Vector3D coordinate, Color color) {
        this.coordinate = coordinate;
        this.color = color;
    }

    public Pixel(Vertex2D vertex2D, Color color) {
        this.coordinate = new Vector3D(vertex2D.x, vertex2D.y, vertex2D.z);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
    public Vector3D getCoordinate() {
        return coordinate;
    }
    public double getX() {
        return coordinate.i;
    }
    public double getY() {
        return coordinate.j;
    }
    public double getZ() {
        return coordinate.k;
    }
    public void addX(double value) {
        coordinate.i += value;
    }
    public void addY(double value) {
        coordinate.j += value;
    }
    public void addZ(double value) {
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
}
