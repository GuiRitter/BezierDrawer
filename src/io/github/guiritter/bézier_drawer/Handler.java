package io.github.guiritter.bézier_drawer;

import static io.github.guiritter.bézier_drawer.Event.Type.BACKGROUND_COLOR;
import static io.github.guiritter.bézier_drawer.Event.Type.CLICKED;
import static io.github.guiritter.bézier_drawer.Event.Type.DRAGGED;
import static io.github.guiritter.bézier_drawer.Event.Type.PRESSED;
import static io.github.guiritter.bézier_drawer.Event.Type.PRESSED_WAIT;
import static io.github.guiritter.bézier_drawer.Event.Type.RELEASED;
import java.awt.image.WritableRaster;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public final class Handler implements Runnable{

    private final WritableRaster backgroundColorRaster;

    private final LinkedList<Point> BézierControlPointList = new LinkedList();

    public int color[] = new int[4];

    private Event event;

    private final LinkedList<Event> eventList = new LinkedList<>();

    private final int height;

    private Point pointSelected = null;

    private final Semaphore pointSelectedSemaphore;

    private final WrapperPoint pointSelectedWrapper;

    private final Semaphore semaphore = new Semaphore(0, true);

    private final Setup setup;

    private final int width;

    private int x;

    private int y;

    void onBackgroundColorChanged(int color[]) {
        eventList.add(new Event(BACKGROUND_COLOR, color));
        semaphore.release();
    }

    void onMouseClicked(int x, int y) {
        eventList.add(new Event(CLICKED, x, y));
        semaphore.release();
    }

    void onMouseClicked(int x, int y, int i) {
        eventList.add(new Event(CLICKED, x, y, i));
        semaphore.release();
    }

    void onMouseDragged(int x, int y) {
        eventList.add(new Event(DRAGGED, x, y));
        semaphore.release();
    }

    void onMousePressed(int x, int y) {
        eventList.add(new Event(PRESSED, x, y));
        semaphore.release();
    }

    void onMousePressedWait(int x, int y) {
        eventList.add(new Event(PRESSED_WAIT, x, y));
        semaphore.release();
    }

    void onMouseReleased() {
        eventList.add(new Event(RELEASED, 0, 0));
        semaphore.release();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            semaphore.acquireUninterruptibly();
            event = eventList.pollFirst();
            if (event == null) {
                continue;
            }
            switch (event.type) {
                case BACKGROUND_COLOR:
                    color = event.color;
                    for (y = 0; y < height; y++) {
                        for (x = 0; x < width; x++) {
                            backgroundColorRaster.setPixel(x, y, color);
                        }
                    }
                    break;
                case CLICKED:
                    if (event.i > -1) {
                        BézierDrawer.addPoint(event.x, event.y, event.i);
                    } else {
                        BézierDrawer.addPoint(event.x, event.y);
                    }
                    break;
                case DRAGGED:
                    if (pointSelected == null) {
                        break;
                    }
                    x = event.x;
                    y = event.y;
                    if ((x < 0) || (x >= width) || (y < 0) || (y >= height)) {
                        break;
                    }
                    pointSelected.x = x;
                    pointSelected.y = y;
                    setup.setPoint(pointSelectedWrapper.index, x, y);
                    break;
                case PRESSED:
                case PRESSED_WAIT:
                    BézierDrawer.getPointList(BézierControlPointList);
                    pointSelected = null;
                    for (Point point : BézierControlPointList) {
                        if (point.isIn(event.x, event.y)) {
                            pointSelected = point;
                            break;
                        }
                    }
                    if (pointSelected != null) {
                        pointSelectedWrapper.index = BézierControlPointList.indexOf(pointSelected);
                    }
                    if (event.type == PRESSED_WAIT) {
                        pointSelectedWrapper.value = pointSelected;
                        pointSelectedSemaphore.release();
                    }
                    break;
                case RELEASED:
                    pointSelected = null;
                    break;
            }
        }
    }

    public Handler(
     int width,
     int height,
     Setup setup,
     WritableRaster backgroundColorRaster,
     Semaphore pointSelectedSemaphore,
     WrapperPoint pointSelectedWrapper
    ) {
        this.width = width;
        this.height = height;
        this.setup = setup;
        this.backgroundColorRaster = backgroundColorRaster;
        this.pointSelectedSemaphore = pointSelectedSemaphore;
        this.pointSelectedWrapper = pointSelectedWrapper;
    }
}
