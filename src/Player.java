/**
 * A class that describes the player
 * @author Dmitriy Stepanov
 */
public class Player {
    private int x;
    private int y;
    public static final int WIDTH = 12;
    public static final int HEIGHT = 72;
    public static final int INDENT = 48;
    public static final double MOVESPEED = 14;
    private final int ID;

    /**
     * Constructor - creating a new player
     * @param ID - ID player
     * @see Player#Player(int)
     */
    public Player(int ID) {
        this.ID = ID;    
    }
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }

    public void moveUp() {
        this.y -= MOVESPEED;
        if(this.y < 16) this.y = 16;
    }

    public void moveDown() {
        this.y += MOVESPEED;
        if(this.y > GameField.HEIGHT - HEIGHT - 40) this.y = GameField.HEIGHT - HEIGHT - 40;
    }

    public void setX(double x) {
        this.x = (int)x;
    }
    public void setY(double y) {
        this.y = (int)y;
    }
    
    public void setStartPosition(int ID) {
        if(this.ID == 0) {
            this.x = INDENT;
        } else if(this.ID == 1) {
            this.x = GameField.WIDTH - WIDTH - INDENT;
        }
        this.y = (GameField.HEIGHT - HEIGHT) / 2;
    }
}