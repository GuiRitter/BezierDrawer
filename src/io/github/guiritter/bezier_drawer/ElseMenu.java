package io.github.guiritter.bezier_drawer;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.image.WritableRaster;
import javax.swing.JColorChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public final class ElseMenu {

    private final int colorInt[] = new int[4];

    private Color colorColor;

    private final Handler handler;

    final JPopupMenu menu;

    private final Component parent;

    private final WritableRaster backgroundRaster;

    public ElseMenu(Component parent, Handler handler, WritableRaster backgroundRaster) {
        this.parent = parent;
        this.handler = handler;
        this.backgroundRaster = backgroundRaster;
        menu = new JPopupMenu();

        JMenuItem item = new JMenuItem("set background color");
        item.addActionListener((ActionEvent e) -> {
            colorColor = JColorChooser.showDialog(parent, "choose background color", null);
            if (colorColor == null) {
                return;
            }
            colorInt[0] = colorColor.getRed();
            colorInt[1] = colorColor.getGreen();
            colorInt[2] = colorColor.getBlue();
            colorInt[3] = colorColor.getAlpha();
            handler.onBackgroundColorChanged(backgroundRaster, colorInt);
        });
        menu.add(item);

        item = new JMenuItem("set curve color");
        item.addActionListener((ActionEvent e) -> {
            colorColor = JColorChooser.showDialog(parent, "choose curve color", null);
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
