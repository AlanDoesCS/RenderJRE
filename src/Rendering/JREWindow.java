package Rendering;

import Levels.Level;
import Scene.objects.Shape;
import Scene.objects.dependencies.Triangle;
import rMath.Vertex2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class JREWindow extends JPanel implements ActionListener {
    private int WIDTH, HEIGHT;
    private Timer timer;
    private Renderer renderer;
    public JREWindow(int FPS, int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        int delay = 1000/FPS;
        this.renderer = new Renderer(1000, 75,  WIDTH, HEIGHT);

        this.timer = new Timer(delay, this);
        this.timer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        renderer.renderScene();
        Pixel[][] rendererImage = renderer.getDepthBuffer().toArray();

        for (Pixel[] row : rendererImage) {
            for (Pixel pixel : row) {
                if (pixel != null) {
                    g2.setColor(pixel.getColor());
                    g2.drawRect((int) pixel.getX(), HEIGHT-(int) pixel.getY(), 1, 1);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public void passArguments(ArrayList<String> arguments) {
        renderer.setArguments(arguments);
    }
    public void passArguments(String arguments) {
        renderer.setArguments(arguments);
    }

    public int getWIDTH() {
        return WIDTH;
    }
    public int getHEIGHT() {
        return HEIGHT;
    }
    public void addLevel(Level level) {
        renderer.addLevel(level);
    }
    public void loadLevel(int i) {
        renderer.loadLevel(i);
    }
}
