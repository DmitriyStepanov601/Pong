import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * A class that describes the game menu
 * @author Dmitriy Stepanov
 */
public class Menu {
    public TextureLoader textureLoader;
    public GameField m;
    private final int WINDOW_WIDTH;
    private final int WINDOW_HEIGHT;
    private final int selectionRegionSizeK = 6;
    public  final int MAX_ANIMATION_ALPHA = 125;
    private Image icon;
    private Image logo;
    public byte ID_start;
    public byte ID_exit;
    public byte ID_player1;
    public byte ID_player2;
    public byte ID_singleplayer;
    public byte ID_multiplayer;
    public byte ID_controls;
    public byte ID_ws;
    public byte ID_arrows;
    public byte ID_information;
    public byte ID_back;
    public byte ID_pause;
    public byte ID_esc;
    public byte ID_esctopause;
    private boolean isMainMenuState = true;
    private byte subState = 0;                  //0 — information menu, 1 — choosing game type
    public byte selection = 0;
    public byte selectionMin = 0;
    public byte selectionsCount = 3;
    private int selectionTransparency = 0;
    private double animationMoment = 0.0d;
    private int selectorY;

    /**
     * Constructor - creating a new menu
     * @param gameField - playing field
     * @see Menu#Menu(GameField)
     */
    public Menu(GameField gameField) {
        this.m = gameField;
        this.textureLoader = gameField.textureLoader;

        textureLoader.loadTextureParams("tex_loc.data");
        WINDOW_WIDTH = GameField.WIDTH;
        WINDOW_HEIGHT = GameField.HEIGHT;
        ID_start = textureLoader.getID("start");
        ID_exit = textureLoader.getID("exit");
        ID_player1 = textureLoader.getID("player1");
        ID_player2 = textureLoader.getID("player2");
        ID_singleplayer = textureLoader.getID("singleplayer");
        ID_multiplayer = textureLoader.getID("multiplayer");
        ID_controls = textureLoader.getID("controls");
        ID_ws = textureLoader.getID("ws");
        ID_arrows = textureLoader.getID("arrows");
        ID_information = textureLoader.getID("information");
        ID_back = textureLoader.getID("back");
        ID_pause = textureLoader.getID("pause");
        ID_esc = textureLoader.getID("esc");
        ID_esctopause = textureLoader.getID("esctopause");
    }

