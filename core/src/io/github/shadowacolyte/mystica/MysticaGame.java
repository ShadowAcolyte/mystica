package io.github.shadowacolyte.mystica;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MysticaGame extends Game {
	private SpriteBatch batch;
	private World world;

	@Override
	public void create () {
		batch = new SpriteBatch();
		world = new World(batch);
		setScreen(world);
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		world.dispose();
		batch.dispose();
	}
}
