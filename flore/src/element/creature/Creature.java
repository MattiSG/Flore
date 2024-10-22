package element.creature;

import game.Player;

import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.lang.Math;
import java.net.URI;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Dimension;

import org.w3c.dom.Node;

import element.XMLLoadableElement;
import xml.GlobalProperties;

public class Creature extends XMLLoadableElement implements Cloneable {
	/**@name	Variables d'unmarshalling*/
	//@{
	private final static double PARSER_VERSION = 0.61;
	private final static String[] ASSETS_NAMES = {"still", "left", "right", "up", "down"};
	private final static int	DEFAULT_X_SIZE = 100,
								DEFAULT_Y_SIZE = 100,
								DEFAULT_LIFETIME = 12;
	private final String ROOT = "creature";
	private String	BRINGS_EXPR = rootElement() + "/brings",
					DIMENSIONS_X_EXPR = rootElement() + "/dimension[@direction=\"x\"]",
					DIMENSIONS_Y_EXPR = rootElement() + "/dimension[@direction=\"y\"]",
					LIFETIME_EXPR = rootElement() + "/lifetime",
					SOUND_EXPR = rootElement() + "/sound",
					DEFAULT_SOUND = defaultFolder() + "/sound.wav";
	//@}
	
	/**@name	Variables membres*/
	//@{
    private BufferedImage img;
	private Map<String, Double> brings;
	private Dimension dimensions;
	private int lifetime;
	private File sound;
    private boolean dead = false,
                    outside = true,
                    inside = false;
    private long timeBorn = System.currentTimeMillis();
    private float mvtSize;
    private Player player = Player.getPlayer();
	//@}
	
	/**@name	Variables d'affichage*/
	//@{
	private int dir;    // type de déplacement (0 à 3 en courbe, 4 à 7 en ligne droite)
    private float mvtT; // progression sur le déplacement en cours (0 à 1)
    private Point pos;  // position
    private Random random = new Random();
	//@}

    public Creature(String ID) {
		load(ID);
        img = getAsset("still");
        mvtSize = stillImages().get(0).getWidth() * 3;
        init();
    }
	
