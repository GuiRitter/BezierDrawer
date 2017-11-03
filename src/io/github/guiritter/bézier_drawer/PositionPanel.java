package io.github.guiritter.bézier_drawer;

import static io.github.guiritter.bézier_drawer.BézierDrawer.SPACE_DIMENSION;
import javax.swing.Box;
import javax.swing.BoxLayout;
import static javax.swing.BoxLayout.PAGE_AXIS;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public final class PositionPanel {

    final JPanel panel;

    private final JSpinner xSpinner;

    private final JSpinner ySpinner;

    public int getX() {
        return ((SpinnerNumberModel) xSpinner.getModel()).getNumber().intValue();
    }

    public int getY() {
        return ((SpinnerNumberModel) ySpinner.getModel()).getNumber().intValue();
    }

    public PositionPanel(int width, int height) {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, PAGE_AXIS));

        JLabel label = new JLabel("x:");
        label.setAlignmentX(0);
        panel.add(label);

        xSpinner = new JSpinner(new SpinnerNumberModel(0, 0, width - 1, 1));
        xSpinner.setAlignmentX(0);
        panel.add(xSpinner);

        panel.add(Box.createRigidArea(SPACE_DIMENSION));

        label = new JLabel("y:");
        label.setAlignmentX(0);
        panel.add(label);

        ySpinner = new JSpinner(new SpinnerNumberModel(0, 0, height - 1, 1));
        ySpinner.setAlignmentX(0);
        panel.add(ySpinner);
    }
}
