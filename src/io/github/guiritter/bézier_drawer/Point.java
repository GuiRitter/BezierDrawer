package io.github.guiritter.bézier_drawer;

import java.util.Arrays;

public final class Point {

    final int color[] = new int[4];

    int radius;

    int x;

    int y;

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
