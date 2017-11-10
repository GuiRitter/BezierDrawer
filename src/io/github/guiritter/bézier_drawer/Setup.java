package io.github.guiritter.bézier_drawer;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NORTH;
import static java.awt.GridBagConstraints.SOUTH;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import javax.swing.JButton;
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

    private static final String fpsString = "frame period (ms) = %f FPS:";

    final JFrame frame;

    private final JLabel framePeriodLabel;

    private final JSpinner framePeriodSpinner;

    private static final int TABLE_COLUMN_INDEX = 0;
    private static final int TABLE_COLUMN_DISPLAY_X = 1;
    private static final int TABLE_COLUMN_DISPLAY_Y = 2;
    private static final int TABLE_COLUMN_OUTPUT_X = 3;
    private static final int TABLE_COLUMN_OUTPUT_Y = 4;
    private static final int TABLE_COLUMN_COLOR = 5;

    private final DefaultTableModel tableModel;

    public void addPoint(int x, int y, int color[]) {
        tableModel.addRow(new Object[]{tableModel.getRowCount(), x, y, 0, 0, Arrays.copyOf(color, color.length)}); // TODO fit
    }

    public void addPoint(int x, int y, int color[], int i) {
        tableModel.insertRow(i, new Object[]{tableModel.getRowCount(), x, y, 0, 0, Arrays.copyOf(color, color.length)}); // TODO fit
        updateIndex(i);
    }

    public long getFramePeriod() {
        return ((SpinnerNumberModel) framePeriodSpinner.getModel()).getNumber().longValue();
    }

    public int[] getNewPointColor() {
        return new int[]{0, 0, 0, 255}; // TODO
    }

    public int getNewPointRadius() {
        return 5; // TODO
    }

    public void removePoint(int i) {
        tableModel.removeRow(i);
        updateIndex(i);
    }

    private void setFPSString() {
        framePeriodLabel.setText(String.format(fpsString, 1000d / ((double) getFramePeriod())));
    }

    public void setPoint(int i, int x, int y) {
        tableModel.setValueAt(x, i, TABLE_COLUMN_DISPLAY_X);
        tableModel.setValueAt(y, i, TABLE_COLUMN_DISPLAY_Y);
    }

    public void setPointColor(int i, int color[]) {
        tableModel.setValueAt(Arrays.copyOf(color, color.length), i, TABLE_COLUMN_COLOR);
        frame.revalidate();
        frame.repaint();
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

        JLabel pointIndexLabel = new JLabel("point index:");
        JSpinner pointIndexSpinner = new JSpinner();
        JButton pointColorButton = new JButton("point color");
        JLabel pointRadiusLabel = new JLabel("point radius:");
        JSpinner pointRadiusSpinner = new JSpinner();
        JLabel outputMinimumLabel = new JLabel("output minimum:");
        JFormattedTextField outputMinimumField = new JFormattedTextField();
        JLabel outputMaximumLabel = new JLabel("output maximum:");
        JFormattedTextField outputMaximumField = new JFormattedTextField();

        framePeriodLabel = new JLabel("frame period (ms):");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = SOUTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(10, 10, 0, 5);
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
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 10, 5, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(framePeriodSpinner, gridBagConstraints);
        setFPSString();

        JLabel curvePointAmountLabel = new JLabel("curve point amount:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = SOUTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 10, 0, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(curvePointAmountLabel, gridBagConstraints);

        JSpinner curvePointAmountSpinner = new JSpinner(new SpinnerNumberModel(1000, 0, Long.MAX_VALUE, 1));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = NORTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 10, 5, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(curvePointAmountSpinner, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = SOUTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 10, 0, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(pointIndexLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = NORTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 10, 5, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(pointIndexSpinner, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = SOUTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 10, 0, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(pointRadiusLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = NORTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 10, 5, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(pointRadiusSpinner, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 10, 5, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(pointColorButton, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = SOUTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 10, 0, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(outputMinimumLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = NORTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 10, 5, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(outputMinimumField, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = SOUTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 10, 0, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(outputMaximumLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = NORTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 10, 10, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(outputMaximumField, gridBagConstraints);

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
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); //To change body of generated methods, choose Tools | Templates.
                if (column != TABLE_COLUMN_INDEX) {
                    return component;
                }
                color = (int[]) tableModel.getValueAt(row, TABLE_COLUMN_COLOR);
                component.setBackground(new Color(color[0], color[1], color[2], color[3]));
                component.setForeground(new Color(
                 (color[0] < 128) ? 255 : 0,
                 (color[1] < 128) ? 255 : 0,
                 (color[2] < 128) ? 255 : 0,
                 color[3]));
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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 13;
        gridBagConstraints.fill = BOTH;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.insets = new Insets(10, 5, 10, 10);
        frame.getContentPane().add(tablePane, gridBagConstraints);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

//    public static void main(String args[]) {
//        Setup setup = new Setup();
//    }
}
