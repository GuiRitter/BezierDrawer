package io.github.guiritter.bezier_drawer;

import io.github.guiritter.imagecomponent.ImageComponentMultiple;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import java.awt.image.WritableRaster;
import java.util.LinkedList;
import java.util.Timer;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public final class EditFrame {

    private BufferedImage backgroundImage;

    private WritableRaster backgroundRaster;

    private BufferedImage foregroundImage;

    private WritableRaster foregroundRaster;

    private Handler handler;

    private int height;

    private ImageComponentMultiple imageComponent;

    private final JFrame frame;

    private int width;

    public EditFrame() {
        frame = new JFrame("Bézier Drawer · Edit");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton button = new JButton("initialize");
        button.addActionListener((ActionEvent e) -> {

            width = button.getWidth();
            height = button.getHeight();
            backgroundImage = new BufferedImage(width, height, TYPE_INT_ARGB);
            backgroundRaster = backgroundImage.getRaster();
            foregroundImage = new BufferedImage(width, height, TYPE_INT_ARGB);
            foregroundRaster = foregroundImage.getRaster();
            LinkedList<BufferedImage> list = new LinkedList<>();
            list.add(backgroundImage);
            list.add(foregroundImage);
            imageComponent = new ImageComponentMultiple(list);
            imageComponent.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    handler.onMouseClicked(e.getX(), e.getY());
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    handler.onMousePressed(e.getX(), e.getY());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    handler.onMouseReleased();
                }
            });
            imageComponent.addMouseMotionListener(new MouseMotionAdapter() {

                @Override
                public void mouseDragged(MouseEvent e) {
                    handler.onMouseDragged(e.getX(), e.getY());
                }
            });
            frame.getContentPane().removeAll();
            frame.getContentPane().add(imageComponent);
            frame.revalidate();
            frame.repaint();
            (new Timer(Renderer.class.getSimpleName())).scheduleAtFixedRate(new Renderer(foregroundRaster, imageComponent), 0, 34);
            (new Thread(handler = new Handler(width, height))).start();
            imageComponent.setComponentPopupMenu((new ElseMenu(frame, handler, backgroundRaster)).menu);
        });
        frame.getContentPane().add(button);

        frame.setVisible(true);
        frame.setExtendedState(frame.getExtendedState() | MAXIMIZED_BOTH);
    }
}
