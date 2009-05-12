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

public class GameView extends JPanel {
    private BufferedImage    grass;
    private List<Point> holes = new ArrayList<Point>();
    private List<Creature> insects  = new ArrayList<Creature>();
    private Point            selectedHole;
    private int              holesNumber = 0;
    private List<Plant> plantedPlants;
    private Mission          mission;
	private int              nbInsects = 0;
    private BufferedImage    cloud;

    public GameView(List<Plant> plantedPlants, List<Creature> insects) {
        this.plantedPlants = plantedPlants;
        this.insects = insects;

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                computeHoles();
            }
        });
    }

    public void setMission(Mission m) {
        mission = m;
        grass = m.getAssets("grass").get(0);
        cloud = m.getAssets("cloud").get(0);
        holesNumber = m.holes();
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

    private void computeHoles() {
        holes.clear();

        int w = getSize().width;
        int h = getSize().height;
        int n = w / (holesNumber + 1);
        for (int i = 0; i < holesNumber; ++i)
            holes.add(new Point(n*(i+1), h-(h/10)));

        selectedHole = holes.get(0);
    }

    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Rectangle rect = g2d.getClipBounds();

        // ciel
        g2d.setColor(new Color(118, 142, 176));
        g2d.fillRect(0, 0, rect.width, rect.height * 2 / 3);

        // soleil
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(20, 20, 75, 75);

        // herbe
        int w = grass.getWidth();
        int h = grass.getHeight();
        int nbW = (rect.width / w) + 1;
        int nbH = (rect.height / h) + 1;
        for (int x = 0; x < nbW; ++x)
            for (int y = 0; y < nbH; ++y)
                g2d.drawImage(grass, null, x * w, (rect.height * 2 / 3) + y * h);

        // trous
        g2d.setColor(Color.BLACK);
        for (Point p : holes)
        {
            if (p == selectedHole) {
                g2d.setColor(Color.RED);
                g2d.fillOval(p.x-50, p.y-50, 100, 100);
                g2d.setColor(Color.BLACK);
            }
            g2d.fillOval(p.x-25, p.y-25, 50, 50);
        }

        // plantes
        for (int i = 0; i < plantedPlants.size(); ++i)
        {
            Plant p = plantedPlants.get(i);
            if (p != null)
            {
                Point pos = holes.get(i);
                p.setX(pos.x);
                p.setY(pos.y+50);
                p.paint(g);

                if (p.hasWater()) {
                    // dessiner le nuage
                    g.drawImage(cloud, p.getX() - cloud.getWidth() / 2, g2d.getClipBounds().height / 3, null);
                }
                
                /*
                // insectes
                if (p.isEnoughtAdult() && Math.random() > 0.10) {
                    Map <String, Double> creatures = p.brings();

                    System.out.println("=====>");
                    for (Map.Entry<String, Double> e : creatures.entrySet())
                        System.out.println(e.getKey() + " : " + e.getValue());

                	Creature c = p.getCreature();
                	//Creature c = new Creature("coccinelle");
                	c.paint(g2d);
                }
                */
            }
        }

        // creatures
        for(Creature creature : insects)
        {
            creature.paint(g2d);
        }
	}
}
