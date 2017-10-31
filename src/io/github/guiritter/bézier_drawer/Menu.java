package io.github.guiritter.bézier_drawer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public final class Menu {

    private final JFileChooser chooser = new JFileChooser();

    private final int colorInt[] = new int[4];

    private Color colorColor;

    private final EditFrame editFrame;

    private File file;

    private final Handler handler;

    final JPopupMenu menu;

    private final WritableRaster backgroundRaster;

    public Menu(EditFrame editFrame, Handler handler, WritableRaster backgroundRaster) {
        this.editFrame = editFrame;
        this.handler = handler;
        this.backgroundRaster = backgroundRaster;
        menu = new JPopupMenu();

        JMenuItem item = new JMenuItem("set background color");
        item.addActionListener((ActionEvent e) -> {
            colorColor = JColorChooser.showDialog(editFrame.frame, "choose background color", null);
            if (colorColor == null) {
                return;
            }
            colorInt[0] = colorColor.getRed();
            colorInt[1] = colorColor.getGreen();
            colorInt[2] = colorColor.getBlue();
            colorInt[3] = colorColor.getAlpha();
            handler.onBackgroundColorChanged(colorInt);
            editFrame.setBackgroundColor();
        });
        menu.add(item);

        item = new JMenuItem("set background picture");
        item.addActionListener((ActionEvent e) -> {
            file = null;
            if (chooser.showOpenDialog(editFrame.frame) != APPROVE_OPTION) {
                return;
            }
            if ((file = chooser.getSelectedFile()) == null) {
                return;
            }
            try {
                editFrame.setBackgroundPicture(ImageIO.read(file));
            } catch (IOException ex) {
                editFrame.showError(ex);
            }
        });
        menu.add(item);

        item = new JMenuItem("set curve color");
        item.addActionListener((ActionEvent e) -> {
            colorColor = JColorChooser.showDialog(editFrame.frame, "choose curve color", null);
            if (colorColor == null) {
                return;
            }
            BézierDrawer.setCurveColor(
             colorColor.getRed(),
             colorColor.getGreen(),
             colorColor.getBlue(),
             colorColor.getAlpha()
            );
        });
        menu.add(item);
    }
}
