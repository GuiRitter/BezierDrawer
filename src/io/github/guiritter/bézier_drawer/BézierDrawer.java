package io.github.guiritter.bézier_drawer;

import java.awt.Dimension;
import java.awt.Font;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

public final class BézierDrawer {

    private static final int backgroundColor[] = new int[]{255, 255, 255, 0};

    private static final LinkedList<Point> BézierControlPointList = new LinkedList();

    private static int color[];

    private static final int curveColor[] = new int[]{0, 0, 0, 255};

    private static final Edit edit;

    public static final Font font = new Font("DejaVu Sans", 0, 12); // NOI18N

    private static final Semaphore semaphore = new Semaphore(1, true);

    private static final Setup setup;

    private static final Wrapper step = new Wrapper();

    public static final int SPACE_INT;

    public static final Dimension SPACE_DIMENSION;

    public static final int SPACE_HALF_INT;

    public static final Dimension SPACE_HALF_DIMENSION;

    static long getFramePeriod() {
        return setup.getFramePeriod();
    }

    static void addPoint(int x, int y) {
        semaphore.acquireUninterruptibly();
        color = setup.getNewPointColor();
        BézierControlPointList.add(new Point(x, y, color, setup.getNewPointRadius()));
        setup.addPoint(x, y, color);
        semaphore.release();
    }

    static void addPoint(int x, int y, int i) {
        semaphore.acquireUninterruptibly();
        color = setup.getNewPointColor();
        BézierControlPointList.add(i, new Point(x, y, color, setup.getNewPointRadius()));
        setup.addPoint(x, y, color, i);
        semaphore.release();
    }

    static int getPointAmount() {
        semaphore.acquireUninterruptibly();
        int returnInt = BézierControlPointList.size();
        semaphore.release();
        return returnInt;
    }

    static void getPointList(LinkedList<Point> list) {
        semaphore.acquireUninterruptibly();
        list.clear();
        list.addAll(BézierControlPointList);
        semaphore.release();
    }

    static void getRenderData(LinkedList<Point> list, Wrapper step, Point point) {
        semaphore.acquireUninterruptibly();
        list.clear();
        list.addAll(BézierControlPointList);
        step.value = BézierDrawer.step.value; // TODO
        System.arraycopy(BézierDrawer.curveColor, 0, point.color, 0, 4);
        semaphore.release();
    }

    static void removePoint(Point point) {
        semaphore.acquireUninterruptibly();
        setup.removePoint(BézierControlPointList.indexOf(point));
        BézierControlPointList.remove(point);
        semaphore.release();
    }

    static void setBackgroundColor(int r, int g, int b, int a) {
        semaphore.acquireUninterruptibly();
        backgroundColor[0] = r;
        backgroundColor[1] = g;
        backgroundColor[2] = b;
        backgroundColor[3] = a;
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

    static void setCurveStep(double step) {
        semaphore.acquireUninterruptibly();
        BézierDrawer.step.value = step;
        semaphore.release();
    }

    static void setFramePeriod(long period) {
        edit.setFramePeriod(period);
    }

    static void setStep(double step) {
        semaphore.acquireUninterruptibly();
        BézierDrawer.step.value = step;
        semaphore.release();
    }

    static {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        UIManager.put("Button.font",             font);
        UIManager.put("CheckBox.font",           font);
        UIManager.put("ComboBox.font",           font);
        UIManager.put("FormattedTextField.font", font);
        UIManager.put("InternalFrame.titleFont", font);
        UIManager.put("Label.font",              font);
        UIManager.put("List.font",               font);
        UIManager.put("MenuItem.font",           font);
        UIManager.put("Spinner.font",            font);
        UIManager.put("Table.font",              font);
        UIManager.put("TableHeader.font",        font);
        UIManager.put("TextField.font",          font);
        UIManager.put("ToolTip.font",            font);
        JLabel label = new JLabel("—");
        SPACE_INT = Math.min(
         label.getPreferredSize().width,
         label.getPreferredSize().height);
        SPACE_HALF_INT = SPACE_INT / 2;
        SPACE_DIMENSION = new Dimension(SPACE_INT, SPACE_INT);
        SPACE_HALF_DIMENSION = new Dimension(SPACE_HALF_INT, SPACE_HALF_INT);

        setup = new Setup();
        edit = new Edit(setup);
    }

    public static void main(String args[]) {}
}
