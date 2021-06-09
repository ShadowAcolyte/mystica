package io.github.shadowacolyte.mystica.math;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class Geometry2DUtils {
    /**
     * Converts rectangle object to polygon object
     */
    public static Polygon RectangleToPolygon(Rectangle rectangle) {
        float[] vertices = {
                rectangle.x, rectangle.y,
                rectangle.width, rectangle.y,
                rectangle.width, rectangle.height,
                rectangle.x, rectangle.height
        };

        return new Polygon(vertices);
    }

    /**
     * Returns closest vertices to a point
     *
     * @param vertices list of vertices
     * @param number number of closest vertices to return
     */
    public static float[] ClosestVerticesToPoint(float[] point, float[] vertices, int number)throws ArithmeticException {
        if (vertices.length % 2 != 0) {
            throw new ArithmeticException("Number of elements in vertices array should be even.");
        }

        float[] vertices_copy = vertices.clone();
        float[] closest_vertices = new float[number * 2];

        for (int i = 0; i < vertices_copy.length - 2; i += 2) {
            int pos = i;
            for (int j = i + 2; j < vertices_copy.length; j += 2) {
                float[] vertex = {vertices_copy[j], vertices_copy[j + 1]};
                float[] closest_vertex = {vertices_copy[pos], vertices_copy[pos + 1]};
                if (DistanceToPoint(point, vertex) < DistanceToPoint(point, closest_vertex))
                    pos = j;
            }
            float[] temp = {vertices_copy[pos], vertices_copy[pos + 1]};
            vertices_copy[pos] = vertices_copy[i];
            vertices_copy[pos + 1] = vertices_copy[i + 1];
            vertices_copy[i] = temp[0];
            vertices_copy[i + 1] = temp[1];
        }

        for (int i = 0; i < closest_vertices.length; i += 2) {
            closest_vertices[i] = vertices_copy[i];
            closest_vertices[i + 1] = vertices_copy[i + 1];
        }

        return closest_vertices;
    }

    public static float DistanceToPoint(float[] point1, float[] point2) {
        float x1 = point1[0];
        float x2 = point2[0];
        float y1 = point1[1];
        float y2 = point2[1];

        // Distance formula
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
}
