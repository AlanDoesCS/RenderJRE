package Rendering;

import rMath.Vector3D;
import java.awt.Color;

public class DepthBuffer {
    private int width, height;
    private final float extent = 1e5F;
    private Pixel[][] buffer;

    public DepthBuffer(int sizeX, int sizeY) {
        buffer = new Pixel[sizeY][sizeX];
        width = sizeX;
        height = sizeY;

        for (int i=0; i<sizeY; i++) {
            for (int j=0; j<sizeX; j++) {
                // fill buffer with pixels in max extent box
                buffer[i][j] = new Pixel(new Vector3D(extent+1, extent+1, extent+1), Color.black);
            }
        }
    }

    public void add(Pixel p) {
        int X = (int) p.getX(), Y = (int) p.getY();
        if (0<=X && X<width && 0<=Y && Y<height) {
            if (buffer[(int) p.getY()][(int) p.getX()] != null) {
                buffer[(int) p.getY()][(int) p.getX()].overwriteIfCloser(p);
            } else { // hasn't been filled
                buffer[(int) p.getY()][(int) p.getX()] = p;
            }
        }
    }

    public Pixel[][] toArray() {
        return buffer;
    }
}
