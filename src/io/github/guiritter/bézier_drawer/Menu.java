package io.github.guiritter.bézier_drawer;

import static io.github.guiritter.bézier_drawer.BézierDrawer.getPointAmount;
import static io.github.guiritter.bézier_drawer.BézierDrawer.setCurveColor;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import static javax.swing.JColorChooser.showDialog;
import javax.swing.JFileChooser;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import javax.swing.JMenuItem;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.showOptionDialog;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public final class Menu {

    private final WritableRaster backgroundRaster;

    private final JFileChooser chooser = new JFileChooser();

    private final int colorInt[] = new int[4];

    private Color colorColor;

    private final EditFrame edit;

    private File file;

    private final Handler handler;

    final JPopupMenu menu;

    private int option;

    private Point point;

    private final JSpinner spinner = new JSpinner();

    public Menu(EditFrame edit, Handler handler, WritableRaster backgroundRaster) {
        this.edit = edit;
        this.handler = handler;
        this.backgroundRaster = backgroundRaster;
        menu = new JPopupMenu(){

            @Override
            public void setVisible(boolean b) {
                if (getParent() != null) {
                    System.out.print(
                     getParent().getBounds() + "\t" +
                     getParent().getLocation() + "\t" +
                     getParent().getMousePosition() + "\t" +
                     getParent().getX() + "\t" +
                     getParent().getY() + "\t");
                }
                super.setVisible(b); //To change body of generated methods, choose Tools | Templates.
                if (getParent() != null) {
                    System.out.println(
                     getParent().getBounds() + "\t" +
                     getParent().getLocation() + "\t" +
                     getParent().getMousePosition() + "\t" +
                     getParent().getX() + "\t" +
                     getParent().getY());
                }
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
            option = showOptionDialog(edit.frame, spinner, "select the new point's index", OK_CANCEL_OPTION, QUESTION_MESSAGE, null, null, null);
            if (option != OK_OPTION) {
                return;
            }
//            System.out.println(point);
            // TODO
        });
        menu.add(item);

        item = new JMenuItem("set background color");
        item.addActionListener((ActionEvent e) -> {
            colorColor = showDialog(edit.frame, "choose background color", null);
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

        item = new JMenuItem("set background picture");
        item.addActionListener((ActionEvent e) -> {
            file = null;
            if (chooser.showOpenDialog(edit.frame) != APPROVE_OPTION) {
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

        item = new JMenuItem("set curve color");
        item.addActionListener((ActionEvent e) -> {
            colorColor = showDialog(edit.frame, "choose curve color", null);
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
    }
}
