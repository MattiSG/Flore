package element.plant;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

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
	private final static double PARSER_VERSION = 0.4;
	private final static String DEFAULT_FOLDER = "../defaults/plant/";
	private final static String[] ASSETS_NAMES = {"seed", "shaft", "leaves", "flowers"};
	private final static String ROOT = "plant";

	private String BRINGS_EXPR = rootElement() + "/brings";

    private BufferedImage nuage;
    private BufferedImage image;
    private float health = 0;
    private float anim   = 0;
    private int		x     = 0,
					y     = 0,
                    water = 0,
					neededSun,
					neededWater,
					neededTime;
	private Map<String, Double> brings;

    public Plant(String ID) {
        this(ID, 512, 777);
    }
    
    public Plant(String ID, int xx, int yy) {
		load(ID);
        image = getAssets("flowers").get(0);
        try {
			nuage = ImageIO.read(new File("../ressources/elements/defaults/plant/cloud.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        x = xx;
        y = yy;

        setHealth(100.0f);
    }

    public void incrWater() {
        ++water;
    }
    
    public void decrWater() {
        --water;
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
	        if (anim < health) {
	            anim += health > anim ? 1 : -1;
	            --water;
	        }
	        if (anim > health) {
	            anim = health;
	            --water;
	        }
    	}
    }

    public boolean isAdult() {
        return anim == health;
    }

    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;

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


        // quantité d'eau
        String msg = "Quantité d'eau : " + water;
        int ww = g.getFontMetrics().stringWidth(msg);
        int hh = g.getFontMetrics().getHeight();
        // fond
        g.setColor(Color.WHITE);
        g.fillRect(x - ww / 2, y - hh, ww, hh);
        // texte
        g.setColor(Color.BLUE);
        g.drawString(msg, x - ww / 2, y);
        
        if (water > 0) {
        	int ch = nuage.getHeight() * imw / nuage.getWidth();
        	g.drawImage(nuage, x - imw / 2, y - imh * 2, imw, ch, null);
        }

        if (anim == 100) {
//            volant.draw(g);
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
