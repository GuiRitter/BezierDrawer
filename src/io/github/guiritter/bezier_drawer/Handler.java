package io.github.guiritter.bezier_drawer;

import static io.github.guiritter.bezier_drawer.Event.Type.CLICKED;
import static io.github.guiritter.bezier_drawer.Event.Type.DRAGGED;
import static io.github.guiritter.bezier_drawer.Event.Type.PRESSED;
import static io.github.guiritter.bezier_drawer.Event.Type.RELEASED;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public final class Handler implements Runnable{

    private final LinkedList<Point> BézierControlPointList = new LinkedList();

    private Event event;

    private final LinkedList<Event> eventList = new LinkedList<>();

    private final int height;

    private Point pointSelected = null;

    private final Semaphore semaphore = new Semaphore(0, true);

    private final int width;

    private int x;

    private int y;

    void clicked(int x, int y) {
        eventList.add(new Event(CLICKED, x, y));
        semaphore.release();
    }

    void dragged(int x, int y) {
        eventList.add(new Event(DRAGGED, x, y));
        semaphore.release();
    }

    void pressed(int x, int y) {
        eventList.add(new Event(PRESSED, x, y));
        semaphore.release();
    }

    void released() {
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
                case CLICKED:
                    BézierDrawer.addPoint(event.x, event.y);
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
                    break;
                case PRESSED:
                    BézierDrawer.getPointList(BézierControlPointList);
                    pointSelected = null;
                    for (Point point : BézierControlPointList) {
                        if (point.isIn(event.x, event.y)) {
                            pointSelected = point;
                            break;
                        }
                    }
                    if (pointSelected == null) {
                        break;
                    }
                    break;
                case RELEASED:
                    pointSelected = null;
                    break;
            }
        }
    }

    public Handler(int width, int height) {
        this.height = height;
        this.width = width;
    }
}
