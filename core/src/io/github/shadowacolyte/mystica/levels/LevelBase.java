package io.github.shadowacolyte.mystica.levels;

import com.badlogic.gdx.maps.tiled.TiledMap;

public class LevelBase {
    // Tiled map for the level
    private TiledMap map;
    private int mapWidth;
    private int mapHeight;

    public LevelBase(TiledMap map, int width, int height) {
        this.map = map;
        mapWidth = width;
        mapHeight = height;
    }

    public TiledMap getMap() {
        return map;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }
}
