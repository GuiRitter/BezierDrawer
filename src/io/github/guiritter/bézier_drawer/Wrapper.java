package io.github.guiritter.bézier_drawer;

public final class Wrapper<C> {

    public C value;

    public Wrapper() {
        value = null;
    }

    public Wrapper(C value) {
        this.value = value;
    }
}
