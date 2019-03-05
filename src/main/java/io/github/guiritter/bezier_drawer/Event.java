package io.github.guiritter.bezier_drawer;

/**
 * User interactions that require an action.
 * @author Guilherme Alan Ritter
 */
public final class Event {

    public enum Type {

        /**
         * Paint the background with a solic color.
         */
        BACKGROUND_COLOR,

        /**
         * Insert a point.
         */
        CLICKED,

        /**
         * Moving a point.
         */
        DRAGGED,

        /**
         * Started moving a point.
         */
        PRESSED,

        /**
         * Right clicked a point.
         */
        PRESSED_WAIT,

        /**
         * Stopped moving a point.
         */
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
