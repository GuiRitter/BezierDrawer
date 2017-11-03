package io.github.guiritter.bézier_drawer;

public final class Event {

    public enum Type {

        BACKGROUND_COLOR,
        CLICKED,
        DRAGGED,
        PRESSED,
        RELEASED
    }

    public final int color[] = new int[4];

    public final int i;

    public final Type type;

    public final int x;

    public final int y;

    public Event(Type type, int color[]) {
        this.type = type;
        System.arraycopy(color, 0, this.color, 0, color.length);
        this.i = -1;
        this.x = 0;
        this.y = 0;
    }

    public Event(Type type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.i = -1;
    }

    public Event(Type type, int x, int y, int i) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.i = i;
    }
}
