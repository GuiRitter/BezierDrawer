package io.github.guiritter.bezier_drawer;

import static io.github.guiritter.bezier_drawer.BezierDrawer.getPointAmount;
import static io.github.guiritter.bezier_drawer.BezierDrawer.removePoint;
import static io.github.guiritter.bezier_drawer.BezierDrawer.setCurveColor;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import javax.imageio.ImageIO;
import static javax.swing.JColorChooser.showDialog;
import javax.swing.JFileChooser;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showOptionDialog;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Curve display area's right click menu. If a control point has been clicked,
 * allows its removal, setting its color, setting its position or its radius.
 * Otherwise, allows the insertion of new points at specified indexes,
 * setting for the background either a solid color or an image or
 * setting the curve's color.
 * @author Guilherme Alan Ritter
 */
public final class Menu {

    private final JFileChooser chooser = new JFileChooser();

    private final int colorInt[] = new int[4];

    private Color colorColor;

    /**
     * Right click menu items that do not relate to control points.
     */
    private final LinkedList<JMenuItem> elseItemList = new LinkedList<>();

    private File file;

    private final Point lastPoint = new Point(0, 0, colorInt, 0);

    private final Point lastPointTransient;

    final JPopupMenu menu;

    private int option;

    /**
     * Right click menu items that relate to control points.
     */
    private final LinkedList<JMenuItem> pointItemList = new LinkedList<>();

    final Semaphore pointSelectedSemaphore;

    private final PositionPanel positionPanel;

    private final JSpinner spinner = new JSpinner();

    private String string;

