package io.github.guiritter.bezier_drawer;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import javax.swing.JDialog;
import javax.swing.JFrame;

public final class BézierDrawer {

    private static final LinkedList<Point> BézierControlPointList = new LinkedList();

    private static final int curveColor[] = new int[]{0, 0, 0, 255};

    private static final Semaphore semaphore = new Semaphore(1, true);

    private static final SetupFrame setupFrame = new SetupFrame();

    private static final WrapperDouble step = new WrapperDouble(0.001);

    static void addPoint(int x, int y) {
        semaphore.acquireUninterruptibly();
        BézierControlPointList.add(new Point(x, y, setupFrame.getNewPointColor(), setupFrame.getNewPointRadius()));
        semaphore.release();
    }

    static void getPointList(LinkedList<Point> list) {
        semaphore.acquireUninterruptibly();
        list.clear();
        list.addAll(BézierControlPointList);
        semaphore.release();
    }

    static void getRenderData(LinkedList<Point> list, WrapperDouble step, Point point) {
        semaphore.acquireUninterruptibly();
        list.clear();
        list.addAll(BézierControlPointList);
        step.value = BézierDrawer.step.value;
        System.arraycopy(BézierDrawer.curveColor, 0, point.color, 0, 4);
        semaphore.release();
    }

    static void setCurveColor(int r, int g, int b, int a) {
        semaphore.acquireUninterruptibly();
        curveColor[0] = r;
        curveColor[1] = g;
        curveColor[2] = b;
        curveColor[3] = a;
        semaphore.release();
    }

    static void setStep(double step) {
        semaphore.acquireUninterruptibly();
        BézierDrawer.step.value = step;
        semaphore.release();
    }

    static {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
    }

    public static void main(String args[]) {
        new EditFrame();
    }
}
