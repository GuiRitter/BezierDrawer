package io.github.guiritter.bezier_drawer;

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

    public Point(int x, int y, int color[], int radius) {
        this.x = x;
        this.y = y;
        System.arraycopy(color, 0, this.color, 0, color.length);
        this.radius = radius;
    }
}
