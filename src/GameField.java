import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A class describing the playing field for the game
 *
 * @author Dmitriy Stepanov
 */
public class GameField extends JPanel implements ActionListener {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 840;
    public static final int GAME_SPEED = 60;
    public TextureLoader textureLoader;
    public Menu menu;
    public Game game;
    public Graphics g;
    private final Keyboard k;
    private final Timer timer = new Timer(1000 / GAME_SPEED, this);
    public int gameState = 0;

    /**
     * Constructor - creating a new playing field
     *
     * @see GameField#GameField()
     */
    public GameField() {
        textureLoader = new TextureLoader();
        menu = new Menu(this);
        game = new Game(false, this);
        k = new Keyboard();
        addKeyListener(k);
        timer.start();
        setFocusable(true);
    }

    public void mainProcess() {
        k.updateKeys();
        if (gameState == 0) {
            menu.menuProcess();
        } else if (gameState == 1) {
            game.gameProcess();
            if (!game.isPaused) {
                if (!game.multiplayer) {
                    game.useAI();
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        this.g = g;
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        if (gameState == 0) {
            menu.drawMenu();
        } else if (gameState == 1) {
            game.drawGame();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainProcess();
        repaint();
    }

    private class Keyboard extends KeyAdapter implements KeyListener {
        boolean[] key = new boolean[255];
        private boolean keyIsAlreadyPressed = false;

        @Override
        public void keyPressed(KeyEvent e) {
            key[e.getKeyCode()] = true;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            key[e.getKeyCode()] = false;
            keyIsAlreadyPressed = false;
        }

        public void updateKeys() {
            if (gameState == 0) {

                if (key[KeyEvent.VK_UP] & !keyIsAlreadyPressed) {
                    if (menu.selection > menu.selectionMin) {
                        System.out.println(menu.selection + " " + menu.selectionsCount);
                    }

                    menu.selectionUp();
                    keyIsAlreadyPressed = true;
                } else if (key[KeyEvent.VK_DOWN] & !keyIsAlreadyPressed) {
                    menu.selectionDown();
                    keyIsAlreadyPressed = true;
                }

                if (key[KeyEvent.VK_SPACE] & !keyIsAlreadyPressed) {
                    keyIsAlreadyPressed = true;
                    menu.enter();
                }

                if (key[KeyEvent.VK_ENTER] & !keyIsAlreadyPressed) {
                    keyIsAlreadyPressed = true;
                    menu.enter();
                }
            } else if (gameState == 1) {
                if (key[KeyEvent.VK_ESCAPE] & !keyIsAlreadyPressed) {
                    keyIsAlreadyPressed = true;
                    game.isPaused = !game.isPaused;
                    game.subState = 0;

                    if (game.isPaused) {
                        game.selection = 0;
                        game.setAnimationMoment(0);
                    }
                }
                if (!game.isPaused) {
                    if (key[KeyEvent.VK_SPACE] & !keyIsAlreadyPressed) {
                        keyIsAlreadyPressed = true;

                        if (gameState == 1) game.launchBall();
                    }

                    if (game.multiplayer) {
                        if (key[KeyEvent.VK_UP]) {
                            game.movePlayerUp(1);
                        }

                        if (key[KeyEvent.VK_DOWN]) {
                            game.movePlayerDown(1);
                        }

                        if (key[KeyEvent.VK_W]) {
                            game.movePlayerUp(0);
                        }

                        if (key[KeyEvent.VK_S]) {
                            game.movePlayerDown(0);
                        }
                    } else {
                        if (key[KeyEvent.VK_W] || key[KeyEvent.VK_UP]) {
                            game.movePlayerUp(0);
                        }
                        if (key[KeyEvent.VK_S] || key[KeyEvent.VK_DOWN]) {
                            game.movePlayerDown(0);
                        }
                    }
                } else {
                    if (key[KeyEvent.VK_UP] & !keyIsAlreadyPressed) {
                        keyIsAlreadyPressed = true;
                        game.selection--;
                    }

                    if (key[KeyEvent.VK_DOWN] & !keyIsAlreadyPressed) {
                        keyIsAlreadyPressed = true;
                        game.selection++;
                    }

                    if (key[KeyEvent.VK_SPACE] & !keyIsAlreadyPressed) {
                        keyIsAlreadyPressed = true;
                        game.pauseEnter();
                    }

                    if (key[KeyEvent.VK_ENTER] & !keyIsAlreadyPressed) {
                        keyIsAlreadyPressed = true;
                        game.pauseEnter();
                    }
                }
            }
        }
    }
}