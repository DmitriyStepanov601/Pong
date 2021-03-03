import java.awt.*;

/**
 * A class that describes game logic
 * @author Dmitriy Stepanov
 */
public class Game {
    private final GameField m;
    private final TextureLoader textureLoader;
    private final Menu menu;
    byte ID_gameIsPaused, ID_continue, ID_exit, ID_difficulty, ID_easy, ID_normal, ID_hardcore, ID_hacker;
    public final Player player1;
    public final Player player2;
    public final Ball ball;
    public final AI AI;
    public boolean multiplayer;
    public boolean isPaused;
    private final int selectionRegionSizeK = 7;
    public byte selection = 0;                   //0 — continue, 1 — exit
    public byte selectionsCount = 2;
    public byte subState = 0;                  //0 — main pause menu, 1 — difficulty menu
    private int selectionTransparency = 0;
    private double animationMoment = 0.0d;
    private int selectorY;
    private int player1_streak = 0;
    private int player2_streak = 0;
    private static final double appearDelay = 1.46d;
    double pauseReducingTransparency = 1;
    private double transparencyTime = 0;

    /**
     * Constructor - creating a new game logic
     * @param multiplayer - 2-player mode available
     * @param m - playing field
     * @see Game#Game(boolean,GameField)
     */
    public Game(boolean multiplayer, GameField m) {
        this.m = m;
        this.multiplayer = multiplayer;
        this.ball = new Ball();
        this.textureLoader = m.textureLoader;
        this.menu = m.menu;
        this.AI = new AI();

        ID_gameIsPaused = textureLoader.getID("gameispaused");
        ID_continue = textureLoader.getID("continue");
        ID_exit = textureLoader.getID("exit");
        ID_difficulty = textureLoader.getID("difficulty");
        ID_easy = textureLoader.getID("easy");
        ID_normal = textureLoader.getID("normal");
        ID_hardcore = textureLoader.getID("hardcore");
        ID_hacker = textureLoader.getID("hacker");

        player1 = new Player(0);
        player2 = new Player(1);
    }
    
    public void startGame() {
        this.setStartBallPosition((GameField.WIDTH - this.ball.getSize()) / 2,
                (GameField.HEIGHT - this.ball.getSize()) / 2);
        player1.setStartPosition(0);
        player2.setStartPosition(1);
    }
    
