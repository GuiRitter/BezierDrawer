package io.github.guiritter.bézier_drawer;

import io.github.guiritter.imagecomponent.ImageComponent;
import java.awt.image.WritableRaster;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.LinkedList;
import java.util.TimerTask;

public final class Renderer extends  TimerTask{

    private final LinkedList<Point> BézierControlPointList = new LinkedList();

    private final BézierCurve curve;

    private final int height;

    private final ImageComponent imageComponent;

    private final Point point = new Point(0, 0, new int[4], 0);

    private final WritableRaster raster;

    private final WrapperDouble step = new WrapperDouble(0.1);

    private double t;

    private final int transparency[] = new int[]{0, 0, 0, 0};

    private final int width;

    private int x;

    private int xHigh;

    private int xLow;

    private int y;

    private int yHigh;

    private int yLow;

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

    @Override
    public void run() {
        BézierDrawer.getRenderData(BézierControlPointList, step, point);
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
        width = raster.getWidth();
        height = raster.getHeight();
        curve = new BézierCurve(BézierControlPointList, point);
    }
}
