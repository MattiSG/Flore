package element.plant;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Plant {
    BufferedImage image;
    float health = 0;
    float anim = 0;

    public Plant() {
        try {
            image = ImageIO.read(new File("../ressources/images/plant.png"));
        } catch(java.io.IOException e) {
            System.err.println("[erreur] " + e);
        }
    }

    public void set(float h) {
        if(h >= 0 && h <= 100)
            health = h;
    }

    public void grow() {
        if(anim < health)
            anim += health > anim ? 1 : -1;
        if(anim > health)
            anim = health;
    }

    public void paint(Graphics g) {
        Rectangle rect = g.getClipBounds();

        // get the ratio for perfect plant size (1 / 3 of screen)
        float ratio = rect.width / 3 / image.getWidth();

        int imw = (int)(ratio * (float)image.getWidth()),
            imh = (int)(ratio * (float)image.getHeight());

        // how much pixels of the plant draw
        int h = (int)((float)imh * (anim / 100));

        g.drawImage(image,
                    rect.width / 2 - imw,
                    rect.height - h,
                    imw,
                    imh,
                    null
                   );
    }
}
