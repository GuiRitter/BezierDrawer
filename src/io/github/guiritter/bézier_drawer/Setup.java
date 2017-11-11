package io.github.guiritter.bézier_drawer;

import static io.github.guiritter.bézier_drawer.BézierDrawer.SPACE_HALF_INT;
import static io.github.guiritter.bézier_drawer.BézierDrawer.SPACE_INT;
import static io.github.guiritter.bézier_drawer.BézierDrawer.fontBold;
import java.awt.Color;
import static java.awt.Color.BLACK;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NORTH;
import static java.awt.GridBagConstraints.SOUTH;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.Arrays;
import javax.swing.JButton;
import static javax.swing.JColorChooser.showDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import static javax.swing.JTable.AUTO_RESIZE_OFF;
import javax.swing.SpinnerNumberModel;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public final class Setup {

    private final int colorInt[] = new int[4];

    private Color colorColor;

    private final JSpinner curvePointAmountSpinner;

    private final Wrapper<FitLinear> fitX = new Wrapper<>();

    private final Wrapper<FitLinear> fitY = new Wrapper<>();

    private static final String fpsString = "frame period (ms) = %f FPS:";

    final JFrame frame;

    private final JLabel framePeriodLabel;

    private final JSpinner framePeriodSpinner;

    private final Wrapper<Integer> height = new Wrapper<>();

    private final Wrapper<Double> outputMaximumX = new Wrapper<>();

    private final Wrapper<Double> outputMaximumY = new Wrapper<>();

    private final Wrapper<Double> outputMinimumX = new Wrapper<>();

    private final Wrapper<Double> outputMinimumY = new Wrapper<>();

    private double outputX;

    private double outputY;

    private final JButton pointColorButton;

    private final JSpinner pointRadiusSpinner;

    private static final int TABLE_COLUMN_INDEX = 0;
    private static final int TABLE_COLUMN_DISPLAY_X = 1;
    private static final int TABLE_COLUMN_DISPLAY_Y = 2;
    private static final int TABLE_COLUMN_OUTPUT_X = 3;
    private static final int TABLE_COLUMN_OUTPUT_Y = 4;
    private static final int TABLE_COLUMN_COLOR = 5;

    private final DefaultTableModel tableModel;

    private final Wrapper<Integer> width = new Wrapper<>();

    public void addPoint(int x, int y, int color[]) {
        if (fitX.value == null) {
            outputX = 0;
            outputY = 0;
        } else {
            outputX = fitX.value.f(x);
            outputY = fitY.value.f(y);
        }
        tableModel.addRow(new Object[]{tableModel.getRowCount(), x, y, outputX, outputY, Arrays.copyOf(color, color.length)});
    }

    public void addPoint(int x, int y, int color[], int i) {
        if (fitX.value == null) {
            outputX = 0;
            outputY = 0;
        } else {
            outputX = fitX.value.f(x);
            outputY = fitY.value.f(y);
        }
        tableModel.insertRow(i, new Object[]{tableModel.getRowCount(), x, y, outputX, outputY, Arrays.copyOf(color, color.length)}); // TODO fit
        updateIndex(i);
    }

    private static Color getContrast(Color color) {
        return new Color(
         (color.getRed()   < 128) ? 255 : 0,
         (color.getGreen() < 128) ? 255 : 0,
         (color.getBlue()  < 128) ? 255 : 0,
         color.getAlpha()
        );
    }

    private static Color getContrast(int color[]) {
        return new Color(
         (color[0] < 128) ? 255 : 0,
         (color[1] < 128) ? 255 : 0,
         (color[2] < 128) ? 255 : 0,
         color[3]
        );
    }

    public double getCurveStep() {
        return 1d / ((SpinnerNumberModel) curvePointAmountSpinner.getModel()).getNumber().doubleValue();
    }

    public long getFramePeriod() {
        return ((SpinnerNumberModel) framePeriodSpinner.getModel()).getNumber().longValue();
    }

    public int[] getNewPointColor() {
        colorColor = pointColorButton.getBackground();
        return new int[]{colorColor.getRed(), colorColor.getGreen(), colorColor.getBlue(), colorColor.getAlpha()};
    }

    public int getPointRadius() {
        return ((SpinnerNumberModel) pointRadiusSpinner.getModel()).getNumber().intValue();
    }

    public void removePoint(int i) {
        tableModel.removeRow(i);
        updateIndex(i);
    }

    private void setFPSString() {
        framePeriodLabel.setText(String.format(fpsString, 1000d / ((double) getFramePeriod())));
    }

    public void setPoint(int i, int x, int y) {
        if (fitX.value == null) {
            outputX = 0;
            outputY = 0;
        } else {
            outputX = fitX.value.f(x);
            outputY = fitY.value.f(y);
        }
        tableModel.setValueAt(      x, i, TABLE_COLUMN_DISPLAY_X);
        tableModel.setValueAt(      y, i, TABLE_COLUMN_DISPLAY_Y);
        tableModel.setValueAt(outputX, i, TABLE_COLUMN_OUTPUT_X);
        tableModel.setValueAt(outputY, i, TABLE_COLUMN_OUTPUT_Y);
    }

    public void setPointColor(int i, int color[]) {
        tableModel.setValueAt(Arrays.copyOf(color, color.length), i, TABLE_COLUMN_COLOR);
        frame.revalidate();
        frame.repaint();
    }

    public void setSize(int width, int height) {
        this.width.value = width - 1;
        this.height.value = height - 1;
    }

    public void updateIndex(int i) {
        for (i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(i, i, TABLE_COLUMN_INDEX);
        }
    }

    public Setup() {
        frame = new JFrame("Bézier Drawer · Setup");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints gridBagConstraints;

        framePeriodLabel = new JLabel("frame period (ms):");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = SOUTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(SPACE_INT, SPACE_INT, 0, SPACE_HALF_INT);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(framePeriodLabel, gridBagConstraints);

        framePeriodSpinner = new JSpinner(new SpinnerNumberModel(34, 1, Long.MAX_VALUE, 1));
        framePeriodSpinner.addChangeListener((ChangeEvent e) -> {

            BézierDrawer.setFramePeriod(getFramePeriod());
            setFPSString();
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = NORTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, SPACE_INT, SPACE_HALF_INT, SPACE_HALF_INT);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(framePeriodSpinner, gridBagConstraints);
        setFPSString();

        JLabel curvePointAmountLabel = new JLabel("curve point amount:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = SOUTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(SPACE_HALF_INT, SPACE_INT, 0, SPACE_HALF_INT);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(curvePointAmountLabel, gridBagConstraints);

        curvePointAmountSpinner = new JSpinner(new SpinnerNumberModel(1000, 0, Long.MAX_VALUE, 1));
        curvePointAmountSpinner.addChangeListener((ChangeEvent e) -> {

            BézierDrawer.setCurveStep(getCurveStep());
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = NORTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, SPACE_INT, SPACE_HALF_INT, SPACE_HALF_INT);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(curvePointAmountSpinner, gridBagConstraints);
        BézierDrawer.setCurveStep(getCurveStep());

        JLabel pointIndexLabel = new JLabel("point index:");
        pointIndexLabel.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = SOUTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(SPACE_HALF_INT, SPACE_INT, 0, SPACE_HALF_INT);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(pointIndexLabel, gridBagConstraints);

        JSpinner pointIndexSpinner = new JSpinner();
        pointIndexSpinner.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = NORTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, SPACE_INT, SPACE_HALF_INT, SPACE_HALF_INT);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(pointIndexSpinner, gridBagConstraints);

        JLabel pointRadiusLabel = new JLabel("point radius:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = SOUTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(SPACE_HALF_INT, SPACE_INT, 0, SPACE_HALF_INT);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(pointRadiusLabel, gridBagConstraints);

        pointRadiusSpinner = new JSpinner(new SpinnerNumberModel(5, 1, Short.MAX_VALUE, 1));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = NORTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, SPACE_INT, SPACE_HALF_INT, SPACE_HALF_INT);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(pointRadiusSpinner, gridBagConstraints);

        pointColorButton = new JButton("point color");
        pointColorButton.setFont(fontBold);
        pointColorButton.setBackground(BLACK);
        pointColorButton.setForeground(getContrast(pointColorButton.getBackground()));
        pointColorButton.addActionListener((ActionEvent e) -> {

            colorColor = showDialog(frame, "choose point color", pointColorButton.getBackground());
            if (colorColor == null) {
                return;
            }
            pointColorButton.setBackground(colorColor);
            pointColorButton.setForeground(getContrast(colorColor));
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(SPACE_HALF_INT, SPACE_INT, SPACE_HALF_INT, SPACE_HALF_INT);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(pointColorButton, gridBagConstraints);

        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(Integer.MAX_VALUE);

        JLabel outputMinimumXLabel = new JLabel("output minimum x:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = SOUTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(SPACE_HALF_INT, SPACE_INT, 0, SPACE_HALF_INT);
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(outputMinimumXLabel, gridBagConstraints);

        final JFormattedTextField outputMinimumXField = new JFormattedTextField(format);
        outputMinimumXField.addPropertyChangeListener("value", new OutputListener(outputMinimumXField, outputMinimumX, outputMinimumX, outputMaximumX, fitX, width));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = NORTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, SPACE_INT, SPACE_HALF_INT, SPACE_HALF_INT);
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(outputMinimumXField, gridBagConstraints);

        JLabel outputMaximumXLabel = new JLabel("output maximum x:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = SOUTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(SPACE_HALF_INT, SPACE_HALF_INT, 0, SPACE_HALF_INT);
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(outputMaximumXLabel, gridBagConstraints);

        final JFormattedTextField outputMaximumXField = new JFormattedTextField(format);
        outputMaximumXField.addPropertyChangeListener("value", new OutputListener(outputMaximumXField, outputMaximumX, outputMinimumX, outputMaximumX, fitX, width));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = NORTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, SPACE_HALF_INT, SPACE_HALF_INT, SPACE_HALF_INT);
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(outputMaximumXField, gridBagConstraints);

        JLabel outputMinimumYLabel = new JLabel("output minimum y:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = SOUTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(SPACE_HALF_INT, SPACE_INT, 0, SPACE_HALF_INT);
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(outputMinimumYLabel, gridBagConstraints);

        JFormattedTextField outputMinimumYField = new JFormattedTextField(format);
        outputMinimumYField.addPropertyChangeListener("value", new OutputListener(outputMinimumYField, outputMinimumY, outputMinimumY, outputMaximumY, fitY, height));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = NORTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, SPACE_INT, SPACE_INT, SPACE_HALF_INT);
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(outputMinimumYField, gridBagConstraints);

        JLabel outputMaximumYLabel = new JLabel("output maximum y:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = SOUTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(SPACE_HALF_INT, SPACE_HALF_INT, 0, SPACE_HALF_INT);
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(outputMaximumYLabel, gridBagConstraints);

        JFormattedTextField outputMaximumYField = new JFormattedTextField(format);
        outputMaximumYField.addPropertyChangeListener("value", new OutputListener(outputMaximumYField, outputMaximumY, outputMinimumY, outputMaximumY, fitY, height));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = NORTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, SPACE_HALF_INT, SPACE_INT, SPACE_HALF_INT);
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(outputMaximumYField, gridBagConstraints);

        tableModel = new DefaultTableModel(
            new Object [][] {},
            new String [] {
                "point", "screen x", "screen y", "output x", "output y", "color"
            }
        ) {
            Class[] types = new Class [] {
                String.class, Integer.class, Integer.class, Double.class, Double.class, Integer[].class
            };

            boolean[] canEdit = new boolean [] {
                false, true, true, false, false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };

        JTable table = new JTable();
        table.setModel(tableModel);
        table.getColumnModel().getColumn(TABLE_COLUMN_COLOR).setMinWidth(0);
        table.getColumnModel().getColumn(TABLE_COLUMN_COLOR).setMaxWidth(0);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        DefaultTableCellRenderer colorCenterRenderer = new DefaultTableCellRenderer(){

            private int color[];

            private Component component;

            @Override
            public Font getFont() {
                return fontBold;
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); //To change body of generated methods, choose Tools | Templates.
                if (column != TABLE_COLUMN_INDEX) {
                    return component;
                }
                color = (int[]) tableModel.getValueAt(row, TABLE_COLUMN_COLOR);
                component.setBackground(new Color(color[0], color[1], color[2], color[3]));
                component.setForeground(getContrast(color));
                return component;
            }
        };
        centerRenderer.setHorizontalAlignment(CENTER);
        colorCenterRenderer.setHorizontalAlignment(CENTER);
        table.setDefaultRenderer(String.class, colorCenterRenderer);
        table.setDefaultRenderer(Integer.class, centerRenderer);
        table.setDefaultRenderer(Double.class, centerRenderer);

        JScrollPane tablePane = new JScrollPane();
        table.setAutoResizeMode(AUTO_RESIZE_OFF);
        tablePane.setViewportView(table);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 13;
        gridBagConstraints.fill = BOTH;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.insets = new Insets(SPACE_INT, SPACE_HALF_INT, SPACE_INT, SPACE_INT);
        frame.getContentPane().add(tablePane, gridBagConstraints);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

//    public static void main(String args[]) {
//        Setup setup = new Setup();
//    }
}
