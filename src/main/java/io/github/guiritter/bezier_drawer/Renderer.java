package io.github.guiritter.bezier_drawer;

import io.github.guiritter.image_component.ImageComponent;
import java.awt.image.WritableRaster;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.LinkedList;
import java.util.TimerTask;

/**
 * Thread that runs at a fixed interval to display the curve, its control points
 * and a background, which can be a solid color or an image.
 * @author Guilherme Alan Ritter
 */
public final class Renderer extends TimerTask {

    /**
     * Local copy of the control points.
     */
    private final LinkedList<Point> BézierControlPointList = new LinkedList<>();

    private final BezierCurve curve;

    /**
     * Curve display area height.
     */
    private int height;

    /**
     * Curve display area component.
     */
    private final ImageComponent imageComponent;

    /**
     * Used to get the curve's computed points from one place
     * and its color from another.
     */
    private final Point point = new Point(0, 0, new int[4], 0);

    /**
     * Curve display area raster.
     */
    private final WritableRaster raster;

    /**
     * Time step used to compute the curve. Inverse to the amount of points
     * in the curve.
     */
    private final Wrapper<Double> step = new Wrapper<>();

    /**
     * Time that is stepped from 0 to 1.
     */
    private double t;

    /**
     * Used to clear the curve display area.
     */
    private final int transparency[] = new int[]{0, 0, 0, 0};

    /**
     * Curve display area width.
     */
    private int width;

    private int x;

    private int xHigh;

    private int xLow;

    private int y;

    private int yHigh;

    private int yLow;

    /**
     * Renders a control point as a square centered on the point's coordinates.
     * Avoids image boundaries.
     * @param point
     */
    private void renderPoint(Point point) {
        xLow = max(((int) point.x) - point.radius + 1, 0);
        xHigh = min(((int) point.x) + point.radius - 1, width - 1);
        yLow = max(((int) point.y) - point.radius + 1, 0);
        yHigh = min(((int) point.y) + point.radius - 1, height - 1);
        for (y = yLow; y <= yHigh; y++) {
            for (x = xLow; x <= xHigh; x++) {
                raster.setPixel(x, y, point.color);
            }
        }
    }

    /**
     * In the following order: clears the curve display area, renders the curve,
     * and renders the control points.
     */
    @Override
    public void run() {
        BezierDrawer.getRenderData(BézierControlPointList, step, point);
        width = raster.getWidth();
        height = raster.getHeight();
        for (y = 0; y < height; y++) {
            for (x = 0; x < width; x++) {
                raster.setPixel(x, y, transparency);
            }
        }
        for (t = 0; t <= 1; t += step.value) {
            curve.op(t);
            raster.setPixel(point.x, point.y, point.color);
        }
        // don't use functional operation
        // test results show an order of magnitude slower
        for (Point pointI : BézierControlPointList) {
            renderPoint(pointI);
        }
        imageComponent.revalidate();
        imageComponent.repaint();
    }

    public Renderer(WritableRaster raster, ImageComponent imageComponent) {
        this.raster = raster;
        this.imageComponent = imageComponent;
        curve = new BezierCurve(BézierControlPointList, point);
    }
}