    public void gameProcess() {
        if(!isPaused) {
            if(transparencyTime <= 0) {
                transparencyTime = 0;
            }

            transparencyTime--;
            ball.updatePosition();
            this.ball.animationMoment += 0.188d;

            if(this.ball.animationMoment > 310000000000d) this.ball.animationMoment = 0;
            this.ball.transparency = Math.abs((int)(Math.sin(this.ball.animationMoment) *
                    (this.ball.MAX_ANIMATION_ALPHA - this.ball.MIN_ANIMATION_ALPHA) +  this.ball.MIN_ANIMATION_ALPHA));

            if(this.ball.getY() < 16) {
                this.ball.setY(16);
                this.ball.setYSpeed((this.ball.getYSpeed() - ball.ballAccelerationY * Math.random()) * -1);
            } else if(this.ball.getY() > GameField.HEIGHT - 40) {
                this.ball.setY(GameField.HEIGHT - 40);
                this.ball.setYSpeed((this.ball.getYSpeed() + ball.ballAccelerationY * Math.random()) * -1);
            }

            if(((ball.getX() < Player.INDENT + Player.WIDTH + ball.getSize() / 2) & (ball.getY() >
                    (player1.getY() - ball.getSize())) & (ball.getY() < player1.getY() + Player.HEIGHT +
                    ball.getSize())) & ball.getX() > Player.INDENT) {
                ball.bounceToRight();
            }

            if(((ball.getX() > GameField.WIDTH - Player.INDENT - Player.WIDTH - ball.getSize() / 2) &
                    (ball.getY() > (player2.getY() - ball.getSize())) & (ball.getY() < player2.getY() + Player.HEIGHT
                    + ball.getSize())) & ball.getX() < GameField.WIDTH - Player.INDENT) {
                ball.bounceToLeft();
            }

            if(ball.getX() < 0 - appearDelay * Math.abs(ball.getXSpeed()) * GameField.GAME_SPEED) {
                ball.setX((GameField.WIDTH - this.ball.getSize()) / 2);
                ball.setY((GameField.HEIGHT - this.ball.getSize()) / 2);
                ball.isLaunched = false;
                player2_streak++;
            }

            if(ball.getX() > GameField.WIDTH + appearDelay * Math.abs(ball.getXSpeed())
                    * GameField.GAME_SPEED) {
                ball.setX((GameField.WIDTH - this.ball.getSize()) / 2);
                ball.setY((GameField.HEIGHT - this.ball.getSize()) / 2);
                ball.isLaunched = false;
                player1_streak++;
            }
        } else {
            if(this.selection < 0) selection = 0;

            if(this.selection > (byte)(selectionsCount - 1)) selection = (byte)(selectionsCount - 1);
             switch(selection) {
                case(0) :
                    selectorY = GameField.HEIGHT / selectionRegionSizeK * (selectionRegionSizeK / 3) - 6;
                    break;
                case(1) :
                    selectorY = GameField.HEIGHT / selectionRegionSizeK * (1 +
                            (selectionRegionSizeK / 3)) - 6;
                    break;
                case(2) :
                    selectorY = GameField.HEIGHT / selectionRegionSizeK * (2 +
                            (selectionRegionSizeK / 3)) - 6;
                    break;
                case(3) :
                    selectorY = GameField.HEIGHT / selectionRegionSizeK * (3 +
                            (selectionRegionSizeK / 3)) - 6;
                    break;
        }

        animationMoment += 0.088d;
        if(animationMoment > 310000000000d) animationMoment = 0;
            this.selectionTransparency = Math.abs((int)(Math.sin(animationMoment) * menu.MAX_ANIMATION_ALPHA));
        }
    }
    
    public void launchBall() {
        if(!ball.isLaunched) {
            ball.isLaunched = true;
            double dir = Math.random() * 4;

            if(dir < 1) {
                ball.setXSpeed(Math.random() * 8 + 6);
                ball.setYSpeed(Math.random() * 7 + 3);
            } else if (dir >= 1 & dir < 2) {
                ball.setXSpeed(-1 * (Math.random() * 8 + 6));
                ball.setYSpeed(Math.random() * 7 + 3);
            } else if (dir >= 2 & dir < 3) {
                ball.setXSpeed(Math.random() * 8 + 6);
                ball.setYSpeed(-1 * (Math.random() * 7 + 3));                
            } else {
                ball.setXSpeed(-1 * (Math.random() * 8 + 6));
                ball.setYSpeed(-1 * (Math.random() * 7 + 3));                
            }
        }
    }
    
    private void setStartBallPosition(int x, int y) {
        this.ball.setX(x);
        this.ball.setY(y);
    }
    
    public void movePlayerUp(int ID) {
        if(ID == 0) {
            this.player1.moveUp();
        } else if(ID == 1) {
            this.player2.moveUp();
        }
    }
    public void movePlayerDown(int ID) {
        if(ID == 0) {
            this.player1.moveDown();
        } else if(ID == 1) {
            this.player2.moveDown();
        }        
    }
    
    public void setAnimationMoment(double x) {
        this.animationMoment = x;
    }
    
