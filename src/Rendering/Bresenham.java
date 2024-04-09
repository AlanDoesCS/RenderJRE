package Rendering;

import java.util.ArrayList;
import java.util.List;

// NOTICE:
// This code is an adapted version of the contributions by ishankhandelwals and Anant Agarwal to GeeksForGeeks.org
public class Bresenham {
    static List<List<Integer>> bresenham2D(Pixel a, Pixel b) {
        int x1= (int) a.getX(), y1= (int) a.getY(), x2= (int) b.getX(), y2= (int) b.getY();
        List<List<Integer>> ListOfPoints = new ArrayList<>();
        ListOfPoints.add(List.of(x1, y1));

        int m_new = 2 * (y2 - y1);
        int slope_error_new = m_new - (x2 - x1);

        for (int x = x1, y = y1; x <= x2; x++) {
            ListOfPoints.add(List.of(x, y));

            // Add slope to increment angle formed
            slope_error_new += m_new;

            // Slope error reached limit, time to
            // increment y and update slope error.
            if (slope_error_new >= 0) {
                y++;
                slope_error_new -= 2 * (x2 - x1);
            }
        }

        return ListOfPoints;
    }
    public static List<List<Integer>> bresenham3D(Pixel a, Pixel b) {
        int x1= (int) a.getX(), y1= (int) a.getY(), z1= (int) a.getZ(), x2= (int) b.getX(), y2= (int) b.getY(), z2= (int) b.getZ();

        List<List<Integer>> ListOfPoints = new ArrayList<>();
        ListOfPoints.add(List.of(x1, y1, z1));
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int dz = Math.abs(z2 - z1);
        int xs;
        int ys;
        int zs;
        if (x2 > x1) {
            xs = 1;
        } else {
            xs = -1;
        }
        if (y2 > y1) {
            ys = 1;
        } else {
            ys = -1;
        }
        if (z2 > z1) {
            zs = 1;
        } else {
            zs = -1;
        }

        // Driving axis is X-axis
        if (dx >= dy && dx >= dz) {
            int p1 = 2 * dy - dx;
            int p2 = 2 * dz - dx;
            while (x1 != x2) {
                x1 += xs;
                if (p1 >= 0) {
                    y1 += ys;
                    p1 -= 2 * dx;
                }
                if (p2 >= 0) {
                    z1 += zs;
                    p2 -= 2 * dx;
                }
                p1 += 2 * dy;
                p2 += 2 * dz;
                ListOfPoints.add(List.of(x1, y1, z1));
            }

            // Driving axis is Y-axis"
        } else if (dy >= dx && dy >= dz) {
            int p1 = 2 * dx - dy;
            int p2 = 2 * dz - dy;
            while (y1 != y2) {
                y1 += ys;
                if (p1 >= 0) {
                    x1 += xs;
                    p1 -= 2 * dy;
                }
                if (p2 >= 0) {
                    z1 += zs;
                    p2 -= 2 * dy;
                }
                p1 += 2 * dx;
                p2 += 2 * dz;
                ListOfPoints.add(List.of(x1, y1, z1));
            }

            // Driving axis is Z-axis"
        } else {
            int p1 = 2 * dy - dz;
            int p2 = 2 * dx - dz;
            while (z1 != z2) {
                z1 += zs;
                if (p1 >= 0) {
                    y1 += ys;
                    p1 -= 2 * dz;
                }
                if (p2 >= 0) {
                    x1 += xs;
                    p2 -= 2 * dz;
                }
                p1 += 2 * dy;
                p2 += 2 * dx;
                ListOfPoints.add(List.of(x1, y1, z1));
            }
        }
        return ListOfPoints;
    }
}
