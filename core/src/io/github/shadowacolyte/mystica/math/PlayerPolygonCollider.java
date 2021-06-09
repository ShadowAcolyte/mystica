package io.github.shadowacolyte.mystica.math;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import io.github.shadowacolyte.mystica.Player;

public class PlayerPolygonCollider {
    public static void collide(Player player, Polygon polygon) {
        float[] vertices = polygon.getVertices();
        float[] position = {player.getPosition().x, player.getPosition().y};
        Vector2 velocity = player.getVelocity();

        float[] closest_vertices = Geometry2DUtils.ClosestVerticesToPoint(position, vertices, 2);
        Vector2 p1 = new Vector2(closest_vertices[0], closest_vertices[1]);
        Vector2 p2 = new Vector2(closest_vertices[2], closest_vertices[3]);
        Vector2 line = p1.cpy().sub(p2);

        float magnitude = velocity.dot(line) / line.len();
        float angle = -line.angleDeg(velocity);
        System.out.println(angle);

        velocity.setAngleDeg(angle);
        velocity.setLength(magnitude);
    }
}
