package game;

import element.plant.Plant;
import element.creature.Creature;
import element.mission.Mission;

import game.Player;

import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;

import java.util.Map;
import java.util.Map.Entry;
import java.util.List;
import java.util.ArrayList;

import java.awt.Transparency;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;

public class GameView extends JPanel {
    // image de l'herbe
    private BufferedImage    grass;
    // image d'un nuage
    private BufferedImage    cloud;
    // image d'un soleil
    private BufferedImage    sun;
    // image d'un trou
    private BufferedImage    hole;
    // image d'un trou sélectionné
    private BufferedImage    holeSelected;
    // image d'un trou plein
    private BufferedImage    holeFull;
    // image d'un trou plein sélectionné
    private BufferedImage    holeFullSelected;
    // image du ciel
    private BufferedImage    sky;
    // image représentant le fond (soleil + ciel + sol)
    //  calculée à partir de la taille de l'écran
    //  mise à jour à chaque redimensionnement
    private BufferedImage    background;
    // listes des trous présents
    private List<Point>      holes   = new ArrayList<Point>();
    // listes des créatures en jeu
    private List<Creature>   insects = new ArrayList<Creature>();
    // listes des plantes en jeu
    private List<Plant>      plantedPlants;
    // trou sélectionné
    private Point            selectedHole;
    // nombres de trous (d'après la mission)
    private int              holesNumber = 0;
    // mission courante
    private Mission          mission;
    // temps écoulé pour avoir un framerate fixe
    private long             timePrev = System.currentTimeMillis();
    // hack pour appeler les insectes toutes les MAX_COUNT_LOOP boucles
    private int              loopCount      = 0;
    private final static int MAX_COUNT_LOOP = 30;
    private Player      player = Player.getPlayer();

