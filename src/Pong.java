import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * A class that describes the game's graphical interface
 * @author Dmitriy Stepanov
 */
public class Pong extends JFrame {
    private final GameField gameField;
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 840;

    /**
     * Constructor - creating a new graphical interface
     * @see Pong#Pong()
     */
    public Pong() {
        setTitle("Pong");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Image windowIcon = null;
        try {
            windowIcon = ImageIO.read(Pong.class.getResource("/pong.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setIconImage(windowIcon);
        gameField = new GameField();
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        add(gameField);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) { new Pong(); }
}