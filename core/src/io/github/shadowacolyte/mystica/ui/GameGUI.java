package io.github.shadowacolyte.mystica.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class GameGUI {
    public Stage stage;
    Table table;

    Touchpad touchpad;
    Touchpad.TouchpadStyle touchpadStyle;

    public GameGUI() {
        stage = new Stage();
        table = new Table();

        table.setFillParent(true);
        stage.addActor(table);

        touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.knob = new TextureRegionDrawable(new Texture("gui/touchpad_knob.png"));
        touchpadStyle.background = new TextureRegionDrawable(new Texture("gui/touchpad_background.png"));
        touchpad = new Touchpad(10, touchpadStyle);
        touchpad.setWidth(Gdx.graphics.getWidth() / 6.0f);
        touchpad.setHeight(Gdx.graphics.getWidth() / 6.0f);

        table.addActor(touchpad);
    }

    public Touchpad getTouchpad() {
        return touchpad;
    }

    public void update(float delta) {
        Gdx.input.setInputProcessor(stage);
        stage.act(delta);
    }

    public void render() {
        stage.draw();
    }
}
