package io.github.shadowacolyte.mystica.levels;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class Level1 extends LevelBase {
    private static final String MAP_FILE_PATH = "maps/map.tmx";

    private static final int MAP_WIDTH = 100;
    private static final int MAP_HEIGHT = 100;

    public Level1(AssetManager assetManager) {
        super((TiledMap) assetManager.get(MAP_FILE_PATH), MAP_WIDTH, MAP_HEIGHT);
    }
}