    public Menu(
     int width,
     int height,
     Edit edit,
     Setup setup,
     JFrame editFrame,
     Handler handler,
     WritableRaster backgroundRaster,
     Point lastPoint,
     Semaphore pointSelectedSemaphore,
     WrapperPoint pointSelectedWrapper
    ) {
        this.lastPointTransient = lastPoint;
        this.pointSelectedSemaphore = pointSelectedSemaphore;
        positionPanel = new PositionPanel(width, height);
        menu = new JPopupMenu(){

            private static final long serialVersionUID = -8132641424283398824L;

            @Override
            public void setVisible(boolean b) {
//                if ((!b) && (getParent() != null)) {
//                    // doesn't work near the right and bottom edges
//                    java.awt.Point point = getParent().getLocation();
//                    SwingUtilities.convertPointFromScreen(point, edit.imageComponent);
//                    System.out.println(point);
//                }
                if ((!b) && (getParent() != null)) {
                    Menu.this.lastPoint.x = lastPointTransient.x;
                    Menu.this.lastPoint.y = lastPointTransient.y;
                    handler.onMousePressedWait(Menu.this.lastPoint.x, Menu.this.lastPoint.y);
                    pointSelectedSemaphore.acquireUninterruptibly();
                }
//                if (getParent() != null) {
//                    System.out.println(
//                     b + "\t" +
//                     "before\t" +
//                     getParent().getBounds() + "\t" +
//                     getParent().getLocation() + "\t" +
//                     getParent().getMousePosition() + "\t" +
//                     getParent().getX() + "\t" +
//                     getParent().getY());
//                }
                super.setVisible(b); //To change body of generated methods, choose Tools | Templates.
//                if (getParent() != null) {
//                    System.out.println(
//                     b + "\t" +
//                     "after\t" +
//                     getParent().getBounds() + "\t" +
//                     getParent().getLocation() + "\t" +
//                     getParent().getMousePosition() + "\t" +
//                     getParent().getX() + "\t" +
//                     getParent().getY());
//                }
            }

//            @Override
//            public void doLayout() {
//                System.out.print(getBounds() + "\t" + getLocation() + "\t" + getMousePosition() + "\t" + getPopupLocation(null) + "\t" + getX() + "\t");
//                super.doLayout(); //To change body of generated methods, choose Tools | Templates.
//                System.out.println(getBounds() + "\t" + getLocation() + "\t" + getMousePosition() + "\t" + getPopupLocation(null) + "\t" + getX());
//            }
        };

        JMenuItem item = new JMenuItem("add point");
        item.addActionListener((ActionEvent e) -> {
            spinner.setModel(new SpinnerNumberModel(0, 0, getPointAmount(), 1));
            option = showOptionDialog(editFrame, spinner, "select the new point's index", OK_CANCEL_OPTION, QUESTION_MESSAGE, null, null, null);
            if (option != OK_OPTION) {
                return;
            }
            handler.onMouseClicked(lastPoint.x, lastPoint.y, ((SpinnerNumberModel) spinner.getModel()).getNumber().intValue());
        });
        menu.add(item);
        elseItemList.add(item);

        item = new JMenuItem("set background color");
        item.addActionListener((ActionEvent e) -> {
            colorColor = showDialog(editFrame, "choose background color", null);
            if (colorColor == null) {
                return;
            }
            colorInt[0] = colorColor.getRed();
            colorInt[1] = colorColor.getGreen();
            colorInt[2] = colorColor.getBlue();
            colorInt[3] = colorColor.getAlpha();
            handler.onBackgroundColorChanged(colorInt);
            edit.setBackgroundColor();
        });
        menu.add(item);
        elseItemList.add(item);

        item = new JMenuItem("set background picture");
        item.addActionListener((ActionEvent e) -> {
            file = null;
            if (chooser.showOpenDialog(editFrame) != APPROVE_OPTION) {
                return;
            }
            if ((file = chooser.getSelectedFile()) == null) {
                return;
            }
            try {
                edit.setBackgroundPicture(ImageIO.read(file));
            } catch (IOException ex) {
                edit.showError(ex);
            }
        });
        menu.add(item);
        elseItemList.add(item);

        item = new JMenuItem("set curve color");
        item.addActionListener((ActionEvent e) -> {
            colorColor = showDialog(editFrame, "choose curve color", null);
            if (colorColor == null) {
                return;
            }
            setCurveColor(
             colorColor.getRed(),
             colorColor.getGreen(),
             colorColor.getBlue(),
             colorColor.getAlpha()
            );
        });
        menu.add(item);
        elseItemList.add(item);

        menu.addSeparator();

        item = new JMenuItem("remove point");
        item.addActionListener((ActionEvent e) -> {
            if (pointSelectedWrapper.value != null) {
                removePoint(pointSelectedWrapper.value);
            }
        });
        menu.add(item);
        pointItemList.add(item);

        item = new JMenuItem("set point color");
        item.addActionListener((ActionEvent e) -> {
            if (pointSelectedWrapper.value == null) {
                return;
            }
            System.arraycopy(pointSelectedWrapper.value.color, 0, colorInt, 0, colorInt.length);
            colorColor = showDialog(editFrame, "choose point color", new Color(colorInt[0], colorInt[1], colorInt[2], colorInt[3]));
            if (colorColor == null) {
                return;
            }
            colorInt[0] = colorColor.getRed();
            colorInt[1] = colorColor.getGreen();
            colorInt[2] = colorColor.getBlue();
            colorInt[3] = colorColor.getAlpha();
            System.arraycopy(colorInt, 0, pointSelectedWrapper.value.color, 0, colorInt.length);
            setup.setPointColor(pointSelectedWrapper.index, colorInt);
        });
        menu.add(item);
        pointItemList.add(item);

        item = new JMenuItem("set point position");
        item.addActionListener((ActionEvent e) -> {
            if (pointSelectedWrapper.value == null) {
                return;
            }
            option = showOptionDialog(editFrame, positionPanel.panel, "insert new point position", OK_CANCEL_OPTION, QUESTION_MESSAGE, null, null, null);
            if (option != OK_OPTION) {
                return;
            }
            pointSelectedWrapper.value.x = positionPanel.getX();
            pointSelectedWrapper.value.y = positionPanel.getY();
        });
        menu.add(item);
        pointItemList.add(item);

        item = new JMenuItem("set point radius");
        item.addActionListener((ActionEvent e) -> {
            if (pointSelectedWrapper.value == null) {
                return;
            }
            try {
                string = showInputDialog(editFrame, "insert the new point radius:", "set point radius", QUESTION_MESSAGE);
                pointSelectedWrapper.value.radius = Integer.parseInt(string);
            } catch (NumberFormatException ex) {
                edit.showWarning(ex);
            }
        });
        menu.add(item);
        pointItemList.add(item);

        menu.addSeparator();

        item = new JMenuItem("cancel");
        menu.add(item);
    }
}
