package io.github.guiritter.bézier_drawer;

import io.github.guiritter.imagecomponent.ImageComponentMultiple;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import java.awt.image.WritableRaster;
import java.util.Timer;
import java.util.concurrent.Semaphore;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;

@SuppressWarnings("CallToPrintStackTrace")
public final class Edit {

    private BufferedImage backgroundColorImage;

    private WritableRaster backgroundColorRaster;

    private BufferedImage backgroundPictureImage;

    private WritableRaster backgroundPictureRaster;

    private BufferedImage foregroundImage;

    private WritableRaster foregroundRaster;

    final JFrame frame;

    private Handler handler;

    private int height;

    private final ImageComponentMultiple imageComponent;

    private final Point lastPoint = new Point(0, 0, new int[4], 0);

    private final Semaphore pointSelectedSemaphore = new Semaphore(0, true);

    private final WrapperPoint pointSelectedWrapper = new WrapperPoint();

    private Renderer renderer;

    private final Setup setup;

    private final Timer timer;

    private int width;

    public void showDialog(Exception ex, String title, int messageType) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(frame, ex.getLocalizedMessage(), title, messageType);
    }

    public void showError(Exception ex) {
        showDialog(ex, "error", ERROR_MESSAGE);
    }

    public void showWarning(Exception ex) {
        showDialog(ex, "warning", WARNING_MESSAGE);
    }

    public void setBackgroundColor() {
        imageComponent.images.set(0, backgroundColorImage);
        frame.revalidate();
        frame.repaint();
    }

    public void setBackgroundPicture(BufferedImage image) {
        imageComponent.images.set(0, image);
        frame.revalidate();
        frame.repaint();
    }

    public void setFramePeriod(long period) {
        if (renderer != null) {
            renderer.cancel();
        }
        renderer = new Renderer(foregroundRaster, imageComponent);
        timer.purge();
        timer.scheduleAtFixedRate(renderer, 0, period);
    }

    public Edit(Setup setup) {
        this.setup = setup;
        imageComponent = new ImageComponentMultiple();
        timer = new Timer(Renderer.class.getSimpleName());

        frame = new JFrame("Bézier Drawer · Edit");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton button = new JButton("initialize");
//        button.setFont(button.getFont().deriveFont(button.getFont().getStyle() | BOLD)); // TODO to be used elsewhere
        button.addActionListener((ActionEvent e) -> {

            width = button.getWidth();
            height = button.getHeight();
            backgroundColorImage = new BufferedImage(width, height, TYPE_INT_ARGB);
            backgroundColorRaster = backgroundColorImage.getRaster();
            foregroundImage = new BufferedImage(width, height, TYPE_INT_ARGB);
            foregroundRaster = foregroundImage.getRaster();
            imageComponent.images.add(backgroundColorImage);
            imageComponent.images.add(foregroundImage);
            imageComponent.update();
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

                @Override
                public void mouseMoved(MouseEvent e) {
                    lastPoint.x = e.getX();
                    lastPoint.y = e.getY();
                }
            });
            frame.getContentPane().removeAll();
            frame.getContentPane().add(imageComponent);
            frame.revalidate();
            frame.repaint();
            setFramePeriod(BézierDrawer.getFramePeriod());
            (new Thread(handler = new Handler(width, height, setup, backgroundColorRaster, pointSelectedSemaphore, pointSelectedWrapper))).start();
            imageComponent.setComponentPopupMenu((new Menu(width, height, this, setup, frame, handler, backgroundColorRaster, lastPoint, pointSelectedSemaphore, pointSelectedWrapper)).menu);
        });
        frame.getContentPane().add(button);

        frame.setVisible(true);
        frame.setExtendedState(frame.getExtendedState() | MAXIMIZED_BOTH);
    }
}
