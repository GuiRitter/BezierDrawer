package io.github.guiritter.bezier_drawer;

import io.github.guiritter.imagecomponent.ImageComponent;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import java.awt.image.WritableRaster;
import java.util.Timer;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public final class EditFrame {

    private Handler handler;

    private int height;

    private BufferedImage image;

    private ImageComponent imageComponent;

    private final JFrame frame;

    private WritableRaster raster;

    private int width;

    public EditFrame() {
        frame = new JFrame("Bézier Drawer · Edit");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton button = new JButton("initialize");
        button.addActionListener((ActionEvent e) -> {

            width = button.getWidth();
            height = button.getHeight();
            image = new BufferedImage(width, height, TYPE_INT_ARGB);
            raster = image.getRaster();
            imageComponent = new ImageComponent(image);
            imageComponent.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    handler.clicked(e.getX(), e.getY());
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    handler.pressed(e.getX(), e.getY());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    handler.released();
                }
            });
            imageComponent.addMouseMotionListener(new MouseMotionAdapter() {

                @Override
                public void mouseDragged(MouseEvent e) {
                    handler.dragged(e.getX(), e.getY());
                }
            });
            frame.getContentPane().removeAll();
            frame.getContentPane().add(imageComponent);
            frame.revalidate();
            frame.repaint();
            (new Timer(Renderer.class.getSimpleName())).scheduleAtFixedRate(new Renderer(raster, imageComponent), 0, 34);
            (new Thread(handler = new Handler(width, height))).start();
        });
        frame.getContentPane().add(button);

        frame.setVisible(true);
        frame.setExtendedState(frame.getExtendedState() | MAXIMIZED_BOTH);
    }
}
