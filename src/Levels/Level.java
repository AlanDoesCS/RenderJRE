package Levels;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

import Scene.lighting.Light;
import Scene.objects.Shape;
import Tools.output;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Level {
    private String name;
    final List<String> specialObjectFormats = Arrays.asList("GLTF", "OBJ");
    ArrayList<Shape> sceneObjects = new ArrayList<>();
    ArrayList<Light> sceneLights = new ArrayList<>();
    public Level(String path, String name) {
        this.name = name;
        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(path)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONObject Scene = (JSONObject) jsonObject.get("Scene");

            // World objects
            JSONArray Objects = (JSONArray) Scene.get("Objects");

            for (JSONObject object : (Iterable<JSONObject>) Objects) {
                if (specialObjectFormats.contains((String) object.get("type"))) {
                    // TODO: implement handling for imported mesh formats
                    output.warnMessage("Support for object format: \""+object.get("type")+"\" not yet implemented.");
                }

                sceneObjects.add(Shape.of(object));
            }

            // Lighting
            JSONArray Lighting = (JSONArray) Scene.get("Lighting");

            for (JSONObject light : (Iterable<JSONObject>) Lighting) {
                sceneLights.add(Light.of(light));
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    // Accessors and Mutators
    public ArrayList<Shape> getSceneObjects() {
        return sceneObjects;
    }

    public void setSceneObjects(ArrayList<Shape> sceneObjects) {
        this.sceneObjects = sceneObjects;
    }
    public void addSceneObject(Shape shape) {
        sceneObjects.add(shape);
    }

    public ArrayList<Light> getSceneLights() {
        return sceneLights;
    }

    public void setSceneLights(ArrayList<Light> sceneLights) {
        this.sceneLights = sceneLights;
    }
    public void addSceneLight(Light light) {
        sceneLights.add(light);
    }

    public String getName() {
        return this.name;
    }
    public void setName(String newName) {
        this.name = newName;
    }
}
