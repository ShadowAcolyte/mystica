package io.github.shadowacolyte.mystica;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;

import io.github.shadowacolyte.mystica.levels.Level1;
import io.github.shadowacolyte.mystica.levels.LevelBase;
import io.github.shadowacolyte.mystica.ui.GameGUI;

public class World implements Screen {
    static final int CAMERA_WIDTH = 32;
    static final int CAMERA_HEIGHT = 32;
    static final float WORLD_SCALE = 1/32f ;

    LevelBase currentLevel;
    AssetManager assetManager;
    SpriteBatch batch;
    OrthographicCamera cam;
    OrthogonalTiledMapRenderer mapRenderer;

    GameGUI gameGUI;
    ShapeRenderer renderer;

    private Player player;

    public World(SpriteBatch batch) {
            this.batch = batch;
    }

    @Override
    public void show() {
        assetManager = new AssetManager();
        loadAssets();

        renderer = new ShapeRenderer();

        // Create camera with proper aspect ratio
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        cam = new OrthographicCamera(CAMERA_WIDTH,CAMERA_HEIGHT * h / w);
        // Centralize the camera
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();

        // Initialize player
        player = new Player(assetManager);

        // Initialize GUI
        gameGUI = new GameGUI();

        // Load level and map renderer
        currentLevel = new Level1(assetManager);
        mapRenderer = new OrthogonalTiledMapRenderer(currentLevel.getMap(), WORLD_SCALE);
    }

    private void loadAssets() {
        // Load maps
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assetManager.load("maps/map.tmx", TiledMap.class);

        // Load player animations
        assetManager.load("player/player_movement.atlas", TextureAtlas.class);
        assetManager.load("player/player_movement_new.atlas", TextureAtlas.class);

        assetManager.finishLoading();
    }

    void update(float delta) {
        gameGUI.update(delta);

        player.update(delta, currentLevel, gameGUI);
        cam.position.set(player.position, 0);

        // Prevent camera from going out of map bounds
        cam.position.x = MathUtils.clamp(cam.position.x, cam.viewportWidth / 2f, currentLevel.getMapWidth() - cam.viewportWidth / 2f);
        cam.position.y = MathUtils.clamp(cam.position.y, cam.viewportHeight / 2f, currentLevel.getMapHeight() - cam.viewportHeight / 2f);
        cam.update();
    }

    @Override
    public void render(float delta) {
        // Game logic and stuff (everything except rendering)
        update(delta);

        // Clear screen
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Set batch position and projection to camera's
        batch.setProjectionMatrix(cam.combined);

        // Render map
        mapRenderer.setView(cam);
        mapRenderer.render();

        // Render stuff
        player.render(batch);
        gameGUI.render();

        renderer.setProjectionMatrix(cam.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.rect(player.hitbox.x, player.hitbox.y, player.hitbox.width, player.hitbox.height);
        renderer.end();
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // Clear assets from memory
        assetManager.dispose();
    }
}
