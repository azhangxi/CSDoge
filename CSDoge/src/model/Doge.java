package model;

import ui.CSDoge;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Doge {
    private static final double DX = 5;
    private static final double DEFAULT_DY = 5;
    private static final double DECELERATION = 0.4;
    static final int WIDTH = 36;
    static final int HEIGHT = 30;
    private static final int LEFT = -1;
    private static final int RIGHT = 1;
    private static final double FLY_FACTOR = -2.0;

    private static double DY;

    private int posX;
    private int posY;
    private int direction;
    private boolean turned;
    private BufferedImage leftBird;
    private BufferedImage rightBird;

    Doge() {
        posX = DogeGame.WIDTH / 2 - WIDTH / 2;
        posY = DogeGame.HEIGHT / 2 - HEIGHT / 2;
        DY = FLY_FACTOR * DEFAULT_DY;
        direction = RIGHT;
        turned = false;
        initBirdImages();
    }

    private void changeDirection() {
        direction *= -1;
        turned = true;
    }

    //MODIFY: this
    //EFFECT: change the doge to a new position depending on default speed on X direction, and Deceleration
    //        and changing speed on Y direction.
    void move() {
        posX = (int) (posX + direction * DX);
        if (DY != DEFAULT_DY) {
            DY += DECELERATION;
        }
        posY = posY + (int) DY;
        handleBoundary();
    }

    //MODIFY:
    //EFFECT: set the velocity on Y direction to maximum upwards.
    void fly() {
        DY = DEFAULT_DY * FLY_FACTOR;
    }

    void draw(Graphics g) {
        if (direction == RIGHT) {
            g.drawImage(rightBird, posX, posY, WIDTH, HEIGHT, null);
        } else if (direction == LEFT) {
            g.drawImage(leftBird, posX, posY, WIDTH, HEIGHT, null);
        }
    }

    private void handleBoundary() {
        if (posX <= 0 || posX >= DogeGame.WIDTH - WIDTH) {
            changeDirection();
        }
    }

    private void initBirdImages() {
        File leftBirdFile = new File(CSDoge.path + "data/image/dogLeft.png");
        File rightBirdFile = new File(CSDoge.path + "data/image/dogRight.png");
        try {
            leftBird = ImageIO.read(leftBirdFile);
            rightBird = ImageIO.read(rightBirdFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean isTurned() {
        return turned;
    }

    void setTurned(boolean turned) {
        this.turned = turned;
    }

    int getDirection() {
        return direction;
    }

    int getX() {
        return posX;
    }

    int getY() {
        return posY;
    }
}

