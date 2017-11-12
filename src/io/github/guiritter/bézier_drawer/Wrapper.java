package io.github.guiritter.bézier_drawer;

/**
 * Wraps an object so it can be passed by reference between objects and,
 * when the reference to the wrapped object is changed, all objects
 * having the wrapper can see the same reference, even if it's null.
 * @author Guilherme Alan Ritter
 */
public class Wrapper<C> {

    public C value;

    public Wrapper() {
        value = null;
    }

    public Wrapper(C value) {
        this.value = value;
    }
}