	/**@name	Getters*/
	//@{
	public Map<String, Double> brings() {
		return brings;
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
	
	public File sound() {
		return sound;
	}
	
	public int lifetime() {
		return lifetime;
	}
	
	protected void parsePrivates() {
		brings = parseBrings();
		lifetime = parseLifetime();
		sound = parseSoundPath();
	}
	
	private Map<String, Double> parseBrings() {
		Map<String, Double> result = new HashMap<String, Double>();
		List<Node> bringsNodes = parser.getNodes(BRINGS_EXPR + "/*");
		for (Node node : bringsNodes)
			result.put(node.getTextContent(), new Double(node.getAttributes().getNamedItem("probability").getNodeValue()));
		return result;
	}
	
	private int parseLifetime() {
		int result = parser.getDouble(LIFETIME_EXPR).intValue();
		return (result <= 0 ? DEFAULT_LIFETIME : result);
	}
	
	private File parseSoundPath() {
		String[] paths = {parser.get(SOUND_EXPR), DEFAULT_SOUND};
		for (String path : paths) {
            if (path.length() == 0) continue;
			URI soundURI = parser.getDocumentURI();
			try {
				soundURI = soundURI.normalize().resolve(new URI(path));
				File sound = new File(soundURI);
				if (! sound.exists())
					throw new java.io.IOException();
				return sound;
			} catch(Exception e) {
				System.err.println("Unable to find sound file " + soundURI + " for creature \"" + ID() + "\".");
			}
		}
		throw new RuntimeException("Erreur au chargement d'un son : fichier introuvable.");
	}
	//@}
	
	
	/**@name	Animations*/
	//@{
    /*
     * Calcule le déplacement relatif à la base
     * Le paramètre t est le 'pourcentage' (de 0 à 1) du déplacement
     * dir est la direction (même règles que Creature::dir)
     */
    private Point calcMvt(float t, int dir)
    {
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
        else //if (dir >= 4 && dir < 8)
        {
            int x = 0,
                y = 0;

            // 4 =>  0  1
            // 5 =>  1  0
            // 6 => -1  0
            // 7 =>  0 -1
            if (dir == 4)
                y = 1;
            else if (dir == 5)
                x = 1;
            else if (dir == 6)
                x = -1;
            else if (dir == 8)
                y = -1;

            return new Point(x * (int)(mvtSize * t),
                    y * (int)(mvtSize * t));
        }
    }

    public void die()
    {
        dead = true;
    }

    public boolean isDead()
    {
        return dead && outside;
    }

    public boolean isOnScreen()
    {
        return inside;
    }

    public String toString() {
        return "" + pos.x + ", " + pos.y;
    }

    public void init() {
        // l'insecte doit être placé au hasard sur l'écran, mais aucun accès ici à la taille de l'écran
        // le couple (-1 -1) signifie alors pour la méthode paint qu'il est nécéssaire d'initialiser la position
        pos = new Point(-1, -1);

        dead = false;
        outside = true;
        inside = false;
        timeBorn = System.currentTimeMillis();
    }

    /**
     * Selectionne au hasard un type de déplacement
     */
    private void randomMvt(int width, int height)
    {
        // anti boucle infinie
        int m = 50;

        // on empêche la créature de sortir de l'écran en cherchant un mouvement correct
        boolean isOutside;
        do
        {
            isOutside = false;

            // selection d'un mouvement au hasard
            dir = random.nextInt(8);

            // calcul de la position après ce déplacement
            Point vd = calcMvt(1.337f, dir);
            Point newPos = new Point(pos.x + vd.x, pos.y + vd.y);

            // si cette nouvelle position sort de l'écran
            if (newPos.x < 0 || newPos.x > width)
                isOutside = true;
            if (newPos.y < 0 || newPos.y > height)
                isOutside = true;

            if(m-- == 0)
                break;

            // sinon on en cherche une autre
        } while (isOutside);

        mvtT = 0;
    }

    public Point calcDep(Graphics g, Rectangle rect, float dtime)
    {
        // place les creatures au hasard à leur création (mais est fait ici car l'on ne peut connaitre la taille de l'écran dans le construteur)
        if (pos.x == -1 || pos.y == -1)
        {
            // spawn sur côté gauche ou droit au hasard
            pos.x = random.nextInt(2) == 0 ? -(img.getWidth() - 1) : rect.width + img.getWidth() - 1;
            pos.y = random.nextInt(rect.height);
            if (pos.y < img.getHeight())
                pos.y = img.getHeight();
            else if(pos.y > rect.height - img.getHeight())
                pos.y = rect.height - img.getHeight();

            // selectionne un mouvement au hasard
            randomMvt(rect.width, rect.height);
        }

        // créature "vivante"
        if (dead == false)
        {
            long timeCurrent = System.currentTimeMillis();
            int age = (int)((timeCurrent - timeBorn) / 1000);

            // crève !§§
            if (lifetime < age)
                dead = true;

            // fin du déplacement ? on en prépare un nouveau
            // TODO /!\ appel récursif en boucle
            if (mvtT >= 1) {
                randomMvt(rect.width, rect.height);
            }

            Point oldMvt = calcMvt(mvtT, dir);

            // incrémentation du déplacement
            mvtT += dtime * (GlobalProperties.getDouble("Creature_Speed") / 10.0f);

            Point newMvt = calcMvt(mvtT, dir);
 
            return new Point(newMvt.x - oldMvt.x, newMvt.y - oldMvt.y);
        }
        // créature "morte"
        else
        {
            // quelle moitié de l'écran ?
            int d = (int)(mvtSize * dtime * (GlobalProperties.getDouble("Creature_Speed") / 10.0f));
            return new Point(pos.x < rect.width / 2 ? -d : d, 0);
        }
    }

    public void paint(Graphics g, float dtime) {
        Rectangle rect = g.getClipBounds();

        // calcule la nouvelle position
        Point dep    = calcDep(g, rect, dtime),
              newPos = new Point(pos.x + dep.x, pos.y + dep.y);

        // selection de l'image en fonction de la direction (gauche, haut, bas, droite, aucun déplacement)
        if (Math.abs(dep.x) > Math.abs(dep.y))      // deplacement horizontal
            img = dep.x > 0 ? rightImages().get(0) : leftImages().get(0);
        else if (Math.abs(dep.y) > Math.abs(dep.x)) // deplacement vertical
            img = dep.y > 0 ? downImages().get(0) : upImages().get(0);
        else                                       // aucun déplacement
            img = stillImages().get(0);

        // en dehors de l'écran ? (totalement invisible)
        if (pos.x + img.getWidth() < 0 ||
                pos.y + img.getHeight() < 0 ||
                pos.x - img.getWidth() > rect.width ||
                pos.y - img.getHeight() > rect.height)
            outside = true;
        else
            outside = false;

        boolean oldInside = inside;

        // à l'intérieur de l'écran ? (soit plus d'à moitié visible)
        if (pos.x > 0 &&
                pos.y > 0 &&
                pos.x < rect.width &&
                pos.y < rect.height)
            inside = true;
        else
            inside = false;

        if(oldInside == false && inside == true) {
            try {
                player.playWav(sound().getCanonicalPath());
            } catch(Exception e) {
                System.err.println("[erreur] Creature: getCanonicalPath()");
            }
        }

        // affichage (au centre de la position indiquée)
        g.drawImage(img, newPos.x - img.getWidth() / 2, newPos.y - img.getHeight() / 2, null);
        
        // sauvegarde de l'ancienne position
        pos = newPos;
    }
	//@}

    public Creature clone() throws CloneNotSupportedException {
        Creature c = (Creature) super.clone();
        // ne pas oublier le init() sinon risque de bugs
        // la méthode clone des créatures réalise des
        // copies bits-à-bits, donc ne recopie que les
        // pointeurs pour les types non-primitifs
        c.init();
        return c;
    }
}
