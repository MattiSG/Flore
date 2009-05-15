package element.plant;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Dimension;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.net.URI;

import element.XMLLoadableElement;
import element.creature.Creature;

public class Plant extends XMLLoadableElement implements Cloneable {
	/**@name	Variables d'unmarshalling*/
	//@{
	private final static double PARSER_VERSION = 0.4;
	private final static String DEFAULT_FOLDER = "../defaults/plant/";
	private final static String[] ASSETS_NAMES = {"seed", "shaft", "leaves", "flowers"};
	private final static String ROOT = "plant";
	private String BRINGS_EXPR = rootElement() + "/brings";
	//@}
	
	/**@name	Variables membres*/
	//@{
    private final static double	ADULT_PERCENT = 0.9;
    private float health = 0;
	private int	water = 0,
				neededSun,
				neededWater,
				neededTime;
	private Map<String, Double> brings;
	//@}
	
	/**@name	Variables d'affichage*/
	//@{
    private BufferedImage image;
    private float anim   = 0;
    private int		x     = 0,
					y     = 0;
	private final static double	SCREEN_RATIO = 1.0 / 2.5;
	//@}
	
    public Plant(String ID) {
        this(ID, 512, 777);
    }
    
    public Plant(String ID, int xx, int yy) {
		load(ID);
        image = getAssets("flowers").get(0);
		
        x = xx;
        y = yy;

        setHealth(100.0f);
    }

    public boolean hasWater() {
        return water > 0;
    }

    public void incrWater() {
        water += 10;
    }
    
    public void decrWater() {
        water -= neededWater();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

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

	/**@name	Getters*/
	//@{
	public int neededWater() {
		return neededWater;
	}
	
	public int neededSun() {
		return neededSun;
	}
	
	public int neededTime() {
		return neededTime;
	}
	
	public Map<String, Double> brings() {
		return brings;
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
	
	
	/**@name	Unmarshalling*/
	//@{
	public double parserVersion() {
		return PARSER_VERSION;
	}
	
	public String rootElement() {
		return ROOT;
	}
	
	public String[] getAssetsNames() {
		return ASSETS_NAMES;
	}
	
	public URI defaultFolder() {
		try {
			return new URI(DEFAULT_FOLDER);
		} catch (java.net.URISyntaxException e) {
			System.err.println("Default folder is unreachable.");
			throw new RuntimeException(e);
		}
	}
	
	protected void parsePrivates() {
		neededWater = parser.getDouble(rootElement() + "/water").intValue();
		neededSun = parser.getDouble(rootElement() + "/sun").intValue();
		neededTime = parser.getDouble(rootElement() + "/time").intValue();
		brings = parseBrings();
	}
	
	private Map<String, Double> parseBrings() {
		Map<String, Double> result = new HashMap<String, Double>();
		List<Node> bringsNodes = parser.getNodes(BRINGS_EXPR + "/*");
		for (Node node : bringsNodes)
			result.put(node.getTextContent(), new Double(node.getAttributes().getNamedItem("probability").getNodeValue()));
		return result;
	}
	//@}

    public void grow() {
    	if (water > 0) {
			anim += 10 / neededTime();
			decrWater();
		}
    }

    public boolean isAdult() {
        return anim == health;
    }

    public boolean isEnoughtAdult() {
        return (anim / (double)health) >= ADULT_PERCENT;
    }

    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;

        Rectangle rect = g.getClipBounds();

        // get the ratio for perfect plant size
		double	width = image.getWidth(),
			height = image.getHeight();
		double	xRatio = (double) rect.width * SCREEN_RATIO / width,
				yRatio = (double) rect.height * SCREEN_RATIO / height;
        double ratio = (xRatio > yRatio ? yRatio : xRatio);

        int imw = (int)(ratio * width),
            imh = (int)(ratio * height);

        // how much pixels of the plant draw
        int h = (int)((double)imh * (anim / 100));

        // drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) 
        g.drawImage(image,
                    x - imw / 2,
                    y - h,
                    x + imw / 2,
                    y,
                    0,
                    0,
                    image.getWidth(),
                    (int)(height * (anim / 100)),
                    null
                   );


        // quantité d'eau
        //   A SUPPRIMER DANS LA VERSION FINALE
        // {
        String msg = "Quantité d'eau : " + water;
        int ww = g.getFontMetrics().stringWidth(msg);
        int hh = g.getFontMetrics().getHeight();
        // fond
        g.setColor(Color.WHITE);
        g.fillRect(x - ww / 2, y - hh, ww, hh);
        // texte
        g.setColor(Color.BLUE);
        g.drawString(msg, x - ww / 2, y);
        // }
    }

    public Plant clone() throws CloneNotSupportedException {
        return (Plant) super.clone();
    }
}
