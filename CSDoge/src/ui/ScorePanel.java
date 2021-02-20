package ui;

import model.DogeGame;

import javax.swing.*;
import java.awt.*;

class ScorePanel extends JPanel {

    private static final int LABEL_W = 200;
    private static final int LABEL_H = 50;
    private DogeGame dogeGame;
    private JLabel scoreLabel;

    ScorePanel(DogeGame dogeGame) {
        this.dogeGame = dogeGame;
        setBackground(new Color(255, 255, 255));
        scoreLabel = new JLabel(dogeGame.getScore() + "", JLabel.CENTER);
        scoreLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 45));
        scoreLabel.setForeground(new Color(152, 166, 255));
        scoreLabel.setPreferredSize(new Dimension(LABEL_W, LABEL_H));
        add(scoreLabel);
        add(Box.createHorizontalStrut(10));
    }

    void update() {
        scoreLabel.setText(dogeGame.getScore() + "");
        repaint();
    }

}