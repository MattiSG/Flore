package element.plant;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Rectangle;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import javax.xml.xpath.*;
import javax.xml.parsers.*;
import org.w3c.dom.Document;

import element.Element;

public class Plant extends Element {
	
	public static final String[] ASSETS_NAMES = {"seed", "shaft", "leaves", "flowers"};
	
    BufferedImage image;
    float health = 0;
    float anim = 0;
    int x, y;

    public Plant(String file) {
        this(file, 512, 777);
    }
    
    public Plant(String ID, int xx, int yy) {
		assetsNames = ASSETS_NAMES;
		load(ID);
        try {
            image = ImageIO.read(new File("ressources/images/plant.png"));
        } catch(java.io.IOException e) {
            System.err.println("[erreur] " + e);
			e.printStackTrace();
        }
        
        x = xx;
        y = yy;
    }

	/**@name	Assets getters*/
	//@{
	public List<BufferedImage> seedImages() {
		return assets.get("seed");
	}
	
	public List<BufferedImage> shaftImages() {
		return assets.get("shaft");
	}
	
	public List<BufferedImage> leavesImages() {
		return assets.get("leaves");
	}
	
	public List<BufferedImage> flowersImages() {
		return assets.get("flowers");
	}
	//@}

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
        float ratio = (float)rect.width / 3 / image.getWidth();

        int imw = (int)(ratio * (float)image.getWidth()),
            imh = (int)(ratio * (float)image.getHeight());

        // how much pixels of the plant draw
        int h = (int)((float)imh * (anim / 100));

        // drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) 
        g.drawImage(image,
                    x - imw / 2,
                    y - h,
                    x + imw / 2,
                    y,

                    0,
                    0,
                    image.getWidth(),
                    (int)((float)image.getHeight() * (anim / 100)),

/*                    rect.width / 2 - imw,
                    rect.height - h,
                    imw,
                    imh,*/
                    null
                   );
    }
}
