package ui;

import model.DogeGame;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

/**************************************************************************
 *
 * Copyright (C) CS Doge - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Xi(Albert) Zhang [albert0223@icloud.com], 2019 [Nov]
 *
 **************************************************************************/

/* This Game contains a lot of ideas from SpaceInvaders and a lot of codes are identical.
 * Contact me if there is any infringement and I will remove that part.
 * idea of this game is inspired by "Don't Touch the Spikes".
 * This code can not be used for commercial purpose.
 */
public class CSDoge extends JFrame {
    public static String path;
    private static final int INTERVAL = 10;
    private DogeGame dogeGame;
    private GamePanel gamePanel;
    private ScorePanel scorePanel;
    private Timer timer;
    private Clip backgroundSound;
    private Clip dyingSound;
    private boolean clipPlayed = false;

    private CSDoge() {
        super("CS Doge");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
//        setUndecorated(true);
        addKeyListener(new KeyHandler());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                backgroundSound.close();
                dyingSound.close();
                timer.stop();
                System.gc();
            }
        });
        startGame();
    }

    private void startGame() {
        dogeGame = new DogeGame();
        gamePanel = new GamePanel(dogeGame);
        scorePanel = new ScorePanel(dogeGame);
        add(gamePanel);
        add(scorePanel, BorderLayout.NORTH);
        pack();
        centreOnScreen();
        setVisible(true);
        initSounds();
        addTimer();
        timer.start();
    }

    private void addTimer() {
        timer = new Timer(INTERVAL, e -> {
            if (!dogeGame.isGameOver() && !dogeGame.isGameNotStartYet()) {
                timerFunctionWhilePlaying();
            }
            if (dogeGame.isGameOver()) {
                timerFunctionWhenDead();
            }
        });
    }

    private void timerFunctionWhilePlaying() {
        dyingSound.close();
        clipPlayed = false;
        if (!backgroundSound.isRunning()) {
            initSounds();
            backgroundSound.start();
        }
        dogeGame.update();
        gamePanel.repaint();
        scorePanel.update();
    }

    private void timerFunctionWhenDead() {
        backgroundSound.close();
        if (!dyingSound.isRunning() && !clipPlayed) {
            initSounds();
            dyingSound.start();
            clipPlayed = true;
        }
    }

    private void centreOnScreen() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
    }

    private class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            dogeGame.keyPressed(e.getKeyCode());
        }
    }

    private void initSounds() {
        File bgSoundFile = new File(path + "data/sound/backgroundSound.wav");
        File dyingSoundFile = new File(path + "data/sound/dyingSound.wav");
        try {
            backgroundSound = AudioSystem.getClip();
            backgroundSound.open(AudioSystem.getAudioInputStream(bgSoundFile));
            dyingSound = AudioSystem.getClip();
            dyingSound.open(AudioSystem.getAudioInputStream(dyingSoundFile));
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        path = new File("").getAbsolutePath() + "/";
        new CSDoge();
    }

}