    public GameView(List<Plant> plantedPlants) {
        this.plantedPlants = plantedPlants;

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                if (e.getID() == ComponentEvent.COMPONENT_RESIZED) {
                    computePainting();
                }
            }
        });
    }

    public void computePainting() {
        computeHoles();
        computeBackgroundImage();
        repaint();
    }

    // calule la nouvelle image de fond après un redimensionnement
    private void computeBackgroundImage() {
        GraphicsEnvironment   ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice        gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();

        background = gc.createCompatibleImage(getWidth(), getHeight(), Transparency.TRANSLUCENT);
        Graphics2D g2d = background.createGraphics();

        // ciel
        int skyWidth  = sky.getWidth();
        int skyHeight = sky.getHeight();
        int nbW = (getWidth() / skyWidth) + 1;
        int nbH = (getHeight() / skyHeight) + 1;
        for (int x = 0; x < nbW; ++x)
            for (int y = 0; y < nbH; ++y)
                g2d.drawImage(sky, null, x * skyWidth, y * skyHeight);

        // soleil
        g2d.drawImage(sun, 0, 0, null);

        // herbe
        int grassWidth  = grass.getWidth();
        int grassHeight = grass.getHeight();
        nbW = (getWidth() / grassWidth) + 1;
        nbH = (getHeight() / grassHeight) + 1;
        for (int x = 0; x < nbW; ++x)
            for (int y = 0; y < nbH; ++y)
                g2d.drawImage(grass, null, x * grassWidth, (getHeight() * 2 / 3) + y * grassHeight);

        g2d.dispose();
    }

    // calculer le placement des trous et des plantes
    private void computeHoles() {
        int previousSelectedHole = holes.indexOf(selectedHole) == -1 ? 0 : holes.indexOf(selectedHole);

        holes.clear();

        int w = getSize().width;
        int h = getSize().height;
        int n = w / (holesNumber + 1);
        for (int i = 0; i < holesNumber; ++i) {
            int x = n*(i+1);
            int y = h-(h/10);

            holes.add(new Point(x,y));

            if (i < plantedPlants.size()) {
                Plant p = plantedPlants.get(i);
                if (p != null) {
                    p.setX(x);
                    p.setY(y);
                }
            }
        }

        selectedHole = holes.get(previousSelectedHole);
    }

    // met à jour les plantes
    public void updatePlantedPlants() {
        for (int i = 0; i < holesNumber; ++i) {
            Point pos = holes.get(i);
            Plant p = plantedPlants.get(i);
            if (p != null) {
                p.setX(pos.x);
                p.setY(pos.y);
            }
        }
    }

    // change de mission
    public void setMission(Mission m) {
        insects.clear();

        mission = m;

        grass = m.getAsset("grass");
        cloud = m.getAsset("cloud");
        sun   = m.getAsset("sun");
        sky   = m.getAsset("sky");

        hole              = m.getAsset("hole");
        holeSelected      = m.getAsset("hole_selected");
        holeFull          = m.getAsset("hole_full");
        holeFullSelected  = m.getAsset("hole_full_selected");

        holesNumber = m.holes();
        computeHoles();
        selectedHole = holes.get(0);

        repaint();
    }

    public int getSelectedHoleIndex() {
        return holes.indexOf(selectedHole);
    }

    public void selectNextHole() {
        int i = getSelectedHoleIndex();
        selectedHole = holes.get(holes.size() == i+1 ? 0 : i+1);
        repaint();
    }

    public void selectPreviousHole() {
        int i = getSelectedHoleIndex();
        selectedHole = holes.get(i == 0 ? holes.size()-1 : i-1);
        repaint();
    }

    public List<Creature> getCreaturesOnGame() {
        return insects;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Rectangle rect = g2d.getClipBounds();

        // calcule un différentiel de temps entre chaque image
        long timeNew = System.currentTimeMillis(),
             timeDiff = timeNew - timePrev;
        float dtime = (float)(timeDiff) / 1000.0f;

        // image de fond
        g2d.drawImage(background, 0, 0, null);

        // plantes
        for (int i = 0; i < plantedPlants.size(); ++i) {
            Point h = holes.get(i);
            BufferedImage holeImg;

            Plant p = plantedPlants.get(i);
            if (p != null) {
                if (h == selectedHole)
                    holeImg = holeFullSelected;
                else
                    holeImg = holeFull;

                p.paint(g);
                if (p.hasWater()) {
                    // dessiner le nuage
                    int cloudWidth  = g2d.getClipBounds().width / (holesNumber + 1);
                    int cloudHeight = cloud.getHeight() * cloudWidth / cloud.getWidth();
                    g.drawImage(cloud,
                                p.getX() - cloudWidth / 2,
                                g2d.getClipBounds().height / 6,
                                cloudWidth,
                                cloudHeight,
                                null);
                }
                
                if (++loopCount > MAX_COUNT_LOOP) {
                    // plantes appelant les insectes
                    if (p.isEnoughtAdult()) {
                        Map <String, Double> creatures = p.brings();
                        for (Map.Entry<String, Double> e : creatures.entrySet())
                            if (e.getValue() > Math.random())
                                insects.add(CreaturePool.getCreature(e.getKey()));
                    }

                    List<Creature> newInsects = new ArrayList<Creature>();
                    // insectes appelant les insectes
                    for (Creature c : insects) 
                        for (Map.Entry<String, Double> e : c.brings().entrySet())
                            if (e.getValue() > Math.random())
                                newInsects.add(CreaturePool.getCreature(e.getKey()));
                    insects.addAll(newInsects);

                    // remise à zéro du compteur de boucle
                    loopCount = 0;
                }
            } else {
                if (h == selectedHole)
                    holeImg = holeSelected;
                else
                    holeImg = hole;
            }

            int holeW = holeImg.getWidth();
            int holeH = holeImg.getHeight();

            int x = h.x - holeW / 2;
            int y = h.y - holeH / 2;
            g2d.drawImage(holeImg, x, y, null);
        }

        // creatures
        for (int i = 0; i < insects.size(); ) {
            Creature creature = insects.get(i);
            if (creature != null) {
                if (creature.isDead()) {
                    insects.remove(i);
                } else {
                    creature.paint(g, dtime);
                    ++i;
                }
            } else {
                insects.remove(i);
            }
        }

        timePrev = timeNew;
	}
}
