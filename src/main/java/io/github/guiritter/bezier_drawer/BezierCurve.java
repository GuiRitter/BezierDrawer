package io.github.guiritter.bezier_drawer;

import static java.lang.Math.pow;
import static java.lang.Math.round;
import java.util.LinkedList;

/**
 * Computes a Bézier curve from a list of control points.
 * @author Guilherme Alan Ritter
 */
public final class BezierCurve {

    private double b;

    private final LinkedList<Point> BézierControlPointList;

    private final BinomialCoefficient binomialCoefficient = new BinomialCoefficient();

    private long i;

    private final Point output;

    private long n;

    private double x;

    private double y;

    /**
     * Computes a point in the curve for a given {@code 0 ≤ t ≤ 1}.
     * @param t curve parameter
     */
    public void op(double t) {
        x = 0;
        y = 0;
        n = BézierControlPointList.size() - 1;
        for (i = 0; i <= n; i++) {
            b = ((double) binomialCoefficient.op(n, i)) * pow(t, (double) i) * pow(1.0 - t, (double) (n - i));
            x += BézierControlPointList.get((int) i).x * b;
            y += BézierControlPointList.get((int) i).y * b;
        }
        output.x = (int) round(x);
        output.y = (int) round(y);
    }

    public BezierCurve(LinkedList<Point> BézierControlPointList, Point output) {
        this.BézierControlPointList = BézierControlPointList;
        this.output = output;
    }
}
