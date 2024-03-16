package Levels;

import java.io.File;

public class Level {
    File source;
    public Level(String path) {
        source = new File(path);
    }
}
