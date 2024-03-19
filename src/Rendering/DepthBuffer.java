package Rendering;

import rMath.Vector3D;

import java.awt.Color;
import Rendering.*;

public class DepthBuffer {
    private double extent = 1e5;
    private Pixel[][] buffer;

    public DepthBuffer(int sizeX, int sizeY) {
        buffer = new Pixel[sizeY][sizeX];

        for (int i=0; i<sizeY; i++) {
            for (int j=0; j<sizeX; j++) {
                // fill buffer with pixels in max extent box
                buffer[i][j] = new Pixel(new Vector3D(extent+1, extent+1, extent+1), Color.black);
            }
        }
    }

    public void add(Pixel p) {
        buffer[(int) p.getY()][(int) p.getX()].overwriteIfCloser(p);
    }

    public Pixel[][] toArray() {
        return buffer;
    }
}
