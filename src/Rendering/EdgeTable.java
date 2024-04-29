package Rendering;

import java.util.HashMap;

public class EdgeTable {
    HashMap<Integer, Pixel> table = new HashMap<>();
    public void put(int key, Pixel pixel) {
        table.put(key, pixel);
    }
    public int[] min_max_Keys() {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int key : table.keySet()) {
            if (key < min) min = key;
            if (key > max) max = key;
        }

        return new int[]{min, max};
    }

    public Pixel getOrDefaultX(int y, int defaultX) {
        return table.getOrDefault(y, new Pixel(defaultX, 0, 0));
    }

    public boolean containsKey(int y) {
        return table.containsKey(y);
    }

    public Pixel get(int y) {
        return table.get(y);
    }
}
