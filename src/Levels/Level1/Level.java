package Levels.Level1;

import Scene.objects.Shape;

import java.awt.*;

public class Level extends Levels.Level {
    public Level() {
        super("src/Levels/Level1/scene.json", "Level 1");

        Shape Ico2 = getSceneObjectByID("Icosahedron 2");
        Ico2.updateColour(new Color(52, 62, 64));
        System.out.println(Ico2);
    }
}
