package io.github.guiritter.bézier_drawer;

/**
 * A linear equation constructed from two points.
 * Doesn't work for points in a vertical line.
 * @author Guilherme Alan Ritter
 */
public final class FitLinear {

    /**
     * y = a * x + b
     */
    public final double a;

    /**
     * y = a * x + b
     */
    public final double b;

    public double f(double x) {
        return (a * x) + b;
    }

    public FitLinear(double x1, double y1, double x2, double y2) {
        a = (y2 - y1) / (x2 - x1);
        b = y1 - (a * x1);
    }
}