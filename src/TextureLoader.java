import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A class that describes loading images from a texture
 * @author Dmitriy Stepanov
 */
public class TextureLoader {
    public List<Texture> texList = new ArrayList<>();
    public Image texMenu;

    /**
     * Constructor - creating a new texture loader
     * @see TextureLoader#TextureLoader()
     */
    public TextureLoader() {
        texMenu = Menu.loadImage("/textures.png");
    }
    
    public void loadTextureParams(String filePath) {
        InputStream texFile = this.getClass().getClassLoader().getResourceAsStream(filePath); 
        try {
            assert texFile != null;
            try(Scanner scanner = new Scanner(texFile)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if(line.equals("#")) {
                        break;
                    }
                    String[] tokens = line.split("\\s+");
                    Texture tex = new Texture(tokens[0]);
                    System.out.print("\n \n || " + tokens[0] +  " || \n \n");
                    tex.setSX1(Integer.parseInt(tokens[1]));
                    tex.setSY1(Integer.parseInt(tokens[2]));
                    tex.setSX2(Integer.parseInt(tokens[3]));
                    tex.setSY2(Integer.parseInt(tokens[4]));
                    System.out.println(tex);
                    texList.add(tex);
                }

                scanner.close();
                System.out.print("\n");

                for(Texture tex : texList) {
                    tex.setSize();
                    System.out.print("\n" + tex.getName() + ": " + tex.getSX1() + " " + tex.getSY1() + " " + tex.getSX2()
                            + " " + tex.getSY2() + " | " + tex.getWidth() + " " + tex.getHeight());
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("Error: failed to load " + filePath + "/n" + e);
        }
    }
    
    public byte getID(String textureName) {
        byte id = 0;

        for(byte i = 0; i < texList.size(); i++) {
            Texture tex = texList.get(i);

            if(tex.getName().equals(textureName)) {
                id = i;
                System.out.println("ID of '" + textureName + "' equals: " + id);
                break;
            }
        }
        return id;
    }
}
