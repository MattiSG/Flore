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

import element.XMLLoadableElement;

public class Plant extends XMLLoadableElement {
	private final static String[] ASSETS_NAMES = {"seed", "shaft", "leaves", "flowers"};
	
    private BufferedImage image;
    private float health = 0;
    private float anim   = 0;
    private int   x      = 0,
                  y      = 0;
	
    public Plant(String ID) {
        this(ID, 512, 777);
    }
    
    public Plant(String ID, int xx, int yy) {
		load(ID);
        //image = seedImages().get(0);
        try {
            image = ImageIO.read(new File("../ressources/images/plant.png"));
        } catch(java.io.IOException e) {
            System.err.println("[erreur] " + e);
        }

        x = xx;
        y = yy;

        setHealth(100.0f);
    }

    public float get() { return anim; }

    public void setX(int xx) {
        x = xx;
    }

    public void setY(int yy) {
        y = yy;
    }

    public void setHealth(float h) {
        if(h >= 0 && h <= 100)
            health = h;
    }

	/**@name	Assets*/
	//@{
	public String[] getAssetsNames() {
		return ASSETS_NAMES;
	}
	
	public List<BufferedImage> seedImages() {
		return getAssets("seed");
	}
	
	public List<BufferedImage> shaftImages() {
		return getAssets("shaft");
	}
	
	public List<BufferedImage> leavesImages() {
		return getAssets("leaves");
	}
	
	public List<BufferedImage> flowersImages() {
		return getAssets("flowers");
	}
	//@}

    public void grow() {
        if (anim < health)
            anim += health > anim ? 1 : -1;
        if (anim > health)
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
