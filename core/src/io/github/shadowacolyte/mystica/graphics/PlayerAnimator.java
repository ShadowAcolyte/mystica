package io.github.shadowacolyte.mystica.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerAnimator {
    private final float FRAME_DURATION = 0.06f;

    private boolean walking;
    private float stateTime;

    private TextureAtlas movementAtlas;

    private TextureRegion standingEast;
    private TextureRegion standingWest;
    private TextureRegion standingNorth;
    private TextureRegion standingSouth;

    private Animation<TextureRegion> animationWalkingEast;
    private Animation<TextureRegion> animationWalkingWest;
    private Animation<TextureRegion> animationWalkingNorth;
    private Animation<TextureRegion> animationWalkingSouth;

    public enum Direction {
            NORTH, EAST, SOUTH, WEST;
    }
    private Direction direction;

    public void loadAnimations(AssetManager assetManager) {
        movementAtlas = assetManager.get("player/player_movement.atlas", TextureAtlas.class);

        // Standing animations
        standingEast = movementAtlas.findRegions("player_walking_east").get(1);
        standingWest = movementAtlas.findRegions("player_walking_west").get(1);
        standingNorth = movementAtlas.findRegions("player_walking_north").get(1);
        standingSouth = movementAtlas.findRegions("player_walking_south").get(1);

        // Walking animations
        animationWalkingEast = new Animation<TextureRegion>(FRAME_DURATION, movementAtlas.findRegions("player_walking_east"), Animation.PlayMode.LOOP);
        animationWalkingWest = new Animation<TextureRegion>(FRAME_DURATION, movementAtlas.findRegions("player_walking_west"), Animation.PlayMode.LOOP);
        animationWalkingNorth = new Animation<TextureRegion>(FRAME_DURATION, movementAtlas.findRegions("player_walking_north"), Animation.PlayMode.LOOP);
        animationWalkingSouth = new Animation<TextureRegion>(FRAME_DURATION, movementAtlas.findRegions("player_walking_south"), Animation.PlayMode.LOOP);

        walking =false;
        direction = Direction.SOUTH;
    }

    public void updateAnimator(float delta) {
        // If the player is not walking ,ie, player is standing still
        // set the state time to zero because the animation must reset
        // when the player is not walking
        stateTime = walking ? stateTime + delta : 0f;
    }

    public void setDirection(Direction newDirection) {
        if (direction != newDirection) {
            direction = newDirection;
            // Every time the player changes direction a new animation
            // is played, hence the state time must be reset to 0
            stateTime = 0f;
        }
    }

    public void setWalking(boolean walking) {
        this.walking = walking;
    }

    public TextureRegion getCurrentTexture() {
        switch (direction) {
            case EAST:
                return walking ? animationWalkingEast.getKeyFrame(stateTime) : standingEast;
            case WEST:
                return walking ? animationWalkingWest.getKeyFrame(stateTime) : standingWest;
            case NORTH:
                return walking ? animationWalkingNorth.getKeyFrame(stateTime) : standingNorth;
            case SOUTH:
                return walking ? animationWalkingSouth.getKeyFrame(stateTime) : standingSouth;
            default:
                return null;
        }
    }
}