    public static BufferedImage loadImage(String path){
        try {
            return ImageIO.read(Menu.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
       
    public void menuProcess() {
        selectionMin = 0;
        if(isMainMenuState) {
           selectionsCount = 3;
        }

        if(subState == 2) {
            selection = 3;
            selectionMin = 2;
        }

        if(this.selection < 0) selection = 0;
        if(this.selection > (byte)(selectionsCount - 1)) selection = (byte)(selectionsCount - 1);

        switch(selection) {
            case(0) :
                selectorY = WINDOW_HEIGHT / selectionRegionSizeK * (1 + (selectionRegionSizeK / 3)) - 6;
                break;
            case(1) :
                selectorY = WINDOW_HEIGHT / selectionRegionSizeK * (2 + (selectionRegionSizeK / 3)) - 6;
                break;
            case(2) :
                selectorY = WINDOW_HEIGHT / selectionRegionSizeK * (3 + (selectionRegionSizeK / 3)) - 6;
                break;
        }

        animationMoment += 0.088d;
        if(animationMoment > 310000000000d) animationMoment = 0;
        this.selectionTransparency = Math.abs((int)(Math.sin(animationMoment) * MAX_ANIMATION_ALPHA));
    }
   
    public void drawMenu() {
        icon = loadImage("/icon.png");
        m.g.drawImage(icon, 40, 40, 200, 200, null);
        logo = loadImage("/logo.png");
        m.g.drawImage(logo, 500, 100, 325, 123, null);
        Texture tex;
        int position;

        if(isMainMenuState) {
            position = 1;

            tex = textureLoader.texList.get(ID_start);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 2,
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)),
                    (WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(),
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) +
                            tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);

            position = 2;

            tex = textureLoader.texList.get(ID_information);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 2,
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)),
                    (WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(),
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) +
                            tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);

            position = 3;

            tex = textureLoader.texList.get(ID_exit);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 2,
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)),
                    (WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(),
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) +
                            tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
        }
        if(subState == 2) {
            position = 1;

            tex = textureLoader.texList.get(ID_controls);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 2,
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)),
                    (WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(),
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) +
                            tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);

            position = 2;

            tex = textureLoader.texList.get(ID_player1);
            int dy1 = WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) - 48;
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH) / 4,
                    dy1, (WINDOW_WIDTH) / 4 + tex.getWidth(),
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) +
                            tex.getHeight() - 48, tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);

            tex = textureLoader.texList.get(ID_player2);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH) / 4,
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)),
                    (WINDOW_WIDTH) / 4 + tex.getWidth(),
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) +
                            tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);

            tex = textureLoader.texList.get(ID_pause);
            int dy11 = WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + 48;
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH) / 4,
                    dy11, (WINDOW_WIDTH) / 4 + tex.getWidth(),
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) +
                            tex.getHeight() + 48, tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);

            tex = textureLoader.texList.get(ID_ws);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 3 * 2,
                    dy1,
                    (WINDOW_WIDTH - tex.getWidth()) / 3 * 2 + tex.getWidth(),
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) +
                            tex.getHeight() - 48, tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);

            tex = textureLoader.texList.get(ID_arrows);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 3 * 2,
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)),
                    (WINDOW_WIDTH - tex.getWidth()) / 3 * 2 + tex.getWidth(),
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) +
                            tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);

            tex = textureLoader.texList.get(ID_esc);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 3 * 2,
                    dy11,
                    (WINDOW_WIDTH - tex.getWidth()) / 3 * 2 + tex.getWidth(),
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) +
                            tex.getHeight() + 48, tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);

            tex = textureLoader.texList.get(ID_esctopause);
            m.g.drawImage(textureLoader.texMenu, 8, WINDOW_HEIGHT - tex.getHeight() * 2 - 16,
                    8 + tex.getWidth(), WINDOW_HEIGHT - tex.getHeight() - 16, tex.getSX1(), tex.getSY1(),
                    tex.getSX2(), tex.getSY2(), m);

            position = 3;

            tex = textureLoader.texList.get(ID_back);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 2,
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)),
                    (WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(),
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) +
                            tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
        }
        if(subState == 1) {
            position = 1;

            tex = textureLoader.texList.get(ID_singleplayer);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 2,
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)),
                    (WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(),
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) +
                            tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);

            position = 2;

            tex = textureLoader.texList.get(ID_multiplayer);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 2,
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)),
                    (WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(),
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) +
                            tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);

            position = 3;

            tex = textureLoader.texList.get(ID_back);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 2,
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)),
                    (WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(),
                    WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) +
                            tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
        }
        m.g.setColor(new Color(220, 0, 0, selectionTransparency));
        m.g.fillRect(0, selectorY, 1280, 44);
    }
    
    public void enter() {
        if(isMainMenuState) {
            if(selection == 0) {
                subState = 1;
                isMainMenuState = false;
            } else if(selection == 1) {
                subState = 2;
                isMainMenuState = false;
            } else if(selection == 2) {
                System.exit(0);
            }
        } else {
            if(subState == 2) {
                subState = 0;
                isMainMenuState = true;
                selection = 1;
            }

            if(subState == 1) {
                if(selection == 2) {
                    subState = 0;
                    isMainMenuState = true;
                    selection = 0;
                } else if(selection == 0) {
                    m.game.multiplayer = false;
                    m.gameState = 1;
                    m.game.startGame();
                } else if(selection == 1) {
                    m.game.multiplayer = true;
                    m.gameState = 1;
                    m.game.startGame();
                }
            }
        }
    }
    
    public void setMainMenu() {
        subState = 0;
        isMainMenuState = true;
    }
    
    public boolean isMainMenuState() {
        return this.isMainMenuState;
    }
    public void setMainMenuState(boolean isMainMenuState) {
        this.isMainMenuState = isMainMenuState;
    }
    public void selectionUp() {
        this.selection--;
    }
    public void selectionDown() {
        this.selection++;
    }
}