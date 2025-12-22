import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel {
    private final Timer timer;
    private boolean gameOver = false;
    private final Random rand = new Random();
    private long startTime;

    // Player Properties
    private int playerW, playerH, playerX, playerY;
    private int speed;

    // Falling Block Properties
    private int blockSize;
    private final ArrayList<Rectangle> blocks;
    private int spawnTimer = 0;

    public GamePanel() {
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        blocks = new ArrayList<>();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateWindowSize();
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                movePlayer(e.getKeyCode());
            }
        });

        timer = new Timer(8, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGame();
            }
        });
        timer.start();
        startTime = System.currentTimeMillis();
    }

    private void updateWindowSize() {
        int width = getWidth();
        int height = getHeight();
        playerW = width / 12;
        playerH = playerW;
        playerX = width / 2 - playerW / 2;
        playerY = height - playerH - 10;
        speed = width / 50;
        blockSize = width / 20;
    }

    private void movePlayer(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT -> {
                playerX -= speed;
                repaint();  // clears screen and calls paint component
            }
            case KeyEvent.VK_RIGHT -> {
                playerX += speed;
                repaint();
            }
        }
        playerX = Math.max(0, Math.min(playerX, getWidth() - playerW)); // ensures player stays in game window

        if (gameOver && keyCode == KeyEvent.VK_R) {
            resetGame();    // reset game if 'r' is pressed
        }
    }

    private void updateGame() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        if (playerW == 0) {
            updateWindowSize();
        }

        Rectangle player = new Rectangle(playerX, playerY, playerW, playerH);

        if (blockSize > 0) {   // ensures window is sized so blocks gets size value
            if (spawnTimer >= 30) {  // adds blocks every x frames
                int blockX = rand.nextInt(panelWidth - blockSize);
                blocks.add(new Rectangle(blockX, 0, blockSize, blockSize));
                spawnTimer = 0;
            }
        }
        spawnTimer++;

        for (int i = blocks.size() - 1; i >= 0; i--) {
            Rectangle block = blocks.get(i);

            // Detect collisions
            if (player.intersects(block)) {
                System.out.println("HIT");
                gameOver = true;
                timer.stop();

            }
            block.y += 5;
            if (block.y > panelHeight) {
                blocks.remove(i);
            }
        }
        repaint();
    }

    private void resetGame() {
        int width = getWidth();
        playerX = width / 2 - playerW / 2;
        gameOver = false;
        blocks.clear(); // delete blocks after reset
        timer.start();
        startTime = System.currentTimeMillis();  // Reset to current time
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();

        if (playerW == 0) {
            updateWindowSize();
        }

        // Draw player
        g.setColor(Color.GREEN);
        g.fillRect(playerX, playerY, playerW, playerH);
        // Draw blocks
        g.setColor(Color.RED);
        for (Rectangle block : blocks) {
            g.fillRect(block.x, block.y, blockSize, blockSize);
        }
        // Draw Timer
        g.setColor(Color.WHITE);
        int timerFontSize = width / 40; // Scale font size with window size
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, timerFontSize));
        double seconds = (System.currentTimeMillis() - startTime) / 1000.0;
        String timerText = String.format("Time: %.1f", seconds);
        g.drawString(timerText, 30, 40);


        if (gameOver) {
            int titleFontSize = width / 20;
            int subtitleFontSize = width / 40;
            g.setColor(Color.WHITE);

            // Center text
            g.setFont(new Font("Comic Sans MS", Font.BOLD, titleFontSize));
            FontMetrics fm = g.getFontMetrics();    // get information about the text
            String gameOverText = "GAME OVER";
            int textWidth = fm.stringWidth(gameOverText);   // get the width of text
            g.drawString(gameOverText, width / 2 - textWidth / 2, height / 2);  // draw text

            g.setFont(new Font("Comic Sans MS", Font.PLAIN, subtitleFontSize));
            fm = g.getFontMetrics();
            String restartText = "Press 'R' to restart";
            textWidth = fm.stringWidth(restartText);
            g.drawString(restartText, width / 2 - textWidth / 2, height / 2 + titleFontSize);
        }
    }
}