    public void drawGame() {
        if(isPaused) {
            pauseReducingTransparency = 0.25d;
        } else {
            pauseReducingTransparency = 1.0d;
        }

        m.g.setColor(new Color(255, 255, 255));
        m.g.fillOval(ball.getX() - ball.getSize() / 2, ball.getY() - ball.getSize() / 2, ball.getSize(),
                ball.getSize());
        m.g.setColor(new Color(255, 255, 255, (int)(240 * pauseReducingTransparency)));
        m.g.fillRect(player1.getX(), player1.getY(), Player.WIDTH, Player.HEIGHT);
        m.g.setFont(new Font("Arial", Font.BOLD, 30));
        m.g.drawString(player1_streak + " : " + player2_streak, 600 , 50);

        if(!multiplayer) {
            switch(AI.getDifficulty()) {
                case "easy":
                    m.g.setColor(new Color(100, 255, 255, (int)(240 * pauseReducingTransparency)));
                    break;
                case "normal":
                    m.g.setColor(new Color(47, 79, 79, (int)(240 * pauseReducingTransparency)));
                    break;
                case "hardcore":
                    m.g.setColor(new Color(255, 200, 50, (int)(240 * pauseReducingTransparency)));
                    break;
                case "hacker":
                    m.g.setColor(new Color(255, 25, 0, (int)(240 * pauseReducingTransparency)));
                    break;
            }
        } else {
            m.g.setColor(new Color(255, 255, 255, (int)(240 * pauseReducingTransparency)));
        }

        m.g.fillRect(player2.getX(), player2.getY(), Player.WIDTH, Player.HEIGHT);

        if(isPaused) {
            Texture tex;
            tex = textureLoader.texList.get(ID_gameIsPaused);
            m.g.drawImage(textureLoader.texMenu, (GameField.WIDTH - tex.getWidth()) / 2,
                    GameField.HEIGHT - tex.getHeight() - 32,
                    (GameField.WIDTH - tex.getWidth()) / 2 + tex.getWidth(),
                    GameField.HEIGHT - 32, tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
            m.g.setColor(new Color(220, 0, 0, selectionTransparency));
            m.g.fillRect(0, selectorY, 1280, 44);
            int position = 0;

            if(subState == 0) {
                if(multiplayer) {
                    selectionsCount = 2;   
                } else {
                    selectionsCount = 3;
                }

                tex = textureLoader.texList.get(ID_continue);
                m.g.drawImage(textureLoader.texMenu, (GameField.WIDTH - tex.getWidth()) / 2,
                        GameField.HEIGHT / selectionRegionSizeK * (position +
                                (selectionRegionSizeK / 3)), (GameField.WIDTH - tex.getWidth()) / 2
                                + tex.getWidth(), GameField.HEIGHT / selectionRegionSizeK *
                                (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(),
                        tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
                position = 1;

                if(!multiplayer) {
                    tex = textureLoader.texList.get(ID_difficulty);
                    m.g.drawImage(textureLoader.texMenu, (GameField.WIDTH - tex.getWidth()) / 2,
                            GameField.HEIGHT / selectionRegionSizeK * (position +
                                    (selectionRegionSizeK / 3)), (GameField.WIDTH - tex.getWidth()) / 2
                                    + tex.getWidth(), GameField.HEIGHT / selectionRegionSizeK *
                                    (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(),
                            tex.getSY1(), tex.getSX2(), tex.getSY2(), m);

                    position = 2;

                    tex = textureLoader.texList.get(ID_exit);
                    m.g.drawImage(textureLoader.texMenu, (GameField.WIDTH - tex.getWidth()) / 2,
                            GameField.HEIGHT / selectionRegionSizeK * (position +
                                    (selectionRegionSizeK / 3)), (GameField.WIDTH - tex.getWidth()) / 2
                                    + tex.getWidth(), GameField.HEIGHT / selectionRegionSizeK *
                                    (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(),
                            tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
                } else {
                    tex = textureLoader.texList.get(ID_exit);  
                    m.g.drawImage(textureLoader.texMenu, (GameField.WIDTH - tex.getWidth()) / 2,
                            GameField.HEIGHT / selectionRegionSizeK * (position +
                                    (selectionRegionSizeK / 3)), (GameField.WIDTH - tex.getWidth()) / 2
                                    + tex.getWidth(), GameField.HEIGHT / selectionRegionSizeK *
                                    (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(),
                            tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
                }
            } else if(subState == 1) {
                selectionsCount = 4;
                position = 0;

                tex = textureLoader.texList.get(ID_easy);
                m.g.drawImage(textureLoader.texMenu, (GameField.WIDTH - tex.getWidth()) / 2,
                        GameField.HEIGHT / selectionRegionSizeK * (position +
                                (selectionRegionSizeK / 3)), (GameField.WIDTH - tex.getWidth()) / 2
                                + tex.getWidth(), GameField.HEIGHT / selectionRegionSizeK *
                                (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(), tex.getSY1(),
                        tex.getSX2(), tex.getSY2(), m);

                position = 1;

                tex = textureLoader.texList.get(ID_normal);
                m.g.drawImage(textureLoader.texMenu, (GameField.WIDTH - tex.getWidth()) / 2,
                        GameField.HEIGHT / selectionRegionSizeK * (position +
                                (selectionRegionSizeK / 3)), (GameField.WIDTH - tex.getWidth()) / 2
                                + tex.getWidth(), GameField.HEIGHT / selectionRegionSizeK *
                                (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(),
                        tex.getSY1(), tex.getSX2(), tex.getSY2(), m);

                position = 2;

                tex = textureLoader.texList.get(ID_hardcore);
                m.g.drawImage(textureLoader.texMenu, (GameField.WIDTH - tex.getWidth()) / 2,
                        GameField.HEIGHT / selectionRegionSizeK * (position +
                                (selectionRegionSizeK / 3)), (GameField.WIDTH - tex.getWidth()) / 2
                                + tex.getWidth(), GameField.HEIGHT / selectionRegionSizeK * (
                                        position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(),
                        tex.getSY1(), tex.getSX2(), tex.getSY2(), m);

                position = 3;

                tex = textureLoader.texList.get(ID_hacker);
                m.g.drawImage(textureLoader.texMenu, (GameField.WIDTH - tex.getWidth()) / 2,
                        GameField.HEIGHT / selectionRegionSizeK * (position +
                                (selectionRegionSizeK / 3)), (GameField.WIDTH - tex.getWidth()) / 2
                                + tex.getWidth(), GameField.HEIGHT / selectionRegionSizeK *
                                (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(),
                        tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
            }
        }
    }
    
    public void pauseEnter() {
        if(subState == 0) {
            if(selection == 0) {
                this.isPaused = !this.isPaused;
            }

            if(!multiplayer) {
                selectionsCount = 3;

                if(selection == 1) {
                    subState = 1;
                } else if(selection == 2) {
                    exitToMainMenu();
                }
            } else {
                selectionsCount = 1;

                if(selection == 1) {
                    exitToMainMenu();
                }
            }
            selection = 0;
        } else if(subState == 1) {
            if(selection == 0) {
                AI.setDifficulty("easy");
            } else if(selection == 1) {
                AI.setDifficulty("normal");
            } else if(selection == 2) {
                AI.setDifficulty("hardcore");
            } else if(selection == 3) {
                AI.setDifficulty("hacker");
            }
            selection = 0;
            subState = 0;
        }
    }

    private void exitToMainMenu() {
        player1.setStartPosition(0);
        player2.setStartPosition(1);
        setStartBallPosition((GameField.WIDTH - this.ball.getSize()) / 2,
                (GameField.HEIGHT - this.ball.getSize()) / 2);
        this.isPaused = false;
        ball.isLaunched = false;
        player1_streak = 0;
        player2_streak = 0;
        m.gameState = 0;
        menu.setMainMenu();
        menu.selectionUp();
    }
    
    public void useAI() {
        AI.updatePositionAI(this.player2, this.ball);
        AI.updateSpeed();
    }
}