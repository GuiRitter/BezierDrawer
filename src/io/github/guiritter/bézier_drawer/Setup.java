package io.github.guiritter.bézier_drawer;

import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NORTH;
import static java.awt.GridBagConstraints.SOUTH;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import static javax.swing.JTable.AUTO_RESIZE_OFF;
import javax.swing.table.DefaultTableModel;

public final class Setup {

    final JFrame frame;

    private final DefaultTableModel tableModel;

    public void addPoint(int x, int y) {
        tableModel.addRow(new Object[]{tableModel.getRowCount(), x, y, 0, 0});
    }

    public void addPoint(int x, int y, int i) {
        tableModel.insertRow(i, new Object[]{tableModel.getRowCount(), x, y, 0, 0});
        updateIndex(i);
    }

    public int[] getNewPointColor() {
        return new int[]{0, 0, 0, 255};
    }

    public int getNewPointRadius() {
        return 5;
    }

    public void removePoint(int i) {
        tableModel.removeRow(i);
        updateIndex(i);
    }

    public void setPoint(int i, int x, int y) {
        tableModel.setValueAt(x, i, 1);
        tableModel.setValueAt(y, i, 2);
    }

    public void updateIndex(int i) {
        for (i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(i, i, 0);
        }
    }

    public Setup() {
        frame = new JFrame("Bézier Drawer · Setup");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints gridBagConstraints;

        JLabel renderTimeLabel = new JLabel("render time (ms):");
        JLabel pointIndexLabel = new JLabel("point index:");
        JSpinner renderTimeSpinner = new JSpinner();
        JSpinner pointIndexSpinner = new JSpinner();
        JButton pointColorButton = new JButton("point color");
        JLabel pointRadiusLabel = new JLabel("point radius:");
        JSpinner pointRadiusSpinner = new JSpinner();
        JLabel outputMinimumLabel = new JLabel("output minimum:");
        JFormattedTextField outputMinimumField = new JFormattedTextField();
        JLabel outputMaximumLabel = new JLabel("output maximum:");
        JFormattedTextField outputMaximumField = new JFormattedTextField();

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = SOUTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(10, 10, 0, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(renderTimeLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = NORTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 10, 5, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(renderTimeSpinner, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = SOUTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 10, 0, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(pointIndexLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = NORTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 10, 5, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(pointIndexSpinner, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = SOUTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 10, 0, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(pointRadiusLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = NORTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 10, 5, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(pointRadiusSpinner, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 10, 5, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(pointColorButton, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = SOUTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 10, 0, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(outputMinimumLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = NORTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 10, 5, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(outputMinimumField, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = SOUTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 10, 0, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(outputMaximumLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = NORTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 10, 10, 5);
        gridBagConstraints.weighty = 1;
        frame.getContentPane().add(outputMaximumField, gridBagConstraints);

        tableModel = new DefaultTableModel(
            new Object [][] {},
            new String [] {
                "point", "screen x", "screen y", "output x", "output y"
            }
        ) {
            Class[] types = new Class [] {
                String.class, Integer.class, Integer.class, Double.class, Double.class
            };

            boolean[] canEdit = new boolean [] {
                false, true, true, false, false
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

        JScrollPane tablePane = new JScrollPane();
        JTable table = new JTable();
        table.setModel(tableModel);
        table.setAutoResizeMode(AUTO_RESIZE_OFF);
        tablePane.setViewportView(table);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 11;
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
