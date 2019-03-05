package io.github.guiritter.bezier_drawer;

import java.awt.Dimension;
import java.awt.Font;
import static java.awt.Font.BOLD;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

/**
 * Allows the drawing of a Bézier curve. Opens one window that displays
 * the curve and allows the manipulation of its control points.
 * Opens another window that allows the setup of runtime parameters
 * and displays control point data in a table.
 * This is the main class that, besides opening the windows,
 * provides access control to variables that might be read from and written to
 * at the same time, such as the control points.
 * @author Guilherme Alan Ritter
 */
public final class BezierDrawer {

    private static final LinkedList<Point> BézierControlPointList = new LinkedList<>();

    private static int color[];

    private static final int curveColor[] = new int[]{0, 0, 0, 255};

    /**
     * Window that displays the curve and allows the manipulation
     * of its control points.
     */
    private static final Edit edit;

    public static final Font font = new Font("DejaVu Sans", 0, 12); // NOI18N

    public static final Font fontBold = font.deriveFont(font.getStyle() | BOLD);

    private static final Semaphore semaphore = new Semaphore(1, true);

    /**
     * Window that sets up runtime parameters and displays control point data
     * in a table.
     */
    private static final Setup setup;

    /**
     * Time step used to compute the curve. Inverse to the amount of points
     * in the curve.
     */
    private static final Wrapper<Double> step = new Wrapper<>();

    /**
     * Distance between graphical user interface components.
     */
    public static final int SPACE_INT;

    /**
     * Distance between graphical user interface components.
     */
    public static final Dimension SPACE_DIMENSION;

    /**
     * Half distance between graphical user interface components.
     */
    public static final int SPACE_HALF_INT;

    /**
     * Half distance between graphical user interface components.
     */
    public static final Dimension SPACE_HALF_DIMENSION;

    /**
     * Inverse to frames per second.
     * @return
     */
    static long getFramePeriod() {
        return setup.getFramePeriod();
    }

    /**
     * Adds a new control point to the list and adds a row in the table
     * to represent this point.
     * @param x
     * @param y
     */
    static void addPoint(int x, int y) {
        semaphore.acquireUninterruptibly();
        color = setup.getPointColor();
        BézierControlPointList.add(new Point(x, y, color, setup.getPointRadius()));
        setup.addPoint(x, y, color);
        semaphore.release();
    }

    /**
     * Adds a new control point to the list and adds a row in the table
     * to represent this point.
     * @param x
     * @param y
     */
    static void addPoint(int x, int y, int index) {
        semaphore.acquireUninterruptibly();
        color = setup.getPointColor();
        BézierControlPointList.add(index, new Point(x, y, color, setup.getPointRadius()));
        setup.addPoint(x, y, color, index);
        semaphore.release();
    }

    /**
     * @return amount of control points
     */
    static int getPointAmount() {
        semaphore.acquireUninterruptibly();
        int returnInt = BézierControlPointList.size();
        semaphore.release();
        return returnInt;
    }

    /**
     * Clears the list passed by parameter and adds all control points
     * to this list.
     * @param list
     */
    static void getPointList(LinkedList<Point> list) {
        semaphore.acquireUninterruptibly();
        list.clear();
        list.addAll(BézierControlPointList);
        semaphore.release();
    }

    /**
     * @param list list of control points
     * @param step time step
     * @param point curve color wrapper
     */
    static void getRenderData(LinkedList<Point> list, Wrapper<Double> step, Point point) {
        semaphore.acquireUninterruptibly();
        list.clear();
        list.addAll(BézierControlPointList);
        step.value = BezierDrawer.step.value;
        System.arraycopy(BezierDrawer.curveColor, 0, point.color, 0, 4);
        semaphore.release();
    }

    /**
     * Removes a control point from the list and removes the corresponding row
     * from the table.
     * @param point
     */
    static void removePoint(Point point) {
        semaphore.acquireUninterruptibly();
        setup.removePoint(BézierControlPointList.indexOf(point));
        BézierControlPointList.remove(point);
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

    /**
     * @param step time step
     */
    static void setCurveStep(double step) {
        semaphore.acquireUninterruptibly();
        BezierDrawer.step.value = step;
        semaphore.release();
    }

    /**
     * @param period inverse to frames per second
     */
    static void setFramePeriod(long period) {
        edit.setFramePeriod(period);
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
