package Rendering;

import rMath.Vector3D;

import java.awt.*;

public class Pixel {
    private Color color;
    private Vector3D coordinate;

    public Pixel(Vector3D coordinate, Color color) {
        this.coordinate = coordinate;
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
