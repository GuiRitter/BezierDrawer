package io.github.guiritter.bézier_drawer;

public final class WrapperPoint {

    public int index;

    public Point value;

    public WrapperPoint() {
        this.value =  null;
    }

    public WrapperPoint(Point value) {
        this.value = value;
    }
}
