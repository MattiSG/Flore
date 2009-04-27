package element.creature;

import java.util.List;
import java.util.LinkedList;
import java.util.Random;
import java.lang.Math;
import java.net.URI;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;

import element.XMLLoadableElement;

public class Creature extends XMLLoadableElement {
    private int dir; // type de déplacement (0 à 3 en courbe, 4 à 7 en ligne droite)
    private float mvtT; 
    private Point pos, virtualPos; // position de référence et position sur le déplacement en cours
    private Random random = new Random();

	private final static double PARSER_VERSION = 0.41;
	private final static String DEFAULT_FOLDER = "../defaults/creature/";
	private final static String[] ASSETS_NAMES = {"still", "left", "right", "up", "down"};
	private final String ROOT = "creature";
	private String	EATS_EXPR = rootElement() + "/eats";
	
    private BufferedImage img;

	private List<String> eats;
	
    public Creature(String ID) {
		load(ID);

        // l'insecte doit être placé au hasard sur l'écran, mais aucun accès ici à la taille de l'écran
        // le couple (-1 -1) signifie alors pour la méthode paint qu'il est nécéssaire d'initialiser la position
        virtualPos = pos = new Point(-1, -1);
    }
	
	/**@name	Getters*/
	//@{
	public List<String> eats() {
		return eats;
	}

	public List<BufferedImage> stillImages() {
		return getAssets("still");
	}
	
	public List<BufferedImage> leftImages() {
		return getAssets("left");
	}
	
	public List<BufferedImage> rightImages() {
		return getAssets("right");
	}
	
	public List<BufferedImage> downImages() {
		return getAssets("down");
	}
	
	public List<BufferedImage> upImages() {
		return getAssets("up");
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
		eats = parseEats();
	}
	
	private List<String> parseEats() {
		return parser.getValues(EATS_EXPR);
	}

    /*
     * Calcule le déplacement relatif à la base
     * Le paramètre t est le 'pourcentage' (de 0 à 1) du déplacement
     * dir est la direction (même règles que Creature::dir)
     */
    private Point calcMvt(float t, int dir) {
        float mvtSize = 150;

        // virage
        if(dir < 4)
        {
            int x = 1,
                y = 1;

            // 0 =>  1  1
            // 1 =>  1 -1
            // 2 => -1 -1
            // 3 => -1  1
            if(dir == 2 || dir == 3)
                x = -1;
            if(dir == 1 || dir == 2)
                y = -1;

            return new Point(x * (int)(mvtSize * (1 - Math.cos(mvtT * (java.lang.Math.PI / 2)))),
                    y * (int)(mvtSize * Math.sin(mvtT * (java.lang.Math.PI / 2))));
        }
        // tout droit
        else //if(dir >= 4 && dir < 8)
        {
            int x = 0,
                y = 0;

            // 4 =>  0  1
            // 5 =>  1  0
            // 6 => -1  0
            // 7 =>  0 -1
            if(dir == 4)
                y = 1;
            else if(dir == 5)
                x = 1;
            else if(dir == 6)
                x = -1;
            else if(dir == 8)
                y = -1;

            return new Point(x * (int)(mvtSize * t),
                    y * (int)(mvtSize * t));
        }
    }

    private void randomMvt(int width, int height)
    {
        // on empêche la créature de sortir de l'écran
        boolean isOutside;
        do
        {
            isOutside = false;

            // selection d'un mouvement au hasard
            dir = random.nextInt(8);

            // calcul de la position après ce déplacement
            Point vd = calcMvt(1.337f, dir);
            Point newPos = new Point(pos.x + vd.x, pos.y + vd.y);

            // si cette nouvelle position sors de l'écran
            if(newPos.x < 0 || newPos.x > width)
                isOutside = true;
            if(newPos.y < 0 || newPos.y > height)
                isOutside = true;

            // on en cherche une autre
        } while (isOutside);

        mvtT = 0;
    }

    public void paint(Graphics g) {
        Rectangle rect = g.getClipBounds();

        // place les creatures au hasard sur l'écran
        if(pos.x == -1 || pos.y == -1)
        {
            pos.x = random.nextInt(rect.width);
            pos.y = random.nextInt(rect.height);

            randomMvt(rect.width, rect.height);
        }

        if(mvtT >= 1)
        {
            // on déplace pour de "vrai" la créature
            Point virtualDisplacement = calcMvt(1, dir);
            pos.x += virtualDisplacement.x;
            pos.y += virtualDisplacement.y;

            randomMvt(rect.width, rect.height);
        }

        // incrémentation du déplacement
        mvtT += 0.04;

        // calcul du déplacement en cours de la créature
        Point virtualDisplacement  = calcMvt(mvtT, dir),
              newVirtualPos        = new Point(pos.x + virtualDisplacement.x, pos.y + virtualDisplacement.y),
              relativeDisplacement = new Point(newVirtualPos.x - virtualPos.x, newVirtualPos.y - virtualPos.y);

        // selection de l'image en fonction de la direction (gauche, haut, bas, droite, aucun déplacement)
        if(Math.abs(relativeDisplacement.x) > Math.abs(relativeDisplacement.y)) // deplacement horizontal
            img = relativeDisplacement.x > 0 ? rightImages().get(0) : leftImages().get(0);
        else if(Math.abs(relativeDisplacement.y) > Math.abs(relativeDisplacement.x)) // deplacement vertical
            img = relativeDisplacement.y > 0 ? downImages().get(0) : upImages().get(0);
        else // aucun déplacement
            img = stillImages().get(0);

        // affichage
        g.drawImage(img, virtualPos.x, virtualPos.y, null);

        // sauvegarde de l'ancienne position
        virtualPos = newVirtualPos;
    }
	//@}
}
