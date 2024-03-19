package Levels;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

import Scene.objects.Shape;
import Tools.output;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Level {
    final List<String> specialObjectFormats = Arrays.asList("GLTF", "OBJ");
    ArrayList<Shape> sceneObjects = new ArrayList<>();
    public Level(String path) {
        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(path)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONObject Scene = (JSONObject) jsonObject.get("Scene");

            JSONArray Objects = (JSONArray) Scene.get("Objects");

            for (JSONObject object : (Iterable<JSONObject>) Objects) {
                if (specialObjectFormats.contains((String) object.get("type"))) {
                    // TODO: implement handling for imported mesh formats
                    output.warnMessage("Support for object format: \""+object.get("type")+"\" not yet implemented.");
                    sceneObjects.add(Shape.of(object));

                } else {
                    sceneObjects.add(Shape.of(object));
                }

                sceneObjects.add(Shape.of(object));
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
