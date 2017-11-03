package io.github.guiritter.bézier_drawer;

import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.HORIZONTAL;
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
import javax.swing.table.DefaultTableModel;

public final class Setup {

    final JFrame frame;

    public int[] getNewPointColor() {
        return new int[]{0, 0, 0, 255};
    }

    public int getNewPointRadius() {
        return 5;
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
        JScrollPane tablePane = new JScrollPane();
        JTable table = new JTable();

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(10, 10, 0, 5);
        frame.getContentPane().add(renderTimeLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 10, 0, 5);

        frame.getContentPane().add(pointIndexLabel, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 10, 5, 5);

        frame.getContentPane().add(renderTimeSpinner, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 10, 5, 5);
        frame.getContentPane().add(pointIndexSpinner, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 10, 5, 5);
        frame.getContentPane().add(pointColorButton, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 10, 0, 5);

        frame.getContentPane().add(pointRadiusLabel, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 10, 5, 5);
        frame.getContentPane().add(pointRadiusSpinner, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 10, 0, 5);
        frame.getContentPane().add(outputMinimumLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 10, 5, 5);
        frame.getContentPane().add(outputMinimumField, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 10, 0, 5);
        frame.getContentPane().add(outputMaximumLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 10, 5, 5);
        frame.getContentPane().add(outputMaximumField, gridBagConstraints);

        table.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
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
        });
        tablePane.setViewportView(table);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 11;
        gridBagConstraints.fill = HORIZONTAL;
        gridBagConstraints.insets = new Insets(10, 5, 10, 10);
        frame.getContentPane().add(tablePane, gridBagConstraints);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String args[]) {
        Setup setup = new Setup();
    }
}
