package io.github.guiritter.bezier_drawer;

import java.awt.image.WritableRaster;

public final class Event {

    public enum Type {

        BACKGROUND_COLOR,
        CLICKED,
        DRAGGED,
        PRESSED,
        RELEASED
    }

    public final int color[] = new int[4];

    public final WritableRaster raster;

    public final Type type;

    public final int x;

    public final int y;

    public Event(Type type, WritableRaster raster, int color[]) {
        this.type = type;
        this.raster = raster;
        System.arraycopy(color, 0, this.color, 0, color.length);
        this.x = 0;
        this.y = 0;
    }

    public Event(Type type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        raster = null;
    }
}
