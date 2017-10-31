package io.github.guiritter.bézier_drawer;

import static java.lang.Math.pow;
import static java.lang.Math.round;
import java.util.LinkedList;

public final class BézierCurve {

    private double b;

    private final LinkedList<Point> BézierControlPointList;

    private final BinomialCoefficient binomialCoefficient = new BinomialCoefficient();

    private long i;

    private final Point output;

    private long n;

    private double x;

    private double y;

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

    public BézierCurve(LinkedList<Point> BézierControlPointList, Point output) {
        this.BézierControlPointList = BézierControlPointList;
        this.output = output;
    }
}
