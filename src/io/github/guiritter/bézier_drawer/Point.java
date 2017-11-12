package io.github.guiritter.bézier_drawer;

import java.util.Arrays;

/**
 * Represents a control point composed of screen coordinates, color and radius.
 * @author Guilherme Alan Ritter
 */
public final class Point {

    final int color[] = new int[4];

    /**
     * It's represented as a square, with width
     * equal to twice the radius minus one.
     */
    int radius;

    int x;

    int y;

    /**
     * @param x
     * @param y
     * @return whether the received coordinate pair is inside the bounds of this control point's representation
     */
    public boolean isIn(int x, int y) {
        return (x >= (this.x - radius + 1))
         && (x <= (this.x + radius - 1))
         && (y >= (this.y - radius + 1))
         && (y <= (this.y + radius - 1));
    }

    @Override
    public String toString() {
        return String.format("x: %d\ty: %d\tcolor: %s\tradius: %d", x, y, Arrays.toString(color), radius);
    }

    public Point(int x, int y, int color[], int radius) {
        this.x = x;
        this.y = y;
        System.arraycopy(color, 0, this.color, 0, color.length);
        this.radius = radius;
    }
}
