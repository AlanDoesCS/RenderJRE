package Levels;

import Scene.lighting.Light;
import Scene.objects.Shape;

import java.util.ArrayList;
import java.util.HashMap;

public class LevelHandler {
    private int currentLevelIndex = 0;
    ArrayList<Level> levels = new ArrayList<>();
    HashMap<String, Integer> levelIndicesByName = new HashMap<>();
    HashMap<Class<? extends Level>, Integer> levelIndicesByClass = new HashMap<>();

    public void addLevel(Level level) {
        levels.add(level);
        int lastIndex = levels.size()-1;
        levelIndicesByName.put(level.getName(), lastIndex);
        levelIndicesByClass.put(level.getClass(), lastIndex);
    }
    public ArrayList<Shape> getLevelObjects() {
        return levels.get(currentLevelIndex).getSceneObjects();
    }
    public ArrayList<Light> getLevelLights() { return levels.get(currentLevelIndex).getSceneLights(); }


    // Accessors and mutators ------------------------------------------------------------------------------------------
    public void setCurrent(int index) {
        currentLevelIndex = index;
    }
    public void goToNext() {
        currentLevelIndex++;

        if (currentLevelIndex >+ levels.size()) {
            currentLevelIndex = 0;
        }
    }
    public void setCurrent(String name) {
        currentLevelIndex = levelIndicesByName.get(name);
    }
    public void setCurrent(Class<? extends Level> parentClass) {
        currentLevelIndex = levelIndicesByClass.get(parentClass);
    }

    public Level getCurrent() {
        return levels.get(currentLevelIndex);
    }
}
