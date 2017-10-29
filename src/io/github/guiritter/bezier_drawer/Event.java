package io.github.guiritter.bezier_drawer;

public final class Event {

    public enum Type {

        CLICKED,
        DRAGGED,
        PRESSED,
        RELEASED
    }

    public final Type type;

    public final int x;

    public final int y;

    public Event(Type type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }
}
