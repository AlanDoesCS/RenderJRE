package Rendering;

import Scene.objects.Shape;
import Scene.objects.dependencies.Triangle;
import rMath.Vertex2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JREWindow extends JPanel implements ActionListener {
    private int WIDTH, HEIGHT;
    private Timer timer;
    public JREWindow(int FPS, Renderer renderer) {
        int delay = 1000/FPS;
        timer = new Timer(delay, this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        // Perform most efficient sorting algorithm based on input size
        Scene.objects.Shape[] orderedObjs = order_shapes(unsortedObjs, camera);

        for (int shape_index = orderedObjs.length - 1; shape_index >= 0; shape_index--) {
            Shape shape = orderedObjs[shape_index];

            // Perform most efficient sorting algorithm based on input size
            Triangle[] orderedTriangles = order_triangles(shape.getTriangles(), shape, camera);

            Vertex2D[][] Triangle_Points = render_triangles(orderedTriangles, shape);

            // draw shapes

            // cell shader outline
            if (cel) {
                g2.setColor(shape.getColour().darker());
                g2.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                for (int tri_index = orderedTriangles.length - 1; tri_index >= 0; tri_index--) {
                    Vertex2D[] triangle = Triangle_Points[tri_index];
                    int[] xpoints = {(int) triangle[0].x, (int) triangle[1].x, (int) triangle[2].x};
                    int[] ypoints = {(int) (WindowResY - triangle[0].y), (int) (WindowResY - triangle[1].y), (int) (WindowResY - triangle[2].y)};

                    Polygon p = new Polygon(xpoints, ypoints, 3);  // This polygon represents a triangle with the above

                    g2.setColor(shape.getColour().darker());
                    g2.drawPolygon(p);
                }
            }

            for (int tri_index = orderedTriangles.length - 1; tri_index >= 0; tri_index--) {
                g2.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                if (diffuse) {
                    Color lit = diffuseBasic(shape.getColour(), light, orderedTriangles[tri_index]);
                    g2.setColor(lit);
                } else if (fill) { // diffuse takes priority over fill
                    g2.setColor(shape.getColour());
                }

                Vertex2D[] triangle = Triangle_Points[tri_index];
                int[] xpoints = {(int) triangle[0].x, (int) triangle[1].x, (int) triangle[2].x};
                int[] ypoints = {(int) (WindowResY - triangle[0].y), (int) (WindowResY - triangle[1].y), (int) (WindowResY - triangle[2].y)};

                Polygon p = new Polygon(xpoints, ypoints, 3);  // This polygon represents a triangle with the above

                // fill shape vertices.
                if (fill || diffuse) {
                    g2.fillPolygon(p);
                }

                // draw wireframe
                if (wireframe) {
                    g2.setColor(shape.getColour().darker());
                    g2.drawPolygon(p);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
