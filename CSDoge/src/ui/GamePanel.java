package ui;


import model.DogeGame;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private static final String START = "Space to fly";
    private static final String OVER = "Game Over";
    private static final String REPLAY = "R to replay";
    private DogeGame dogeGame;
    private Image image;

    GamePanel(DogeGame dogeGame) {
        this.dogeGame = dogeGame;
        setPreferredSize(new Dimension(DogeGame.WIDTH, DogeGame.HEIGHT));
        setBackground(Color.WHITE);
        image = new ImageIcon(CSDoge.path + "data/image/CSDogeBackground.png").getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0,0, DogeGame.WIDTH, DogeGame.HEIGHT, null);
        drawGame(g);

        if (dogeGame.isGameNotStartYet()) {
            gameStart(g);
        } else if (dogeGame.isGameOver()) {
            gameOver(g);
        }
    }

    private void drawGame(Graphics g) {
        dogeGame.draw(g);
    }

    private void gameOver(Graphics g) {
        dogeGame.setGameOver(true);
        Color saved = g.getColor();
        g.setColor(new Color(0, 0, 0));
        g.setFont(new Font("Arial", Font.ITALIC, 20));
        FontMetrics fm = g.getFontMetrics();
        centreString(OVER, g, fm, DogeGame.HEIGHT / 2);
        centreString(REPLAY, g, fm, DogeGame.HEIGHT / 2 + 50);
        g.setColor(saved);
    }

    private void gameStart(Graphics g) {
        Color saved = g.getColor();
        g.setColor(new Color(0, 0, 0));
        g.setFont(new Font("Arial", Font.ITALIC, 20));
        FontMetrics fm = g.getFontMetrics();
        centreString(START, g, fm, DogeGame.HEIGHT / 2 + 50);
        g.setColor(saved);
    }

    private void centreString(String str, Graphics g, FontMetrics fm, int posY) {
        int width = fm.stringWidth(str);
        g.drawString(str, (DogeGame.WIDTH - width) / 2, posY);
    }
}

