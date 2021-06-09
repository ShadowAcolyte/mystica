package io.github.shadowacolyte.mystica;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import io.github.shadowacolyte.mystica.graphics.PlayerAnimator;
import io.github.shadowacolyte.mystica.levels.LevelBase;
import io.github.shadowacolyte.mystica.math.PlayerPolygonCollider;
import io.github.shadowacolyte.mystica.math.Geometry2DUtils;
import io.github.shadowacolyte.mystica.ui.GameGUI;

import static io.github.shadowacolyte.mystica.World.WORLD_SCALE;

public class Player {
    // Player position
    Vector2 position;
    Vector2 velocity;
    Rectangle hitbox;
    // Default walking speed
    int walkSpeed;

    PlayerAnimator playerAnimator;

    boolean walking = false;

    Player(AssetManager assetManager) {
        walkSpeed = 10;
        position = new Vector2();
        velocity = new Vector2();
        hitbox = new Rectangle(position.x, position.y, 3, 3);
        playerAnimator = new PlayerAnimator();
        playerAnimator.loadAnimations(assetManager);
    }

    void update(float delta, LevelBase level, GameGUI gameGUI) {
        playerAnimator.updateAnimator(delta);
        handleInput(delta, level, gameGUI.getTouchpad());
    }

    void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(playerAnimator.getCurrentTexture(), position.x, position.y, 3f, 3f);
        batch.end();
    }

    void handleInput(float delta, LevelBase level, Touchpad touchpad) {
        //Joystick movement
        float knobX = touchpad.getKnobPercentX();
        float knobY = touchpad.getKnobPercentY();

        if (knobX != 0 && (Math.abs(knobX) > 0 || Math.abs(knobY) > 0)) {
            //Find the angle the joystick is making with the x-axis
            float displacementAngle = (float) Math.atan2(knobY, knobX);

            //Make velocity dependent on how much the joystick is pulled
            float magnitude = (float) Math.sqrt(knobX * knobX + knobY * knobY);
            magnitude = MathUtils.clamp(magnitude, 0.5f, 1.0f);

            //The walk speed is constant, ie, the magnitude of the displacement vector
            //Hence, x component = magnitude * cos(theta)
            //and, y component = magnitude * sin(theta)
            float displacementX = magnitude * walkSpeed * (float) Math.cos(displacementAngle);
            float displacementY = magnitude * walkSpeed * (float) Math.sin(displacementAngle);
            velocity.set(displacementX, displacementY).scl(delta);

            displacementAngle = (float) Math.toDegrees(displacementAngle);
            if (displacementAngle > -60 && displacementAngle <= 60) {
                playerAnimator.setDirection(PlayerAnimator.Direction.EAST);
                playerAnimator.setWalking(true);
            }
            else if (displacementAngle > 60 && displacementAngle <= 120) {
                playerAnimator.setDirection(PlayerAnimator.Direction.NORTH);
                playerAnimator.setWalking(true);
            }
            else if (displacementAngle > 120 || displacementAngle <= -120) {
                playerAnimator.setDirection(PlayerAnimator.Direction.WEST);
                playerAnimator.setWalking(true);
            }
            else if (displacementAngle <= -60 && displacementAngle >= -120) {
                playerAnimator.setDirection(PlayerAnimator.Direction.SOUTH);
                playerAnimator.setWalking(true);
            }

            detectCollisions(level);
            position.add(velocity);
        }
        else {
            playerAnimator.setWalking(false);
        }

        // Prevent player going out of map
        position.x = MathUtils.clamp(position.x, 0, level.getMapWidth() - 32 / 32f);
        position.y = MathUtils.clamp(position.y, 0, level.getMapHeight() - 32 / 32f);

        hitbox.setPosition(position);
    }

    boolean detectCollisions(LevelBase level) {
        MapLayer layer = level.getMap().getLayers().get("collision_layer");
        MapObjects objects = layer.getObjects();
        for (RectangleMapObject r : objects.getByType(RectangleMapObject.class)) {
            Rectangle rect = new Rectangle();
            rect.x = r.getRectangle().x * WORLD_SCALE;
            rect.y = r.getRectangle().y  * WORLD_SCALE;
            rect.width = r.getRectangle().width * WORLD_SCALE;
            rect.height = r.getRectangle().height * WORLD_SCALE;

            Polygon polygon = Geometry2DUtils.RectangleToPolygon(r.getRectangle());
            polygon.scale(WORLD_SCALE);
            if (Intersector.overlaps(rect, hitbox)) {
                PlayerPolygonCollider.collide(this, polygon);
                return true;
            }
        }
        return false;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }
}
