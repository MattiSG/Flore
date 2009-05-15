package game;

import element.plant.Plant;
import element.creature.Creature;
import element.mission.Mission;

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
    private BufferedImage    grass;
    private List<Point>      holes   = new ArrayList<Point>();
    private List<Creature>   insects = new ArrayList<Creature>();
    private Point            selectedHole;
    private int              holesNumber = 0;
    private List<Plant>      plantedPlants;
    private Mission          mission;
    private BufferedImage    cloud;
    private int grassWidth, grassHeight;
    private long             timePrev = System.currentTimeMillis();

    private BufferedImage background;

    private int gg = 0;

    public GameView(List<Plant> plantedPlants) {
        this.plantedPlants = plantedPlants;

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                if (e.getID() == ComponentEvent.COMPONENT_RESIZED) {
                    computeHoles();
                    computeBackgroundImage();
                    repaint();
                }
            }
        });
    }

    private void computeBackgroundImage() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();

        background = gc.createCompatibleImage(getWidth(), getHeight(), Transparency.TRANSLUCENT);
        //background = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = background.createGraphics();

        // ciel
        g2d.setColor(new Color(118, 142, 176));
        g2d.fillRect(0, 0, getWidth(), getHeight() * 2 / 3);

        // soleil
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(20, 20, 75, 75);

        // herbe
        int nbW = (getWidth() / grassWidth) + 1;
        int nbH = (getHeight() / grassHeight) + 1;
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

    public void updatePlantedPlants() {
        computeHoles();
    }

    public void setMission(Mission m) {
        insects.clear();

        mission = m;

        grass = m.getAssets("grass").get(0);
        // taille de l'image
        grassWidth  = grass.getWidth();
        grassHeight = grass.getHeight();

        cloud = m.getAssets("cloud").get(0);

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

        // trous
        g2d.setColor(Color.BLACK);
        for (Point p : holes) {
            if (p == selectedHole) {
                g2d.setColor(Color.RED);
                g2d.fillOval(p.x-50, p.y-50, 100, 100);
                g2d.setColor(Color.BLACK);
            }
            g2d.fillOval(p.x-25, p.y-25, 50, 50);
        }

        // plantes
        for (Plant p : plantedPlants) {
            if (p != null) {
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
                
                // insectes
                if (p.isEnoughtAdult() && ++gg > 30) {
                    Map <String, Double> creatures = p.brings();
                    for (Map.Entry<String, Double> e : creatures.entrySet())
                        if (e.getValue() > Math.random()) {
                            Creature c = CreaturePool.getCreature(e.getKey());
                            insects.add(c);
                            //System.out.println("insecte créé : " + c.ID() + " => " + c.toString());
                        }
                    gg = 0;
                }
            }
        }

        // creatures
        for (int i = 0; i < insects.size(); ) {
            Creature creature = insects.get(i);
            if (creature != null) {
                if (creature.isDead()) {
                    //System.out.println("suppression : " + creature.ID());
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
