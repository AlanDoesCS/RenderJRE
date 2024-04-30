package Levels;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.List;

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
    HashMap<String, Shape> sceneObjectsByID = new HashMap<>();
    ArrayList<Light> sceneLights = new ArrayList<>();
    HashMap<String, Light> sceneLightsByID = new HashMap<>();

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

                sceneObjects.add(Shape.of(object, this));
                Shape s = Shape.of(object, this);
                sceneObjects.add(s);
                sceneObjectsByID.put(s.getId(), s);
            }

            // Lighting
            JSONArray Lighting = (JSONArray) Scene.get("Lighting");

            for (JSONObject light : (Iterable<JSONObject>) Lighting) {
                Light l = Light.of(light, this);
                sceneLights.add(l);
                sceneLightsByID.put(l.getId(), l);
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
    public Shape getSceneObjectByID(String s) {
        return sceneObjectsByID.getOrDefault(s, null);
    }
    public Light getSceneLightByID(String s) {
        return sceneLightsByID.getOrDefault(s, null);
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

    public void updateShape(Shape shape) {
        int index = sceneObjects.indexOf(getSceneObjectByID(shape.getId()));
        if (index != -1) {
            sceneObjects.set(index, shape);
            sceneObjectsByID.put(shape.getId(), shape);
        } else { //Object ID not recognised
            Tools.output.warnMessage(shape.getId() + ", WAS NOT FOUND!!");
            sceneObjects.add(shape); // add object to list of objects
            sceneObjectsByID.put(shape.getId(), shape);
        }
    }
}
