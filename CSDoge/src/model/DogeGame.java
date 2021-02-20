package model;

import ui.CSDoge;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.awt.event.KeyEvent.VK_R;
import static java.awt.event.KeyEvent.VK_SPACE;
import static model.Spike.*;

public class DogeGame {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 600;
    private static final Random RNG = new Random();
    private List<Spike> spikes;
    private Doge doge;
    private boolean gameNotStartYet;
    private boolean isGameOver;
    private int score;
    private int difficulty;

    public DogeGame() {
        spikes = new ArrayList<>();
        gameNotStartYet = true;
        reset();
    }

    private void reset() {
        spikes.clear();
        spikes = new ArrayList<>();
        addDefaultSpikes();
        doge = new Doge();
        isGameOver = false;
        score = 0;
        difficulty = 1;
    }

    //EFFECT: draw the doge and the spikes onto graphics g
    public void draw(Graphics g) {
        doge.draw(g);
        for (Spike spike : spikes) {
            spike.draw(g);
        }
    }

    private void addDefaultSpikes() {
        for (int topbotX : TOPBOT_X) {
            spikes.add(new Spike(TOP, topbotX, TOP_Y));
            spikes.add(new Spike(BOTTOM, topbotX, BOTTOM_Y));
        }
    }

    //EFFECT: update difficulty, spikes, doge and check if game is over
    public void update() {
        refreshDifficulty();
        refreshSpikes();
        moveBird();
        checkGameOver();
    }

    //EFFECT: check what key is pressed, if Space is pressed during game time, play the fly sound and make doge fly
    //        if game has not start yet, set gameNotStartYet to false, if KeyCode is R, and game is over, reset the game
    public void keyPressed(int keyCode) {
        if (keyCode == VK_SPACE && !isGameOver && !gameNotStartYet) {
            playSound(CSDoge.path + "data/sound/fly.wav");
            doge.fly();
        } else if (keyCode == VK_R && isGameOver) {
            reset();
        } else if (keyCode == VK_SPACE && gameNotStartYet) {
            gameNotStartYet = false;
        }
    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public boolean isGameNotStartYet() {
        return gameNotStartYet;
    }

    private void moveBird() {
        doge.move();
    }

    private void refreshDifficulty() {
        if (difficulty <= LEFTRIGHT_Y.length) {
            difficulty = score / 5 + 1;
        }
    }

    private void refreshSpikes() {
        if (doge.isTurned() && (doge.getX() >= 25 && doge.getX() <= 325)) {
            refreshScore();
            int position = doge.getDirection() * -1;
            List<Spike> spikesToChange = new ArrayList<>();
            for (Spike spike : spikes) {
                if (spike.getPosition() == position) {
                    spikesToChange.add(spike);
                }
            }
            spikes.removeAll(spikesToChange);
            generateSpikes(position);
            doge.setTurned(false);
        }
    }

    private void generateSpikes(int position) {
        if (position == RIGHT || position == LEFT) {
            int posX = LEFT_X;
            if (position == RIGHT) {
                posX = RIGHT_X;
            }
            generateSpikesHelper(position, posX);
        }
    }

    private void generateSpikesHelper(int position, int posX) {
        int[] pos = new int[difficulty];
        for (int i = 0; i < difficulty; i++) {
            int temp;
            do {
                temp = RNG.nextInt(LEFTRIGHT_Y.length);
            } while (Arrays.asList(pos).contains(temp));
            pos[i] = temp;
        }
        Arrays.sort(pos);
        List<Spike> tempSpikes = new ArrayList<>();
        for (int j : pos) {
            tempSpikes.add(new Spike(position, posX, LEFTRIGHT_Y[j]));
        }
        spikes.addAll(leaveSpaceBetween(tempSpikes));
    }

    private List<Spike> leaveSpaceBetween(List<Spike> tempSpikes) {
        int tempPosY = LEFTRIGHT_Y[0];
        int index = -1;
        for (Spike spike: tempSpikes) {
            if (spike.getY() - tempPosY > 110) {
                return tempSpikes;
            }
            if (spike.getY() - tempPosY == 110) {
                index = tempSpikes.indexOf(spike);
            }
            tempPosY = spike.getY();
        }
        int lastPosY = LEFTRIGHT_Y[LEFTRIGHT_Y.length - 1];
        if (lastPosY == tempPosY) {
            if (index == -1) {
                tempSpikes.remove(RNG.nextInt(tempSpikes.size()) - 1);
            } else {
                tempSpikes.remove(index);
            }
        }
        return tempSpikes;
    }

    private void checkGameOver() {
        for (Spike spike : spikes) {
            if (spike.collideWith(doge)) {
                isGameOver = true;
            }
        }

//        if (isGameOver) {
//            reset();
//        }
    }

    // learned method from https://www.youtube.com/watch?v=QVrxiJyLTqU
    private void playSound(String path) {
        File sound = new File(path);
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            clip.start();
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    private void refreshScore() {
        score += 1;
    }
}