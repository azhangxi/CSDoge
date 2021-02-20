package model;

import java.awt.*;
import java.awt.geom.Area;

public class Spike {
    static final int TOP_Y = 0;
    static final int BOTTOM_Y = 600;
    static final int RIGHT_X = 400;
    static final int LEFT_X = 0;
    static final int TOP = 2;
    static final int BOTTOM = -2;
    static final int RIGHT = 1;
    static final int LEFT = -1;

    static final int[] TOPBOT_X = {10, 65, 120, 175, 230, 285, 340};
    //    public static final int[] LEFTRIGHT_Y = {60, 115, 170, 225, 280, 335, 390, 445, 500, 555, 610, 665, 720};
    static final int[] LEFTRIGHT_Y = {60, 115, 170, 225, 280, 335, 390, 445, 500};
    private static final int WIDTH = 50;
    private static final int HEIGHT = 25;

    private static final Color COLOR = new Color(255, 4, 5);

    private int posX;
    private int posY;
    private int position;
    private int[] points = new int[4];

    Spike(int position, int posX, int posY) {
        this.position = position;
        this.posX = posX;
        this.posY = posY;
    }

    void draw(Graphics g) {
        Color savedColor = g.getColor();
        g.setColor(COLOR);
        g.fillPolygon(makeSpike());
//        g.drawPolygon(makeSpike(posX, posY));
        g.setColor(savedColor);
    }

    private Polygon makeSpike() {
        initPoints();
        //top case
        int[] xs = {posX, points[0], points[1]};
        int[] ys = {posY, points[2], points[3]};
        return new Polygon(xs, ys, 3);
    }

    //MODIFY: this
    //EFFECT: Depending on the position of the spike, initialize the second and third points
    //        of a triangle.
    private void initPoints() {
        if (position == TOP || position == BOTTOM) {
            points[0] = posX + WIDTH;
            points[1] = posX + HEIGHT;
            points[2] = posY;
            points[3] = posY + HEIGHT;
            if (position == BOTTOM) {
                points[3] = posY - HEIGHT;
            }
        } else if (position == RIGHT || position == LEFT) {
            points[0] = posX;
            points[1] = posX - HEIGHT;
            points[2] = posY + WIDTH;
            points[3] = posY + HEIGHT;
            if (position == LEFT) {
                points[1] = posX + HEIGHT;
            }
        }
    }

    boolean collideWith(Doge doge) {
        Rectangle birdBoundary = new Rectangle(doge.getX(), doge.getY(), Doge.WIDTH, Doge.HEIGHT);
        Polygon spike = makeSpike();
        Area area = new Area(spike);
        area.intersect(new Area(birdBoundary));

        return !area.isEmpty();
    }

    int getY() {
        return posY;
    }

    int getPosition() {
        return position;
    }
}

